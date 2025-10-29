package content.region.kandarin.ardougne.quest.tol.dialogue

import content.data.GameAttributes
import core.api.getAttribute
import core.api.hasAnItem
import core.api.getQuestStage
import core.api.setQuestStage
import core.api.sendMessage
import core.api.setAttribute
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.game.world.GameWorld
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Represents the Bonafido dialogue.
 */
@Initializable
class BonafidoDialogue(player: Player? = null) : Dialogue(player) {

    override fun handle(componentID: Int, buttonID: Int): Boolean {
        when (getQuestStage(player!!, Quests.TOWER_OF_LIFE)) {
            0 -> when (stage) {
                START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "Hi there.").also { stage++ }
                1 -> npcl(FaceAnim.FRIENDLY, "A'up, babe, how's it goin'?").also { stage++ }
                2 -> playerl(FaceAnim.FRIENDLY, "Not bad, thank you. What are you building here?").also { stage++ }
                3 -> npcl(FaceAnim.FRIENDLY, "Nowt at moment. We're on strike!").also { stage++ }
                4 -> playerl(FaceAnim.FRIENDLY, "You're on strike? Whatever for?").also { stage++ }
                5 -> npcl(FaceAnim.FRIENDLY, "Those strange alchemists are up ta somefin', best go ta one ov 'em.").also { stage++ }
                6 -> playerl(FaceAnim.FRIENDLY, "Oh, okay.").also { stage = END_DIALOGUE }
            }

            1 -> when (stage) {
                START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "Are you Bonafido by any chance?").also { stage++ }
                1  -> npcl(FaceAnim.FRIENDLY, "That I am, babe.").also { stage++ }
                2  -> npcl(FaceAnim.FRIENDLY, "How can I be of assistance?").also { stage++ }
                3  -> playerl(FaceAnim.FRIENDLY, "Well, I've just spoken with Effigy about this tower. He tells me he hired you to work but you're on strike. Something about an extended tea break?").also { stage++ }
                4  -> npcl(FaceAnim.FRIENDLY, "Extended cup o' char? E's got it all wrong, babe. We know those alchemi-whatsits are up ta somefin' weird in building this tower, wot wiv all the machinery, and until we get some answers we're not moving a muscle.").also { stage++ }
                5  -> playerl(FaceAnim.FRIENDLY, "Well, he wanted me to do something about it.").also { stage++ }
                6  -> playerl(FaceAnim.FRIENDLY, "Is there no way I can convince you to continue?").also { stage++ }
                7  -> npcl(FaceAnim.FRIENDLY, "Sorry, babe.").also { stage++ }
                8  -> playerl(FaceAnim.FRIENDLY, "Well, can I at least have a look inside the tower?").also { stage++ }
                9  -> npcl(FaceAnim.FRIENDLY, "Only builders are allowed in there, like. You don't look much ov a builder to me.").also { stage++ }
                10 -> playerl(FaceAnim.FRIENDLY, "I take offence at that! Give me a chance; you may be surprised").also { stage++ }
                11 -> npcl(FaceAnim.FRIENDLY, "Okay. Come on me kitted out likea  builder - hard hat, some boots, scruffy trousers and shirt - and I'll let ya try out ta be one ov us.").also { stage++ }
                12 -> playerl(FaceAnim.FRIENDLY, "Count me in!").also { stage++ }
                13 -> {
                    end()
                    setQuestStage(player, Quests.TOWER_OF_LIFE, 2)
                    setAttribute(player, GameAttributes.TOL_BUILDER_COSTUME, 0)
                }
            }

