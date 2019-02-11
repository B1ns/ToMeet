package com.example.tomeet.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.tomeet.R
import com.example.tomeet.data.Result
import com.example.tomeet.data.User
import com.example.tomeet.utils.Services
import com.example.tomeet.utils.Utils
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import kotlinx.android.synthetic.main.activity_login.*
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.ApiException
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.*
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class LoginActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    var callbackManager: CallbackManager? = null
    private lateinit var Id: TextInputEditText
    private lateinit var Passwrod: TextInputEditText

    companion object {
        private const val RC_SIGN_IN = 9001
        private val FINSH_INTERVAL_TIME = 2000
        private var backPressedTime: Long = 0
    }

    override fun onConnectionFailed(p0: ConnectionResult) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        callbackManager = CallbackManager.Factory.create()

        login_button.setOnClickListener {
            if (editText_id.text.toString().isNotEmpty() && editText_password.text.toString().isNotEmpty()) {
                login()
            } else {
                Toast.makeText(applicationContext, "아이디나 비밀번호를 입력하세요", Toast.LENGTH_SHORT).show()
            }
        }

        register()

        googleAuth()

        Facebook_button.setOnClickListener {
            facebookAuth()
        }
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

        Google_Button.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)

        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("fuck", "인증성공")
                    loginSucceed(auth.currentUser)
                } else {
                    loginSucceed(null)
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        callbackManager?.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {

            }
        }
    }

    fun facebookAuth() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"))
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                handleFacebookAccessToken(result?.accessToken)
            }

            override fun onCancel() {
            }

            override fun onError(error: FacebookException?) {
            }

        })
    }

    fun handleFacebookAccessToken(token: AccessToken?) {
        val credential = FacebookAuthProvider.getCredential(token?.token!!)
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                loginSucceed(task.result?.user)
            } else {
                Toast.makeText(this, "성공", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

    }

    private fun loginSucceed(user: FirebaseUser?) {
        if (user != null) {
            Toast.makeText(this@LoginActivity, "로그인 성공 !", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }


    fun login() {
        val user = User(editText_id.text.toString(), editText_password.text.toString())
        val service: Services = Utils.retrofit.create(Services::class.java)
        val call: Call<Result> = service.login(user)
        call.enqueue(object : Callback<Result> {
            override fun onFailure(call: Call<Result>, t: Throwable) {

                Toast.makeText(applicationContext, "서버에서 에러가 발생하였습니다.", Toast.LENGTH_SHORT).show()
                Log.e("loginError", t.message)
            }

            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                if (response.code() == 200) {
                    Log.e("login", Gson().toJson(response.body()))

                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                    Toast.makeText(applicationContext, "로그인이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                } else if (response.code() == 404) {
                    Toast.makeText(applicationContext, "아이디나 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("code : ", response.code().toString())
                }

            }

        })


    }


}
