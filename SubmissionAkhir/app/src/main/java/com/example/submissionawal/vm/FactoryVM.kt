package com.example.submissionawal.vm
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.submissionawal.di.Injection
import com.example.submissionawal.data.local.settingsPreferences
import com.example.submissionawal.data.repo.EventsRepo


class FactoryVM private constructor(
    private val pref: settingsPreferences,
    private val eventRepository: EventsRepo
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainVM::class.java)) {
            return MainVM(pref, eventRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class: " + modelClass.name)
    }



    companion object {
        @Volatile
        private var instance: FactoryVM? = null
        fun getInstance(context: Context): Any =
            instance ?: synchronized(this) {
                val preferences = Injection.providePreferences(context)
                val repository = Injection.provideRepository(context)
                instance ?: FactoryVM(preferences, repository)
            }.also { instance = it }
    }
}