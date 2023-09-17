package com.example.spacegame

import android.content.res.Resources
import android.util.Log
import androidx.core.math.MathUtils.clamp


class Player(res : Resources) : BitmapEntity(){
    private val TAG = "Player"
    private val projectiles = mutableListOf<Projectile>()
    private var lastFired: Long = 0
    private var isInv: Boolean = false
    private var invStartTime: Long = 0
    private var isVisible: Boolean = true

    fun getIsVisible(): Boolean{
        return isVisible
    }

    fun startInv() {
        isInv = true
        invStartTime = System.currentTimeMillis()
    }

    fun endInv() {
        isInv = false
    }

    fun isInvincible(): Boolean {
        return isInv
    }

    var health = GameConfig.PLAYER_STARTING_HEALTH
    init {
        setSprite(loadBitmap(res, R.drawable.player, GameConfig.PLAYER_HEIGHT))
        respawn()
    }

    fun getProjectiles(): List<Projectile> {
        return projectiles
    }

    fun fireProjectile(): Boolean {
        val currentTime = System.currentTimeMillis()

        if (currentTime - lastFired >= GameConfig.FIRE_RATE) {
            lastFired = currentTime
            if (projectiles.size < GameConfig.MAX_PROJECTILES) {
                val newProjectile = Projectile(x + width, y + height / 2)
                projectiles.add(newProjectile)
                return true
            }
        }
        return false
    }

    fun removeProjectile(projectile: Projectile) {
        projectiles.remove(projectile)
    }

    override fun respawn() {
        health = GameConfig.PLAYER_STARTING_HEALTH
        x = GameConfig.PLAYER_STARTING_POSITION
    }

    override fun onCollision(that: Entity) {
        Log.d(TAG, "onCollision")
        health--
    }

    override fun update() {
        velX *= GameConfig.DRAG
        velY += (GameConfig.GRAVITY * 0.75).toFloat()
        if (isBoosting){
            velX *= GameConfig.ACCELERATION
            velY += GameConfig.LIFT
        }
        velX = clamp(velX, GameConfig.MIN_VEL, GameConfig.MAX_VEL)
        velY = clamp(velY, -GameConfig.MAX_VEL, GameConfig.MAX_VEL)

        y +=velY
        playerSpeed = velX
        if (bottom() > GameConfig.STAGE_HEIGHT){
            setBottom(GameConfig.STAGE_HEIGHT.toFloat())
        }
        else if (top() < 0f){
            setTop(0f)
        }
        for (projectile in projectiles) {
            projectile.update()
        }
        if (isInv) {
            isVisible = (System.currentTimeMillis() - invStartTime) % 500 < 250

            if (System.currentTimeMillis() - invStartTime >= GameConfig.PLAYER_INV_TIMER) {
                endInv()
            }
        }
    }
}