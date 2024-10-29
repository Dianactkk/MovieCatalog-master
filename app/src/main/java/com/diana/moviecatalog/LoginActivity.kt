package com.diana.moviecatalog

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import com.bumptech.glide.Glide
import com.diana.moviecatalog.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Konfigurasi opsi login Google
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        auth = FirebaseAuth.getInstance()

        // Panggil fungsi login saat tombol ditekan
        findViewById<AppCompatImageButton>(R.id.Google).setOnClickListener {
            googleSignInClient.revokeAccess()  // Hapus akses agar bisa memilih akun lagi
                .addOnCompleteListener {
                    googleSignInClient.signOut()  // Logout dari akun Google
                        .addOnCompleteListener {
                            signInWithGoogle()  // Mulai ulang proses login Google
                        }
                }
        }
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    @SuppressLint("SetTextI18n")
    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        if (task.isSuccessful) {
            val account = task.result
            if (account != null) {
                firebaseAuthWithGoogle(account)
            }
        } else {
            val errorMessage = task.exception?.message
            val errorCause = task.exception?.cause
            val errorClass = task.exception?.javaClass?.simpleName
            val errorStackTrace = task.exception?.stackTrace?.joinToString("\n")

            Log.e("GoogleSignInError", "Google Sign-In gagal dengan error:")
            Log.e("GoogleSignInError", "Error Message: $errorMessage")
            Log.e("GoogleSignInError", "Error Cause: $errorCause")
            Log.e("GoogleSignInError", "Error Class: $errorClass")
            Log.e("GoogleSignInError", "Stack Trace: $errorStackTrace")
        }
    }

    @SuppressLint("SetTextI18n")
    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val name = user?.displayName
                    val email = user?.email
                    val welcomeMessage = "Selamat datang, ${name ?: "Pengguna"}! Email Anda: ${email ?: "Tidak tersedia"}"
                    Toast.makeText(this, welcomeMessage, Toast.LENGTH_LONG).show()

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish() // Opsional: Tutup LoginActivity setelah login berhasil
                } else {
                    val errorCode = task.exception?.message
                    Log.e("FirebaseAuthError", "Login dengan Firebase gagal: $errorCode")
                }
            }
    }
}