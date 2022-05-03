package com.cs.flickr.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.cs.flickr.MainActivity
import com.cs.flickr.R
import com.cs.flickr.model.Photo


class MainFragment : Fragment() {

    private lateinit var viewModelFactory: MainViewModelFactory
    private var lesPhotos = MutableLiveData<ArrayList<Photo>>()
    private lateinit var laPhotoEnCours : Photo
    private lateinit var viewModel : MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        val layout = inflater.inflate(R.layout.main_fragment, container, false)

        laPhotoEnCours = (activity as MainActivity).model.premierePhoto
        lesPhotos = (activity as MainActivity).model.photos
        viewModelFactory = MainViewModelFactory(laPhotoEnCours, lesPhotos)
        viewModel = ViewModelProvider(this,viewModelFactory)[MainViewModel::class.java]

        val titreImage = layout.findViewById<TextView>(R.id.titre_image)
        val image = layout.findViewById<ImageView>(R.id.image)

        val btnNext = layout.findViewById<Button>(R.id.button_nextImage)
        btnNext.setOnClickListener {
            viewModel.nextPhoto()
        }

        viewModel.listePhotos.observe(requireActivity(), { listePhotos ->
            viewModel.currentPhoto.value = listePhotos[0]
            viewModel.position = 0
        })

        viewModel.currentPhoto.observe(requireActivity(), {
            titreImage.text = it.title
            val url = "https://farm" + it.farm + ".staticflickr.com/" + it.server + "/" + it.id+"_"+ it.secret + ".jpg"
            Glide.with(requireActivity()).load(url).into(image)
        })

        val btnAllIm = layout.findViewById<Button>(R.id.button_allImages)
        btnAllIm.setOnClickListener {
             Navigation.findNavController(it).navigate(R.id.versListFragment)
        }

        image.setOnClickListener{
            val cPh = viewModel.currentPhoto.value
            val url = "https://farm" + cPh?.farm + ".staticflickr.com/" + cPh?.server + "/" + cPh?.id+"_"+ cPh?.secret + ".jpg"
            val action = MainFragmentDirections.versFullscreenImage(url, cPh!!.title)
            findNavController().navigate(action)
        }

        return layout
    }
}