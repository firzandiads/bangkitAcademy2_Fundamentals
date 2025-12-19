package com.example.submissionawal.ui
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissionawal.data.response.eventsResponse
import com.example.submissionawal.data.retrofit.apiConfig
import com.example.submissionawal.data.retrofit.apiService
import com.example.submissionawal.databinding.ActivitySearchBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class searchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var eventAdapter: eventsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val apiService = apiConfig.getApiService()


        eventAdapter = eventsAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = eventAdapter

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { _, _, _ ->
                val keyword = searchView.text.toString()
                performSearch(apiService, keyword)
                searchBar.setText(keyword)
                searchView.hide()
                false
            }
        }
    }


    private fun performSearch(apiService: apiService, keyword: String) {
        showLoading(true)
        apiService.searchEvents(keyword = keyword).enqueue(object : Callback<eventsResponse> {
            override fun onResponse(call: Call<eventsResponse>, response: Response<eventsResponse>) {
                if (response.isSuccessful) {
                    showLoading(false)
                    val events = response.body()?.listEvents
                    Log.d("SearchActivity", "Events: $events")
                    if (!events.isNullOrEmpty()) {
                        eventAdapter.setEvents(events)
                        Toast.makeText(this@searchActivity, "Found ${events.size} events", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@searchActivity, "No events found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@searchActivity, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }


            override fun onFailure(call: Call<eventsResponse>, t: Throwable) {
                Toast.makeText(this@searchActivity, "Failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}