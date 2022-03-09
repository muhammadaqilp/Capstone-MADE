package com.example.capstone_made.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.capstone_made.R
import com.example.capstone_made.databinding.FragmentHomeBinding
import com.example.capstone_made.detail.DetailGamesActivity
import com.example.core.data.Resource
import com.example.core.ui.GamesAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment(), GamesAdapter.OnItemClickCallback {

    private val homeViewModel: HomeViewModel by viewModel()

    private val binding: FragmentHomeBinding by viewBinding(CreateMethod.INFLATE)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity != null) {
            val gamesAdapter = GamesAdapter(this)

            homeViewModel.games.observe(viewLifecycleOwner, { games ->
                if (games != null) {
                    when (games) {
                        is Resource.Loading -> loading(true)
                        is Resource.Success -> {
                            loading(false)
                            gamesAdapter.setData(games.data)
                            Log.d("Data", games.data.toString())
                        }
                        is Resource.Error -> {
                            loading(false)
                            binding.viewError.root.visibility = View.VISIBLE
                            binding.viewError.tvError.text =
                                games.message ?: getString(R.string.something_wrong)
                        }
                    }
                }
            })

            with(binding.rvGames) {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context)
                adapter = gamesAdapter
            }
        }
    }

    private fun loading(status: Boolean) {
        binding.progressBar.visibility = if (status) View.VISIBLE else View.GONE
    }

    override fun onItemClicked(id: Int?) {
        val intent = Intent(context, DetailGamesActivity::class.java)
        intent.putExtra(DetailGamesActivity.EXTRA_ID, id)
        startActivity(intent)
    }
}