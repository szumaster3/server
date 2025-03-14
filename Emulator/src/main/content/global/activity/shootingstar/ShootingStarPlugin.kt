package content.global.activity.shootingstar

import core.ServerStore
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.system.command.Privilege
import core.game.world.GameWorld
import core.tools.Log
import core.tools.secondsToTicks
import org.json.simple.JSONObject
import org.rs.consts.Items
import org.rs.consts.Scenery

class ShootingStarPlugin :
    LoginListener,
    InteractionListener,
    TickListener,
    Commands,
    StartupListener {
    override fun login(player: Player) {
        if (star.isSpawned && !star.spriteSpawned) {
            sendMessage(
                player,
                "<img=12><col=CC6600>News: A shooting star (Level ${star.level.ordinal + 1}) has just crashed near the ${star.location}!",
            )
        }
    }

    override fun tick() {
        ++star.ticks

        val maxDelay = tickDelay + (tickDelay / 3)
        if (star.ticks > maxDelay && star.spriteSpawned) {
            star.clearSprite()
        }

        if ((star.ticks >= tickDelay && !star.spriteSpawned) || (!star.isSpawned && !star.spriteSpawned)) {
            star.fire()
        }
    }

    override fun defineListeners() {
        on(Scenery.SHOOTING_STAR_NOTICEBOARD_38669, IntType.SCENERY, "read") { player, _ ->
            var index = 0
            scoreboardEntries.forEach { entry ->
                val timeElapsed = secondsToTicks(GameWorld.ticks - entry.time) / 60
                sendString(player, "$timeElapsed minutes ago", scoreboardIface, index + 6)
                sendString(player, entry.player, scoreboardIface, index + 11)
                ++index
            }
            openInterface(player, scoreboardIface)
            return@on true
        }

        on(SHOOTING_STARS, IntType.SCENERY, "mine") { player, _ ->
            star.mine(player)
            return@on true
        }

        on(SHOOTING_STARS, IntType.SCENERY, "prospect") { player, _ ->
            star.prospect(player)
            return@on true
        }
    }

    override fun defineCommands() {
        define("tostar", Privilege.ADMIN) { player, _ ->
            teleport(player, star.starScenery.location.transform(1, 1, 0))
        }

        define("submit", Privilege.ADMIN) { _, _ ->
            star.fire()
        }

        define("resetsprite", Privilege.ADMIN) { player, _ ->
            player.savedData.globalData.starSpriteDelay = 0L
        }
    }

    override fun startup() {
        log(this::class.java, Log.FINE, "Shooting Stars initialized.")
    }

    private data class ScoreboardEntry(
        val player: String,
        val time: Int,
    )

    companion object {
        private val star = ShootingStar()
        private val tickDelay = if (GameWorld.settings?.isDevMode == true) 2000 else 25000
        private val scoreboardEntries = ArrayList<ScoreboardEntry>()
        private val scoreboardIface = 787
        val SHOOTING_STARS = ShootingStarType.values().map(ShootingStarType::objectId).toIntArray()
        val STAR_DUST = Items.STARDUST_13727

        @JvmStatic
        fun submitScoreBoard(player: Player) {
            if (scoreboardEntries.size == 5) {
                scoreboardEntries.removeAt(0)
            }
            scoreboardEntries.add(ScoreboardEntry(player.username, GameWorld.ticks))
        }

        @JvmStatic
        fun getStar(): ShootingStar {
            return star
        }

        @JvmStatic
        fun getStoreFile(): JSONObject {
            return ServerStore.getArchive("shooting-star")
        }

        fun getStarDust(player: Player): Int {
            return player.inventory.getAmount(STAR_DUST) + player.bank.getAmount(STAR_DUST)
        }
    }
}
