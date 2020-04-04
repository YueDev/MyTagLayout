package com.womeiyouyuming.android.mylayouttest

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

/**
 * Created by Yue on 2020/4/2.
 */
class MyTagLayout : ViewGroup {

    //用来存储子view的位置
    //用list存不是很好，因为onMeasure里要初始化Rect，IDE不提倡在onMeasure里做初始化
    //可以自定义一个LayoutParams，在每一个子view的LayoutParams里存比较好
    private val childBounds = mutableListOf<Rect>()

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)


        var usedWidth = 0
        var usedHeight = 0

        var maxLineWidth = 0
        var maxLineHeight = 0


        (0 until childCount).forEach {


            val child = getChildAt(it)


            //测量子view，这里不传入usedWidth是因为有的控件无法反馈自己空间不够需要换行，因此给他们足够大的空间，我们稍后自己计算换行
            //measureChildWithMargins这个方法会把付layout的padding和子view的margin减出来，所以得到的数据是view自身的大小，稍后我们在计算子view的位置的时候要把padding和margin考虑到。
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0)

            //获取child的lp，之后可以用lp拿到margin
            val lp =child.layoutParams as MarginLayoutParams

            //获取子view的测量宽高
            val childWidth = child.measuredWidth
            val childHeight = child.measuredHeight

            //childBounds初始为空，需要在使用添加空的Rect，这里初始化Rect不好,IDE会有提示
            if (childBounds.size < it + 1) {
                childBounds.add(Rect())
            }

            //是否需要换行，如果width不是无限大并且子view的宽度+margin+已用宽度 超过layout的可用宽度，需要换行
            //换行就是把useHeight加上之前的maxineHeight

            if (widthMode != MeasureSpec.UNSPECIFIED && childWidth + lp.leftMargin + lp.rightMargin + usedWidth > widthSize - paddingLeft - paddingRight) {
                usedHeight += maxLineHeight
                maxLineHeight = 0

                maxLineWidth = maxLineWidth.coerceAtLeast(usedWidth)
                usedWidth = 0

            }


            //存储子view的四边数据，在layout里布局
            childBounds[it].apply {
                left = usedWidth + paddingLeft + lp.leftMargin
                top = usedHeight + paddingTop + lp.topMargin
                right = left + childWidth
                bottom = top + childHeight
            }

            //测量完成后，更新usedWidth和maxLineHeight, 要加上margin
            usedWidth += childWidth + lp.leftMargin + lp.rightMargin
            maxLineHeight = maxLineHeight.coerceAtLeast(childHeight + lp.bottomMargin + lp.topMargin)

        }

        //测量完全部的子view后，处理maxLineWidth和usedHeight，这两个是layout想要的宽高
        maxLineWidth = maxLineWidth.coerceAtLeast(usedWidth)
        usedHeight += maxLineHeight



        setMeasuredDimension(
            View.resolveSize(maxLineWidth + paddingLeft + paddingRight, widthMeasureSpec),
            View.resolveSize(usedHeight + paddingTop + paddingBottom, heightMeasureSpec)
        )

    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        (0 until childCount).forEach {
            val rect = childBounds[it]
            getChildAt(it).layout(rect.left, rect.top, rect.right, rect.bottom)
        }
    }


    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }


    //不是滑动的viewgroup应该返回false
    //这个方法就是让子view的触摸延迟100ms，来判断是不是滑动状态，默认返回true，非滑动viewgroup要返回false，这样触摸就没有100ms延迟，响应更快
    override fun shouldDelayChildPressedState() = false


}