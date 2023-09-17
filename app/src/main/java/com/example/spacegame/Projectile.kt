package com.example.spacegame

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

class Projectile( x: Float,  y: Float) : Entity() {

    init {
        this.x = x
        this.y = y
    }

    override fun update() {
        x += GameConfig.PROJECTILE_SPEED
    }

    override fun render(canvas: Canvas, paint: Paint) {
        paint.color = Color.RED
        canvas.drawCircle(x, y, GameConfig.PROJECTILE_RADIUS, paint)
    }
}
