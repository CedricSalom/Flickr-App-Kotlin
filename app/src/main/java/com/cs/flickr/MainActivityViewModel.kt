package com.cs.flickr

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cs.flickr.model.Photo
import com.cs.flickr.model.SearchResult
import com.cs.flickr.repository.Repository
import com.google.firebase.firestore.FirebaseFirestore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// La liste des photos est définie désormais non plus aux fragments mais à l'activité et
// pour une gestion plus simple du filtre
class MainActivityViewModel : ViewModel(){
    var photos = MutableLiveData<ArrayList<Photo>>()
    lateinit var premierePhoto: Photo

    init {
        getPhotos()
    }

    fun getPhotos(){
        val list = ArrayList<Photo>()
            Repository().getPhotos(object : Callback<SearchResult> {

                override fun onResponse(call: Call<SearchResult>, response: Response<SearchResult>) {
                    var premierePh = 0
                    val testResponse  = response.body()?.photos?.photo
                    if(testResponse != null) {
                        response.body()?.photos?.photo!!.forEach {
                            if(premierePh == 0) {
                                premierePhoto = it
                                premierePh = 1
                            }
                            list.add(it)
                        }
                    }
                    photos.value = list
                }

                override fun onFailure(call: Call<SearchResult>, t: Throwable) {
                    Log.v("Error", "Erreur lors de la recherche !")
                }
            })
    }

    fun getPhotosWithFiltre(filtre: Float, usr: String) {
        val list = ArrayList<Photo>()

        val db = FirebaseFirestore.getInstance()
        db.collection("UserRatingImages")
            .whereEqualTo("user", usr)
            .whereGreaterThanOrEqualTo("note", filtre)
            .get()
            .addOnSuccessListener { response ->
                if(response.documents.size> 0) {
                    for(docs in response) {
                        val urlItems = getUrlItems(docs.data["url"] as String)
                        val titre = docs.data["titre"] as String
                        list.add(Photo(urlItems[0], urlItems[1], urlItems[2], urlItems[3], titre))
                    }
                    photos.value = list
                }
            }
    }

    private fun getUrlItems(url: String) : List<String>{
        // Get Farm
        val debutFarm = url.indexOf("m", 0)
        val finFarm = url.indexOf(".", debutFarm)
        val farm = url.substring(debutFarm+1, finFarm)

        // Get Server
        val debutServer = url.indexOf("/", finFarm)
        val finServer = url.indexOf("/", debutServer+1)
        val server = url.substring(debutServer+1, finServer)

        // Get Id
        val debutId = url.indexOf("/", finServer)
        val finId = url.indexOf("_", debutId)
        val id = url.substring(debutId+1, finId)

        // Get Secret
        val debutSecret = url.indexOf("_", finId)
        val finSecret = url.indexOf(".jpg", debutSecret)
        val secret = url.substring(debutSecret+1, finSecret)

        return listOf(id, secret, server, farm)
    }
}