package content.region.kandarin.quest.phoenix.handlers

import core.api.*
import core.api.interaction.getSceneryName
import core.api.item.allInInventory
import core.api.quest.isQuestComplete
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.link.TeleportManager
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import org.rs.consts.*

val allTwigs =
    intArrayOf(
        Items.CINNAMON_TWIGS_14606,
        Items.SASSAFRAS_TWIGS_14607,
        Items.AILANTHUS_TWIGS_14608,
        Items.CEDAR_TWIGS_14609,
        Items.MASTIC_TWIGS_14610,
    )

class PhoenixLairListener : InteractionListener {
    init {
        woundedPhoenix.init()
    }

    companion object {
        val weavingRibbons =
            intArrayOf(
                Items.CINNAMON_WEAVING_RIBBON_14611,
                Items.CEDAR_WEAVING_RIBBON_14614,
                Items.AILANTHUS_WEAVING_RIBBON_14613,
                Items.SASSAFRAS_WEAVING_RIBBON_14612,
                Items.MASTIC_WEAVING_RIBBON_14615,
            )

        private val treeItemMap =
            mapOf(
                Scenery.CINNAMON_TREE_41903 to Items.CINNAMON_TWIGS_14606,
                Scenery.SASSAFRAS_TREE_41904 to Items.SASSAFRAS_TWIGS_14607,
                Scenery.AILANTHUS_TREE_41905 to Items.AILANTHUS_TWIGS_14608,
                Scenery.CEDAR_TREE_41906 to Items.CEDAR_TWIGS_14609,
                Scenery.MASTIC_TREE_41907 to Items.MASTIC_TWIGS_14610,
            )

        val woundedPhoenix: NPC = NPC.create(NPCs.WOUNDED_PHOENIX_8547, Location.create(3534, 5196, 0), Direction.NORTH)
    }

