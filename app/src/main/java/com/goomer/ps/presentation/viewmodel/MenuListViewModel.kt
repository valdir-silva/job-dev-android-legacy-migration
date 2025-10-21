package com.goomer.ps.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goomer.ps.R
import com.goomer.ps.domain.exception.ErrorCode
import com.goomer.ps.domain.model.CardapioResult
import com.goomer.ps.domain.model.MenuItem
import com.goomer.ps.domain.usecase.GetMenuItemsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MenuListViewModel(
    private val getMenuItemsUseCase: GetMenuItemsUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val application: Application,
) : ViewModel() {
    private val _uiState = MutableStateFlow<CardapioResult<List<MenuItem>>>(CardapioResult.Loading())
    val uiState: StateFlow<CardapioResult<List<MenuItem>>> = _uiState.asStateFlow()

    private val savedStateKey = application.getString(R.string.saved_state_menu_items)

    init {
        val savedData = savedStateHandle.get<List<MenuItem>>(savedStateKey)
        if (savedData != null) {
            _uiState.value = CardapioResult.Success(savedData)
        }
    }

    fun loadMenu() {
        if (_uiState.value is CardapioResult.Success) return

        viewModelScope.launch {
            getMenuItemsUseCase().collect { result ->
                _uiState.value = result
                if (result is CardapioResult.Success) {
                    savedStateHandle[savedStateKey] = result.value
                }
            }
        }
    }

    fun retry() {
        _uiState.value = CardapioResult.Loading()
        loadMenu()
    }

    fun getErrorMessage(throwable: Throwable?): String =
        when (throwable?.message) {
            ErrorCode.INVALID_ID.name -> application.getString(R.string.error_invalid_id, 0)
            ErrorCode.ITEM_NOT_FOUND.name -> application.getString(R.string.error_item_not_found, 0)
            else -> application.getString(R.string.error_load_menu)
        }
}
