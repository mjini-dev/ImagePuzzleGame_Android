package com.mjin.imagepuzzlegame

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mjin.imagepuzzlegame.databinding.ItemPuzzlePieceBinding

class PieceAdapter(private val onPieceClickListener: OnPieceClickListener) :
    ListAdapter<Bitmap, PieceAdapter.PieceViewHolder>(PieceItemCallback()) {


//    val items = ArrayList<Bitmap>()

    interface OnPieceClickListener {
        fun onPieceClicked(items: Bitmap)
    }


    inner class PieceViewHolder(val binding: ItemPuzzlePieceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val parent = binding.root
        val context: Context = parent.context

        fun bind(bitmap: Bitmap) {

            itemView.setOnClickListener {
                onPieceClickListener.onPieceClicked(bitmap)
                Log.d("TAG", "piece item clicked")
            }

            binding.apply {

                ivPiece.setImageBitmap(bitmap)

            }

        }

    }


    class PieceItemCallback : DiffUtil.ItemCallback<Bitmap>() {
        override fun areItemsTheSame(
            oldItem: Bitmap,
            newItem: Bitmap
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: Bitmap,
            newItem: Bitmap
        ): Boolean {
            return oldItem.sameAs(newItem)
        }

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PieceAdapter.PieceViewHolder {
        return PieceViewHolder(
            ItemPuzzlePieceBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PieceAdapter.PieceViewHolder, position: Int) {
        holder.bind(getItem(position))

    }

}