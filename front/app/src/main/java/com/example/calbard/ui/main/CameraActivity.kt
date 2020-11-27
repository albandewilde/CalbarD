package com.example.calbard.ui.main

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64.encodeToString
import android.widget.AdapterView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.example.calbard.MainPage
import com.example.calbard.R
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.io.ByteArrayOutputStream
import java.sql.Timestamp
import java.util.*


class CameraActivity : AppCompatActivity() {
    val REQUEST_IMAGE_CAPTURE = 1
    lateinit var pseudo: String;
    var picture: Picture? = null;
    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        pseudo = intent.getStringExtra("Pseudo")!!

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }

        val saveButton: Button = findViewById(R.id.saveButton)
        saveButton.setOnClickListener {
            if( this.picture != null ) {
                val json = Gson().toJson(this.picture)
                disposable.add(ApiService.savePicture(this.picture!!, json.length.toString())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError { e ->
                        Toast.makeText(this, "Error while saving picture", Toast.LENGTH_LONG).show()
                    }
                    .subscribe({
                        if( it === "Successfully saved" ) {
                            finish()
                        }
                        finish()
                    }, Throwable::printStackTrace)
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            val byteArrayOutputStream = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayOutputStream)
            val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
            val encoded: String = Base64.getEncoder().encodeToString(byteArray)
            this.picture = createPicture(encoded)
            val pic: AppCompatImageView = findViewById(R.id.pic)
            pic.setImageBitmap(imageBitmap)
        }
    }


    fun createPicture(img: String): Picture {
        return Picture(img, "test", Timestamp(System.currentTimeMillis()), this.pseudo)
    }
}