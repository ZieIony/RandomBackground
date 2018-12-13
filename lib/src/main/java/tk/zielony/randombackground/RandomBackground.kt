package tk.zielony.randombackground

import android.animation.ArgbEvaluator
import android.graphics.*
import android.graphics.drawable.Drawable
import java.util.*

class RandomBackground : Drawable {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var bitmap: Bitmap? = null
    private var color: Int
    private val random = Random()
    private var style: Style
    private val count: Int

    private val colors = arrayOf(Color.RED, Color.GRAY, Color.GREEN, Color.BLUE, Color.CYAN, Color.MAGENTA, Color.YELLOW)

    enum class Style {
        RECT, CIRCLE, HEX
    }

    constructor() {
        color = ArgbEvaluator().evaluate(0.2f, Color.WHITE, 0xff008577.toInt()) as Int
        style = Style.values()[random.nextInt(Style.values().size)]
        count = random.nextInt(10) + 5
    }

    constructor(color: Int, style: Style, count: Int) {
        this.color = color
        this.style = style
        this.count = count
    }

    fun generate() {
        color = ArgbEvaluator().evaluate(0.2f, Color.WHITE, colors[random.nextInt(colors.size)]) as Int

        val p = Paint()
        val width = bounds.width()
        val height = bounds.height()
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap!!)
        val gradient =
            LinearGradient(0.0f, 0.0f, 0.0f, height.toFloat(), Color.WHITE, color, Shader.TileMode.CLAMP)
        p.shader = gradient
        canvas.drawRect(0.0f, 0.0f, width.toFloat(), height.toFloat(), p)

        p.shader = null
        p.color = 0xffffffff.toInt()
        //p.setShadowLayer(20.0f, 0.0f, 5.0f, 0x7f000000)
        p.alpha = 0x2f

        val min = Math.min(width, height) / 6
        for (i in 0 until count) {
            //  canvas.saveLayer(0.0f,0.0f,width.toFloat(), height.toFloat(),p, Canvas.ALL_SAVE_FLAG)
            //p.alpha = 0xff
            canvas.drawCircle(
                random.nextInt(width).toFloat(),
                random.nextInt(height).toFloat(),
                (random.nextInt(min) + min).toFloat(),
                p
            )
            //p.alpha = 0x2f
            //canvas.restore()
        }
    }

    override fun setBounds(bounds: Rect) {
        super.setBounds(bounds)
        generate()
    }

    override fun setBounds(left: Int, top: Int, right: Int, bottom: Int) {
        super.setBounds(left, top, right, bottom)
        generate()
    }

    override fun draw(canvas: Canvas) {
        canvas.drawBitmap(bitmap!!, 0.0f, 0.0f, paint)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun getOpacity(): Int {
        return PixelFormat.OPAQUE
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }
}