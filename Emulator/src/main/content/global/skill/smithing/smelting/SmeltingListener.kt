package content.global.skill.smithing.smelting

import content.global.skill.smithing.Bar
import content.region.islands.tutorial_island.plugin.TutorialStage
import core.api.*
import core.game.dialogue.FaceAnim
import core.game.dialogue.SkillDialogueHandler
import core.game.event.ResourceProducedEvent
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.GameWorld
import core.game.world.update.flag.context.Animation
import org.rs.consts.*

class SmeltingListener : InteractionListener {
    override fun defineListeners() {

        /*
         * Handles perfect gold smelting (Family crest quest special interaction).
         */

        onUseWith(IntType.SCENERY, Items.PERFECT_GOLD_ORE_446, *FURNACE_IDS) { player, used, _ ->
            queueScript(player, 1, QueueStrength.SOFT) { stage: Int ->
                when (stage) {
                    0 -> {
                        if (removeItem(player, used.asItem())) {
                            sendMessage(player, "You place a lump of gold in the furnace.")
                            lock(player, 4)
                            lockInteractions(player, 4)
                            animate(player, Animations.HUMAN_FURNACE_SMELT_3243)
                        }
                        return@queueScript delayScript(player, 2)
                    }

                    1 -> {
                        sendMessage(player, "You retrieve a bar of gold from the furnace.")
                        addItem(player, Items.PERFECT_GOLD_BAR_2365)
                        rewardXP(player, Skills.SMITHING, 22.5)
                        return@queueScript stopExecuting(player)
                    }

                    else -> return@queueScript stopExecuting(player)
                }
            }
            return@onUseWith true
        }

        /*
         * Handles creating cannonballs.
         */

        onUseWith(IntType.SCENERY, Items.STEEL_BAR_2353, *FURNACE_IDS) { player, used, _ ->
            val handler: SkillDialogueHandler =
                object : SkillDialogueHandler(player, SkillDialogue.ONE_OPTION, Item(Items.CANNONBALL_2)) {
                    override fun create(
                        amount: Int,
                        index: Int,
                    ) {
                        submitIndividualPulse(player, CannonballPulse(player, used.asItem(), amount))
                    }

                    override fun getAll(index: Int): Int = amountInInventory(player, used.id)
                }

            handler.open()
            return@onUseWith true
        }

        /*
         * Handles furnace options interaction.
         */

        on(IntType.SCENERY, "smelt", "smelt-ore") { player, node ->
            if (node.id == Scenery.FURNACE_26814 && !isDiaryComplete(player, DiaryType.VARROCK, 0)) {
                if (!GameWorld.settings!!.isMembers) {
                    sendNPCDialogue(player, NPCs.JEFFERY_6298, "Keep away from that! It's dangerous!")
                } else {
                    sendNPCDialogue(player, NPCs.JEFFERY_6298, "You want to use my furnace?", FaceAnim.HALF_ASKING)
                    addDialogueAction(player) { _, _ ->
                        sendNPCDialogue(
                            player,
                            NPCs.JEFFERY_6298,
                            "No one can use my furnace! Only I can use my furnace!",
                            FaceAnim.ANNOYED
                        )
                    }
                    sendMessage(
                        player,
                        "You need to have completed the easy tasks in the Varrock Diary in order to use this."
                    )
                }
                return@on true
            }
            openChatbox(player, Components.SMELTING_311)
            return@on true
        }

        /*
         * Handles use option for tutorial furnace.
         */

        on(specialFurnace, IntType.SCENERY, "use") { player, _ ->
            if (inBorders(player, getRegionBorders(TUTORIAL_REGION))) {
                if (!anyInInventory(player, *tutorialOres)) {
                    sendPlainDialogue(player,
                        false,
                        "This is a furnace for smelting metal. To use it simply click on the",
                        "ore you wish to smelt then click on the furnace you would like to",
                        "use.",
                    )
                    TutorialStage.rollback(player)
                    return@on true
                }
                if (!inInventory(player, Items.TIN_ORE_438) || !inInventory(player, Items.COPPER_ORE_436)) {
                    sendPlainDialogue(player,
                        false,
                        "",
                        "You do not have the required ores to make this bar.",
                        "",
                    )
                    TutorialStage.rollback(player)
                } else {
                    sendPlainDialogue(player,
                        false,
                        "This is a furnace for smelting metal. To use it simply click on the",
                        "ore you wish to smelt then click on the furnace you would like to",
                        "use.",
                    )
                    TutorialStage.rollback(player)
                }
            } else if (!isDiaryComplete(player, DiaryType.VARROCK, 0)) {
                sendMessage(
                    player,
                    "You need to have completed the easy tasks in the Varrock Diary in order to use this."
                )
            } else {
                openChatbox(player, Components.SMELTING_311)
            }
            return@on true
        }

        /*
         * Handles use the ores on furnaces.
         */

        onUseWith(IntType.SCENERY, getOreIds(), *FURNACE_IDS) { player, _, with ->
            if (with.asScenery().id == Scenery.FURNACE_26814 && !isDiaryComplete(player, DiaryType.VARROCK, 0)) {
                if (!GameWorld.settings!!.isMembers) {
                    sendNPCDialogue(player, NPCs.JEFFERY_6298, "Keep away from that! It's dangerous!")
                } else {
                    sendNPCDialogue(player, NPCs.JEFFERY_6298, "You want to use my furnace?", FaceAnim.HALF_ASKING)
                    addDialogueAction(player) { _, _ ->
                        sendNPCDialogue(
                            player,
                            NPCs.JEFFERY_6298,
                            "No one can use my furnace! Only I can use my furnace!",
                            FaceAnim.ANNOYED
                        )
                    }
                }
                return@onUseWith false
            }
            openChatbox(player, Components.SMELTING_311)
            return@onUseWith true
        }

        /*
         * Handles tutorial island interaction.
         */

        onUseWith(IntType.SCENERY, tutorialOres, *specialFurnace) { player, used, _ ->
            if (!inInventory(player, Items.TIN_ORE_438) || !inInventory(player, Items.COPPER_ORE_436)) {
                player.dialogueInterpreter.sendPlainMessage(
                    false,
                    "",
                    "You do not have the required ores to make this bar.",
                    "",
                )
                TutorialStage.rollback(player)
                return@onUseWith false
            }
            if (removeItem(player, Item(Items.TIN_ORE_438, 1)) && removeItem(player, Item(Items.COPPER_ORE_436, 1))) {
                animate(player, smeltAnimation)
                playAudio(player, Sounds.FURNACE_2725, 1)
                sendTutorialMessage(player, "You smelt the copper and tin together in the furnace.")
                addItem(player, Items.BRONZE_BAR_2349)
                queueScript(player, 4, QueueStrength.SOFT) {
                    rewardXP(player, Skills.SMITHING, Bar.BRONZE.experience)
                    player.dispatch(
                        ResourceProducedEvent(
                            Bar.BRONZE.product.id,
                            1,
                            used.asItem(),
                            original = if (used.id != Items.TIN_ORE_438) Items.COPPER_ORE_436 else Items.TIN_ORE_438,
                        ),
                    )
                    sendTutorialMessage(player, "You retrieve a bar of bronze.")
                    return@queueScript stopExecuting(player)
                }
            }
            return@onUseWith true
        }
    }

    companion object {
        private val smeltAnimation = Animation(Animations.HUMAN_FURNACE_SMELT_3243)
        private val tutorialOres = intArrayOf(Items.TIN_ORE_438, Items.COPPER_ORE_436)
        private val specialFurnace = intArrayOf(Scenery.CLAY_FORGE_21303, Scenery.FURNACE_3044)

        private const val TUTORIAL_REGION = 12436

        private val FURNACE_IDS = intArrayOf(
            Scenery.FURNACE_4304,
            Scenery.FURNACE_6189,
            Scenery.FURNACE_11010,
            Scenery.FURNACE_11666,
            Scenery.FURNACE_12100,
            Scenery.FURNACE_12809,
            Scenery.SMALL_FURNACE_14921,
            Scenery.FURNACE_18497,
            Scenery.FURNACE_26814,
            Scenery.FURNACE_30021,
            Scenery.FURNACE_30510,
            Scenery.FURNACE_36956,
            Scenery.FURNACE_37651
        )

        private fun getOreIds(): IntArray {
            val ids = mutableListOf<Int>()
            for (bar in Bar.values()) {
                for (item in bar.ores) {
                    ids.add(item.id)
                }
            }
            return ids.toIntArray()
        }
    }
}
