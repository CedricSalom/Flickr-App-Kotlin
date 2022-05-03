package com.cs.flickr.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cs.flickr.model.Photo

// Classe servant à passer un parametre supplémentaire au viewModel
class MainViewModelFactory(private val photo: Photo, private val listePhotos : MutableLiveData<ArrayList<Photo>>) : ViewModelProvider.Factory {
   override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(photo, listePhotos) as T
    }
}