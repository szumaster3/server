package content.global.ame.frogs

import content.data.RandomEvent
import core.api.*
import core.api.ui.restoreTabs
import core.game.dialogue.FaceAnim
import core.game.node.Node
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.update.flag.context.Animation
import org.rs.consts.*

object FrogUtils {
    const val ATTRIBUTE_FROG_TASK_FAIL = "frog:fail"

    const val FROG_NPC = NPCs.FROG_2469
    const val FROG_APPEARANCE_NPC = NPCs.FROG_2473
    const val FROG_PRINCE_NPC = NPCs.FROG_PRINCE_2474
    const val FROG_PRINCESS_NPC = NPCs.FROG_PRINCESS_2475

    const val TRANSFORM_INTO_FROG = Animations.GET_INTO_FROG_POSITION_2377
    const val TRANSFORM_INTO_HUMAN = 2375

    const val FROG_KISS_ANIM = 2374
    const val HUMAN_KISS_ANIM = Animations.HUMAN_BLOW_KISS_1374
    const val HUMAN_KISS_GFX = Graphics.KISS_EMOTE_ORIGINAL_1702

    fun cleanup(player: Player) {
        player.properties.teleportLocation = getAttribute(player, RandomEvent.save(), null)
        restoreTabs(player)
        player.appearance.transformNPC(-1)
        clearLogoutListener(player, RandomEvent.logout())
        removeAttributes(
            player,
            ATTRIBUTE_FROG_TASK_FAIL,
            RandomEvent.save(),
            RandomEvent.logout(),
        )
    }

    fun kissTheFrog(
        player: Player,
        node: Node,
    ) {
        val npc = node as NPC
        lock(player, 12)
        submitIndividualPulse(
            player,
            object : Pulse() {
                var counter = 0

                override fun pulse(): Boolean {
                    when (counter++) {
                        1 -> {
                            face(player, npc)
                            face(npc, player)
                            animate(npc, Animation(FROG_KISS_ANIM))
                        }

                        2 -> {
                            animate(npc, Animation(TRANSFORM_INTO_HUMAN))
                            sendGraphics(Graphics.SPELL_SPLASH_85, npc.location)
                        }

                        3 -> {
                            findLocalNPC(player, FROG_NPC)!!.transform(if (player.isMale) FROG_PRINCESS_NPC else FROG_PRINCE_NPC)
                        }

                        5 -> {
                            player.dialogueInterpreter.sendDialogues(
                                if (player.isMale) FROG_PRINCESS_NPC else FROG_PRINCE_NPC,
                                FaceAnim.HAPPY,
                                "Thank you so much, ${player.username}.",
                                "I must return to my fairy tale kingdom now, but I will",
                                "leave you a reward for your kindness.",
                            )
                        }

                        6 -> visualize(npc, HUMAN_KISS_ANIM, HUMAN_KISS_GFX)
                        8 -> visualize(npc, HUMAN_KISS_ANIM, HUMAN_KISS_GFX)
                        9 -> visualize(npc, HUMAN_KISS_ANIM, HUMAN_KISS_GFX).also {
                            openInterface(player, Components.FADE_TO_BLACK_120)
                        }
                        12 -> {
                            npc.reTransform()
                            cleanup(player)
                            reward(player)
                            sendMessage(player, "You've received a frog token!")
                        }
                    }
                    return false
                }
            },
        )
        return
    }

    fun reward(player: Player) {
        addItemOrDrop(player, Items.FROG_TOKEN_6183)
    }
}
