package com.example.newsapp.presentation.fragments

import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.presentation.MainActivity
import com.example.newsapp.presentation.NewsAdapter
import com.example.newsapp.presentation.NewsViewModel
import com.example.newsapp.utill.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_breaking_news.*

class BreakingNewsFragment:Fragment(R.layout.fragment_breaking_news) {

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel=(activity as MainActivity).viewModel
        setupViewAdapter()


        viewModel.breakingNews.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading
                -> {
                    showLoading()
                }
                is Resource.Success
                -> {
                    hideLoading()
                    newsAdapter.differ.submitList(it.data?.articles!!.toList())
                    val totalPages = it.data?.totalResults!! / 20 + 2
                    isLastPage = viewModel.breakingNewsPage == totalPages
                    if(isLastPage){
                        rvBreakingNews.setPadding(0,0,0,0)
                    }

                }
                is Resource.Error -> {
                    hideLoading()
                    Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
                }
            }

        })

        newsAdapter.setOnItemClickListener {
            changeFragment( ArticleFragment.start(it))
        }


    }
    private fun hideLoading(){
        paginationProgressBar.isVisible=false
        isLoading = false
    }
    private fun showLoading(){
        paginationProgressBar.isVisible=true
        isLoading = true
    }


    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollListener = object : RecyclerView.OnScrollListener(){

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }

        }


        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible =totalItemCount >= 20
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling

            if (shouldPaginate){
                viewModel.getBreakingNews("eg")
                isScrolling = false
            }
        }
    }



    private fun setupViewAdapter() {
        newsAdapter = NewsAdapter()
        rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@BreakingNewsFragment.scrollListener)

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