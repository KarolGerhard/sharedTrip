package com.akgs.sharedtrip.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.akgs.sharedtrip.R
import com.akgs.sharedtrip.model.Despesa

class DespesasAdapter(private val despesas: ArrayList<Despesa>) : RecyclerView.Adapter<DespesasAdapter.DespesasHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DespesasHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rcv_despesas, parent, false)

        return DespesasHolder(view)
    }

    override fun onBindViewHolder(holder: DespesasHolder, position: Int) {
        val despesa = despesas[position]
        holder.txtNomeItem.text = despesa.nomeItem
        holder.txtValor.text = despesa.valor.toString()
        holder.txtFormaPagamento.text = despesa.formaPagamento
        holder.txtPessoaPagante.text = despesa.pessoaPagante
    }

    override fun getItemCount(): Int = despesas.size

    fun getDespesa(position: Int): Despesa {

        return despesas[position]
    }

//
//    fun atualiza(despesas: List<Despesa>) {
//        this.despesas.clear()
//        this.despesas.addAll(despesas)
//        notifyDataSetChanged()
//    }

//    inner class DespesasHolder(view: View) : RecyclerView.ViewHolder(view) {
//
//        var layDespesa: LinearLayout
//        var txtNomeItem: TextView
//        var txtValor: TextView
//        var txtFormaPagamento: TextView
//        var txtPessoaPagante: TextView
//
//
//
//
//        init {
//            layDespesa = view.findViewById(R.id.rcvDespesas)
//            txtNomeItem = view.findViewById(R.id.txtName)
//            txtValor = view.findViewById(R.id.txtValor)
//            txtFormaPagamento = view.findViewById(R.id.txtPagamento)
//            txtPessoaPagante = view.findViewById(R.id.txtPessoa)
//
//        }
//    }

     class DespesasHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
         var txtNomeItem: TextView = itemView.findViewById(R.id.txtName)
        var txtValor: TextView = itemView.findViewById(R.id.txtValor)
        var txtFormaPagamento: TextView = itemView.findViewById(R.id.txtPagamento)
        var txtPessoaPagante: TextView = itemView.findViewById(R.id.txtPessoa)
    }

}