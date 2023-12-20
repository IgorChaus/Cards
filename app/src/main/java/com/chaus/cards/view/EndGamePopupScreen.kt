package com.chaus.cards.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.chaus.cards.R
import com.chaus.cards.databinding.EndGamePopupScreenBinding

class EndGamePopupScreen: Fragment() {

    private var _binding: EndGamePopupScreenBinding? = null
    private val binding: EndGamePopupScreenBinding
        get() = _binding ?: throw RuntimeException("EndGamePopupScreenBinding == null")


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
        val numberCoins = requireArguments().getInt(KEY_COINS)
        binding.tvCoins.text = numberCoins.toString()
        binding.homeButton.setOnClickListener {
            launchMenuScreen()
        }
        binding.btDoubleReward.setOnClickListener {
            launchGameSceneScreen(numberCoins * 2)
        }
    }

    private fun launchMenuScreen(){
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container_activity, MenuViewScreen.getInstance())
            .commit()
    }

    private fun launchGameSceneScreen(numberCoins: Int){
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container_activity, GameSceneScreen.getInstance(numberCoins))
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{
        fun getInstance(numberCoins: Int): Fragment{
            return EndGamePopupScreen().apply {
                arguments = Bundle().apply {
                    putInt(KEY_COINS,numberCoins)
                }
            }
        }
        private const val KEY_COINS = "numberCoins"
    }
}