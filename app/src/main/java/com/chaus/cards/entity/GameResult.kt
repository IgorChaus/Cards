package com.chaus.cards.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameResult(
    val time: Int?,
    val numberCoins: Int?
): Parcelable