package content.global.skill.agility.shortcuts

import core.api.*
import core.api.quest.getQuestStage
import core.game.dialogue.FaceAnim
import core.game.global.action.ClimbActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs.consts.Animations
import org.rs.consts.NPCs
import org.rs.consts.Quests
import org.rs.consts.Scenery

class WatchtowerShortcut : InteractionListener {
    private val END = Location.create(2548, 3117, 1)
    private val ANIMATION = Animation.create(Animations.HUMAN_CLIMB_STAIRS_828)

    override fun defineListeners() {

        /*
         * Handles try to climb the ladder.
         */

        on(Scenery.LADDER_2833, IntType.SCENERY, "climb-up") { player, _ ->
            if (getQuestStage(player, Quests.WATCHTOWER) >= 1) {
                sendNPCDialogue(player, NPCs.TOWER_GUARD_877, "It is the wizards' helping hand - let 'em up.", FaceAnim.FRIENDLY)
                addDialogueAction(player) { _, _ ->
                    ClimbActionHandler.climb(player, Animation(Animations.USE_LADDER_828), Location.create(2544, 3112, 1))
                }
            } else {
                sendNPCDialogue(player, NPCs.TOWER_GUARD_877, "You can't go up there. That's private, that is.", FaceAnim.ANNOYED)
            }
            return@on true
        }

        /*
         * Handles talking to Tower guards.
         */

        on(NPCs.TOWER_GUARD_877, IntType.NPC, "talk-to") { player, _ ->
            sendPlayerDialogue(player, "Hello. What are you doing here?", FaceAnim.HALF_ASKING)
            addDialogueAction(player) { _, _ ->
                sendNPCDialogue(player, NPCs.TOWER_GUARD_877, "We are the tower guards - our business is our own!", FaceAnim.ANNOYED)
            }
            return@on true
        }

        /*
         * Handles trellis shortcut.
         */

        on(Scenery.TRELLIS_20056, IntType.SCENERY, "climb-up") { player, _ ->
            if (!hasLevelDyn(player, Skills.AGILITY, 18)) {
                sendDialogue(player, "You need an agility level of at least 18 to negotiate this obstacle.")
                return@on true
            }
            lock(player, 2)
            sendMessage(player, "You climb up the wall...")
            sendMessageWithDelay(player, "...and squeeze in through the window.", 1)
            animate(player, ANIMATION)
            queueScript(player, 2, QueueStrength.SOFT) {
                teleport(player, END)
                rewardXP(player, Skills.AGILITY, 31.0)
                return@queueScript stopExecuting(player)
            }
            return@on true
        }
    }

}
