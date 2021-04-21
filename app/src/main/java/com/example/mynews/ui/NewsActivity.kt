package com.example.mynews.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mynews.R
import com.example.mynews.databinding.ActivityNewsBinding
import com.example.mynews.ui.viewmodels.NewsViewModel
import org.koin.android.ext.android.inject

class NewsActivity : AppCompatActivity() {

    lateinit var binding:ActivityNewsBinding
    val newsViewModel:NewsViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_news)

        binding.bottomNav.setupWithNavController(findNavController(R.id.news_nav))

        val navController = findNavController(R.id.news_nav)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id == R.id.articleFragment) {

                binding.bottomNav.visibility = View.GONE
            } else {

                binding.bottomNav.visibility = View.VISIBLE
            }
        }

    }
}