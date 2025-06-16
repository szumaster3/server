package content.region.island.apeatoll.dialogue

import content.global.plugin.item.withnpc.ItemsOnZooknockListener
import content.region.karamja.quest.mm.cutscene.DungeonPlanCutscene
import core.api.*
import core.api.quest.setQuestStage
import core.game.component.Component
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.item.Item
import core.game.world.update.flag.context.Graphics
import core.tools.END_DIALOGUE
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.Quests

class ZooknockAfterBattleDialogueFile : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        when (stage) {
            0 -> end()
        }
    }
}

class ZooknockDialogueFile(
    val it: Int,
) : DialogueFile() {
    var i = ItemsOnZooknockListener()
    var n = NPC(i.zooknockNPC)
    var itemUsed = it

    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = n

        when (stage) {
            0 ->
                when {
                    itemUsed == Items.GOLD_BAR_2357 && !getAttribute(player!!, "/save:mm:gold-given", false) -> {
                        setAttribute(player!!, "/save:mm:gold-given", true)
                        playerl("What do you think of this?")
                        player!!.inventory.remove(Item(Items.GOLD_BAR_2357))
                        stage = 15
                    }

                    itemUsed == Items.MAMULET_MOULD_4020 && !getAttribute(player!!, "/save:mm:mould-given", false) -> {
                        setAttribute(player!!, "/save:mm:mould-given", true)
                        playerl("What do you think of this?")
                        player!!.inventory.remove(Item(Items.MAMULET_MOULD_4020))
                        stage = 20
                    }

                    itemUsed == Items.MONKEY_DENTURES_4006 &&
                        !getAttribute(
                            player!!,
                            "/save:mm:dentures-given",
                            false,
                        ) -> {
                        setAttribute(player!!, "/save:mm:dentures-given", true)
                        playerl("What do you think of this?")
                        player!!.inventory.remove(Item(Items.MONKEY_DENTURES_4006))
                        stage = 30
                    }

                    itemUsed == Items.MONKEY_BONES_3179 && !getAttribute(player!!, "/save:mm:bones-given", false) -> {
                        setAttribute(player!!, "/save:mm:bones-given", true)
                        sendItemDialogue(player!!, Items.MONKEY_BONES_3179, "You hand Zooknock the monkey remains.")
                        player!!.inventory.remove(Item(Items.MONKEY_BONES_3179))
                        stage = 3
                    }

                    itemUsed == Items.MONKEY_TALISMAN_4023 &&
                        !getAttribute(
                            player!!,
                            "/save:mm:talisman-given",
                            false,
                        ) -> {
                        setAttribute(player!!, "/save:mm:talisman-given", true)
                        sendItemDialogue(player!!, Items.MONKEY_TALISMAN_4023, "You hand Zooknock the monkey talisman.")
                        player!!.inventory.remove(Item(Items.MONKEY_TALISMAN_4023))
                        stage = 3
                    }
                }

            2 -> npcl(FaceAnim.ASKING, "Nicely done!").also { stage++ }
            3 -> {
                if (validateTalismanItemsGiven() != null) {
                    npcl(FaceAnim.OLD_CALM_TALK1, validateTalismanItemsGiven()).also { stage = END_DIALOGUE }
                } else {
                    npcl("Excellent!").also { stage = 5 }
                }
            }

            5 ->
                npcl(
                    "Bear with me human: I must now cast an extremely powerful spell. It is not often we are successful in investing shapeshifting powers within objects.",
                ).also {
                    stage++
                }
            6 ->
                sendItemDialogue(
                    player!!,
                    Items.MONKEY_GREEGREE_4024,
                    "Zooknock hands you back the talisman. It seems to glow slightly.",
                ).also {
                    addItemOrDrop(player!!, Items.MONKEY_GREEGREE_4024, 1)
                    npc!!.graphics(
                        Graphics(
                            org.rs.consts.Graphics.WIND_BLAST_IMPACT_134,
                            96,
                        ),
                    )
                    stage = 8
                }

            8 ->
                npcl(
                    "I am afraid I have not been able to fully invest my powers in that talisman. You may use it, but it will continue to draw its energy directly from me.",
                ).also {
                    stage++
                }
            9 ->
                npcl(
                    "The range at which I will be able to sustain it is limited. I cannot ensure it will be effective off the atoll.",
                ).also {
                    stage++
                }
            10 ->
                npcl(
                    "Furthermore, you will not be able to attack whilst using this, so be careful. Perhaps when I refine my spells I could look into making this possible.",
                ).also {
                    end()
                    DungeonPlanCutscene(player!!).start()
                    setQuestStage(player!!, Quests.MONKEY_MADNESS, 32)
                    stage = 14
                }

            14 -> {
                val interfaceIdentification = Components.QUEST_COMPLETE_SCROLL_277
                player!!.interfaceManager.open(Component(interfaceIdentification))
                player!!.packetDispatch.sendItemZoomOnInterface(
                    Items.MSPEAK_AMULET_4022,
                    230,
                    interfaceIdentification,
                    5,
                )

                for (i in 0..17) {
                    when (i) {
                        3 -> player!!.packetDispatch.sendString("Monkey Madness: Chapter 3", interfaceIdentification, i)
                        9 ->
                            player!!.packetDispatch.sendString(
                                "In which our ${if (player!!.isMale) "hero finds himself" else "heroine finds herlself"} contending with life as a",
                                interfaceIdentification,
                                i,
                            )
                        10 -> player!!.packetDispatch.sendString("monkey.", interfaceIdentification, i)
                        else -> player!!.packetDispatch.sendString("", interfaceIdentification, i)
                    }
                }
                stage = 99
            }

            15 -> sendItemDialogue(player!!, Items.GOLD_BAR_2357, "You hand Zooknock the gold bar.").also { stage++ }
            16 -> npc(FaceAnim.OLD_CALM_TALK1, "Nicely done.").also { stage++ }
            17 -> {
                if (validateAmuletItemsGiven() != null) {
                    npcl(FaceAnim.OLD_CALM_TALK1, validateAmuletItemsGiven()).also { stage = END_DIALOGUE }
                } else {
                    npcl(
                        FaceAnim.OLD_CALM_TALK1,
                        "Now listen closely, human. I will cast a spell to enchant this gold bar with the power contained in these monkey dentures.",
                    ).also {
                        stage =
                            40
                    }
                }
            }

            20 ->
                sendItemDialogue(
                    player!!,
                    Items.MAMULET_MOULD_4020,
                    "You hand Zooknock the monkey amulet mould.",
                ).also {
                    stage++
                }

            21 -> npc(FaceAnim.OLD_CALM_TALK1, "Good work.").also { stage++ }
            22 -> {
                if (validateAmuletItemsGiven() != null) {
                    npcl(FaceAnim.OLD_CALM_TALK1, validateAmuletItemsGiven()).also { stage = END_DIALOGUE }
                } else {
                    npcl(
                        FaceAnim.OLD_CALM_TALK1,
                        "Now listen closely, human. I will cast a spell to enchant this gold bar with the power contained in these monkey dentures.",
                    ).also {
                        stage =
                            40
                    }
                }
            }

            30 ->
                sendItemDialogue(
                    player!!,
                    Items.MONKEY_DENTURES_4006,
                    "You hand Zooknock the magical monkey dentures.",
                ).also {
                    stage++
                }

            31 -> npc(FaceAnim.OLD_CALM_TALK1, "Nicely done.").also { stage++ }
            32 -> {
                if (validateAmuletItemsGiven() != null) {
                    npcl(FaceAnim.OLD_CALM_TALK1, validateAmuletItemsGiven()).also { stage = END_DIALOGUE }
                } else {
                    npcl(
                        FaceAnim.OLD_CALM_TALK1,
                        "Now listen closely, human. I will cast a spell to enchant this gold bar with the power contained in these monkey dentures.",
                    ).also {
                        stage =
                            40
                    }
                }
            }

            40 ->
                npcl(
                    FaceAnim.OLD_CALM_TALK1,
                    "You must then smith the gold using the monkey amulet mould. However, unless you do this in a place of religious significance to the monkeys, the spirits",
                ).also {
                    stage++
                }

            41 -> npcl(FaceAnim.OLD_CALM_TALK2, "contained within will likely depart.").also { stage++ }
            42 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "Where do I find a place of religious significance to monkeys?",
                ).also { stage++ }

            43 ->
                npcl(
                    FaceAnim.OLD_CALM_TALK1,
                    "Somewhere in the village. It out to be obvious. Now give me a moment.",
                ).also {
                    npc!!.graphics(Graphics(org.rs.consts.Graphics.WIND_BLAST_IMPACT_134, 96))
                    stage++
                }

            44 -> {
                addItemOrDrop(player!!, Items.ENCHANTED_BAR_4007, 1)
                addItemOrDrop(player!!, Items.MAMULET_MOULD_4020, 1)
                sendDoubleItemDialogue(
                    player!!,
                    Items.ENCHANTED_BAR_4007,
                    Items.MAMULET_MOULD_4020,
                    "Zooknock hands you back the gold bar and the monkey amulet mould.",
                ).also {
                    stage =
                        99
                }
            }

            99 -> end()
        }
    }

    private fun validateAmuletItemsGiven(): String? {
        val missingItems = mutableListOf<Item>()

        if (!getAttribute(player!!, "/save:mm:gold-given", false)) {
            missingItems.add(Item(Items.GOLD_BAR_2357))
        }
        if (!getAttribute(player!!, "/save:mm:mould-given", false)) {
            missingItems.add(Item(Items.MAMULET_MOULD_4020))
        }
        if (!getAttribute(player!!, "/save:mm:dentures-given", false)) {
            missingItems.add(Item(Items.MONKEY_DENTURES_4006))
        }

        return when {
            missingItems.isEmpty() -> null
            missingItems.size == 1 -> "We still need the ${missingItems.first().name}."
            missingItems.size == 2 -> "We still need the ${missingItems.first().name} and ${missingItems.last().name}."
            else -> {
                "We still need the ${Item(Items.GOLD_BAR_2357).name}, ${Item(Items.MAMULET_MOULD_4020).name} and the ${
                    Item(
                        Items.MONKEY_DENTURES_4006,
                    ).name
                }."
            }
        }
    }

    private fun validateTalismanItemsGiven(): String? {
        val missingItems = mutableListOf<Item>()

        if (!getAttribute(player!!, "/save:mm:bones-given", false)) {
            missingItems.add(Item(Items.MONKEY_BONES_3179))
        }
        if (!getAttribute(player!!, "/save:mm:talisman-given", false)) {
            missingItems.add(Item(Items.MONKEY_TALISMAN_4023))
        }

        return when {
            missingItems.isEmpty() -> null
            missingItems.size == 1 -> "We still need the ${missingItems.first().name}."
            else -> {
                "We still need the ${Item(
                    Items.MONKEY_BONES_3179,
                ).name} and the ${Item(Items.MONKEY_TALISMAN_4023).name}."
            }
        }
    }
}
