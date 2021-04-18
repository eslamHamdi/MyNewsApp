package com.example.mynews

import android.app.Application
import androidx.room.Room
import com.example.mynews.api.NewsClient
import com.example.mynews.api.Service
import com.example.mynews.database.LocalDataBase
import com.example.mynews.repository.DataSource
import com.example.mynews.repository.NewsRepository
import com.example.mynews.ui.viewmodels.NewsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MyNewsApp : Application() {

    override fun onCreate() {
        super.onCreate()

val myModule = module {

    viewModel {
        NewsViewModel(repo = get() as DataSource)
    }

    single<DataSource> { NewsRepository(dao=get(),NewService = get())}

    single { Room.databaseBuilder(this@MyNewsApp,LocalDataBase::class.java,"NewsDataBase").build().getDao()}

    single { NewsClient.get(this@MyNewsApp).create(Service::class.java) }
}








        startKoin {
            androidContext(this@MyNewsApp)
            modules(listOf(myModule))
        }
    }
}