package com.example.capstone_made.detail

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.capstone_made.R
import com.example.capstone_made.databinding.ActivityDetailGamesBinding
import com.example.core.data.Resource
import com.example.core.domain.model.Games
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailGamesActivity : AppCompatActivity(R.layout.activity_detail_games) {

    private val detailGamesViewModel: DetailGamesViewModel by viewModel()
    private val binding by viewBinding(ActivityDetailGamesBinding::bind)

    private var stateFav = false
    private var title: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.ivBack.setOnClickListener { onBackPressed() }

        binding.ivShare.setOnClickListener { shareGames() }

        val extras = intent.extras
        if (extras != null) {
            val cat = extras.getString(EXTRA_FROM)
            val id = extras.getInt(EXTRA_ID, 0)
            if (cat != null) {
                if (id != 0) {
                    observeGamesFromSearch(id)
                }
            } else {
                if (id != 0) {
                    observeGames(id)
                }
            }
        }
    }

    private fun observeGamesFromSearch(id: Int) {
        detailGamesViewModel.getDetailGamesFromSearch(id).observe(this, { games ->
            if (games != null) {
                when (games) {
                    is Resource.Loading -> loading(true)
                    is Resource.Success -> {
                        loading(false)
                        binding.nestedSV.visibility = View.VISIBLE
                        val data = games.data
                        if (data != null) {
                            showData(data)
                            stateFav = data.isFavorite == true
                            stateFavorite(stateFav)
                            binding.ivFavorite.setOnClickListener {
                                lifecycleScope.launch {
                                    detailGamesViewModel.insertToDB(data, !stateFav)
                                    stateFavorite(!stateFav)
                                    stateFav = !stateFav
                                }
                            }
                        }
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
    }

    private fun observeGames(id: Int) {
        detailGamesViewModel.getDetailGames(id).observe(this, { games ->
            if (games != null) {
                when (games) {
                    is Resource.Loading -> loading(true)
                    is Resource.Success -> {
                        loading(false)
                        binding.nestedSV.visibility = View.VISIBLE
                        val data = games.data
                        if (data != null) {
                            showData(data)
                        }
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
    }

    private fun showData(data: Games) {
        with(binding) {
            tvName.text = data.name
            title = data.name
            tvRating.text = resources.getString(R.string.rating, data.rating.toString())
            tvGenre.text = data.genres
            Glide.with(this@DetailGamesActivity)
                .load(data.image)
                .into(ivBg)
            if (data.description != null) {
                tvDescription.text =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Html.fromHtml(
                            data.description,
                            Html.FROM_HTML_MODE_COMPACT
                        )
                    } else {
                        @Suppress("DEPRECATION")
                        Html.fromHtml(data.description)
                    }
            }
            stateFav = data.isFavorite == true
            stateFavorite(stateFav)
            ivFavorite.setOnClickListener {
                detailGamesViewModel.setFavoriteGames(data, !stateFav)
            }
        }
    }

    private fun stateFavorite(state: Boolean) {
        if (state) {
            binding.ivFavorite.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_favorited
                )
            )
        } else {
            binding.ivFavorite.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_favorite_border
                )
            )
        }
    }

    private fun shareGames() {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.share_text, title))
        startActivity(Intent.createChooser(intent, "Share This Games"))
    }

    private fun loading(status: Boolean) {
        binding.progressBar.visibility = if (status) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_FROM = "extra_from"
    }
}