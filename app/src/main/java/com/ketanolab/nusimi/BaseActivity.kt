package com.ketanolab.nusimi

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.ketanolab.nusimi.util.ResourcesUtil
import java.util.Locale


abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setupLanguage()

    }

    protected fun setupLanguage() {
        var lang = PreferenceManager.getDefaultSharedPreferences(applicationContext).getString("language", "es")
        if (lang == null || lang == "sy") {
            lang = Locale.getDefault().language
        }
        var config = ResourcesUtil.setAppLanguage(lang!!, resources.configuration)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            createConfigurationContext(config)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}