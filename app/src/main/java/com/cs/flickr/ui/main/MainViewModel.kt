package com.cs.flickr.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cs.flickr.model.Photo

class MainViewModel(var photo: Photo, var listePhotos: MutableLiveData<ArrayList<Photo>>) : ViewModel() {

    var position : Int
    var currentPhoto = MutableLiveData<Photo>()

    init {
        currentPhoto.value = photo
        position = 0
    }

    fun nextPhoto() {
        if (listePhotos.value?.size != null) {
            if (position == listePhotos.value?.size!! -1)  {
                position = 0
            }
            else {
                position += 1
            }
        }
        currentPhoto.value = listePhotos.value?.get(position)
    }

}