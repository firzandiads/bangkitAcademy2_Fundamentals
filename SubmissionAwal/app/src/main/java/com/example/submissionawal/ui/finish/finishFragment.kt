package com.example.submissionawal.ui.finish
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
import com.example.submissionawal.databinding.FragmentFinishBinding
import com.example.submissionawal.ui.detailEventsActivity

class finishFragment : Fragment() {

    private var _binding: FragmentFinishBinding? = null
    private val binding get() = _binding!!
    private val finishVM by viewModels<finishVM>()

    private lateinit var adapter: FinishAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishBinding.inflate(inflater, container, false)
        val root: View = binding.root

        adapter = FinishAdapter(FinishAdapter.DIFF_CALLBACK)
        binding.rvFinished.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFinished.adapter = adapter

        finishVM.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        finishVM.finished.observe(viewLifecycleOwner) { listEvents ->
            setFinishedList(listEvents)
        }

        finishVM.isError.observe(viewLifecycleOwner) { isError ->
            if (isError == true) {
                finishVM.message.value?.let { errorMessage ->
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                } ?: Toast.makeText(requireContext(), "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
            }
        }

        finishVM.detailFinished.observe(viewLifecycleOwner) { event ->
            event?.let { detailEvent ->
                val intent = Intent(requireContext(), detailEventsActivity::class.java)
                intent.putExtra("EXTRA_EVENT", detailEvent)
                startActivity(intent)
            }
        }

        return root
    }

    private fun setFinishedList(listEvents: List<listEventsItem>?) {
        adapter.submitList(listEvents)

        adapter.onItemClickListener = { event ->
            event.id?.let { finishVM.detailFinishedEvents(it) }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}