package tk.zielony.randombackground.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import tk.zielony.randombackground.RandomBackground

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val b = RandomBackground()
        background.setBackgroundDrawable(b)

        generate.setOnClickListener {
            b.generate()
            background.postInvalidate()
        }
    }
}
