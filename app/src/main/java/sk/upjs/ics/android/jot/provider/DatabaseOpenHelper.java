package sk.upjs.ics.android.jot.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

import sk.upjs.ics.android.util.Defaults;

import static android.provider.BaseColumns._ID;
import static sk.upjs.ics.android.jot.provider.Provider.Note.DESCRIPTION;
import static sk.upjs.ics.android.jot.provider.Provider.Note.TABLE_NAME;
import static sk.upjs.ics.android.jot.provider.Provider.Note.TIMESTAMP;
import static sk.upjs.ics.android.util.Defaults.DEFAULT_CURSOR_FACTORY;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "jot";
    public static final int DATABASE_VERSION = 1;

    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, DEFAULT_CURSOR_FACTORY, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTableSql());
        insertSampleEntry(db, "Write a summary article");
        insertSampleEntry(db, "Enjoy Coffee at Whim's");
        insertSampleEntry(db, "Implement #235");
    }

    private void insertSampleEntry(SQLiteDatabase db, String description) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Provider.Note.DESCRIPTION, description);
        contentValues.put(Provider.Note.TIMESTAMP, System.currentTimeMillis() / 1000);
        db.insert(Provider.Note.TABLE_NAME, Defaults.NO_NULL_COLUMN_HACK, contentValues);
    }

    private String createTableSql() {
        String sqlTemplate = "CREATE TABLE %s ("
                + "%s INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "%s TEXT,"
                + "%s DATETIME"
                + ")";
        return String.format(sqlTemplate, TABLE_NAME, _ID, DESCRIPTION, TIMESTAMP);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // do nothing
    }
}
