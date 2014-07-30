package com.nearsoft.examenboom.database.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.nearsoft.examenboom.common.Note;
import com.nearsoft.examenboom.database.NoteDatabaseAccess;
import com.nearsoft.examenboom.database.NotesSQLite;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jsalcido on 7/27/14.
 */
public class NoteRepositoryImpl implements NoteRepository {

    private static String[] sColumns = new String[]{
            NotesSQLite.COLUMN_NOTE_ID,
            NotesSQLite.COLUMN_NOTE_TITLE,
            NotesSQLite.COLUMN_NOTE_TEXT,
            NotesSQLite.COLUMN_NOTE_LATITUDE,
            NotesSQLite.COLUMN_NOTE_LONG
    };

    private Context mContext;

    public NoteRepositoryImpl(Context context) {
        mContext = context;
    }

    private SQLiteDatabase obtainDatabase() {
        return NoteDatabaseAccess.getDatabaseConnection(mContext);
    }

    private ContentValues getContentValues(Note note) {
        ContentValues noteValues = new ContentValues();

        // Lets make use of the AUTOINCREMENT SHIT from SQLite.
        if (note.getId() > 0) {
            noteValues.put(NotesSQLite.COLUMN_NOTE_ID, note.getId());
        }
        noteValues.put(NotesSQLite.COLUMN_NOTE_TITLE, note.getTitle());
        noteValues.put(NotesSQLite.COLUMN_NOTE_TEXT, note.getText());
        noteValues.put(NotesSQLite.COLUMN_NOTE_LATITUDE, note.getLatitude());
        noteValues.put(NotesSQLite.COLUMN_NOTE_LONG, note.getLongitude());
        return noteValues;
    }

    @Override
    public void saveNote(Note note) {
        SQLiteDatabase db = obtainDatabase();
        if (db.isOpen()) {

            try {
                db.insertOrThrow(NotesSQLite.TABLE_NOTES, null, getContentValues(note));
            } catch (SQLException e) {
                Log.e("NOTES", e.getMessage());
                e.printStackTrace();
            }
        }
        db.close();
    }

    @Override
    public void deleteNote(Note note) {
        SQLiteDatabase db = obtainDatabase();
        if (db.isOpen()) {
            db.delete(NotesSQLite.TABLE_NOTES, whereClauseById(), new String[]{String.valueOf(note.getId())});
        }
        db.close();
    }

    @Override
    public void updateNote(Note note) {
        SQLiteDatabase db = obtainDatabase();
        if (db.isOpen()) {
            db.update(NotesSQLite.TABLE_NOTES, getContentValues(note), whereClauseById(), new String[]{String.valueOf(note.getId())});
        }
        db.close();
    }

    @Override
    public List<Note> allNotes() {
        List<Note> list = new ArrayList<Note>();
        SQLiteDatabase db = obtainDatabase();
        if (db.isOpen()) {
            Cursor cursor = db.query(NotesSQLite.TABLE_NOTES, sColumns, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    list.add(getNoteFromCursor(cursor));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        return list;
    }

    @Override
    public Note getNoteById(int id) {
        Note note = new Note();
        SQLiteDatabase db = obtainDatabase();
        if (db.isOpen()) {
            Cursor cursor = db.query(NotesSQLite.TABLE_NOTES, sColumns, whereClauseById(), new String[] {String.valueOf(id)}, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    note = getNoteFromCursor(cursor);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        return note;
    }

    private Note getNoteFromCursor(Cursor cursor) {
        Note note = new Note();
        note.setId(cursor.getInt(cursor.getColumnIndex(NotesSQLite.COLUMN_NOTE_ID)));
        note.setTitle(cursor.getString(cursor.getColumnIndex(NotesSQLite.COLUMN_NOTE_TITLE)));
        note.setText(cursor.getString(cursor.getColumnIndex(NotesSQLite.COLUMN_NOTE_TEXT)));
        note.setLatitude(cursor.getDouble(cursor.getColumnIndex(NotesSQLite.COLUMN_NOTE_LATITUDE)));
        note.setLongitude(cursor.getDouble(cursor.getColumnIndex(NotesSQLite.COLUMN_NOTE_LONG)));
        return note;
    }

    private String whereClauseById() {
        return NotesSQLite.COLUMN_NOTE_ID + " = ?";
    }
}
