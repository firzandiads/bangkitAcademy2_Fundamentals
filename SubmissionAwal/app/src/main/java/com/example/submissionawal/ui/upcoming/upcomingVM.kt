package com.example.submissionawal.ui.upcoming
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.submissionawal.data.response.detailResponse
import com.example.submissionawal.data.response.events
import com.example.submissionawal.data.response.eventsResponse
import com.example.submissionawal.data.response.listEventsItem
import com.example.submissionawal.data.retrofit.apiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class upcomingVM : ViewModel() {
    private val _upcoming = MutableLiveData<List<listEventsItem>?>()
    val upcoming: LiveData<List<listEventsItem>?> = _upcoming


    private val _detailUpcoming = MutableLiveData<events?>()
    val detailUpcoming: LiveData<events?> = _detailUpcoming


    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    private val _error = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _error


    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message


    companion object {
        private const val TAG = "UpcomingViewModel"
        private const val EVENT_ID = 1
    }

    init {
        listUpcomingEvents()
    }


    private fun listUpcomingEvents() {
        _isLoading.value = true
        val client = apiConfig.getApiService().getListEvents(EVENT_ID)
        client.enqueue(object : Callback<eventsResponse> {
            override fun onResponse(call: Call<eventsResponse>, response: Response<eventsResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        _isLoading.value = false
                        _upcoming.value = it.listEvents as List<listEventsItem>?
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _error.value = true
                    _message.value = "Error: ${response.message()}"
                }
            }


            override fun onFailure(p0: Call<eventsResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
                _isLoading.value = false
                _error.value = true
                if (t is java.net.UnknownHostException) {
                    _message.value = "Tidak ada koneksi internet"
                } else {
                    _message.value = "Error: ${t.message}"
                }
            }
        })
    }


    fun detailUpcomingEvent(id: Int) {
        _isLoading.value = true
        val client = apiConfig.getApiService().getDetailEvent(id)
        client.enqueue(object : Callback<detailResponse<events>> {
            override fun onResponse(
                call: Call<detailResponse<events>>,
                response: Response<detailResponse<events>>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    _detailUpcoming.value = response.body()?.event
                } else {
                    _isLoading.value = false
                    _error.value = true
                    _message.value = "Error: ${response.message()}"
                    Log.e(
                        TAG,
                        "onFailure: ${response.message()}"
                    )
                }
            }


            override fun onFailure(call: Call<detailResponse<events>>, t: Throwable) {
                _isLoading.value = false
                _error.value = true
                _message.value = "Error: ${t.message}"
                Log.e(
                    TAG,
                    "onFailure: ${t.message}"
                )
            }
        })
    }
}