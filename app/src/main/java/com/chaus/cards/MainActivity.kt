package com.chaus.cards

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.chaus.cards.databinding.ActivityMainBinding
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private val binding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    var items = arrayListOf<Item>(
        Item(image = R.drawable.diamond_1),
        Item(image = R.drawable.diamond_2),
        Item(image = R.drawable.diamond_3),
        Item(image = R.drawable.diamond_4),
        Item(image = R.drawable.diamond_5),
        Item(image = R.drawable.diamond_6),
        Item(image = R.drawable.diamond_7),
        Item(image = R.drawable.diamond_8),
        Item(image = R.drawable.diamond_9),
        Item(image = R.drawable.diamond_10),
    )

    var firstPressedIndexItem: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        items.addAll(items)
        items.shuffle()
        val adapter = ItemAdapter()
        val scope = CoroutineScope(Dispatchers.Main)

        adapter.clickListener = {

            scope.launch {
                val item = items[it]
                items[it] = item.copy(visibility = true)
                adapter.items = items
                adapter.notifyItemChanged(it)

                if (firstPressedIndexItem == null){
                    firstPressedIndexItem = it
                } else {
                    if(items[firstPressedIndexItem!!].image != items[it].image){
                        delay(300)
                        items[it] = item.copy(visibility = false)

                        val firstItem = items[firstPressedIndexItem!!]
                        items[firstPressedIndexItem!!] = firstItem.copy(visibility = false)

                        adapter.items = items
                        adapter.notifyItemChanged(it)
                        adapter.notifyItemChanged(firstPressedIndexItem!!)
                    }
                    firstPressedIndexItem = null
                }
            }

        }
        binding.rv.adapter = adapter
        adapter.items = items
        adapter.notifyDataSetChanged()
    }
}