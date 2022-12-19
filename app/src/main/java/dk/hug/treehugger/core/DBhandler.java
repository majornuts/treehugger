package dk.hug.treehugger.core;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLngBounds;

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
    private static SQLiteDatabase dbstatic;


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

    public static void storeTreeList(Context context, Root root) {
        TreeDBHelper dbHelper = new TreeDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.beginTransaction();

        db.execSQL(TreeDBHelper.SQL_DELETE_ENTRIES);
        db.execSQL(TreeDBHelper.SQL_CREATE_ENTRIES);

        for (Feature feature : root.getFeatures()) {
            ContentValues values = new ContentValues();
            values.put(TreeDBContract.TreeEntry.COLUMN_NAME_TRAE_ART, feature.getProperties().getTraeArt());
            values.put(TreeDBContract.TreeEntry.COLUMN_NAME_DANSK_NAVN, feature.getProperties().getDanskNavn());
            values.put(TreeDBContract.TreeEntry.COLUMN_NAME_COORDINATE_LAT, feature.getGeometry().getCoordinates().get(1));
            values.put(TreeDBContract.TreeEntry.COLUMN_NAME_COORDINATE_LON, feature.getGeometry().getCoordinates().get(0));
            db.insert(TreeDBContract.TreeEntry.TABLE_NAME, null, values);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public static List<Tree> getTreeList(Context context) {

        TreeDBHelper dbHelper = new TreeDBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = new String[]{TreeDBContract.TreeEntry.COLUMN_NAME_TRAE_ART,
                TreeDBContract.TreeEntry.COLUMN_NAME_DANSK_NAVN,
                TreeDBContract.TreeEntry.COLUMN_NAME_COORDINATE_LAT,
                TreeDBContract.TreeEntry.COLUMN_NAME_COORDINATE_LON};
        Cursor cursor = db.query(TreeDBContract.TreeEntry.TABLE_NAME, projection, null, null, null, null, null);
        List<Tree> trees = new ArrayList<>(cursor.getCount());
        int speciesIndex = cursor.getColumnIndex(TreeDBContract.TreeEntry.COLUMN_NAME_TRAE_ART);
        int nameIndex = cursor.getColumnIndex(TreeDBContract.TreeEntry.COLUMN_NAME_DANSK_NAVN);
        int latitudeIndex = cursor.getColumnIndex(TreeDBContract.TreeEntry.COLUMN_NAME_COORDINATE_LAT);
        int longitudeIndex = cursor.getColumnIndex(TreeDBContract.TreeEntry.COLUMN_NAME_COORDINATE_LON);
        while (cursor.moveToNext()) {
            Tree tree = new Tree(cursor.getString(speciesIndex),
                    cursor.getString(nameIndex),
                    cursor.getDouble(latitudeIndex),
                    cursor.getDouble(longitudeIndex));
            trees.add(tree);
        }
        cursor.close();
        db.close();

        return trees;
    }

    public static void closeDB() {
        if(dbstatic!=null)
            dbstatic.close();
        dbstatic = null;
    }

    public static List<Tree> getRegionTreeList(Context context, Projection projectionRigion) {

        if (dbstatic == null) {
            TreeDBHelper dbHelper = new TreeDBHelper(context);
            dbstatic = dbHelper.getReadableDatabase();
        }

        String[] projection = new String[]{TreeDBContract.TreeEntry.COLUMN_NAME_TRAE_ART,
                TreeDBContract.TreeEntry.COLUMN_NAME_DANSK_NAVN,
                TreeDBContract.TreeEntry.COLUMN_NAME_COORDINATE_LAT,
                TreeDBContract.TreeEntry.COLUMN_NAME_COORDINATE_LON};

        LatLngBounds bounds = projectionRigion.getVisibleRegion().latLngBounds;
        String selection = TreeDBContract.TreeEntry.COLUMN_NAME_COORDINATE_LAT +
                " Between " + bounds.southwest.latitude +
                " And " + bounds.northeast.latitude +
                " And " + TreeDBContract.TreeEntry.COLUMN_NAME_COORDINATE_LON +
                " Between " + bounds.southwest.longitude +
                " And " + bounds.northeast.longitude +
                " LIMIT 2000";

        Cursor cursor = dbstatic.query(TreeDBContract.TreeEntry.TABLE_NAME, projection, selection, null, null, null, null);
        List<Tree> trees = new ArrayList<>(cursor.getCount());
        int speciesIndex = cursor.getColumnIndex(TreeDBContract.TreeEntry.COLUMN_NAME_TRAE_ART);
        int nameIndex = cursor.getColumnIndex(TreeDBContract.TreeEntry.COLUMN_NAME_DANSK_NAVN);
        int latitudeIndex = cursor.getColumnIndex(TreeDBContract.TreeEntry.COLUMN_NAME_COORDINATE_LAT);
        int longitudeIndex = cursor.getColumnIndex(TreeDBContract.TreeEntry.COLUMN_NAME_COORDINATE_LON);
        while (cursor.moveToNext()) {
            Tree tree = new Tree(cursor.getString(speciesIndex),
                    cursor.getString(nameIndex),
                    cursor.getDouble(latitudeIndex),
                    cursor.getDouble(longitudeIndex));
            trees.add(tree);
        }
        cursor.close();

        return trees;
    }

}
