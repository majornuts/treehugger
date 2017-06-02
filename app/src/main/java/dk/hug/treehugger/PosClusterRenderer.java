package dk.hug.treehugger;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import dk.hug.treehugger.model.Pos;

/**
 * Created by mfi on 02/06/2017.
 */

class PosClusterRenderer extends DefaultClusterRenderer<Pos> {
    private final Context context;

    public PosClusterRenderer(Context context, GoogleMap map, ClusterManager<Pos> clusterManager) {
        super(context, map, clusterManager);
        this.context = context;
    }


    @Override
    protected void onBeforeClusterItemRendered(Pos item, MarkerOptions markerOptions) {
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.tree);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(getResizedBitmap(icon,80,80))).title(item.getName()).snippet(item.getSnippet());
    }


    private  Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }
}
