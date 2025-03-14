package content.region.misthalin.quest.anma.dialogue

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

class AliceDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private var quest: Quest? = null

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        quest = player.getQuestRepository().getQuest(Quests.ANIMAL_MAGNETISM)
        when (quest!!.getStage(player)) {
            10, 11, 12, 13, 14, 15, 16, 17, 18 ->
                options(
                    "What are you selling?",
                    "I'm okay, thank you.",
                    "I'm here about a quest.",
                )

            else -> options("What are you selling?", "I'm okay, thank you.")
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (quest!!.getStage(player)) {
            10, 11, 12, 13, 14, 15, 16, 17, 18 ->
                when (stage) {
                    0 ->
                        when (buttonId) {
                            1 -> end().also { openNpcShop(player, npc.id) }
                            2 -> player("I'm okay, thank you.").also { stage++ }
                            3 -> player("I'm here about a quest.").also { stage++ }
                        }

                    1 -> end()
                    else -> handleQuest(buttonId)
                }

            else ->
                when (stage) {
                    0 ->
                        when (buttonId) {
                            1 -> end().also { openNpcShop(player, npc.id) }
                            2 -> player("I'm okay, thank you.").also { stage = END_DIALOGUE }
                        }

                    1 -> end()
                }
        }
        return true
    }

    private fun handleQuest(buttonId: Int) {
        when (quest!!.getStage(player)) {
            10 ->
                when (stage) {
                    2 ->
                        player(
                            "I am after one of your, er, unhealthier poultry. Could",
                            "you help me?",
                        ).also { stage++ }
                    3 ->
                        npc(
                            "You need those useless, undead chickens? How odd you",
                            "adventurers are.",
                        ).also { stage++ }
                    4 -> npc("You need to talk to my husband though - not that I", "can these days.").also { stage++ }
                    5 -> player("Why ever would this be?").also { stage++ }
                    6 -> npc("Can't you see, he is dead. I can't talk to the dead.").also { stage = END_DIALOGUE }
                }

            11 ->
                when (stage) {
                    2 ->
                        player(
                            "I have a message from your husband. He wants you to",
                            "know that he still loves you, despite his ghostly state.",
                        ).also { stage++ }

                    3 ->
                        npc(
                            "The curse of undeath was so cruel; all the men out",
                            "here succumbed, but Lyra and I were left alive.",
                        ).also { stage++ }

                    4 -> npc("Ever since that day, I've not been able to speak to him.").also { stage++ }
                    5 ->
                        npc(
                            "Tell him I love him but I can't find our savings. I",
                            "know he had our collection of gold and 'prize cow'",
                            "rosettes just before the curse struck.",
                        ).also { stage++ }

                    6 -> player("I'll have a word with him then; magic has its uses I", "suppose.").also { stage++ }
                    7 -> {
                        quest!!.setStage(player, 12)
                        end()
                    }
                }

            12 ->
                when (stage) {
                    2 -> npc("Have you spoken to my husband yet?").also { stage++ }
                    3 -> player("I'm working on it.").also { stage = END_DIALOGUE }
                }

            13 ->
                when (stage) {
                    2 -> player("Your husband say he put the cash in the bank.").also { stage++ }
                    3 -> npc("I'll need his bank pass, in that case.").also { stage++ }
                    4 ->
                        player(
                            "Can't you just take a ghostspeak amulet? Then you",
                            "could talk to him directly?",
                        ).also { stage++ }

                    5 ->
                        npc(
                            "I tried that once, but all those other ghosts - and even",
                            "the undead chickens and cows - scared me so much. I",
                            "wouldn't try it again for all the cash in Varrock bank.",
                        ).also { stage++ }

                    6 -> {
                        quest!!.setStage(player, 14)
                        end()
                    }
                }

            14 ->
                when (stage) {
                    2 -> npc("Have you asked him about the bank pass?").also { stage++ }
                    3 -> player("Not yet.").also { stage = END_DIALOGUE }
                }

            15 ->
                when (stage) {
                    2 -> player("He says he won't trust me with the bank pass.").also { stage++ }
                    3 ->
                        player(
                            "What if I gave some sort of altered ghostspeak amulet",
                            "to him - surely that would work?",
                        ).also { stage++ }

                    4 ->
                        npc(
                            "You're so clever; I've overheard passing adventurers",
                            "say that there's some witch near here who changes",
                            "ghostspeak amulets.",
                        ).also { stage++ }

                    5 ->
                        npc(
                            "I think she lives a bit west of that mad Professor",
                            "Frenksomething, past the Farming patch.",
                        ).also { stage++ }

                    6 ->
                        player(
                            "I'll see if I can find her. Big nose and a monstrous hat",
                            "I assume? I wonder where the beautiful young witches",
                            "hide...",
                        ).also { stage++ }

                    7 ->
                        npc(
                            "Mysterious indeed, but in this case she actually looks",
                            "pretty normal.",
                        ).also { stage++ }
                    8 -> {
                        quest!!.setStage(player, 16)
                        end()
                    }
                }

            16, 17 ->
                when (stage) {
                    2 -> npc("Have you found a way for me to talk to my husband", "yet?").also { stage++ }
                    3 -> player("I've not progressed at all, I'm afraid.").also { stage = END_DIALOGUE }
                    4 -> end()
                }

            18 ->
                when (stage) {
                    2 -> npc("Have you handed him an enhanced amulet?").also { stage++ }
                    3 ->
                        player(
                            "I Have obtained the amulet; I just haven't handed it",
                            "over yet. So, it's looking good!",
                        ).also { stage = END_DIALOGUE }
                }
        }
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ALICE_2307)
    }
}
