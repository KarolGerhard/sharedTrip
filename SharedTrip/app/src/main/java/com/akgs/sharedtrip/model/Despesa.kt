package com.akgs.sharedtrip.model

class Despesa
    (
    var id: String,
    var nomeItem: String? = "",
    var valor: Double? = 0.0,
    var formaPagamento: String? = "",
    var pessoaPagante: String? = ""
) {
}