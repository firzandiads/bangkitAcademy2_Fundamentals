package com.example.submissionawal.ui
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.submissionawal.R
import com.example.submissionawal.data.local.model.FavEvents
import com.example.submissionawal.databinding.ActivityDetailEventsBinding
import com.example.submissionawal.vm.FactoryVM
import com.example.submissionawal.vm.MainVM

class detailEventsActivity : AppCompatActivity() {


    private lateinit var binding: ActivityDetailEventsBinding
    private val mainVM: MainVM by viewModels {
        FactoryVM.getInstance(this) as ViewModelProvider.Factory
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailEventsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val eventId = intent?.getIntExtra("eventId", -1) ?: -1
        Log.d("DetailEventActivity", "Event ID diterima: $eventId")

        if (eventId != -1) {
            mainVM.getDetailEvent(eventId)
        }


        mainVM.getFavoriteEventById(eventId).observe(this) { favoriteEvent ->
            binding.favoriteButton.setImageResource(
                if (favoriteEvent == null) R.drawable.ic_love_unfill else R.drawable.ic_love_fill
            )


            binding.favoriteButton.setOnClickListener {
                val currentEvent = mainVM.detailEvent.value
                currentEvent?.let { event ->
                    if (favoriteEvent == null) {
                        val favorite = FavEvents(
                            eventId = event.id,
                            name = event.name,
                            description = event.summary,
                            ownerName = event.ownerName,
                            cityName = event.cityName,
                            image = event.imageLogo
                        )
                        mainVM.insertFavoriteEvent(favorite)
                    } else {
                        mainVM.deleteFavoriteEvent(favoriteEvent)
                    }
                }
            }
        }


        mainVM.detailEvent.observe(this) { event ->
            event?.let {
                Glide.with(this).load(it.mediaCover).into(binding.imageView)
                binding.tvName.text = it.name
                binding.tvOwner.text = it.ownerName
                binding.tvLocation.text = it.cityName
                binding.tvQuota.text = "${it.quota?.minus(it.registrants ?: 0)}"
                binding.tvCategory.text = it.category
                binding.tvDescription.text = it.description?.let { it1 ->
                    HtmlCompat.fromHtml(
                        it1,
                        HtmlCompat.FROM_HTML_MODE_LEGACY
                    )
                }
                binding.tvBegin.text = it.beginTime
                val link = it.link
                binding.btnRegist.setOnClickListener {
                    val web = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                    startActivity(web)
                }
            }
        }


        mainVM.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        mainVM.errorMessage.observe(this) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                mainVM.zeroErrorMessage()
            }
        }
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}