package com.example.firestorefootball.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firestorefootball.R
import com.example.firestorefootball.adapter.FootballAdapter
import com.example.firestorefootball.databinding.FragmentFirstBinding
import com.example.firestorefootball.model.FootballClub
import com.google.firebase.firestore.FirebaseFirestore

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {
    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    private val firebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val footballAdapter by lazy { FootballAdapter() }
    private val footClubList: MutableList<FootballClub> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.apply {
            adapter = footballAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//            throw RuntimeException("Test")
        }
        firebaseFirestore.collection("footballClubs")
            .get()
            .addOnCompleteListener {
                footClubList.clear()
                if (it.isSuccessful){
                    binding.progressBar.isVisible = false
                    val footballClub = it.result.toObjects(FootballClub::class.java)
                    footClubList.addAll(footballClub)
                }
                footballAdapter.submitList(footClubList)
                footballAdapter.notifyDataSetChanged()
            }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}