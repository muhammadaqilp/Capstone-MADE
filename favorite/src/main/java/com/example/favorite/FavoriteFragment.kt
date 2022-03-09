package com.example.favorite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.capstone_made.detail.DetailGamesActivity
import com.example.core.ui.GamesAdapter
import com.example.favorite.databinding.FragmentFavoriteBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

class FavoriteFragment : Fragment(), GamesAdapter.OnItemClickCallback {

    private val favoriteViewModel: FavoriteViewModel by viewModel()

    private val binding: FragmentFavoriteBinding by viewBinding(CreateMethod.INFLATE)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
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

    override fun onItemClicked(id: Int?) {
        val intent = Intent(context, DetailGamesActivity::class.java)
        intent.putExtra(DetailGamesActivity.EXTRA_ID, id)
        startActivity(intent)
    }

}