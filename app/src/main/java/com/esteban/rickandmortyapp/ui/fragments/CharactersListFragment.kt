package com.esteban.rickandmortyapp.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.esteban.rickandmortyapp.R
import com.esteban.rickandmortyapp.adapters.CharactersAdapter
import com.esteban.rickandmortyapp.ui.CharactersActivity
import com.esteban.rickandmortyapp.ui.viewmodel.CharactersViewModel
import com.esteban.rickandmortyapp.util.Constants.Companion.QUERY_PAGE_SIZE
import com.esteban.rickandmortyapp.util.Resource
import kotlinx.android.synthetic.main.fragment_characters_list.*

class CharactersListFragment : Fragment(R.layout.fragment_characters_list) {


    lateinit var viewModel : CharactersViewModel

    lateinit var charactersAdapter: CharactersAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.actionBar?.setDisplayHomeAsUpEnabled(false)
        viewModel = (activity as CharactersActivity).viewModel
        charactersAdapter = CharactersAdapter()
        setUpRecyclerView()

        charactersAdapter.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable("character", it)
            }
            findNavController().navigate(R.id.action_charactersListFragment_to_displayCharacterFragment, bundle)
        }


        viewModel.characters.observe(viewLifecycleOwner) { response ->

            when(response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { charactersResult ->

                        charactersAdapter.differ.submitList(charactersResult.results.toList())
                        val totalPages  = charactersResult.info.count / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.charactersPage == totalPages

                        if(isLastPage) {
                            rvCharacters.setPadding(0,0,0,0)
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


                else -> {}
            }

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
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling


            if(shouldPaginate) {
                viewModel.getCharacters()
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
        rvCharacters.apply {
            adapter = charactersAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(scrollListener)
        }
    }
}