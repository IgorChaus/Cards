package com.chaus.cards.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.chaus.cards.databinding.GameSceneScreenBinding
import com.chaus.cards.entity.GameResult
import com.chaus.cards.viewmodel.MainViewModel
import com.chaus.cards.viewmodel.MainViewModelFactory

class GameSceneScreen: Fragment() {

    private var _binding: GameSceneScreenBinding? = null
    private val binding: GameSceneScreenBinding
        get() = _binding ?: throw RuntimeException("GameSceneScreenBinding == null")

    private val args by navArgs<GameSceneScreenArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View {

        _binding = GameSceneScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = MainViewModelFactory(args.gameSettings)
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

        viewModel.time.observe(viewLifecycleOwner){
            binding.tvTimer.text = it
        }

        viewModel.coins.observe(viewLifecycleOwner){
            binding.tvScore.text = it.toString()
        }

        viewModel.gameResult.observe(viewLifecycleOwner){
            launchEndGamePopupScreen(it)
        }

        binding.rv.overScrollMode = View.OVER_SCROLL_NEVER
        binding.rv.adapter = adapter

    }

    private fun launchEndGamePopupScreen(gameResult: GameResult){
        findNavController().navigate(
            GameSceneScreenDirections.actionGameSceneScreenToEndGamePopupScreen(
                gameResult,
                args.gameSettings
            )
        )

    }

}