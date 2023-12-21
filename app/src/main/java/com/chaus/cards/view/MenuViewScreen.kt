package com.chaus.cards.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.chaus.cards.R
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
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container_activity, GameSceneScreen.getInstance(gameSettings))
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{
        fun getInstance() = MenuViewScreen()
        const val NUMBER_COINS = 100
        const val NUMBER_ROWS = 5
    }

}
