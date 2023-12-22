package com.chaus.cards.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.chaus.cards.databinding.EndGamePopupScreenBinding
import com.chaus.cards.entity.GameSettings

class EndGamePopupScreen: Fragment() {

    private var _binding: EndGamePopupScreenBinding? = null
    private val binding: EndGamePopupScreenBinding
        get() = _binding ?: throw RuntimeException("EndGamePopupScreenBinding == null")

    private val args by navArgs<EndGamePopupScreenArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View {

        _binding = EndGamePopupScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvCoins.text = args.gameResult.numberCoins.toString()
        binding.homeButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btDoubleReward.setOnClickListener {
            var numberCoins = args.gameResult.numberCoins
            if (numberCoins == null){
                numberCoins = 0
            }
            val copyGameSettings = args.gameSettings.copy(numberCoins = numberCoins * 2)
            launchGameSceneScreen(copyGameSettings)
        }
    }

    private fun launchGameSceneScreen(gameSettings: GameSettings){
        findNavController().navigate(
            EndGamePopupScreenDirections.actionEndGamePopupScreenToGameSceneScreen(gameSettings)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}