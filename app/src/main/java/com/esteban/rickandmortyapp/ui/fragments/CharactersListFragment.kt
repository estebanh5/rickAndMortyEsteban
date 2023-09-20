package com.esteban.rickandmortyapp.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController

import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import com.esteban.rickandmortyapp.R
import com.esteban.rickandmortyapp.RickAndMortyApplication
import com.esteban.rickandmortyapp.adapters.CharactersAdapter
import com.esteban.rickandmortyapp.dagger.DaggerRickMortyComponent
import com.esteban.rickandmortyapp.ui.CharactersActivity
import com.esteban.rickandmortyapp.ui.viewmodel.CharactersViewModel
import com.esteban.rickandmortyapp.util.Constants.Companion.QUERY_PAGE_SIZE
import com.esteban.rickandmortyapp.util.Resource
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.android.synthetic.main.fragment_characters_list.*
import javax.inject.Inject

class CharactersListFragment : Fragment(R.layout.fragment_characters_list) {


    lateinit var viewModel : CharactersViewModel

    @Inject lateinit var charactersAdapter: CharactersAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity?.application as RickAndMortyApplication).rickMortyComponent.inject(this)

        activity?.actionBar?.setDisplayHomeAsUpEnabled(false)
        viewModel = (activity as CharactersActivity).viewModel
        setUpRecyclerView()

        (activity as CharactersActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (activity as CharactersActivity).supportActionBar?.title = getString(R.string.home)


        charactersAdapter.setOnClickListener {
            view, character ->

            val bundle = Bundle().apply {
                putSerializable("character", character)

            }
            val extras = FragmentNavigator.Extras.Builder().addSharedElements(mapOf(view.findViewById<ShapeableImageView>(R.id.ivCharacterImage) to "characterContainer")).build()


            findNavController().navigate(R.id.action_charactersListFragment_to_displayCharacterFragment, bundle, null, extras)
        }


        etCharacters.setOnClickListener {
            findNavController().navigate(R.id.action_charactersListFragment_to_searchCharacters)
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
        //paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        //paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false


    private fun setUpRecyclerView() {

        rvCharacters.apply {
            adapter = charactersAdapter
            layoutManager = com.jackandphantom.carouselrecyclerview.CarouselLayoutManager(isLoop = true, isItem3D= true, ratio= 0.7f, flat =true, alpha=true, isScrollingEnabled = true, HORIZONTAL,)
            set3DItem(true)
            setAlpha(true)
            setInfinite(true)

        }
    }
}