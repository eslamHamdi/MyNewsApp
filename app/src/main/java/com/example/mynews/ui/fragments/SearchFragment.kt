package com.example.mynews.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mynews.R
import com.example.mynews.adapters.NewsAdapter
import com.example.mynews.databinding.FragmentSearchBinding
import com.example.mynews.domain.Article
import com.example.mynews.ui.viewmodels.NewsViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class SearchFragment : Fragment(),NewsAdapter.OnArticleClick {

    val viewModel:NewsViewModel by sharedViewModel()
    lateinit var binding:FragmentSearchBinding
    val adapter = NewsAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_search, container, false)
        binding.searchRecycler.adapter = adapter
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        adapter.articleClickListener = this
        viewModel.searchNews.observe(viewLifecycleOwner,{
            adapter.submitList(it)
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var job: Job? = null
        binding.searchBar.addTextChangedListener {
            job?.cancel()
            job = MainScope().launch {
                delay(2000)
                it?.let {
                    if(it.toString().isNotEmpty()) {
                        viewModel.newsSearch(it.toString())
                    }
                }
            }
        }

        }

    override fun clickArticle(article: Article) {
        findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToArticleFragment(article))
    }
}

