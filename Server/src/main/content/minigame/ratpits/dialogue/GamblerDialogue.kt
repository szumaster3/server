package content.minigame.ratpits.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Gambler dialogue.
 */
@Initializable
class GamblerDialogue(player: Player? = null) : Dialogue(player) {

    private var coinsAmount = 0

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if(npc.id == NPCs.GAMBLER_3006) {
            playerl(FaceAnim.HAPPY, "Good day.").also { stage = 39 }
        } else {
            playerl(FaceAnim.FRIENDLY, "Hello!")
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0  -> playerl(FaceAnim.FRIENDLY, "What are you doing here?").also { stage++ }
            1  -> npcl(FaceAnim.FRIENDLY, "Trying to win my shirt back. I lost it last night in a silly bet.").also { stage++ }
            2  -> playerl(FaceAnim.FRIENDLY, "Well I hope your luck improves. I doubt it can get any worse.").also { stage++ }
            3  -> npcl(FaceAnim.FRIENDLY, "Well I've little left to lose, maybe some more hair and my trousers.").also { stage++ }
            4  -> playerl(FaceAnim.FRIENDLY, "I really think you should try to hold on to them.").also { stage++ }
            5  -> playerl(FaceAnim.FRIENDLY, "So how do these rat pits work?").also { stage++ }
            6  -> npcl(FaceAnim.FRIENDLY, "Things work simply in the Ardougne ratpits.").also { stage++ }
            7  -> npcl(FaceAnim.FRIENDLY, "If you have a kitten and at least 100 coins you can challenge someone else who also has a kitten and some spare change.").also { stage++ }
            8  -> playerl(FaceAnim.FRIENDLY, "That sounds simple enough so far.").also { stage++ }
            9  -> npcl(FaceAnim.FRIENDLY, "You then agree on a wager. It's rumoured that some ratcatchers can talk to their own cats and give them tactics to aid them.").also { stage++ }
            10 -> playerl(FaceAnim.FRIENDLY, "So then how do you win the challenge?").also { stage++ }
            11 -> npcl(FaceAnim.FRIENDLY, "I was just getting to that. Both the gamblers' cats are placed inside of the fenced off area, and whoever kills 5 rats first wins.").also { stage++ }
            12 -> options("That sounds vile and cruel.", "It sounds a little dangerous for my cat.", "Wow that sounds like fun!", "Is there a limit to how much you can bet?", "Can I challenge you and your cat?").also { stage++ }
            13 -> when(buttonId) {
                1 -> playerl(FaceAnim.FRIENDLY, "That sounds vile and cruel.").also { stage++ }
                2 -> playerl(FaceAnim.FRIENDLY, "It sounds a little dangerous for my cat.").also { stage = 18 }
                3 -> playerl(FaceAnim.FRIENDLY, "Wow that sounds like fun!").also { stage = 26 }
                4 -> playerl(FaceAnim.FRIENDLY, "Is there a limit to how much you can bet?").also { stage = 28 }
                5 -> playerl(FaceAnim.FRIENDLY, "Can I challenge you and your cat?").also { stage = 32 }
            }
            14 -> npcl(FaceAnim.FRIENDLY, "It's a cruel world and the pits are no exception.").also { stage++ }
            15 -> npcl(FaceAnim.FRIENDLY, "Every day guards are killed for no better reason than to sate the bloodlust of some despicable adventurer.").also { stage++ }
            16 -> options("One more thing...", "Thanks for your help.").also { stage++ }
            17 -> when(buttonId) {
                1 -> options("That sounds vile and cruel.", "It sounds a little dangerous for my cat.", "Wow that sounds like fun!", "Is there a limit to how much you can bet?", "Can I challenge you and your cat?").also { stage = 13 }
                2 -> playerl(FaceAnim.FRIENDLY, "Thanks for your help.").also { stage = END_DIALOGUE }
            }
            18 -> npcl(FaceAnim.FRIENDLY, "I agree. Cats have been known to be killed in fights.").also { stage++ }
            19 -> playerl(FaceAnim.FRIENDLY, "Oh how terrible. Is there nothing you can do to save your cat?").also { stage++ }
            20 -> npcl(FaceAnim.FRIENDLY, "Of course there is, you can instruct it to be cautious, and it will leave once it feels as if it is in danger.").also { stage++ }
            21 -> playerl(FaceAnim.FRIENDLY, "Oh that sounds quite wise.").also { stage++ }
            22 -> npcl(FaceAnim.FRIENDLY, "That depends on how you value your cat.").also { stage++ }
            23 -> playerl(FaceAnim.FRIENDLY, "Sorry, I don't follow.").also { stage++ }
            24 -> npcl(FaceAnim.FRIENDLY, "Well if your cat dies or it runs away you lose.").also { stage++ }
            25 -> npcl(FaceAnim.FRIENDLY, "So the longer it stays in the fight the better chance you have of winning.").also { stage = 16 }
            26 -> npcl(FaceAnim.FRIENDLY, "I don't know about that.").also { stage++ }
            27 -> npcl(FaceAnim.FRIENDLY, "One thing I know though is that it's a quick way to make and lose money.").also { stage = 16 }
            28 -> npcl(FaceAnim.FRIENDLY, "Hmmm...let me see. If I'm not mistaken, you can currently bet up to a maximum of $coinsAmount coins.").also { stage++ }
            29 -> playerl(FaceAnim.FRIENDLY, "That sounds like a silly amount of money to bet on a cat fight.").also { stage++ }
            30 -> npcl(FaceAnim.FRIENDLY, "You'd be surprised what some people would be willing to bet.").also { stage++ }
            31 -> npcl(FaceAnim.FRIENDLY, "There is a minimum bet of 100 coins too.").also { stage = 16 }
            32 -> npcl(FaceAnim.FRIENDLY, "No, not now, we don't really bet with outsiders.").also { stage++ }
            33 -> playerl(FaceAnim.FRIENDLY, "But I'm not an outsider, am I not inside in the pits now?").also { stage++ }
            34 -> npcl(FaceAnim.FRIENDLY, "I don't know, maybe once you've done something to win over my trust.").also { stage++ }
            35 -> playerl(FaceAnim.FRIENDLY, "Can it be bought?").also { stage++ }
            36 -> npcl(FaceAnim.FRIENDLY, "What?").also { stage++ }
            37 -> playerl(FaceAnim.FRIENDLY, "Your trust.").also { stage++ }
            38 -> npcl(FaceAnim.FRIENDLY, "No. Regular townsfolk don't like us, which leads us to distrust them and anyone else.").also { stage = 16 }
            39 -> playerl(FaceAnim.FRIENDLY, "Good day.").also { stage++ }
            40 -> npcl(FaceAnim.FRIENDLY, "What's so good about it?").also { stage++ }
            41 -> playerl(FaceAnim.FRIENDLY, "What's eating you.").also { stage++ }
            42 -> npcl(FaceAnim.FRIENDLY, "I'm ruined, all because of that stupid cat! It has ruined me.").also { stage++ }
            43 -> npcl(FaceAnim.FRIENDLY, "Why did it have to get itself killed? Why?").also { stage++ }
            44 -> playerl(FaceAnim.FRIENDLY, core.tools.BLUE + "*Aside* I think I should leave him alone. He seems a little upset right now.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = GamblerDialogue(player)

    override fun getIds(): IntArray = intArrayOf(
        NPCs.GAMBLER_2998,
        NPCs.GAMBLER_2999,
        NPCs.GAMBLER_3001,
        NPCs.GAMBLER_3002,
        NPCs.GAMBLER_3003,
        NPCs.GAMBLER_3004,
        NPCs.GAMBLER_3005,
        NPCs.GAMBLER_3006
    )
}