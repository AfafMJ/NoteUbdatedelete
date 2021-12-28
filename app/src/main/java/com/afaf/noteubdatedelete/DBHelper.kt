package com.afaf.noteubdatedelete


import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DBHelper(ctx: Context , private val mainActivity: MainActivity): SQLiteOpenHelper(ctx, "notes.db", null, 2) {

    private val sqLite: SQLiteDatabase = writableDatabase
    private val sqLiteRead: SQLiteDatabase = readableDatabase


    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table notesV2 (pk Integer not null primary key autoincrement, note Text)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS notes")
        onCreate(db)
    }

    fun addNotes(note: String): Long{
        val cv = ContentValues()
        cv.put("note", note)
        val su = sqLite.insert("notesV2", null, cv)
        return su
    }



    fun retrieveNotes(): MutableList<NoteData> {
        val res = mutableListOf<NoteData>()

        val c: Cursor = sqLiteRead.rawQuery("select * from notesV2", null)

        if(c.moveToFirst()){
            res.add(NoteData(c.getInt(0), c.getString(1).toString()))
            while (c.moveToNext()) {
                res.add(NoteData(c.getInt(0), c.getString(1).toString()))
            }
        }
        return res
    }


    fun deleteNote(p: Int){
        val res = sqLite.delete("notesV2", "pk = ?", arrayOf(p.toString()))
        mainActivity.notifyRecycler()
    }

    fun update(pk:Int, newNote: String){
        val cv = ContentValues()
        cv.put("note", newNote)
        val res = sqLite.update("notesV2", cv, "pk = ?", arrayOf(pk.toString()))
    }



}