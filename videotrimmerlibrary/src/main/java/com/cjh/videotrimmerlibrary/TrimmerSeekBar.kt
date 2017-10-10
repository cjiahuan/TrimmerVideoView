package com.cjh.videotrimmerlibrary

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.cjh.videotrimmerlibrary.callback.EndTouchActionListener
import com.cjh.videotrimmerlibrary.callback.UpdatePosListener
import com.cjh.videotrimmerlibrary.utils.DensityUtils

/**
 * Created by cjh on 2017/8/30.
 */
class TrimmerSeekBar : View {

    var leftPosX = 0f
    var rightPosX = 0f
    var actionDownPosX = -1f
    var cursorWidth = 0
    var offsetValue = 0
    val perSecondWidth = 0
    var side = ""

    var shadowPaint: Paint? = null
    var trimmerPaint: Paint? = null
    var cursorPaint: Paint? = null

    var leftShadowRect: Rect? = null
    var rightShadowRect: Rect? = null
    var trimmerRect: Rect? = null

    var leftCursorRect: Rect? = null
    var rightCursorRect: Rect? = null

    var leftCursorBitmap: Bitmap? = null
    var rightCursorBitmap: Bitmap? = null

    var imeasureWidth = 0

    var updateThumbBySeekBar: EndTouchActionListener? = null

