package com.chaus.cards.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chaus.cards.entity.GameSettings

class MainViewModelFactory(
    private val gameSettings: GameSettings): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(gameSettings) as T
    }
}