package com.example.spacegame

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

class GameUI(private val context: Context) {

    fun renderHud(canvas: Canvas, paint: Paint, player: Player, distanceTraveled: Int, isGameOver: Boolean) {
        val textSize = 48f
        val margin = 10f
        paint.color = Color.WHITE
        paint.textAlign = Paint.Align.LEFT
        paint.textSize = textSize

        if (!isGameOver) {
            canvas.drawText("${context.getString(R.string.health)} ${player.health}", margin, textSize, paint)
            canvas.drawText("${context.getString(R.string.distance_traveled)} $distanceTraveled", margin, textSize * 2, paint)
        } else {
            paint.textAlign = Paint.Align.CENTER
            val centerX = GameConfig.STAGE_WIDTH * 0.5f
            val centerY = GameConfig.STAGE_HEIGHT * 0.5f
            canvas.drawText(context.getString(R.string.game_over), centerX, centerY, paint)
            canvas.drawText(context.getString(R.string.press_to_restart), centerX, centerY + textSize, paint)
        }
    }

}
