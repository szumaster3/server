package content.region.kandarin.quest.elementalquest2.handlers

import content.region.kandarin.quest.elementalquest2.dialogue.SchematicCrateDialogue
import core.api.*
import core.api.interaction.transformNpc
import core.api.quest.isQuestComplete
import core.api.quest.setQuestStage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.scenery.SceneryBuilder
import core.game.system.task.Pulse
import core.game.world.map.Location
import org.rs.consts.*

class EW2Listener : InteractionListener {
    init {
        EW2Utils.JigCartNPC.init()
    }

    override fun defineListeners() {
        fun eW2scroll(player: Player) {
            val ew2Scroll =
                arrayOf("Down", "", "2 N", "", "2 E", "", "2 N", "", "2 E", "", "10 N", "", "4 W", "", "Pipe 3.")
            sendString(player, ew2Scroll.joinToString("<br>"), EW2Utils.BlankScroll, 1)
        }

        on(Items.SCROLL_9721, IntType.ITEM, "read") { player, _ ->
            openInterface(player, EW2Utils.BlankScroll).also { eW2scroll(player) }
            setQuestStage(player, Quests.ELEMENTAL_WORKSHOP_II, 3)
            return@on true
        }

        on(Scenery.BOOKCASE_17382, IntType.SCENERY, "search") { player, _ ->
            if (isQuestComplete(player, Quests.ELEMENTAL_WORKSHOP_I) &&
                !inInventory(player, Items.BEATEN_BOOK_9717) &&
                hasLevelStat(player, Skills.MAGIC, 20) &&
                hasLevelStat(player, Skills.SMITHING, 30)
            ) {
                sendItemDialogue(player, Items.BEATEN_BOOK_9717, "You find an old-looking book.")
                addItemOrDrop(player, Items.BEATEN_BOOK_9717)
                setQuestStage(player, Quests.ELEMENTAL_WORKSHOP_II, 1)
                setAttribute(player, EW2Utils.foundBook, 1)
                setVarbit(player, EW2Utils.EW2_QUEST_VARBIT, 1, true)
            } else {
                sendMessage(player, "You search the books...")
                sendMessage(player, "You find nothing of interests to you.")
            }
            return@on true
        }

        on(Scenery.BOOKCASE_17321, IntType.SCENERY, "search") { player, _ ->
            sendMessage(player, "You search through the bookcase...")
            player.dialogueInterpreter.sendDialogue(
                "The label on this shelf reads 'Earth Sciences'; however, the helpful",
                "books have been taken. It looks like the other students got to them",
                "first.",
            )
            return@on true
        }

        on(Scenery.MACHINERY_18594, IntType.SCENERY, "search") { player, _ ->
            sendItemDialogue(player, Items.KEY_9722, "You find a key hidden in the third pipe.")
            setQuestStage(player, Quests.ELEMENTAL_WORKSHOP_II, 4)
            addItemOrDrop(player, Items.KEY_9722)
            return@on true
        }

        onUseWith(IntType.SCENERY, Items.KEY_9722, Scenery.HATCH_18595) { player, _, _ ->
            sendItemDialogue(player, Items.KEY_9722, "The key fits the lock in the hatch perfectly.")
            setVarbit(player, EW2Utils.EW2_HATCH_VARBIT, 3)
            setQuestStage(player, Quests.ELEMENTAL_WORKSHOP_II, 5)
            return@onUseWith true
        }

        on(Scenery.HATCH_18596, IntType.SCENERY, "climb-down") { player, _ ->
            sendMessage(player, "You climb down the stairs.")
            teleport(player, location(1955, 5155, 2))
            player.musicPlayer.unlock(Music.PRIME_TIME_202, true)
            return@on true
        }

        on(Scenery.STAIRS_18598, IntType.SCENERY, "climb-up") { player, _ ->
            animate(player, 828)
            runTask(player, 2) {
                teleport(player, location(2720, 9892, 0))
            }
            sendMessage(player, "You climb up the stairs.")
            return@on true
        }

        on(Scenery.STAIRWELL_18597, IntType.SCENERY, "climb-down") { player, _ ->
            teleport(player, location(1948, 5158, 0))
            sendMessage(player, "You climb down the stairs.")
            return@on true
        }

        on(Scenery.STAIRS_18599, IntType.SCENERY, "climb-up") { player, _ ->
            animate(player, 828)
            runTask(player, 2) {
                teleport(player, location(1948, 5157, 2))
            }
            sendMessage(player, "You climb up the stairs.")
            return@on true
        }

        on(EW2Utils.PipeCrate, IntType.SCENERY, "search") { player, _ ->
            if (!inInventory(player, EW2Utils.Pipe)) {
                lock(player, 2)
                animate(player, EW2Utils.SearchCrate)
                addItemOrDrop(player, EW2Utils.Pipe)
                sendMessage(player, "You find a pipe.")
            } else {
                sendMessage(player, "You search the crate but find nothing.")
            }
            return@on true
        }

        on(EW2Utils.SmallCogCrate, IntType.SCENERY, "search") { player, _ ->
            if (!inInventory(player, EW2Utils.SmallCog)) {
                lock(player, 2)
                animate(player, EW2Utils.SearchCrate)
                addItemOrDrop(player, EW2Utils.SmallCog)
                sendMessage(player, "You find a small cog.")
            } else {
                sendMessage(player, "You search the crate but find nothing.")
            }
            return@on true
        }

        on(EW2Utils.MediumCogCrate, IntType.SCENERY, "search") { player, _ ->
            if (!inInventory(player, EW2Utils.MediumCog)) {
                lock(player, 2)
                animate(player, EW2Utils.SearchCrate)
                addItemOrDrop(player, EW2Utils.MediumCog)
                sendMessage(player, "you find a medium-sized cog.")
            } else {
                sendMessage(player, "You search the crate but find nothing.")
            }
            return@on true
        }

        on(EW2Utils.LargeCogCrate, IntType.SCENERY, "search") { player, _ ->
            if (!inInventory(player, EW2Utils.LargeCog)) {
                lock(player, 2)
                animate(player, EW2Utils.SearchCrate)
                addItemOrDrop(player, EW2Utils.LargeCog)
                sendMessage(player, "you find a large cog.")
            } else {
                sendMessage(player, "You search the crate but find nothing.")
            }
            return@on true
        }

        on(EW2Utils.SchematicCrate, IntType.SCENERY, "take-from") { player, _ ->
            player.packetDispatch.sendInterfaceConfig(228, 6, true)
            player.packetDispatch.sendInterfaceConfig(228, 9, false)
            openDialogue(player, SchematicCrateDialogue())
            return@on true
        }

        on(EW2Utils.CraneSchematic, IntType.ITEM, "read") { player, _ ->
            player.dialogueInterpreter.sendDialogue(
                "The schematic drawing details the construction and maintenance of a",
                "mechanical arm. One of the sections you notice is an explanation of",
                "how to make an elemental claw.",
            )
            return@on true
        }

        on(EW2Utils.LeverSchematic, IntType.ITEM, "read") { player, _ ->
            openInterface(player, EW2Utils.SwitchDiagram)
            return@on true
        }

        on(EW2Utils.JunctionBox, IntType.SCENERY, "open") { player, _ ->
            openInterface(player, Components.JUNCTION_BOX_262)
            return@on true
        }

        on(EW2Utils.Doors, IntType.SCENERY, "take") { player, _ ->
            sendMessage(player, "The doors appear to be stuck.")
            return@on true
        }

        on(EW2Utils.LeftLever, IntType.SCENERY, "pull") { player, _ ->
            animate(player, EW2Utils.PullLever)
            playAudio(player, Sounds.EW2_CRANE_TURN_3170)
            if (player.getAttribute(EW2Utils.PullSupport, true)) {
                SceneryBuilder.add(
                    core.game.node.scenery.Scenery(
                        EW2Utils.BrokenCraneUp,
                        location(1954, 5145, 2),
                    ),
                )

                removeAttribute(player, EW2Utils.PullSupport)
            } else {
                core.game.node.scenery
                    .Scenery(EW2Utils.BrokenCraneDown, location(1954, 5145, 2))
                setAttribute(player, EW2Utils.PullSupport, true)
            }

            if (player.getAttribute(EW2Utils.PullSupport, true) &&
                player.getAttribute(
                    EW2Utils.TurnSupport,
                    true,
                )
            ) {
                SceneryBuilder.add(
                    core.game.node.scenery.Scenery(
                        EW2Utils.BrokenCraneUpOnCorrectSide,
                        Location(1954, 5145, 2),
                    ),
                )
                removeAttribute(player, EW2Utils.TurnSupport)
            } else {
                SceneryBuilder.add(
                    core.game.node.scenery.Scenery(
                        EW2Utils.BrokenCraneDown,
                        location(1954, 5145, 2),
                    ),
                )
                setAttribute(player, EW2Utils.TurnSupport, true)
            }
            return@on true
        }

        on(EW2Utils.RightLever, IntType.SCENERY, "pull") { player, _ ->
            if (player.getAttribute(EW2Utils.PullSupport, true)) {
                playAudio(player, Sounds.EW2_CRANE_LOWER_3169)
                addScenery(EW2Utils.BrokenCraneDownOnCorrectSide, Location.create(1954, 5145, 2))
                removeAttribute(player, EW2Utils.PullSupport)
            }

            animate(player, EW2Utils.PullLever)
            return@on true
        }

        on(EW2Utils.CenterLever, IntType.SCENERY, "pull") { player, _ ->
            animate(player, EW2Utils.PullLever)
            player.dialogueInterpreter.sendDialogue(
                "You pull the lever and the press responds. The hardy comes down,",
                "hitting the elemental bar. The bar has been flattened",
            )
            return@on true
        }

        on(EW2Utils.EastLever, IntType.SCENERY, "pull") { player, _ ->
            animate(player, EW2Utils.PullLever)
            submitIndividualPulse(
                player,
                object : Pulse() {
                    var count = 0

                    override fun pulse(): Boolean {
                        when (count++) {
                            0 -> sendDialogue(player, "the fan starts to blow.")
                            2 -> sendDialogue(player, "the machine starts rumbling.")
                        }
                        return false
                    }
                },
            )
            return@on true
        }

        on(EW2Utils.CartLever, IntType.SCENERY, "pull") { player, _ ->
            lock(player, 5)
            animate(player, EW2Utils.PullLever)
            player.dialogueInterpreter.sendDialogue(
                "The cart does not respond; maybe the cart is being blocked by",
                "watertank door",
            )
            return@on true
        }

        on(EW2Utils.NorthLever, IntType.SCENERY, "pull") { player, _ ->
            animate(player, EW2Utils.PullLever)
            playAudio(player, Sounds.PULL_LEVER_GENERAL_2400)
            if (getAttribute(player, "ew2:door-lock-cart", false)) {
                SceneryBuilder.add(
                    core.game.node.scenery.Scenery(
                        EW2Utils.PipingDoorClose,
                        location(1953, 5163, 2),
                    ),
                )
                removeAttribute(player, "ew2:door-lock-cart")
                return@on true
            } else {
                SceneryBuilder.remove(
                    core.game.node.scenery
                        .Scenery(18651, location(1953, 5163, 2)),
                )
                SceneryBuilder.add(
                    core.game.node.scenery.Scenery(
                        EW2Utils.PipingDoorOpen,
                        location(1953, 5163, 2),
                    ),
                )
                setAttribute(player, "ew2:door-lock-cart", true)
            }
            return@on true
        }

        on(EW2Utils.WaterValveLeft, IntType.SCENERY, "turn") { player, _ ->
            player.locks.lockInteractions(4)
            animate(player, EW2Utils.turnValveAnimation)
            if (player.getAttribute(EW2Utils.FillingWaterTank, false) == true) {
                runTask(player, 3) {
                    SceneryBuilder.add(
                        core.game.node.scenery.Scenery(
                            18660,
                            location(1951, 5164, 2),
                        ),
                    )
                    playAudio(player, Sounds.EW2_WATER_RACK_3184)
                    playAudio(player, Sounds.EW2_WATER_VALVE_3185)
                    removeAttribute(player, EW2Utils.FillingWaterTank)
                }
            } else {
                runTask(player, 3) {
                    SceneryBuilder.add(
                        core.game.node.scenery.Scenery(
                            18661,
                            location(1951, 5164, 2),
                        ),
                    )
                    playAudio(player, Sounds.EW2_WATER_FLOW_3183)
                    playAudio(player, Sounds.EW2_WATER_VALVE_3185)
                    setAttribute(player, EW2Utils.FillingWaterTank, true)
                }
            }
            return@on true
        }

        on(EW2Utils.WaterValveRight, IntType.SCENERY, "turn") { player, _ ->
            lock(player, 3)
            animate(player, EW2Utils.turnValveAnimation)
            return@on true
        }

        on(EW2Utils.JigCartWithElementalBar, IntType.NPC, "take-from") { player, _ ->
            lock(player, 2)
            animate(player, EW2Utils.SearchCrate)
            addItemOrDrop(player, EW2Utils.ElementalBar)
            sendItemDialogue(player, EW2Utils.ElementalBar, "You take the elemental bar.")
            return@on true
        }

        on(EW2Utils.JigCartWithElementalMindBar, IntType.NPC, "take-from") { player, _ ->
            lock(player, 2)
            animate(player, EW2Utils.SearchCrate)
            addItemOrDrop(player, EW2Utils.ElementalMindBar)
            sendItemDialogue(player, EW2Utils.PrimedBar, "You take the elemental mind bar.")
            return@on true
        }

        on(EW2Utils.JigCartWithPrimedBar, IntType.NPC, "take-from") { player, _ ->
            lock(player, 2)
            animate(player, EW2Utils.SearchCrate)
            addItemOrDrop(player, EW2Utils.PrimedBar)
            sendItemDialogue(player, EW2Utils.PrimedBar, "You take the primed bar.")
            return@on true
        }

        on(EW2Utils.Doors, IntType.SCENERY, "open") { player, _ ->
            sendMessage(player, "The doors appear to be stuck.")
            return@on true
        }

        onUseWith(IntType.SCENERY, EW2Utils.Pipe, EW2Utils.BrokenPipe) { player, _, _ ->
            sendItemDialogue(player, EW2Utils.Pipe, "You replace the section of broken pipe.")
            playAudio(player, Sounds.EW2_PLACE_PIPE_3179)
            setVarbit(player, EW2Utils.EW2_PIPE_VARBIT, 1, true)
            removeItem(player, EW2Utils.Pipe)
            return@onUseWith true
        }

        onUseWith(IntType.SCENERY, EW2Utils.elementalClaw, EW2Utils.OldCrane) { player, _, _ ->
            sendItemDialogue(player, EW2Utils.elementalClaw, "You replace the broken claw.")
            removeItem(player, EW2Utils.elementalClaw)
            return@onUseWith true
        }

        onUseWith(IntType.NPC, Items.ELEMENTAL_METAL_2893, EW2Utils.EmptyJigCart) { player, _, _ ->
            sendItemDialogue(player, EW2Utils.elementalClaw, "You replace the broken claw.")
            removeItem(player, Items.ELEMENTAL_METAL_2893)
            transformNpc(NPC(EW2Utils.EmptyJigCart), EW2Utils.JigCartWithElementalBar, -1)
            return@onUseWith true
        }

        onUseWith(IntType.SCENERY, EW2Utils.allCogs, EW2Utils.Pin) { player, used, _ ->
            if (removeItem(player, EW2Utils.SmallCog)) {
                setVarbit(player, EW2Utils.EW2_SMALL_COG_VARBIT, 3, true)
                sendItemDialogue(player, used.asItem(), "You put the small cog onto the shaft.")
                return@onUseWith true
            }
            if (removeItem(player, EW2Utils.MediumCog)) {
                setVarbit(player, EW2Utils.EW2_MEDIUM_COG_VARBIT, 3, true)
                sendItemDialogue(player, used.asItem(), "You put the medium-sized cog onto the shaft.")
                return@onUseWith true
            }
            if (removeItem(player, EW2Utils.LargeCog)) {
                setVarbit(player, EW2Utils.EW2_LARGE_COG_VARBIT, 3, true)
                sendItemDialogue(player, used.asItem(), "You put the large cog onto the shaft.")
                return@onUseWith true
            }
            return@onUseWith true
        }

        on(EW2Utils.TakeSmallCog, IntType.SCENERY, "take") { player, _ ->
            sendItemDialogue(player, EW2Utils.SmallCog, "You take the large cog from the shaft.")
            addItemOrDrop(player, EW2Utils.SmallCog, 1)
            setVarbit(player, EW2Utils.EW2_SMALL_COG_VARBIT, 0, true)
            return@on true
        }

        on(EW2Utils.TakeMediumCog, IntType.SCENERY, "take") { player, _ ->
            sendItemDialogue(player, EW2Utils.MediumCog, "You take the medium-sized cog from the shaft.")
            addItemOrDrop(player, EW2Utils.MediumCog, 1)
            setVarbit(player, EW2Utils.EW2_MEDIUM_COG_VARBIT, 0, true)
            return@on true
        }

        on(EW2Utils.TakeLargeCog, IntType.SCENERY, "take") { player, _ ->
            sendItemDialogue(player, EW2Utils.LargeCog, "You take the medium-sized cog from the shaft.")
            addItemOrDrop(player, EW2Utils.LargeCog, 1)
            setVarbit(player, EW2Utils.EW2_LARGE_COG_VARBIT, 0, true)
            return@on true
        }

        on(EW2Utils.CorkscrewLever, IntType.SCENERY, "turn") { player, _ ->
            lock(player, 3)
            animate(player, EW2Utils.PullCorkscrewLever)
            playAudio(player, Sounds.EW2_SCREW_TURN_3182)
            return@on true
        }
    }

    override fun defineDestinationOverrides() {
        setDest(IntType.SCENERY, intArrayOf(Scenery.STAIRS_18599), "climb-up") { _, _ ->
            return@setDest Location.create(1948, 5158, 0)
        }

        setDest(IntType.SCENERY, intArrayOf(Scenery.PIPING_18650), "use") { _, _ ->
            return@setDest Location.create(1953, 5167, 3)
        }
    }
}
