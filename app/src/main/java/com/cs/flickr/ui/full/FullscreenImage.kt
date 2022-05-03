package com.cs.flickr.ui.full

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.cs.flickr.MainActivity
import com.cs.flickr.R
import com.google.firebase.firestore.FirebaseFirestore

class FullscreenImage : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_fullscreen_image, container, false)
        val model = ViewModelProvider(this)[FullscreenImageViewModel::class.java]

        val url = FullscreenImageArgs.fromBundle(requireArguments()).url

        val user = (activity as MainActivity).user

        val title = FullscreenImageArgs.fromBundle(requireArguments()).title
        val titreImage = view.findViewById<TextView>(R.id.titre_image_fullscreen)
        titreImage.text = title

        val ratingBar = view.findViewById<RatingBar>(R.id.ratingBar)

        val db = FirebaseFirestore.getInstance()

        db.collection("UserRatingImages")
            .whereEqualTo("url", url)
            .whereEqualTo("user", user)
            .get()
            .addOnSuccessListener { response ->
                for(doc in response) {
                    if(doc.data["url"] == url && doc.data["user"] == user) {
                        ratingBar.rating = doc.data["note"].toString().toFloat()
                        titreImage.text = doc.data["titre"].toString()
                        break
                    }
                }
            }

        val img = view.findViewById<ImageView>(R.id.image_fullscreen)
        Glide.with(requireActivity()).load(url).into(img)

        ratingBar?.setOnRatingBarChangeListener { _, rating, _  ->
            // Enregistrer la valeur du rating sur FireBase
            model.rateImage(user, rating, url, title)
        }

        return view
    }
}