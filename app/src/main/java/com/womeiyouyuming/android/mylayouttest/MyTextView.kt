package com.womeiyouyuming.android.mylayouttest

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * Created by Yue on 2020/3/31.
 */

class MyTextView : AppCompatTextView {

    //自定义的TextView，随机宽高并加上外边框，随机内容，方便测试MyTagLayout

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val lineWidth = 9f
    private val lineDx = 4f

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        val randomX  = (5..100).random()
        val randomY = (5..50).random()
        setPadding(paddingLeft + randomX, paddingTop + randomY, paddingRight + randomX, paddingBottom + randomY)

        val color = arrayOf(Color.GREEN, Color.BLACK, Color.CYAN, Color.RED, Color.BLUE).random()
        paint.color = color
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = lineWidth

        setTextColor(Color.BLACK)
        textSize = 24f
        text = arrayOf("apple", "bad apple", "orange", "ABC", "moon", "beautiful", "playstation", "switch").random()

    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawRoundRect(lineDx, lineDx, width - lineDx, height - lineDx, 24f, 24f, paint)
    }

}