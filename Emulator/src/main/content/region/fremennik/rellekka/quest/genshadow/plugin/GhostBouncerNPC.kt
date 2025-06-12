package content.region.fremennik.rellekka.quest.genshadow.plugin

import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.impl.Projectile
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class GhostBouncerNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC = GhostBouncerNPC(id, location)

    override fun getIds(): IntArray = intArrayOf(NPCs.BOUNCER_5564)

    companion object {
        fun spawnGhostBouncer(player: Player) {
            val ghostBouncer = GhostBouncerNPC(NPCs.BOUNCER_5564)
            ghostBouncer.location = Location(1761, 4705, 0)
            ghostBouncer.isWalks = true
            ghostBouncer.isAggressive = true
            ghostBouncer.isActive = false

            if (ghostBouncer.asNpc() != null && ghostBouncer.isActive) {
                ghostBouncer.properties.teleportLocation = ghostBouncer.properties.spawnLocation
            }
            ghostBouncer.isActive = true
            GameWorld.Pulser.submit(
                object : Pulse(14, ghostBouncer) {
                    override fun pulse(): Boolean {
                        ghostBouncer.init()
                        ghostBouncer.attack(player)
                        return true
                    }
                },
            )
        }
    }

    override fun finalizeDeath(killer: Entity?) {
        if (killer is Player) {
            val player = killer.asPlayer()
            if (!removeItem(player, Items.SEVERED_LEG_10857)) {
                return
            }
            lock(player, 10)
            submitIndividualPulse(
                player,
                object : Pulse(1) {
                    var counter = 0

                    override fun pulse(): Boolean {
                        when (counter++) {
                            0 -> {
                                sendMessage(player, "You grab the severed leg and throw it to distract the hound.")
                                playAudio(player, 3415)
                                spawnProjectile(Projectile.getLocation(killer), Location.getRandomLocation(Projectile.getLocation(player), 5, true), 1024, 40, 30, 1, 250, 0)
                            }
                            1 -> sendChat(player, "Away, darn spot!")
                            2 -> animate(player, Animation(Animations.SEVERED_LEG_ATTACK_5812))
                            8 -> {
                                rewardXP(player, Skills.SLAYER, 2000.0)
                                sendItemDialogue(
                                    player,
                                    Items.SHADOW_SWORD_10858,
                                    "You receive a shadow sword and 2000 Slayer xp."
                                )
                                addItemOrDrop(player, Items.SHADOW_SWORD_10858)
                                GeneralShadow.setShadowProgress(player, 5)
                                return true
                            }
                        }
                        return false
                    }
                })
        }
        clear()
        super.finalizeDeath(killer)
    }
}
