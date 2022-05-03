package com.cs.flickr

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    lateinit var user: String
    lateinit var model: MainActivityViewModel

    // Choose authentication providers
    private val providers = arrayListOf(
        AuthUI.IdpConfig.EmailBuilder().build(),
        AuthUI.IdpConfig.GoogleBuilder().build())

    private val signInLauncher =
        registerForActivityResult(FirebaseAuthUIActivityResultContract())
    { res ->
        this.onSignInResult(res)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProvider(this)[MainActivityViewModel::class.java]

        // Create and launch sign-in intent
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        signInLauncher.launch(signInIntent)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            user = FirebaseAuth.getInstance().currentUser!!.uid
            setContentView(R.layout.activity_main)
            setSupportActionBar(findViewById(R.id.my_toolbar))
            Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show()
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            Toast.makeText(this, "Not Connected", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.title) {
            "Filtre" -> {
                showPopUpFiltre()
            }
            "mon_compte" -> {
                showPopUpCompte()
            }
            else -> {
                println("Erreur de sélection dans le menu")
            }
        }
        return true
    }

    // Boite de dialogue : Filtre
    private fun showPopUpFiltre() {
        val builder = android.app.AlertDialog.Builder(this)
        val customView = layoutInflater.inflate(R.layout.filtre_pop_up, null)
        val ratingFiltre = customView.findViewById<RatingBar>(R.id.ratingFiltre)

        builder.setView(customView)
        builder.setTitle(R.string.titre_filtre)
        builder.setIcon(R.drawable.iconfiltre)

        builder.setPositiveButton("Réinitialiser le filtre"){ _, _ ->
            model.getPhotos()
        }

        val alertDialog = builder.create()
        alertDialog.show()
        alertDialog.window?.setLayout(980, 635)

        ratingFiltre.setOnRatingBarChangeListener { _, rating, _ ->
            model.getPhotosWithFiltre(rating, user)
            alertDialog.dismiss()
        }
    }

    // Boite de dialogue : Compte
    private fun showPopUpCompte() {
        val builder = android.app.AlertDialog.Builder(this)
        val customView = layoutInflater.inflate(R.layout.compte_pop_up, null)

        val nom = customView.findViewById<TextView>(R.id.nom)
        val email = customView.findViewById<TextView>(R.id.email)

        nom.text = FirebaseAuth.getInstance().currentUser!!.displayName
        email.text = FirebaseAuth.getInstance().currentUser!!.email

        builder.setView(customView)
        builder.setTitle(R.string.titre_compte)
        builder.setIcon(R.drawable.user)

        builder.setNegativeButton("Deconnexion"){ _, _ ->
            FirebaseAuth.getInstance().signOut()
            finish()
            startActivity(intent)
        }

        val alertDialog = builder.create()
        alertDialog.show()
        alertDialog.window?.setLayout(1000, 840)
    }
}