package com.ketanolab.nusimi.util

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import java.util.ArrayList
import java.util.Arrays

object Dictionaries {
    fun scanDictionariesAndValidation(context: Context): ArrayList<String> {
        val paths = ArrayList<String>()
        if (Util.checkExternalStorageAvailable()) {
            val directory = context.getExternalFilesDir(null)
            if (directory!!.exists()) {
                val files = directory.listFiles()
                if (files.size > 0) {
                    Arrays.sort(files)
                    for (i in files.indices) {
                        if (Util.checkFilenameDictionary(files[i].name)) {
                            val path = files[i].absolutePath
                            // Last question
                            val db = SQLiteDatabase.openOrCreateDatabase(path, null)
                            try {
                                Util.getNameAndAuthorDictionary(db) // test consistent file
                                Log.i(Constants.DEBUG, "Find " + files[i].name)
                                paths.add(path)
                            } catch (ex: Exception) {
                                Log.i(
                                    Constants.DEBUG,
                                    "El archivo isFavorite dañado " + files[i].name
                                )
                            }
                            db?.close()
                        }
                    }
                } else {
                    Log.i(Constants.DEBUG, "No hay ningun archivo.")
                    Util.showAlertNoDictionaries(context)
                }
            } else {
                Log.i(Constants.DEBUG, "No existe ruta.")
                Util.showAlertNoDictionaries(context)
            }
        } else {
            Log.i(Constants.DEBUG, "No hay SD.")
            Util.showAlertNoExternalStorage(context)
        }
        return paths
    }

    fun createDictQuery(query: String, characters: HashMap<String,String>): String {

        var columnName = "word"
        var searchString = "\"%$query%\""
        var orderString = "\"$query%\""
        for ((key, value) in characters) {
            columnName = "replace($columnName,\"$key\",\"$value\")"
            searchString = "replace($searchString,\"$key\",\"$value\")"
        }
        return "SELECT _id, word, summary FROM words WHERE $columnName LIKE $searchString  ORDER BY case when word like $orderString then 0 else 1 end,  word"
    }

    fun getDefaultReplacementCharactersForSearchs(): HashMap<String, String> {
        var hashmap = HashMap<String, String> ()
        hashmap.put("á","a")
        hashmap.put("é","e")
        hashmap.put("í","i")
        hashmap.put("ó","o")
        hashmap.put("ú","u")
        hashmap.put("ü","u")
        hashmap.put("ñ","n")
        hashmap.put("ï","i")
        hashmap.put("ɨ","i")
        hashmap.put("ɨ̈̈","i")
        hashmap.put("ë","e")
        hashmap.put("ɨ̈","i")
        hashmap.put("ö","o")
        hashmap.put("ã","a")
        hashmap.put("ẽ","")
        hashmap.put("ĩ","i")
        hashmap.put("ỹ","i")
        hashmap.put("õ","o")
        hashmap.put("ũ","u")
        hashmap.put("ĝ","")
        hashmap.put("g̃","g")
        hashmap.put("'","")
        return hashmap
    }
}