package com.example.spacegame

import android.content.res.Resources


class Enemy(res: Resources) : BitmapEntity() {

    init {
        var id = R.drawable.enemy_1
        when(RNG.nextInt(1, 6)){
            1-> id = R.drawable.enemy_1
            2-> id = R.drawable.enemy_2
            3-> id = R.drawable.enemy_3
            4-> id = R.drawable.enemy_4
            5-> id = R.drawable.enemy_5
        }
        val bmp = loadBitmap(res, id, GameConfig.ENEMY_HEIGHT)
        setSprite(flipVertically(bmp))
        respawn()
    }

    override fun respawn(){
        x = (GameConfig.STAGE_WIDTH + RNG.nextInt(GameConfig.ENEMY_SPAWN_OFFSET)).toFloat()
        y = RNG.nextInt(GameConfig.STAGE_HEIGHT - GameConfig.ENEMY_HEIGHT).toFloat()
    }

    override fun update() {
        velX = -playerSpeed
        x += velX
        if(right() < 0){
            respawn()
        }
    }

    override fun onCollision(that: Entity) {
        respawn()
    }
}