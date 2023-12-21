package com.chaus.cards.repository

import com.chaus.cards.entity.Item

interface GameRepository {
    fun generateCards(numberRows: Int): List<Item>
}