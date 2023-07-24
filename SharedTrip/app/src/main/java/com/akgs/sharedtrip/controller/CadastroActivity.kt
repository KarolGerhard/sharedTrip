package com.akgs.sharedtrip.controller

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.akgs.sharedtrip.databinding.ActivityCadastroBinding
import com.akgs.sharedtrip.model.Usuario
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.Result.Companion.success

class CadastroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCadastroBinding
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = Color.parseColor("#297BF5")

        binding.btnCadastrar.setOnClickListener { view ->
            val nome = binding.edtNome.text.toString()
            val email = binding.edtEmail.text.toString()
            val senha = binding.edtSenha.text.toString()

            val usuario = Usuario(id = "", nome, email)

            if(nome.isEmpty() || email.isEmpty() || senha.isEmpty()){
                val snackbar = Snackbar.make(view, "Preencha todos os campos", Snackbar.LENGTH_SHORT)
                snackbar.setBackgroundTint(Color.RED)
                snackbar.show()
            }else{
                auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener { cadastro ->
                    if (cadastro.isSuccessful){
                        val snackbar = Snackbar.make(view, "Sucesso ao cadastrar usuario", Snackbar.LENGTH_SHORT)
                        snackbar.setBackgroundTint(Color.BLUE)
                        snackbar.show()

                        val id = auth.currentUser?.uid.let {
                            it
                        } ?: run { email }
                        binding.edtNome.setText("")
                        binding.edtEmail.setText("")
                        binding.edtSenha.setText("")

                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)

                        db.collection("COLLECTION_USUARIOS").document(id).set(
                            usuario
                        ).addOnSuccessListener {
                            success(auth.currentUser)
                        }.addOnFailureListener {
                           error(it)
                        }
                    }
                }.addOnFailureListener{ exception ->
                    val mensagemErro = when(exception) {
                        is FirebaseAuthWeakPasswordException -> "Digite uma senha com no minimo de 6 caracteres!"
                        is FirebaseAuthInvalidCredentialsException -> "Digite um email valido!"
                        is FirebaseAuthUserCollisionException -> "Esta conta ja foi cadastrada."
                        is FirebaseNetworkException -> "Sem conexão com a internet."
                        else -> "Erro ao cadastrar usuário"
                    }
                    val snackbar = Snackbar.make(view, mensagemErro, Snackbar.LENGTH_SHORT)
                    snackbar.setBackgroundTint(Color.RED)
                    snackbar.show()
                }

            }
        }
    }
}