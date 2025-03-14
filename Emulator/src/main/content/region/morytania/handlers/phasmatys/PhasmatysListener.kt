package content.region.morytania.handlers.phasmatys

import content.region.morytania.handlers.phasmatys.EnergyBarrierUtils.passGate
import content.region.morytania.handlers.phasmatys.EnergyBarrierUtils.passGateAfterQuest
import core.api.*
import core.api.quest.isQuestComplete
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.tools.END_DIALOGUE
import org.rs.consts.*

class PhasmatysListener : InteractionListener {
    companion object {
        private val BOTH_BEDSHEET = intArrayOf(Items.BEDSHEET_4284, Items.BEDSHEET_4285)
        private const val CAPTAIN = Scenery.PIRATE_CAPTAIN_5287
        private const val GHOST = NPCs.GHOST_DISCIPLE_1686
        private const val SLIME_BUCKET = Items.BUCKET_OF_SLIME_4286
        private const val BEDSHEET_BROWN = Items.BEDSHEET_4284
        private const val BEDSHEET_GREEN = Items.BEDSHEET_4285
        private const val EMPTY_BUCKET = Items.BUCKET_1925
        private const val ENERGY_BARRIER = Scenery.ENERGY_BARRIER_5259
        private const val GHOST_GUARD = NPCs.GHOST_GUARD_1706
        private val ECTO = intArrayOf(5461, 17116, 17117, 17118, 17119)
    }

    override fun defineDestinationOverrides() {
        setDest(IntType.NPC, intArrayOf(GHOST_GUARD)) { player, _ ->
            if (inBorders(player, 3660, 3508, 3662, 3509)) {
                return@setDest Location(3661, 3509, 0)
            }
            if (inBorders(player, 3657, 3508, 3659, 3509)) {
                return@setDest Location(3658, 3509, 0)
            }
            if (inBorders(player, 3651, 3486, 3652, 3488)) {
                return@setDest Location(3651, 3487, 0)
            }
            if (inBorders(player, 3651, 3483, 3652, 3485)) {
                return@setDest Location(3651, 3484, 0)
            }
            return@setDest player.location
        }
    }

