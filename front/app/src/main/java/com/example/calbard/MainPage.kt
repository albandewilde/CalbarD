package com.example.calbard

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.app.LoaderManager
import androidx.viewpager.widget.ViewPager
import com.example.calbard.ui.main.CameraActivity
import com.example.calbard.ui.main.SectionsPagerAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout


class MainPage : AppCompatActivity()  {
    lateinit var pseudo: String;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        pseudo = intent.getStringExtra("Pseudo")!!
        val pseudoPlaceholder: TextView = findViewById(R.id.title)
        pseudoPlaceholder.text = pseudo

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            openCamera()
        }
    }

    fun openCamera() {
        val takePictureIntent = Intent(this, CameraActivity::class.java)
        takePictureIntent.putExtra("Pseudo", this.pseudo)
        startActivity(takePictureIntent)
    }
}