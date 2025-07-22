package content.region.desert.pollniveach.quest.smki_miniquest.dialogue

import content.data.GameAttributes
import core.api.getAttribute
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class CatolaxDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc(FaceAnim.AMAZED, "I sense you are becoming adept at new Slayer techniques.", "Impressive.")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> playerl(FaceAnim.FRIENDLY, "It was all thanks to your help.").also { stage++ }
            1 -> npcl(FaceAnim.FRIENDLY, "Nice of you to say so, although the cats have definitely had something to do with it. It hints at divine intervention.").also { stage++ }
            2 -> playerl(FaceAnim.NEUTRAL, "It seems I'm the subject of a few too many divine interventions for comfort. It's lucky indeed that I have my feline friend.").also { stage++ }
            3 -> npcl(FaceAnim.HALF_ASKING, "Heroes aren't the same as they were in my day. Can I help you with anything else?").also { stage++ }
            4 -> showTopics(
                Topic("Could you tell me what's going on?", 5),
                Topic("Can you tell me about the smoky dungeon?",15),
                Topic("I'd like some information on the remaining guardians.", 19),
                Topic("Are you both in your tomb and the smoky dungeon?",34),
            )
            // Could you tell me what's going on?
            5  -> npcl(FaceAnim.FRIENDLY, "The crux of the matter is that you were working for Sumona, who is the Devourer.").also { stage++ }
            6  -> playerl(FaceAnim.FRIENDLY, "I can't say I am happy to have helped her in her plans. She never struck me as a nice type.").also { stage++ }
            7  -> npcl(FaceAnim.FRIENDLY, "Oh, but you didn't help her - she expected you to die. She did promise to reward you, however, so take advantage of that.").also { stage++ }
            8  -> playerl(FaceAnim.HALF_ASKING, "What about this twin sister then? Is she a deity too?").also { stage++ }
            9  -> npcl(FaceAnim.FRIENDLY, "Twin sister? It was the Devourer too; she just wanted to watch when the banshee defeated you.").also { stage++ }
            10 -> playerl(FaceAnim.FRIENDLY, "I presume that her being a deity is going to make it awkward for me, having riled her so much.").also { stage++ }
            11 -> npcl(FaceAnim.FRIENDLY, "Her bargain isn't one she can wriggle out of easily, although sending you on Slayer tasks will be far from safe.").also { stage++ }
            12 -> npcl(FaceAnim.FRIENDLY, "You'll also find that you forget her evil deeds when talking with her, such is the power she possesses.").also { stage++ }
            13 -> playerl(FaceAnim.FRIENDLY, "The fake-sister thing explains why she burned that book and what the insectoids were doing there.").also { stage++ }
            14 -> npcl(FaceAnim.FRIENDLY, "Indeed. Thank you for avenging me. It is as much as can be expected by a dead mortal who wishes revenge on an immortal deity.").also { stage = 4 }
            // Can you tell me about the smoky dungeon?
            15 -> npcl(FaceAnim.FRIENDLY, "You will, of course, be aware that the place is a hive of Slayer creatures. Each of these must be combated within the smoke you mention.").also { stage++ }
            16 -> playerl(FaceAnim.HALF_ASKING, "There are no new dangers I should be aware of?").also { stage++ }
            17 -> npcl(FaceAnim.FRIENDLY, "There are four guardians - turoth, kurask, cave crawler and basilisk - that must be slain in order to access the four new areas within the underground passages.").also { stage++ }
            18 -> npcl(FaceAnim.HALF_ASKING, "No problem for you, I would suspect, as you were able to easily deal with the banshee mistress. Is there anything more I can help with?").also { stage = 4 }
            // I'd like some information on the remaining guardians.
            19 -> if(getAttribute(player, GameAttributes.MINIQUEST_SMKI_GUARDIANS, false)) {
                npcl(FaceAnim.THINKING, "You seem to have killed all of these creatures already. I'm not sure what more you need to know.")
            } else {
                val askAbout = "I'd like to know about the"
                showTopics(
                    Topic("$askAbout mightiest turoth.", 20),
                    Topic("$askAbout kurask overlord.", 24),
                    Topic("$askAbout monstrous cave crawler.", 28),
                    Topic("$askAbout basilisk boss.", 31),
                    Topic("I'm not after anything in fact.", 33)
                )
            }
            // Mightiest turoth.
            20 -> npcl(FaceAnim.FRIENDLY, "From what I've heard, the mightiest turoth can summon its offspring to aid it.").also { stage++ }
            21 -> npcl(FaceAnim.FRIENDLY, "These are smaller and weaker than the mightiest turoth but they are essentially unlimited in number.").also { stage++ }
            22 -> playerl(FaceAnim.HALF_THINKING, "How can I deal with such an onslaught?").also { stage++ }
            23 -> npcl(FaceAnim.FRIENDLY,"I'd guess that killing the leader first would be wise. If you like infinite carnage, however, then slaughtering the offspring might be pleasant for a few moments.").also { stage = 19 }
            // Kurask overlord.
            24 -> npcl(FaceAnim.FRIENDLY, "As far as I can divine, the kurask overlord in the caves can summon its cohorts to aid it.").also { stage++ }
            25 -> npcl(FaceAnim.FRIENDLY, "These are almost the equal of the creature itself.").also { stage++ }
            26 -> playerl(FaceAnim.HALF_THINKING, "What would you suggest?").also { stage++ }
            27 -> npcl(FaceAnim.FRIENDLY, "I'd guess that killing the overlord as quickly as you can would be the plan. They are bulky creatures, however, so you may be able to use this to your advantage.").also { stage = 19 }
            // Monstrous cave crawler.
            28 -> npcl(FaceAnim.FRIENDLY, "The cave crawlers are a venomous bunch and their guardian even more so.").also { stage++ }
            29 -> npcl(FaceAnim.FRIENDLY, "Adventurers who have seen it and lived have told of its power to poison, even if an opponent has temporary immunity.").also { stage++ }
            30 -> npcl(FaceAnim.FRIENDLY, "I certainly would not look at it as a feeble pushover. Take loads of some poison cure.").also { stage = 19 }
            // Basilisk boss.
            31 -> npcl(FaceAnim.FRIENDLY, "The basilisk boss is similar to most of its kind, but with a fiercer glare.").also { stage++ }
            32 -> npcl(FaceAnim.NEUTRAL, "Whatever draining effects a normal basilisk unleashes, you can expect the basilisk boss to unleash with more power.").also { stage = 19 }
            33 -> end()
            // Are you both in your tomb and the smoky dungeon?
            34 -> npcl(FaceAnim.FRIENDLY, "I have a spiritual presence in both places associated with my death - my tomb and the place of my murder.").also { stage++ }
            35 -> playerl(FaceAnim.FRIENDLY, "That must be useful. I wish I could be in two places at once.").also { stage++ }
            36 -> npcl(FaceAnim.FRIENDLY, "I don't think that my position is altogether enviable. After all, I'm dead and can only ever exist in two places.").also { stage++ }
            37 -> npcl(FaceAnim.FRIENDLY, "You are alive and can go anywhere your legs take you.").also { stage = 33 }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = CatolaxDialogue(player)


    override fun getIds(): IntArray {
        return intArrayOf(
            NPCs.CATOLAX_7782,
            /*
            NPCs.CATOLAX_7783
            NPCs.CATOLAX_7784
            */
        )
    }
}