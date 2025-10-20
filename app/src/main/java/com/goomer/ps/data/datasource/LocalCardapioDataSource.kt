package com.goomer.ps.data.datasource

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.goomer.ps.data.dto.MenuItemDto
import com.goomer.ps.domain.exception.LocalDataLoadException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.lang.reflect.Type

class LocalCardapioDataSource(
    private val context: Context,
) {
    private val gson = Gson()
    private val menuItemListType: Type = object : TypeToken<List<MenuItemDto>>() {}.type

    suspend fun loadMenuItems(): List<MenuItemDto> =
        withContext(Dispatchers.IO) {
            try {
                val json = readAsset("menu.json")
                gson.fromJson<List<MenuItemDto>>(json, menuItemListType) ?: emptyList()
            } catch (e: IOException) {
                throw LocalDataLoadException("Erro ao carregar itens do menu: ${e.message}", e)
            } catch (e: JsonSyntaxException) {
                throw LocalDataLoadException("Erro ao processar dados do menu: ${e.message}", e)
            }
        }

    private fun readAsset(fileName: String): String =
        context.assets.open(fileName).use { inputStream ->
            inputStream.bufferedReader().use { reader ->
                reader.readText()
            }
        }
}
