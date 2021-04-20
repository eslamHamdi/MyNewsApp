package com.example.mynews.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mynews.R
import com.example.mynews.adapters.NewsAdapter
import com.example.mynews.databinding.FragmentSavedArticlesBinding
import com.example.mynews.domain.Article
import com.example.mynews.ui.viewmodels.NewsViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class SavedArticlesFragment : Fragment() ,NewsAdapter.OnArticleClick{
lateinit var binding:FragmentSavedArticlesBinding
val viewModel:NewsViewModel by sharedViewModel()
    private val adapter = NewsAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_saved_articles,container,false)

        adapter.articleClickListener = this

        viewModel.savedNews.observe(viewLifecycleOwner,{
            adapter.submitList(it)
        })

        binding.savedRecycler.adapter = adapter

        return binding.root
    }

    override fun clickArticle(article: Article) {
        findNavController().navigate(SavedArticlesFragmentDirections.actionSavedArticlesFragmentToArticleFragment(article))
    }


}