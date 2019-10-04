package com.ht.balloonwindow

import android.app.ActionBar
import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.PopupWindow
import android.widget.TextView
import kotlinx.android.synthetic.main.view_balloon.view.*


open class BalloonWindow : PopupWindow {
    enum class Position {
        above, left, right, below
    }
    var context: Context

    var targetView: View
    var position: Position
    var arrowHeight = 12.toPx()
    var offset: Int
    var margin: Int = 4.toPx()

    var paddingLeft = 32.toPx()
    var paddingRight = 32.toPx()
    var paddingTop = 24.toPx()
    var paddingBottom = 24.toPx()

    var balloonColor: Int? = null

    var x: Int = 0
    var y: Int = 0

    private var measuredContentsWidth: Int? = null
    private var measuredContentsHeight: Int? = null

    var listener: BalloonWindowListener? = null

    constructor(context: Context, targetView: View, position: Position, offset: Int = 0) : super(context) {
        this.context = context
        this.position = position
        this.offset = offset
        this.targetView = targetView
        isOutsideTouchable = true
        isFocusable = true
        setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(context, android.R.color.transparent)))
    }

    override fun dismiss() {
        listener?.willDisappear(this)

        val animation = AlphaAnimation(1f, 0f)
        animation.duration = 100
        contentView.startAnimation(animation)
        animation.setAnimationListener(object: Animation.AnimationListener{
            override fun onAnimationRepeat(p0: Animation?) {}

            override fun onAnimationEnd(p0: Animation?) {
                super@BalloonWindow.dismiss()

                listener?.didDisappear(this@BalloonWindow)
            }

            override fun onAnimationStart(p0: Animation?) {
            }
        })
    }

    open fun show(view: View) {
        listener?.willAppear(this)

        contentView = BalloonView(context, view)
        val balloonView = (contentView as BalloonView)
        balloonView.contentsLl.setPadding(
                paddingLeft, paddingTop, paddingRight, paddingBottom
        )

        height = ViewGroup.LayoutParams.WRAP_CONTENT
        width = ViewGroup.LayoutParams.WRAP_CONTENT
        showAtLocation(contentView, Gravity.NO_GRAVITY, 0, 0)
    }

    fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        paddingLeft = left
        paddingRight = right
        paddingTop = top
        paddingBottom = bottom
    }

    fun setBalloonListener(listener: BalloonWindowListener) {
        this.listener = listener
    }

    open fun getTargetAbsolutePosition(view: View): Pair<Int, Int> {
        val locations = IntArray(2)
        view.getLocationOnScreen(locations)
        val xPos = locations[0]
        val yPos = locations[1]

        return Pair(xPos, yPos)
    }

    open fun onBalloonMeasured() {
        if (measuredContentsWidth == contentView.measuredWidth && measuredContentsHeight == contentView.measuredHeight) {
            return
        }
        measuredContentsWidth = contentView.measuredWidth
        measuredContentsHeight = contentView.measuredHeight

        var (xPos, yPos) = getTargetAbsolutePosition(targetView)
        val pivotX: Float
        val pivotY: Float
        val margin = margin.toPx()
        val offset = offset.toPx()
        when(position) {
            Position.below -> {
                xPos = xPos - (contentView.measuredWidth / 2) + (targetView.measuredWidth / 2) + offset
                yPos += (targetView.measuredHeight) + margin
                pivotX = (0.5f * contentView.measuredWidth + offset * -1) / contentView.measuredWidth
                pivotY = 0f
            }

            Position.above -> {
                xPos = xPos - (contentView.measuredWidth / 2) + (targetView.measuredWidth / 2) + offset
                yPos = yPos - (contentView.measuredHeight) - margin
                pivotX = (0.5f * contentView.measuredWidth + offset * -1) / contentView.measuredWidth
                pivotY = 1f
            }

            Position.right -> {
                xPos += targetView.measuredWidth + margin
                yPos = yPos - (contentView.measuredHeight / 2) + targetView.measuredHeight / 2 + offset
                pivotX = 0f
                pivotY = (0.5f * contentView.measuredHeight + offset * -1) / contentView.measuredHeight
            }

            Position.left -> {
                xPos = xPos - contentView.measuredWidth - margin
                yPos = yPos - (contentView.measuredHeight / 2) + targetView.measuredHeight / 2 + offset
                pivotX = 1f
                pivotY = (0.5f * contentView.measuredHeight + offset * -1) / contentView.measuredHeight
            }
        }

        update(xPos, yPos, this@BalloonWindow.width, this@BalloonWindow.height)

        x = xPos
        y = yPos
        val anim = ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, pivotX, Animation.RELATIVE_TO_SELF, pivotY)
        anim.duration = 300
        contentView.startAnimation(anim)
        anim.setAnimationListener(object: Animation.AnimationListener{
            override fun onAnimationStart(p0: Animation?) {
            }

            override fun onAnimationRepeat(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                listener?.didAppear(this@BalloonWindow)
            }
        })
    }

    inner class BalloonView: ConstraintLayout {

        constructor(context: Context, view: View): super(context) {
            LayoutInflater.from(context).inflate(R.layout.view_balloon, this, true)
            contentsLl.addView(view)

            if (balloonColor != null) {
                arrowTopIv.setColorFilter(balloonColor!!, PorterDuff.Mode.SRC_ATOP)
                arrowRightIv.setColorFilter(balloonColor!!, PorterDuff.Mode.SRC_ATOP)
                contentsLl.background.setColorFilter(balloonColor!!, PorterDuff.Mode.SRC_ATOP)
            }

            val set = ConstraintSet()
            set.constrainWidth(R.id.arrowRightIv, ConstraintSet.WRAP_CONTENT)
            set.constrainHeight(R.id.arrowRightIv,ConstraintSet.WRAP_CONTENT)
            set.constrainWidth(R.id.arrowTopIv, ConstraintSet.WRAP_CONTENT)
            set.constrainHeight(R.id.arrowTopIv,ConstraintSet.WRAP_CONTENT)
            set.constrainWidth(R.id.contentsLl, ConstraintSet.WRAP_CONTENT)
            set.constrainHeight(R.id.contentsLl, ConstraintSet.WRAP_CONTENT)

            val offset = offset.toPx()

            when(position) {
                Position.below -> {
                    set.connect(R.id.arrowTopIv, ConstraintSet.TOP, R.id.container, ConstraintSet.TOP)
                    set.connect(R.id.arrowTopIv, ConstraintSet.LEFT, R.id.container, ConstraintSet.LEFT, offset * -2)
                    set.connect(R.id.arrowTopIv, ConstraintSet.RIGHT, R.id.container, ConstraintSet.RIGHT, offset * 2)

                    set.connect(R.id.contentsLl, ConstraintSet.TOP, R.id.container, ConstraintSet.TOP, arrowHeight)
                    if (offset > 0)
                        set.connect(R.id.contentsLl, ConstraintSet.LEFT, R.id.container, ConstraintSet.LEFT)
                    else
                        set.connect(R.id.contentsLl, ConstraintSet.RIGHT, R.id.container, ConstraintSet.RIGHT)
                    set.applyTo(container)


                    arrowTopIv.visibility = View.VISIBLE
                    arrowRightIv.visibility = View.GONE
                }
                Position.above -> {
                    set.connect(R.id.arrowTopIv, ConstraintSet.BOTTOM, R.id.container, ConstraintSet.BOTTOM)
                    set.connect(R.id.arrowTopIv, ConstraintSet.LEFT, R.id.container, ConstraintSet.LEFT, offset * -2)
                    set.connect(R.id.arrowTopIv, ConstraintSet.RIGHT, R.id.container, ConstraintSet.RIGHT,offset * 2)

                    set.connect(R.id.contentsLl, ConstraintSet.BOTTOM, R.id.container, ConstraintSet.BOTTOM, arrowHeight)
                    set.applyTo(container)
                    arrowTopIv.rotation = 180f

                    arrowTopIv.visibility = View.VISIBLE
                    arrowRightIv.visibility = View.GONE
                }

                Position.right -> {
                    set.connect(R.id.arrowRightIv, ConstraintSet.BOTTOM, R.id.container, ConstraintSet.BOTTOM, offset * 2)
                    set.connect(R.id.arrowRightIv, ConstraintSet.TOP, R.id.container, ConstraintSet.TOP, offset * -2)
                    set.connect(R.id.arrowRightIv, ConstraintSet.LEFT, R.id.container, ConstraintSet.LEFT )

                    set.connect(R.id.contentsLl, ConstraintSet.LEFT, R.id.container, ConstraintSet.LEFT, arrowHeight)
                    set.applyTo(container)
                    arrowRightIv.rotation = 180f
                    arrowTopIv.visibility = View.GONE
                    arrowRightIv.visibility = View.VISIBLE
                }

                Position.left -> {
                    set.connect(R.id.arrowRightIv, ConstraintSet.BOTTOM, R.id.container, ConstraintSet.BOTTOM, offset * 2)
                    set.connect(R.id.arrowRightIv, ConstraintSet.TOP, R.id.container, ConstraintSet.TOP, offset * -2)
                    set.connect(R.id.arrowRightIv, ConstraintSet.RIGHT, R.id.container, ConstraintSet.RIGHT)

                    set.connect(R.id.contentsLl, ConstraintSet.RIGHT, R.id.container, ConstraintSet.RIGHT, arrowHeight)
                    set.applyTo(container)
                    arrowTopIv.visibility = View.GONE
                    arrowRightIv.visibility = View.VISIBLE
                }
            }

        }

        override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            this@BalloonWindow.onBalloonMeasured()
        }
    }
}
