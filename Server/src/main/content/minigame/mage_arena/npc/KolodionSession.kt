package content.minigame.mage_arena.npc

import core.api.*
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.map.Location
import shared.consts.Graphics

class KolodionSession(val player: Player) {
    private val kolodion =
        KolodionNPC(
            KolodionNPC.KolodionType.values()[player.getSavedData().activityData.kolodionBoss].npcId,
            Location(3106, 3934, 0),
            this,
        )

    init {
        if (player.getExtension<Any?>(KolodionSession::class.java) != null) {
            player.removeExtension(KolodionSession::class.java)
        }
        player.addExtension(KolodionSession::class.java, this)
    }

    fun start() {
        if (kolodion.type!!.ordinal > 0) {
            kolodion.init()
            sendChat(kolodion, "Let us continue with our battle.")
            kolodion.properties.combatPulse.attack(player)
            unlock(player)
            resetAnimator(player)
            return
        }
        submitIndividualPulse(
            player,
            object : Pulse(1, player) {
                var count: Int = 0
                override fun pulse(): Boolean {
                    when (++count) {
                        3 -> resetAnimator(player)
                        5 -> kolodion.init().also {
                            visualize(this@KolodionSession.kolodion, -1, Graphics.RE_PUFF_86)
                            face(kolodion, player, 1)
                        }
                        7 -> sendChat(kolodion, "You must prove yourself... now!")
                        9 -> {
                            unlock(player)
                            kolodion.isCommenced = true
                            return true
                        }
                    }
                    return false
                }
            },
        )
    }

    fun close() {
        kolodion.clear()
        getSession(player).close()
    }

    companion object {
        fun create(player: Player): KolodionSession = KolodionSession(player)
        fun getSession(player: Player): KolodionSession = getSession(player)
    }
}
