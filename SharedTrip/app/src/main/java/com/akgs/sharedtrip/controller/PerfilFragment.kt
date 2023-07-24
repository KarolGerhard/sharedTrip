package com.akgs.sharedtrip.controller

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.akgs.sharedtrip.databinding.FragmentPerfilBinding
import com.akgs.sharedtrip.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PerfilFragment : Fragment() {

    private var _binding: FragmentPerfilBinding? = null

    private val binding: FragmentPerfilBinding get() = _binding!!

    private val db = FirebaseFirestore.getInstance()

    private val auth = FirebaseAuth.getInstance()

    private lateinit var usuario: Usuario

    var id: String? = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPerfilBinding.inflate(inflater, container, false)



        var nomeUsuario: TextView = binding.txtNome
        var emailUsuario: TextView = binding.txtEmail

        id = auth.currentUser?.uid


        db.collection("COLLECTION_USUARIOS").document("$id").get()
            .addOnSuccessListener { snapshot ->
                if (snapshot != null) {
                    usuario = Usuario(
                        snapshot.id,
                        snapshot.getString("nome"),
                        snapshot.getString("email")
                    )
                    nomeUsuario.setText(usuario.nome)
                    emailUsuario.setText(usuario.email)
                }
            }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSair.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val backToLogin = Intent(context, LoginActivity::class.java)
            startActivity(backToLogin)
            activity?.finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }


}