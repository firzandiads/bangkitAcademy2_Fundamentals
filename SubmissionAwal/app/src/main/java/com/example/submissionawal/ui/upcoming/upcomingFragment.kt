package com.example.submissionawal.ui.upcoming
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissionawal.data.response.listEventsItem
import com.example.submissionawal.databinding.FragmentUpcomingBinding
import com.example.submissionawal.ui.detailEventsActivity

class upcomingFragment : Fragment() {


    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!
    private val upcomingVM by viewModels<upcomingVM>()


    private lateinit var adapter: UpcomingAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)

        adapter = UpcomingAdapter(UpcomingAdapter.DIFF_CALLBACK)
        binding.rvUpcoming.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUpcoming.adapter = adapter

        upcomingVM.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        upcomingVM.upcoming.observe(viewLifecycleOwner) { listEvents ->
            setUpcomingEvent(listEvents)
        }


        upcomingVM.isError.observe(viewLifecycleOwner) { isError ->
            if (isError == true) {
                upcomingVM.message.value?.let { errorMessage ->
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                } ?: Toast.makeText(requireContext(), "Terjadi kesalahan", Toast.LENGTH_SHORT)
                    .show()
            }
        }


        upcomingVM.detailUpcoming.observe(viewLifecycleOwner) { event ->
            event?.let { detailEvent ->
                val intent = Intent(requireContext(), detailEventsActivity::class.java)
                intent.putExtra("EXTRA_EVENT", detailEvent)
                startActivity(intent)
            }
        }

        val root: View = binding.root

        return root
    }


    private fun setUpcomingEvent(listEventsItem: List<listEventsItem>?) {
        adapter.submitList(listEventsItem)

        adapter.onItemClickListener = { event ->
            event.id?.let { upcomingVM.detailUpcomingEvent(it) }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}