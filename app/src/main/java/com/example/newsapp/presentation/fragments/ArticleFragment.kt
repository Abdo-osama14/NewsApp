package com.example.newsapp.presentation.fragments

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.example.newsapp.R
import com.example.newsapp.domain.modles.Article
import com.example.newsapp.presentation.MainActivity
import com.example.newsapp.presentation.NewsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_article.*

class ArticleFragment:Fragment(R.layout.fragment_article) {
    lateinit var viewModel: NewsViewModel
    companion object{
        fun start(article: Article)=ArticleFragment().apply {
            arguments= Bundle().apply {
                putSerializable("article",article)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel=(activity as MainActivity).viewModel
        val args=this.arguments
        val selectedArticle:Article= args?.get("article") as Article
        webView.apply {
            webViewClient= WebViewClient()
            selectedArticle.url?.let { loadUrl(it) }
        }
        fab.setOnClickListener {
            viewModel.addToFav(selectedArticle)
            Snackbar.make(view,"the Article saved successfully", Snackbar.LENGTH_SHORT).show()
        }
    }

}