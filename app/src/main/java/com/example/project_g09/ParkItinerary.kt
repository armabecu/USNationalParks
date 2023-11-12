package com.example.project_g09

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.project_g09.databinding.FragmentItineraryBinding
import com.example.project_g09.databinding.FragmentParkItineraryBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ParkItinerary : Fragment(R.layout.fragment_park_itinerary) {

    private var _binding: FragmentParkItineraryBinding? = null
    private val binding get() = _binding!!

    val db = Firebase.firestore

    // Get the arguments passed to this fragment
    private val args:ParkItineraryArgs by navArgs()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentParkItineraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Call the doStuff function to populate the view and add click listeners
        doStuff()

    }

    private fun doStuff() {
        // Get the shared preferences for this fragment using the park name as the key
        val preferences = requireActivity().getSharedPreferences(args.fullName, Context.MODE_PRIVATE)
        // Get the park name and address from the arguments and display them in the view
        val name = args.fullName
        val address = args.address
        // Get the saved date and notes from shared preferences and display them in the view
        binding.tvFullName.text = name
        binding.tvAddress.text = address
        binding.etDate.setText(preferences.getString("${args.fullName}_date", ""))
        binding.etNotes.setText(preferences.getString("${args.fullName}_notes", ""))

        // Save the itinerary to shared preferences and the database when the save button is clicked
        binding.btnSave.setOnClickListener {
            val date = binding.etDate.text.toString()
            val notes = binding.etNotes.text.toString()

            preferences.edit().apply {
                putString("${args.fullName}_date", date)
                putString("${args.fullName}_notes", notes)
                apply()
                db.collection("parks")
                    .whereEqualTo("fullName", args.fullName)
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            val parkDocRef = db.collection("parks").document(document.id)
                            val date = binding.etDate.text.toString()
                            parkDocRef.update("currentDate", date)
                                .addOnSuccessListener {
                                    // Date field updated successfully
                                    Toast.makeText(requireContext(), "Itinerary Saved", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
                    .addOnFailureListener { exception ->
                        // Handle the error here
                    }
            }
        }
        // Remove the itinerary from shared preferences and the database when the remove button is clicked
        binding.btnRemove.setOnClickListener {
            db.collection("parks")
                .whereEqualTo("fullName", args.fullName)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val parkDocRef = db.collection("parks").document(document.id)
                        parkDocRef.delete().addOnSuccessListener {
                            Toast.makeText(
                                requireContext(),
                                "Itinerary Removed",
                                Toast.LENGTH_SHORT
                            ).show()
                            findNavController().popBackStack()
                            val date = binding.etDate.text.toString()
                            val notes = binding.etNotes.text.toString()

                            preferences.edit().apply {
                                remove("${args.fullName}_date")
                                remove("${args.fullName}_notes")
                                apply()
                            }
                        }
                    }
                }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
