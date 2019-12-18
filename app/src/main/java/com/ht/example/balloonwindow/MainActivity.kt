package com.ht.example.balloonwindow

import android.animation.ValueAnimator
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.TextView
import com.ht.balloonwindow.BalloonWindow
import com.ht.balloonwindow.BalloonWindowListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.PI
import kotlin.math.sign
import kotlin.math.sin

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        aboveBtn.setOnClickListener {
            val view = TextView(this)
            view.text = "position:above\noffset:-35\nballoonColor: #F79b00"

            val window = BalloonWindow(this, targetView, BalloonWindow.Position.above)
            window.balloonColor = Color.parseColor("#f79b00")
            window.offset = -35
            window.show(view)

            val anim = ValueAnimator.ofFloat(0f, PI.toFloat())
            anim.duration = 1500
            anim.repeatCount = 3

            window.setBalloonListener(object: BalloonWindowListener {
                override fun didAppear(window: BalloonWindow) {
                    anim.addUpdateListener {
                        val value = it.animatedValue as Float
                        val sign = sin(value)
                        window.update(window.x, (window.y + (30 * sign)).toInt(), window.width, window.height)
                    }
                    anim.start()
                }
            })
        }

        belowBtn.setOnClickListener {
            val view = TextView(this)
            view.text = "position:below\noffset:55\nmargin:25\nballoonColor: #f1f1f1"

            val window = BalloonWindow(this, targetView, BalloonWindow.Position.below)
            window.offset = 55
            window.margin = 25
            window.balloonColor = Color.parseColor("#f1f1f1")
            window.show(view)
        }
        rightBtn.setOnClickListener {
            val view = TextView(this)
            view.text = "position:right\noffset:10\npaddingTop:5\npaddtingBottom:5\npaddingRight:5\npaddingLeft:5\nradius_20 drawable"

            val window = BalloonWindow(this, targetView, BalloonWindow.Position.right)
            window.offset = 10
            window.paddingTop = 5
            window.paddingBottom = 5
            window.paddingLeft = 5
            window.paddingRight = 5
            window.balloonDrawable = ContextCompat.getDrawable(this, R.drawable.bg_blue_30a4ff_round_20)
            window.show(view)
        }
        leftBtn.setOnClickListener {
            val view = TextView(this)
            view.text = "position:left\noffset:-10"

            val window = BalloonWindow(this, targetView, BalloonWindow.Position.left)
            window.offset = -10
            window.show(view)
        }
    }
}
