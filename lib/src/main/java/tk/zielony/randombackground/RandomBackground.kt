package tk.zielony.randombackground

import android.animation.ArgbEvaluator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.TypedValue
import java.util.*


class RandomBackground : Drawable {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var bitmap: Bitmap? = null
    private var color: Int
    private val random = Random()
    private val intensity: Float
    private var style: Style
    private var count: Int
    private var particleColor: Int
    private val context: Context

    enum class Style {
        RECT, CIRCLE, HEX
    }

    constructor(context: Context) {
        this.context = context
        particleColor = arrayOf(
            getThemeColor(context, android.R.attr.colorForeground),
            getThemeColor(context, android.R.attr.colorForegroundInverse)
        )[random.nextInt(2)]
        intensity = 0.3f
        color = ArgbEvaluator().evaluate(intensity, particleColor, getThemeColor(context, R.attr.colorPrimary)) as Int
        style = Style.values()[random.nextInt(Style.values().size)]
        count = random.nextInt(10) + 5
    }

    constructor(context: Context, color: Int, particleColor: Int, intensity: Float, style: Style, count: Int) {
        this.context = context
        this.color = color
        this.particleColor = particleColor
        this.intensity = Math.max(0.0f, Math.min(intensity, 1.0f))
        this.style = style
        this.count = count
    }

    private fun getThemeColor(context: Context, attr: Int): Int {
        val theme = context.theme
        val typedValue = TypedValue()
        theme.resolveAttribute(attr, typedValue, true)
        return if (typedValue.resourceId != 0) context.resources.getColor(typedValue.resourceId) else typedValue.data
    }

    fun generate() {
        val p = Paint()
        val width = bounds.width()
        val height = bounds.height()
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap!!)
        val gradient =
            LinearGradient(0.0f, 0.0f, 0.0f, height.toFloat(), particleColor, color, Shader.TileMode.CLAMP)
        p.shader = gradient
        canvas.drawRect(0.0f, 0.0f, width.toFloat(), height.toFloat(), p)

        p.shader = null
        p.color = particleColor
        p.setShadowLayer(
            context.resources.getDimension(R.dimen.randomBackground_blurSize),
            0.0f,
            context.resources.getDimension(R.dimen.randomBackground_blurOffset),
            particleColor
        )
        p.alpha = (0xff * intensity).toInt()

        val min = Math.min(width, height) / 6
        for (i in 0 until count) {
            when (style) {
                Style.CIRCLE ->
                    canvas.drawCircle(
                        random.nextInt(width).toFloat(),
                        random.nextInt(height).toFloat(),
                        (random.nextInt(min) + min).toFloat(),
                        p
                    )
                Style.RECT -> {
                    val x = random.nextInt(width).toFloat()
                    val y = random.nextInt(height).toFloat()
                    val size = (random.nextInt(min) + min).toFloat()

                    canvas.drawRect(x - size / 2, y - size / 2, x + size / 2, y + size / 2, p)
                }
                Style.HEX -> {
                    val x = random.nextInt(width).toFloat()
                    val y = random.nextInt(height).toFloat()
                    val size = (random.nextInt(min) + min).toFloat()
                    canvas.save()
                    canvas.translate(x, y)
                    canvas.drawPath(getHexagonPath(size), p)
                    canvas.restore()
                }
            }
        }
    }

    private fun getHexagonPath(radius: Float): Path {
        val triangleHeight = (Math.sqrt(3.0) * radius / 2).toFloat()
        val path = Path()
        path.moveTo(0.0f, radius)
        path.lineTo(-triangleHeight, radius / 2)
        path.lineTo(-triangleHeight, -radius / 2)
        path.lineTo(0.0f, -radius)
        path.lineTo(triangleHeight, -radius / 2)
        path.lineTo(triangleHeight, radius / 2)
        path.moveTo(0.0f, radius)
        return path
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
