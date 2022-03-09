package com.example.capstone_made.search

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstone_made.R
import com.example.capstone_made.databinding.FragmentSearchBinding
import com.example.capstone_made.detail.DetailGamesActivity
import com.example.core.data.Resource
import com.example.core.ui.GamesAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment(), GamesAdapter.OnItemClickCallback {

    private val searchViewModel: SearchViewModel by viewModel()

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity != null) {

            val gamesAdapter = GamesAdapter(this)

            binding.edtGames.queryHint = getString(R.string.search_hint)
            binding.edtGames.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    if (p0 != null) {
                        searchViewModel.searchGames(p0).observe(viewLifecycleOwner, { games ->
                            when (games) {
                                is Resource.Loading -> loading(true)
                                is Resource.Success -> {
                                    loading(false)
                                    binding.rvGames.visibility = View.VISIBLE
                                    gamesAdapter.setData(games.data)
                                }
                                is Resource.Error -> {
                                    loading(false)
                                    binding.viewError.root.visibility = View.VISIBLE
                                    binding.viewError.tvError.text =
                                        games.message ?: getString(R.string.something_wrong)
                                }
                            }
                        })
                    }
                    binding.edtGames.clearFocus()
                    return true
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    return false
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClicked(id: Int?) {
        val intent = Intent(context, DetailGamesActivity::class.java)
        intent.putExtra(DetailGamesActivity.EXTRA_ID, id)
        intent.putExtra(DetailGamesActivity.EXTRA_FROM, "search")
        startActivity(intent)
    }

}