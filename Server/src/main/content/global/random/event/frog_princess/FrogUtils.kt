package content.global.random.event.frog_princess

import content.data.GameAttributes
import content.data.RandomEvent
import core.api.*
import core.game.dialogue.FaceAnim
import core.game.node.Node
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.update.flag.context.Animation
import shared.consts.*

/**
 * Utils for Frog random event.
 */
object FrogUtils {
    const val FROG_NPC = NPCs.FROG_2469
    const val FROG_APPEARANCE_NPC = NPCs.FROG_2473
    private const val FROG_PRINCE_NPC = NPCs.FROG_PRINCE_2474
    private const val FROG_PRINCESS_NPC = NPCs.FROG_PRINCESS_2475
    const val TRANSFORM_INTO_FROG = Animations.MORPH_TO_FROG_2377
    const val TRANSFORM_INTO_HUMAN = Animations.MORPH_FROM_FROG_2375
    const val FROG_KISS_ANIM = Animations.FROG_KISS_2374
    private const val HUMAN_KISS_ANIM = Animations.HUMAN_BLOW_KISS_1374
    private const val HUMAN_KISS_GFX = Graphics.KISS_EMOTE_ORIGINAL_1702

    fun cleanup(player: Player) {
        player.properties.teleportLocation = getAttribute(player, RandomEvent.save(), null)
        restoreTabs(player)
        resetAnimator(player)
        clearLogoutListener(player, RandomEvent.logout())
        removeAttributes(player, GameAttributes.KTF_KISS_FAIL)
    }

    fun kissTheFrog(player: Player, node: Node) {
        val npc = node as NPC
        val transformation = if (player.isMale) FROG_PRINCESS_NPC else FROG_PRINCE_NPC
        fun visualize() = visualize(npc, HUMAN_KISS_ANIM, HUMAN_KISS_GFX)

        player.lock(16)
        submitIndividualPulse(player, object : Pulse(1, player) {
            var counter = 0
            override fun pulse(): Boolean {
                when (counter++) {
                    1 -> {
                        face(player, npc)
                        face(npc, player)
                        npc.animate(Animation(FROG_KISS_ANIM))
                    }

                    2 -> {
                        npc.animate(Animation(TRANSFORM_INTO_HUMAN))
                        sendGraphics(Graphics.SPELL_SPLASH_85, npc.location)
                    }

                    3 -> findLocalNPC(player, FROG_NPC)!!.transform(transformation)
                    5 -> player.dialogueInterpreter.sendDialogues(
                        transformation,
                        FaceAnim.HAPPY,
                        "Thank you so much, ${player.username}.",
                        "I must return to my fairy tale kingdom now, but I will",
                        "leave you a reward for your kindness."
                    )

                    6, 8, 9 -> {
                        visualize()
                        if (counter == 10) openInterface(player, Components.FADE_TO_BLACK_120)
                    }

                    14 -> {
                        npc.reTransform()
                        cleanup(player)
                        openInterface(player, Components.FADE_FROM_BLACK_170)
                        addItemOrDrop(player, Items.FROG_TOKEN_6183)
                        sendMessage(player, "You've received a frog token!")
                        return true
                    }
                }
                return false
            }
        })
    }
}