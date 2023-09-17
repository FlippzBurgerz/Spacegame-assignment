package com.example.spacegame

import android.graphics.Color

class GameConfig {
    companion object {
        const val ENEMY_HEIGHT = 60
        const val ENEMY_COUNT = 10
        const val STAR_COUNT = 40
        val STAR_COLORS = arrayOf(Color.YELLOW, Color.WHITE, Color.CYAN)
        const val MIN_STAR_SPEED = 0.5f
        const val MAX_STAR_SPEED = 1.5f
        const val MAX_STAR_RADIUS = 5
        const val PLAYER_HEIGHT = 75
        const val PLAYER_STARTING_HEALTH = 3
        const val PLAYER_STARTING_POSITION = 10f
        const val PLAYER_INV_TIMER = 2000 // 2 seconds
        const val ACCELERATION = 1.1f
        const val MIN_VEL = 0.1f
        const val MAX_VEL = 20f
        const val GRAVITY = 0.6f
        const val LIFT = -(GRAVITY*2f)
        const val DRAG = 0.97f
        const val STAGE_WIDTH = 1080
        const val STAGE_HEIGHT = 720
        const val ENEMY_SPAWN_OFFSET = STAGE_WIDTH*2
        const val PROJECTILE_SPEED = 8f
        const val MAX_PROJECTILES = 10
        const val PROJECTILE_RADIUS = 7f
        const val FIRE_RATE = 200  // in milliseconds
        const val LONGEST_DIST = "longest_distance"
        const val PREFS = "com.example.spacegame"
        val BACKGROUND_COLOR = Color.parseColor("#00008B")





    }
}