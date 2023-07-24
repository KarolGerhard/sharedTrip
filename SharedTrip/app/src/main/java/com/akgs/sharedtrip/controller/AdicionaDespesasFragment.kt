package com.akgs.sharedtrip.controller


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.akgs.sharedtrip.R
import com.akgs.sharedtrip.databinding.FragmentAdicionaDespesasBinding
import com.akgs.sharedtrip.model.Despesa
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore


class AdicionaDespesasFragment : Fragment() {

    private var _binding: FragmentAdicionaDespesasBinding? = null

    private val binding: FragmentAdicionaDespesasBinding get() = _binding!!

    private val db = FirebaseFirestore.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAdicionaDespesasBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val botaoAdicionar = binding.btnAdicionar

        botaoAdicionar.setOnClickListener { view ->
            val nomeItem = binding.edtNomeItem.text.toString()
            val valor = binding.edtValor.text.toString()
            val formaPagamento = binding.edtFormaPagamento.text.toString()
            val pessoaPagante = binding.edtPessoaPagante.text.toString()
            val despesa =
                Despesa(id = "", nomeItem, valor.toDouble(), formaPagamento, pessoaPagante)

            db.collection("COLLECTION_DESPESAS").add(
                despesa
            ).addOnCompleteListener { despesa ->
                if (despesa.isSuccessful) {
                    val snackbar = Snackbar.make(
                        view,
                        "Sucesso ao adicionar uma despesa",
                        Snackbar.LENGTH_SHORT
                    )
                    snackbar.setBackgroundTint(Color.BLUE)
                    snackbar.show()
                    binding.edtNomeItem.setText("")
                    binding.edtValor.setText("")
                    binding.edtFormaPagamento.setText("")
                    binding.edtPessoaPagante.setText("")

                    findNavController().navigate(R.id.action_adicionaDespesasFragment_to_homeFragment)
                }
            }.addOnFailureListener {
                val snackbar =
                    Snackbar.make(view, "Erro ao adicionar despesa", Snackbar.LENGTH_SHORT)
                snackbar.setBackgroundTint(Color.RED)
                snackbar.show()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}