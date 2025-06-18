package com.surendramaran.yolov8tflite

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import java.util.LinkedList
import kotlin.math.max

class OverlayView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var results = listOf<BoundingBox>()
    private var boxPaint = Paint()
    private var textBackgroundPaint = Paint()
    private var textPaint = Paint()

    private var bounds = Rect()

    init {
        initPaints()
    }

    fun clear() {
        textPaint.reset()
        textBackgroundPaint.reset()
        boxPaint.reset()
        invalidate()
        initPaints()
    }

    private fun initPaints() {
        textBackgroundPaint.color = Color.BLACK
        textBackgroundPaint.style = Paint.Style.FILL
        textBackgroundPaint.textSize = 50f

        textPaint.color = Color.WHITE
        textPaint.style = Paint.Style.FILL
        textPaint.textSize = 50f

        boxPaint.color = ContextCompat.getColor(context!!, R.color.bounding_box_color)
        boxPaint.strokeWidth = 8F
        boxPaint.style = Paint.Style.STROKE
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        results.forEach {
            val left = it.x1 * width
            val top = it.y1 * height
            val right = it.x2 * width
            val bottom = it.y2 * height

            // 步驟 1: 準備顏色和文字
            val colorIndex = it.cls
            val color = if (colorIndex >= 0 && colorIndex < CLASS_COLORS.size) {
                CLASS_COLORS[colorIndex]
            } else {
                ContextCompat.getColor(context!!, R.color.bounding_box_color)
            }

            boxPaint.color = color
            textBackgroundPaint.color = color

            val confidencePercentage = (it.cnf * 100).toInt()
            val drawableText = String.format("%s  cnf.%d%%", it.clsName, confidencePercentage)

            // 步驟 2: 計算文字大小
            textBackgroundPaint.getTextBounds(drawableText, 0, drawableText.length, bounds)
            val textWidth = bounds.width()
            val textHeight = bounds.height()

            // 步驟 3: 繪製文字背景 (放置在偵測框正上方)
            // newTop = 偵測框的 top - 文字高度 - padding
            // 這樣可以確保文字背景和偵測框之間有空隙
            // 使用 max(0f, ...) 確保背景不會畫出螢幕頂部
            val newTop = max(0f, top - textHeight - BOUNDING_RECT_TEXT_PADDING)
            canvas.drawRect(
                left,
                newTop,
                left + textWidth + BOUNDING_RECT_TEXT_PADDING,
                newTop + textHeight + BOUNDING_RECT_TEXT_PADDING,
                textBackgroundPaint
            )

            // 步驟 4: 繪製邊界框
            canvas.drawRect(left, top, right, bottom, boxPaint)

            // 步驟 5: 繪製文字 (在新的文字背景上)
            canvas.drawText(
                drawableText,
                left + (BOUNDING_RECT_TEXT_PADDING / 2),
                newTop + bounds.height() + (BOUNDING_RECT_TEXT_PADDING / 2),
                textPaint
            )
        }
    }

    fun setResults(boundingBoxes: List<BoundingBox>) {
        results = boundingBoxes
        invalidate()
    }

    companion object {
        private const val BOUNDING_RECT_TEXT_PADDING = 8

        // --- START: 新增的程式碼 (專為 17 個類別設計) ---
        private val CLASS_COLORS = listOf(
            // 索引 0-9: 一組高對比度的基礎顏色
            Color.parseColor("#1E88E5"), // 0: 鮮藍色 (Vivid Blue)
            Color.parseColor("#D81B60"), // 1: 洋紅色 (Magenta)
            Color.parseColor("#FB8C00"), // 2: 亮橘色 (Bright Orange)
            Color.parseColor("#8E24AA"), // 3: 深紫色 (Deep Purple)
            Color.parseColor("#00ACC1"), // 4: 青色 (Cyan)
            Color.parseColor("#F4511E"), // 5: 番茄紅 (Tomato Red)
            Color.parseColor("#43A047"), // 6: 中綠色 (Medium Green)
            Color.parseColor("#5E35B1"), // 7: 靛藍色 (Indigo)
            Color.parseColor("#FFD600"), // 8: 金黃色 (Gold)
            Color.parseColor("#6D4C41"), // 9: 棕色 (Brown)
            Color.parseColor("#0277BD"), // 10:淺藍色 (Light Blue)
            Color.parseColor("#D32F2F"), // 11:紅色 (Red)
            Color.parseColor("#2E7D32"), // 12:綠色 (Green)
            Color.parseColor("#FBC02D"), // 13:黃色 (Yellow)
            Color.parseColor("#C2185B"), // 14:深粉色 (Deep Pink)
            Color.parseColor("#757575"), // 15:灰色 (Gray)
            Color.parseColor("#000000")  // 16:橄欖綠 (Olive Green)
        )

    }
}