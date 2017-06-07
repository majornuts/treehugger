package dk.siit.treehugger.core;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TreeDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "TreeReader.db";

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TreeDBContract.TreeEntry.TABLE_NAME + " (" +
                    TreeDBContract.TreeEntry._ID + " INTEGER PRIMARY KEY," +
                    TreeDBContract.TreeEntry.COLUMN_NAME_TRAE_ART + " TEXT," +
                    TreeDBContract.TreeEntry.COLUMN_NAME_DANSK_NAVN + " TEXT, " +
                    TreeDBContract.TreeEntry.COLUMN_NAME_COORDINATE_LAT + " REAL," +
                    TreeDBContract.TreeEntry.COLUMN_NAME_COORDINATE_LON + " REAL)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TreeDBContract.TreeEntry.TABLE_NAME;

    public TreeDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
