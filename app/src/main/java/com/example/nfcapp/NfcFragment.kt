package com.example.nfcapp

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.nfcapp.databinding.FragmentNfcBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class NfcFragment : Fragment() {

    private lateinit var binding: FragmentNfcBinding
    private var nfcAdapter: NfcAdapter? = null
    private val firestore = Firebase.firestore
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNfcBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        // Check if NFC is supported
        nfcAdapter = NfcAdapter.getDefaultAdapter(requireContext())
        if (nfcAdapter == null) {
            // NFC is not supported on this device
            binding.textStatus.text = "NFC not supported"
            Toast.makeText(requireContext(), "NFC is not supported on this device", Toast.LENGTH_LONG).show()
        } else if (!nfcAdapter!!.isEnabled) {
            // NFC is supported but not enabled
            binding.textStatus.text = "NFC is disabled"
            Toast.makeText(requireContext(), "Please enable NFC in settings", Toast.LENGTH_LONG).show()
        } else {
            // NFC is supported and enabled
            binding.textStatus.text = "Tap an NFC card"
        }
    }

    override fun onResume() {
        super.onResume()
        // Enable NFC foreground dispatch
        nfcAdapter?.enableForegroundDispatch(requireActivity(), null, null, null)
    }

    override fun onPause() {
        super.onPause()
        // Disable NFC foreground dispatch
        nfcAdapter?.disableForegroundDispatch(requireActivity())
    }

    fun handleNfcTag(tag: Tag) {
        // Read NFC card data
        val cardId = tag.id.joinToString("") { "%02x".format(it) } // Convert card ID to hex string

        // Get the device's current location
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request location permission if not granted
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    // Save NFC card data to Firestore
                    val nfcCard = NfcCard(
                        id = cardId,
                        cardData = cardId,
                        location = "Lat: ${location.latitude}, Long: ${location.longitude}",
                        timestamp = System.currentTimeMillis()
                    )

                    firestore.collection("nfcCards").add(nfcCard)
                        .addOnSuccessListener {
                            binding.textStatus.text = "Card data saved!"
                        }
                        .addOnFailureListener {
                            binding.textStatus.text = "Failed to save card data"
                        }
                } else {
                    binding.textStatus.text = "Failed to get location"
                }
            }
            .addOnFailureListener {
                binding.textStatus.text = "Failed to get location"
            }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 100
    }
}