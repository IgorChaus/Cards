package com.chaus.cards.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.chaus.cards.*
import com.chaus.cards.databinding.GameSceneScreenBinding
import com.chaus.cards.viewmodel.MainViewModel
import com.chaus.cards.viewmodel.MainViewModelFactory

class GameSceneScreen: Fragment() {

    private var _binding: GameSceneScreenBinding? = null
    private val binding: GameSceneScreenBinding
        get() = _binding ?: throw RuntimeException("GameSceneScreenBinding == null")

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
        val numberCoins = requireArguments().getInt(KEY_COINS)
        val factory = MainViewModelFactory(numberCoins)
        val viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
        val adapter = ItemAdapter()

        adapter.clickListener = {
            viewModel.checkItem(it)
        }

        viewModel.items.observe(viewLifecycleOwner){
            adapter.items = it
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

        binding.rv.adapter = adapter
        viewModel.startCounter()

    }

    private fun launchEndGamePopupScreen(numberCoins: Int){
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container_activity, EndGamePopupScreen.getInstance(numberCoins))
            .commit()
    }

    private fun launchMenuScreen(){
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container_activity, MenuViewScreen.getInstance())
            .commit()
    }

    companion object{
        fun getInstance(numberCoins: Int): Fragment{
            return GameSceneScreen().apply {
                arguments = Bundle().apply {
                    putInt(KEY_COINS,numberCoins)
                }
            }
        }
        private const val KEY_COINS = "numberCoins"
    }
}