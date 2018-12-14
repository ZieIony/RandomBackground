package tk.zielony.randombackground.sample

import android.animation.ArgbEvaluator
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import carbon.Carbon.getThemeColor
import kotlinx.android.synthetic.main.activity_main.*
import tk.zielony.randombackground.RandomBackground
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        generate()

        generate.setOnClickListener {
            generate()
            background.postInvalidate()
        }
    }

    private fun generate(){
        val random = Random()
        val colors = arrayOf(Color.RED, Color.GRAY, Color.GREEN, Color.BLUE, Color.CYAN, Color.MAGENTA, Color.YELLOW)

        val particleColor = arrayOf(getThemeColor(this, android.R.attr.colorForeground), getThemeColor(this, android.R.attr.colorForegroundInverse))[random.nextInt(2)]
        val color = ArgbEvaluator().evaluate(0.3f, particleColor, colors[random.nextInt(colors.size)]) as Int
        val intensity = random.nextFloat()/2
        val style = RandomBackground.Style.values()[random.nextInt(RandomBackground.Style.values().size)]
        val count = random.nextInt(10) + 5

        val b = RandomBackground(applicationContext, color, particleColor, intensity, style, count)
        background.setBackgroundDrawable(b)
    }
}
