package com.example.submissionawal.vm
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.submissionawal.data.local.settingsPreferences
import com.example.submissionawal.data.local.model.FavEvents
import com.example.submissionawal.data.remote.response.events
import com.example.submissionawal.data.remote.response.listEventsItem
import com.example.submissionawal.data.repo.EventsRepo
import kotlinx.coroutines.launch

class MainVM(
    private val pref: settingsPreferences,
    private val eventRepository: EventsRepo
) : ViewModel() {
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isLoadingUpcoming = MutableLiveData<Boolean>()
    val isLoadingUpcoming: LiveData<Boolean> = _isLoadingUpcoming

    private val _isLoadingFinished = MutableLiveData<Boolean>()
    val isLoadingFinished: LiveData<Boolean> = _isLoadingFinished

    private val _upcomingEvent = MutableLiveData<List<listEventsItem?>>()
    val upcomingEvent: LiveData<List<listEventsItem?>> = _upcomingEvent

    private val _finishedEvent = MutableLiveData<List<listEventsItem?>>()
    val finishedEvent: LiveData<List<listEventsItem?>> = _finishedEvent

    private val _detailEvent = MutableLiveData<events?>()
    val detailEvent: LiveData<events?> = _detailEvent

    private val _searchEvent = MutableLiveData<List<listEventsItem?>>()
    val searchEvent: LiveData<List<listEventsItem?>> = _searchEvent

    private val _allFavoriteEvents = MutableLiveData<List<FavEvents?>>()
    val allFavoriteEvents: LiveData<List<FavEvents?>> get() = _allFavoriteEvents

    private val _isLoadingFavorite = MutableLiveData<Boolean>()
    val isLoadingFavorite: LiveData<Boolean> = _isLoadingFavorite


    init {
        listUpcomingEvents()
        listFinishedEvents()
        listFavoriteEvents()
    }

    fun listUpcomingEvents() {
        _isLoadingUpcoming.value = true
        viewModelScope.launch {
            val result = eventRepository.getUpcomingEvent()
            _isLoadingUpcoming.value = false
            result.onSuccess {
                _upcomingEvent.value = it
                zeroErrorMessage()
            }.onFailure {
                _errorMessage.value = it.message
            }
        }
    }

    fun listFinishedEvents() {
        _isLoadingFinished.value = true
        viewModelScope.launch {
            val result = eventRepository.getFinishedEvent()
            _isLoadingFinished.value = false
            result.onSuccess {
                _finishedEvent.value = it
                zeroErrorMessage()
            }.onFailure {
                _errorMessage.value = it.message
            }
        }
    }

    fun getDetailEvent(id: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            val result = eventRepository.getDetailEvent(id)
            _isLoading.value = false
            result.onSuccess {
                _detailEvent.value = it
                zeroErrorMessage()
            }.onFailure {
                _errorMessage.value = it.message
            }
        }
    }

    fun searchEvent(keyword: String) {
        _isLoading.value = true  // Show loading
        viewModelScope.launch {
            val result = eventRepository.searchEvent(keyword)  // Call your repository to fetch events
            _isLoading.value = false  // Hide loading once done
            result.onSuccess {
                _finishedEvent.value = it  // Update the LiveData with the search result
            }.onFailure {
                _errorMessage.value = it.message  // Handle error
            }
        }
    }

    fun insertFavoriteEvent(event: FavEvents) {
        viewModelScope.launch {
            val success = eventRepository.insertFavoriteEvent(event)
            if (!success) {
                _errorMessage.value = "Gagal memasukkan ke favorite event, coba lagi"
            }
        }
    }

    fun deleteFavoriteEvent(event: FavEvents) {
        viewModelScope.launch {
            val success = eventRepository.deleteFavoriteEvent(event)
            if (!success) {
                _errorMessage.value = "Gagal menghapus favorite event, coba lagi"
            }
        }
    }

    private fun listFavoriteEvents() {
        Log.d("MainVM", "Fetching favorite events...")
        _isLoadingFavorite.value = true
        eventRepository.getAllFavoriteEvent().observeForever { favoriteEvents ->
            _isLoadingFavorite.value = false
            _allFavoriteEvents.value = favoriteEvents
            Log.d("MainVM", "Favorite events fetched: ${favoriteEvents?.size ?: 0}")
        }
    }




    fun getFavoriteEventById(eventId: Int): LiveData<FavEvents> {
        return eventRepository.getFavoriteEventById(eventId)
    }

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }

    fun getReminderSettings(): LiveData<Boolean> {
        return pref.getReminderSetting().asLiveData()
    }

    fun saveReminderSetting(isReminderActive: Boolean) {
        viewModelScope.launch {
            pref.saveReminderSetting(isReminderActive)
        }
    }

    fun zeroErrorMessage() {
        _errorMessage.value = null
    }
}