package com.chaus.cards.viewmodel


import androidx.lifecycle.*
import com.chaus.cards.data.GameRepositoryImpl
import com.chaus.cards.entity.GameResult
import com.chaus.cards.entity.GameSettings
import com.chaus.cards.entity.Item
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.max

class MainViewModel(private val gameSettings: GameSettings): ViewModel() {

    private val repository = GameRepositoryImpl()

    private var counter: Int = 0

    var itemList: ArrayList<Item> = repository.generateCards(gameSettings.numberColumns) as ArrayList<Item>

    private val _item: MutableLiveData<Int> = MutableLiveData()
    val item: LiveData<Int>
        get() = _item

    private val _time: MutableLiveData<String> = MutableLiveData("00:00")
    val time: LiveData<String>
        get() = _time

    private val _gameResult = MutableLiveData<GameResult>()
    val gameResult: LiveData<GameResult>
        get() = _gameResult

    private val _coins: MutableLiveData<Int> = MutableLiveData(gameSettings.numberCoins)
    val coins: LiveData<Int>
        get() = _coins


    private var timer: Timer? = null

    private var firstPressedIndexItem: Int? = null
    private var currentItem: Item? = null


    init{
        startCounter()
    }

    private fun startCounter() {
        timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                counter++
                _time.postValue(formatTime(counter))
                val numberCoins = if (counter <= 20){
                    gameSettings.numberCoins
                } else {
                    max(gameSettings.numberCoins - (counter - 20) * 5, 10)
                }
                _coins.postValue(numberCoins)
                if (counter == 5999){
                    _gameResult.value = GameResult(counter, coins.value)
                }
            }
        }, 1000, 1000)
    }

    private fun clearCounter(){
        timer?.cancel()
        _time.value = "00:00"
    }


    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }

    fun checkItem(currentIndexItem: Int) {
        if (currentItem != null) return
        viewModelScope.launch {
            currentItem = itemList[currentIndexItem].copy(visibility = true)
            itemList[currentIndexItem] = currentItem!!
            _item.value = currentIndexItem
            if (firstPressedIndexItem == null) {
                firstPressedIndexItem = currentIndexItem
                currentItem = null
            } else {
                if (itemList[firstPressedIndexItem!!].image != itemList[currentIndexItem].image) {
                    delay(300)
                    itemList[currentIndexItem] = currentItem!!.copy(visibility = false)
                    _item.value = currentIndexItem

                    val firstItem = itemList[firstPressedIndexItem!!]
                    itemList[firstPressedIndexItem!!] = firstItem.copy(visibility = false)
                    _item.value = firstPressedIndexItem!!
                }
                firstPressedIndexItem = null
                currentItem = null
                checkFinishGame()
            }
        }
    }

    private fun formatTime(seconds: Int): String {
        val minutes = seconds / SECONDS_IN_MINUTES
        val leftSeconds = seconds - (minutes * SECONDS_IN_MINUTES)
        return String.format("%02d:%02d", minutes, leftSeconds)
    }

    private fun checkFinishGame() {
        val hideItem = itemList.find { !it.visibility }
        if (hideItem == null) {
            _gameResult.value = GameResult(counter, coins.value)
            clearCounter()
        }
    }

    companion object{
        private const val SECONDS_IN_MINUTES = 60
    }

}