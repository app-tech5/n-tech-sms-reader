package com.ntech.smsreader

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SmsDatabaseHelper(context: Context) : SQLiteOpenHelper(
    context.applicationContext,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE $TABLE_SMS (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                sender TEXT NOT NULL,
                body TEXT NOT NULL,
                received_at INTEGER NOT NULL
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SMS")
        onCreate(db)
    }

    fun insert(sender: String, body: String, receivedAt: Long): Long {
        val values = ContentValues().apply {
            put("sender", sender)
            put("body", body)
            put("received_at", receivedAt)
        }
        return writableDatabase.insert(TABLE_SMS, null, values)
    }

    fun getAll(): List<SmsRecord> {
        val records = mutableListOf<SmsRecord>()
        val cursor = readableDatabase.query(
            TABLE_SMS,
            arrayOf("id", "sender", "body", "received_at"),
            null,
            null,
            null,
            null,
            "received_at DESC"
        )

        cursor.use {
            val idIndex = it.getColumnIndexOrThrow("id")
            val senderIndex = it.getColumnIndexOrThrow("sender")
            val bodyIndex = it.getColumnIndexOrThrow("body")
            val receivedAtIndex = it.getColumnIndexOrThrow("received_at")

            while (it.moveToNext()) {
                records.add(
                    SmsRecord(
                        id = it.getLong(idIndex),
                        sender = it.getString(senderIndex),
                        body = it.getString(bodyIndex),
                        receivedAt = it.getLong(receivedAtIndex)
                    )
                )
            }
        }

        return records
    }

    companion object {
        private const val DATABASE_NAME = "ntech_sms.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_SMS = "sms_messages"
    }
}
