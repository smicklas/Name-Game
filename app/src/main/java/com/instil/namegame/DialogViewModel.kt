package com.instil.namegame

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DialogViewModel : ViewModel() {
    private val selectedNumber = MutableLiveData<Int>()


    fun setItem(selectNum: String){
        selectedNumber.value = selectNum.toInt()
    }

    fun getSelectedNumber(): Int? {
        return selectedNumber.value
    }
}