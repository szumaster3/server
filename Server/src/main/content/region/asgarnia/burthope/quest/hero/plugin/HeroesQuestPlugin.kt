package content.region.asgarnia.burthope.quest.hero.plugin

import content.region.asgarnia.burthope.quest.hero.HeroesQuest
import content.region.karamja.brimhaven.dialogue.CharlieTheCookDialogueFile
import content.region.karamja.brimhaven.dialogue.GarvDialogueFile
import content.region.karamja.brimhaven.dialogue.GruborDialogueFile
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.npc.NPC
import core.game.node.item.GroundItem
import core.game.world.map.Location
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests
import shared.consts.Scenery

class HeroesQuestPlugin : InteractionListener {

    override fun defineListeners() {
        on(Scenery.DOOR_2626, IntType.SCENERY, "open") { player, node ->
            if (getQuestStage(player, Quests.HEROES_QUEST) >= 2 &&
                getAttribute(player, HeroesQuest.attributeGruborLetsYouIn, false) &&
                HeroesQuest.isBlackArm(player)
            ) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                openDialogue(player, GruborDialogueFile(), NPC(NPCs.GRUBOR_789))
            }
            return@on true
        }

        on(Scenery.DOOR_2628, IntType.SCENERY, "open") { player, node ->
            if (getQuestStage(player, Quests.HEROES_QUEST) >= 3 && HeroesQuest.isPhoenix(player)) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                sendDialogue(player, "This door is locked.")
            }
            return@on true
        }

        on(Scenery.WALL_2629, IntType.SCENERY, "push") { player, node ->
            if (getQuestStage(player, Quests.HEROES_QUEST) >= 4 && HeroesQuest.isPhoenix(player)) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                openDialogue(player, CharlieTheCookDialogueFile(), NPC(NPCs.CHARLIE_THE_COOK_794))
            }
            return@on true
        }

        on(Scenery.DOOR_2627, IntType.SCENERY, "open") { player, node ->
            if (getQuestStage(player, Quests.HEROES_QUEST) >= 4 && HeroesQuest.isBlackArm(player)) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                openDialogue(player, GarvDialogueFile(), NPC(NPCs.GARV_788))
            }
            return@on true
        }

        on(Scenery.CUPBOARD_2636, IntType.SCENERY, "search") { player, _ ->
            openDialogue(
                player,
                object : DialogueFile() {
                    override fun handle(
                        componentID: Int,
                        buttonID: Int,
                    ) {
                        when (stage) {
                            START_DIALOGUE -> sendNPCDialogue(player, NPCs.PIRATE_GUARD_799, "I don't think Mr Grip will like you opening that. That's his private drinks cabinet.").also { stage++ }
                            1 -> showTopics(
                                Topic(FaceAnim.NEUTRAL, "He won't notice me having a quick look.", 2),
                                Topic(FaceAnim.NEUTRAL, "Ok, I'll leave it.", END_DIALOGUE)
                            )
                            2 ->
                                end().also {
                                    val gripNpc = findNPC(NPCs.GRIP_792)
                                    sendChat(gripNpc!!, "Stay out of my drinks cabinet!")
                                    forceWalk(gripNpc, Location(2777, 3198, 0), "smart")
                                }
                        }
                    }
                },
            )
            return@on true
        }

        on(Scenery.DOOR_2622, IntType.SCENERY, "open") { player, node ->
            if (getAttribute(player, HeroesQuest.attributeHasOpenedBackdoor, false)) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                sendDialogue(player, "This door is locked.")
            }
            return@on true
        }

        onUseWith(IntType.SCENERY, Items.MISCELLANEOUS_KEY_1586, Scenery.DOOR_2622) { player, _, with ->
            setAttribute(player, HeroesQuest.attributeHasOpenedBackdoor, true)
            DoorActionHandler.handleAutowalkDoor(player, with.asScenery())
            return@onUseWith true
        }

        on(Scenery.DOOR_2621, IntType.SCENERY, "open") { player, node ->
            if (getAttribute(player, HeroesQuest.attributeHasOpenedChestDoor, false)) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                sendDialogue(player, "This door is locked.")
            }
            return@on true
        }

        onUseWith(IntType.SCENERY, Items.GRIPS_KEY_RING_1588, Scenery.DOOR_2621) { player, _, with ->
            setAttribute(player, HeroesQuest.attributeHasOpenedChestDoor, true)
            DoorActionHandler.handleAutowalkDoor(player, with.asScenery())
            return@onUseWith true
        }

        on(Scenery.CHEST_2633, IntType.SCENERY, "search") { player, _ ->
            if (inInventory(player, Items.PETES_CANDLESTICK_1577)) {
                sendMessage(player, "You search the chest but find nothing.")
            } else {
                if (getQuestStage(player, Quests.HEROES_QUEST) == 4) {
                    setQuestStage(player, Quests.HEROES_QUEST, 5)
                }
                sendDialogue(
                    player,
                    "You find two candlesticks in the chest. So that will be one for you, and one for the person who killed Grip for you.",
                )
                addItemOrDrop(player, Items.PETES_CANDLESTICK_1577, 2)
            }
            return@on true
        }

        on(Items.FIRE_FEATHER_1583, IntType.GROUND_ITEM, "take") { player, groundItem ->
            if (inEquipment(player, Items.ICE_GLOVES_1580)) {
                addItem(player, Items.FIRE_FEATHER_1583)
                removeGroundItem(groundItem as GroundItem)
            } else {
                sendChat(player, "Ouch!")
                impact(player, 9, ImpactHandler.HitsplatType.NORMAL)
                sendMessage(player, "It is too hot to take. You need something cold to pick it up with.")
            }
            return@on true
        }
    }
}
