package com.esteban.rickandmortyapp.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.esteban.rickandmortyapp.R
import com.esteban.rickandmortyapp.adapters.CharacterSearchAdapter
import com.esteban.rickandmortyapp.ui.CharactersActivity
import com.esteban.rickandmortyapp.ui.viewmodel.CharactersViewModel
import com.esteban.rickandmortyapp.util.Constants
import com.esteban.rickandmortyapp.util.Constants.Companion.SEARCH_CHARACTERS_TIME_DELAY
import com.esteban.rickandmortyapp.util.Resource
import kotlinx.android.synthetic.main.fragment_search_character.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchCharactersFragment : Fragment(R.layout.fragment_search_character) {


    lateinit var viewModel: CharactersViewModel
    lateinit var charactersAdapter: CharacterSearchAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as CharactersActivity).viewModel

        charactersAdapter = CharacterSearchAdapter()

        setUpRecyclerView()


        (activity as CharactersActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as CharactersActivity).supportActionBar?.title = getString(R.string.search)

        viewModel.searchCharacters("")


        var job: Job? = null
        etSearch.addTextChangedListener { editable ->

            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_CHARACTERS_TIME_DELAY)

                editable?.let {
                    if(editable.toString().isNotEmpty()) {
                        viewModel.searchCharacters(editable.toString())
                    }
                }

            }

        }

        charactersAdapter.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable("character", it)
            }
            findNavController().navigate(R.id.action_searchCharacters_to_displayCharacter,
                bundle)
        }


        viewModel.searchCharacters.observe(viewLifecycleOwner) { response ->

            when(response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { charactersResponse ->

                        charactersAdapter.differ.submitList(charactersResponse.results.toList())
                        val totalPages  = charactersResponse.info.count / Constants.QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.searchCharactersPage == totalPages

                        if(isLastPage) {
                            rvSearchCharacters.setPadding(0,0,0,0)
                        }

                    }
                }

                is Resource.Error -> {
                    hideProgressBar()

                    response.message?.let { message ->
                        Toast.makeText(activity, "Error occurred: $message", Toast.LENGTH_LONG).show()

                    }
                }


                is Resource.Loading -> {
                    showProgressBar()
                }


            }

        }
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    private val scrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager

            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling


            if(shouldPaginate) {
                viewModel.searchCharacters(etSearch.text.toString())
                isScrolling = false
            }

        }


        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }


    }


    private fun setUpRecyclerView() {
        rvSearchCharacters.apply {
            adapter = charactersAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(scrollListener)
        }
    }



    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }


    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }
}