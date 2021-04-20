package com.example.mynews.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.mynews.R
import com.example.mynews.databinding.FragmentArticleBinding


class ArticleFragment : Fragment() {

    lateinit var binding: FragmentArticleBinding
   val args:ArticleFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

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

        return binding.root
    }


}