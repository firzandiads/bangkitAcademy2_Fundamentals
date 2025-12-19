package com.example.submissionawal.ui
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissionawal.data.response.listEventsItem
import com.example.submissionawal.databinding.FragmentHomeBinding
import com.example.submissionawal.ui.finish.FinishAdapter
import com.example.submissionawal.ui.finish.finishVM
import com.example.submissionawal.ui.upcoming.UpcomingAdapter
import com.example.submissionawal.ui.upcoming.upcomingVM

class homeFragment : Fragment() {


    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    private val finishVM by viewModels<finishVM>()
    private val upcomingVM by viewModels<upcomingVM>()


    private lateinit var upcomingAdapter: UpcomingAdapter
    private lateinit var finishAdapter: FinishAdapter


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

        upcomingAdapter = UpcomingAdapter(UpcomingAdapter.DIFF_CALLBACK)
        binding.rvUpcomingEvents.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvUpcomingEvents.adapter = upcomingAdapter

        finishAdapter = FinishAdapter(FinishAdapter.DIFF_CALLBACK)
        binding.rvFinishedEvents.layoutManager = LinearLayoutManager(context)
        binding.rvFinishedEvents.adapter = finishAdapter

        upcomingVM.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showUpcomingLoading(isLoading)
        }

        upcomingVM.upcoming.observe(viewLifecycleOwner) { listEvents ->
            listEvents?.let {
                setUpcomingList(it.take(5))
            }
        }

        finishVM.finished.observe(viewLifecycleOwner) { listEvents ->
            listEvents?.let {
                setFinishedList(it.take(5))
            }
        }

        finishVM.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showFinishedLoading(isLoading)
        }

        upcomingVM.detailUpcoming.observe(viewLifecycleOwner) { event ->
            event?.let { detailEvent ->
                val intent = Intent(requireContext(), detailEventsActivity::class.java)
                intent.putExtra("EXTRA_EVENT", detailEvent)
                startActivity(intent)
            }
        }

        finishVM.detailFinished.observe(viewLifecycleOwner) { event ->
            event?.let { detailEvent ->
                val intent = Intent(requireContext(), detailEventsActivity::class.java)
                intent.putExtra("EXTRA_EVENT", detailEvent)
                startActivity(intent)
            }
        }

        binding.searchButton.setOnClickListener {
            val intent = Intent(requireContext(), searchActivity::class.java)
            startActivity(intent)
        }
    }


    private fun setUpcomingList(listEvents: List<listEventsItem>) {
        upcomingAdapter.submitList(listEvents)

        upcomingAdapter.onItemClickListener = { event ->
            event.id?.let { upcomingVM.detailUpcomingEvent(it) }
        }
    }


    private fun setFinishedList(listEvents: List<listEventsItem>) {
        finishAdapter.submitList(listEvents)

        finishAdapter.onItemClickListener = { event ->
            event.id?.let { finishVM.detailFinishedEvents(it) }
        }
    }

    private fun showUpcomingLoading(isLoading: Boolean) {
        binding.progressBarUpcoming.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showFinishedLoading(isLoading: Boolean) {
        binding.progressBarFinished.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
