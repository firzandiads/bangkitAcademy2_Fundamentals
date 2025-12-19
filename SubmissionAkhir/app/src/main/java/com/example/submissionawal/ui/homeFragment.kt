package com.example.submissionawal.ui
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissionawal.R
import com.example.submissionawal.data.remote.response.listEventsItem
import com.example.submissionawal.databinding.FragmentHomeBinding
import com.example.submissionawal.vm.FinishAdapter
import com.example.submissionawal.vm.MainVM
import com.example.submissionawal.vm.UpcomingAdapter
import com.example.submissionawal.vm.FactoryVM

class homeFragment : Fragment() {



    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val mainVM: MainVM by viewModels {
        FactoryVM.getInstance(requireActivity()) as ViewModelProvider.Factory
    }



    private lateinit var upcomingAdapter: UpcomingAdapter
    private lateinit var finishedAdapter: FinishAdapter



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapters()
        setupRecyclerViews()
        setupSearchButton()
        observeViewModel()
    }



    private fun setupAdapters() {
        upcomingAdapter = UpcomingAdapter { eventId ->
            val bundle = Bundle().apply {
                if (eventId != null) {
                    putInt("eventId", eventId)
                }
            }
            findNavController().navigate(R.id.detailEventActivity, bundle)
        }

        finishedAdapter = FinishAdapter { eventId ->
            val bundle = Bundle().apply {
                if (eventId != null) {
                    putInt("eventId", eventId)
                }
            }
            findNavController().navigate(R.id.detailEventActivity, bundle)
        }
    }



    private fun setupRecyclerViews() {
        binding.rvUpcomingEvents.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = upcomingAdapter
        }

        binding.rvFinishedEvents.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = finishedAdapter
        }
    }



    private fun setupSearchButton() {
        binding.searchButton.setOnClickListener {
            val intent = Intent(requireContext(), searchActivity::class.java)
            startActivity(intent)
        }
    }



    private fun observeViewModel() {
        // Observe Upcoming Events
        mainVM.upcomingEvent.observe(viewLifecycleOwner) { listItems ->
            setUpcomingList(listItems)
            mainVM.zeroErrorMessage()
        }

        // Observe Finished Events
        mainVM.finishedEvent.observe(viewLifecycleOwner) { listItems ->
            setFinishedList(listItems)
            mainVM.zeroErrorMessage()
        }

        // Loading untuk Upcoming Events
        mainVM.isLoadingUpcoming.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBarUpcoming.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Loading untuk Finished Events
        mainVM.isLoadingFinished.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBarFinished.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Global Error Message
        mainVM.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                mainVM.zeroErrorMessage()
            }
        }
    }


    private fun setUpcomingList(listEvents: List<listEventsItem?>) {
        val list = if (listEvents.size > 5) listEvents.take(5) else listEvents
        upcomingAdapter.submitList(list)
    }



    private fun setFinishedList(listEvents: List<listEventsItem?>) {
        val list = if (listEvents.size > 5) listEvents.take(5) else listEvents
        finishedAdapter.submitList(list)
    }



    private fun showLoading(isLoading: Boolean) {
        binding.progressBarUpcoming.visibility = if (isLoading) View.VISIBLE else View.GONE
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