    var updatePosListener: UpdatePosListener? = null

    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        offsetValue = DensityUtils.dip2px(context, Constant.DEFAULT_SEEKBAR_OFFSET_VALUE.toFloat())
        initialPaints()
    }

    private fun initialPaints() {
        initialShadowPaint()
        initialTrimmerPaint()
        initialCursorPaint()
    }

    private fun initialShadowPaint() {
        shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        shadowPaint?.color = Color.parseColor(MediaHandleManager.getInstance().getConfigVo().seekBarShaowColor)
        shadowPaint?.style = Paint.Style.FILL_AND_STROKE
    }

    private fun initialTrimmerPaint() {
        trimmerPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        trimmerPaint?.color = Color.parseColor(MediaHandleManager.getInstance().getConfigVo().seekBarStrokeColor)
        trimmerPaint?.strokeWidth = DensityUtils.dip2px(context, MediaHandleManager.getInstance().getConfigVo().seekBarStrokeWidth.toFloat()).toFloat()
        trimmerPaint?.style = Paint.Style.STROKE
    }

    private fun initialCursorPaint() {
        cursorPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        cursorPaint?.color = Color.parseColor(MediaHandleManager.getInstance().getConfigVo().seekBarStrokeColor)
        cursorPaint?.style = Paint.Style.FILL_AND_STROKE
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        var imeasureHeight = 0
        if (widthMode == MeasureSpec.EXACTLY) {
            imeasureWidth = widthSize
            Log.e("imeasureWidth", imeasureWidth.toString())
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            imeasureHeight = heightSize
        }
        setMeasuredDimension(imeasureWidth, imeasureHeight)
        if (leftShadowRect == null) {
            rightPosX = imeasureWidth.toFloat()
            cursorWidth = imeasureWidth / 32
            leftShadowRect = Rect(0, 0, imeasureWidth, imeasureHeight)
            rightShadowRect = Rect(0, 0, imeasureWidth, imeasureHeight)
            trimmerRect = Rect(0, 0, imeasureWidth, imeasureHeight)
            leftCursorRect = Rect(0, 0, imeasureWidth, imeasureHeight)
            rightCursorRect = Rect(0, 0, imeasureWidth, imeasureHeight)
        }
    }

    private fun setBitmap(leftRes: Int, rightRes: Int) {
        leftCursorBitmap = (context.resources.getDrawable(leftRes) as BitmapDrawable).bitmap
        if (rightRes == null || rightRes == 0 || rightRes == -1) {
            rightCursorBitmap = leftCursorBitmap
        }
        rightCursorBitmap = (context.resources.getDrawable(rightRes) as BitmapDrawable).bitmap
    }

    //    - - - - - - - - - -- - - - - - - - - - - - - - - - -  Touch logic - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) {
            return super.onTouchEvent(event)
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN -> if (checkMoveAvailableByDownEvent(event)) return true
            MotionEvent.ACTION_MOVE -> if (isAvailableMove(event)) return true
            MotionEvent.ACTION_UP -> endAction(event)
            MotionEvent.ACTION_CANCEL -> endAction(event)
        }
        return super.onTouchEvent(event)
    }

    private fun checkMoveAvailableByDownEvent(event: MotionEvent): Boolean {
        actionDownPosX = event.x
        whichSide()
        return isLeft() || isRight()
    }

    private fun whichSide() {
        when (actionDownPosX) {
            in (leftPosX - offsetValue)..(leftPosX + cursorWidth + offsetValue) -> side = ActionSideType.LEFT
            in (rightPosX - cursorWidth - offsetValue)..(rightPosX + offsetValue) -> side = ActionSideType.RIGHT
            else -> side = ""
        }
    }

    private fun isLeft(): Boolean {
        return side.equals(ActionSideType.LEFT)
    }

    private fun isRight(): Boolean {
        return side.equals(ActionSideType.RIGHT)
    }

    private fun isAvailableMove(event: MotionEvent): Boolean {
        val movingX = event.x
        val available: Boolean
        available = when {
            isLeft() -> actionMoveLeft(movingX)
            isRight() -> actionMoveRight(movingX)
            else -> false
        }
        if (available) {
            updatePosListener?.updatePos()
        }
        return available
    }

    private fun actionMoveLeft(movingX: Float): Boolean {
        if (movingX == leftPosX) {
            return false
        }
        if (movingX in 0f..(rightPosX - perSecondWidth)) {
            leftPosX = movingX
            postInvalidate()
            return true
        }
        return false
    }

    private fun actionMoveRight(movingX: Float): Boolean {
        if (movingX == rightPosX) {
            return false
        }
        if (movingX in (leftPosX + perSecondWidth)..width.toFloat()) {
            rightPosX = movingX
            postInvalidate()
            return true
        }
        return false
    }

    private fun endAction(event: MotionEvent) {
        side = ""
        actionDownPosX = -1f
        if (isLeft()) leftPosX = event.x else if (isRight()) rightPosX = event.x else super.onTouchEvent(event)
        Log.e("201::", leftPosX.toString())
        Log.e("202::", rightPosX.toString())
        updateThumbBySeekBar?.updateRegionIndex()
    }

    //    - - - - - - - - - -- - - - - - - - - - - - - - - - -  draw logic - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) return
        drawShadowRects(canvas)
        drawTrimmerRect(canvas)
        drawCursor(canvas)
    }

    private fun drawShadowRects(canvas: Canvas) {
        if (leftShadowRect == null || rightShadowRect == null) return
        leftShadowRect!!.right = leftPosX.toInt()
        rightShadowRect!!.left = rightPosX.toInt()
        canvas.drawRect(leftShadowRect, shadowPaint)
        canvas.drawRect(rightShadowRect, shadowPaint)
    }

    private fun drawTrimmerRect(canvas: Canvas) {
        if (trimmerRect == null) return
        trimmerRect!!.left = leftPosX.toInt()
        trimmerRect!!.right = rightPosX.toInt()
        canvas.drawRect(trimmerRect, trimmerPaint)
    }

    private fun drawCursor(canvas: Canvas) {
        if ((leftCursorBitmap == null || leftCursorRect == null) && leftCursorRect != null && rightCursorRect != null) {
            leftCursorRect!!.left = leftPosX.toInt()
            leftCursorRect!!.right = leftPosX.toInt() + cursorWidth
            rightCursorRect!!.left = rightPosX.toInt() - cursorWidth
            rightCursorRect!!.right = rightPosX.toInt()
            canvas.drawRect(leftCursorRect, cursorPaint)
            canvas.drawRect(rightCursorRect, cursorPaint)
        }
    }

    fun addEndActionListener(listener: EndTouchActionListener) {
        updateThumbBySeekBar = listener
    }

    fun addUpdatePosListener(updatePosListener: UpdatePosListener) {
        this.updatePosListener = updatePosListener
    }

    fun changePaints() {
        offsetValue = MediaHandleManager.getInstance().getConfigVo().offsetValue
        shadowPaint?.color = Color.parseColor(MediaHandleManager.getInstance().getConfigVo().seekBarShaowColor)
        trimmerPaint?.color = Color.parseColor(MediaHandleManager.getInstance().getConfigVo().seekBarStrokeColor)
        trimmerPaint?.strokeWidth = DensityUtils.dip2px(context, MediaHandleManager.getInstance().getConfigVo().seekBarStrokeWidth.toFloat()).toFloat()
        cursorPaint?.color = Color.parseColor(MediaHandleManager.getInstance().getConfigVo().seekBarStrokeColor)
        postInvalidate()
    }
}