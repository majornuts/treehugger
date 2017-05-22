package dk.hug.treehugger.core;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dk.hug.treehugger.model.Feature;
import dk.hug.treehugger.model.Root;

/**
 * Created by  Mads Fisker on 2016 - 09/03/16  21:44.
 */
public class DBhandler {

    private final static String trees = "trees";
    private final static String treeState = "treeState";
    private static final String PREF_NAME = "PREF_NAME";


    public static void storeTreeState(Context context, int RootState) {
        SharedPreferences settings = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = settings.edit();
        edit.putInt(treeState, RootState).apply();
    }

    public static int getTreeState(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        int s = settings.getInt(treeState, 0);
        return s;
    }


    public static void storeTrees(Context context, Root root) {
        storeTreeState(context,1);
        SharedPreferences settings = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = settings.edit();
        edit.putString(trees, serialize(root)).apply();
    }

    public static Root getTrees(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String s = settings.getString(trees, null);
        ObjectMapper mapper = new ObjectMapper();
        Root root = null;
        try {
            root = mapper.readValue(s, Root.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return root;
    }

    public static void storeTreeList(Context context, Root root) {
        TreeDBHelper dbHelper = new TreeDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.beginTransaction();

        db.execSQL(TreeDBHelper.SQL_DELETE_ENTRIES);
        db.execSQL(TreeDBHelper.SQL_CREATE_ENTRIES);

        for(Feature feature :root.getFeatures()) {
            ContentValues values = new ContentValues();
            values.put(TreeDBContract.TreeEntry.COLUMN_NAME_TRAE_ART, feature.getProperties().getTraeArt());
            values.put(TreeDBContract.TreeEntry.COLUMN_NAME_DANSK_NAVN, feature.getProperties().getDanskNavn());
            values.put(TreeDBContract.TreeEntry.COLUMN_NAME_COORDINATE_LAT, feature.getGeometry().getCoordinates().get(1));
            values.put(TreeDBContract.TreeEntry.COLUMN_NAME_COORDINATE_LON, feature.getGeometry().getCoordinates().get(0));
            long newRowId = db.insert(TreeDBContract.TreeEntry.TABLE_NAME, null, values);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public static List<Tree> getTreeList(Context context) {
        TreeDBHelper dbHelper = new TreeDBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = new String[] {TreeDBContract.TreeEntry.COLUMN_NAME_TRAE_ART,
            TreeDBContract.TreeEntry.COLUMN_NAME_DANSK_NAVN,
            TreeDBContract.TreeEntry.COLUMN_NAME_COORDINATE_LAT,
            TreeDBContract.TreeEntry.COLUMN_NAME_COORDINATE_LON};
        Cursor cursor = db.query(TreeDBContract.TreeEntry.TABLE_NAME, projection, null, null, null, null, null);
        List<Tree> trees = new ArrayList<>(cursor.getCount());
        while(cursor.moveToNext()) {
            Tree tree = new Tree(cursor.getString(cursor.getColumnIndex(TreeDBContract.TreeEntry.COLUMN_NAME_TRAE_ART)),
                    cursor.getString(cursor.getColumnIndex(TreeDBContract.TreeEntry.COLUMN_NAME_DANSK_NAVN)),
                    cursor.getDouble(cursor.getColumnIndex(TreeDBContract.TreeEntry.COLUMN_NAME_COORDINATE_LAT)),
                    cursor.getDouble(cursor.getColumnIndex(TreeDBContract.TreeEntry.COLUMN_NAME_COORDINATE_LON)));
            trees.add(tree);
        }
        cursor.close();
        db.close();
        return trees;
    }

    @Nullable
    public static String serialize(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
