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

class MainViewModel(gameSettings: GameSettings): ViewModel() {

    private val repository = GameRepositoryImpl()

    var itemList: ArrayList<Item> = repository.generateCards(gameSettings.numberColumns) as ArrayList<Item>

    private val _item: MutableLiveData<Int> = MutableLiveData()
    val item: LiveData<Int>
        get() = _item

    private val _counter: MutableLiveData<Int> = MutableLiveData(0)
    val counter: LiveData<Int>
        get() = _counter

    private val _gameResult = MutableLiveData<GameResult>()
    val gameResult: LiveData<GameResult>
        get() = _gameResult

    val coin = MediatorLiveData<Int>().apply {
        addSource(counter) {
            value = if (it <= 20){
                gameSettings.numberCoins
            } else {
                max(gameSettings.numberCoins - (it - 20) * 5, 10)
            }
        }
    }

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
                _counter.postValue(counter.value?.plus(1))
            }
        }, 1000, 1000)
    }

    private fun clearCounter(){
        timer?.cancel()
        _counter.value = 0
    }


    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }

    fun checkItem(indexItem: Int) {
        if (currentItem != null) return
        viewModelScope.launch {
            currentItem = itemList[indexItem].copy(visibility = true)
            itemList[indexItem] = currentItem!!
            _item.value = indexItem
            if (firstPressedIndexItem == null) {
                firstPressedIndexItem = indexItem
                currentItem = null
            } else {
                if (itemList[firstPressedIndexItem!!].image != itemList[indexItem].image) {
                    delay(300)
                    itemList[indexItem] = currentItem!!.copy(visibility = false)
                    _item.value = indexItem

                    val firstItem = itemList[firstPressedIndexItem!!]
                    itemList[firstPressedIndexItem!!] = firstItem.copy(visibility = false)
                    _item.value = firstPressedIndexItem
                }
                firstPressedIndexItem = null
                currentItem = null
                checkItemsVisibility()
            }
        }
    }

    private fun checkItemsVisibility() {
        val hideItem = itemList.find { !it.visibility }
        if (hideItem == null) {
            _gameResult.value = GameResult(counter.value, coin.value)
            clearCounter()
        }
    }

}