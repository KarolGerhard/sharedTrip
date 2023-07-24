package com.akgs.sharedtrip.controller

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akgs.sharedtrip.R
import com.akgs.sharedtrip.databinding.FragmentHomeBinding
import com.akgs.sharedtrip.model.Despesa
import com.akgs.sharedtrip.model.Usuario
import com.akgs.sharedtrip.view.DespesasAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {


    private var _binding: FragmentHomeBinding? = null

    private val binding: FragmentHomeBinding get() = _binding!!

    private lateinit var recyclerView: RecyclerView

    private lateinit var despesasAdapter: DespesasAdapter

    private val db = FirebaseFirestore.getInstance()

    private val auth = FirebaseAuth.getInstance()

    private lateinit var usuario: Usuario

    var id: String? = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        var nomeUsuario: TextView = binding.txtNomeUsuario

        id = auth.currentUser?.uid


        db.collection("COLLECTION_USUARIOS").document("$id").get()
            .addOnSuccessListener { snapshot ->
                if (snapshot != null) {
                    usuario = Usuario(
                        snapshot.id,
                        snapshot.getString("nome"),
                    )
                    nomeUsuario.setText(usuario.nome)
                }
            }


        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        recyclerView = binding.rcvDespesas
        recyclerView.layoutManager = LinearLayoutManager(context)

        db.collection("COLLECTION_DESPESAS").addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.w(TAG, "Erro.", error)
                return@addSnapshotListener
            }
            val despesas = ArrayList<Despesa>()
            if (snapshot != null && !snapshot.isEmpty) {

                for (doc in snapshot!!) {
                    val despesa = Despesa(
                        doc.id,
                        doc.getString("nomeItem"),
                        doc.getDouble("valor"),
                        doc.getString("formaPagamento"),
                        doc.getString("pessoaPagante")
                    )
                    despesas.add(despesa)
                }
                despesasAdapter = DespesasAdapter(despesas)
                recyclerView.adapter = despesasAdapter


            } else {
                Log.d(TAG, "Current data: null")
            }

        }


        val gestureDetector =
            GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {

                override fun onSingleTapConfirmed(e: MotionEvent): Boolean {

                    e?.let {

                        val view = recyclerView.findChildViewUnder(e.x, e.y)
                        view?.let {

                            val position = recyclerView.getChildAdapterPosition(view)
                            val id = despesasAdapter.getDespesa(position).id


                            val bundle = bundleOf("id" to id)
                            findNavController().navigate(
                                R.id.action_homeFragment_to_editaRemoveDespesasFragment,
                                bundle
                            )
                        }
                    }

                    return super.onSingleTapConfirmed(e)
                }
            })

        recyclerView.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {

                var child = rv.findChildViewUnder(e.x, e.y)

                return (child != null && gestureDetector.onTouchEvent(e))
            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}