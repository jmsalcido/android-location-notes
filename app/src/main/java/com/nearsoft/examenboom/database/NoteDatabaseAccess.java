package com.nearsoft.examenboom.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;


public final class NoteDatabaseAccess {

    private static SQLiteDatabase databaseAccess;
    private static Context context;

    public static SQLiteDatabase getDatabaseConnection(Context context) {
        NoteDatabaseAccess.context = context;

        if(databaseAccess == null || !databaseAccess.isOpen()) {
            try{
                openDatabaseConnection();
            } catch(SQLiteException e) {
                createDB();
                openDatabaseConnection();
            }
        }
        return databaseAccess;
    }

    private static void openDatabaseConnection() {
        databaseAccess = SQLiteDatabase.openDatabase(context.getDatabasePath(NotesSQLite.DB_NAME).toString(), null, SQLiteDatabase.OPEN_READWRITE);
    }

    public static void closeDatabaseConnection() {
        if(databaseAccess != null && databaseAccess.isOpen())
            databaseAccess.close();
    }

    private static void createDB(){
        NotesSQLite createDBObject = new NotesSQLite(context, NotesSQLite.DB_NAME, null, 1);
        SQLiteDatabase db = createDBObject.getWritableDatabase();
        db.close();
    }
}
