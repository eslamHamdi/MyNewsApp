package com.example.mynews.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.mynews.R
import com.example.mynews.adapters.NewsAdapter
import com.example.mynews.databinding.FragmentSearchBinding
import com.example.mynews.domain.Article
import com.example.mynews.ui.viewmodels.NewsViewModel
import com.example.mynews.utils.observeInLifecycle
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class SearchFragment : Fragment(),NewsAdapter.OnArticleClick {

    val viewModel:NewsViewModel by sharedViewModel()
    lateinit var binding:FragmentSearchBinding
   lateinit var adapter :NewsAdapter

    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        viewModel.toastFlow.onEach {
            Toast.makeText(this.requireContext(), it, Toast.LENGTH_SHORT).show()
        }.observeInLifecycle(this)

    }


    @SuppressLint("NewApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_search, container, false)


        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        adapter = NewsAdapter()
        binding.searchRecycler.adapter = adapter
        val pager = PagerSnapHelper()
        pager.attachToRecyclerView(binding.searchRecycler)

        viewModel.searchNews.observe(viewLifecycleOwner,{
            adapter = NewsAdapter()
            binding.searchRecycler.adapter = adapter
            validateNetowrkAndList(it,this.requireContext())
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var job: Job? = null
        binding.searchBar.addTextChangedListener {
            job?.cancel()
            resetSearchList()
            job = MainScope().launch {
                delay(1000)
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


    @SuppressLint("NewApi")
    fun validateNetowrkAndList(list:List<Article>,context: Context)
    {
        if (list.isNullOrEmpty())
        {
           binding.noNetwork.visibility = View.VISIBLE
            binding.executePendingBindings()

        }else
        {
            binding.noNetwork.visibility = View.GONE

            adapter.submitList(list)
            adapter.articleClickListener = this
        }

    }

    fun resetSearchList(){
        if ( binding.searchBar.editableText.isEmpty() ||  binding.searchBar.editableText.isBlank())
        {
            viewModel.searchNews.value =listOf()
        }
    }


}

