package com.mjin.imagepuzzlegame

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipDescription
import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.View
import android.view.View.generateViewId
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.mjin.imagepuzzlegame.databinding.ActivityMainBinding
import com.mjin.imagepuzzlegame.model.Piece
import com.mjin.imagepuzzlegame.model.PuzzlePiece
import kotlin.math.sqrt


class MainActivity : AppCompatActivity(), PieceAdapter.OnPieceLongClickListener {

    private var pieceAdapter = PieceAdapter(this)

    lateinit var relativeLayout: RelativeLayout
    lateinit var rvPuzzle: RecyclerView
    lateinit var cvPuzzle: CardView

    var pieceList: MutableList<Piece> = ArrayList()

    var piecesModelHashMap: HashMap<String, Piece> = HashMap()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {

            this@MainActivity.relativeLayout = relativeLayout
            this@MainActivity.rvPuzzle = rvPuzzlePieces
            this@MainActivity.cvPuzzle = cvPuzzle

            relativeLayout.setOnDragListener(OnDragListener(null))
            rvPuzzle.setOnDragListener(OnDragListener(null))

            pieceList = splitImage(16).shuffled() as MutableList<Piece>

            setPuzzlePieceListAdapter()

        }
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private fun splitImage(numberOfPiece: Int): ArrayList<Piece> {
        var params: RelativeLayout.LayoutParams

        val cols = sqrt(numberOfPiece.toDouble()).toInt()
        val rows = sqrt(numberOfPiece.toDouble()).toInt()

        val pieceHeight: Int
        val pieceWidth: Int

        val pieceList = ArrayList<Piece>(numberOfPiece)

        val drawable = getDrawable(R.drawable.sample_image) as BitmapDrawable
        val bitmap = drawable.bitmap
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.width, bitmap.height, true)


        pieceWidth = bitmap.width / cols
        pieceHeight = bitmap.height / rows

        val horizontalStep = bitmap.width /3
        val verticalStep = bitmap.height /3

        var countGrid = 0
        var yCoord = 0
        for (x in 0 until rows) {
            var xCoord = 0
            for (y in 0 until cols) {

                val pieceBitmap = Bitmap.createBitmap(
                    scaledBitmap,
                    xCoord,
                    yCoord,
                    pieceWidth,
                    pieceHeight
                )

                val piece =   Piece(x, y, pieceBitmap, countGrid)

                pieceList.add(piece)
                piecesModelHashMap["$x,$y"] = piece

                val puzzlePiece = PuzzlePiece(
                    bitmap = pieceBitmap,
                    anchorPoint = Point(
                        x * horizontalStep + horizontalStep / 2,
                        y * verticalStep + verticalStep / 2
                    ),
                    centerPoint = Point(
                        horizontalStep / 2,
                        verticalStep / 2
                    ),
                    width = pieceWidth,
                    height = pieceHeight
                )

                params = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                )
                val dimX: Int = puzzlePiece.anchorPoint.x - puzzlePiece.centerPoint.x
                val dimY: Int = puzzlePiece.anchorPoint.y - puzzlePiece.centerPoint.y


                params.setMargins(dimX*2, dimY*2, 0, 0)

                val button2 = ImageView(this)

                button2.id = generateViewId()
                button2.tag = "$y,$x"
                button2.setImageResource(R.drawable.ic_1)
                button2.setOnDragListener(OnDragListener(button2))
                button2.layoutParams = params

                relativeLayout.addView(button2)

                countGrid++
                xCoord += pieceWidth
            }
            yCoord += pieceHeight
        }
        return pieceList
    }


    override fun onPieceLongClicked(items: Piece, view: View) {

        val item = ClipData.Item(view.tag as CharSequence)

        val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
        val data = ClipData(view.tag.toString(), mimeTypes, item)
        val shadowBuilder = View.DragShadowBuilder(view)

        view.startDragAndDrop(
            data,
            shadowBuilder,
            view,
            0
        )
    }


    fun setPuzzlePieceListAdapter() {
        pieceAdapter = PieceAdapter(this)
        rvPuzzle.apply {
            adapter = pieceAdapter
            setHasFixedSize(true)
        }
        pieceAdapter.submitList(pieceList)
    }

    inner class OnDragListener(imageView: ImageView?) : View.OnDragListener {
        private var imageViews: ImageView? = imageView

        override fun onDrag(v: View, event: DragEvent): Boolean {

            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> {}
                DragEvent.ACTION_DRAG_ENTERED -> {}
                DragEvent.ACTION_DRAG_EXITED -> {}
                DragEvent.ACTION_DROP -> {

                    if (v == imageViews) {
                        val view = event.localState as View
                        val owner = v.parent as ViewGroup

                        if (owner == relativeLayout) {

                            val selectedViewTag = view.tag.toString()
                            val piece: Piece? = piecesModelHashMap[v.tag.toString()]
                            val xy: String =
                                piece?.pX.toString() + "," + piece?.pY

                            Log.d("TAG", "selectedViewTag :$selectedViewTag ,xy :$xy ")

                            if (xy == selectedViewTag) {
                                val imageView: ImageView = v as ImageView
                                imageView.setImageBitmap(piece?.bitmap)
                                pieceList.remove(piece)
                                setPuzzlePieceListAdapter()

                            } else {
                                view.visibility = View.VISIBLE
                            }
                        } else {
                            val view1 = event.localState as View
                            view1.visibility = View.VISIBLE

                        }
                    } else if (v == cvPuzzle) {
                        val view1 = event.localState as View
                        view1.visibility = View.VISIBLE

                    } else if (v == rvPuzzle) {
                        val view1 = event.localState as View
                        view1.visibility = View.VISIBLE

                    } else {
                        val view = event.localState as View
                        view.visibility = View.VISIBLE
                    }
                }
                DragEvent.ACTION_DRAG_ENDED -> {}
                else -> {}
            }
            return true
        }

    }

}