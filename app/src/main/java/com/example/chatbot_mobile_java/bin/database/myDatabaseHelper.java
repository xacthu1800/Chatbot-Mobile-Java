package com.example.chatbot_mobile_java.bin.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.chatbot_mobile_java.bin.data.sql_chatMessage;
import com.example.chatbot_mobile_java.bin.data.sql_list_chatMessage;

import java.util.List;

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
            Log.d("addChatMessage","Fail inserted");
            //Toast.makeText(context, "Fail inserted", Toast.LENGTH_SHORT).show();
        }else{
            Log.d("addChatMessage","Success inserted");
            //Toast.makeText(context, "Success inserted", Toast.LENGTH_SHORT).show();
        }
    }

    public sql_list_chatMessage getChatsByConversationId(int conversationId){
        sql_list_chatMessage chatList = new sql_list_chatMessage();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_CONVERSATION_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new  String[]{String.valueOf(conversationId)});
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                int convId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CONVERSATION_ID));
                String content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT));
                boolean isClient = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_CLIENT)) == 1;
                int timestamp = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TIMESTAMP));

                // Tạo đối tượng ChatMessage (giả định bạn có lớp này)
                sql_chatMessage chat = new sql_chatMessage(id, convId, content, isClient, (int) (System.currentTimeMillis() / 1000));
                chatList.add(chat);
            } while (cursor.moveToNext());
        }

        return chatList;
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
