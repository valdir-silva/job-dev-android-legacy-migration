package com.goomer.ps.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goomer.ps.domain.model.CardapioResult
import com.goomer.ps.domain.model.MenuItem
import com.goomer.ps.domain.usecase.GetMenuItemsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val SAVED_STATE_KEY = "menu_items"

class MenuListViewModel(
    private val getMenuItemsUseCase: GetMenuItemsUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _uiState = MutableStateFlow<CardapioResult<List<MenuItem>>>(CardapioResult.Loading())
    val uiState: StateFlow<CardapioResult<List<MenuItem>>> = _uiState.asStateFlow()

    init {
        val savedData = savedStateHandle.get<List<MenuItem>>(SAVED_STATE_KEY)
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
                    savedStateHandle[SAVED_STATE_KEY] = result.value
                }
            }
        }
    }

    fun retry() {
        _uiState.value = CardapioResult.Loading()
        loadMenu()
    }
}
