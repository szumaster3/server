package content.region.kandarin.dialogue.ardougne

import core.api.getStatLevel
import core.api.quest.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class CharlieDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (isQuestComplete(player, Quests.EAGLES_PEAK)) {
            playerl(FaceAnim.HALF_ASKING, "Hi, how are you getting on with that ferret?").also { stage = 25 }
        } else {
            playerl(FaceAnim.NEUTRAL, "Hi!")
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Yes, er, welcome to the zoo! I'm afraid I'm kind of busy right now; what was it you wanted?",
                ).also {
                    stage++
                }
            1 ->
                options(
                    "Ah, you sound like someone who needs a quest doing!",
                    "Where did you get the animals from?",
                ).also {
                    stage++
                }
            2 ->
                when (buttonId) {
                    1 ->
                        playerl(
                            FaceAnim.FRIENDLY,
                            "Ah, you sound like someone who needs a quest doing!",
                        ).also { stage++ }
                    2 -> playerl(FaceAnim.FRIENDLY, "Where did you get the animals from?").also { stage = 21 }
                }
            3 ->
                if (getStatLevel(player, Skills.HUNTER) < 27) {
                    npcl(FaceAnim.FRIENDLY, "Well... maybe. How much do you know about hunting?").also { stage = 9 }
                } else {
                    npcl(
                        FaceAnim.FRIENDLY,
                        "Actually, now that you come to mention it, you might be able to help me.",
                    ).also {
                        stage =
                            11
                    }
                }
            9 -> playerl(FaceAnim.FRIENDLY, "Well I know a little bit...").also { stage++ }
            10 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "No, I could do with some help, but I'm afraid I need somebody with a little more experience.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            11 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "A few weeks ago we had a delivery of a northern ferret. They're funny little creatures. Turns out they're tricky little blighters too.",
                ).also {
                    stage++
                }
            12 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Whilst we were unloading him from the cart he managed to escape his cage. What's more he gave the driver a nasty nip on the way out too.",
                ).also {
                    stage++
                }
            13 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "This was obviously a bit of a blow, you can't make a new attraction without any animals.",
                ).also {
                    stage++
                }
            14 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "We got in contact with one of our associates, a huntsman called Nickolaus, who agreed to capture us another.",
                ).also {
                    stage++
                }
            15 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Funny thing is we haven't heard from him for some time now. I don't suppose you'd mind having a look for him?",
                ).also {
                    stage++
                }
            16 ->
                options(
                    "Sure. Any idea where I should start looking?",
                    "Sorry, I don't have the time.",
                ).also { stage++ }
            17 ->
                when (buttonId) {
                    1 ->
                        playerl(
                            FaceAnim.HALF_ASKING,
                            "Sure. Any idea where I should start looking?",
                        ).also { stage = 18 }
                    2 -> playerl(FaceAnim.FRIENDLY, "Sorry, I don't have the time.").also { stage = 20 }
                }
            18 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Well the northern ferret is mostly found around the mountains just west of the Gnome Stronghold, so that would probably be as good a place as any to start looking.",
                ).also {
                    stage++
                }
            19 -> playerl(FaceAnim.FRIENDLY, "Okay, thanks.").also { stage = END_DIALOGUE }
            20 ->
                npcl(FaceAnim.FRIENDLY, "Oh well, I'm sure he'll be okay out there on his own.").also {
                    stage =
                        END_DIALOGUE
                }
            21 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "We get them from all over the place! The most exotic creatures have been brought back by explorers and sold to us.",
                ).also {
                    stage++
                }
            22 ->
                playerl(
                    FaceAnim.HALF_ASKING,
                    "Where on Gielinor did you get that scary looking Cyclops?!",
                ).also { stage++ }
            23 -> npcl(FaceAnim.LAUGH, "Yes he is scary looking!").also { stage++ }
            24 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "He's from a very far away land, we couldn't find out more as the explorer who brought him back died shortly afterwards!",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            25 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Well he seems to be just as vicious as the one we lost, but we're still very grateful for your help in acquiring him.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return CharlieDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.CHARLIE_5138)
    }
}
