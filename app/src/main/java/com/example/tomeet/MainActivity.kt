package com.example.tomeet

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.app_bar_fav -> Toast.makeText(this,"프로필을 눌르셨습니다 !",Toast.LENGTH_LONG).show()
            R.id.app_bar_search -> Toast.makeText(this,"검색버튼 을 눌르셨습니다 !",Toast.LENGTH_LONG).show()
            R.id.app_bar_settings -> Toast.makeText(this,"셋팅 을 눌르셨습니다 !",Toast.LENGTH_LONG).show()
        }
        return true
    }
}
