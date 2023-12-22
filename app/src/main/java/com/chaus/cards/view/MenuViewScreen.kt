package com.chaus.cards.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.chaus.cards.databinding.MenuViewScreenBinding
import com.chaus.cards.entity.GameSettings

class MenuViewScreen: Fragment() {

    private var _binding: MenuViewScreenBinding? = null
    private val binding: MenuViewScreenBinding
        get() = _binding ?: throw RuntimeException("MenuViewScreenBinding == null")

    private val gameSettings = GameSettings(NUMBER_COINS, NUMBER_ROWS)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View {

        _binding = MenuViewScreenBinding.inflate(inflater, container, false)
        Log.i("MyTag", "MenuViewScreen onViewCreated")
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvCoins.text = gameSettings.numberCoins.toString()
        binding.btPlay.setOnClickListener {
            launchGameSceneScreen()
        }
    }

    private fun launchGameSceneScreen(){
        findNavController()
            .navigate(MenuViewScreenDirections.actionMenuViewScreenToGameSceneScreen(gameSettings))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.i("MyTag", "MenuViewScreen onDestroyView")
    }

    override fun onDestroy() {
        Log.i("MyTag", "MenuViewScreen onDestroy")
        super.onDestroy()
    }

    companion object{
        const val NUMBER_COINS = 100
        const val NUMBER_ROWS = 5
    }

}
