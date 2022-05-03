package com.cs.flickr.ui.full

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class FullscreenImageViewModel : ViewModel() {

    fun rateImage(user: String, note: Float, urlImg: String, titre: String) {
        val contentToSave = hashMapOf(
            "user" to user,
            "url" to urlImg,
            "note" to note,
            "titre" to titre
        )

        val db = FirebaseFirestore.getInstance()

        db.collection("UserRatingImages")
            .whereEqualTo("url", urlImg)
            .whereEqualTo("user", user)
            .get()
            .addOnSuccessListener { response ->
                if(response.size() > 0) { // Existe deja => Update
                    for(document in response){
                        db.collection("UserRatingImages")
                            .document(document.id)
                            .update("note", note)
                    }
                }
                else { // N'existe pas => Add
                    db.collection("UserRatingImages")
                        .add(contentToSave)
                }
            }
    }
}