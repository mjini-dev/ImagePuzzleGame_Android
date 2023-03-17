package com.mjin.imagepuzzlegame.model

import android.graphics.Bitmap

data class Piece(
    var pX: Int,
    var pY: Int,
    var bitmap: Bitmap,
    var position : Int
    )
