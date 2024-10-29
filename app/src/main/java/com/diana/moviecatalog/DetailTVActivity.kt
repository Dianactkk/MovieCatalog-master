package com.diana.moviecatalog

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.diana.moviecatalog.databinding.ActivityDetailTvactivityBinding
import com.diana.moviecatalog.models.TV

class DetailTVActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailTvactivityBinding

    companion object {
        const val EXTRA_TVS = "extra_tvs"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailTvactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.imgItemPhoto.clipToOutline = true

        val detailTvs = intent.getParcelableExtra<TV>(EXTRA_TVS)

        if (detailTvs != null) {
            val IMAGE_BASE = "https://image.tmdb.org/t/p/w500/"
            Glide.with(this).load(IMAGE_BASE + detailTvs.poster).into(binding.imgItemPhoto)
            binding.tvItemName.text = detailTvs.title
            binding.tvItemDescription.text = detailTvs.overview
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}