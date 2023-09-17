package com.example.spacegame

import android.graphics.Canvas
import android.graphics.Paint
import android.util.Log


class Star : Entity() {
    private var speed: Float = 0f
    private val TAG = "STAR"
    private val color: Int = GameConfig.STAR_COLORS[RNG.nextInt(GameConfig.STAR_COLORS.size)]
    private val radius = (RNG.nextInt(6)+2).toFloat()

    init {
        speed = radius /2f
        respawn()
    }

    override fun respawn() {
        x = RNG.nextInt(GameConfig.STAGE_WIDTH).toFloat()
        y = RNG.nextInt(GameConfig.STAGE_HEIGHT).toFloat()
        width = radius * 2f
        height = width
    }

    override fun update() {
        super.update()
        x += -playerSpeed * speed
        if (right() < 0) {
            setLeft(GameConfig.STAGE_WIDTH.toFloat())
            setTop(RNG.nextFloat() * GameConfig.STAGE_HEIGHT)
            // vi börjar med minsta hastigheten, för star ska inte gå under denna hastighet, vi räknar ut mellanskillnaden mella max star speed och min starspeed, detta är så mycket som en stjärnas fart kan öka
            //sedan tar vi ut ett värde mellan 0-1 genam att dela stjärnans radie med den maximala radien en stjärna kan ha.
            //genom att multiplicera kvoten av divisionen med mellanskillnaden av minsta hastigheten och högsta så får vi fram hur mycket hastighet som ska läggas till på den första minsta hastigheten
            // och på detta sätt får vi fram denna stjärnas hastighet baserat på dess framslumpade storlek
            speed = GameConfig.MIN_STAR_SPEED + (GameConfig.MAX_STAR_SPEED - GameConfig.MIN_STAR_SPEED) *(radius /GameConfig.MAX_STAR_RADIUS)

        }
    }

    override fun render(canvas: Canvas, paint: Paint) {
        Log.d(TAG, "Drawing star at ($x, $y)")

        super.render(canvas, paint)
        paint.color = color
        canvas.drawCircle(x, y, radius, paint)
    }
}