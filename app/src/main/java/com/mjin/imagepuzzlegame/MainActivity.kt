package com.mjin.imagepuzzlegame

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.mjin.imagepuzzlegame.databinding.ActivityMainBinding
import kotlin.math.sqrt

class MainActivity : AppCompatActivity(), PieceAdapter.OnPieceClickListener {

    private val pieceAdapter = PieceAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {

            val list = splitImage(16)

            rvPuzzlePieces.apply {
                adapter = pieceAdapter
            }

            pieceAdapter.submitList(list.shuffled())

        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun splitImage(numberOfPiece: Int): ArrayList<Bitmap> {

        val rows: Int
        val cols: Int

        val pieceHeight: Int
        val pieceWidth: Int

        val pieceImages: ArrayList<Bitmap> = ArrayList<Bitmap>(numberOfPiece)

        val drawable = getDrawable(R.drawable.yoyo) as BitmapDrawable
        val bitmap = drawable.bitmap
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.width, bitmap.height, true)

        cols = sqrt(numberOfPiece.toDouble()).toInt()
        rows = cols
        pieceHeight = bitmap.height / rows
        pieceWidth = bitmap.width / cols

        //pixel positions of the image piece
        var yCoord = 0
        for (x in 0 until rows) {
            var xCoord = 0
            for (y in 0 until cols) {
                pieceImages.add(
                    Bitmap.createBitmap(
                        scaledBitmap,
                        xCoord,
                        yCoord,
                        pieceWidth,
                        pieceHeight
                    )
                )
                xCoord += pieceWidth
            }
            yCoord += pieceHeight
        }

        return pieceImages

    }

    override fun onPieceClicked(items: Bitmap) {
        Log.d("TAG", "piece item clicked")
    }
}