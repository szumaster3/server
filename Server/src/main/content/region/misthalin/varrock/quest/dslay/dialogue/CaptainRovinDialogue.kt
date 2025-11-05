package content.region.misthalin.varrock.quest.dslay.dialogue

import content.region.misthalin.varrock.quest.dslay.plugin.DemonSlayerUtils
import core.api.freeSlots
import core.api.getQuestStage
import core.api.hasAnItem
import core.api.sendItemDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.IfTopic
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.tools.END_DIALOGUE
import shared.consts.NPCs
import shared.consts.Quests

class CaptainRovinDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HALF_GUILTY, "What are you doing up here? Only the palace guards", "are allowed up here.")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        val questStage = getQuestStage(player, Quests.DEMON_SLAYER)
        val hasSecondKey = hasAnItem(player, DemonSlayerUtils.SECOND_KEY.id).container != null
        when (stage) {
            0 -> showTopics(
                Topic("I am one of the palace guards.", 20),
                Topic("What about the King?", 21),
                IfTopic("Yes I know, but this is important.", 1, questStage < 100)
            )
            1 -> npc(FaceAnim.HALF_GUILTY, "Ok, I'm listening. Tell me what's so important.").also { stage++ }
            2 -> if (questStage == 20) {
                player("There's a demon who wants to invade the city.").also { stage++ }
            } else {
                player(FaceAnim.HALF_GUILTY, "Erm... I forgot.").also { stage = END_DIALOGUE }
            }
            3 -> if (!hasSecondKey) {
                npc("Is it a powerful demon?").also { stage++ }
            } else {
                npc("Yes, you said before, haven't you killed it yet?").also { stage = 22 }
            }
            4 -> player("Yes, very.").also { stage++ }
            5 -> npc("As good as the palace guards are, I don't know if", "they're up to taking on a very powerful demon.").also { stage++ }
            6 -> player("It's not them who are going to fight the demon, it's me.").also { stage++ }
            7 -> npc("What, all by yourself? How are you going to do that?").also { stage++ }
            8 -> player("I'm going to use the powerful sword Silverlight, which I", "believe you have one of the keys for?").also { stage++ }
            9 -> npc("Yes, I do. But why should I give it to you?").also { stage++ }
            10 -> player("Sir Prysin said you would give me the key.").also { stage++ }
            11 -> npc("Oh, he did, did he? Well I don't report to Sir Prysin, I", "report directly to the king!").also { stage++ }
            12 -> npc("I didn't work my way up through the ranks of the", "palace guards so I could take orders from an ill-bred", "moron who only has his job because his great-", "grandfather was a hero with a silly name!").also { stage++ }
            13 -> player("Why did he give you one of the keys then?").also { stage++ }
            14 -> npc("Only because the king ordered him to! The king", "couldn't get Sir Prysin to part with his precious", "ancestral sword, but he made him lock it up so he", "couldn't lose it.").also { stage++ }
            15 -> npc("I got one key and I think some wizard got another.", "Now what happened to the third one?").also { stage++ }
            16 -> player("Sir Prysin dropped it down a drain!").also { stage++ }
            17 -> npc("Ha ha ha! The idiot!").also { stage++ }
            18 -> npc("Okay, I'll give you the key, just so that it's you that", "kills the demon and not Sir Prysin!").also { stage++ }
            19 -> {
                end()
                if (freeSlots(player) == 0) {
                    npc("Talk to me again when you have free inventory space.")
                    return true
                }
                if (player.inventory.add(DemonSlayerUtils.SECOND_KEY)) {
                    sendItemDialogue(player, DemonSlayerUtils.SECOND_KEY, "Captain Rovin hands you a key.")
                    return true
                }
            }
            20 -> npc(FaceAnim.HALF_GUILTY, "No, you're not! I know all the palace guards.").also { stage = END_DIALOGUE }
            21 -> npc(FaceAnim.HALF_GUILTY, "Well, yes I suppose we'd let him up, He doesn't", "generally want to come up here, but if he did want to,", "he could.").also { stage = END_DIALOGUE }
            22 -> player("Well I'm going to use the powerful sword Silverlight", "which I believe you have one of the keys for?").also { stage++ }
            23 -> npc("I already gave you my key. Check your pockets.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = CaptainRovinDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.CAPTAIN_ROVIN_884)
}
