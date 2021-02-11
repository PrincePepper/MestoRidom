package mesto.ridom.mestoridom.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import mesto.ridom.mestoridom.adapters.DisplayPlaceAdapter

class PlacesViewModel : ViewModel() {
    private val places: MutableLiveData<List<DisplayPlaceAdapter.Place>> by lazy {
        MutableLiveData<List<DisplayPlaceAdapter.Place>>().apply {
            value = loadPlaces()
        }
    }

    fun getPlaces(): LiveData<List<DisplayPlaceAdapter.Place>> = places

    private fun loadPlaces(): List<DisplayPlaceAdapter.Place> {
        return listOf(
                DisplayPlaceAdapter.Place("Кофейня 1", 7),
                DisplayPlaceAdapter.Place("yo", 2),
                DisplayPlaceAdapter.Place("yo yo yo yo", 3),
                DisplayPlaceAdapter.Place("yo yo yo yo", 3),
                DisplayPlaceAdapter.Place("yo yo yo yo", 3),
                DisplayPlaceAdapter.Place("yo yo yo yo", 3),
                DisplayPlaceAdapter.Place("yo yo yo yo", 3)
        )
    }
}