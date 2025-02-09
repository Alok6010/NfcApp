package com.example.nfcapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nfcapp.databinding.FragmentHistoryBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private val firestore = FirebaseFirestore.getInstance()
    private var firestoreListener: ListenerRegistration? = null // To hold the real-time listener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up RecyclerView
        binding.recyclerViewHistory.layoutManager = LinearLayoutManager(requireContext())

        // Load initial data
        loadHistory()

        // Set up real-time listener
        setupRealTimeListener()
    }

    private fun loadHistory() {
        firestore.collection("nfcCards").get()
            .addOnSuccessListener { result ->
                val nfcCards = result.toObjects(NfcCard::class.java)
                binding.recyclerViewHistory.adapter = NfcCardAdapter(nfcCards)
            }
            .addOnFailureListener {
                binding.textStatus.text = "Failed to load history"
            }
    }

    private fun setupRealTimeListener() {
        // Listen for real-time updates
        firestoreListener = firestore.collection("nfcCards")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    binding.textStatus.text = "Failed to load real-time updates"
                    return@addSnapshotListener
                }

                // Update the RecyclerView with new data
                if (snapshot != null) {
                    val nfcCards = snapshot.toObjects(NfcCard::class.java)
                    binding.recyclerViewHistory.adapter = NfcCardAdapter(nfcCards)
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Remove the real-time listener when the fragment is destroyed
        firestoreListener?.remove()
    }
}