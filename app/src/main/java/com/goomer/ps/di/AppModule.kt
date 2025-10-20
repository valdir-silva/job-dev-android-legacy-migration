package com.goomer.ps.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.koin.dsl.module

val appModule =
    module {
        single<Gson> {
            GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .setLenient()
                .create()
        }
    }
