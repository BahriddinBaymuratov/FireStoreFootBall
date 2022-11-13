package com.example.firestorefootball.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.firestorefootball.databinding.ClubLayoutBinding
import com.example.firestorefootball.model.FootballClub
import com.squareup.picasso.Picasso

class FootballAdapter :
    ListAdapter<FootballClub, FootballAdapter.UserViewHolder>(DiffCallback()) {
    private class DiffCallback : DiffUtil.ItemCallback<FootballClub>() {

        override fun areItemsTheSame(oldItem: FootballClub, newItem: FootballClub): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: FootballClub, newItem: FootballClub): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            ClubLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.find(getItem(position))
    }

    inner class UserViewHolder(private val binding: ClubLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun find(club: FootballClub) {
            binding.apply {
                Picasso.get()
                    .load(club.logo)
                    .into(imageView)

                textView.text = club.name
            }
        }
    }

}