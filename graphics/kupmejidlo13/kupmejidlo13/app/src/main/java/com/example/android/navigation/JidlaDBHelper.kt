package com.example.android.navigation

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper


// propojuje práci aplikace s databází

class JidlaDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    // Jelikož mi nefunguje normální upgradování databáze, musela jsem se uchýlit k tomuto celkovému přepsání
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }


   override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
       onUpgrade(db, oldVersion, newVersion)
    }

    // vloží den do databáze
    @Throws(SQLiteConstraintException::class)
    fun vlozDen(den: DenModel) : Boolean {
        val db = writableDatabase
        val values = ContentValues()

        values.put(DBConcrat.UserEntry.COLUMN_DEN, den.den)
        values.put(DBConcrat.UserEntry.COLUMN_SNIDANE, den.sn)
        values.put(DBConcrat.UserEntry.COLUMN_OBED, den.ob)
        values.put(DBConcrat.UserEntry.COLUMN_VECERE, den.ve)
        values.put(DBConcrat.UserEntry.COLUMN_INGR_SN, den.ingr_sn)
        values.put(DBConcrat.UserEntry.COLUMN_INGR_OB, den.ingr_ob)
        values.put(DBConcrat.UserEntry.COLUMN_INGR_VE, den.ingr_ve)

        val newRowId = db.insert(DBConcrat.UserEntry.TABLE_NAME,null, values)
        // nezaloží vždycky nový řádek, jelikož primary key je název dne (tudíž přepíše již vzniklé pondělí, nezaloží nové)

        return (newRowId > 0)
    }

    @Throws(SQLiteConstraintException::class)
    fun smazDen(den:String) : Boolean {
        val db = writableDatabase
        val selection = DBConcrat.UserEntry.COLUMN_DEN + " LIKE ?"
        val selectionArgs = arrayOf(den)

        return (db.delete(DBConcrat.UserEntry.TABLE_NAME,selection, selectionArgs) > 0)
    }

    @Throws(SQLiteConstraintException::class)
    fun smazVsechno(){
        val db = writableDatabase
        var selection = "den='utery' OR den='streda' OR den='ctvrtek' OR den='patek' OR den='sobota' OR den='nedele' OR den=''"
        db.delete(DBConcrat.UserEntry.TABLE_NAME,selection,null)
        // pondělí nemaže celé, protože má o sloupec navíc, do kterého se ukládají Poznámky
        upravDen(DenModel("pondeli","","","","","",""))
    }

    @Throws(SQLiteConstraintException::class)
    fun upravDen(den: DenModel) : Boolean {
        val db =writableDatabase
        val values = ContentValues()

        values.put(DBConcrat.UserEntry.COLUMN_SNIDANE, den.sn)
        values.put(DBConcrat.UserEntry.COLUMN_OBED, den.ob)
        values.put(DBConcrat.UserEntry.COLUMN_VECERE, den.ve)
        values.put(DBConcrat.UserEntry.COLUMN_INGR_SN, den.ingr_sn)
        values.put(DBConcrat.UserEntry.COLUMN_INGR_OB, den.ingr_ob)
        values.put(DBConcrat.UserEntry.COLUMN_INGR_VE, den.ingr_ve)

        return (db.update(DBConcrat.UserEntry.TABLE_NAME, values, "den='"+den.den+"'", null) > 0 )
        //db.close()


    }

    // vytvoří den z toho, co je v databázi
    fun nactiDen(nazev:String): DenModel {
        val db=writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("SELECT * FROM " + DBConcrat.UserEntry.TABLE_NAME + " WHERE " + DBConcrat.UserEntry.COLUMN_DEN + "='" + nazev + "'", null)
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
            //  return ArrayList()
        }

        var denNazev: String
        var snidane : String
        var obed: String
        var vecere :String
        var ing_sn : String
        var ingr_ob : String
        var ingr_ve : String
        if (cursor!!.moveToFirst()) {
            //while(cursor.isAfterLast == false) {
            denNazev = cursor.getString(cursor.getColumnIndex(DBConcrat.UserEntry.COLUMN_DEN))
            snidane = cursor.getString(cursor.getColumnIndex(DBConcrat.UserEntry.COLUMN_SNIDANE))
            obed = cursor.getString(cursor.getColumnIndex(DBConcrat.UserEntry.COLUMN_OBED))
            vecere = cursor.getString(cursor.getColumnIndex(DBConcrat.UserEntry.COLUMN_VECERE))
            ing_sn = cursor.getString(cursor.getColumnIndex(DBConcrat.UserEntry.COLUMN_INGR_SN))
            ingr_ob = cursor.getString(cursor.getColumnIndex(DBConcrat.UserEntry.COLUMN_INGR_OB))
            ingr_ve = cursor.getString(cursor.getColumnIndex(DBConcrat.UserEntry.COLUMN_INGR_VE))


            //jidla.add(DenModel(nazev, ingr))
            // cursor.moveToNext()
            //}
            return DenModel(denNazev,snidane,obed,vecere,ing_sn,ingr_ob, ingr_ve)
        }
        else
            return DenModel("","","","","","","")
    }

    // vytvoří pole dnů podle toho, co je uložené v databázi
    fun nactiVsechnyJidla(): ArrayList<DenModel> {
        val dny = ArrayList<DenModel>()
        val db=writableDatabase
        var cursor: Cursor? =null
        try{
            cursor = db.rawQuery("SELECT * FROM " + DBConcrat.UserEntry.TABLE_NAME, null)
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        var denNazev: String
        var snidane : String
        var obed: String
        var vecere :String
        var ing_sn : String
        var ingr_ob : String
        var ingr_ve : String
        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                denNazev = cursor.getString(cursor.getColumnIndex(DBConcrat.UserEntry.COLUMN_DEN))
                snidane = cursor.getString(cursor.getColumnIndex(DBConcrat.UserEntry.COLUMN_SNIDANE))
                obed = cursor.getString(cursor.getColumnIndex(DBConcrat.UserEntry.COLUMN_OBED))
                vecere = cursor.getString(cursor.getColumnIndex(DBConcrat.UserEntry.COLUMN_VECERE))
                ing_sn = cursor.getString(cursor.getColumnIndex(DBConcrat.UserEntry.COLUMN_INGR_SN))
                ingr_ob = cursor.getString(cursor.getColumnIndex(DBConcrat.UserEntry.COLUMN_INGR_OB))
                ingr_ve = cursor.getString(cursor.getColumnIndex(DBConcrat.UserEntry.COLUMN_INGR_VE))

                dny.add(DenModel(denNazev,snidane,obed, vecere,ing_sn, ingr_ob, ingr_ve))
                cursor.moveToNext()
            }
        }
        return dny
    }

    // existuje kvůli vkládání ingrediencí do správných buněk v databázi
    fun zjistiTypJidla(jidlo: String):String {
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("SELECT * FROM " + DBConcrat.UserEntry.TABLE_NAME + " WHERE " + DBConcrat.UserEntry.COLUMN_SNIDANE + "='" + jidlo + "'",null)
            if(cursor.count > 0)
                return "snidane"
            else {
                cursor = db.rawQuery("SELECT * FROM " + DBConcrat.UserEntry.TABLE_NAME + " WHERE " + DBConcrat.UserEntry.COLUMN_OBED + "='" + jidlo + "'",null)
                if(cursor.count > 0)
                    return "obed"
                else
                    return ""
                   // cursor = db.rawQuery("SELECT * FROM " + DBConcrat.UserEntry.TABLE_NAME + " WHERE " + DBConcrat.UserEntry.COLUMN_VECERE + "='" + jidlo + "'",null)
            }
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
            //  return ArrayList()
            return ""
        }
    }

    fun vlozIng(jidlo : String, ingredience: String) {
        val db=writableDatabase
        val values = ContentValues()
        var typ = zjistiTypJidla(jidlo)

        if ( typ == "snidane") {
            values.put(DBConcrat.UserEntry.COLUMN_INGR_SN, ingredience)
            db.update(DBConcrat.UserEntry.TABLE_NAME,values, DBConcrat.UserEntry.COLUMN_SNIDANE +"='"+jidlo+"'", null)
        }
        else if ( typ == "obed") {
            values.put(DBConcrat.UserEntry.COLUMN_INGR_OB, ingredience)
            db.update(DBConcrat.UserEntry.TABLE_NAME, values, DBConcrat.UserEntry.COLUMN_OBED + "='" + jidlo + "'", null)
        }
        else {
            values.put(DBConcrat.UserEntry.COLUMN_INGR_VE, ingredience)
            db.update(DBConcrat.UserEntry.TABLE_NAME, values, DBConcrat.UserEntry.COLUMN_VECERE + "='" + jidlo + "'", null)
        }
    }

    fun nactiIngr(jidlo:String) : String {
        val db = writableDatabase
        var typ =""
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("SELECT * FROM " + DBConcrat.UserEntry.TABLE_NAME + " WHERE " + DBConcrat.UserEntry.COLUMN_SNIDANE + "='" + jidlo + "'",null)
            if(cursor.count > 0)
                typ = "snidane"
            else {
                cursor = db.rawQuery("SELECT * FROM " + DBConcrat.UserEntry.TABLE_NAME + " WHERE " + DBConcrat.UserEntry.COLUMN_OBED + "='" + jidlo + "'",null)
                if(cursor.count > 0)
                    typ = "obed"
                else {
                    typ = "vecere"
                    cursor = db.rawQuery("SELECT * FROM " + DBConcrat.UserEntry.TABLE_NAME + " WHERE " + DBConcrat.UserEntry.COLUMN_VECERE + "='" + jidlo + "'", null)
                }
                }
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
        }
        var vysledek = ""
        if ( typ == "snidane") {
            if (cursor!!.moveToFirst())
                return cursor.getString(cursor.getColumnIndex(DBConcrat.UserEntry.COLUMN_INGR_SN))
            else
                return ""
        }
        else if( typ == "obed") {
            if (cursor!!.moveToFirst())
                return cursor.getString(cursor.getColumnIndex(DBConcrat.UserEntry.COLUMN_INGR_OB))
            else
                return ""
        }
        else
            if (cursor!!.moveToFirst())
                return cursor.getString(cursor.getColumnIndex(DBConcrat.UserEntry.COLUMN_INGR_VE))
            else
                return ""
    }

    fun vlozPoznamky(textik : String) {
        val db = writableDatabase
        val values = ContentValues()

        values.put(DBConcrat.UserEntry.COLUMN_NOTES_SN, textik)
        db.update(DBConcrat.UserEntry.TABLE_NAME, values, DBConcrat.UserEntry.COLUMN_DEN+"='pondeli'", null)
    }

    fun nactiPoznamky() : String {
        val db=writableDatabase
        var cursor: Cursor? =null
        try{
            cursor = db.rawQuery("SELECT * FROM " + DBConcrat.UserEntry.TABLE_NAME +" WHERE "+ DBConcrat.UserEntry.COLUMN_DEN + "='pondeli'", null)
            return cursor.getString(cursor.getColumnIndex(DBConcrat.UserEntry.COLUMN_NOTES_SN))
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
            return ""
        }
    }


    companion object {
        val DATABASE_NAME = "Jidlicka.db"
        val DATABASE_VERSION = 2

        // Vytvoření databáze
        private val SQL_CREATE_ENTRIES =
                "CREATE TABLE " + DBConcrat.UserEntry.TABLE_NAME + " (" +
                        DBConcrat.UserEntry.COLUMN_DEN + " TEXT PRIMARY KEY," +
                        DBConcrat.UserEntry.COLUMN_SNIDANE + " TEXT," +
                        DBConcrat.UserEntry.COLUMN_OBED + " TEXT," +
                        DBConcrat.UserEntry.COLUMN_VECERE + " TEXT," +
                        DBConcrat.UserEntry.COLUMN_INGR_SN + " TEXT," +
                        DBConcrat.UserEntry.COLUMN_INGR_OB + " TEXT," +
                        DBConcrat.UserEntry.COLUMN_INGR_VE + " TEXT" +
                        DBConcrat.UserEntry.COLUMN_NOTES_SN + " TEXT)"

        // Smazání databáze
        private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DBConcrat.UserEntry.TABLE_NAME
    }
}