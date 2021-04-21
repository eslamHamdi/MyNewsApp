package com.example.mynews.ui

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
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

        val actionbar = supportActionBar
        val appBarConfiguration = AppBarConfiguration.Builder(setOf(R.id.newsListFragment,R.id.savedArticlesFragment,R.id.searchFragment)).build()
        setupActionBarWithNavController(navController,appBarConfiguration)
    }

    //hiding keyboard if edit texts out of focus
    override fun onUserInteraction()
    {
        super.onUserInteraction()
        if (currentFocus != null)
        {
            val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }

//    override fun onSupportNavigateUp(): Boolean {
//        return super.onSupportNavigateUp()
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}