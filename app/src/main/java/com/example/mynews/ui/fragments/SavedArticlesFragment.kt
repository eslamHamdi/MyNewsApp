package com.example.mynews.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.mynews.R
import com.example.mynews.adapters.NewsAdapter
import com.example.mynews.databinding.FragmentSavedArticlesBinding
import com.example.mynews.domain.Article
import com.example.mynews.ui.viewmodels.NewsViewModel
import com.example.mynews.utils.observeInLifecycle
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class SavedArticlesFragment : Fragment() ,NewsAdapter.OnArticleClick{
lateinit var binding:FragmentSavedArticlesBinding
val viewModel:NewsViewModel by sharedViewModel()
    private val adapter = NewsAdapter()

    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        viewModel.toastFlow.onEach {
            Toast.makeText(this.requireContext(), it, Toast.LENGTH_SHORT).show()
        }.observeInLifecycle(this)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_saved_articles,container,false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        adapter.articleClickListener = this
        binding.savedRecycler.adapter = adapter
        val pager = PagerSnapHelper()
        pager.attachToRecyclerView(binding.savedRecycler)


        viewModel.returnSavedArticles().observe(viewLifecycleOwner,{
            binding.savedProgress.visibility = View.VISIBLE
            viewModel.noData.value = it.isNullOrEmpty()
            adapter.submitList(it)
            binding.savedProgress.visibility= View.GONE


        })


        return binding.root
    }

    override fun clickArticle(article: Article) {
        findNavController().navigate(SavedArticlesFragmentDirections.actionSavedArticlesFragmentToArticleFragment(article))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = adapter.currentList[position]
                article.url?.let { viewModel.deleteArticle(it) }
                Snackbar.make(view, "Successfully deleted article", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        viewModel.addToFavorites(article)
                    }
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.savedRecycler)
        }



    }

    }

