package content.region.kandarin.quest.swept.handlers

import content.data.GameAttributes
import core.api.*
import core.api.quest.getQuestStage
import core.api.quest.isQuestComplete
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.emote.Emotes
import core.game.world.map.Location
import core.tools.END_DIALOGUE
import core.tools.RandomFunction
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.rs.consts.*

class SweptAwayListener :
    InteractionListener,
    InterfaceListener {
    init {
        addScenery(39363, Location.create(3294, 4515, 0), 2, 22)
        addScenery(39364, Location.create(3295, 4515, 0), 2, 22)
        addScenery(39365, Location.create(3296, 4515, 0), 2, 22)
        addScenery(39366, Location.create(3297, 4515, 0), 4, 22)

        addScenery(39367, Location.create(3298, 4515, 0), 2, 22)
        addScenery(39368, Location.create(3299, 4515, 0), 2, 22)
        addScenery(39369, Location.create(3300, 4515, 0), 2, 22)
        addScenery(39370, Location.create(3301, 4515, 0), 4, 22)

        addScenery(39371, Location.create(3293, 4514, 0), 3, 22)
        addScenery(39372, Location.create(3292, 4514, 0), 1, 22)
        addScenery(39373, Location.create(3292, 4513, 0), 1, 22)

        addScenery(39374, Location.create(3294, 4514, 0), 2, 22)
        addScenery(39375, Location.create(3295, 4514, 0), 1, 22)
        addScenery(39376, Location.create(3295, 4513, 0), 4, 22)

        addScenery(39377, Location.create(3297, 4514, 0), 3, 22)
        addScenery(39378, Location.create(3296, 4514, 0), 1, 22)
        addScenery(39379, Location.create(3296, 4513, 0), 1, 22)

        addScenery(39380, Location.create(3298, 4514, 0), 2, 22)
        addScenery(39381, Location.create(3299, 4514, 0), 1, 22)
        addScenery(39382, Location.create(3299, 4513, 0), 4, 22)

        addScenery(39383, Location.create(3301, 4514, 0), 3, 22)
        addScenery(39384, Location.create(3300, 4514, 0), 1, 22)
        addScenery(39385, Location.create(3300, 4513, 0), 1, 22)

        addScenery(39386, Location.create(3292, 4512, 0), 2, 22)
        addScenery(39387, Location.create(3293, 4512, 0), 2, 22)
        addScenery(39388, Location.create(3294, 4512, 0), 2, 22)
        addScenery(39389, Location.create(3295, 4512, 0), 4, 22)

        addScenery(39390, Location.create(3296, 4512, 0), 2, 22)
        addScenery(39391, Location.create(3297, 4512, 0), 2, 22)
        addScenery(39392, Location.create(3298, 4512, 0), 2, 22)
        addScenery(39393, Location.create(3299, 4512, 0), 4, 22)

        addScenery(39394, Location.create(3291, 4511, 0), 3, 22)
        addScenery(39395, Location.create(3290, 4511, 0), 1, 22)
        addScenery(39396, Location.create(3290, 4510, 0), 1, 22)

        addScenery(39397, Location.create(3292, 4511, 0), 2, 22)
        addScenery(39398, Location.create(3293, 4511, 0), 1, 22)
        addScenery(39399, Location.create(3293, 4510, 0), 4, 22)

        addScenery(39400, Location.create(3295, 4511, 0), 3, 22)
        addScenery(39401, Location.create(3294, 4511, 0), 1, 22)
        addScenery(39402, Location.create(3294, 4510, 0), 1, 22)

        addScenery(39403, Location.create(3296, 4511, 0), 2, 22)
        addScenery(39404, Location.create(3297, 4511, 0), 1, 22)
        addScenery(39405, Location.create(3297, 4510, 0), 4, 22)

        addScenery(39406, Location.create(3299, 4511, 0), 3, 22)
        addScenery(39407, Location.create(3298, 4511, 0), 1, 22)
        addScenery(39408, Location.create(3298, 4510, 0), 1, 22)

        addScenery(39409, Location.create(3290, 4509, 0), 2, 22)
        addScenery(39410, Location.create(3291, 4509, 0), 2, 22)
        addScenery(39411, Location.create(3292, 4509, 0), 2, 22)
        addScenery(39412, Location.create(3293, 4509, 0), 4, 22)

        addScenery(39413, Location.create(3294, 4509, 0), 2, 22)
        addScenery(39414, Location.create(3295, 4509, 0), 2, 22)
        addScenery(39415, Location.create(3296, 4509, 0), 2, 22)
        addScenery(39416, Location.create(3297, 4509, 0), 4, 22)
    }

    override fun defineListeners() {
        /*
         * Handles opening the Betty's trapdoor.
         */

        on(Scenery.TRAPDOOR_39274, IntType.SCENERY, "open") { player, _ ->
            if (!isQuestComplete(player, Quests.SWEPT_AWAY)) {
                setVarbit(player, SweptUtils.VARBIT_BETTY_TRAPDOOR, 1)
            } else {
                sendMessage(player, "It's locked.")
            }

            return@on true
        }

        /*
         * Handles closing the Betty's trapdoor.
         */

        on(Scenery.TRAPDOOR_39275, IntType.SCENERY, "close") { player, _ ->
            setVarbit(player, SweptUtils.VARBIT_BETTY_TRAPDOOR, 0)
            return@on true
        }

        /*
         * Handles opening the Rimmington trapdoor if the quest is incomplete.
         */

        on(Scenery.TRAPDOOR_39330, IntType.SCENERY, "open") { player, _ ->
            if (!isQuestComplete(player, Quests.SWEPT_AWAY)) {
                setVarbit(player, SweptUtils.VARBIT_RIMMINGTON_TRAPDOOR, 1)
            } else {
                sendMessage(player, "It's locked.")
            }

            return@on true
        }

        /*
         * Handles closing the Rimmington trapdoor.
         */

        on(Scenery.TRAPDOOR_39331, IntType.SCENERY, "close") { player, _ ->
            setVarbit(player, SweptUtils.VARBIT_RIMMINGTON_TRAPDOOR, 0)
            return@on true
        }

        /*
         * Handles talking to NPC Aggie.
         */

        on(NPCs.AGGIE_8207, IntType.NPC, "talk-to") { player, node ->
            player.dialogueInterpreter.open(NPCs.AGGIE_8207, node.id)
            return@on true
        }

        /*
         * Handles talking to NPC Norman.
         */

        on(NPCs.NORMAN_8203, IntType.NPC, "talk-to") { player, _ ->
            sendNPCDialogue(player, NPCs.NORMAN_8203, "Moo-ooooooooooo?", FaceAnim.CHILD_NORMAL)
            addDialogueAction(player) { p, button ->
                if (button > 0) {
                    sendNPCDialogue(
                        p,
                        NPCs.MAGGIE_8078,
                        "I wouldn't pester Norman at the mo - he's a bit on edge, what with Babe being poorly. The sneezing really gets to him!",
                    )
                }
            }
            return@on true
        }

        /*
         * Handles talking to NPC Babe.
         */

        on(NPCs.BABE_8204, IntType.NPC, "talk-to") { player, _ ->
            sendNPCDialogue(player, NPCs.BABE_8204, "*Sniff* *sniffle* *snort* Ah-choo!", FaceAnim.CHILD_NORMAL)
            addDialogueAction(player) { p, button ->
                if (button > 0) {
                    sendNPCDialogue(
                        p,
                        NPCs.MAGGIE_8078,
                        "I wouldn't get too close to her if I were you. I gave her a portion of that goulash, so she's on the mend, but she still might be a bit contagious.",
                    )
                }
            }
            return@on true
        }

        /*
         * Handles stirring the cauldron at Maggie's camp.
         */

        on(Scenery.MAGGIE_S_CAULDRON_39230, IntType.SCENERY, "stir") { player, _ ->
            lock(player, 1)
            if (getQuestStage(player, Quests.SWEPT_AWAY) >= 4) {
                animate(player, SweptUtils.STIR_WITH_BROOMSTICK_ANIMATION)
                sendPlayerDialogue(player, "There, that should do it.", FaceAnim.NEUTRAL)
                return@on true
            }

            animate(player, SweptUtils.STIR_ANIMATION)

            sendPlayerDialogue(player, "What was that?", FaceAnim.EXTREMELY_SHOCKED)
            addDialogueAction(player) { p, button ->
                if (button > 1) {
                    openDialogue(p, MaggieSceneryInteraction())
                }
            }
            return@on true
        }

        val sweepLines = hashMapOf<Int, (Player) -> Unit>(
            Scenery.LINE_39364 to SweptUtils::sweepFirstLines,
            Scenery.LINE_39377 to SweptUtils::sweepSecondLines,
            Scenery.LINE_39408 to SweptUtils::sweepThirdLines,
            Scenery.LINE_39414 to SweptUtils::sweepFourthLines
        )

        /*
         * Handles sweeping the lines of the area during the quest.
         */

        sweepLines.forEach { (lineId, sweepAction) ->
            on(lineId, IntType.SCENERY, "sweep-away") { player, _ ->
                sweepAction(player)
                return@on true
            }
        }

        /*
         * Handles extracting a creature from Gus's crates.
         */

        on(SweptUtils.GUS_CRATES, IntType.SCENERY, "extract-creature") { player, node ->
            val rand = RandomFunction.random(0, 1)
            if (node.id == SweptUtils.N_CRATE) {
                animate(player, SweptUtils.TAKE_NEWT_FROM_CRATE_ANIMATION)
                sendMessage(player, "You pull out a newt. It wriggles out of your hand and back into the crate.")
                sendPlayerDialogue(player, "Bleh! A newt!", FaceAnim.EXTREMELY_SHOCKED)
                return@on true
            }
            if (node.id == SweptUtils.T_CRATE) {
                animate(player, SweptUtils.TAKE_FROG_FROM_CRATE_ANIMATION)
                sendMessage(player, "You pull out a toad. It wriggles out of your hand and back into the crate.")
                sendPlayerDialogue(player, "Yuk! A toad!", FaceAnim.EXTREMELY_SHOCKED)
                return@on true
            }

            if (node.id == SweptUtils.NT_CRATE) {
                when (rand) {
                    0 -> {
                        animate(player, SweptUtils.TAKE_FROG_FROM_CRATE_ANIMATION)
                        sendMessage(
                            player,
                            "You pull out a toad. It wriggles out of your hand and back into the crate.",
                        )
                        sendPlayerDialogue(player, "Yuk! A toad!", FaceAnim.EXTREMELY_SHOCKED)
                    }

                    else -> {
                        animate(player, SweptUtils.TAKE_FROG_FROM_CRATE_ANIMATION)
                        sendMessage(
                            player,
                            "You pull out a toad. It wriggles out of your hand and back into the crate.",
                        )
                        sendPlayerDialogue(player, "Yuk! A toad!", FaceAnim.EXTREMELY_SHOCKED)
                    }
                }
            }
            return@on true
        }

        /*
         * Handles extracting a creature from Gus's labelled crates.
         */

        on(SweptUtils.GUS_CRATES_LABELLED, IntType.SCENERY, "extract-creature") { player, _ ->
            if (!getAttribute(player, GameAttributes.QUEST_SWEPT_AWAY_LABELS_COMPLETE, false)) {
                player.dialogueInterpreter.sendDialogues(
                    player,
                    NPCs.GUS_8205,
                    "No! remember what I said: we can't waste the stock.",
                    "One look should be enough. We wouldn't want to get",
                    "Ms. Hetty Annoyed.",
                )
                return@on true
            }

            animate(player, SweptUtils.TAKE_NEWT_FROM_CRATE_ANIMATION)
            sendMessage(player, "You fish around in the crate and extract a newt.")
            addItem(player, Items.NEWT_14064)
            return@on true
        }

        /*
         * Handles removing the label from Gus's crates based on the crate type.
         */

        on(SweptUtils.GUS_CRATES_LABELLED, IntType.SCENERY, "remove-label") { player, node ->
            when (node.id) {
                SweptUtils.GUS_CRATES[0] -> setVarbit(player, SweptUtils.VARBIT_TOAD_CRATE_LABEL, 0, true)
                SweptUtils.GUS_CRATES[1] -> setVarbit(player, SweptUtils.VARBIT_NEWT_CRATE_LABEL, 0, true)
                SweptUtils.GUS_CRATES[2] -> setVarbit(player, SweptUtils.VARBIT_NEWT_AND_TOAD_CRATE_LABEL, 0, true)
            }
            return@on true
        }

        /*
         * Handles labelling the crate with Newts and Toads when used with a label.
         */
        onUseWith(IntType.SCENERY, Items.NEWTS_AND_TOADS_LABEL_14067, SweptUtils.NT_CRATE) { player, used, _ ->
            SweptUtils.handleCrateLabelling(player, used.id, SweptUtils.VARBIT_NEWT_AND_TOAD_CRATE_LABEL, 3)
            return@onUseWith true
        }

        /*
         * Handles labelling the crate with a Toad when used with a Toad label.
         */

        onUseWith(IntType.SCENERY, Items.TOAD_LABEL_14066, SweptUtils.T_CRATE) { player, used, _ ->
            SweptUtils.handleCrateLabelling(player, used.id, SweptUtils.VARBIT_TOAD_CRATE_LABEL, 2)
            return@onUseWith true
        }

        /*
         * Handles labelling the crate with a Newt when used with a Newt label.
         */

        onUseWith(IntType.SCENERY, Items.NEWT_LABEL_14065, SweptUtils.N_CRATE) { player, used, _ ->
            SweptUtils.handleCrateLabelling(player, used.id, SweptUtils.VARBIT_NEWT_CRATE_LABEL, 1)
            SweptUtils.checkGusTask(player)
            return@onUseWith true
        }

        /*
         * Handles viewing the magic slate. Opens the pen table interface.
         */

        on(Items.MAGIC_SLATE_14069, IntType.ITEM, "view") { player, _ ->
            openInterface(player, SweptUtils.PEN_TABLE_INTERFACE)
            return@on true
        }

        /*
         * Handles opening the lottie chest.
         */

        on(Scenery.CHEST_39278, IntType.SCENERY, "open") { player, _ ->
            setVarbit(player, SweptUtils.VARBIT_LOTTIE_CHEST, 1)
            return@on true
        }

        /*
         * Handles searching the chest for Betty's wand.
         */

        on(Scenery.CHEST_39279, IntType.SCENERY, "search") { player, _ ->
            if (!getAttribute(player, GameAttributes.QUEST_SWEPT_AWAY_BETTY_WAND, false)) {
                sendMessages(player, "You search the chest and find Betty's wand.")
                addItemOrDrop(player, Items.BETTYS_WAND_14068)
                setAttribute(player, GameAttributes.QUEST_SWEPT_AWAY_BETTY_WAND, true)
            } else {
                sendMessage(player, "You search the chest but find nothing.")
            }
            return@on true
        }

        /*
         * Handles closing the chest.
         */

        on(Scenery.CHEST_39279, IntType.SCENERY, "close") { player, _ ->
            setVarbit(player, SweptUtils.VARBIT_LOTTIE_CHEST, 0)
            return@on true
        }

        /*
         * Handles moving the creature in the pen.
         */

        on(SweptUtils.CREATURE_PEN, IntType.SCENERY, "Move-creature") { player, node ->
            SweptUtils.handlePenInteraction(player, node.id)
            val interactions = getAttribute(player, GameAttributes.QUEST_SWEPT_AWAY_CREATURE_INTER, -1)

            // Authentically, this interaction with the messages occurs after
            // 4 minutes have passed, and then each subsequent interaction triggers a reset.
            if (interactions > 16) {
                if (anyInInventory(player, *SweptUtils.CREATURE_PEN_ITEM)) {
                    for (creatureItem in SweptUtils.CREATURE_PEN_ITEM) {
                        removeItem(player, creatureItem)
                        sendMessages(
                            player,
                            "The creature laps out of your grasp and scampers away.",
                            "You can't take a creature into a chamber that has another creature inside.",
                        )
                        sendPlayerDialogue(
                            player,
                            "I don't think I should take this little guy into a room that has another creature in it.",
                        )
                        removeAttribute(player, GameAttributes.QUEST_SWEPT_AWAY_CREATURE_INTER)
                    }

                    openDialogue(player, LottieSupportDialogue())
                }
            }
            return@on true
        }

        /*
         * Handles using a creatures with the pen.
         */

        onUseWith(IntType.SCENERY, SweptUtils.CREATURE_PEN_ITEM, *SweptUtils.CREATURE_PEN) { player, used, with ->
            SweptUtils.handlePenInteraction(player, with.id)
            return@onUseWith true
        }
    }

    override fun defineInterfaceListeners() {
        on(SweptUtils.PEN_TABLE_INTERFACE) { player, _, _, buttonID, _, _ ->
            if (buttonID == 4) closeInterface(player)
            return@on true
        }
    }

    /*
     * Handles interactions with the Maggie scenery during the quest.
     * Unlocks Halloween emotes for the player as they progress through stages.
     */

    class MaggieSceneryInteraction : DialogueFile() {
        override fun handle(
            componentID: Int,
            buttonID: Int,
        ) {
            val emotes =
                listOf(
                    Pair(Emotes.TRICK, "Trick"),
                    Pair(Emotes.SCARED, "Terrified"),
                    Pair(Emotes.ZOMBIE_HAND, "Zombie hand"),
                )

            if (stage < emotes.size) {
                val (emote, emoteName) = emotes[stage]
                if (!player!!.emoteManager.isUnlocked(emote)) {
                    SweptUtils.unlockHalloweenEmotes(player!!, emote, emoteName)
                    stage++
                } else {
                    stage++
                }

                if (stage > emotes.size) {
                    closeInterface(player!!)
                }
            } else {
                closeInterface(player!!)
            }
        }
    }

    /*
     * Handles restart task at the basement with Lottie.
     */

    class LottieSupportDialogue : DialogueFile() {
        override fun handle(
            componentID: Int,
            buttonID: Int,
        ) {
            npc = NPC(NPCs.LOTTIE_8206)
            when (stage) {
                0 ->
                    npc(
                        FaceAnim.SAD,
                        "Eh? Something looks very wrong. Not all the creatures",
                        "are there!",
                    ).also { stage++ }
                1 ->
                    npc(
                        FaceAnim.SAD,
                        "I'll just put things back to how they were and have a",
                        "look for the missing critters.",
                    ).also {
                        stage++
                    }
                2 -> {
                    GlobalScope.launch {
                        openInterface(player!!, Components.FADE_TO_BLACK_120)
                        delay(4000)
                        openInterface(player!!, Components.FADE_FROM_BLACK_170)
                        SweptUtils.resetBettyBasementNPCs()
                        if (anyInInventory(player!!, *SweptUtils.CREATURE_PEN_ITEM)) {
                            for (creatureItem in SweptUtils.CREATURE_PEN_ITEM) {
                                removeItem(player!!, creatureItem)
                            }
                        }
                    }
                }
                3 ->
                    npc(
                        FaceAnim.HAPPY,
                        "Not to worry - I found everyone! All the critters are",
                        "accounted for you're fine to start over.",
                    ).also {
                        stage++
                    }
            }
        }
    }

    /*
     * Handles interactions with Gus for the creature pen task.
     */

    class GusSupportDialogue : DialogueFile() {
        override fun handle(
            componentID: Int,
            buttonID: Int,
        ) {
            npc = NPC(NPCs.GUS_8205)
            when (stage) {
                0 ->
                    npc(
                        "I see you've put new labels on all the crates. Oh, newts!",
                        "This is so confusing, but I think something is wrong",
                        "with the labelling.",
                    ).also { stage++ }

                1 ->
                    npc(
                        "Please can you give it another go? Do you want me to",
                        "put everything back to how it was for you?",
                    ).also { stage++ }

                2 ->
                    options(
                        "Yes, please.",
                        "No, I think I can solve this.",
                        "What's wrong with the way I did it?",
                        "But, Gus...",
                    )

                3 ->
                    when (buttonID) {
                        1 -> player("Yes, please.").also { stage++ }
                        2 -> player("No, I think I can solve this.").also { stage = END_DIALOGUE }
                        3 -> player("What's wrong with the way I did it?").also { stage = END_DIALOGUE }
                        4 -> player("But, Gus...").also { stage = END_DIALOGUE }
                    }

                4 -> {
                    end()
                    SweptUtils.resetGusTask(player!!)
                }
            }
        }
    }
}
