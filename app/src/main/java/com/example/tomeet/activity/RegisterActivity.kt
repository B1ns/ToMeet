package com.example.tomeet.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.tomeet.R
import com.example.tomeet.data.Register
import com.example.tomeet.data.Result
import com.example.tomeet.data.User
import com.example.tomeet.utils.Services
import com.example.tomeet.utils.Utils
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        register_button.setOnClickListener {
            register()
        }
    }


    private fun register() {
        val register = Register(
            editText_name.text.toString(),
            editText_Age.text.toString(),
            editText_id2.text.toString(),
            editText_password2.text.toString()
        )
        val service: Services = Utils.retrofit.create(Services::class.java)
        val call: Call<Result> = service.register(register)
        call.enqueue(object : Callback<Result> {
            override fun onFailure(call: Call<Result>, t: Throwable) {

            }

            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                finish()
            }

        })

    }
}
