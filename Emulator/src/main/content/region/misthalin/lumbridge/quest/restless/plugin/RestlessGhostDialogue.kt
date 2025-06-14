package content.region.misthalin.lumbridge.quest.restless.plugin

import core.api.inEquipment
import core.api.inInventory
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class RestlessGhostDialogue(player: Player? = null) : Dialogue(player) {
    
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.HALF_GUILTY, "Hello ghost, how are you?")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                if (!inEquipment(player, Items.GHOSTSPEAK_AMULET_552, 1)) {
                    npc(FaceAnim.HALF_GUILTY, "Wooo wooo wooooo!")
                    stage = 1
                    return true
                }
                if (getQuestStage(player, Quests.THE_RESTLESS_GHOST) == 20) {
                    npc(FaceAnim.HALF_GUILTY, "Not very good actually.")
                    stage = 18
                    return true
                }
                if (getQuestStage(player, Quests.THE_RESTLESS_GHOST) == 30) {
                    npc(FaceAnim.HALF_GUILTY, "How are you doing finding my skull?")
                    stage = 32
                    return true
                }
                if (getQuestStage(player, Quests.THE_RESTLESS_GHOST) == 40) {
                    npc(FaceAnim.HALF_GUILTY, "How are you doing finding my skull?").also { stage = 35 }
                } else {
                    npc("Fine, thanks.").also { stage = END_DIALOGUE }
                }
            }

            1 ->
                options(
                    "Sorry, I don't speak ghost.",
                    "Ooh... THAT'S interesting.",
                    "Any hints where I can find some treasure?",
                ).also { stage++ }

            2 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_GUILTY, "Sorry, I don't speak ghost.").also { stage++ }
                    2 -> player(FaceAnim.HALF_GUILTY, "Ooh... THAT'S interesting.").also { stage = 7 }
                    3 -> player(FaceAnim.HALF_GUILTY, "Any hints where I can find some treasure?").also { stage = 15 }
                }

            3 -> npc(FaceAnim.HALF_GUILTY, "Woo woo?").also { stage++ }
            4 -> player(FaceAnim.HALF_GUILTY, "Nope, still don't understand you.").also { stage++ }
            5 -> npc(FaceAnim.HALF_GUILTY, "WOOOOOOOOO!").also { stage++ }
            6 -> player(FaceAnim.HALF_GUILTY, "Never mind.").also { stage = END_DIALOGUE }
            7 -> npc(FaceAnim.HALF_GUILTY, "Woo woooo. Woooooooooooooooooo!").also { stage++ }
            8 -> player(FaceAnim.HALF_GUILTY, "Did he really?").also { stage++ }
            9 -> npc(FaceAnim.HALF_GUILTY, "Woo.").also { stage++ }
            10 -> player(FaceAnim.HALF_GUILTY, "My brother had EXACTLY the same problem.").also { stage++ }
            11 -> npc(FaceAnim.HALF_GUILTY, "Woo Wooooo!").also { stage++ }
            12 -> npc(FaceAnim.HALF_GUILTY, "Wooooo Woo woo woo!").also { stage++ }
            13 -> player(FaceAnim.HALF_GUILTY, "Goodbye. Thanks for the chat.").also { stage++ }
            14 -> npc(FaceAnim.HALF_GUILTY, "Wooo wooo?").also { stage = END_DIALOGUE }
            15 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Wooooooo woo! Wooooo woo wooooo woowoowoo wooo",
                    "Woo woooo. Wooooo woo woo? Wooooooooooooooooooo!",
                ).also { stage++ }

            16 -> player(FaceAnim.HALF_GUILTY, "Sorry, I don't speak ghost.").also { stage++ }
            17 -> npc(FaceAnim.HALF_GUILTY, "Woo woo?").also { stage = 4 }
            18 -> player(FaceAnim.HALF_GUILTY, "What's the problem then?").also { stage++ }
            19 -> npc(FaceAnim.HALF_GUILTY, "Did you just understand what I said???").also { stage++ }
            20 -> player(FaceAnim.HALF_GUILTY, "Yep, now tell me what the problem is.").also { stage++ }
            21 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "WOW! This is INCREDIBLE! I didn't expect anyone",
                    "to ever understand me again!",
                ).also { stage++ }

            22 -> player(FaceAnim.HALF_GUILTY, "Ok, Ok, I can understand you!").also { stage++ }
            23 ->
                player(
                    FaceAnim.HALF_GUILTY,
                    "But have you any idea WHY you're doomed to be a",
                    "ghost?",
                ).also { stage++ }

            24 -> npc(FaceAnim.HALF_GUILTY, "Well, to be honest... I'm not sure.").also { stage++ }
            25 ->
                player(
                    FaceAnim.HALF_GUILTY,
                    "I've been told a certain task may need to be completed",
                    "so you can rest in peace.",
                ).also { stage++ }

            26 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "I should think it is probably because a warlock has come",
                    "along and stolen my skull. If you look inside my coffin",
                    "there, you'll find my corpse without a head on it.",
                ).also { stage++ }

            27 -> player(FaceAnim.HALF_GUILTY, "Do you know where this warlock might be now?").also { stage++ }
            28 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "I think it was one of the warlocks who lives in the big",
                    "tower by the sea south-west from here.",
                ).also { stage++ }

            29 ->
                player(
                    FaceAnim.HALF_GUILTY,
                    "Ok. I will try and get the skull back for you, then you",
                    "can rest in peace.",
                ).also {
                    setQuestStage(player, Quests.THE_RESTLESS_GHOST, 30)
                    stage++
                }

            30 -> npc(FaceAnim.HALF_GUILTY, "Ooh, thank you. That would be such a great relief!").also { stage++ }
            31 -> npc(FaceAnim.HALF_GUILTY, "It is so dull being a ghost...").also { stage = END_DIALOGUE }
            32 -> {
                if (inInventory(player, Items.SKULL_964, 1)) {
                    end()
                } else {
                    player(FaceAnim.HALF_GUILTY, "Sorry, I can't find it at the moment.").also { stage++ }
                }
            }

            33 -> npc(FaceAnim.HALF_GUILTY, "Ah well. Keep on looking.").also { stage++ }
            34 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "I'm pretty sure it's somewhere in the tower south-west",
                    "from here. There's a lot of levels to the tower, though. I",
                    "suppose it might take a little while to find.",
                ).also { stage = END_DIALOGUE }

            35 -> player(FaceAnim.HALF_GUILTY, "I have found it!").also { stage++ }
            36 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Hurrah! Now I can stop being a ghost! You just need",
                    "to put it on my coffin there, and I will be free!",
                ).also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.RESTLESS_GHOST_457)
}
