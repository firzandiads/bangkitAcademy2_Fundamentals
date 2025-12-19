package com.example.submissionawal.ui
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.submissionawal.databinding.FragmentSettingsBinding
import com.example.submissionawal.vm.FactoryVM
import com.example.submissionawal.vm.MainVM
import java.text.SimpleDateFormat
import java.util.Locale



class settingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding


    private val mainVM: MainVM by viewModels {
        FactoryVM.getInstance(requireActivity()) as ViewModelProvider.Factory
    }


    private lateinit var workManager: WorkManager
    private lateinit var workRequest: PeriodicWorkRequest


    private val requestPermissionManager = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(
                requireContext(), "Notifications permission granted", Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                requireContext(), "Notifications permission rejected", Toast.LENGTH_SHORT
            )
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        workManager = WorkManager.getInstance(requireContext())

        mainVM.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding?.switchDarkMode?.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding?.switchDarkMode?.isChecked = false
            }
        }

        binding?.switchDarkMode?.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            mainVM.saveThemeSetting(isChecked)
        }

        mainVM.getReminderSettings()
            .observe(viewLifecycleOwner) { isReminderActive: Boolean ->
                if (isReminderActive) {
                    binding?.switchDailyReminder?.isChecked = true
                    if (Build.VERSION.SDK_INT >= 33) {
                        if (ContextCompat.checkSelfPermission(
                                requireContext(),
                                Manifest.permission.POST_NOTIFICATIONS
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            requestPermissionManager.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }
                } else {
                    binding?.switchDailyReminder?.isChecked = false
                }
            }

        binding?.switchDailyReminder?.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            mainVM.saveReminderSetting(isChecked)
            if (isChecked) {
                setupDailyReminder()
            } else {
                cancelDailyReminder()
            }
        }

        return requireNotNull(binding?.root) { "Binding is null!" }
    }



    private fun cancelDailyReminder() {
        if (this::workRequest.isInitialized) {
            workManager.cancelWorkById(workRequest.id)
        }
    }



    private fun setupDailyReminder() {
        mainVM.listUpcomingEvents()
        mainVM.upcomingEvent.observe(viewLifecycleOwner) { listEvent ->
            if (listEvent.isEmpty()) {
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

                val validEvents = listEvent.filter { event ->
                    event?.beginTime?.let {
                        val eventDate = sdf.parse(it)?.time ?: 0
                        eventDate >= System.currentTimeMillis()
                    } ?: false
                }

                if (validEvents.isNotEmpty()) {
                    val nearesEvent = validEvents.minByOrNull { event ->
                        event?.beginTime?.let { sdf.parse(it)?.time } ?: Long.MAX_VALUE
                    }
                }
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}