package dk.hug.treehugger;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import dk.hug.treehugger.core.DBhandler;
import dk.hug.treehugger.model.Root;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;


/**
 * Created by  Mads Fisker on 2016 - 08/03/16  13:08.
 */
public class TreeDownload extends AsyncTask<Void, String, Void> {
    private static final String TAG = "TreeDownload";
    private static final boolean DEBUG = false;
    private final Context context;
    private final Handler.Callback sa;
    private TreeDownloadCallback callback;

    private long time;
    private long avgDownloadTime;
    private long avgBytesRead;
    private boolean isDone = false;

    public TreeDownload(Context context, Handler.Callback startActivity, TreeDownloadCallback callback) {
        this.context = context;
        this.sa = startActivity;
        this.callback = callback;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Log.e(TAG, "doInBackground: start ");
        String url = "http://wfs-kbhkort.kk.dk/k101/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=k101:gadetraer&outputFormat=json&SRSNAME=EPSG:4326";
        InputStream is = null;
        Root root = null;
        publishProgress(context.getString(R.string.downloading_trees));

        Request request = new Request.Builder()
                .url(url)
                .build();

        final ProgressListener progressListener = new ProgressListener() {
            @Override public void update(long bytesRead, long contentLength, boolean done) {
                long currentDownloadTime = System.currentTimeMillis();
                if((currentDownloadTime-avgDownloadTime)>100) {
                    if(DEBUG) Log.i(TAG, "bytesRead: " + (bytesRead - avgBytesRead) + " in "+(currentDownloadTime-avgDownloadTime));
                    long speed = (bytesRead - avgBytesRead)/(currentDownloadTime-avgDownloadTime);
                    publishProgress(context.getString(R.string.downloading_trees_progress, speed));
                    avgDownloadTime = System.currentTimeMillis();
                    avgBytesRead = bytesRead;
                }
            }
        };

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .addNetworkInterceptor(new Interceptor() {
                    @Override public Response intercept(Chain chain) throws IOException {
                        Response originalResponse = chain.proceed(chain.request());
                        return originalResponse.newBuilder()
                                .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                                .build();
                    }
                })
                .build();

        try {
            avgDownloadTime = System.currentTimeMillis();
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            //is = new URL(url).openStream();

            String responseBody = response.body().string();
            long downloadTime = System.currentTimeMillis();
            Log.e(TAG, "doInBackground:download time:" + (downloadTime - time));
            publishProgress(context.getString(R.string.saving_trees));

            ObjectMapper mapper = new ObjectMapper();
            root = mapper.readValue(responseBody, Root.class);

            long parseTime = System.currentTimeMillis();
            Log.e(TAG, "doInBackground:parse time:" + (parseTime - downloadTime));

            DBhandler.storeTreeList(context, root);
            Log.e(TAG, "doInBackground:save time:" + (System.currentTimeMillis() - parseTime));
            isDone = true;
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        callback.updateDownloadProgress(values[0]);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        time = System.currentTimeMillis();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        DBhandler.storeTreeState(context, 1);

        callback.updateDownloadComplete(isDone);

        Bundle b = new Bundle();
        b.putBoolean("isDone", isDone);
        Message m = new Message();
        m.setData(b);
        sa.handleMessage(m);
    }

    public TreeDownloadCallback getCallback() {
        return callback;
    }

    public void setCallback(TreeDownloadCallback callback) {
        this.callback = callback;
    }

    private static class ProgressResponseBody extends ResponseBody {

        private final ResponseBody responseBody;
        private final ProgressListener progressListener;
        private BufferedSource bufferedSource;

        ProgressResponseBody(ResponseBody responseBody, ProgressListener progressListener) {
            this.responseBody = responseBody;
            this.progressListener = progressListener;
        }

        @Override public MediaType contentType() {
            return responseBody.contentType();
        }

        @Override public long contentLength() {
            return responseBody.contentLength();
        }

        @Override public BufferedSource source() {
            if (bufferedSource == null) {
                bufferedSource = Okio.buffer(source(responseBody.source()));
            }
            return bufferedSource;
        }

        private Source source(Source source) {
            return new ForwardingSource(source) {
                long totalBytesRead = 0L;

                @Override public long read(Buffer sink, long byteCount) throws IOException {
                    long bytesRead = super.read(sink, byteCount);
                    // read() returns the number of bytes read, or -1 if this source is exhausted.
                    totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                    progressListener.update(totalBytesRead, responseBody.contentLength(), bytesRead == -1);
                    return bytesRead;
                }
            };
        }
    }

    interface ProgressListener {
        void update(long bytesRead, long contentLength, boolean done);
    }
}


