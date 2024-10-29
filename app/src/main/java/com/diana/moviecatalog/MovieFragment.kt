package com.diana.moviecatalog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.diana.moviecatalog.databinding.FragmentMovieBinding
import com.diana.moviecatalog.models.Movie
import com.diana.moviecatalog.models.MovieResponse
import com.diana.moviecatalog.services.MovieApiInterface
import com.diana.moviecatalog.services.MovieApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieFragment : Fragment() {
    private var _binding: FragmentMovieBinding? = null
    private val binding get() = _binding!!
    private val movies = arrayListOf<Movie>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvMoviesList.layoutManager = LinearLayoutManager(context)
        binding.rvMoviesList.setHasFixedSize(true)

        getMovieData { movies: List<Movie> ->
            binding.rvMoviesList.adapter = MovieAdapter(movies)
        }
        showRecyclerView()
    }

    private fun getMovieData(callback: (List<Movie>) -> Unit) {
        val apiService = MovieApiService.getInstance().create(MovieApiInterface::class.java)
        apiService.getMovieList().enqueue(object : Callback<MovieResponse> {
            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                // Tambahkan penanganan error jika diperlukan
            }

            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                response.body()?.movies?.let { callback(it) }
            }
        })
    }

    private fun showRecyclerView() {
        binding.rvMoviesList.layoutManager = LinearLayoutManager(context)
        binding.rvMoviesList.adapter = MovieAdapter(movies)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}