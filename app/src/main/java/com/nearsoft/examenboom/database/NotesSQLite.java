package com.nearsoft.examenboom.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jsalcido on 7/27/14.
 */
public class NotesSQLite extends SQLiteOpenHelper {
    public final static String DB_NAME = "note_db";
    public final static String TABLE_NOTES = "note";
    public final static String COLUMN_NOTE_ID = "note_id";
    public final static String COLUMN_NOTE_TITLE = "note_title";
    public final static String COLUMN_NOTE_TEXT = "note_text";
    public final static String COLUMN_NOTE_LATITUDE = "note_lat";
    public final static String COLUMN_NOTE_LONG = "note_long";

    private final String SQL_DELETE = "DELETE TABLE " + TABLE_NOTES;
    private final String SQL_CREATE = "CREATE TABLE " + TABLE_NOTES + "(" +
            COLUMN_NOTE_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_NOTE_TITLE + " TEXT, " +
            COLUMN_NOTE_TEXT + " TEXT, " +
            COLUMN_NOTE_LATITUDE + " REAL," +
            COLUMN_NOTE_LONG + " REAL)";

    public NotesSQLite(Context context, String db_name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, db_name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE);
        db.execSQL(SQL_CREATE);
    }
}
