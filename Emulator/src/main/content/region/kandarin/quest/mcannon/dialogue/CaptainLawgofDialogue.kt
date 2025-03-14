package content.region.kandarin.quest.mcannon.dialogue

import content.region.kandarin.quest.mcannon.DwarfCannon
import core.api.setVarp
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.item.Item
import org.rs.consts.NPCs
import org.rs.consts.Quests

class CaptainLawgofDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private var quest: Quest? = null

    override fun open(vararg args: Any): Boolean {
        quest = player.getQuestRepository().getQuest(Quests.DWARF_CANNON)
        when (quest!!.getStage(player)) {
            80 -> player("Hi.")
            50 ->
                npc(
                    "How are you doing in there, trooper? We've been",
                    "trying our best with that thing, but I just haven't got",
                    "the patience.",
                )
            40 -> player("Hello, has Lollk returned yet?")
            else -> player("Hello.")
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (quest!!.getStage(player)) {
            0 ->
                when (stage) {
                    0 -> {
                        npc(
                            FaceAnim.OLD_NORMAL,
                            "Guthix be praised, the cavalry has arrived! Hero, how",
                            "would you like to be made an honorary member of the",
                            "Black Guard?",
                        )
                        stage++
                    }

                    1 -> {
                        player("The Black Guard, what's that?")
                        stage++
                    }

                    2 -> {
                        npc(
                            FaceAnim.OLD_NORMAL,
                            "Hawhaw! 'What's that' " + (if (player.appearance.isMale) "he" else "she") +
                                " asks, what a sense of",
                            "humour! The Black Guard is the finest regiment in the",
                            "dwarven army. Only the best of the best are allowed to",
                            "join it and then they receive months of rigorous",
                        )
                        stage++
                    }

                    3 -> {
                        npc(
                            FaceAnim.OLD_NORMAL,
                            "training. However, we are currently in need of a hero,",
                            "so for a limited time only I'm offering you, a human, a",
                            "chance to join this prestigious regiment. What do you",
                            "say?",
                        )
                        stage++
                    }

                    4 -> {
                        player("Sure, I'd be honoured to join.")
                        stage++
                    }

                    5 -> {
                        quest!!.start(player)
                        npc(
                            FaceAnim.OLD_NORMAL,
                            "That's the spirit! Now trooper, we have no time to waste",
                            "- the goblins are attacking from the forests to the",
                            "South. There are so many of them, they are",
                            "overwhelming my men and breaking through our",
                        )
                        stage = 100
                    }
                }

            10 ->
                when (stage) {
                    0 -> {
                        if (DwarfCannon.allRailsFixed(player)) {
                            npc(
                                FaceAnim.OLD_NORMAL,
                                "Well done, trooper! The goblins seems to have stopped",
                                "getting in. I think you've done the job!",
                            )
                            stage = 105
                        }
                        npc(FaceAnim.OLD_NORMAL, "Hello, trooper, how are you doing with those railings?")
                        stage++
                    }

                    1 -> {
                        player("I'm getting there.")
                        stage++
                    }

                    2 -> {
                        npc(
                            FaceAnim.OLD_NORMAL,
                            "The goblins are still getting in, so there must still be",
                            "some broken railings.",
                        )
                        stage++
                    }

                    3 -> {
                        if (!player.hasItem(Item(14))) {
                            player("But I'm out of railings...")
                            stage = 5
                        }
                        player("Don't worry, I'll find them soon enough.")
                        stage++
                    }

                    4 -> end()
                    5 -> {
                        npc(FaceAnim.OLD_NORMAL, "That's okay, we've got plenty, here you go.")
                        stage++
                    }

                    6 -> {
                        player.packetDispatch.sendMessage("The Dwarf Captain gives you another railing.")
                        player.inventory.add(Item(14), player)
                        end()
                    }

                    100 -> {
                        npc(
                            FaceAnim.OLD_NORMAL,
                            "perimeter defences; could you please try to fix the",
                            "stockade by replacing the broken rails with these new",
                            "ones?",
                        )
                        stage++
                    }

                    101 -> {
                        player("Sure, sounds easy enough...")
                        stage++
                    }

                    102 -> {
                        player.inventory.add(Item(14, 6), player)
                        player.dialogueInterpreter.sendDialogue("The Dwarf Captain gives you six railings.")
                        stage++
                    }

                    103 -> {
                        npc(FaceAnim.OLD_NORMAL, "Report back to me once you've fixed the railings.")
                        stage++
                    }

                    104 -> {
                        player("Yes Sir, Captain!")
                        end()
                    }

                    105 -> {
                        player("Great, I'll be getting on then.")
                        stage++
                    }

                    106 -> {
                        npc(FaceAnim.OLD_NORMAL, "What? I'll have you jailed for desertion!")
                        stage++
                    }

                    107 -> {
                        npc(
                            FaceAnim.OLD_NORMAL,
                            "Besides, I have another commission for you. Just",
                            "before the goblins over-ran us we lost contact with our",
                            "watch tower to the South, that's why the goblins",
                            "managed to catch us unawares. I'd like you to perform.",
                        )
                        stage++
                    }

                    108 -> {
                        npc(
                            FaceAnim.OLD_NORMAL,
                            "a covert operation into enemy territory, to check up on",
                            "the guards we have stationed there.",
                        )
                        stage++
                    }

                    109 -> {
                        npc(FaceAnim.OLD_NORMAL, "They should have reported in by now ...")
                        stage++
                    }

                    110 -> {
                        player("Okay, I'll see what I can find out.")
                        stage++
                    }

                    111 -> {
                        npc(
                            FaceAnim.OLD_NORMAL,
                            "Excellent! I have two men there, the dwarf-in-charge is",
                            "called Gilob, find him and tell him that I'll send him a",
                            "relief guard just as soon as we mop up these remaining",
                            "goblins.",
                        )
                        stage++
                    }

                    112 -> {
                        setVarp(player, 0, 3, true)
                        quest!!.setStage(player, 20)
                        end()
                    }
                }

            20 ->
                when (stage) {
                    0 -> {
                        if (player.inventory.containsItem(DwarfCannon.DWARF_REMAINS)) {
                            npc(FaceAnim.OLD_NORMAL, "Have you been to the watch tower yet?")
                            stage = 4
                        }
                        npc(FaceAnim.OLD_NORMAL, "Hello, any news from the watchman?")
                        stage++
                    }

                    1 -> {
                        player("Not yet.")
                        stage++
                    }

                    2 -> {
                        npc(FaceAnim.OLD_NORMAL, "Well, as quick as you can then.")
                        stage++
                    }

                    3 -> end()
                    4 -> {
                        player(
                            "I have some terrible news for you Captain, the goblins",
                            "over ran the tower, your guards fought well but were",
                            "overwhelmed.",
                        )
                        stage++
                    }

                    5 -> {
                        interpreter.sendItemMessage(
                            DwarfCannon.DWARF_REMAINS,
                            "You give the Dwarf Captain his subordinate's remains...",
                        )
                        stage++
                    }

                    6 -> {
                        npc(
                            FaceAnim.OLD_NORMAL,
                            "I can't believe it, Gilob was the finest lieutenant I had!",
                            "We'll give him a fitting funeral, but what of his",
                            "command? His son, Lollk, was with him. Did you find",
                            "his body too?",
                        )
                        stage++
                    }

                    7 -> {
                        player("No, there was only one body there, I searched pretty", "well.")
                        stage++
                    }

                    8 -> {
                        npc(
                            FaceAnim.OLD_NORMAL,
                            "The goblins must have taken him. Please traveller, seek",
                            "out the goblin's hideout and return the lad to us. They",
                            "always attack from the South-west, so they must be",
                            "based down there.",
                        )
                        stage++
                    }

                    9 -> {
                        player("Okay, I'll see if I can find their hideout.")
                        stage++
                    }

                    10 -> {
                        end()
                        setVarp(player, 0, 5, true)
                        player.inventory.remove(DwarfCannon.DWARF_REMAINS)
                        quest!!.setStage(player, 30)
                    }
                }

            30 ->
                when (stage) {
                    0 -> {
                        npc(FaceAnim.OLD_NORMAL, "Trooper, have you managed to find the goblins' base?")
                        stage++
                    }

                    1 -> {
                        player("Not yet I'm afraid, but I'll keep looking...")
                        stage++
                    }

                    2 -> end()
                }

            40 ->
                when (stage) {
                    0 -> {
                        npc(
                            FaceAnim.OLD_NORMAL,
                            "He has, and I thank you from the bottom of my heart",
                            "- without you he'd be a goblin barbecue!",
                        )
                        stage++
                    }

                    1 -> {
                        player("Always a please to help.")
                        stage++
                    }

                    2 -> {
                        npc(FaceAnim.OLD_NORMAL, "In that case could I ask one more favour of you...")
                        stage++
                    }

                    3 -> {
                        npc(
                            "When the goblins attacked us some of them managed to",
                            "slip past my guards and sabotage our cannon. I don't",
                            "have anybody who understands how it works, could you,",
                            "have a look at it and see if you could get it working for",
                        )
                        stage++
                    }

                    4 -> {
                        npc(FaceAnim.OLD_NORMAL, "us, please?")
                        stage++
                    }

                    5 -> {
                        player("Okay, I'll see what I can do.")
                        stage++
                    }

                    6 -> {
                        npc(FaceAnim.OLD_NORMAL, "Thank you, take this toolkit, you'll need it...")
                        stage++
                    }

                    7 -> {
                        npc(FaceAnim.OLD_NORMAL, "Report back to me if you manage to fix it.")
                        stage++
                    }

                    8 -> {
                        player.inventory.add(DwarfCannon.TOOL_KIT, player)
                        setVarp(player, 0, 7)
                        quest!!.setStage(player, 50)
                        end()
                    }
                }

            50 ->
                when (stage) {
                    0 -> {
                        player("It's not an easy job, but I'm getting there.")
                        stage++
                    }

                    1 -> {
                        if (!player.hasItem(DwarfCannon.TOOL_KIT)) {
                            player("I'm afraid I lost the toolkit...")
                            stage = 3
                        }
                        npc(
                            FaceAnim.OLD_NORMAL,
                            "Good stuff, let me know if you have any luck. If we",
                            "manage to get that thing working, those goblins will be",
                            "no trouble at all.",
                        )
                        stage++
                    }

                    2 -> end()
                    3 -> {
                        npc(FaceAnim.OLD_NORMAL, "That was silly... never mind, here you go.")
                        stage++
                    }

                    4 -> {
                        end()
                        player.sendMessage("The Dwarf Captain gives you another toolkit.")
                        player.inventory.add(DwarfCannon.TOOL_KIT, player)
                    }
                }

            60 ->
                when (stage) {
                    -1 -> end()
                    0 -> {
                        npc(FaceAnim.OLD_NORMAL, "Hello there trooper, how's things?")
                        stage++
                    }

                    1 -> {
                        player("Well, I think I've done it, take a look...")
                        stage++
                    }

                    2 -> {
                        if (!player.inventory.contains(1, 1)) {
                            npc(FaceAnim.OLD_NORMAL, "Bring me back the toolkit please.")
                            stage = -1
                        }
                        npc(FaceAnim.OLD_NORMAL, "That's fantastic, well done!")
                        stage++
                    }

                    3 -> {
                        npc(
                            FaceAnim.OLD_NORMAL,
                            "Well I don't believe it, it seems to be working perfectly!",
                            "I seem to have underestimated you, trooper!",
                        )
                        stage++
                    }

                    4 -> {
                        player("Not bad for an adventurer eh?")
                        stage++
                    }

                    5 -> {
                        npc(
                            FaceAnim.OLD_NORMAL,
                            "Not bad at all, your effort is appreciated, my friend.",
                            "Now, if I could figure what the thing uses as ammo...",
                        )
                        stage++
                    }

                    6 -> {
                        npc(
                            FaceAnim.OLD_NORMAL,
                            "The Black Guard forgot to send instructions. I know I",
                            "said that was the last favour, but...",
                        )
                        stage++
                    }

                    7 -> {
                        player("What now?")
                        stage++
                    }

                    8 -> {
                        npc(
                            FaceAnim.OLD_NORMAL,
                            "I can't leave this post, could you go to the Black Guard",
                            "base and find out what this thing actually shoots?",
                        )
                        stage++
                    }

                    9 -> {
                        player("Okay then, just for you!")
                        stage++
                    }

                    10 -> {
                        npc(
                            FaceAnim.OLD_NORMAL,
                            "That's great, we were lucky you came along when you",
                            "did. The base is located just South of the Ice Mountain.",
                        )
                        stage++
                    }

                    11 -> {
                        npc(
                            FaceAnim.OLD_NORMAL,
                            "You'll need to speak to Nulodion, the Dwarf Cannon",
                            "engineer. He's the Weapons Development Chief for the",
                            "Black Guard, so if anyone knows how to fire this thing,",
                            "it'll be him.",
                        )
                        stage++
                    }

                    12 -> {
                        player("Okay, I'll see what I can do.")
                        stage++
                    }

                    13 -> {
                        player.inventory.remove(DwarfCannon.TOOL_KIT)
                        player.sendMessage("You give the toolkit back to Captain Lawgof.")
                        setVarp(player, 0, 9, true)
                        quest!!.setStage(player, 70)
                        end()
                    }
                }

            80 ->
                when (stage) {
                    0 -> {
                        npc(FaceAnim.OLD_NORMAL, "Hello trooper, any word from the Cannon Engineer?")
                        stage++
                    }

                    1 -> {
                        player("Yes, I have spoken to him.")
                        stage++
                    }

                    2 ->
                        if (!player.inventory.containsItem(DwarfCannon.MOULD) ||
                            !player.inventory.containsItem(DwarfCannon.NULODION_NOTES)
                        ) {
                            player("He gave me some items to give you... but I seem to", "have lost something.")
                            stage++
                        } else {
                            player("He gave me an ammo mould and some notes to give to", "you...")
                            stage = 6
                        }

                    3 -> {
                        npc(FaceAnim.OLD_NORMAL, "If you could go back and get another, I'd appreciate it.")
                        stage++
                    }

                    4 -> {
                        player("Oh, okay then.")
                        stage++
                    }

                    5 -> end()
                    6 -> {
                        npc(
                            FaceAnim.OLD_NORMAL,
                            "Aah, of course, we make the ammo! This is great, now",
                            "we will be able to defend ourselves. I don't know how to",
                            "thank you...",
                        )
                        stage++
                    }

                    7 -> {
                        player("You could give me a cannon...")
                        stage++
                    }

                    8 -> {
                        npc(FaceAnim.OLD_NORMAL, "Hah! You'd be lucky, those things are worth a fortune.")
                        stage++
                    }

                    9 -> {
                        npc(
                            FaceAnim.OLD_NORMAL,
                            "I'll tell you what, I'll write to the Cannon",
                            "Engineer requesting him to sell you one. He controls",
                            "production of the cannons.",
                        )
                        stage++
                    }

                    10 -> {
                        npc(
                            FaceAnim.OLD_NORMAL,
                            "He won't be able to give you one, but for the right",
                            "price, I'm sure he'll sell one to you.",
                        )
                        stage++
                    }

                    11 -> {
                        player("Hmmm... sounds interesting, I might take you up on", "that.")
                        stage++
                    }

                    12 -> {
                        quest!!.finish(player)
                        end()
                    }
                }

            100 ->
                when (stage) {
                    0 -> {
                        npc(FaceAnim.OLD_NORMAL, "Well hello there, how you doing?")
                        stage++
                    }

                    1 -> {
                        player("Not bad, yourself?")
                        stage++
                    }

                    2 -> {
                        npc(
                            FaceAnim.OLD_NORMAL,
                            "I'm great now, those goblins can't get close with this",
                            "cannon blasting at them!",
                        )
                        stage++
                    }

                    3 -> end()
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.CAPTAIN_LAWGOF_208)
    }
}
