package com.chaus.cards.viewmodel


import androidx.lifecycle.*
import com.chaus.cards.R
import com.chaus.cards.entity.Item
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.max

class MainViewModel(coins: Int): ViewModel() {

    private var itemList = arrayListOf(
        Item(image = R.drawable.diamond_1),
        Item(image = R.drawable.diamond_2),
        Item(image = R.drawable.diamond_3),
        Item(image = R.drawable.diamond_4),
        Item(image = R.drawable.diamond_5),
        Item(image = R.drawable.diamond_6),
        Item(image = R.drawable.diamond_7),
        Item(image = R.drawable.diamond_8),
        Item(image = R.drawable.diamond_9),
        Item(image = R.drawable.diamond_10)
    )


    private val _items: MutableLiveData<List<Item>> = MutableLiveData()
    val items: LiveData<List<Item>>
        get() = _items

    private val _counter: MutableLiveData<Int> = MutableLiveData(0)
    val counter: LiveData<Int>
        get() = _counter

    private val _gameResult = MutableLiveData<Int>()
    val gameResult: LiveData<Int>
        get() = _gameResult

    val coin = MediatorLiveData<Int>().apply {
        addSource(counter) {
            value = if (it <= 20){
                coins
            } else {
                max(coins - (it - 20) * 5, 10)
            }
        }
    }

    private var timer: Timer? = null

    private var firstPressedIndexItem: Int? = null
    private var currentItem: Item? = null

    init{
        itemList.addAll(itemList)
        itemList.shuffle()
        _items.value = itemList
    }

    fun startCounter() {
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
            val copyItems = items.value as ArrayList<Item>
            currentItem = copyItems[indexItem]
            copyItems[indexItem] = currentItem!!.copy(visibility = true)
            _items.value = copyItems

            if (firstPressedIndexItem == null) {
                firstPressedIndexItem = indexItem
                currentItem = null
            } else {
                if (copyItems[firstPressedIndexItem!!].image != copyItems[indexItem].image) {
                    delay(300)
                    copyItems[indexItem] = currentItem!!.copy(visibility = false)

                    val firstItem = copyItems[firstPressedIndexItem!!]
                    copyItems[firstPressedIndexItem!!] = firstItem.copy(visibility = false)

                    _items.value = copyItems
                }
                firstPressedIndexItem = null
                currentItem = null
                val hideItem = copyItems.find{ !it.visibility }
                if (hideItem == null){
                    _gameResult.value = coin.value
                    clearCounter()
                }
            }
        }
    }

}