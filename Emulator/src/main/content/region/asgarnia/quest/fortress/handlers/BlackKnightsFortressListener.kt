package content.region.asgarnia.quest.fortress.handlers

import content.data.GameAttributes
import core.api.*
import core.api.movement.finishedMoving
import core.api.quest.getQuestStage
import core.game.dialogue.FaceAnim
import core.game.global.action.ClimbActionHandler.climb
import core.game.global.action.ClimbActionHandler.climbLadder
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.scenery.Scenery
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Quests

class BlackKnightsFortressListener : InteractionListener {
    companion object {
        private fun determineDestination(
            scenery: Scenery?,
            id: Int,
        ): Location? {
            return when (id) {
                17160 ->
                    when (scenery?.location) {
                        Location(3022, 3518, 1) -> Location.create(3022, 3517, 0)
                        else -> null
                    }

                17149 ->
                    when (scenery?.location) {
                        Location(3023, 3513, 2) -> Location.create(3023, 3514, 1)
                        Location(3025, 3513, 2) -> Location.create(3025, 3514, 1)
                        Location(3016, 3519, 2) -> Location.create(3016, 3518, 1)
                        Location(3015, 3519, 1) -> Location.create(3015, 3518, 0)
                        Location(3017, 3516, 2) -> Location.create(3017, 3515, 1)
                        else -> null
                    }

                17148 ->
                    when (scenery?.location) {
                        Location(3021, 3510, 0) -> Location.create(3022, 3510, 1)
                        Location(3015, 3519, 0) -> Location.create(3015, 3518, 1)
                        Location(3016, 3519, 0) -> Location.create(3016, 3518, 2)
                        else -> null
                    }

                else -> null
            }
        }
    }

    override fun defineListeners() {
        onUseWith(IntType.SCENERY, intArrayOf(Items.CABBAGE_1965, Items.CABBAGE_1967), 2336) { player, used, _ ->
            if (getQuestStage(player, Quests.BLACK_KNIGHTS_FORTRESS) >= 20) {
                if (used.id == Items.CABBAGE_1967) {
                    sendDialogue(player, "This is the wrong sort of cabbage!")
                    return@onUseWith false
                }
                openDialogue(player, 992752973, true, true)
                return@onUseWith true
            } else {
                sendPlayerDialogue(player, "Why exactly would I want to do that?", FaceAnim.THINKING)
            }
            return@onUseWith true
        }

        on(2342, IntType.SCENERY, "listen-at") { player, _ ->
            if (!finishedMoving(player)) return@on true

            lock(player, 2)
            animate(player, Animations.ON_GROUND_LISTENING_TO_DOOR_4195)
            queueScript(player, 2, QueueStrength.SOFT) {
                animate(player, Animations.CROUCH_HIDE_4552)
                openDialogue(player, 992752973)
                return@queueScript stopExecuting(player)
            }
            return@on true
        }

        on(Items.DOSSIER_9589, IntType.ITEM, "read") { player, node ->
            if (removeItem(player, node.asItem())) {
                lock(player, 1)
                queueScript(player, 0, QueueStrength.NORMAL) { stage: Int ->
                    when (stage) {
                        0 -> {
                            visualize(player, Animations.BURN_NOTE_4551, 770)
                            sendDialogueLines(
                                player,
                                "Infiltrate fortress... sabotage secret weapon... self",
                                "destruct in 3...2...ARG!",
                            )
                            setAttribute(player, GameAttributes.QUEST_BKF_DOSSIER_INTER, true)
                            return@queueScript delayScript(player, animationCycles(Animations.BURN_NOTE_4551))
                        }

                        1 -> {
                            closeChatBox(player)
                            resetAnimator(player)
                            return@queueScript stopExecuting(player)
                        }

                        else -> return@queueScript stopExecuting(player)
                    }
                }
            }
            return@on true
        }

        on(
            intArrayOf(
                org.rs.consts.Scenery.LADDER_17160,
                org.rs.consts.Scenery.LADDER_17149,
                org.rs.consts.Scenery.LADDER_17148,
            ),
            IntType.SCENERY,
            "climb-up",
            "climb-down",
        ) { player, node ->
            val dest: Location? = determineDestination(node.asScenery(), node.id)
            val option = getUsedOption(player)
            if (dest != null) {
                climb(player, Animation(Animations.HUMAN_CLIMB_STAIRS_828), dest)
            } else {
                climbLadder(player, node as Scenery, option)
            }
            return@on true
        }
    }
}
