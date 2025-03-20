package com.example.chatbot_mobile_java.bin.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class myDatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "chatDB.db";
    private static final int DATABASE_VERSION = 1;

    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_CONVERSATION_ID = "conversation_id";
    private static final String COLUMN_CONTENT = "content";
    private static final String COLUMN_IS_CLIENT = "is_client";
    private static final String COLUMN_TIMESTAMP = "timestamp";
    private static final String TABLE_NAME = "chat_history";



    public myDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " +
                 COLUMN_ID   + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                 COLUMN_CONVERSATION_ID + " INTEGER, " +
                 COLUMN_CONTENT +" TEXT NOT NULL, " +
                 COLUMN_IS_CLIENT+ " BOOLEAN NOT NULL, " +
                 COLUMN_TIMESTAMP+ " INTEGER NOT NULL)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addChatMessage(int conversation_id, String content, boolean is_client, int timestamp){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_CONVERSATION_ID, conversation_id);
        cv.put(COLUMN_CONTENT, content);
        cv.put(COLUMN_IS_CLIENT, is_client);
        cv.put(COLUMN_TIMESTAMP, timestamp);

        long result = db.insert(TABLE_NAME, null, cv);
        if(result == -1){
            Toast.makeText(context, "False inserted", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Success inserted", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor= db.rawQuery(query, null);
        }
        return cursor;
    }

}
