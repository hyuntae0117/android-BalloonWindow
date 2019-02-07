package com.ht.example.balloonwindow

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.ht.balloonwindow.BalloonWindow
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        aboveBtn.setOnClickListener {
            val view = TextView(this)
            view.text = "position:above\noffset:-35"

            val window = BalloonWindow(this, targetView, BalloonWindow.Position.above)
            window.offset = -35
            window.show(view)

        }
        belowBtn.setOnClickListener {
            val view = TextView(this)
            view.text = "position:below\noffset:55\nmargin:25"

            val window = BalloonWindow(this, targetView, BalloonWindow.Position.below)
            window.offset = 55
            window.margin = 25
            window.show(view)
        }
        rightBtn.setOnClickListener {
            val view = TextView(this)
            view.text = "position:right\noffset:10\npaddingTop:5\npaddtingBottom:5\npaddingRight:5\npaddingLeft:5"

            val window = BalloonWindow(this, targetView, BalloonWindow.Position.right)
            window.offset = 10
            window.paddingTop = 5
            window.paddingBottom = 5
            window.paddingLeft = 5
            window.paddingRight = 5
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
