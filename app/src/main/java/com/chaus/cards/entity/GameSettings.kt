package com.chaus.cards.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameSettings(
    val numberCoins: Int = 100,
    val numberColumns: Int = 5,
): Parcelable