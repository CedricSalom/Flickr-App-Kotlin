package com.cs.flickr.ui.liste

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cs.flickr.model.Photo
import java.util.ArrayList

// Classe servant à passer un parametre supplémentaire au viewModel
class ListViewModelFactory(private val listePhotos: MutableLiveData<ArrayList<Photo>>) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ListViewModel(listePhotos) as T
    }
}