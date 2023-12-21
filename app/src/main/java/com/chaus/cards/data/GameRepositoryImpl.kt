package com.chaus.cards.data

import com.chaus.cards.R
import com.chaus.cards.entity.Item
import com.chaus.cards.repository.GameRepository
import kotlin.math.min

class GameRepositoryImpl: GameRepository {

    private var itemList = arrayListOf(
        Item(image = R.drawable.diamond_1),
        Item(image = R.drawable.diamond_2),
        Item(image = R.drawable.diamond_3),
        Item(image = R.drawable.diamond_4),
        Item(image = R.drawable.diamond_5),
        Item(image = R.drawable.diamond_6),
        Item(image = R.drawable.diamond_7),
        Item(image = R.drawable.diamond_8),
        Item(image = R.drawable.diamond_9),
        Item(image = R.drawable.diamond_10)
    )

    override fun generateCards(numberRows: Int): List<Item> {
        val numberItems = min(numberRows * 2, 10)
        if (numberItems > 0) {
            itemList.subList(numberRows * 2, itemList.size).clear()
        }
        itemList.addAll(itemList)
        itemList.shuffle()
        return itemList
    }

}