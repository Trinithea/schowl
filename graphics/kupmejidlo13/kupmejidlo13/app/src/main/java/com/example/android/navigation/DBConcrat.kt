package com.example.android.navigation

import android.provider.BaseColumns

// podoba databáze - jednotlivé sloupce

object DBConcrat {
    class UserEntry : BaseColumns {
        companion object {
            val TABLE_NAME = "jidlaTable"
            val COLUMN_DEN = "den"
            val COLUMN_SNIDANE = "snidane"
            val COLUMN_OBED = "obed"
            val COLUMN_VECERE = "vecere"
            val COLUMN_INGR_SN = "ingrSn"
            val COLUMN_INGR_OB = "ingrOb"
            val COLUMN_INGR_VE = "ingrVe"
            val COLUMN_NOTES_SN = "notesSn"
            val COLUMNS_NOTES_OB = "notesOb"
            val COLUMN_NOTES_VE = "notesVe"
        }
    }
}