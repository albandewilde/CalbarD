package com.example.calbard.ui.main

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.calbard.R
import java.util.*


internal class MainAdapter(
    private val context: Context,
    private val pictures: Array<Picture>,
) :
    BaseAdapter() {
    private var layoutInflater: LayoutInflater? = null
    private lateinit var imageView: ImageView
    private lateinit var textView: TextView
    override fun getCount(): Int {
        return pictures.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return (position as Long)
    }

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View? {
        var convertView = convertView
        if (layoutInflater == null) {
            layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        if (convertView == null) {
            convertView = layoutInflater!!.inflate(R.layout.row_item, null)
        }

        imageView = convertView!!.findViewById(R.id.imageView)
        val decodedString: ByteArray = Base64.getDecoder().decode(pictures[position].img)
        val decodedByte: Bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        imageView.setImageBitmap(decodedByte)

        textView = convertView!!.findViewById(R.id.testText)
        textView.setText(pictures[position].author)
        return convertView
    }
}
