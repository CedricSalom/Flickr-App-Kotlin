package com.cs.flickr.ui.liste

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cs.flickr.MainActivity
import com.cs.flickr.R
import com.cs.flickr.model.Photo
import me.zhanghai.android.fastscroll.FastScrollerBuilder

class ListFragment : Fragment() {

    private lateinit var viewModelFactory: ListViewModelFactory
    private lateinit var activityModelPhotos : MutableLiveData<ArrayList<Photo>>
    private lateinit var viewModel: ListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater.inflate(R.layout.list_fragment, container, false)

        activityModelPhotos = (activity as MainActivity).model.photos
        viewModelFactory = ListViewModelFactory(activityModelPhotos)
        viewModel = ViewModelProvider(this,viewModelFactory)[ListViewModel::class.java]

        // Verifie que le fragment est toujours attaché à l'activité
        // Si non, l'app plante
        if(isAdded) {
            val recycler = layout.findViewById<RecyclerView>(R.id.listeImages)
            recycler.layoutManager = GridLayoutManager(requireActivity(), 3)

            // Sert a mieux gérer l'affichage de la grille
            // Voir la classe MarginItemDecoration
            recycler.addItemDecoration(MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.margin)))

            // Ajout d'une scrollbar personnalisée car bug au moment du défilement dans la grille
            // -> Bug fixé avec définition de la taille dans le layout de chaque item de la grille
            FastScrollerBuilder(recycler).build()

            viewModel.listePhotos.observe(requireActivity(), { listePhoto ->
                recycler.adapter = MyAdapter(listePhoto){ positionPhoto -> // click sur une image
                    val photo = listePhoto[positionPhoto]
                    val url = "https://farm" + photo.farm + ".staticflickr.com/" + photo.server + "/" + photo.id+"_"+ photo.secret + ".jpg"
                    val action = ListFragmentDirections.listFragmentToFullscreenImage(url, photo.title)
                    findNavController().navigate(action)
                }
            })
        }
        return layout
    }
}