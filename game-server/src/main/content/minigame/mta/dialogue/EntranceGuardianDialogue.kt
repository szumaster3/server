package content.minigame.mta.dialogue

import content.minigame.mta.plugin.MTAZone
import content.minigame.mta.plugin.ProgressHat
import core.api.addItem
import core.api.getVarbit
import core.api.hasAnItem
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.game.world.GameWorld.settings
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

/**
 * Represents the Entrance guardian dialogue.
 */
@Initializable
class EntranceGuardianDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        player("Hi.")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> if (player.getSavedData().activityData.isStartedMta) {
                options(
                    "Can you tell me about this place again?",
                    "Can you explain the different portals?",
                    "About the progress hat...",
                    "Thanks, bye!",
                ).also {
                    stage = 31
                }
            } else {
                npc(FaceAnim.OLD_NORMAL, "Greetings. What wisdom do you seek?").also { stage++ }
            }

            1 -> options("I'm new to this place. Where am I?", "None, I don't really care.").also { stage++ }
            2 -> when (buttonId) {
                1 -> player("I'm new to this place. Where am I?").also { stage++ }
                2 -> end()
            }
            3 -> npc(FaceAnim.OLD_NORMAL, "Well young one, you have entered the Magic Training", "Arena. It was built at the start of the Fifth Age, when", "runestones were first discovered. It was made because", "of the many pointless accidents caused by inexperienced").also { stage++ }
            4 -> npc(FaceAnim.OLD_NORMAL, "mages.").also { stage++ }
            5 -> player("Who created it?").also { stage++ }
            6 -> npc(FaceAnim.OLD_NORMAL, "Good question. It was originally made by the ancestors", "of the wizards in the Wizards Tower. However, it was", "destroyed by melee and ranged warriors who took", "offence at the use of this new 'Magic Art'. Recently").also { stage++ }
            7 -> npc(FaceAnim.OLD_NORMAL, "the current denizens of the Wizards Tower have", "resurrected the arena including various Guardians you", "will see as you look around. We are here to help and to", "ensure things run smoothly.").also { stage++ }
            8 -> player("Interesting. So what can I do here?").also { stage++ }
            9 -> npc(FaceAnim.OLD_NORMAL, "You may train up your skills in the magic arts by", "travelling through one of the portals at the back of this", "entrance hall. By training up in one of these areas you", "will be awarded special Pizazz Points unique to each.").also { stage++ }
            10 -> npc(FaceAnim.OLD_NORMAL, "room. With these points you may claim a variety of", "items from my fellow guardian up the stairs.").also { stage++ }
            11 -> player("How do you record the points I have earned?").also { stage++ }
            12 -> {
                if (player.getSavedData().activityData.isStartedMta) {
                    npc(FaceAnim.OLD_NORMAL, "With the Pizazz Progress Hat I gave you, of course.").also { stage = 40 }
                } else {
                    npc(FaceAnim.OLD_NORMAL, "You really are full of questions! You will need a special", "Pizzaz Progress Hat! I can give you one if you so", "wish to train here.").also { stage++ }
                }
            }

            13 -> player("Yes Please!").also { stage++ }
            14 -> {
                player.getSavedData().activityData.isStartedMta = true
                ProgressHat.progress(player)
                npc(FaceAnim.OLD_NORMAL, "Here you go. Talk to the hat to find out your current", "Pizzaz Points totals.")
                stage++
            }

            15 -> player("Talk to it?").also { stage++ }
            16 -> npc(FaceAnim.OLD_NORMAL, "Well of course, it's a magic Pizazz Progress Hat! Mind", "your manners though, hats have feelings too!").also { stage++ }
            17 -> player("Er... if you insist.").also { stage++ }
            18 -> npc(FaceAnim.OLD_NORMAL, "Oh, and a word of warning: should you decide to leave", "the rooms by any method other than the exit portals,", "you will be teleported to the entrance and have any", "items that you picked up in the room removed.").also { stage++ }
            19 -> player("Okay. Thanks!").also { stage = END_DIALOGUE }
            30 -> options(
                "Can you tell me about this place again?",
                "Can you explain the different portals?",
                "About the progress hat...",
                "Thanks, bye!",
            ).also {
                stage++
            }
            31 -> when (buttonId) {
                1 -> player("Can you tell me about this place again?").also { stage = 3 }
                2 -> player("Can you explain the different portals?").also { stage = 50 }
                3 -> {
                    val hasHat = hasAnItem(player, *ProgressHat.hatIds).container != null
                    if (!hasHat) {
                        npc(FaceAnim.OLD_NORMAL, "You want another one don't you.").also { stage = 110 }
                    } else {
                        npc(FaceAnim.OLD_NORMAL, "Which you have stored somewhere I'm sure.").also { stage = 101 }
                    }
                }

                4 -> end()
            }
            101 -> player("Yes. What's it for?").also { stage++ }
            102 -> npc(FaceAnim.OLD_NORMAL, "Collect Pizazz Points from the various areas and the", "hat will remember your totals. Talk to the hat at any", "time to find out what points you have. Go upstairs and", "talk to the Rewards Guardian to claim items for the").also { stage++ }
            103 -> npc(FaceAnim.OLD_NORMAL, "points.").also { stage = 30 }
            110 -> player("Sorry, can I?").also { stage++ }
            111 -> {
                val points = MTAZone.getTotalPoints(player)
                if (points < 300) {
                    addItem(player, Items.PROGRESS_HAT_6885)
                } else {
                    ProgressHat.progress(player)
                }
                npc(FaceAnim.OLD_NORMAL, "Here you go. Talk to the hat to find out your current", "Pizazz Point totals.").also { stage = 30 }
            }

            40 -> player("OK.").also { stage++ }
            41 -> npc(FaceAnim.OLD_NORMAL, "Oh, and a word of warning: should you decide to log", "out whilst in any of the rooms in the arena, you will be", "teleported to the entrance and have any items that you", "picked up in the room removed.").also { stage = 30 }
            50 -> npc(FaceAnim.OLD_NORMAL, "They lead to four areas to train your magic: The", "Telekinetic Theatre, The Alchemists' Playground, The", "Enchanting Chamber, and The Creature Graveyard.").also { stage++ }
            51 -> options(
                "What's the Telekinetic Theatre?",
                "What's the Alchemists' Playground?",
                "What's the Enchanting CHamber?",
                "What's the Creature Gaveyard?",
                "Thanks, bye!",
            ).also {
                stage++
            }

            52 -> when (buttonId) {
                1 -> player("What's the Telekinetic Theatre?").also { stage = 60 }
                2 -> player("What's the Alchemists' Playground?").also { stage = 70 }
                3 -> player("What's the Enchanting Chamber?").also { stage = 80 }
                4 -> player("What's the Creature Graveyard?").also { stage = 90 }
                5 -> end()
            }

            60 -> npc(FaceAnim.OLD_NORMAL, "In there you can earn Telekinetic Pizazz Points for", "trde upstairs.").also { stage++ }
            61 -> player("What will I be doing in there?").also { stage++ }
            62 -> npc(FaceAnim.OLD_NORMAL, "That depends on how much of a time-waster you are!", "You are required to use the Telekinetic Grab spell in", "order to move a statue through a maze. Casting the", "spell will move the statue towards you until it reaches a").also { stage++ }
            63 -> npc(FaceAnim.OLD_NORMAL, "wall. So by standing on the different sides of the maze", "you can move the statue North, East, South or West.", "You will be rewarded Pizazz Points for each maze", "successfully solved.").also { stage = 51 }
            70 -> npc(FaceAnim.OLD_NORMAL, "In the playground you can earn Alchemist Pizazz", "Points for trading with the rewards guardian upstairs.").also { stage++ }
            71 -> player("What's in there?").also { stage++ }
            72 -> npc(FaceAnim.OLD_NORMAL, "You'll find eight cupboards containing items you can", "turn to gold using the low and high alchemy spells. The", "money you earn will be taken from you upon leaving", "the playground to pay for the upkeep of this training").also { stage++ }
            73 -> npc(FaceAnim.OLD_NORMAL, "arena and to help fund magic shops around " + settings!!.name + ".", "You will be rewarded with 1 Pizazz Point for every", "100 coins deposited and a percentage of the money you", "create. Keep in mind that you will not be able to take").also { stage++ }
            74 -> npc(FaceAnim.OLD_NORMAL, "more than 1000 coins back out with you.").also { stage++ }
            75 -> player("Sounds simple.").also { stage = 51 }
            80 -> npc(FaceAnim.OLD_NORMAL, "In here you will be able to earn Enchantment Pizazz", "Points for trade upstairs.").also { stage++ }

            81 -> player("What will I have to do?").also { stage++ }
            82 -> npc(FaceAnim.OLD_NORMAL, "You will find yourself amongst various piles of shapes.", "You can pick up these shapes and use on of your", "enchanting spells upon it to morph it into an orb. You'll", "also see a hole in the centre of the room; put the orbs").also { stage++ }
            83 -> npc(FaceAnim.OLD_NORMAL, "in here and for every 20 we will credit you with a gift,", "You will get Pizazz Points for every 10 shapes that", "you convert and the amount of points depends on the", "spell you use.").also { stage = 51 }
            90 -> npc(FaceAnim.OLD_NORMAL, "In here you will be able to earn Graveyard Pizazz", "Points for trade upstairs.").also { stage++ }
            91 -> player("But what do I have to do in there?").also { stage++ }
            92 -> npc(FaceAnim.OLD_NORMAL, "Patience young one. A great many creatures die in", "this world and much of their reminds can cause clutter.", "We have taken it upon ourselves to teleport many", "bones from these creatures into this graveyard for a").also { stage++ }
            93 -> npc(FaceAnim.OLD_NORMAL, "use. It's up to you to practice your bones to bananas", "spell in order to put these bones into immediate use -", "nutritious food for monsters! Just convert the bones", "and put them in the holes in the walls. There is a").also { stage++ }
            94 -> npc(FaceAnim.OLD_NORMAL, "drawback to this room however, the bones often fall on", "people so you may want to eat some of those bananas", "to stay alive. When people die we confiscate Pizazz", "Points for the effort of teleporting incompetent mages.").also { stage = 51 }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.ENTRANCE_GUARDIAN_3097)
}
