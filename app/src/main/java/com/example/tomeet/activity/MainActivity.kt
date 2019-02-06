package com.example.tomeet.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tomeet.R
import kotlinx.android.synthetic.main.activity_main.*
import androidx.appcompat.app.AlertDialog
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cardview.test
import com.example.tomeet.fragment.BottomNavigationDrawerFragment
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var currentFabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(bottom_app_bar)
        favListeners()
        bottom()
        recyclerView()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.bottomappbar_menu, menu)

        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                val bottomNavDrawerFragment = BottomNavigationDrawerFragment()
                bottomNavDrawerFragment.show(supportFragmentManager, bottomNavDrawerFragment.tag)
                return true
            }

            R.id.app_bar_fav -> {
                Toast.makeText(applicationContext, "프로필을 선택하셨습니다.", Toast.LENGTH_LONG).show()
                true
            }
            R.id.app_bar_search -> {
                Toast.makeText(applicationContext, "검색을 선택하셨습니다.", Toast.LENGTH_LONG).show()
                updateUI()
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

    private fun updateUI() {
        if (search_button.visibility == View.VISIBLE) {
            search_button.visibility = View.GONE
        } else {
            search_button.visibility = View.VISIBLE
        }
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle("ToMeet")
        builder.setIcon(R.drawable.main_icon)
        builder.setMessage("정말로 종료하시겠습니까?")
        builder.setPositiveButton("확인") { _, _ -> finishAffinity() }
        builder.setNegativeButton("취소", null)
        val alertDialog = builder.create()
        alertDialog.show()
    }

    fun bottom() {
        val addVisibilityChanged: FloatingActionButton.OnVisibilityChangedListener =
            object : FloatingActionButton.OnVisibilityChangedListener() {
                override fun onShown(fab: FloatingActionButton?) {
                    super.onShown(fab)
                }

                override fun onHidden(fab: FloatingActionButton?) {
                    super.onHidden(fab)
                    bottom_app_bar.toggleFabAlignment()
                    bottom_app_bar.replaceMenu(
                        if (currentFabAlignmentMode == BottomAppBar.FAB_ALIGNMENT_MODE_CENTER) R.menu.bottomappbar_menu_primary
                        else R.menu.bottomappbar_menu
                    )
                    fab?.setImageDrawable(
                        if (currentFabAlignmentMode == BottomAppBar.FAB_ALIGNMENT_MODE_CENTER) getDrawable(
                            R.drawable.ic_reply_black_24dp
                        )
                        else getDrawable(R.drawable.ic_add_black_24dp)
                    )
                    fab?.show()
                }
            }

        toggle_fab_alignment_button.setOnClickListener {
            fab.hide(addVisibilityChanged)
            invalidateOptionsMenu()
            bottom_app_bar.navigationIcon = if (bottom_app_bar.navigationIcon != null) null
            else getDrawable(R.drawable.ic_menu_black_24dp)
            when(screen_label.text) {
                getString(R.string.primary_screen_text) -> screen_label.text = getString(R.string.secondary_sceen_text)
                getString(R.string.secondary_sceen_text) -> screen_label.text = getString(R.string.primary_screen_text)
            }
        }

        fab.setOnClickListener {
            displayMaterialSnackBar()
        }

    }

    private fun displayMaterialSnackBar() {
        val marginSide = 0
        val marginBottom = 550
        val snackbar = Snackbar.make(
            coordinatorLayout2,
            "FAB Clicked",
            Snackbar.LENGTH_LONG
        ).setAction("UNDO") {  }
        // Changing message text color
        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.colorSnackbarButton))

        val snackbarView = snackbar.view
        val params = snackbarView.layoutParams as CoordinatorLayout.LayoutParams

        params.setMargins(
            params.leftMargin + marginSide,
            params.topMargin,
            params.rightMargin + marginSide,
            params.bottomMargin + marginBottom
        )

        snackbarView.layoutParams = params
        snackbar.show()
    }

    private fun BottomAppBar.toggleFabAlignment() {
        currentFabAlignmentMode = fabAlignmentMode
        fabAlignmentMode = currentFabAlignmentMode.xor(1)
    }

    private fun recyclerView() {

        val view: RecyclerView = findViewById(R.id.recyclerView)
        view.layoutManager = LinearLayoutManager(applicationContext)
        view.adapter = test()

    }


}

