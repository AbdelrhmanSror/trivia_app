package com.example.android.navigation.settings


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.example.android.navigation.R
import com.example.android.navigation.repositry.QuestionsReopsitry
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


/**
 * A simple [Fragment] subclass.
 *
 */
class SettingsFragment : PreferenceFragmentCompat(),CoroutineScope {

    /**
     *variable indicates to that something has changed in preference setings
     */
    private var isPrefChanged=false
    private lateinit var fragmentJob:Job
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_pref,rootKey)
        fragmentJob=Job()
        //getting difficulty preference to observe the changes had been made to it
        val difficulty:ListPreference?=findPreference(getString(R.string.difficulty))
        //getting category preference to observe the changes had been made to it
        val category:ListPreference?=findPreference(getString(R.string.category))
        difficulty?.setOnPreferenceChangeListener { preference, newValue ->
            isPrefChanged=true
            true
        }
        category?.setOnPreferenceChangeListener { preference, newValue ->
            isPrefChanged=true
            true
        }
    }

    /**
     * if some thing has changed in preference we start fetching from network based on the changes that happened
     * doing that check so we do not make expensive network call for useless result that won't change any thing
     */
    override fun onDestroyView() {
        super.onDestroyView()
        if (isPrefChanged) {
            Log.v("sizeoflist","settings has changed")

            launch {
                withContext(Dispatchers.IO)
                {
                    QuestionsReopsitry(activity!!.application).fetchQuestion()
                    Log.v("sizeoflist","data saved ")

                }
            }
        }

    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO+fragmentJob
}
