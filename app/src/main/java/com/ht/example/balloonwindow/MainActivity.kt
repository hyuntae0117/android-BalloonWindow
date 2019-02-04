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
            val window = BalloonWindow(this, targetView, BalloonWindow.Position.above)
            val view = TextView(this)
            view.text = "position:above"
            window.show(view)

        }
        belowBtn.setOnClickListener {
            val window = BalloonWindow(this, targetView, BalloonWindow.Position.below)
            val view = TextView(this)
            view.text = "position:below"
            window.show(view)
        }
        rightBtn.setOnClickListener {
            val window = BalloonWindow(this, targetView, BalloonWindow.Position.right)
            val view = TextView(this)
            view.text = "position:right"
            window.show(view)
        }
        leftBtn.setOnClickListener {
            val window = BalloonWindow(this, targetView, BalloonWindow.Position.left)
            val view = TextView(this)
            view.text = "position:left"
            window.show(view)
        }
    }
}
