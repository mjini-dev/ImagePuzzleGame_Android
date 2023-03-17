package com.mjin.imagepuzzlegame

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mjin.imagepuzzlegame.databinding.ItemPuzzlePieceBinding
import com.mjin.imagepuzzlegame.model.Piece

class PieceAdapter(
    private val onPieceLongClickListener: OnPieceLongClickListener
) :
    ListAdapter<Piece, PieceAdapter.PieceViewHolder>(PieceItemCallback()) {


    interface OnPieceLongClickListener {
        fun onPieceLongClicked(items: Piece, view: View)
    }


    inner class PieceViewHolder(val binding: ItemPuzzlePieceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(piece: Piece) {
            itemView.setOnLongClickListener {
                it.tag = ""+piece.pX+","+piece.pY
                onPieceLongClickListener.onPieceLongClicked(piece,it)

                return@setOnLongClickListener true
            }

            binding.apply {
                ivPiece.setImageBitmap(piece.bitmap)

            }
        }
    }


    class PieceItemCallback : DiffUtil.ItemCallback<Piece>() {
        override fun areItemsTheSame(
            oldItem: Piece,
            newItem: Piece
        ): Boolean {
            return oldItem.bitmap == newItem.bitmap
        }

        override fun areContentsTheSame(
            oldItem: Piece,
            newItem: Piece
        ): Boolean {
            return oldItem == newItem
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