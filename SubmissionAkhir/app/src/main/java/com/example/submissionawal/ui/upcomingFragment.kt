package com.example.submissionawal.ui
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissionawal.R
import com.example.submissionawal.data.remote.response.listEventsItem
import com.example.submissionawal.databinding.FragmentUpcomingBinding
import com.example.submissionawal.vm.FactoryVM
import com.example.submissionawal.vm.MainVM
import com.example.submissionawal.vm.UpcomingAdapter


class upcomingFragment : Fragment() {


    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!


    private val mainVM: MainVM by viewModels {
        FactoryVM.getInstance(requireActivity()) as ViewModelProvider.Factory
    }


    private lateinit var adapter: UpcomingAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)

        setupRecyclerView()
        setupAdapter()
        observeViewModel()

        val root: View = binding.root

        return root
    }



    private fun setupRecyclerView() {
        binding.apply {
            val upcomingEvent = LinearLayoutManager(requireContext())
            rvUpcoming.layoutManager = upcomingEvent
            val itemUpcomingEvent =
                DividerItemDecoration(requireContext(), upcomingEvent.orientation)
            rvUpcoming.addItemDecoration(itemUpcomingEvent)
        }
    }



    private fun setupAdapter() {
        adapter = UpcomingAdapter { eventId ->
            val bundle = Bundle().apply {
                if (eventId != null) {
                    putInt("eventId", eventId)
                }
            }
            findNavController().navigate(R.id.detailEventActivity, bundle)
        }

        binding.rvUpcoming.adapter = adapter
    }



    private fun observeViewModel() {
        binding.apply {
            mainVM.upcomingEvent.observe(viewLifecycleOwner) { listItems ->
                setUpcomingEvent(listItems)
                mainVM.zeroErrorMessage()
            }

            mainVM.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
                if (errorMessage != null) {
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                    mainVM.zeroErrorMessage()
                }
            }

            mainVM.isLoadingUpcoming.observe(viewLifecycleOwner) { isLoading ->
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }
    }



    private fun setUpcomingEvent(lifeUpcomingEvent: List<listEventsItem?>) {
        adapter.submitList(lifeUpcomingEvent)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}