    override fun defineListeners() {
        on(CAPTAIN, IntType.SCENERY, "talk-to") { player, _ ->
            sendDialogue(
                player,
                "The pirate captain ignores you and continues to stare lifelessly at nothing, as he has clearly been dead for some time.",
            )
            return@on true
        }

        on(GHOST, IntType.NPC, "collect") { player, node ->
            openDialogue(player, NPCs.GHOST_DISCIPLE_1686, node.asNpc(), true)
            return@on true
        }

        onUseWith(IntType.ITEM, SLIME_BUCKET, Items.BEDSHEET_4284) { player, used, with ->
            if (removeItem(player, used.asItem()) && removeItem(player, with.asItem())) {
                addItemOrDrop(player, BEDSHEET_GREEN)
                addItemOrDrop(player, EMPTY_BUCKET)
            }
            return@onUseWith true
        }

        onEquip(BEDSHEET_BROWN) { player, _ ->
            if (inEquipment(player, BEDSHEET_GREEN)) return@onEquip false
            player.appearance.transformNPC(1707)
            return@onEquip true
        }

        onEquip(BEDSHEET_GREEN) { player, _ ->
            if (inEquipment(player, BEDSHEET_BROWN)) return@onEquip false
            if (inBorders(player, 3673, 9955, 3685, 9964) || inBorders(player, 3650, 3456, 3737, 3508)) {
                player.appearance.transformNPC(1708)
                return@onEquip true
            } else {
                sendDialogue(player, "You can only wear the bedsheet inside Port Phasmatys.")
                return@onEquip false
            }
        }

        onUnequip(BOTH_BEDSHEET) { player, _ ->
            player.appearance.transformNPC(-1)
            player.appearance.sync()
            return@onUnequip true
        }

        on(ENERGY_BARRIER, IntType.SCENERY, "pay-toll(2-ecto)", "pass") { player, node ->
            if (inEquipment(player, BEDSHEET_GREEN)) {
                sendDialogue(player, "You can't pass the barriers of Port Phasmatys while wearing the bedsheet.")
            } else if (inBorders(player, 3658, 3506, 3661, 3507) || inBorders(player, 3653, 3484, 3654, 3487)) {
                passGate(player, node.asScenery())
            } else {
                openDialogue(
                    player,
                    object : DialogueFile() {
                        override fun handle(
                            componentID: Int,
                            buttonID: Int,
                        ) {
                            npc = NPC(GHOST_GUARD)
                            when (stage) {
                                0 -> {
                                    if (isQuestComplete(player, Quests.GHOSTS_AHOY) &&
                                        inEquipment(player, Items.GHOSTSPEAK_AMULET_552)
                                    ) {
                                        npcl(
                                            FaceAnim.FRIENDLY,
                                            "All visitors to Port Phasmatys must pay a toll charge of 2 Ectotokens. However, you have done the ghosts of our town a service that surpasses all value, so you may pass without charge.",
                                        )
                                        stage = 7
                                    } else if (!inEquipment(player, Items.GHOSTSPEAK_AMULET_552)) {
                                        sendDialogue(
                                            player,
                                            "The ghostly guards wail at you incomprehensibly, and though you cannot understand their exact words you understand their meaning - you may not pass the barriers of Port Phasmatys.",
                                        )
                                        stage = END_DIALOGUE
                                    } else {
                                        findLocalNPC(player, 1706)?.let { face(player, it, 1) }
                                        npcl(
                                            FaceAnim.FRIENDLY,
                                            "All visitors to Port Phasmatys must pay a toll charge of 2 Ectotokens.",
                                        )
                                        stage = 1
                                    }
                                }

                                1 -> {
                                    if (amountInInventory(player, Items.ECTO_TOKEN_4278) >= 2) {
                                        options(
                                            "I would like to enter Port Phasmatys - here's 2 Ectotokens.",
                                            "I'm not paying you Ectotokens just to go through a gate.",
                                            "Where can I get Ectotokens?",
                                        ).also { stage++ }
                                    } else {
                                        options(
                                            "I don't have that many Ectotokens.",
                                            "Where can I get Ectotokens?",
                                        ).also { stage = 6 }
                                    }
                                }

                                2 ->
                                    when (buttonID) {
                                        1 ->
                                            player(
                                                "I would like to enter Port Phasmatys - here's",
                                                "2 Ectotokens.",
                                            ).also { stage = 3 }

                                        2 ->
                                            player("I'm not paying you Ectotockens just to go through a gate.").also {
                                                stage = 4
                                            }

                                        3 -> player("Where can I get Ectotokens?").also { stage = 5 }
                                    }

                                3 -> {
                                    end()
                                    passGate(player, node.asScenery())
                                }

                                4 -> npc("Sorry - it's Town Policy.").also { stage = END_DIALOGUE }
                                5 ->
                                    npc(
                                        "You need to go to the Temple and earn some.",
                                        "Talk to the disciples - they will tell you how.",
                                    ).also { stage = END_DIALOGUE }

                                6 ->
                                    when (buttonID) {
                                        1 -> player("I don't have that many Ectotokens.").also { stage = 4 }
                                        2 -> player("Where can I get Ectotokens?").also { stage = 5 }
                                    }

                                7 -> {
                                    end()
                                    passGateAfterQuest(player, node.asScenery())
                                }
                            }
                        }
                    },
                    node.asScenery(),
                )
            }
            return@on true
        }

        onUseWith(IntType.SCENERY, EMPTY_BUCKET, *ECTO) { player, used, _ ->
            animate(player, Animation.create(4471))
            playAudio(player, Sounds.FILL_ECTOPLASM_1132)
            sendMessage(player, "You fill the bucket with ectoplasm.")
            submitIndividualPulse(
                player,
                object : Pulse(3, player) {
                    override fun pulse(): Boolean {
                        if (removeItem(player, used.asItem())) {
                            addItem(player, Items.BUCKET_OF_SLIME_4286)
                            if (inInventory(player, Items.BUCKET_1925, 1)) {
                                player.animate(Animation.create(4471), 1)
                                playAudio(player, Sounds.FILL_ECTOPLASM_1132)
                                return false
                            }
                        }
                        return !player.inventory.contains(1925, 1)
                    }
                },
            )
            return@onUseWith true
        }
    }
}
