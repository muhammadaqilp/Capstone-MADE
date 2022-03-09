package com.example.core.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.core.databinding.ItemGamesBinding
import com.example.core.domain.model.Games

class GamesAdapter(
    private val callback: OnItemClickCallback
) : RecyclerView.Adapter<GamesAdapter.ViewHolder>() {

    private var list = ArrayList<Games>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newList: List<Games>?) {
        if (newList == null) return
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GamesAdapter.ViewHolder {
        val binding = ItemGamesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GamesAdapter.ViewHolder, position: Int) {
        val games = list[position]
        holder.bind(games)
    }

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(private val binding: ItemGamesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Games) {
            with(binding) {
                tvName.text = data.name
                tvRating.text = data.rating.toString()
                tvGenre.text = data.genres
                Glide.with(itemView.context)
                    .load(data.image)
                    .into(ivBg)

                if (data.genres?.isEmpty() == true) {
                    binding.tvBlt.visibility = View.GONE
                }

                itemView.setOnClickListener {
                    callback.onItemClicked(data.id)
                }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(id: Int?)
    }
}