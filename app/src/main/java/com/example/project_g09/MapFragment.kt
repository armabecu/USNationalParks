package com.example.project_g09

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.constraintlayout.helper.widget.MotionEffect.TAG
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.project_g09.databinding.FragmentMapBinding
import com.example.project_g09.models.AllStatesResponse
import com.example.project_g09.models.State
import com.example.project_g09.networking.ApiService
import com.example.project_g09.networking.RetrofitInstance
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch


class MapFragment : Fragment(R.layout.fragment_map),OnMapReadyCallback {

    //Binding Variables
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private val lisOfStates:MutableList<String> = mutableListOf()
    //Spinner  Adapter variable
    private lateinit var spinnerAdapter: ArrayAdapter<String>

    //Google maps variable
    private lateinit var myMap: GoogleMap

    //Get list of states from Data Store Singleton to populate the statelist spinner.
    val statesFromDataStorage = DataStorage.getInstance().stateList
    //List of Coordinates for markers, empty list to be populated all parks from specific state when API is called
    var markerList:MutableList<LatLng> = mutableListOf()
    private lateinit var dataFromAPI: AllStatesResponse

    //Binding logic to use with fragments
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =FragmentMapBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Map fragment variable
        val mapFragment =  childFragmentManager.findFragmentById(binding.mapContainer.id) as? SupportMapFragment


        //Logic to check if map fragment is empty or not
        if (mapFragment == null) {
            Log.d(TAG, "++++ map fragment is null")
        }
        else {
            Log.d(TAG, "++++ map fragment is NOT null")
            // assuming the screen can find the map fragment in the xml file, then
            // connect with Google and get whatever information you need from Google
            // to setup the map
            mapFragment?.getMapAsync(this)
        }


        //Loop through states list from DataStorage and add the full name for each state to listOfStates list.
        for(states in statesFromDataStorage){
            lisOfStates.add(states.stateFullName)
        }

        //Array adapter for spinner that populates it with all states' full names.
        this.spinnerAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item,lisOfStates)
        binding.listOfStatesSpinner.adapter = spinnerAdapter


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //API function to trigger the API query
    private suspend fun getAllStatesFromAPI(statesToSearch:String): AllStatesResponse? {
        //Api Service and Retrofit instance
        var apiService: ApiService = RetrofitInstance.retrofitService
        //Response variable that sets the API key and calls the getStates function from the APIService file.
        val response: retrofit2.Response<AllStatesResponse> = apiService.getStates(statesToSearch.trim(),"FnvA2Hh1uDqFiQqvmAJRPInDrW6a1E94IpBaiOtT")

        //Logic whether there an error or not when doing the API query, otherwise it returns the data.
        if (response.isSuccessful) {
            val dataFromAPI = response.body()
            if (dataFromAPI == null) {
                Log.d("API", "No data from API or some other error")
                return null
            }

            Log.d(TAG, "Here is the data from the API")
            Log.d(TAG, dataFromAPI.toString())
            return dataFromAPI
        }
        else {
            // Handle error
            Log.d(TAG, "An error occurred")
            return null
        }
    }

    //onMapReady function to set Map settings and markers.
    override fun onMapReady(googleMap: GoogleMap) {
        Log.d(TAG, "+++ Map callback is executing...")

        //Set custom adapter for marker's info window
        val adapter = CustomInfoWindowAdapter(requireContext())
        googleMap.setInfoWindowAdapter(adapter)
        // Map initialization
        this.myMap = googleMap

        // Configuring the map's options
        myMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        myMap.isTrafficEnabled = false
        val uiSettings = googleMap.uiSettings
        uiSettings.isZoomControlsEnabled = true
        uiSettings.isCompassEnabled = true

        //Set initial location for the map, in this case it focuses the US map.
        val intialLocation = LatLng(37.0902, -98.7129)
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(intialLocation, 3.4f))

        //Spinner listener when one category is selected.
        binding.findParks.setOnClickListener{
                //Map is clear just in case it contained markers from previous searches.
                 myMap.clear()

                //Get full State name from spinner variable
                val position = binding.listOfStatesSpinner.selectedItemPosition

                //Searches the full name of the state in the list of states from singleton and finds it's shortened code
                var stateToSearch = statesFromDataStorage[position].stateShort

            //Function to move to next fragment
            fun moveToNextFragment(stateInfoToSend:State) {
                val action = MapFragmentDirections.actionMapFragmentToViewParkDetails(stateInfoToSend)
                findNavController().navigate(action)
            }

                //Launching API query

                lifecycleScope.launch {
                    //Call the function getAllStatesFormAPI using the state shortened code variable.
                    var responseFromAPI:AllStatesResponse? = getAllStatesFromAPI(stateToSearch)
                    if (responseFromAPI == null) {
                        return@launch
                    }
                    Log.d(TAG, "Success: Data retrieved from API")
                    Log.d(TAG, responseFromAPI.toString())

                    //Gets response from API and sets all the information to stateParkDataList
                    var stateParkDataList:List<State> = responseFromAPI.data

                    //parkLocation variable
                    var parkLocation:LatLng
                    //Builder variable to be used to focus map on certain state
                    val builder = LatLngBounds.Builder()

                    //State info variable to be used to send data to next fragment
                    var stateInfo:State

                    //Loop through stateParks from the API response and set the markers on the map.
                    for(states in stateParkDataList){
                        //Get lat and long variable for the park
                        val lat = states.latitude.toDouble()
                        val long = states.longitude.toDouble()
                        //Set parLocation variable values
                        parkLocation = LatLng(lat,long)
                        //Add parklocation to markerList.
                        markerList.add(parkLocation)
                        //mark location included in builder.
                        builder.include(parkLocation)
                        //Set address format to be shown in marker
                        var addressForMap = "${states.addresses[0].line1}"
                        var addressForMap2 = "${states.addresses[0].city} , ${states.addresses[0].stateCode}, ${states.addresses[0].postalCode}"
                        //stateInfo = State(states.url,states.fullName,states.description,states.latitude,states.longitude,states.addresses,states.images)
                        //Set marker with the name of the park and the address.
                        myMap.addMarker(
                            MarkerOptions()
                                .position(parkLocation)
                                .title("${states.fullName}")
                                .snippet("${addressForMap}\n${addressForMap2}")

                        )
                        //Set marker listener
                        myMap.setOnInfoWindowClickListener { marker ->
                            // Find the State object associated with the clicked marker
                            val stateInfo = stateParkDataList.find { it.latitude == marker.position.latitude.toString() && it.longitude == marker.position.longitude.toString() }
                            stateInfo?.let {
                                //Call movetoNextFragment function passing through lat and long data to identify which park it was clicked.
                                moveToNextFragment(it)
                            }
                        }

                    }

                    //Variable to focus googleMap camera to focus on builder (focuses on the area of the latest state park search)
                    val bounds = builder.build()
                    val padding = 200
                    val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
                    googleMap.moveCamera(cameraUpdate)

                }

            }

        }

}