    override fun defineListeners() {
        /*
         * Handles entering the Phoenix Lair.
         */

        on(Scenery.CAVE_ENTRANCE_41900, IntType.SCENERY, "enter") { player, _ ->
            val familiar = player.familiarManager.familiar
            if (player.familiarManager.hasFamiliar() &&
                familiar != null &&
                (familiar.id == NPCs.PHOENIX_8575 || familiar.id == NPCs.PHOENIX_8576)
            ) {
                sendNPCDialogue(
                    player,
                    NPCs.PHOENIX_8575,
                    "${player.username}, this is my lair. You must dismiss my summoned form if you wish to enter; I will only duel you when I am at full strength.",
                )
                return@on true
            }
            if (getVarbit(player, Vars.VARBIT_QUEST_IN_PYRE_NEED_PROGRESS_5761) < 1 ||
                !isQuestComplete(player, Quests.IN_PYRE_NEED)
            ) {
                sendPlayerDialogue(
                    player,
                    "Hey, wait! I have something to ask you before going down there.",
                    FaceAnim.THINKING,
                )
                addDialogueAction(player) { p, button ->
                    if (button > 0) {
                        sendPlayerDialogue(
                            p,
                            "Maybe I should see what he has to say before entering this dark, scary, and unreasonably warm cave.",
                            FaceAnim.THINKING,
                        )
                    }
                }
            } else {
                teleport(player, Location.create(3461, 5218, 0), TeleportManager.TeleportType.INSTANT)
            }
            return@on true
        }

        /*
         * Handles random cave teleportation logic.
         */

        on(
            intArrayOf(Scenery.CAVE_ENTRANCE_41901, Scenery.CAVE_ENTRANCE_41902),
            IntType.SCENERY,
            "look-at",
            "enter",
        ) { player, _ ->
            val locations =
                listOf(
                    Location.create(3466, 5213, 0),
                    Location.create(3493, 5186, 0), // Sassafras
                    Location.create(3543, 5218, 0),
                    Location.create(3476, 5186, 0),
                    Location.create(3516, 5224, 0), // Authentic
                )

            val phoenixChamber = Location(3566, 5224, 0)
            val outsideCave = Location.create(2294, 3626, 0)
            val randomRoll = RandomFunction.random(100)

            if (player.location == phoenixChamber) {
                setTitle(player, 2)
                sendDialogueOptions(player, "Are you sure you want to leave?", "Yes", "No")
                addDialogueAction(player) { _, button ->
                    when (button) {
                        1 -> {
                            openInterface(player, Components.FADE_TO_BLACK_120)
                            player.animate(Animation.create(Animations.HUMAN_CRAWL_INTO_CAVE_11042), 1)

                            queueScript(player, 3, QueueStrength.SOFT) {
                                teleport(player, Location(3566, 5224, 0))
                                resetAnimator(player)
                                openInterface(player, Components.FADE_FROM_BLACK_170)
                                return@queueScript stopExecuting(player)
                            }
                        }
                        2 -> {
                            openInterface(player, Components.FADE_TO_BLACK_120)
                            player.animate(Animation.create(Animations.HUMAN_CRAWL_INTO_CAVE_11042), 1)

                            queueScript(player, 3, QueueStrength.SOFT) {
                                teleport(player, locations.random())
                                resetAnimator(player)
                                openInterface(player, Components.FADE_FROM_BLACK_170)
                                return@queueScript stopExecuting(player)
                            }
                        }
                    }
                }
                return@on true
            }

            if (randomRoll == 0) {
                teleport(player, phoenixChamber)
                return@on true
            }

            val newLocation =
                when {
                    getVarbit(player, Vars.VARBIT_QUEST_IN_PYRE_NEED_PROGRESS_5761) == 10 -> outsideCave
                    allInInventory(player, *allTwigs) -> Location.create(3535, 5186, 0)
                    else -> locations.random()
                }

            openInterface(player, Components.FADE_TO_BLACK_120)
            player.animate(Animation.create(Animations.HUMAN_CRAWL_INTO_CAVE_11042), 1)

            queueScript(player, 3, QueueStrength.SOFT) {
                teleport(player, newLocation)
                if (newLocation == Location.create(3535, 5186, 0)) {
                    player.musicPlayer?.let { music ->
                        if (!music.hasUnlocked(Music.THE_PHOENIX_596)) {
                            music.unlock(Music.THE_PHOENIX_596)
                        }
                    }
                }
                resetAnimator(player)
                openInterface(player, Components.FADE_FROM_BLACK_170)
                return@queueScript stopExecuting(player)
            }

            return@on true
        }

        /*
         * Handles escaping from the Phoenix lair.
         */

        on(
            intArrayOf(Scenery.CAVE_ENTRANCE_41901, Scenery.CAVE_ENTRANCE_41902),
            IntType.SCENERY,
            "escape",
        ) { player, _ ->
            openInterface(player, Components.FADE_TO_BLACK_120)
            animate(player, Animations.HUMAN_CRAWL_INTO_CAVE_11042)
            runTask(player, 3) {
                resetAnimator(player)
                openInterface(player, Components.FADE_FROM_BLACK_170)
                teleport(player, Location.create(2294, 3626, 0))
                if (!isQuestComplete(player, Quests.IN_PYRE_NEED)) {
                    player.dialogueInterpreter.open(NPCs.PRIEST_OF_GUTHIX_8555)
                }
            }
            return@on true
        }

        /*
         * Handles harvesting twigs from trees.
         */

        on(treeItemMap.keys.toIntArray(), IntType.SCENERY, "harvest-twigs") { player, node ->
            val item = treeItemMap[node.id] ?: Items.MASTIC_TWIGS_14610
            if (!player.inventory.contains(item, 1)) {
                lock(player, 3)
                animate(player, Animations.PRUNE_WITH_SECATEURS_11088)

                addItemOrDrop(player, item)
                val twigs = getSceneryName(node.id).lowercase().replace("tree", "twigs")
                sendMessage(player, "You harvest some $twigs.")
            } else {
                sendMessage(player, "You already have ${getItemName(item).lowercase()}.")
            }
            return@on true
        }

        /*
         * Handles weaving ribbons into a basket.
         */

        onUseWith(IntType.SCENERY, weavingRibbons, Scenery.PYRE_41908) { player, _, _ ->
            if (getVarbit(player, Vars.VARBIT_QUEST_IN_PYRE_NEED_PROGRESS_5761) >= 5) {
                animate(player, Animations.PRUNE_WITH_SECATEURS_11088)
                sendMessage(
                    player,
                    "You weave a large basket from the five wooden ribbons and add it to the pyre base.",
                )
                WoundedPhoenixCutscene(player).start(true)
                weavingRibbons.forEach {
                    removeItem(player, Item(it, 1), Container.INVENTORY)
                }
            }
            return@onUseWith true
        }

        /*
         * Handles lighting the pyre.
         */

        onUseWith(IntType.SCENERY, Items.TINDERBOX_590, Scenery.PYRE_41908) { player, _, _ ->
            if (getStatLevel(player, Skills.FIREMAKING) < 55) {
                sendDialogue(player, "You need at least 55 firemaking to light the funeral pyre.")
                return@onUseWith false
            }
            if (getVarbit(player, Vars.VARBIT_QUEST_IN_PYRE_NEED_PROGRESS_5761) >= 5) {
                animate(player, Animations.TINDERBOX_733)
                FuneralPyreCutscene(player).start()
            }
            return@onUseWith true
        }

        /*
         * Prevents players from lighting the wounded Phoenix.
         */

        onUseWith(IntType.NPC, Items.TINDERBOX_590, NPCs.WOUNDED_PHOENIX_8547) { player, _, _ ->
            sendNPCDialogue(player, NPCs.WOUNDED_PHOENIX_8547, "Light the pyre, not me!", FaceAnim.OLD_NORMAL)
            return@onUseWith true
        }

        /*
         * Handles fletch twigs into ribbons.
         */

        onUseWith(IntType.ITEM, Items.KNIFE_946, *allTwigs) { player, _, with ->
            if (allTwigs.isNotEmpty() && allTwigs.all { hasAnItem(player, it).container == player.inventory }) {
                lock(player, 1)
                animate(player, Animations.FLETCH_LOGS_4433)
                queueScript(player, 2, QueueStrength.SOFT) {
                    allTwigs.forEach { removeItem(player, Item(it, 1), Container.INVENTORY) }
                    setVarbit(player, Vars.VARBIT_QUEST_IN_PYRE_NEED_PROGRESS_5761, 5, true)
                    weavingRibbons.forEach {
                        addItem(player, it, 1, Container.INVENTORY)
                    }
                    sendItemDialogue(
                        player,
                        with.id,
                        "You fetch the ${getItemName(with.asItem().id).lowercase()} into wooden ribbons.",
                    )
                    return@queueScript stopExecuting(player)
                }
            } else {
                sendMessage(player, "You don't have the required twigs to fletch.")
                return@onUseWith false
            }
            return@onUseWith true
        }
    }
}
