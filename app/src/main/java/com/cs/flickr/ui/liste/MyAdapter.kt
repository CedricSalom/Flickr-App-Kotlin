package com.cs.flickr.ui.liste

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cs.flickr.R
import com.cs.flickr.model.Photo

class MyAdapter(private val photos : List<Photo>, val callback: (Int) -> Unit) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {
     // un ViewHolder permet de stocker la vue de chaque item de la liste

    class ViewHolder(itemView: LinearLayout) : RecyclerView.ViewHolder(itemView)

     // appelé quand le ViewHolder doit être créé (probablement parce que l'item devient visible)
     // on crée (inflate) le layout "user" et on le place dans le ViewHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

      // Inflate the custom layout
        val view = LayoutInflater.from(parent.context).inflate(R.layout.content_item_list, parent, false)
        val holder = ViewHolder(view as LinearLayout)
        view.setOnClickListener {
            callback(holder.adapterPosition)
        }
        return holder
    }

     // appelé quand le recycerview a besoin de connaître la taille de la liste qu'il doit afficher
     override fun getItemCount(): Int = photos.size


     // appelé quand on doit peupler le ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

         val imgV : ImageView  = holder.itemView.findViewById(R.id.imageDeLaListe)
         val elemPhoto = photos[position]

         val url = "https://farm" + elemPhoto.farm + ".staticflickr.com/" + elemPhoto.server + "/" + elemPhoto.id+"_"+ elemPhoto.secret + ".jpg"

         Glide.with(holder.itemView).load(url).into(imgV)
    }
}