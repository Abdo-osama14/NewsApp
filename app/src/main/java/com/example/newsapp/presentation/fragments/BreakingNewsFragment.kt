package com.example.newsapp.presentation.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
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

        viewModel.getBreakingNews("us")
        viewModel.breakingNews.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading
                -> {
                    showLoading()
                }
                is Resource.Success
                -> {
                    hideLoading()
                    newsAdapter.differ.submitList(it.data?.articles)
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
    }
    private fun showLoading(){
        paginationProgressBar.isVisible=true
    }
    private fun setupViewAdapter() {
        newsAdapter = NewsAdapter()
        rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)

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