package com.akgs.sharedtrip.controller

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.akgs.sharedtrip.R
import com.akgs.sharedtrip.databinding.FragmentEditaRemoveDespesasBinding
import com.akgs.sharedtrip.model.Despesa
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore


class EditaRemoveDespesasFragment : Fragment() {

    private var _binding: FragmentEditaRemoveDespesasBinding? = null

    private val binding: FragmentEditaRemoveDespesasBinding get() = _binding!!

    private val db = FirebaseFirestore.getInstance()

    private lateinit var despesa: Despesa
    var id: String? = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditaRemoveDespesasBinding.inflate(inflater, container, false)


        this.id = requireArguments().getString("id")

        val nomeItem: EditText = binding.edtNomeItem
        val valor: EditText = binding.edtValor
        val formaPagamento: EditText = binding.edtFormaPagamento
        val pessoaPagante: EditText = binding.edtPessoaPagante


        db.collection("COLLECTION_DESPESAS").document("$id").get()
            .addOnSuccessListener { snapshot ->
                if (snapshot != null) {
                    despesa = Despesa(
                        snapshot.id,
                        snapshot.getString("nomeItem"),
                        snapshot.getDouble("valor"),
                        snapshot.getString("formaPagamento"),
                        snapshot.getString("pessoaPagante")
                    )
                    nomeItem.setText(despesa.nomeItem)
                    valor.setText(despesa.valor.toString())
                    formaPagamento.setText(despesa.formaPagamento)
                    pessoaPagante.setText(despesa.pessoaPagante)
                }
            }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val botaoAtualizar = binding.btnAtualiza
        val botaoRemover = binding.btnRemove
        val botaoVoltar = binding.btnVoltar


        botaoAtualizar.setOnClickListener { view ->
            val nomeItem = binding.edtNomeItem.text.toString()
            val valor = binding.edtValor.text.toString()
            val formaPagamento = binding.edtFormaPagamento.text.toString()
            val pessoaPagante = binding.edtPessoaPagante.text.toString()
            val despesa =
                Despesa(despesa.id, nomeItem, valor.toDouble(), formaPagamento, pessoaPagante)

            db.collection("COLLECTION_DESPESAS").document(despesa.id).set(
                Despesa(
                    id = despesa.id,
                    nomeItem = despesa.nomeItem,
                    valor = despesa.valor,
                    formaPagamento = despesa.formaPagamento,
                    pessoaPagante = despesa.pessoaPagante,
                )
            ).addOnCompleteListener { despesa ->
                if (despesa.isSuccessful) {
                    val snackbar = Snackbar.make(
                        view,
                        "Sucesso ao atualizar a despesa",
                        Snackbar.LENGTH_SHORT
                    )
                    snackbar.setBackgroundTint(Color.BLUE)
                    snackbar.show()
                    binding.edtNomeItem.setText("")
                    binding.edtValor.setText("")
                    binding.edtFormaPagamento.setText("")
                    binding.edtPessoaPagante.setText("")

                    findNavController().navigate(R.id.action_editaRemoveDespesasFragment_to_homeFragment)
                }
            }.addOnFailureListener {
                val snackbar =
                    Snackbar.make(view, "Erro ao atualizar despesa", Snackbar.LENGTH_SHORT)
                snackbar.setBackgroundTint(Color.RED)
                snackbar.show()
            }
        }

        botaoRemover.setOnClickListener { view ->
            db.collection("COLLECTION_DESPESAS").document(despesa.id)
                .delete()
                .addOnSuccessListener {
                    val snackbar = Snackbar.make(
                        view,
                        "Despesa excluida com sucesso!",
                        Snackbar.LENGTH_SHORT
                    )
                    snackbar.setBackgroundTint(Color.BLUE)
                    snackbar.show()

                    findNavController().navigate(R.id.action_editaRemoveDespesasFragment_to_homeFragment)
                }.addOnFailureListener {
                    val snackbar =
                        Snackbar.make(view, "Erro ao excluir a despesa!", Snackbar.LENGTH_SHORT)
                    snackbar.setBackgroundTint(Color.RED)
                    snackbar.show()
                }
        }

        botaoVoltar.setOnClickListener {
            findNavController().popBackStack()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}