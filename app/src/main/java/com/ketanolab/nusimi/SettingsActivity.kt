package com.ketanolab.nusimi

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import java.util.Locale

class SettingsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupLanguage()
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = getString(R.string.settings)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return true
    }

    class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {


        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            preferenceManager.findPreference<Preference>("language")?.let {
                it.setOnPreferenceChangeListener { _, newValue ->
                    Locale.setDefault(Locale(newValue.toString()))
                    var intent = Intent(requireContext(), MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    true
                }
            }
        }

        override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {

            return true
        }
    }
}


