package com.example.spacegame

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.SystemClock.uptimeMillis
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.lang.Exception
import kotlin.random.Random

val RNG = Random(uptimeMillis())
@Volatile var isBoosting = false
private val TAG = "Game"


var playerSpeed = 0f

class Game(context: Context) : SurfaceView(context), Runnable, SurfaceHolder.Callback {
    private val prefs = context.getSharedPreferences(GameConfig.PREFS, Context.MODE_PRIVATE)
    private val editor = prefs.edit()
    private val gameUI = GameUI(context)

    private lateinit var gameThread: Thread
    @Volatile
    private var isRunning = false
    private var isGameOver = false
    private val jukebox = Jukebox(context.assets)
    private val player = Player(resources)
    private var entities = ArrayList<Entity>()
    private val paint = Paint()
    private var distanceTraveled = 0
    private var maxDistanceTraveled = 0
    @Volatile var isFiring = false
    var playedDeathSound = false


    init{
        Log.d(TAG, "INIT FOR GAME WORKING")
        holder.addCallback(this)
        holder.setFixedSize(GameConfig.STAGE_WIDTH, GameConfig.STAGE_HEIGHT)
        for(i in 0 until GameConfig.STAR_COUNT){
            entities.add(Star())
        }
        for(i in 0 until GameConfig.ENEMY_COUNT){
            entities.add(Enemy(resources))
        }
    }



    private fun restart() {
        for(entity in entities){
            entity.respawn()
        }
        jukebox.play(SFX.start)
        playedDeathSound = false
        player.respawn()
        distanceTraveled = 0
        maxDistanceTraveled = prefs.getInt(GameConfig.LONGEST_DIST, 0)
        isGameOver = false
    }

    override fun run() {
        while(isRunning) {
            update()
            render()
        }
    }

    private fun update() {
        player.update()
        for(entity in entities){
            entity.update()
        }

        distanceTraveled += playerSpeed.toInt()
        checkCollisions()
        checkGameOver()

        for (projectile in player.getProjectiles()) {
            projectile.update()
        }


        val toRemove = mutableListOf<Projectile>()
        for (projectile in player.getProjectiles()) {
            if (projectile.x < 0 || projectile.x > GameConfig.STAGE_WIDTH) {
                toRemove.add(projectile)
            }
        }
        for (projectile in toRemove) {
            player.removeProjectile(projectile)
        }

        val newEnemies = mutableListOf<Enemy>()
        val entitiesC = ArrayList(entities)
        val iter = entitiesC.iterator()

        while (iter.hasNext()) {
            val entity = iter.next()
            if (entity is Enemy) {
                for (projectile in player.getProjectiles()) {
                    if (isColliding(projectile, entity)) {
                        entities.remove(entity)
                        newEnemies.add(Enemy(resources))
                        player.removeProjectile(projectile)
                        break
                    }
                }
            }
        }
        entities.addAll(newEnemies)

        if (isFiring) {
            val fired = player.fireProjectile()
            if (fired) {
                jukebox.play(SFX.fireprojectile)
            }
        }
    }



    private fun checkGameOver() {
        if(player.health < 1 && !playedDeathSound){
            isGameOver = true
            jukebox.play(SFX.death)
            playedDeathSound = true
            if(distanceTraveled > maxDistanceTraveled){
                editor.putInt(GameConfig.LONGEST_DIST, distanceTraveled)
                editor.apply()
            }
        }

    }

    private fun checkCollisions() {

        for(i in GameConfig.STAR_COUNT until entities.size){
            val enemy = entities[i]
            if(isColliding(enemy, player)){
                if(!player.isInvincible()) {
                    enemy.onCollision(player)
                    player.onCollision(enemy)
                    jukebox.play(SFX.crash)
                    player.startInv()
                }
            }
        }
    }

    private fun render() {
        val entitiesList= ArrayList(entities)
        Log.d(TAG, "RENDER in GAME IS BEING CALLED")
        val canvas = acquireAndLockCanvas() ?: return
        canvas.drawColor(GameConfig.BACKGROUND_COLOR)

        for(entity in entitiesList){
            entity.render(canvas, paint)
        }

        for (projectile in player.getProjectiles()) {
            projectile.render(canvas, paint)
        }

        if(player.getIsVisible()) {
            player.render(canvas, paint)
        }
        gameUI.renderHud(canvas, paint, player, distanceTraveled, isGameOver)
        holder.unlockCanvasAndPost(canvas)
    }

    private fun acquireAndLockCanvas() : Canvas?{
        if(holder?.surface?.isValid == false){
            return null
        }

        return holder.lockCanvas()
    }

    fun pause() {
        Log.d(TAG, "Paus")
        isRunning = false
        try{
            gameThread.join()
        }
        catch (e: Exception) {/*swallow exception, we're exiting anyway*/}
    }

    fun resume() {
        Log.d(TAG, "Resume")
        isRunning = true
        gameThread = Thread(this)
        gameThread.start()
    }
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_UP -> {
                Log.d(TAG, "slowing down")
                isBoosting = false
                isFiring = false // stops firing
            }
            MotionEvent.ACTION_DOWN -> {
                Log.d(TAG, "isBoosting")
                if(isGameOver){
                    restart()
                }
                else{
                    isBoosting = true
                    isFiring = true
                }
            }
        }
        return true
    }

    override fun surfaceCreated(p0: SurfaceHolder) {
        Log.d(TAG, "surface created")
        resume()
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
        Log.d(TAG, "surface changed, width: $width, height: $height")
    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        Log.d(TAG, "surface destroyed")

    }
}