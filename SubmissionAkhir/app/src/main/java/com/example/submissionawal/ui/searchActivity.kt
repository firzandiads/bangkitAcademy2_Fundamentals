package com.example.submissionawal.ui
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissionawal.data.remote.response.eventsResponse
import com.example.submissionawal.data.remote.retrofit.apiConfigs
import com.example.submissionawal.data.remote.retrofit.apiServices
import com.example.submissionawal.databinding.ActivitySearchBinding
import com.example.submissionawal.vm.EventsAdapter
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class searchActivity : AppCompatActivity() {


    private lateinit var binding: ActivitySearchBinding
    private lateinit var eventAdapter: EventsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val apiService = apiConfigs.getApiService()


        eventAdapter = EventsAdapter()
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


    private fun performSearch(apiService: apiServices, keyword: String) {
        lifecycleScope.launch {
            try {
                showLoading(true)
                val response = apiService.searchEvents(keyword)
                showLoading(false)
                if (response.isSuccessful) {
                    val events = response.body()?.listEvents
                    if (!events.isNullOrEmpty()) {
                        eventAdapter.setEvents(events.filterNotNull()) // Filter elemen null
                        Toast.makeText(this@searchActivity, "Found ${events.size} events", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@searchActivity, "No events found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@searchActivity, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                showLoading(false)
                Toast.makeText(this@searchActivity, "Failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }



    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}