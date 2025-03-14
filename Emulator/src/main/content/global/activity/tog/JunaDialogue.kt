package content.global.activity.tog

import content.data.GameAttributes
import content.region.kandarin.miniquest.barcrawl.BarcrawlManager
import content.region.misthalin.quest.tog.TearsOfGuthix
import core.api.*
import core.api.quest.finishQuest
import core.api.quest.getQuestStage
import core.api.quest.isQuestComplete
import core.api.quest.setQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.splitLines
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests
import org.rs.consts.Vars

@Initializable
class JunaDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        val daysLeft = TearsOfGuthix.daysLeft(player) > 0
        val xpLeft = TearsOfGuthix.xpLeft(player) > 0
        val questPointsLeft = TearsOfGuthix.questPointsLeft(player) > 0
        val questStage = getQuestStage(player, Quests.TEARS_OF_GUTHIX)

        when {
            daysLeft && xpLeft && questPointsLeft -> {
                npcl(FaceAnim.OLD_DEFAULT, "You must wait longer before entering the Tears of Guthix cave.")
                stage = END_DIALOGUE
            }

            xpLeft && questPointsLeft -> {
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "You need more experience and quest points before entering the Tears of Guthix cave."
                )
                stage = END_DIALOGUE
            }

            daysLeft -> {
                npcl(FaceAnim.OLD_DEFAULT, "You must wait longer before entering the Tears of Guthix cave.")
                stage = END_DIALOGUE
            }

            else -> {
                npcl(FaceAnim.OLD_DEFAULT, "You may now enter the Tears of Guthix cave.")
            }
        }

        when {
            !isQuestComplete(player, Quests.TEARS_OF_GUTHIX) && questStage < 1 -> {
                npc(FaceAnim.OLD_DEFAULT, "Tell me... a story...").also { stage = 3 }
            }

            questStage >= 1 && inInventory(player, Items.STONE_BOWL_4704) -> {
                options("Okay...", "A story?", "Not now", "You tell me a story").also { stage = 19 }
            }

            questStage >= 1 -> {
                npc(FaceAnim.OLD_DEFAULT,
                    "Before you can collect the Tears of Guthix you must",
                    "make a bowl out of the stone in the cave on the south",
                    "of the chasm."
                ).also { stage = 25 }
            }
        }

        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        val questDialogue = mutableListOf<String>()
        when (stage) {
            0 -> {
                if (getVarbit(player, 492) >= 4) {
                    questDialogue.add(0, "I gained access to the Abyss.")
                    questDialogue.add(1, "...")
                }
                if (isQuestComplete(player, Quests.ALL_FIRED_UP)) {
                    questDialogue.add(0, "...And that was how I learned to maintain King Roald's watchtower beacons.")
                    questDialogue.add(1, "...")
                }
                if (BarcrawlManager.getInstance(player).isFinished) {
                    questDialogue.add(
                        0,
                        "I completed my bar crawl card and earned access to the Barbarian Outpost agility course.",
                    )
                    questDialogue.add(1, "...")
                }
                if (isQuestComplete(player, Quests.BLACK_KNIGHTS_FORTRESS)) {
                    questDialogue.add(0, "...in the end, the Black Knights were defeated by a cabbage!")
                    questDialogue.add(1, "One should never underestimate the vegetables of Guthix.")
                }
                if (isQuestComplete(player, Quests.COOKS_ASSISTANT)) {
                    questDialogue.add(
                        0,
                        "...And, in the end, I found all the ingredients, so the Duke of Lumbridge had a birthday cake after all.",
                    )
                    questDialogue.add(
                        1,
                        "Ah, a happy ending. It would not be good for such an anniversary to go unmarked.",
                    )
                }
                if (isQuestComplete(player, Quests.DEMON_SLAYER)) {
                    questDialogue.add(0, "...so I destroyed the demon Delrith and saved Varrock!")
                    questDialogue.add(
                        1,
                        "I remember Delrith. A most unpleasant character; I am glad he has been dispatched.",
                    )
                }
                if (isQuestComplete(player, Quests.DORICS_QUEST)) {
                    questDialogue.add(0, "...so once I had got all the ores he wanted, Doric let me use his anvils.")
                    questDialogue.add(1, "Such a small task hardly seems worthy of the term 'quest'.")
                }
                if (isQuestComplete(player, Quests.DRAGON_SLAYER)) {
                    questDialogue.add(
                        0,
                        "...so with Elvarg the dragon dead, the master of the Champions' Guild let me in, and I was able to wear rune plate armour!",
                    )
                    questDialogue.add(
                        1,
                        "I remember Delrith. A most unpleasant character; I am glad he has been dispatched.",
                    )
                }
                if (isQuestComplete(player, Quests.DORICS_QUEST)) {
                    questDialogue.add(0, "...so once I had got all the ores he wanted, Doric let me use his anvils.")
                    questDialogue.add(1, "Such a small task hardly seems worthy of the term 'quest'.")
                }
                if (isQuestComplete(player, Quests.DRAGON_SLAYER)) {
                    questDialogue.add(
                        0,
                        "...so with Elvarg the dragon dead, the master of the Champions' Guild let me in, and I was able to wear rune plate armour!",
                    )
                    questDialogue.add(1, "...")
                }
                if (isQuestComplete(player, Quests.DRUIDIC_RITUAL)) {
                    questDialogue.add(0, "...so Kaqemeex taught me how to use the Herblore skill.")
                    questDialogue.add(1, "A generous reward indeed.")
                }
                if (isQuestComplete(player, Quests.DWARF_CANNON)) {
                    questDialogue.add(0, "...and that was how I fixed the dwarf multicannon.")
                    questDialogue.add(
                        1,
                        "So, war still rages in the world above? Will you never tire of creating machines of destruction?",
                    )
                }
                if (isQuestComplete(player, Quests.ERNEST_THE_CHICKEN)) {
                    questDialogue.add(
                        0,
                        "...So, once I had found all the parts for the machine, poor Ernest could be himself once more.",
                    )
                    questDialogue.add(
                        1,
                        "That was a good deed. It is a terrible thing to be locked out of one's natural form.",
                    )
                }
                if (isQuestComplete(player, Quests.FAMILY_CREST)) {
                    questDialogue.add(0, "...So all three parts of the family crest were reunited.")
                    questDialogue.add(1, "...")
                }
                if (isQuestComplete(player, Quests.FISHING_CONTEST)) {
                    questDialogue.add(
                        0,
                        "...And after I had won the fishing contest, the Dwarves let me go under White Wolf Mountain.",
                    )
                    questDialogue.add(1, "Fishing? A strange test of worthiness to pass through an underground tunnel!")
                }
                if (isQuestComplete(player, Quests.THE_FREMENNIK_TRIALS)) {
                    questDialogue.add(
                        0,
                        "...And that was how I became an honourary member of the Fremennik, and was given my Fremennik name, ${
                            getAttribute(
                                player,
                                "fremennikname",
                                "fremmyname",
                            )
                        }.",
                    )
                    questDialogue.add(1, "...")
                }
                if (isQuestComplete(player, Quests.GERTRUDES_CAT)) {
                    questDialogue.add(0, "...I returned Fluffs safely to Gertrude and she gave me a cat of my own!")
                    questDialogue.add(
                        1,
                        "Cats are one of the most mysterious creatures of Guthix. I hope you take your responsibility seriously.",
                    )
                }
                if (isQuestComplete(player, Quests.GOBLIN_DIPLOMACY)) {
                    questDialogue.add(
                        0,
                        "...and so the goblins ended up wearing the armour colour they had to start off with!",
                    )
                    questDialogue.add(
                        1,
                        "Poor, silly goblins! Their race had such potential; if only they could rise above their petty squabbles.",
                    )
                }
                if (isQuestComplete(player, Quests.IMP_CATCHER)) {
                    questDialogue.add(
                        0,
                        "...took some time, but I finally got all four beads back, and Mizgog gave me my reward.",
                    )
                    questDialogue.add(
                        1,
                        "Imps! I remember the age of great war, when armies of Zamorak's imps bloodied the ankles of the other gods' creatures.",
                    )
                }
                if (isQuestComplete(player, Quests.JUNGLE_POTION)) {
                    questDialogue.add(
                        0,
                        "...and once I had gathered all the herbs, Trufitus Shakaya was able to commune with his gods.",
                    )
                    questDialogue.add(1, "...")
                }
                if (isQuestComplete(player, Quests.LOST_CITY)) {
                    questDialogue.add(
                        0,
                        "...And when I entered the door carrying the Dramen Staff, I was transported to a whole new world - a world populated by magical fairies!",
                    )
                    questDialogue.add(1, "...")
                }
                if (isQuestComplete(player, Quests.THE_LOST_TRIBE)) {
                    questDialogue.add(
                        0,
                        "...Sigmund was dismissed, and the duke and the ruler of the cave goblins signed a peace treaty.",
                    )
                    questDialogue.add(
                        1,
                        "The Dorgueshuun goblins have been good neighbours during my vigil here. They are a timid race, but not cowardly, and I am glad they have the peace they desire.",
                    )
                }
                if (isQuestComplete(player, Quests.MERLINS_CRYSTAL)) {
                    questDialogue.add(
                        0,
                        "...and when I told King Arthur that I had single-handedly freed Merlin from his crystal prison, he made me a knight of the Round Table!",
                    )
                    questDialogue.add(1, "...")
                }
                if (isQuestComplete(player, Quests.MONKS_FRIEND)) {
                    questDialogue.add(0, "...so the monks had a drunken party to celebrate the child's birthday.")
                    questDialogue.add(
                        1,
                        "Mirth should not be taken to excess. These Saradominists have never understood the importance of balance.",
                    )
                }
                if (isQuestComplete(player, Quests.NATURE_SPIRIT)) {
                    questDialogue.add(
                        0,
                        "...and that was how I helped Filliman Tarlock transform into a nature spirit.",
                    )
                    questDialogue.add(1, "It is good that you helped a servant of Guthix to his rest.")
                }
                if (isQuestComplete(player, Quests.PIRATES_TREASURE)) {
                    questDialogue.add(
                        0,
                        "...And when I dug in the middle of the park in Falador, I found the pirate's treasure!",
                    )
                    questDialogue.add(
                        1,
                        "Such is ever the folly of pirates, to bury their loot in the ground so that another can dig it up.",
                    )
                }
                if (isQuestComplete(player, Quests.PRIEST_IN_PERIL)) {
                    questDialogue.add(0, "...but with Drezel's help I was able to purify the River Salve.")
                    questDialogue.add(1, "...")
                }
                if (isQuestComplete(player, Quests.PRINCE_ALI_RESCUE)) {
                    questDialogue.add(0, "...And I had to disguise Prince Ali as a woman in order to smuggle him out!")
                    questDialogue.add(1, "...")
                }
                if (isQuestComplete(player, Quests.ROMEO_JULIET)) {
                    questDialogue.add(
                        0,
                        "...I gave the message to Romeo, but he misunderstood, so they never were together.",
                    )
                    questDialogue.add(1, "Never was there a story of more woe that this of Juliet and her Romeo.")
                }
                if (isQuestComplete(player, Quests.ROVING_ELVES)) {
                    questDialogue.add(0, "...and when I planted the shard it grew into a crystal tree!")
                    questDialogue.add(1, "...")
                }
                if (isQuestComplete(player, Quests.RUNE_MYSTERIES)) {
                    questDialogue.add(
                        0,
                        "...So, I brought Aubury's notes to Sedridor the head wizard and, from then on, I was able to mine rune essence.",
                    )
                    questDialogue.add(1, "So, the mortals above have discovered magic once more? Very interesting.")
                }
                if (isQuestComplete(player, Quests.SHEEP_HERDER)) {
                    questDialogue.add(0, "...so the sheep were safely killed and incinerated.")
                    questDialogue.add(1, "...")
                }
                if (isQuestComplete(player, Quests.SHEEP_SHEARER)) {
                    questDialogue.add(
                        0,
                        "Out of sheer hard work, I managed to shear some sheep for farmer Fred. Then I got in a spin and created some balls of wool.",
                    )
                    questDialogue.add(1, "...")
                }
                if (isQuestComplete(player, Quests.SHIELD_OF_ARRAV)) {
                    questDialogue.add(0, "...so that's how I helped return the Shield of Arrav.")
                    questDialogue.add(1, "...")
                }
                if (isQuestComplete(player, Quests.THE_KNIGHTS_SWORD)) {
                    questDialogue.add(0, "...that was how I found the Imcando Dwarves and got the knight a new sword.")
                    questDialogue.add(1, "...")
                }
                if (isQuestComplete(player, Quests.THE_RESTLESS_GHOST)) {
                    questDialogue.add(0, "...and once I returned the skull, the ghost was able to rest.")
                    questDialogue.add(1, "A strange attachment to an item that has no use after one's death.")
                }
                if (isQuestComplete(player, Quests.THE_TOURIST_TRAP)) {
                    questDialogue.add(
                        0,
                        "...Ana wasn't too happy to be cooped up in that barrel! But at least I got her out of the mining camp.",
                    )
                    questDialogue.add(1, "...")
                }
                if (isQuestComplete(player, Quests.VAMPIRE_SLAYER)) {
                    questDialogue.add(
                        0,
                        "...And once the vampyre was dead, the people of Draynor no longer lived in fear.",
                    )
                    questDialogue.add(1, "...")
                }
                if (isQuestComplete(player, Quests.WATERFALL_QUEST)) {
                    questDialogue.add(0, "...And that was how I retrieved the Chalice of Eternity from the waterfall.")
                    questDialogue.add(1, "...")
                }
                if (isQuestComplete(player, Quests.WHAT_LIES_BELOW)) {
                    questDialogue.add(
                        0,
                        "...but after I defeated King Roald, Rat was able to remove Surok's mind control spell from him.",
                    )
                    questDialogue.add(
                        1,
                        "I see the servants of Zamorak still play their part in balancing the world between good and evil.",
                    )
                }
                if (isQuestComplete(player, Quests.WITCHS_HOUSE)) {
                    questDialogue.add(0, "...All that trouble just to get a ball out of someone's garden!")
                    questDialogue.add(1, "It is often hard to know how long a task will take when we begin it.")
                }
                if (isQuestComplete(player, Quests.WITCHS_POTION)) {
                    questDialogue.add(
                        0,
                        "...And once I got her all the ingredients, Hetty's potion increased my magical power!",
                    )
                    questDialogue.add(1, "I see you are on your way to becoming strong in the magical arts.")
                }
                if (isQuestComplete(player, Quests.WOLF_WHISTLE)) {
                    questDialogue.add(0, "...And that was how I learned the secrets of summoning.")
                    questDialogue.add(1, "...")
                }
                if (isQuestComplete(player, Quests.BIG_CHOMPY_BIRD_HUNTING)) {
                    questDialogue.add(
                        0,
                        "...poor Rantz was so clumsy he couldn't shoot anything, but I managed to kill the Chompy bird.",
                    )
                    questDialogue.add(1, "...")
                }
                if (isQuestComplete(player, Quests.CREATURE_OF_FENKENSTRAIN)) {
                    questDialogue.add(
                        0,
                        "...But in the end I stopped Fenkenstrain from continuing his horrible experiments.",
                    )
                    questDialogue.add(1, "...")
                }
                if (isQuestComplete(player, Quests.ENLIGHTENED_JOURNEY)) {
                    questDialogue.add(
                        0,
                        "...we landed the balloon in Taverley and Auguste thanked me for helping him get his idea off the ground.",
                    )
                    questDialogue.add(1, "...")
                }
                if (isQuestComplete(player, Quests.GARDEN_OF_TRANQUILLITY)) {
                    questDialogue.add(0, "...so the garden at Varrock palace is all my work!")
                    questDialogue.add(
                        1,
                        "It is good that you can take time from slaying and adventuring to grow a beautiful garden.",
                    )
                }
                if (isQuestComplete(player, Quests.HORROR_FROM_THE_DEEP)) {
                    questDialogue.add(0, "...And that was the end of the horror from the deep!")
                    questDialogue.add(1, "...")
                }
                if (isQuestComplete(player, Quests.MURDER_MYSTERY)) {
                    questDialogue.add(0, "...And that's how I solved the murder of Lord Sinclair.")
                    questDialogue.add(1, "...")
                }
                if (isQuestComplete(player, Quests.SHEEP_SHEARER)) {
                    questDialogue.add(
                        0,
                        "...I would never have thought that three scorpions would take that long to find!",
                    )
                    questDialogue.add(1, "...")
                }
                if (isQuestComplete(player, Quests.TEMPLE_OF_IKOV)) {
                    questDialogue.add(
                        0,
                        "...Lucien said that the Staff of Armadyl I gave him had made him more powerful.",
                    )
                    questDialogue.add(1, "...")
                }
                if (isQuestComplete(player, Quests.TEMPLE_OF_IKOV) && getAttribute(
                        player, GameAttributes.QUEST_IKOV_SELECTED_END, 0
                    ) == 1
                ) {
                    questDialogue.add(0, "I refused to give Lucien the Staff of Armadyl and had to fight him.")
                    questDialogue.add(1, "...")
                }
                if (isQuestComplete(player, Quests.THE_DIG_SITE)) {
                    questDialogue.add(
                        0,
                        "...And the examiner was very impressed that I had discovered an ancient altar of Zaros.",
                    )
                    questDialogue.add(
                        1,
                        "Zaros? I had not heard that name for a thousand years, even before the start of my sojourn here.",
                    )
                }
                if (isQuestComplete(player, Quests.TOWER_OF_LIFE)) {
                    questDialogue.add(
                        0,
                        "...So, it turned out that the alchemists had created a Homunculus - a mixture of logic and magic.",
                    )
                    questDialogue.add(
                        1,
                        "Ah, that is good. New forms of life should always be created according to a balance between different principles.",
                    )
                }
                if (isQuestComplete(player, Quests.TREE_GNOME_VILLAGE)) {
                    questDialogue.add(
                        0,
                        "...And King Bolren thanked me for defeating the warlord and returning the orbs to the gnome people.",
                    )
                    questDialogue.add(1, "...")
                }
                player(FaceAnim.HAPPY, *splitLines(questDialogue[0])).also { stage = 1 }
            }

            1 -> npc(FaceAnim.OLD_DEFAULT, "...").also { stage = 21 }
            3 -> player("A story?").also { stage++ }
            4 -> npc(
                FaceAnim.OLD_DEFAULT,
                "I have been waiting here three thousand years,",
                "guarding the Tears of Guthix. I serve my",
                "master faithfully, but I am bored.",
            ).also {
                stage++
            }

            5 -> npc(
                FaceAnim.OLD_DEFAULT,
                "An adventurer such as yourself must have many tales",
                "to tell. If you can entertain me, I will let you into the",
                "cave for a time.",
            ).also {
                stage++
            }

            6 -> npc(
                FaceAnim.OLD_DEFAULT,
                "The more I enjoy your story, the more time I will give",
                "you in the cave.",
            ).also {
                stage++
            }

            7 -> npc(
                FaceAnim.OLD_DEFAULT,
                "Then you can drink of the power of balance, which will",
                "make you stronger in whatever area you are weakest.",
            ).also {
                stage++
            }

            8 -> options("Okay...", "Not now.", "What are the Tears of Guthix?").also { stage++ }
            9 -> when (buttonId) {
                1 -> player("Okay...").also { stage = 20 }
                2 -> player("Not now.").also { stage = END_DIALOGUE }
                3 -> player("What are the Tears of Guthix?").also { stage++ }
            }

            10 -> npc(
                FaceAnim.OLD_DEFAULT,
                "The Third Age of the world was a time of great",
                "conflict, of destruction never seen before or since, when",
                "all the gods save Guthix warred for control.",
            ).also {
                stage++
            }

            11 -> npc(
                FaceAnim.OLD_DEFAULT,
                "The colossal wyrms, of whom today's dragons are a",
                "pale reflection, turned all the sky to fire, while on the",
                "ground armies of foot soldiers, goblins and trolls and",
                "humans, filled the valleys and plains with blood.",
            ).also {
                stage++
            }

            12 -> npc(
                FaceAnim.OLD_DEFAULT,
                "In time the noise of the conflict woke Guthix from His",
                "deep slumber, and He rose and stood in the centre of",
                "the battlefield so that the splendour of His wrath filled",
                "the world, and He called for the conflict to cease!",
            ).also {
                stage++
            }

            13 -> npc(
                FaceAnim.OLD_DEFAULT,
                "Silence fell, for the gods knew that none could challenge",
                "the power of the mighty Guthix; for His power is that",
                "of nature itself, to which all other things are subject, in",
                "the end.",
            ).also {
                stage++
            }

            14 -> npc(
                FaceAnim.OLD_DEFAULT,
                "Guthix reclaimed that which had been stolen from Him,",
                "and went back underground to return to His sleep and",
                "continue to draw the world's power into Himself.",
            ).also {
                stage++
            }

            15 -> npc(
                FaceAnim.OLD_DEFAULT,
                "But on His way into the depths of the earth He sat and",
                "rested in this cave; and, thinking of the battle-scarred",
                "desert that now stretched from one side of His world to",
                "the other, He wept.",
            ).also {
                stage++
            }

            16 -> npc(
                FaceAnim.OLD_DEFAULT,
                "And so great was His sorrow, and so great was His life- ",
                "giving power, that the rocks themselves began to weep",
                "with Him.",
            ).also {
                stage++
            }

            17 -> npc(
                FaceAnim.OLD_DEFAULT,
                "Later, Guthix noticed that the rocks continued to weep,",
                "and that their tears were infused with a small part of",
                "His power.",
            ).also {
                stage++
            }

            18 -> {
                npc(
                    FaceAnim.OLD_NORMAL,
                    "So He set me, His servant, to guard the cave, and He",
                    "entrusted to me the task of judging who was and was",
                    "not worthy to access the tears.",
                )
                setQuestStage(player, Quests.TEARS_OF_GUTHIX, 1)
                stage = END_DIALOGUE
            }

            19 -> when (buttonId) {
                1 -> player("Okay...").also { stage++ }
                2 -> player("A story?").also { stage = 4 }
                3 -> player("Not now.").also { stage = END_DIALOGUE }
                4 -> player("You tell me a story").also { stage = 10 }
            }

            20 -> sendDialogue(player, "You tell Juna some stories of your adventures.").also { stage = 0 }
            21 -> npc(
                FaceAnim.OLD_DEFAULT,
                "Your stories have entertained me. I will let you into",
                "the cave for a short time.",
            ).also {
                stage++
            }

            22 -> if (!inInventory(player, Items.STONE_BOWL_4704)) {
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "But first you will need to make a bowl in which to",
                    "collect the tears.",
                ).also { stage++ }
            } else {
                player("I have a bowl.").also { stage = 26 }
            }

            23 -> {
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "There is a cave on the south side of the chasm that is",
                    "similarly infused with the power of Guthix. The stone in",
                    "that cave is the only substance that can catch the Tears",
                    "of Guthix.",
                ).also {
                    stage++
                }
            }

            24 -> {
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "Mine some stone from that cave, make it into a bowl,",
                    "and bring it to me, and then I will let you catch the tears.",
                )
                setQuestStage(player, Quests.TEARS_OF_GUTHIX, 1)
                stage = END_DIALOGUE
            }

            25 -> player("I have a bowl.").also { stage++ }

            26 -> if (!isQuestComplete(player, Quests.TEARS_OF_GUTHIX)) {
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "I will keep your bowl for you, so that you may collect",
                    "the tears many times in the future.",
                ).also {
                    stage++
                }
            } else {
                if (!hasHandsFree(player)) {
                    npc(
                        FaceAnim.OLD_NORMAL,
                        "But you must have both hands free to carry the bowl.",
                        "Speak to me again when your hands are free."
                    ).also { stage = END_DIALOGUE }
                } else {
                    npc(
                        FaceAnim.OLD_NORMAL,
                        "Collect as much as you can from the blue streams. If",
                        "you let in water from the green streams, it will take",
                        "away from the blue. For Guthix is god of balance, and",
                        "balance lies in the juxtaposition of opposites."
                    ).also { stage = 28 }
                }
            }

            27 -> {
                end()
                removeItem(player, Items.STONE_BOWL_4704)
                finishQuest(player, Quests.TEARS_OF_GUTHIX)
            }

            28 -> {
                end()
                TearsOfGuthixActivity.startGame(player)
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.JUNA_2023)
    }
}
