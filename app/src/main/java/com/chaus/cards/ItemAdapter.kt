package com.chaus.cards

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chaus.cards.databinding.ItemBinding

class ItemAdapter(): RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    var items = listOf<Item>()
    var clickListener: ((Int) -> Unit)? = null

    class ItemViewHolder(val binding: ItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ItemViewHolder(
        ItemBinding.inflate(
            LayoutInflater
                .from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.binding.imageView.setImageResource(item.image)
        if(item.visibility){
            holder.binding.imageView.visibility = View.VISIBLE
        } else {
            holder.binding.imageView.visibility = View.INVISIBLE
            holder.binding.item.setOnClickListener {
                clickListener?.invoke(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }


}