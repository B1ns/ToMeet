package com.example.tomeet.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.tomeet.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(bottom_app_bar)
        favListeners()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.bottomappbar_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.app_bar_fav -> {
                Toast.makeText(applicationContext, "프로필을 선택하셨습니다.", Toast.LENGTH_LONG).show()
                true
            }
            R.id.app_bar_search -> {
                Toast.makeText(applicationContext, "검색을 선택하셨습니다.", Toast.LENGTH_LONG).show()
                search_button.visibility = View.VISIBLE
                return true
            }
            R.id.app_bar_settings -> {
                Toast.makeText(applicationContext, "설정을 선택하셨습니다.", Toast.LENGTH_LONG).show()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun favListeners() {
        fab.setOnClickListener {
            Toast.makeText(this@MainActivity, "Fab버튼을 누르셨습니다.", Toast.LENGTH_LONG).show()
        }
    }

}
