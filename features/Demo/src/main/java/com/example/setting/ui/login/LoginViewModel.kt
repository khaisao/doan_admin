package com.example.setting.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.core.base.BaseViewModel
import com.example.core.model.network.ApiUser
import com.example.core.network.ApiInterface
import com.example.setting.model.SettingItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val apiInterface: ApiInterface,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    val listData = MutableLiveData<ArrayList<SettingItem>>(arrayListOf())

    val listUser = MutableLiveData<List<ApiUser>>(arrayListOf())

    init {
        fetchListData()
    }

    private fun fetchListData() {
        viewModelScope.launch {
            isLoading.value = true
            try {
                listUser.value = apiInterface.getUsers()
                isLoading.value = false
            } catch (e: Exception) {
                isLoading.value = false
            }
        }
    }

//    fun initListData(): ArrayList<SettingItem> {
//        val listData = arrayListOf<SettingItem>()
//        listData.add(SettingItem("Item 1", true))
//        listData.add(SettingItem("Item 2", false))
//        listData.add(SettingItem("Item 3", true))
//        listData.add(SettingItem("Item 4", false))
//        listData.add(SettingItem("Item 5", true))
//        listData.add(SettingItem("Item 6", true))
//        listData.add(SettingItem("Item 7", false))
//        listData.add(SettingItem("Item 8", true))
//        listData.add(SettingItem("Item 9", false))
//        listData.add(SettingItem("Item 10", false))
//        listData.add(SettingItem("Item 11", true))
//        listData.add(SettingItem("Item 12", false))
//        listData.add(SettingItem("Item 13", true))
//        listData.add(SettingItem("Item 14", false))
//        listData.add(SettingItem("Item 15", true))
//        listData.add(SettingItem("Item 16", false))
//        listData.add(SettingItem("Item 17", true))
//        listData.add(SettingItem("Item 18", false))
//        listData.add(SettingItem("Item 19", true))
//        listData.add(SettingItem("Item 20", false))
//        listData.add(SettingItem("Item 21", true))
//        listData.add(SettingItem("Item 22", false))
//        listData.add(SettingItem("Item 23", true))
//        listData.add(SettingItem("Item 24", false))
//        listData.add(SettingItem("Item 25", true))
//        return listData
//    }

    fun onChooseCheckbox(position: Int, isChecked: Boolean) {

        if (listData.value!![position].isSelected != isChecked) {
            val settingItem = listData.value!![position].let {
                it.copy(name = it.name, isSelected = isChecked, isSelectedEnd = isChecked)
            }
            listData.value!![position] = settingItem
            listData.value = listData.value
        }
    }

    fun onClear(position: Int) {

        listData.value?.removeAt(position)
        listData.value = listData.value
    }
}
