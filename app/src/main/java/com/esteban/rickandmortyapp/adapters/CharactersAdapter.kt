package com.esteban.rickandmortyapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.esteban.rickandmortyapp.R
import com.esteban.rickandmortyapp.models.RickAndMortyCharacter
import kotlinx.android.synthetic.main.fragment_display_character.*
import kotlinx.android.synthetic.main.item_character_preview.view.*
import javax.inject.Inject

class CharactersAdapter @Inject constructor():
    RecyclerView.Adapter<CharactersAdapter.CharacterViewHolder>() {


    inner class CharacterViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)


    private val differCallback = object : DiffUtil.ItemCallback<RickAndMortyCharacter> () {
        override fun areItemsTheSame(oldItem: RickAndMortyCharacter, newItem: RickAndMortyCharacter): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: RickAndMortyCharacter, newItem: RickAndMortyCharacter): Boolean {
            return oldItem == newItem
        }

    }


    val differ = AsyncListDiffer(this, differCallback)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        return CharacterViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_character_preview, parent,
                false),

            )
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(character.image).into(ivCharacterImage)
            tvName.text = character.name
            chipOrigin.text = character.origin.name
            chipLocation.text = character.location.name
            chipType.text = character.type

            if(character?.type?.isNotEmpty()!!) {
                chipType.text = character.type
            }else {
                chipType.visibility = View.INVISIBLE
            }
            setOnClickListener {
                onItemClickListener?.let { it(holder.itemView, character) }
            }
        }
    }



    private var onItemClickListener: ((View, RickAndMortyCharacter) -> Unit)? = null

    fun setOnClickListener(listener: (View, RickAndMortyCharacter) -> Unit) {
        onItemClickListener = listener
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }



}