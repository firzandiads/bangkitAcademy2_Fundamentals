package com.example.submissionawal.ui
import android.os.Bundle
import android.util.Log
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
import com.example.submissionawal.databinding.FragmentFinishBinding
import com.example.submissionawal.vm.FactoryVM
import com.example.submissionawal.vm.FinishAdapter
import com.example.submissionawal.vm.MainVM


class finishFragment : Fragment() {


    private var _binding: FragmentFinishBinding? = null
    private val binding get() = _binding!!

    private val mainVM: MainVM by viewModels {
        FactoryVM.getInstance(requireActivity()) as ViewModelProvider.Factory
    }

    private lateinit var adapter: FinishAdapter



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishBinding.inflate(inflater, container, false)

        setupRecyclerView()
        setupAdapter()
        observeViewModel()
        return binding.root
    }



    private fun setupRecyclerView() {
        binding.rvFinished.layoutManager = LinearLayoutManager(requireContext())
        val itemDecoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        binding.rvFinished.addItemDecoration(itemDecoration)
    }

    private fun setupAdapter() {
        adapter = FinishAdapter { eventId ->
            val bundle = Bundle().apply {
                if (eventId != null) {
                    putInt("eventId", eventId)
                    Log.d("FinishedFragment", "Event ID dikirim: $eventId")
                }
            }
            findNavController().navigate(R.id.detailEventActivity, bundle)
        }
        binding.rvFinished.adapter = adapter
    }



    private fun observeViewModel() {
        binding.apply {
            mainVM.finishedEvent.observe(viewLifecycleOwner) { listItems ->
                setFinishedEvent(listItems)
                mainVM.zeroErrorMessage()
            }

            mainVM.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
                if (errorMessage != null) {
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                    mainVM.zeroErrorMessage()
                }
            }

            mainVM.isLoadingFinished.observe(viewLifecycleOwner) { isLoading ->
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }
    }



    private fun setFinishedEvent(lifeFinishedEvent: List<listEventsItem?>) {
        adapter.submitList(lifeFinishedEvent)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}