            3 -> if (hasAnItem(player, Items.BUILDERS_SHIRT_10863, Items.BUILDERS_TROUSERS_10864, Items.BUILDERS_BOOTS_10865).container != null) {
                when (stage) {
                    START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "Hey, Bonafido.").also {
                        sendMessage(player, "You whistle for attention.")
                        stage++
                    }

                    1  -> npcl(FaceAnim.FRIENDLY, "Well, if it ain't ol' skinny bones.").also { stage++ }
                    2  -> playerl(FaceAnim.FRIENDLY, "So...").also { stage++ }
                    3  -> playerl(FaceAnim.FRIENDLY, "How do I look?").also { stage++ }
                    4  -> npcl(FaceAnim.FRIENDLY, "Like a builder frew and frew... Good job, babe! Nah, it's time to test yur skills.").also { stage++ }
                    5  -> playerl(FaceAnim.FRIENDLY, "Okay, what do you want me to build?").also { stage++ }
                    6  -> npcl(FaceAnim.FRIENDLY, "Build?").also { stage++ }
                    7  -> npcl(FaceAnim.FRIENDLY, "Hahaha, don't be a plonka!").also { stage++ }
                    8  -> npcl(FaceAnim.FRIENDLY, "It's not building skills that makes a true builder, you have to have the right mental attitude, yeah?").also { stage++ }
                    9  -> npcl(FaceAnim.FRIENDLY, "Let me see...").also { stage++ }
                    10 -> npcl(FaceAnim.FRIENDLY, "You've plenty of work to do, but you need a drink fast - what do you go for?").also { stage++ }
                    11 -> options("Orange juice", "Tea", "Bottle of wine").also { stage++ }
                    12 -> when (buttonID) {
                        1 -> playerl(FaceAnim.FRIENDLY, "Orange juice").also { stage++ }
                        2 -> playerl(FaceAnim.FRIENDLY, "Tea").also { stage = 15 }
                        3 -> playerl(FaceAnim.FRIENDLY, "Bottle of wine").also { stage = 14 }
                    }
                    13 -> npcl(FaceAnim.FRIENDLY, "What are you? A healthy builder? Of course, not juice!").also { stage = END_DIALOGUE }
                    14 -> npcl(FaceAnim.FRIENDLY, "Ooh, got '" + (if (player.isMale) "MISTER" else "MISS") + " Fancy Pants' here and " + (if (player.isMale) "HIS" else "HER") + " posh wine. You're no builder.").also { stage = END_DIALOGUE }
                    15 -> npcl(FaceAnim.FRIENDLY, "Bingo! Ain't nufin' betta.").also { stage++ }
                    16 -> npcl(FaceAnim.FRIENDLY, "Now, let's hear you whistle!").also { stage++ }
                    17 -> options("Do a little dance and whistle as loud as you can", "Whistle a pretty tune", "Whiste for attention").also { stage++ }
                    18 -> when (buttonID) {
                        1 -> playerl(FaceAnim.FRIENDLY, "Do a little dance and whistle as loud as you can").also { stage++ }
                        2 -> playerl(FaceAnim.FRIENDLY, "Whistle a pretty tune").also { stage = 20 }
                        3 -> playerl(FaceAnim.FRIENDLY, "Whiste for attention").also { stage = 24 }
                    }
                    19 -> npcl(FaceAnim.FRIENDLY, "What on " + GameWorld.settings!!.name + " are you doing? Get out of my sight.").also { stage = END_DIALOGUE }
                    20 -> npcl(FaceAnim.FRIENDLY, "You know, you have quite a talent there.").also { stage++ }
                    21 -> playerl(FaceAnim.FRIENDLY, "Why thank you. Do you think I have what it takes to become a famous whistler?").also { stage++ }
                    22 -> npcl(FaceAnim.FRIENDLY, "No.").also { stage++ }
                    23 -> playerl(FaceAnim.FRIENDLY, "Oh.").also { stage = END_DIALOGUE }
                    24 -> npcl(FaceAnim.FRIENDLY, "Wahey! Nice on. Next question.").also { stage++ }
                    25 -> npcl(FaceAnim.FRIENDLY, "What's a good sign that you need to replace your trousers?").also { stage++ }
                    26 -> options("Your legs are getting a bit cold", "They're ripped and full of holes", "The colour is starting to fade").also { stage++ }
                    27 -> when (buttonID) {
                        1 -> playerl(FaceAnim.FRIENDLY, "Your legs are getting a bit cold").also { stage = 30 }
                        2 -> playerl(FaceAnim.FRIENDLY, "They're ripped and full of holes").also { stage++ }
                        3 -> playerl(FaceAnim.FRIENDLY, "The colour is starting to fade").also { stage = 29 }
                    }
                    28 -> npcl(FaceAnim.FRIENDLY, "No, no, no. That's the only way they should be!").also { stage = END_DIALOGUE }
                    29 -> npcl(FaceAnim.FRIENDLY, "Oh no, mate, you're really not getting the hang of this are you?").also { stage = END_DIALOGUE }
                    30 -> npcl(FaceAnim.FRIENDLY, "Exactamondo! Glad to see you are thinking like a builder.").also { stage++ }
                    31 -> playerl(FaceAnim.FRIENDLY, "I am?").also { stage++ }
                    32 -> npcl(FaceAnim.FRIENDLY, "Yes, obviously if your legs are cold, you lost your trousers altogether!").also { stage++ }
                    33 -> playerl(FaceAnim.FRIENDLY, "That's just what I was thinking.").also { stage++ }
                    34 -> npcl(FaceAnim.FRIENDLY, "Now the last question - what do you do if you cut your finger?").also { stage++ }
                    35 -> options("Cry", "Carry on, it'll fix itself", "Fetch a plaster and ointment").also { stage++ }
                    36 -> when (buttonID) {
                        1 -> playerl(FaceAnim.FRIENDLY, "Cry").also { stage = 37 }
                        2 -> playerl(FaceAnim.FRIENDLY, "Carry on, it'll fix itself").also { stage = 39 }
                        3 -> playerl(FaceAnim.FRIENDLY, "Fetch a plaster and ointment").also { stage = 38 }
                    }
                    37 -> npcl(FaceAnim.FRIENDLY, "When was the last time you saw a builder cry?").also { stage = END_DIALOGUE }
                    38 -> npcl(FaceAnim.FRIENDLY, "Look, let me give you a bit of advice. If you want to look weak, that's the best way to go.").also { stage = END_DIALOGUE }
                    39 -> npcl(FaceAnim.FRIENDLY, "Yep, that's the one!").also { stage++ }
                    40 -> playerl(FaceAnim.FRIENDLY, "Suppose it could get infected then, but that'll just impress the lads all the more.").also { stage++ }
                    41 -> npcl(FaceAnim.FRIENDLY, "Exactly, you're gettin the hang of this quickly!").also { stage++ }
                    42 -> npcl(FaceAnim.FRIENDLY, "Cosmic! I canna see no reason why ya can't go in t'tower now. But be careful, there be some precarious situations in there.").also { stage++ }
                    43 -> playerl(FaceAnim.FRIENDLY, "So, what's left to build?").also { stage++ }
                    44 -> npcl(FaceAnim.FRIENDLY, "Well, there's a pipe system, a pressure machine, and a strange cage up at the top that needs finishing off. The alchemists couldn't explain what they were for.").also { stage++ }
                    45 -> playerl(FaceAnim.FRIENDLY, "Thanks!").also { stage = END_DIALOGUE }
                }
            } else {
                if (getAttribute(player, GameAttributes.TOL_TOWER_ACCESS, 0) == 1) {
                    when (stage) {
                        0 -> playerl(FaceAnim.FRIENDLY, "Hi again Bonafido.").also { stage++ }
                        1 -> npcl(FaceAnim.FRIENDLY, "Hi, ${player.username} the Builder'! Been into the tower yet?").also { stage++ }
                        2 -> playerl(FaceAnim.FRIENDLY, "Not yet. I'll go when I'm ready. I'm taking my time, may have a quick cup of tea first.").also { stage++ }
                        3 -> npcl(FaceAnim.FRIENDLY, "Hahaha.").also { stage = END_DIALOGUE }
                    }
                } else {
                    when (stage) {
                        0 -> playerl(FaceAnim.FRIENDLY, "Heya, Bona. I had a look at the building. There's some complex machinery in there! Going to cost someone.").also { stage++ }
                        1 -> npcl(FaceAnim.FRIENDLY, "Yep, told ya so. Any idea what those alchemists are up ta?").also { stage++ }
                        2 -> playerl(FaceAnim.FRIENDLY, "Well, I agree now they're up to something. The question is what...? I don't think we're going to find out much from the alchemists, though.").also { stage++ }
                        3 -> npcl(FaceAnim.FRIENDLY, "So what are you going to do?").also { stage++ }
                        4 -> playerl(FaceAnim.FRIENDLY, "I shall see if I can't fix it up and see for myself what is going on. Should be able to get a good reward from them. But don't worry, I'll make sure your efforts don't go unpaid.").also { stage++ }
                        5 -> npcl(FaceAnim.FRIENDLY, "You're a star among builders, darling!").also { stage = END_DIALOGUE }
                    }
                }
            }

            5 -> when (stage) {
                START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "I'm really getting annoyed with those alchemists.").also { stage++ }
                1 -> npcl(FaceAnim.FRIENDLY, "Why?").also { stage++ }
                2 -> playerl(FaceAnim.FRIENDLY, "Well, I completed the tower and I still have no idea what it does, and then as soon as I tell Effigy of my hard work, he doesn't say one word of thanks and legs it off into the tower!").also { stage++ }
                3 -> npcl(FaceAnim.FRIENDLY, "Well, I'll be a rabbit in da eyes of a fox. Best ya chase after 'im then and get us our dosh! Don't you do a runner, now!").also { stage++ }
                4 -> playerl(FaceAnim.FRIENDLY, "Don't worry, I'm not about to let Effigy get away with treating us builders like this!").also { stage = END_DIALOGUE }
            }

            6 -> if (getAttribute(player, GameAttributes.TOL_CUTSCENE, false)) {
                when (stage) {
                    START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "You won't BELIEVE what I just saw!").also { stage++ }
                    1 -> npcl(FaceAnim.FRIENDLY, "What? What?").also { stage++ }
                    2 -> playerl(FaceAnim.FRIENDLY, "That tower is...a tower of life! I just witnessed with my very own eyes the creation of a homunculus.").also { stage++ }
                    3 -> npcl(FaceAnim.FRIENDLY, "A homanque-what?").also { stage++ }
                    4 -> playerl(FaceAnim.FRIENDLY, "Homunculus! They have found a way of creating life itself, but the creature they have made is not a happy being.").also { stage++ }
                    5 -> npcl(FaceAnim.FRIENDLY, "And our money?").also { stage++ }
                    6 -> playerl(FaceAnim.FRIENDLY, "Are you listening at all? This creature is very dangerous. It needs to be stopped. And fast!").also { stage++ }
                    7 -> npcl(FaceAnim.FRIENDLY, "I'm glad you've got the money sorted.").also { stage = END_DIALOGUE }
                }
            }

            8 -> when (stage) {
                START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "Hi, Bonafido.").also { stage++ }
                1 -> npcl(FaceAnim.FRIENDLY, "Hi. How's the tower?").also { stage++ }
                2 -> playerl(FaceAnim.FRIENDLY, "Finished, really.").also { stage++ }
                3 -> npcl(FaceAnim.FRIENDLY, "Looks like we're out of a job then.").also { stage++ }
                4 -> playerl(FaceAnim.FRIENDLY, "Oh, I wouldn't worry about it. There are frequently new buildings required around " + GameWorld.settings!!.name + ". I'm sure your services will be called upon again.").also { stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.BONAFIDO_5580)
}
