package com.cs.flickr.ui.liste

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cs.flickr.model.Photo

class ListViewModel(var listePhotos : MutableLiveData<ArrayList<Photo>>) : ViewModel()