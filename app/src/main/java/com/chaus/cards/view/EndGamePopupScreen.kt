package com.chaus.cards.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.chaus.cards.R
import com.chaus.cards.databinding.EndGamePopupScreenBinding
import com.chaus.cards.entity.GameResult
import com.chaus.cards.entity.GameSettings

class EndGamePopupScreen: Fragment() {

    private var _binding: EndGamePopupScreenBinding? = null
    private val binding: EndGamePopupScreenBinding
        get() = _binding ?: throw RuntimeException("EndGamePopupScreenBinding == null")

    private var gameSettings = GameSettings()
    private var gameResult: GameResult? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View {

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                launchMenuScreen()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)


        _binding = EndGamePopupScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parsArgs()
        binding.tvCoins.text = gameResult?.numberCoins.toString()
        binding.homeButton.setOnClickListener {
            launchMenuScreen()
        }
        binding.btDoubleReward.setOnClickListener {
            val numberCoins = gameResult?.numberCoins ?:0
            val copyGameSettings = gameSettings.copy(numberCoins = numberCoins * 2)
            launchGameSceneScreen(copyGameSettings)
        }
    }

    @Suppress("DEPRECATION")
    private fun parsArgs() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getParcelable(KEY_SETTINGS, GameSettings::class.java)?.let {
                gameSettings = it
            }
            requireArguments().getParcelable(KEY_RESULT, GameResult::class.java)?.let {
                gameResult = it
            }
        } else {
            requireArguments().getParcelable<GameSettings>(KEY_SETTINGS)?.let {
                gameSettings = it
            }
            requireArguments().getParcelable<GameResult>(KEY_RESULT)?.let {
                gameResult = it
            }
        }
    }

    private fun launchMenuScreen(){
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container_activity, MenuViewScreen.getInstance())
            .commit()
    }

    private fun launchGameSceneScreen(gameSettings: GameSettings){
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container_activity, GameSceneScreen.getInstance(gameSettings))
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{
        fun getInstance(gameResult: GameResult, gameSettings: GameSettings): Fragment{
            return EndGamePopupScreen().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_RESULT, gameResult)
                    putParcelable(KEY_SETTINGS, gameSettings)
                }
            }
        }
        private const val KEY_RESULT = "gameResult"
        private const val KEY_SETTINGS = "gameSettings"
    }
}