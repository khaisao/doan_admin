package com.khaipv.attendance.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Size
import android.view.View
import com.kbyai.facesdk.FaceBox
import com.khaipv.attendance.ui.teacher.faceReco.FaceRecoKbiFragment.FaceDataWithName


class FaceView(private var context: Context?, attrs: AttributeSet?) :
    View(context, attrs) {
    private var realPaint: Paint? = null
    private var spoofPaint: Paint? = null
    private var frameSize: Size? = null
    private var faceBoxes: List<FaceBox>? = null
    private var faceDataWithName: List<FaceDataWithName>? = null
    private var name: String? = null

    constructor(context: Context?) : this(context, null) {
        this.context = context
        init()
    }

    init {
        init()
    }

    fun init() {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        realPaint = Paint()
        realPaint!!.style = Paint.Style.STROKE
        realPaint!!.strokeWidth = 3f
        realPaint!!.color = Color.GREEN
        realPaint!!.isAntiAlias = true
        realPaint!!.textSize = 50f
        spoofPaint = Paint()
        spoofPaint!!.style = Paint.Style.STROKE
        spoofPaint!!.strokeWidth = 3f
        spoofPaint!!.color = Color.RED
        spoofPaint!!.isAntiAlias = true
        spoofPaint!!.textSize = 50f
    }

    fun setFrameSize(frameSize: Size?) {
        this.frameSize = frameSize
    }

    fun setFaceBoxes(faceBoxes: List<FaceBox>?, name: String?) {
        this.faceBoxes = faceBoxes
        this.name = name
        invalidate()
    }

    fun setFaceBoxes(faceDataWithName: List<FaceDataWithName>?) {
        this.faceDataWithName = faceDataWithName
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (frameSize != null && faceDataWithName != null) {
            val x_scale = frameSize!!.width / canvas.width.toFloat()
            val y_scale = frameSize!!.height / canvas.height.toFloat()
            for (i in faceDataWithName!!.indices) {
                val faceBox = faceDataWithName!![i].face
                val name = faceDataWithName!![i].name
                realPaint!!.strokeWidth = 3f
                realPaint!!.style = Paint.Style.FILL_AND_STROKE
                canvas.drawText(
                    name, faceBox.x1 / x_scale + 10, faceBox.y1 / y_scale - 30,
                    realPaint!!
                )
                realPaint!!.style = Paint.Style.STROKE
                realPaint!!.strokeWidth = 5f
                canvas.drawRect(
                    Rect(
                        (faceBox.x1 / x_scale).toInt(),
                        (faceBox.y1 / y_scale).toInt(),
                        (faceBox.x2 / x_scale).toInt(),
                        (faceBox.y2 / y_scale).toInt()
                    ),
                    realPaint!!
                )
            }
        } else {
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        }
    }
}
