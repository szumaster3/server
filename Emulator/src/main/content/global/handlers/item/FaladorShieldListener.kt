package content.global.handlers.item

import content.data.consumables.effects.PrayerEffect
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.Entity
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.update.flag.context.Graphics
import org.rs.consts.Animations
import org.rs.consts.Items
import java.util.concurrent.TimeUnit

class FaladorShieldListener : InteractionListener {
    override fun defineListeners() {
        on(FALADOR_SHIELD, IntType.ITEM, "prayer-restore", "operate") { player, node ->
            val item = node as Item
            val level = getLevel(item.id)
            when (getUsedOption(player)) {
                "prayer-restore" -> {
                    val attrTime = player.getAttribute<Long>("diary:falador:shield-restore-time")
                    setTitle(player, 2)
                    sendDialogueOptions(
                        player,
                        "Are you sure you wish to recharge?",
                        "Yes, recharge my Prayer points.",
                        "No, I've changed my mind.",
                    )
                    addDialogueAction(player) { _, button ->
                        if (button == 2) {
                            if (attrTime != null && attrTime > System.currentTimeMillis()) {
                                sendMessage(player, "You have no charges left today.")
                            } else {
                                val effect =
                                    PrayerEffect(
                                        0.0,
                                        when (level) {
                                            0 -> 0.25
                                            1 -> 0.5
                                            2 -> 1.0
                                            else -> 0.0
                                        },
                                    )
                                visualize(
                                    player,
                                    -1,
                                    Graphics(GFX_PRAYER_RESTORE[level]),
                                )
                                setAttribute(
                                    player,
                                    "/save:diary:falador:shield-restore-time",
                                    System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1),
                                )
                                sendMessage(player, "You restore ${if (level < 2) "some" else "your"} prayer points.")
                                effect.activate(player)
                            }
                        }
                    }
                }

                "operate" -> Pulser.submit(getPulse(player, level))
            }
            return@on true
        }
    }

    private fun getLevel(itemId: Int): Int {
        return when (itemId) {
            Items.FALADOR_SHIELD_1_14577 -> 0
            Items.FALADOR_SHIELD_2_14578 -> 1
            Items.FALADOR_SHIELD_3_14579 -> 2
            else -> -1
        }
    }

    private fun getPulse(
        entity: Entity,
        level: Int,
    ): Pulse {
        return object : Pulse(1) {
            override fun pulse(): Boolean {
                return when (delay++) {
                    0 -> {
                        lock(entity, 3)
                        visualize(
                            entity,
                            ANIM_EMOTE,
                            Graphics(GFX_EMOTE[level]),
                        )
                        false
                    }

                    3 -> {
                        resetAnimator(entity.asPlayer())
                        true
                    }

                    else -> false
                }
            }
        }
    }

    companion object {
        const val ANIM_EMOTE: Int = Animations.HUMAN_FALADOR_SHIELD_RESTORE_11012
        val GFX_EMOTE: IntArray =
            intArrayOf(
                org.rs.consts.Graphics.FALADOR_SHIELD_1_EMOTE_1966,
                org.rs.consts.Graphics.FALADOR_SHIELD_3_EMOTE_1965,
                org.rs.consts.Graphics.FALADOR_SHIELD_3_EMOTE_1965,
            )
        val GFX_PRAYER_RESTORE: IntArray =
            intArrayOf(1962, 1963, org.rs.consts.Graphics.FALADOR_SHIELD_PRAY_RESTORE_1964)
        val FALADOR_SHIELD: IntArray =
            intArrayOf(Items.FALADOR_SHIELD_1_14577, Items.FALADOR_SHIELD_2_14578, Items.FALADOR_SHIELD_3_14579)
    }
}
