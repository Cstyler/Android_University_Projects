package bmstu.ru.lab6

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.preference.ListPreference
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import android.util.Log
import android.widget.Toast

class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener,
    Preference.OnPreferenceChangeListener {
    companion object {
        private const val TAG = "SettingsFragment"
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.pref_fragment)

        val sharedPreferences = preferenceScreen.sharedPreferences
        setListSummary(sharedPreferences)

        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        findPreference(getString(R.string.edit_text_pref_key)).onPreferenceChangeListener = this
    }

    private fun setListSummary(sharedPreferences: SharedPreferences) {
        val preference = findPreference(getString(R.string.list_pref_key)) as ListPreference
        val value = sharedPreferences.getString(preference.key, "")
        setPreferenceSummary(preference, value)
    }

    override fun onPreferenceChange(preference: Preference, newValue: Any): Boolean {
        if (preference.key == getString(R.string.edit_text_pref_key)) {
            val prefValue = (newValue as String).toIntOrNull()
            Log.i(TAG, "New value of editText preference: $newValue")
            if (prefValue != null) {
                return true
            }
            Toast.makeText(activity, "invalid text, type a number", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?) {
        Log.i(TAG, "Preference changed: $key")
        when (key) {
            getString(R.string.list_pref_key) -> {
                setListSummary(sharedPreferences)
            }
        }
    }

    private fun setPreferenceSummary(preference: ListPreference, value: String) {
        Log.i(TAG, "Setting summary to $value")
        val prefIndex = preference.findIndexOfValue(value)
        if (0 <= prefIndex && prefIndex < preference.entries.size) {
            preference.summary = preference.entries[prefIndex]
        }
    }
}