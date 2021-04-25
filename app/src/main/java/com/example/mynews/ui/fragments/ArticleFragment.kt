package com.example.mynews.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.mynews.R
import com.example.mynews.databinding.FragmentArticleBinding
import com.example.mynews.ui.viewmodels.NewsViewModel
import com.example.mynews.utils.observeInLifecycle
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class ArticleFragment : Fragment() {

    lateinit var binding: FragmentArticleBinding
   val args:ArticleFragmentArgs by navArgs()
    val viewModel:NewsViewModel by sharedViewModel()


    @InternalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_article,container,false)
        binding.itemArticle = args.article
        binding.webView.apply {
            webViewClient = WebViewClient()
            args.article.url?.let { loadUrl(it) }
        }

        binding.fab.setOnClickListener {
            viewModel.addToFavorites(args.article)
        }

        //single event
        viewModel.toastFlow.onEach {
            Toast.makeText(this.requireContext(), it, Toast.LENGTH_SHORT).show()
        }.observeInLifecycle(this)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(false)

    }




}