package com.udacity.asteroidradar.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants.TAG
import com.udacity.asteroidradar.databinding.ListItemAsteriodBinding

class AsteroidListAdapter(private val clickListener: ClickListener) :
    ListAdapter<Asteroid, AsteroidListAdapter.ViewHolder>(
        DiffUtilCallback()
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d(TAG, "onCreateViewHolder: ")
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder: ")
        holder.bind(getItem(position), clickListener)
    }

    class ViewHolder(
        private val binding: ListItemAsteriodBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(asteroid: Asteroid, clickListener: ClickListener) {
            binding.asteroid = asteroid
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ListItemAsteriodBinding.inflate(inflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    private class DiffUtilCallback : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem == newItem
        }
    }

    class ClickListener(val block: (asteroid: Asteroid) -> Unit) {
        fun onClick(asteroid: Asteroid) = block(asteroid)
    }
}

