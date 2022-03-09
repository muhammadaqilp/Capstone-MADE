package com.example.favorite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstone_made.detail.DetailGamesActivity
import com.example.core.ui.GamesAdapter
import com.example.favorite.databinding.FragmentFavoriteBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

class FavoriteFragment : Fragment(), GamesAdapter.OnItemClickCallback {

    private val favoriteViewModel: FavoriteViewModel by viewModel()

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        loadKoinModules(favoriteModule)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity != null) {
            val gamesAdapter = GamesAdapter(this)

            favoriteViewModel.favorites.observe(viewLifecycleOwner, { games ->
                if (games != null) {
                    gamesAdapter.setData(games)
                }
            })

            with(binding.rvGames) {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context)
                adapter = gamesAdapter
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClicked(id: Int?) {
        val intent = Intent(context, DetailGamesActivity::class.java)
        intent.putExtra(DetailGamesActivity.EXTRA_ID, id)
        startActivity(intent)
    }

}