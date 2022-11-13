package com.example.firestorefootball.fragment

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.example.firestorefootball.databinding.FragmentSecondBinding
import com.example.firestorefootball.model.FootballClub
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class SecondFragment : Fragment() {
    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!
    private val firestore by lazy { FirebaseFirestore.getInstance() }
    private lateinit var photoUrl: Uri
    private val leagues = listOf("APL", "Laliga", "Seria A", "BundesLiga", "Liga 1", "CocaCola")
    private lateinit var leagueName: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        val arrayAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, leagues)
        binding.autoComplete.setAdapter(arrayAdapter)
        binding.autoComplete.setOnItemClickListener { _, _, position, _ ->
            leagueName = leagues[position]
        }
        binding.imageView.setOnClickListener {
            launcher.launch("image/*")
        }
        binding.saveBtn.setOnClickListener {
            val name = binding.name.text.toString().trim()
            val whenCreated = binding.textWhen.text.toString().trim().toInt()
            val cupNumbers = binding.cubNumber.text.toString().trim().toInt()
            val coachName = binding.coachName.text.toString().trim()
            val price = binding.price.text.toString().trim()
            val rating = binding.rating.text.toString().trim().toInt()
            if (name.isNotBlank() && ::photoUrl.isInitialized && ::leagueName.isInitialized) {
                saveToDatabase(name, whenCreated, cupNumbers, coachName, price, rating)
            }
        }
    }

    private fun saveToDatabase(
        name: String,
        whenC: Int,
        cupN: Int,
        coach: String,
        price: String,
        rating: Int
    ) {
        val fileName = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("images/$fileName")
        ref.putFile(photoUrl)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    firestore.collection("footballClubs").document().set(
                        FootballClub(
                            name,
                            whenC,
                            cupN,
                            coach,
                            it.toString(),
                            price,
                            leagueName,
                            rating
                        )
                    ).addOnCompleteListener { task->
                        if (task.isSuccessful){
                            findNavController().popBackStack()
                            Toast.makeText(requireContext(), "Successfly created", Toast.LENGTH_SHORT).show()
                        }else{
                            Log.d("@@@", task.exception?.message!!)
                        }
                    }
                }
            }
    }
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()){
        it?.let { imageUri ->
            photoUrl = imageUri
            binding.imageView.setImageURI(imageUri)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}