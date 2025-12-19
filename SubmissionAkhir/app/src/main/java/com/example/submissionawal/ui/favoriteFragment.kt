package com.example.submissionawal.ui
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissionawal.R
import com.example.submissionawal.data.local.model.FavEvents
import com.example.submissionawal.databinding.FragmentFavoriteBinding
import com.example.submissionawal.vm.FavoriteAdapter
import com.example.submissionawal.vm.MainVM
import com.example.submissionawal.vm.FactoryVM

class favoriteFragment : Fragment() {


    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding


    private val mainVM: MainVM by viewModels {
        FactoryVM.getInstance(requireActivity()) as ViewModelProvider.Factory
    }


    private lateinit var adapter: FavoriteAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)

        setupRecyclerView()
        setupAdapter()
        observeViewModel()

        return requireNotNull(binding?.root) { "Binding is null!" }
    }


    private fun observeViewModel() {
        binding?.apply {
            val verticalLayout = LinearLayoutManager(requireContext())
            rvFavoriteEvent.layoutManager = verticalLayout
            val itemFavoriteDecoration = DividerItemDecoration(requireContext(), verticalLayout.orientation)
            rvFavoriteEvent.addItemDecoration(itemFavoriteDecoration)
        }

        // Observe loading state for favorite events
        mainVM.isLoadingFavorite.observe(viewLifecycleOwner) { isLoading ->
            Log.d("FavoriteFragment", "isLoadingFavorite: $isLoading")
            isLoading(isLoading)  // Show/hide ProgressBar based on loading state
        }

        // Observe favorite events
        mainVM.allFavoriteEvents.observe(viewLifecycleOwner) { favoriteEvents ->
            adapter.submitList(favoriteEvents)  // Update the adapter with the new data
        }

        // Handle any error messages if necessary
        mainVM.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                mainVM.zeroErrorMessage()  // Reset error message after showing
            }
        }
    }



    private fun isLoading(isLoading: Boolean) {
        binding?.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }



    private fun setupAdapter() {
        adapter = FavoriteAdapter { eventId ->
            val bundle = Bundle().apply {
                eventId?.let { putInt("eventId", it) }
            }
            findNavController().navigate(R.id.detailEventActivity, bundle)
        }

        binding?.rvFavoriteEvent?.adapter = adapter
    }


    private fun setupRecyclerView() {
        binding?.rvFavoriteEvent?.apply {


            mainVM.allFavoriteEvents.observe(viewLifecycleOwner) { listEvent ->
                Log.d("FavoriteFragment", "Observed Favorite Events: $listEvent")
                setFavoriteEvents(listEvent)
                mainVM.zeroErrorMessage()
            }

            mainVM.isLoadingFavorite.observe(viewLifecycleOwner) { isLoading ->
                showLoading(isLoading)  // Pastikan ini menunjukkan ProgressBar
            }


            mainVM.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
                if (errorMessage != null) {
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                    mainVM.zeroErrorMessage()
                }
            }
        }
    }


    private fun showLoading(isLoading: Boolean) {
        binding?.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }




    private fun setFavoriteEvents(listEvent: List<FavEvents?>?) {
        adapter.submitList(listEvent)
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}