package com.diogoizele.ticTacToeGame

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginBottom
import com.diogoizele.ticTacToeGame.databinding.ScoreboardItemBinding

class ScoreboardView @JvmOverloads constructor (context: Context, attrs: AttributeSet, defStyleAttr: Int = 0)
    : ConstraintLayout(context, attrs, defStyleAttr) {

    private var binding: ScoreboardItemBinding =
        ScoreboardItemBinding.inflate(LayoutInflater.from(context), this, false)
    private var scoreCount: Int? = null
        set(value) {
            binding.tvScoreCounter.text = value.toString()
            field = value
        }

    init {

        addView(binding.root)

        attrs.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.ScoreboardView, 0, 0)
            val playerIcon = typedArray.getResourceId(R.styleable.ScoreboardView_src, 0)
            typedArray.recycle()

            setPlayerIcon(playerIcon)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        scoreCount?.let {
            binding.ivPlayerIcon.bottom = 10
        }
    }

    private fun setPlayerIcon (id: Int) {
        binding.ivPlayerIcon.setImageResource(id)
    }

    fun setOpacity(value: Float) {
        this.alpha = value
    }

    fun initializeScore() {
        this.scoreCount = 0
        binding.llScoreCounter.visibility = VISIBLE
    }

    fun setScore(score: Int) {
        this.scoreCount = score
    }

}