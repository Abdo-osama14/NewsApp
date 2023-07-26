package com.example.newsapp.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.presentation.MainActivity
import com.example.newsapp.presentation.NewsAdapter
import com.example.newsapp.presentation.NewsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_saved_news.*

class SavedNewsFragment :Fragment(R.layout.fragment_saved_news){
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    viewModel=(activity as MainActivity).viewModel
        setupViewAdapter()
        newsAdapter.setOnItemClickListener {
            changeFragment(ArticleFragment.start(it))
        }

        viewModel.getFavArticles().observe(viewLifecycleOwner, Observer {
            newsAdapter.differ.submitList(it)
        })


        val itemHelperCallBack= object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position=viewHolder.adapterPosition
                val article=newsAdapter.differ.currentList[position]
                viewModel.deleteArticle(article)
                Snackbar.make(view,"the article is delete", Snackbar.LENGTH_LONG).apply {
                    setAction("undo"){
                        viewModel.addToFav(article)
                    }
                    show()
                }
            }
        }
        ItemTouchHelper(itemHelperCallBack).apply {
            attachToRecyclerView(rvSavedNews)
        }

    }
    private fun setupViewAdapter(){
        newsAdapter= NewsAdapter()
        rvSavedNews.apply {
            adapter=newsAdapter
            layoutManager= LinearLayoutManager(activity)
        }
    }
    private fun changeFragment(fragment: Fragment){
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.flFragment, fragment)
            addToBackStack(fragment.javaClass.name)
            commit()
        }
    }

}