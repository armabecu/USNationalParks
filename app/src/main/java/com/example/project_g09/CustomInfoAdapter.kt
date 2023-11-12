package com.example.project_g09

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

//Adapter to make a cutomized infowindow for marker
class CustomInfoWindowAdapter(private val context: Context) : GoogleMap.InfoWindowAdapter {
    override fun getInfoContents(marker: Marker): View {
        val view = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null)
        // Bind data to views in the layout

        // Find the views in the layout
        val titleTextView = view.findViewById<TextView>(R.id.info_window_title)
        val addressTextView = view.findViewById<TextView>(R.id.address)

        // Set the text values for the views
        titleTextView.text = marker.title
        addressTextView.text = marker.snippet

        return view
    }

    override fun getInfoWindow(marker: Marker): View? {
        // Return null here so that getInfoContents() is called.
        return null
    }
}