package com.chaus.cards.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.chaus.cards.*
import com.chaus.cards.databinding.GameSceneScreenBinding
import com.chaus.cards.entity.GameResult
import com.chaus.cards.entity.GameSettings
import com.chaus.cards.viewmodel.MainViewModel
import com.chaus.cards.viewmodel.MainViewModelFactory

class GameSceneScreen: Fragment() {

    private var _binding: GameSceneScreenBinding? = null
    private val binding: GameSceneScreenBinding
        get() = _binding ?: throw RuntimeException("GameSceneScreenBinding == null")

    private var gameSettings = GameSettings()

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

        _binding = GameSceneScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parsArgs()
        val factory = MainViewModelFactory(gameSettings)
        val viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
        val adapter = ItemAdapter()
        adapter.items = viewModel.itemList

        adapter.clickListener = {
            viewModel.checkItem(it)
        }

        viewModel.item.observe(viewLifecycleOwner){
            adapter.items = viewModel.itemList
            adapter.notifyItemChanged(it)
        }

        viewModel.counter.observe(viewLifecycleOwner){
            binding.tvTimer.text = it.toString()
        }

        viewModel.coin.observe(viewLifecycleOwner){
            binding.tvScore.text = it.toString()
        }

        viewModel.gameResult.observe(viewLifecycleOwner){
            launchEndGamePopupScreen(it)
        }

        binding.rv.overScrollMode = View.OVER_SCROLL_NEVER
        binding.rv.adapter = adapter

    }

    private fun parsArgs() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getParcelable(KEY_SETTINGS, GameSettings::class.java)?.let {
                gameSettings = it
            }
        } else {
            @Suppress("DEPRECATION")
            requireArguments().getParcelable<GameSettings>(KEY_SETTINGS)?.let {
                gameSettings = it
            }
        }
    }

    private fun launchEndGamePopupScreen(gameResult: GameResult){
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container_activity, EndGamePopupScreen.getInstance(gameResult, gameSettings))
            .commit()
    }

    private fun launchMenuScreen(){
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container_activity, MenuViewScreen.getInstance())
            .commit()
    }

    companion object{
        fun getInstance(gameSettings: GameSettings): Fragment{
            return GameSceneScreen().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_SETTINGS, gameSettings)
                }
            }
        }
        private const val KEY_SETTINGS = "gameSettings"
    }
}