package content.region.kandarin.dialogue.piscatoris

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class HermanCaranosDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.ASKING, "Hello there! Lovely day, isn't it?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> {
                playerl(FaceAnim.FRIENDLY, "Hello. Lovely day, isn't it?")
                stage++
            }

            1 -> {
                npcl(
                    FaceAnim.ANNOYED,
                    "Lovely? If you say so... But I'm afraid I don't have time to chat about the weather.",
                )
                stage++
            }

            2 -> {
                playerl(FaceAnim.HALF_ASKING, "What's the rush?")
                stage++
            }

            3 -> {
                npcl(FaceAnim.FRIENDLY, "Trolls! They're attacking my fishing colony!")
                stage++
            }

            4 -> {
                playerl(FaceAnim.FRIENDLY, "Trolls? I didn't know there were any trolls around here.")
                stage++
            }

            5 -> {
                npcl(
                    FaceAnim.WORRIED,
                    "They're sea-dwelling trolls! They feed on the monkfish in these waters, but recently they've started attacking my colony!",
                )
                stage++
            }

            6 -> {
                playerl(FaceAnim.FRIENDLY, "Do you need any help?")
                stage++
            }

            7 -> {
                npcl(
                    FaceAnim.FRIENDLY,
                    "Yes, I do. I've researched this, and I believe our best hope is to find an experienced adventurer.",
                )
                stage++
            }

            8 -> {
                npcl(
                    FaceAnim.FRIENDLY,
                    "In the legends I've read, adventurers can deal with any monster that threatens a helpless settlement!",
                )
                stage++
            }

            9 -> {
                playerl(FaceAnim.FRIENDLY, "You've been... reading stories about adventurers?")
                stage++
            }

            10 -> {
                npcl(
                    FaceAnim.FRIENDLY,
                    "Yes! I've done extensive research! I always make important decisions based on careful analysis. For example, I chose this site for the colony because records show this part of the sea is perfect for monkfish fishing. I don't just stumble along blindly!",
                )
                stage++
            }

            11 -> {
                playerl(FaceAnim.FRIENDLY, "Fair enough, I suppose! So, you really think you need an adventurer?")
                stage++
            }

            12 -> {
                npcl(FaceAnim.THINKING, "Yes, exactly. A brave adventurer with lots of experience.")
                stage++
            }

            13 -> {
                playerl(FaceAnim.FRIENDLY, "I'm a brave adventurer! Can I try?")
                stage++
            }

            14 -> {
                npcl(FaceAnim.FRIENDLY, "You? Hmm, let me take a look at you...")
                stage++
            }

            15 -> {
                npcl(
                    FaceAnim.NEUTRAL,
                    "Well... you're not quite what I had in mind. But, I see you've earned quite a few Quest Points. Maybe you're up for the task after all...",
                )
                stage++
            }

            16 -> {
                npcl(
                    FaceAnim.HAPPY,
                    "Alright then, the Piscatoris Fishing Colony would be grateful for your help. Once the colony is safe, you will be handsomely rewarded.",
                )
                stage++
            }

            17 -> {
                playerl(FaceAnim.FRIENDLY, "Oh goody, I love rewards! Now, how can I help?")
                stage++
            }

            18 -> {
                npcl(
                    FaceAnim.ANNOYED,
                    "Well, as much as I admire your enthusiasm, I still believe we need a truly expert adventurer if the colony stands any chance.",
                )
                stage++
            }

            19 -> {
                playerl(
                    FaceAnim.FRIENDLY,
                    "An expert adventurer? I've had more adventures than you've had pickled gherkins!",
                )
                stage++
            }

            20 -> {
                npcl(FaceAnim.FRIENDLY, "If you could just stop blowing your own trumpet for a moment...")
                stage++
            }

            21 -> {
                playerl(FaceAnim.FRIENDLY, "...")
                stage++
            }

            22 -> {
                npcl(
                    FaceAnim.FRIENDLY,
                    "Fine, let me tell you what I've read about one of the greatest adventurers of all time.",
                )
                stage++
            }

            23 -> {
                npcl(
                    FaceAnim.FRIENDLY,
                    "In far-off Misthalin, there lives a legendary adventurer. His name is lost to time, but you shouldn't have trouble finding the Wise Old Man in Draynor Village.",
                )
                stage++
            }

            24 -> {
                playerl(FaceAnim.FRIENDLY, "Him? What in the world would you want with *him*?")
                stage++
            }

            25 -> {
                npcl(
                    FaceAnim.HALF_ASKING,
                    "Do you know him? He's a bit... eccentric, but he's one of the greatest adventurers to ever live!",
                )
                stage++
            }

            26 -> {
                playerl(
                    FaceAnim.FRIENDLY,
                    "Yes! He's mad! Didn't he smash his way into a bank and steal a bunch of stuff?",
                )
                stage++
            }

            27 -> {
                npcl(FaceAnim.FRIENDLY, "Ah, well... we must forgive him his little foibles.")
                stage++
            }

            28 -> {
                playerl(FaceAnim.FRIENDLY, "And you want me to fetch this Wise Old Man?")
                stage++
            }

            29 -> {
                npcl(
                    FaceAnim.FRIENDLY,
                    "Yes, that's right. But watch out for those sea trolls, and be careful when you speak to him.",
                )
                stage++
            }

            30 -> {
                sendDialogue(
                    "Herman describes the Wise Old Man's greatest feats. A cutscene plays, showing the Wise Old Man slaying various creatures.",
                )
                stage++
            }

            31 -> {
                playerl(
                    FaceAnim.FRIENDLY,
                    "Right... so you want me to fetch this old man who used to be a great adventurer?",
                )
                stage++
            }

            32 -> {
                npcl(FaceAnim.FRIENDLY, "Exactly! And watch out for those trolls!")
                stage = END_DIALOGUE
            }

            33 -> {
                playerl(FaceAnim.THINKING, "Finally, you've arrived! Did you bring the Wise Old Man?")
                stage++
            }

            34 -> {
                npcl(FaceAnim.HAPPY, "Indeed, I am here!")
                stage++
            }

            35 -> {
                playerl(FaceAnim.HAPPY, "Excellent! You have no idea how grateful I am!")
                stage++
            }

            36 -> {
                playerl(FaceAnim.SILENT, "Speaking of gratitude, I believe you mentioned some kind of reward...")
                stage++
            }

            37 -> {
                npcl(
                    FaceAnim.FRIENDLY,
                    "Reward? Well... we've got to discuss the Colony's defenses now. Perhaps you could make yourself useful around here? My brother, Franklin, could use some help. Also, Arnold, the storemaster, needed an extra pair of hands. He's in one of the big storage sheds.",
                )
                stage++
            }

            38 -> {
                playerl(FaceAnim.FRIENDLY, "Okay, I'll help out, then I get my reward?")
                stage++
            }

            39 -> {
                npcl(FaceAnim.HAPPY, "Yes, yes... Now please excuse us.")
                stage = END_DIALOGUE
            }

            40 -> {
                playerl(FaceAnim.FRIENDLY, "I've finished helping Franklin and Arnold.")
                stage++
            }

            41 -> {
                npcl(FaceAnim.FRIENDLY, "Oh, uh... that's good...")
                stage++
            }

            42 -> {
                playerl(FaceAnim.FRIENDLY, "Does this mean I get my reward now?")
                stage++
            }

            43 -> {
                npcl(FaceAnim.THINKING, "Well, there's something else I need you to do first...")
                stage++
            }

            44 -> {
                playerl(FaceAnim.FRIENDLY, "There always is.")
                stage++
            }

            45 -> {
                npcl(
                    FaceAnim.FRIENDLY,
                    "The Wise Old Man and I have discussed the situation. He's come up with a plan...",
                )
                stage++
            }

            46 -> {
                npcl(
                    FaceAnim.FRIENDLY,
                    "Yes, that's right. The sheer number of trolls means our best hope is to gather our forces and wipe out all the trolls at once.",
                )
                stage++
            }

            47 -> {
                playerl(FaceAnim.FRIENDLY, "Well, that sounds like a nice plan. One big fight, no more trolls.")
                stage++
            }

            48 -> {
                npcl(FaceAnim.FRIENDLY, "There's a slight problem though...")
                stage++
            }

            49 -> {
                npcl(FaceAnim.THINKING, "Yes, the part about gathering our forces.")
                stage++
            }

            50 -> {
                playerl(FaceAnim.FRIENDLY, "Let me guess - you haven't got any forces to gather!")
                stage++
            }

            51 -> {
                npcl(
                    FaceAnim.FRIENDLY,
                    "I sent most of the colonists away for their safety when the trolls began attacking the Colony.",
                )
                stage++
            }

            52 -> {
                npcl(FaceAnim.FRIENDLY, "So what you need to do, [Player], is fetch us an army!")
                stage++
            }

            53 -> {
                playerl(FaceAnim.FRIENDLY, "???")
                stage++
            }

            54 -> {
                npcl(
                    FaceAnim.CALM,
                    "In the Wizards' Guild in Yanille, Wizard Frumscone always seems to have a limitless supply of zombies and similar creatures. If you can persuade him to lend us an army, we should be able to defeat these trolls.",
                )
                stage++
            }

            55 -> {
                playerl(FaceAnim.FRIENDLY, "Okay, so I just need to go see Wizard Frumscone in Yanille?")
                stage++
            }

            56 -> {
                npcl(FaceAnim.HALF_GUILTY, "Yes, please hurry!")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return HermanCaranosDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.HERMAN_CARANOS_3822)
    }
}
