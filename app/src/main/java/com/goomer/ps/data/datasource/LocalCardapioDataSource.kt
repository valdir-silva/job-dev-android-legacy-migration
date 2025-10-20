package com.goomer.ps.data.datasource

import android.content.Context
import com.goomer.ps.data.dto.MenuItemDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.reflect.Type

class LocalCardapioDataSource(
    private val context: Context
) {
    
    private val gson = Gson()
    private val menuItemListType: Type = object : TypeToken<List<MenuItemDto>>() {}.type

    suspend fun loadMenuItems(): List<MenuItemDto> = withContext(Dispatchers.IO) {
        try {
            val json = readAsset("menu.json")
            gson.fromJson<List<MenuItemDto>>(json, menuItemListType) ?: emptyList()
        } catch (e: Exception) {
            throw Exception("Erro ao carregar itens do menu: ${e.message}", e)
        }
    }

    private fun readAsset(fileName: String): String {
        return context.assets.open(fileName).use { inputStream ->
            inputStream.bufferedReader().use { reader ->
                reader.readText()
            }
        }
    }
}
