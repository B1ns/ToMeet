package com.example.tomeet.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.tomeet.R
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import kotlinx.android.synthetic.main.activity_login.*
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider


class LoginActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var callbackManager: CallbackManager

    companion object {
        private const val RC_SIGN_IN = 9001
        private val FINSH_INTERVAL_TIME = 2000
        private var backPressedTime: Long = 0
    }

    override fun onConnectionFailed(p0: ConnectionResult) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        lottie()

        register()

        googleAuth()

        facebookAuth()

    }

    private fun lottie() {
        lottie_background.playAnimation()
    }

    private fun register() {
        button_register.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
    }


    override fun onBackPressed() {
        val tempTime = System.currentTimeMillis()
        val intervalTime = tempTime - backPressedTime
        if (intervalTime in 0..FINSH_INTERVAL_TIME) {
            ActivityCompat.finishAffinity(this)
        } else {
            backPressedTime = tempTime
            Toast.makeText(applicationContext, "한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show()
        }
    }


    private fun googleAuth() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("231549717366-5hbkc89vfjd778mj6dn0cgqgrja2i0bj.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        auth = FirebaseAuth.getInstance()

        Google_Button.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        callbackManager.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {

            }
        }
    }


    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)

        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("fuck", "인증성공")
                    Toast.makeText(this@LoginActivity, "인증성공 ! ", Toast.LENGTH_LONG).show()
                    loginSucceed(auth.currentUser)
                } else {
                    Toast.makeText(this@LoginActivity, "인증 실패 !", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun facebookAuth() {
        auth = FirebaseAuth.getInstance()
        callbackManager = CallbackManager.Factory.create()

        Facebook_button.setReadPermissions("email", "public_profile")
        Facebook_button.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                handleFacebookAccessToken(result.accessToken!!)
            }

            override fun onCancel() {
            }

            override fun onError(error: FacebookException?) {
                loginSucceed(null)
            }
        })
    }

    private fun handleFacebookAccessToken(token: AccessToken) {

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(baseContext, "로그인에 성공하셨습니다.", Toast.LENGTH_SHORT).show()
                    loginSucceed(user)
                } else {
                    Toast.makeText(baseContext, "로그인에 실패하셨습니다.", Toast.LENGTH_SHORT).show()
                }

            }
    }

    private fun loginSucceed(user: FirebaseUser?) {
        if (user != null) {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        } else {
            Toast.makeText(this@LoginActivity, "로그인을 해주세요.", Toast.LENGTH_LONG).show()
        }
    }
}
