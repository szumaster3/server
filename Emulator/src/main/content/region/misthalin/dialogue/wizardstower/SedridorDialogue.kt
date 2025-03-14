package content.region.misthalin.dialogue.wizardstower

import content.global.travel.EssenceTeleport.teleport
import core.api.*
import core.api.quest.finishQuest
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.GroundItem
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class SedridorDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc("Welcome adventurer, to the world renowned", "Wizards' Tower. How may I help you?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        val quest = getQuestStage(player, Quests.RUNE_MYSTERIES)
        when (stage) {
            0 -> {
                if (quest == 100) {
                    player("Hello there.")
                    stage = 400
                    return true
                }
                if (quest == 10) {
                    options(
                        "Nothing thanks, I'm just looking around.",
                        "What are you doing down here?",
                        "I'm looking for the head wizard.",
                    )
                    stage = 1000
                    return true
                }
                if (quest == 20) {
                    npc("Wow! This is... incredible!")
                    stage = 52
                    return true
                }
                if (quest == 30) {
                    npc(
                        "Ah, " + player.username + ". How goes your quest? Have you",
                        "delivered the research notes to my friend Aubury yet?",
                    )
                    stage = 800
                    return true
                }
                if (quest == 50) {
                    npc(
                        "Ah, " + player.username + ". How goes your quest? Have you",
                        "delivered the research notes to my friend Aubury yet?",
                    )
                    stage = 900
                    return true
                }
                options("Nothing thanks, I'm just looking around.", "What are you doing down here?")
                stage = 1
            }

            400 -> {
                npc("Hello again " + player.username + ". What can I do for you?")
                stage = 401
            }

            401 -> {
                options("Nothing thanks, I'm just looking around.", "Can you teleport me to the Rune Essence?")
                stage = 402
            }

            402 ->
                when (buttonId) {
                    1 -> {
                        player("Nothing thanks, I'm just looking around.")
                        stage = 403
                    }

                    2 -> {
                        player("Can you teleport me to the Rune Essence?")
                        stage = 405
                    }
                }

            403 -> {
                npc(
                    "Well, take care adventurer. You stand on the",
                    "ruins of the old destroyed Wizards' Tower.",
                    "Strange and powerful magicks lurk here,",
                )
                stage = 404
            }

            404 -> end()
            405 -> {
                end()
                teleport(npc, player)
            }

            900 -> {
                player("Yes, I have. He gave me some research notes", "to pass on to you.")
                stage = 901
            }

            901 -> {
                npc("May I have his notes then?")
                stage = 902
            }

            902 -> {
                if (!inInventory(player, NOTES)) {
                    player("Uh... I kind of... lost them...")
                    stage = 903
                    return false
                }
                player("Sure. I have them here.")
                stage = 905
            }

            905 -> {
                npc(
                    "Well, before you hand them over to me, as you",
                    "have been nothing but truthful with me to this point,",
                    "and I admire that in an adventurer, I will let you",
                    "into the secret of our research.",
                )
                stage = 906
            }

            906 -> {
                npc(
                    "Now as you may or may not know, many",
                    "centuries ago, the wizards at this Tower",
                    "learnt the secret of creating Rune Stones, which",
                    "allowed us to cast Magic very easily.",
                )
                stage = 907
            }

            907 -> {
                npc(
                    "When this Tower was burnt down the secret of",
                    "creating runes was lost to us for all time... except it",
                    "wasn't. Some months ago, while searching these ruins",
                    "for information from the old days,",
                )
                stage = 908
            }

            908 -> {
                npc(
                    "I came upon a scroll, almost destroyed, that detailed a",
                    "magical rock deep in the icefields of the North, closed",
                    "off from access by anything other than magical means.",
                )
                stage = 909
            }

            909 -> {
                npc(
                    "This rock was called the 'Rune Essence' by the",
                    "magicians who studied its power. Apparently, by simply",
                    "breaking a chunk from it, a Rune Stone could be",
                    "fashioned very quickly and easily at certain",
                )
                stage = 910
            }

            910 -> {
                npc(
                    "elemental altars that were scattered across the land",
                    "back then. Now, this is an interesting little piece of",
                    "history, but not much use to us as modern wizards",
                    "without access to the Rune Essence,",
                )
                stage = 911
            }

            911 -> {
                npc(
                    "or these elemental altars. This is where you and",
                    "Aubury come into this story. A few weeks back,",
                    "Aubury discovered in a standard delivery of runes",
                    "to his store, a parchment detailing a",
                )
                stage = 912
            }

            912 -> {
                npc(
                    "teleportation spell that he had never come across",
                    "before. To his shock, when cast it took him to a",
                    "strange rock he had never encountered before...",
                    "yet that he felt strangely familiar...",
                )
                stage = 913
            }

            913 -> {
                npc(
                    "As I'm sure you have now guessed, he had discovered a",
                    "portal leading to the mythical Rune Essence. As soon as",
                    "he told me of this spell, I saw the importance of his find,",
                )
                stage = 914
            }

            914 -> {
                npc(
                    "for it we could but find the elemental altars spoken",
                    "of in the ancient texts, we would once more be able",
                    "to create runes as our ancestors had done! It would",
                    "be the saviour of the wizards' art!",
                )
                stage = 915
            }

            915 -> {
                player("I'm still not sure how I fit into", "this little story of yours...")
                stage = 916
            }

            916 -> {
                npc(
                    "You haven't guessed? This talisman you brought me...",
                    "it is the key to the elemental altar of air! When",
                    "you hold it next, it will direct you towards",
                )
                stage = 917
            }

            917 -> {
                npc(
                    "the entrance to the long forgotten Air Altar! By",
                    "bringing pieces of the Rune Essence to the Air Temple,",
                    "you will be able to fashion your own Air Runes!",
                )
                stage = 918
            }

            918 -> {
                npc(
                    "And this is not all! By finding other talismans similar",
                    "to this one, you will eventually be able to craft every",
                    "rune that is available on this world! Just",
                )
                stage = 919
            }

            919 -> {
                npc(
                    "as our ancestors did! I cannot stress enough what a",
                    "find this is! Now, due to the risks involved of letting",
                    "this mighty power fall into the wrong hands",
                )
                stage = 920
            }

            920 -> {
                npc(
                    "I will keep the teleport skill to the Rune Essence",
                    "a closely guarded secret, shared only by myself",
                    "and those Magic users around the world",
                    "whom I trust enough to keep it.",
                )
                stage = 921
            }

            921 -> {
                npc(
                    "This means that if any evil power should discover",
                    "the talismans required to enter the elemental",
                    "temples, we will be able to prevent their access",
                    "to the Rune Essence and prevent",
                )
                stage = 922
            }

            922 -> {
                npc(
                    "tragedy befalling this world. I know not where the",
                    "temples are located, nor do I know where the talismans",
                    "have been scattered to in this land, but I now",
                )
                stage = 923
            }

            923 -> {
                npc(
                    "return your Air Talisman to you. Find the Air",
                    "Temple, and you will be able to charge your Rune",
                    "Essences to become Air Runes at will. Any time",
                )
                stage = 924
            }

            924 -> {
                npc(
                    "you wish to visit the Rune Essence, speak to me",
                    "or Aubury and we will open a portal to that",
                    "mystical place for you to visit.",
                )
                stage = 925
            }

            925 -> {
                player("So only you and Aubury know the teleport", "spell to the Rune Essence?")
                stage = 926
            }

            926 -> {
                npc(
                    "No... there are others... whom I will tell of your",
                    "authorisation to visit that place. When you speak",
                    "to them, they will know you, and grant you",
                    "access to that place when asked.",
                )
                stage = 927
            }

            927 -> {
                npc(
                    "Use the Air Talisman to locate the air temple,",
                    "and use any further talismans you find to locate",
                    "the other missing elemental temples.",
                    "Now... my research notes please?",
                )
                stage = 928
            }

            928 -> {
                sendItemDialogue(
                    player,
                    Items.AIR_TALISMAN_1438,
                    "You hand the head wizard the research notes. He hands you back the Air Talisman.",
                )
                stage = 929
            }

            929 -> {
                if (player.inventory.remove(Item(NOTES))) {
                    if (!player.inventory.add(Item(TALISMAN))) {
                        GroundItemManager.create(GroundItem(Item(TALISMAN), player.location, player))
                    }
                    finishQuest(player, Quests.RUNE_MYSTERIES)
                    player.getQuestRepository().syncronizeTab(player)
                }
                end()
            }

            903 -> {
                npc(
                    "You did? You are extremely careless aren't you? I",
                    "suggest you go and speak to Aubury once more, with",
                    "luck he will have made copies of his research.",
                )
                stage = 904
            }

            904 -> end()
            800 -> {
                player("Not yet...")
                stage = 801
            }

            801 -> {
                if (!inInventory(player, PACKAGE) && !inBank(player, PACKAGE)) {
                    player("...I lost the package you gave me.")
                    stage = 804
                    return false
                }
                npc(
                    "Well, please do so as soon as possible. Remember: to get",
                    "to Varrock, head due North, through Draynor Village,",
                    "around Draynor Manor, and then head East when",
                )
                stage = 802
            }

            802 -> {
                npc(
                    "you get to the Barbarian village. The man you seek",
                    "is named Aubury, and he owns the rune shop there.",
                    "It is vital he receives this package.",
                )
                stage = 803
            }

            803 -> end()
            804 -> {
                npc("You WHAT?")
                stage = 805
            }

            805 -> {
                npc(
                    "Tch, that was really very careless of you. Luckily as",
                    "head wizard I have great powers, and will be able to",
                    "teleport it back here without too much effort.",
                )
                stage = 806
            }

            806 -> {
                close()
                lock(player, 3)
                findLocalNPC(player!!, NPCs.SEDRIDOR_300)?.let { visualize(it, -1, Graphics) }
                player!!.pulseManager.run(
                    object : Pulse(1) {
                        override fun pulse(): Boolean {
                            npc(
                                "Ok, I have retrieved it. Luckily it doesn't appear to",
                                "have been damaged. Now please take it to Aubury, ",
                                "and try not to lose it again.",
                            )
                            stage = 807
                            return true
                        }
                    },
                )
            }

            807 -> {
                if (!player.inventory.add(Item(PACKAGE))) {
                    GroundItemManager.create(GroundItem(Item(PACKAGE), player.location, player))
                }
                end()
            }

            1000 ->
                when (buttonId) {
                    1 -> {
                        player("Nothing thanks, I'm just looking around.")
                        stage = 10
                    }

                    2 -> {
                        player("What are you doing down here?")
                        stage = 20
                    }

                    3 -> {
                        player("I'm looking for the head wizard.")
                        stage = 30
                    }
                }

            30 -> {
                npc("Oh, you are, are you?", "And just why would you be doing that?")
                stage = 31
            }

            31 -> {
                player(
                    "The Duke of Lumbridge sent me to find him. I have",
                    "this weird talisman he found. He said the head wizard",
                    "would be very interested in it.",
                )
                stage = 32
            }

            32 -> {
                npc(
                    "Did he now? HmmmMMMMMmmmmm.",
                    "Well that IS interesting. Hand it over then adventurer,",
                    "let me see what all the hubbub about it is.",
                    "Just come amulet I'll wager.",
                )
                stage = 33
            }

            33 -> {
                options("Ok, here you are.", "No, I'll only give it to the head wizard.")
                stage = 34
            }

            34 ->
                when (buttonId) {
                    1 -> {
                        player("Ok, here you are.")
                        stage = 50
                    }

                    2 -> {
                        player("No, I'll only give it to the head wizard.")
                        stage = 40
                    }
                }

            50 -> {
                if (!inInventory(player, TALISMAN)) {
                    player("...except I don't have it with me...")
                    stage = 99
                    return true
                }
                sendDialogue("You hand the Talisman to the wizard.")
                stage = 51
            }

            51 ->
                if (removeItem(player, Item(TALISMAN))) {
                    setQuestStage(player, Quests.RUNE_MYSTERIES, 20)
                    npc("Wow! This is... incredible!")
                    stage = 52
                }

            52 -> {
                npc(
                    "Th-this talisman you brought me...! It is the last piece",
                    "of the puzzle, I think! Finally! The legacy of our",
                    "ancestors... it will return to us once more!",
                )
                stage = 53
            }

            53 -> {
                npc(
                    "I need time to study this, " + player.username + ". Can you please",
                    "do me this task while I study this talisman you have",
                    "brought me? In the mighty town of Varrock, which",
                )
                stage = 54
            }

            54 -> {
                npc(
                    "is located North East of here, there is a certain shop",
                    "that sells magical runes. I have in this package all of the",
                    "research I have done relating to the Rune Stones, and",
                )
                stage = 55
            }

            55 -> {
                npc(
                    "require somebody to take them to the shopkeeper so that",
                    "he may share my research and offer me his insights.",
                    "Do this thing for me, and bring back what he gives you,",
                )
                stage = 56
            }

            56 -> {
                npc(
                    "and if my suspicions are correct, I will let you into the",
                    "knowledge of one of the greatest secrets this world has",
                    "ever known! A secret so powerful that it destroyed the",
                )
                stage = 57
            }

            57 -> {
                npc(
                    "original Wizards' Tower all of those centuries",
                    "ago! My research, combined with this mysterious",
                    "talisman... I cannot believe the answer",
                    "the mysteries is so close now!",
                )
                stage = 58
            }

            58 -> {
                npc("Do this thing for me " + player.username + ". Be rewarded in a", "way you can never imagine.")
                stage = 59
            }

            59 -> {
                options("Yes, certainly.", "No, I'm busy.")
                stage = 60
            }

            60 ->
                when (buttonId) {
                    1 -> {
                        player("Yes, certainly.")
                        stage = 70
                    }

                    2 -> {
                        player("No, I'm busy.")
                        stage = 61
                    }
                }

            70 -> {
                npc(
                    "Take this package, and head directly North",
                    "from here, through Draynor village, until you reach",
                    "the Barbarian Village. Then head East from there",
                    "until you reach Varrock.",
                )
                stage = 71
            }

            71 -> {
                npc(
                    "Once in Varrock, take this package to the owner of the",
                    "rune shop. His name is Aubury. You may find it",
                    "helpful to ask one of Varrock's citizens for directions,",
                )
                stage = 72
            }

            72 -> {
                npc(
                    "as Varrock can be a confusing place for the first time",
                    "visitor. He will give you a special item - bring it back to",
                    "me, and I shall show you the mystery of the runes...",
                )
                stage = 73
            }

            73 -> {
                sendDialogue("The head wizard gives you a package.")
                stage = 74
            }

            74 -> {
                setQuestStage(player, Quests.RUNE_MYSTERIES, 30)
                npc("Best of luck with your quest, " + player.username + ".")
                if (!player.inventory.add(Item(PACKAGE))) {
                    GroundItemManager.create(GroundItem(Item(PACKAGE), player.location, player))
                }
                stage = 75
            }

            75 -> end()
            61 -> {
                npc(
                    "As you wish adventurer. I will continue to study this",
                    "talisman you have brought me. Return here if you find",
                    "yourself with some spare time to help me.",
                )
                stage = 62
            }

            62 -> end()
            99 -> end()
            40 -> {
                npc(
                    "HA HA HA HA HA! I can tell you are new to this",
                    "land, for I AM the head wizard! Hand it over and",
                    "let me have a proper look at it, hmmm?",
                )
                stage = 41
            }

            41 -> {
                options("Ok, here you are.", "No, I'll only give it to the head wizard.")
                stage = 34
            }

            1 ->
                when (buttonId) {
                    1 -> {
                        player("Nothing thanks, I'm just looking around.")
                        stage = 10
                    }

                    2 -> {
                        player("What are you doing down here?")
                        stage = 20
                    }
                }

            10 -> {
                npc(
                    "Well, take care adventurer. You stand on the",
                    "ruins of the destroyed Wizards' Tower.",
                    "Strange and powerful magicks lurk here.",
                )
                stage = 11
            }

            11 -> end()
            20 -> {
                npc(
                    "That is indeed a good question. Here in the cellar",
                    "of the Wizards' Tower you find the remains of",
                    "the old Wizards' Tower, destroyed by fire",
                )
                stage = 21
            }

            21 -> {
                npc(
                    "many years past by the treachery of the Zamorakians.",
                    "Many mysteries were lost, which we try to find once",
                    "more. By building this tower on the remains of the old,",
                )
                stage = 22
            }

            22 -> {
                npc(
                    "we sought to show the world of our dedication to",
                    "learning the mysteries of Magic. I am here searching",
                    "through these fragments for knowledge from",
                    "the artefacts from our past.",
                )
                stage = 23
            }

            23 -> {
                player("And have you found anything useful?")
                stage = 24
            }

            24 -> {
                npc(
                    "Aaah... that would be telling adventurer. Anything I",
                    "have found I cannot speak freely of, for fear the",
                    "treachery of the past might be repeated.",
                )
                stage = 25
            }

            25 -> {
                player("Ok, well I'll leave you to it.")
                stage = 26
            }

            26 -> {
                npc("Perhaps I will see you later " + player.username + ".")
                stage = 27
            }

            27 -> {
                player("How did you know my name???")
                stage = 28
            }

            28 -> {
                npc("Well, I AM the head wizard here...")
                stage = 29
            }

            29 -> end()
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return SedridorDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SEDRIDOR_300)
    }

    companion object {
        private const val TALISMAN = Items.AIR_TALISMAN_1438
        private const val PACKAGE = Items.RESEARCH_PACKAGE_290
        private const val NOTES = Items.NOTES_291
        private val Graphics = Graphics(6)
    }
}
