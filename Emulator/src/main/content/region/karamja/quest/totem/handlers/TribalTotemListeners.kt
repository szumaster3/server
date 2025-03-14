package content.region.karamja.quest.totem.handlers

import content.data.QuestItem
import core.api.*
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.game.global.action.ClimbActionHandler
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.node.scenery.SceneryBuilder
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.tools.BLUE
import org.rs.consts.*

class TribalTotemListeners : InteractionListener {
    override fun defineListeners() {
        on(Scenery.DOOR_2706, IntType.SCENERY, "Open") { player, door ->
            if (player.questRepository.getStage(Quests.TRIBAL_TOTEM) >= 35) {
                DoorActionHandler.handleAutowalkDoor(player, door.asScenery())
            }
            sendMessage(player, "The door is locked shut.")
            return@on true
        }

        on(Scenery.CRATE_2708, IntType.SCENERY, "Investigate") { player, _ ->
            if (getQuestStage(player, Quests.TRIBAL_TOTEM) in 1..19 &&
                !player.inventory.containsAtLeastOneItem(Items.ADDRESS_LABEL_1858)
            ) {
                sendDialogue(
                    player,
                    "There is a label on this crate. It says;$BLUE Lord Handelmort, Handelmort Mansion Ardogune.</col> You carefully peel it off and take it.",
                )
                addItem(player, Items.ADDRESS_LABEL_1858, 1)
            } else if (getQuestStage(player, Quests.TRIBAL_TOTEM) in 1..19 &&
                player.inventory.containsAtLeastOneItem(Items.ADDRESS_LABEL_1858)
            ) {
                sendDialogue(player, "There was a label on this crate, but it's gone now since you took it!")
            } else {
                openDialogue(player, QuestItem(Items.ADDRESS_LABEL_1858))
            }
            return@on true
        }

        on(Scenery.CRATE_2707, IntType.SCENERY, "Investigate") { player, _ ->
            sendDialogue(
                player,
                "There is a label on this crate. It says; Senior Patents Clerk, Chamber of Invention, The Wizards' Tower, Misthalin. The crate is securely fastened shut and ready for delivery.",
            )
            return@on true
        }

        onUseWith(IntType.SCENERY, Items.ADDRESS_LABEL_1858, Scenery.CRATE_2707) { player, _, _ ->
            if (getQuestStage(player, Quests.TRIBAL_TOTEM) < 20) {
                sendDialogue(player, "You have already replaced the delivery address label.")
            } else {
                sendDialogue(
                    player,
                    "You carefully place the delivery address label over the existing label, covering it completely.",
                )
                removeItem(player, Items.ADDRESS_LABEL_1858)
                setQuestStage(player, Quests.TRIBAL_TOTEM, 20)
            }
            return@onUseWith true
        }

        on(Scenery.DOOR_2705, IntType.SCENERY, "Open") { player, node ->
            if (player.getAttribute("TT:DoorUnlocked", false) == true) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                openInterface(player, Components.COMBI_LOCK_369)
            }
            return@on true
        }

        on(Scenery.STAIRS_2711, IntType.SCENERY, "Climb-up") { player, _ ->
            if (player.getAttribute("TT:StairsChecked", false)) {
                ClimbActionHandler.climb(player, Animation(Animations.USE_LADDER_828), Location.create(2629, 3324, 1))
            } else {
                sendMessage(
                    player,
                    "You set off a trap and the stairs give way under you, dropping you into the sewers.",
                )
                player.teleport(Location.create(2641, 9721, 0))
            }
            return@on true
        }

        on(Scenery.STAIRS_2711, IntType.SCENERY, "Investigate") { player, _ ->
            if (player.getSkills().getStaticLevel(Skills.THIEVING) >= 21) {
                sendDialogue(
                    player,
                    "Your trained senses as a thief enable you to see that there is a trap in these stairs. You make a note of its location for future reference when using these stairs",
                )
                setAttribute(player, "/save:TT:StairsChecked", true)
            } else {
                sendDialogue(player, "You don't see anything out of place on these stairs.")
            }
            return@on true
        }

        on(Scenery.CHEST_2709, IntType.SCENERY, "Open") { _, node ->
            SceneryBuilder.replace(node.asScenery(), node.asScenery().transform(Scenery.CHEST_2710))
            return@on true
        }

        on(Scenery.CHEST_2710, IntType.SCENERY, "Close") { _, node ->
            SceneryBuilder.replace(node.asScenery(), node.asScenery().transform(Scenery.CHEST_2709))
            return@on true
        }

        on(Scenery.CHEST_2710, IntType.SCENERY, "Search") { player, _ ->
            if (!player.inventory.containsAtLeastOneItem(Items.TOTEM_1857)) {
                sendDialogue(player, "Inside the chest you find the tribal totem.")
                addItem(player, Items.TOTEM_1857)
            } else {
                sendDialogue(player, "Inside the chest you don't find anything because you already took the totem!")
            }
            return@on true
        }
    }
}
