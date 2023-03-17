package com.mjin.imagepuzzlegame.model

import android.graphics.Bitmap
import android.graphics.Point

data class PuzzlePiece(
    var bitmap: Bitmap,
    var anchorPoint: Point,
    var centerPoint: Point,
    var width: Int,
    var height: Int

)