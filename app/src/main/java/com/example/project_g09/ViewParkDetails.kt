package com.example.project_g09

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.project_g09.databinding.FragmentViewParkDetailsBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.concurrent.locks.LockSupport.park

class ViewParkDetails : Fragment(R.layout.fragment_view_park_details) {

    private var _binding: FragmentViewParkDetailsBinding? = null
    private val binding get() = _binding!!


    private val args:ViewParkDetailsArgs by navArgs()


    val db = Firebase.firestore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentViewParkDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()

        binding.btnAdd.setOnClickListener {
            addToFirebase()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    //Function to add Park Information to Firebase
    private fun addToFirebase() {
        val park = args.Park
        val parkData = hashMapOf(
            "fullName" to park.fullName,
            "address" to "${park.addresses[0].line1}, ${park.addresses[0].city} , ${park.addresses[0].stateCode}, ${park.addresses[0].postalCode}",
            "currentDate" to LocalDate.now().format(DateTimeFormatter.ISO_DATE)
        )
        //Adding Data
        db.collection("parks")
            .add(parkData)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(requireContext(), "Park added to Itinerary", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Failed to add park to Firebase", Toast.LENGTH_SHORT).show()
            }
    }


    override fun onResume() {
        super.onResume()
        loadData()
    }

    //Function to load Park data to views
    private fun loadData() {
        val park = args.Park
        var addressForMap = "${park.addresses[0].line1}, ${park.addresses[0].city} , ${park.addresses[0].stateCode}, ${park.addresses[0].postalCode}"
        binding.tvParkName.text = park.fullName
        binding.tvAddress.text = addressForMap
        binding.tvDesc.text = park.description
        binding.tvLink.text = park.url
        Glide.with(requireContext())
            .load(park.images[0].url)
            .into(binding.imageView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
