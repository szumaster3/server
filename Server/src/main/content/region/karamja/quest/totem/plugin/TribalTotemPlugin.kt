package content.region.karamja.quest.totem.plugin

import content.data.GameAttributes
import core.api.*
import core.game.global.action.ClimbActionHandler
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import shared.consts.*

class TribalTotemPlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles front door of Lord Handelmort mansion.
         */

        on(Scenery.DOOR_2706, IntType.SCENERY, "Open") { player, door ->
            val questStage = getQuestStage(player, Quests.TRIBAL_TOTEM)
            if (questStage < 35) {
                sendMessage(player, "The door is locked shut.")
            } else {
                DoorActionHandler.handleAutowalkDoor(player, door.asScenery())
            }
            return@on true
        }

        /*
         * Handles investigate the quest crate.
         */

        on(Scenery.CRATE_2708, IntType.SCENERY, "Investigate") { player, _ ->
            val questStage = getQuestStage(player, Quests.TRIBAL_TOTEM)
            val hasItem = hasAnItem(player, Items.ADDRESS_LABEL_1858).container != null
            when(questStage) {
                in 1..20 -> {
                    sendDialogueLines(player, "There is a label on this crate. It says; To Lord Handelmort,","Handelmort Mansion, Ardougne.You carefully peel it off and take it.")
                    addDialogueAction(player) { _, _ ->
                        closeDialogue(player)
                        if(!hasItem) {
                            addItem(player, Items.ADDRESS_LABEL_1858, 1)
                        } else {
                            sendDialogue(player, "There was a label on this crate, but it's gone now since you took it!")
                        }
                    }
                }
                else -> sendMessage(player, "You search the crate but find nothing.")
            }
            return@on true
        }

        /*
         * Handles investigation of other crates.
         */

        on(Scenery.CRATE_2707, IntType.SCENERY, "Investigate") { player, _ ->
            sendDialogue(player, "There is a label on this crate. It says; Senior Patents Clerk, Chamber of Invention, The Wizards' Tower, Misthalin. The crate is securely fastened shut and ready for delivery.")
            return@on true
        }

        /*
         * Handles use the adress label on crate.
         */

        onUseWith(IntType.SCENERY, Items.ADDRESS_LABEL_1858, Scenery.CRATE_2707) { player, _, _ ->
            if (getQuestStage(player, Quests.TRIBAL_TOTEM) >= 20) {
                sendDialogue(player, "You have already replaced the delivery address label.")
                return@onUseWith true
            }
            if(removeItem(player, Items.ADDRESS_LABEL_1858)) {
                sendDialogueLines(player, "You carefully place the delivery address label over the existing label,", "covering it completely.")
                setQuestStage(player, Quests.TRIBAL_TOTEM, 20)
                addDialogueAction(player) { _, _ ->
                    sendPlayerDialogue(player, "Now I just need someone to deliver it for me.")
                    return@addDialogueAction closeDialogue(player)
                }
            }
            return@onUseWith true
        }

        /*
         * Handles opening the trap doors.
         */

        on(Scenery.DOOR_2705, IntType.SCENERY, "Open") { player, node ->
            if (getAttribute(player, GameAttributes.QUEST_TRIBAL_TOTEM_DOORS, false)) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                openInterface(player, Components.COMBI_LOCK_369)
            }
            return@on true
        }

        /*
         * Handles climb up the trap stairs.
         */

        on(Scenery.STAIRS_2711, IntType.SCENERY, "Climb-up") { player, _ ->
            if (getAttribute(player, GameAttributes.QUEST_TRIBAL_TOTEM_STAIRS, false)) {
                sendMessage(player, "You climb up the stairs.")
                ClimbActionHandler.climb(player, null, Location.create(2629, 3324, 1))
            } else {
                sendMessage(player, "As you climb the stairs you hear a click...")
                teleport(player, Location.create(2641, 9721, 0))
                sendMessage(player, "You have fallen through a trap!")
            }
            return@on true
        }

        /*
         * Handles disarm the stairs trap.
         */

        on(Scenery.STAIRS_2711, IntType.SCENERY, "Investigate") { player, _ ->
            val requiredLevel = getStatLevel(player, Skills.THIEVING) >= 21
            if (requiredLevel) {
                sendDialogueLines(player, "You trained senses as a thief enable you to see that there is a trap", "in these stairs. You make a note of its location for future reference", "when using these stairs.")
                setAttribute(player, GameAttributes.QUEST_TRIBAL_TOTEM_STAIRS, true)
            } else {
                sendDialogue(player, "You don't see anything out of place on these stairs.")
            }
            return@on true
        }

        /*
         * Handles opening the chest.
         */

        on(Scenery.CHEST_2709, IntType.SCENERY, "Open") { player, node ->
            animate(player, Animations.HUMAN_OPEN_CHEST_536)
            sendMessage(player, "You open the chest.")
            replaceScenery(node.asScenery(), node.id + 1, -1)
            return@on true
        }

        /*
         * Handles close the chest.
         */

        on(Scenery.CHEST_2710, IntType.SCENERY, "Close") { player, node ->
            animate(player, Animations.CLOSE_CHEST_539)
            replaceScenery(node.asScenery(), node.id - 1, -1)
            return@on true
        }

        /*
         * Handles search the quest chest.
         */

        on(Scenery.CHEST_2710, IntType.SCENERY, "Search") { player, _ ->
            val hasTotem = hasAnItem(player, Items.TOTEM_1857).container != null
            if (hasTotem) {
                sendMessage(player, "The chest is empty.")
            } else {
                setQuestStage(player, Quests.TRIBAL_TOTEM, 35)
                sendDialogue(player, "Inside the chest you find the tribal totem.")
                addItem(player, Items.TOTEM_1857)
            }
            return@on true
        }
    }

}
