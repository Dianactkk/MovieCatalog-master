package com.diana.moviecatalog

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.diana.moviecatalog.databinding.TvItemBinding
import com.diana.moviecatalog.models.TV

class TVAdapter(
    private val tvs: List<TV>
) : RecyclerView.Adapter<TVAdapter.TVViewHolder>() {

    class TVViewHolder(private val binding: TvItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val IMAGE_BASE = "https://image.tmdb.org/t/p/w500/"

        fun bindTV(tv: TV) {
            binding.tvTitle.text = tv.title
            binding.tvOverview.text = tv.overview
            Glide.with(binding.root).load(IMAGE_BASE + tv.poster).into(binding.tvPoster)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TVViewHolder {
        val binding = TvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TVViewHolder(binding)
    }

    override fun getItemCount(): Int = tvs.size

    override fun onBindViewHolder(holder: TVViewHolder, position: Int) {
        val tv = tvs[position]
        holder.bindTV(tv)

        holder.itemView.setOnClickListener {
            moveToTvSDetail(tv, it)
        }
    }

    private fun moveToTvSDetail(tv: TV, view: View) {
        val detailTvsIntent = Intent(view.context, DetailTVActivity::class.java).apply {
            putExtra(DetailTVActivity.EXTRA_TVS, tv)
        }
        view.context.startActivity(detailTvsIntent)
    }
}
