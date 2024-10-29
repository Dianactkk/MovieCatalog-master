package com.diana.moviecatalog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.diana.moviecatalog.databinding.FragmentTelevisionBinding
import com.diana.moviecatalog.models.TV
import com.diana.moviecatalog.models.TVResponse
import com.diana.moviecatalog.services.TVApiInterface
import com.diana.moviecatalog.services.TVApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TelevisionFragment : Fragment() {

    private var _binding: FragmentTelevisionBinding? = null
    private val binding get() = _binding!!
    private val tvList = arrayListOf<TV>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTelevisionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvTvList.layoutManager = LinearLayoutManager(context)
        binding.rvTvList.setHasFixedSize(true)

        getTVData { tvs: List<TV> ->
            binding.rvTvList.adapter = TVAdapter(tvs)
        }
    }

    private fun getTVData(callback: (List<TV>) -> Unit) {
        val apiService = TVApiService.getInstance().create(TVApiInterface::class.java)
        apiService.getTVList().enqueue(object : Callback<TVResponse> {
            override fun onFailure(call: Call<TVResponse>, t: Throwable) {
                // Handle error here
            }

            override fun onResponse(call: Call<TVResponse>, response: Response<TVResponse>) {
                response.body()?.let {
                    callback(it.tv)
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}