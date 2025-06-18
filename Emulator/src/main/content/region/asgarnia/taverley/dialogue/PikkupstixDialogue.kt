package content.region.asgarnia.taverley.dialogue

import core.api.*
import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class PikkupstixDialogue(player: Player? = null) : Dialogue(player) {

    private var quest: Quest? = null
    private val ITEMS = arrayOf(Item(Items.SUMMONING_CAPE_12169), Item(Items.SUMMONING_CAPET_12170), Item(Items.SUMMONING_HOOD_12171))
    private val BONES = Item(Items.WOLF_BONES_2859, 2)
    private val WOLF_ITEMS = arrayOf(Item(Items.GOLD_CHARM_12158, 2), Item(Items.POUCH_12155, 2), Item(Items.SPIRIT_SHARDS_12183, 14), Item(Items.TRAPDOOR_KEY_12528))
    private val HOWL_SCROLL = Item(Items.HOWL_SCROLL_12425)
    private val WOLF_POUCH = Item(Items.SPIRIT_WOLF_POUCH_12047)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        quest = player.getQuestRepository().getQuest(Quests.WOLF_WHISTLE)
        when (quest!!.getStage(player)) {
            0 -> npc(FaceAnim.FRIENDLY,"You there! What are you doing here, as if I didn't have", "enough troubles?")
            10 -> {
                npc(FaceAnim.HALF_ASKING,"Have you met the monster upstairs?")
                stage = 0
            }

            20 -> {
                player(FaceAnim.SCARED,"The teeth!")
                stage = 0
            }

            30 -> {
                if (!player.inventory.containsItem(BONES)) {
                    npc("You need to bring two lots of wolf bones. I can provide", "the other items you will need.")
                    stage = 0
                } else {
                    player("I have the wolf bones right here.")
                    stage = 1
                }
            }

            40 -> {
                if (player.inventory.containsItem(WOLF_POUCH) && player.inventory.containsItem(HOWL_SCROLL)) {
                    player("Here is the pouch and scroll.")
                    stage = 1
                    return true
                }
                if (!player.inventory.contains(12528, 1) && !player.bank.contains(12528, 1) && !player.getAttribute("has-key", false)) {
                    player.inventory.add(Item(12528, 1), player)
                }
                if (!player.inventory.containsItem(WOLF_POUCH) || !player.inventory.containsItem(HOWL_SCROLL)) {
                    npc("You need to bring me a wolf pouch and a howl scroll.")
                    stage = 0
                }
            }

            50 -> {
                if (!player.inventory.containsItem(WOLF_POUCH) || !player.inventory.containsItem(HOWL_SCROLL)) {
                    for (i in WOLF_ITEMS) {
                        if (i.id == 12183) {
                            continue
                        }
                        if (!player.inventory.containsItem(i) && !player.bank.containsItem(i) && player.getAttribute<Any?>("taken-summoning-supplies") == null) {
                            if (i.id == 12528 && getVarp(player, 1178) == (2 shl 11)) {
                                continue
                            }
                            setAttribute(player, "taken-summoning-supplies", true)
                            player.inventory.add(i, player)
                        }
                    }
                }
                npc("Hurry up! Go remove the wolpertinger at last!")
                stage = END_DIALOGUE
            }

            60 -> if (player.getSkills().getLevel(Skills.SUMMONING) < player.getSkills().getStaticLevel(Skills.SUMMONING)) {
                npc(FaceAnim.HALF_ASKING,"So, how did it go?")
            } else if (player.getSkills().getLevel(Skills.SUMMONING) >= player.getSkills().getStaticLevel(Skills.SUMMONING)) {
                npc(FaceAnim.HALF_ASKING,"Feeling refreshed?")
                stage = 20
            }

            100 -> npc("Welcome to my humble abode. How can I help", "you?")
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (quest!!.getStage(player)) {
            0 -> when (stage) {
                0 -> player(FaceAnim.HALF_ASKING,"I'm just a passing adventurer. Who are you?").also { stage++ }
                1 -> npc("Who am I? My dear boy, I just so happen to be one", "of the most important druids in this village!").also { stage++ }
                2 -> player("Wow, I don't meet that many celebrities.").also { stage++ }
                3 -> player(FaceAnim.HALF_ASKING,"What do you do here?").also { stage++ }
                4 -> npc("Well, firstly, I-").also { stage++ }
                5 -> sendDialogue("There is a loud crash from upstairs, followed by the tinkling of", "broken glass.").also { stage++ }
                6 -> npc(FaceAnim.FURIOUS, "Confound you, lapine menace!").also { stage++ }
                7 -> player(FaceAnim.HALF_ASKING,"What was that?").also { stage++ }
                8 -> npc("That was the sound of my carefully arranged room", "being destroyed by a fluffy typhoon of wickedness!").also { stage++ }
                9 -> player(FaceAnim.HALF_ASKING,"What?").also { stage++ }
                10 -> player("Wait, I'm good with monsters. Do you want me to go", "kill it for you?").also { stage++ }
                11 -> npc("Well, yes, but it's a little more complicated than that.").also { stage++ }
                12 -> player(FaceAnim.HALF_ASKING,"How complicated can it be?").also { stage++ }
                13 -> sendDialogue(player, "A resounding crash comes from upstairs.").also { stage++ }
                14 -> npc("Complicated enough for me to waste time explaining it", "to you.").also { stage++ }
                15 -> npc("Tell me, have you ever heard of Summoning?").also { stage++ }
                16 -> player("Not really.").also { stage++ }
                17 -> npc("Well, some of the concepts I am about to discuss might", "go over your head, so I'll stick to the basics.").also { stage++ }
                18 -> interpreter.sendDialogue("There is a series of loud crunches from upstairs.").also { stage++ }
                19 -> interpreter.sendDialogues(npc, FaceAnim.FURIOUS, "Gah! That better not be my wardrobe!").also { stage++ }
                20 -> npc("Summoners are able to call upon animal familiars for a", "number of uses. These familiars can help the summoner", "practice their skills, fight on the battlefield, or offer any", "number of other benefits.").also { stage++ }
                21 -> npc("Beneath this house is a monument - an obelisk - that", "allows us druids to trap this Summoning power.").also { stage++ }
                22 -> interpreter.sendDialogue("A loud gnawing begins upstairs.").also { stage++ }
                23 -> npc("I chased it upstairs, but I didn't have the necessary", "Summoning pouch to banish it.").also { stage++ }
                24 -> npc("I sent my assistant, Stikklebrix, out to bring me what I", "needed, but he was not returned.").also { stage++ }
                25 -> npc("If I leave this house, it will come down here, potentially", "even getting to the obelisk. If that happens, it could call", "another familiar through and multiply.").also { stage++ }
                26 -> interpreter.sendDialogue("The gnawing stops. There is a crash.").also { stage++ }
                27 -> player("How fast can it multiply?").also { stage++ }
                28 -> npc("Like a rabbit, because it is-").also { stage++ }
                29 -> player("A rabbit!").also { stage++ }
                30 -> npc("Well yes, rabbit-like, but-").also { stage++ }
                31 -> player("Oh, don't worry, I'll deal with this myself. One rabbit", "stew coming right up.").also { stage++ }
                32 -> interpreter.sendDialogues(npc, FaceAnim.FURIOUS, "Very well, young fool. You go see how well you do", "against it.").also { stage++ }
                33 -> end().also { quest!!.start(player) }
            }

            10 -> when (stage) {
                0 -> player("No, not yet.").also { stage++ }
                1 -> npc("What are you waiting for? I need to get", "that monster out of my room.").also { stage++ }
                2 -> player("Sorry, I'll go right now.").also { stage++ }
                3 -> end()
            }

            20 -> when (stage) {
                0 -> npc("So, you finally saw what you're against, eh? Not as", "harmless as you assumed, I take it?").also { stage++ }
                1 -> player("Horns! Teeth!").also { stage++ }
                2 -> npc("Are you prepared to treat this situation with the gravity", "it deserves, now?").also { stage++ }
                3 -> player(FaceAnim.HALF_ASKING,"What IS it?").also { stage++ }
                4 -> npc("A wolpertinger, and a pretty big one at that.").also { stage++ }
                5 -> npc("They are spirits that tend to be on the energetic and", "destructive side when they manifest here, but are a little", "less violent on the spiritual plane.").also { stage++ }
                6 -> player(FaceAnim.HALF_ASKING,"So, what can be done about it? Can't you banish it?").also { stage++ }
                7 -> npc("Well, I could, but in order to do so, I'd need a spirit", "wolf pouch.").also { stage++ }
                8 -> npc("Wolpertingers are generally afraid of wolves; that's the", "rabbity-side of them. A spirit wolf will scare it and banish", "it.").also { stage++ }
                9 -> npc("The problem is that I don't have any of the necessary", "spirit wolf pouches, and the only thing keeping the giant", "wolpertingers upstairs is my presence. If I were to leave,", "it would amble downstairs to bring more of its kind.").also { stage++ }
                10 -> npc("thought, as I said before.").also { stage++ }
                11 -> player(FaceAnim.THINKING,"Well, what if I were to bring you the elements you", "needed? Would that work?").also { stage++ }
                12 -> npc("Not really; I would need to go down to the obelisk in", "my cellar and use the necessary ingredients to make a", "spirit wolf pouch and some Howl scrolls, but it may slip", "out of the front door.").also { stage++ }
                13 -> npc("You could infuse the pouch yourself, though. Would", "you like to learn the secrets of Summoning? I can", "sense the spark within you, urging you to master the", "art.").also { stage++ }
                14 -> player("Certainly!").also { stage++ }
                15 -> player(FaceAnim.HALF_ASKING,"What do you need me to bring?").also { stage++ }
                16 -> npc(FaceAnim.FRIENDLY, "That's wonderful!").also { stage++ }
                17 -> npc("You need to bring two lots of wolf bones. I can provide", "the other items you will need.").also { stage++ }
                18 -> player("I'll get right on it.").also { stage++ }
                19 -> end().also { quest!!.setStage(player, 30) }
            }

            30 -> when (stage) {
                0 -> end()
                1 -> npc("Splendid, dear boy.").also { stage++ }
                2 -> {
                    quest!!.setStage(player, 40)
                    for (i in WOLF_ITEMS) {
                        player.inventory.add(i, player)
                    }
                    npc("Here are the pouches, spirit shards and charms you will", "need to make the spirit wolf pouch and Howl scrolls.", "This key is to the trapdoor over there, which leads to", "the obelisk.")
                    stage = 20
                }
            }

            40 -> when (stage) {
                0 -> end()
                1 -> npc("Wonderful! Now, all you have to do is go upstairs and", "summon the spirit wolf, then the Howl effect.").also { stage++ }
                2 -> player(FaceAnim.HALF_ASKING,"That's it?").also { stage++ }
                3 -> npc("It's that simple. The spirit wolf will, when you use the", "Howl scroll, chase away the giant wolpertinger and then", "disappear. Under normal circumstances, the spirit wolf", "would follow you around, defend you in combat and").also { stage++ }
                4 -> npc("often lend you its powers.").also { stage++ }
                5 -> player(FaceAnim.HALF_ASKING,"What sort of powers?").also { stage++ }
                6 -> npc("Well, for a start, it has the ability to perform a", "Summoning scroll that forces the opponents to flee. That is", "what it will use on the giant wolpertinger, with the", "difference being that the giant wolpertinger will be so").also { stage++ }
                7 -> npc("scared that it will retreat to its spiritual plane, rather", "than face the spirit wolf.").also { stage++ }
                8 -> player(FaceAnim.HALF_ASKING,"Great! So, how will I make it perform that Summoning", "scroll?").also { stage++ }
                9 -> npc("All you need to do is use the special move button in", "your Summoning interface, and aim it at the giant", "wolpertinger, once the spirit wolf has been summoned.", "The spirit wolf will then perform its special ability.").also { stage++ }
                10 -> player("What a relief!").also { stage++ }
                11 -> npc("Well, I think that I have talked enough. Now you have", "to put it into practice.").also { stage++ }
                12 -> player("Wish me luck!").also { stage++ }
                13 -> end().also { quest!!.setStage(player, 50) }
                20 -> player(FaceAnim.HALF_ASKING,"What are these things?").also { stage++ }
                21 -> npc("Well, the wolf bones, spirit shards and gold charms are", "all ingredients to go into the pouch.").also { stage++ }
                22 -> npc("The wolf bones give a solidity to the spirit, the charm", "attracts the type of familiar that you desire - in this", "case a gold charm - and that shards are the spirit's", "focus.").also { stage++ }
                23 -> npc("You will first need to make two Summoning pouches:", "one that can be used to summon the spirit wolf, and", "another to tear open at the obelisk, creating some Howl", "scrolls. These Howl scrolls will help make the spirit wolf").also { stage++ }
                24 -> npc("perform a fear-inducing Howl special move.").also { stage++ }
                25 -> player(FaceAnim.HALF_THINKING,"Okay, I think I understood that. So, how do I get the", "obelisk to work?").also { stage++ }
                26 -> npc("Well, you stand before the obelisk with the charms,", "pouches, shards and bones necessary to complete two", "Summoning pouches. You should then 'use' your empty", "pouch on the obelisk. Your ingredients will be added to").also { stage++ }
                27 -> npc("the pouch, mixing with the spirits of a familiar to create a", "spirit wolf pouch.").also { stage++ }
                28 -> npc("Creating a scroll is a similar process. By using a", "Summoning pouch on an obelisk and breaking it open,", "you are allowing the spirit energy to transform into a", "different form - some scrolls.").also { stage++ }
                29 -> npc("Once you have a spirit wolf pouch and some Howl", "scrolls, you can come upstairs to see me, to find out", "what must be done with the giant wolpertinger.").also { stage++ }
                30 -> player("It all sounds simple enough.").also { stage++ }
                31 -> end()
            }
            50 -> end()
            60 -> when (stage) {
                0 -> player("The spirit wolf scared it away!").also { stage++ }
                1 -> npc("My dear boy, you've done it!").also { stage++ }
                2 -> npc("I just hope the damage to my room isn't too bad.").also { stage++ }
                3 -> player("All in a day's...").also { stage++ }
                4 -> player("Woah, I feel a little dizzy.").also { stage++ }
                5 -> npc("Well, that Summoning was a drain on your rather", "untrained reserves.").also { stage++ }
                6 -> npc("Go downstairs to the obelisk, as it will renew your", "energy.").also { stage++ }
                7 -> npc("You should do that immediately - you'll be cut off from", "Summoning while you have no Summoning skill points", "remaining.").also { stage++ }
                8 -> player("Woooooah...").also { stage++ }
                9 -> npc("You really need to renew your Summoning skill points", "at the obelisk, " + player.username + ".").also { stage++ }
                10 -> end()

                20 -> player("Yeah, that does feel a lot better!").also { stage++ }
                21 -> player(FaceAnim.HALF_ASKING,"Won't my energy renew on its own?").also { stage++ }
                22 -> npc("Not really. Your Summoning skill points need to be", "renewed at the obelisks or mini obelisks scattered", "around the world, while your special move bar will", "recharge slowly over time.").also { stage++ }
                23 -> npc("The special move bar is like your special attack bar, not", "only because it is useful when attacking, but because it", "recharges slowly overtime.").also { stage++ }
                24 -> npc("To help you on your way, I can tell you that I know", "of two other obelisks you can use.").also { stage++ }
                25 -> npc("One is guarded by the ogres in Gu'Tanoth, and the", "other is on the coast near Brimhaven. There will be", "others, however.").also { stage++ }
                26 -> player(FaceAnim.HAPPY,"Thanks for the information.").also { stage++ }
                27 -> npc("My dear boy, it was nothing.").also { stage++ }
                28 -> npc("Well, from your actions, you have proved yourself", "worthy to learn the secrets of Summoning.").also { stage++ }
                29 -> npc("Speak to me on the subject if you wish to learn more", "about it. In the meantime, I will use my power to grant", "you access to the skill.").also { stage++ }
                30 -> npc("Here is a little something to help you start your", "Summoning training.").also { stage++ }
                31 -> player("Thanks!").also { stage++ }
                32 -> end().also { quest!!.finish(player) }
            }

            100 -> when (stage) {
                0 -> if (getStatLevel(player, Skills.SUMMONING) == 99) {
                    options("So, what's Summoning all about, then?", "Can I buy some Summoning supplies?", "Can I buy a Summoning skillcape?").also { stage = 600 }
                } else {
                    options("So, what's Summoning all about, then?", "Can I buy some Summoning supplies?", "Please tell me about skillcapes.").also { stage++ }
                }
                1 -> when (buttonId) {
                    1 -> player(FaceAnim.HALF_ASKING,"So, what's summoning all about, then?").also { stage = 10 }
                    2 -> player(FaceAnim.HALF_ASKING,"Can I buy some summoning supplies, please?").also { stage = 34 }
                    3 -> player(FaceAnim.HALF_ASKING,"Please tell me about Skillcapes.").also { stage = 400 }
                }
                10 -> npc("In general? Or did you have a specific topic in mind?").also { stage++ }
                11 -> options("In general.", "Tell me about summoning familiars.", "Tell me about special moves.", "Tell me about pets.").also { stage++ }
                12 -> when (buttonId) {
                    1 -> npc("Well, you already know about Summoning in general;", "otherwise, we would not be having this conversation!").also { stage = 100 }
                    2 -> npc("Well, if a Summoning pouch is split apart at an obelisk,", "then the energy it contained will reconstitute itself -", "transform - into a scroll. This scroll can then be used to", "make your familiar perform its special move.").also { stage = 129 }
                    3 -> npc("Well, if a Summoning pouch is split apart at an obelisk,", "then the energy it contained will reconstitute itself -", "transform - into a scroll. This scroll can then be used to", "make your familiar perform its special move.").also { stage = 129 }
                    4 -> npc("Well, these are not really an element of the skill, as such,", "but more like a side-effect of training.").also { stage = 168 }
                }
                100 -> npc("Effectively, the skill can be broken into two main parts:", "summoned familiars, and pets.").also { stage++ }
                101 -> npc("Summoned familiars are spiritual animals that can be", "called to you from the spirit plane, to serve you for a", "period of time.").also { stage++ }
                102 -> npc("These animals can also perform a special move, which is", "specific to the species. For example, a spirit wolf can", "perform the Howl special move if you are holding the", "correct Howl scroll.").also { stage++ }
                103 -> npc("The last part of Summoning: the pets. The more", "you practice the skill, the more you will comprehend the", "natural world around you.").also { stage++ }
                104 -> npc("This is reflected in your increased ability to raise animals", "as pets. It takes a skilled summoner to be able to raise", "some of Gielinor's more exotic animals, such as the lizards", "of Karamja, or even dragons!").also { stage++ }
                105 -> npc("Now that I've given you this overview, do you want to", "know about anything specific?").also { stage++ }
                106 -> options("Tell me about summoning familiars.", "Tell me about special moves.", "Tell me about charged items.", "Tell me about pets.").also { stage++ }
                107 -> when (buttonId) {
                    1 -> npc("Summoned familiars are at the very core of Summoning.", "Each familiar is different, and the more powerful the", "summoner, the more powerful the familiar they can", "summon.").also { stage++ }
                    2 -> npc("Well, if a Summoning pouch is split apart at an obelisk,", "then the energy it contained will reconstitute itself -", "transform - into a scroll. This scroll can then be used to", "make your familiar perform its special move.").also { stage = 129 }
                    3 -> npcl(FaceAnim.FRIENDLY, "Charged items are very simple to create, if you have the correct Crafting level.").also { stage = 148 }
                    4 -> npc("Well, these are not really an element of the skill, as such,", "but more like a side-effect of training.").also { stage = 168 }
                }
                // Tell me about summoning familiars.
                108 -> npcl(FaceAnim.FRIENDLY, "The animals themselves are not real, in the sense that you or I are real; they are spirits drawn from the spirit plane.").also { stage++ }
                109 -> npcl(FaceAnim.FRIENDLY, "As a result, they have powers that the animals they resemble do not.").also { stage++ }
                110 -> npcl(FaceAnim.FRIENDLY, "They cannot remain in this world indefinitely; they require a constant supply of energy to maintain their link to the spirit plane.").also { stage++ }
                111 -> npcl(FaceAnim.FRIENDLY, "This energy is drained from your Summoning skill points.").also { stage++ }
                112 -> npcl(FaceAnim.FRIENDLY, "Your level of the Summoning skill is not drained, and you can refresh your points back up to your maximum level at an obelisk whenever you wish.").also { stage++ }
                113 -> playerl(FaceAnim.NEUTRAL, "So, my Summoning skill points are like food to them?").also { stage++ }
                114 -> npcl(FaceAnim.FRIENDLY, "Yes, that is an appropriate analogy.").also { stage++ }
                115 -> npcl(FaceAnim.FRIENDLY, "The more powerful the familiar, the more it must 'feed' and the more 'food' it will need to be satisfied.").also { stage++ }
                116 -> npcl(FaceAnim.FRIENDLY, "As a result, only the most powerful summoners are able to maintain a link from a familiar to the spirit plane, since they are able to provide more 'food' with each Summoning level they gain.").also { stage++ }
                117 -> playerl(FaceAnim.NEUTRAL, "I'm starting to get a little hungry now.").also { stage++ }
                118 -> npcl(FaceAnim.FRIENDLY, "As you gain mastery of the skill you will be able to have familiars out for the full time they exist. And still have some points over to re-summon them afterwards, if you wish.").also { stage++ }
                119 -> npcl(FaceAnim.FRIENDLY, "This is because you are able to feed them the energy they need and have leftovers to spare!").also { stage++ }
                120 -> playerl(FaceAnim.NEUTRAL, "Great!").also { stage++ }
                121 -> playerl(FaceAnim.HALF_ASKING, "So, what can these familiars do?").also { stage++ }
                122 -> npcl(FaceAnim.FRIENDLY, "Why not ask me to count every blade of grass on a lawn?").also { stage++ }
                123 -> npcl(FaceAnim.FRIENDLY, "The familiars each have unique abilities and even the most experienced summoner will not know them all.").also { stage++ }
                124 -> playerl(FaceAnim.HALF_ASKING, "Well, can you give me some hints?").also { stage++ }
                125 -> npcl(FaceAnim.FRIENDLY, "Well, some familiars are able to fight with you in combat. They will keep an eye out to see if you are fighting and will intervene, if they can.").also { stage++ }
                126 -> npcl(FaceAnim.FRIENDLY, "Other familiars will not fight, for various reasons, but they may provide bonuses in other tasks. Some will even carry your items for you, if you need them to.").also { stage++ }
                127 -> playerl(FaceAnim.HAPPY, "Amazing!").also { stage++ }
                128 -> npcl(FaceAnim.FRIENDLY, "Would you like to know anything else about Summoning?").also { stage = 106 }
                // Tell me about special moves.
                129 -> npcl(FaceAnim.FRIENDLY, "Well, if a Summoning pouch is split apart at an obelisk, then the energy it contained will reconstitute itself - transform - into a scroll.").also { stage++ }
                130 -> npcl(FaceAnim.FRIENDLY, "This scroll can then be used to make your familiar perform its special move.").also { stage++ }
                131 -> npcl(FaceAnim.FRIENDLY, "For example, spirit wolves are able to Howl, scaring away an opponent for a short period of time.").also { stage++ }
                132 -> playerl(FaceAnim.NEUTRAL, "Or longer, in the case of that giant wolpertinger.").also { stage++ }
                133 -> npcl(FaceAnim.FRIENDLY, "Indeed!").also { stage++ }
                134 -> npcl(FaceAnim.FRIENDLY, "Well, each familiar has its own special move, and you can only use scrolls on the familiar that it applies to.").also { stage++ }
                135 -> npcl(FaceAnim.FRIENDLY, "For example, a spirit wolf will only look at you oddly if you wish it to perform a dreadfowl special move.").also { stage++ }
                136 -> playerl(FaceAnim.HALF_ASKING, "So, what sort of special moves are there?").also { stage++ }
                137 -> npcl(FaceAnim.FRIENDLY, "The special moves are as varied as the familiars themselves.").also { stage++ }
                138 -> npcl(FaceAnim.FRIENDLY, "A good rule of thumb is that if a familiar helps you in combat, then its special move is likely to damage attackers when you use a scroll.").also { stage++ }
                139 -> npcl(FaceAnim.FRIENDLY, "On the other hand, if a familiar is more peaceful by nature, then its special move might heal or provide means to train your other skills - that sort of thing.").also { stage++ }
                140 -> playerl(FaceAnim.HALF_ASKING, "Are the familiar's special moves similar to its normal abilities?").also { stage++ }
                141 -> npcl(FaceAnim.FRIENDLY, "In general, no. But some familiars' special moves can be a more powerful version of their normal ability.").also { stage++ }
                142 -> npcl(FaceAnim.FRIENDLY, "Take the spirit wolf, for example. Its special move and its normal ability are essentially the same.").also { stage++ }
                143 -> npcl(FaceAnim.FRIENDLY, "However, the special move can be used on any nearby opponent, while the normal ability only works on those opponents you are currently fighting.").also { stage++ }
                144 -> npcl(FaceAnim.FRIENDLY, "If your familiar is fighting with you, it will use its normal ability whenever it can.").also { stage++ }
                145 -> npcl(FaceAnim.FRIENDLY, "It won't however, use its special move unless you have expressly asked it to, by activating a scroll.").also { stage++ }
                146 -> playerl(FaceAnim.HAPPY, "I see. Thanks for the information.").also { stage++ }
                147 -> npcl(FaceAnim.FRIENDLY, "Would you like to know anything else about Summoning?").also { stage = 106 }
                // Tell me about charged items.
                149 -> npcl(FaceAnim.FRIENDLY, "If not, then you can simply buy a set of antlers or a lizard skull from me. You can take an item that can be charged, usually a headdress, and attach scrolls to them.").also { stage++ }
                150 -> npcl(FaceAnim.FRIENDLY, "The scrolls are then stored in the headdress. Aggressive combat scrolls, however, are the only ones that can be used in this way.").also { stage++ }
                151 -> playerl(FaceAnim.HALF_ASKING, "Why is that?").also { stage++ }
                152 -> npcl(FaceAnim.FRIENDLY, "Well, a scroll in a charged item will only be activated when it is hit with enough power.").also { stage++ }
                153 -> npcl(FaceAnim.FRIENDLY, "Since this can only happen from the impact of combat, it makes sense that only combat-based scrolls can be stored here.").also { stage++ }
                154 -> npcl(FaceAnim.FRIENDLY, "The activated scroll will then be sensed by your familiar, and it will defend you.").also { stage++ }
                155 -> playerl(FaceAnim.NEUTRAL, "What sort of impact are we talking about here?").also { stage++ }
                156 -> npcl(FaceAnim.FRIENDLY, "Oh, nothing too serious: stabbings, explosions, arrows to the torso - that sort of thing.").also { stage++ }
                157 -> playerl(FaceAnim.HALF_ASKING, "What?").also { stage++ }
                158 -> npcl(FaceAnim.FRIENDLY, "Oh, come now; a strapping lad like you will barely feel it. Your opponent, however, will certainly feel the power of your retaliation!").also { stage++ }
                159 -> npcl(FaceAnim.FRIENDLY, "Anyway, one impact is not enough; it will take a series of strikes to activate the power in the charged item.").also { stage++ }
                160 -> playerl(FaceAnim.NEUTRAL, "So, will this allow familiars to perform the special moves of other familiars? Or will it work if I have no familiar out?").also { stage++ }
                161 -> npcl(FaceAnim.FRIENDLY, "No. As with the normal method of instigating a special move, the correct scroll is required for the correct familiar.").also { stage++ }
                162 -> npcl(FaceAnim.FRIENDLY, "Even if some dreadfowl scroll power was hanging in the air, a spirit wolf would not be able to do anything with it.").also { stage++ }
                163 -> playerl(FaceAnim.NEUTRAL, "What happens when the item runs out of charge? Do they disintegrate or something?").also { stage++ }
                164 -> npcl(FaceAnim.FRIENDLY, "Only if you've made them shoddily!").also { stage++ }
                165 -> npcl(FaceAnim.FRIENDLY, "The items should be useful for a long time. When you run out of charges, you simply need to attach more scrolls.").also { stage++ }
                166 -> playerl(FaceAnim.NEUTRAL, "That's a relief.").also { stage++ }
                167 -> npcl(FaceAnim.FRIENDLY, "Would you like to know anything else about Summoning?").also { stage = 106 }
                // Tell me about pets.
                168 -> npcl(FaceAnim.FRIENDLY, "Essentially, the higher the Summoning level an adventurer has, the more they become in tune with nature.").also { stage++ }
                169 -> npcl(FaceAnim.FRIENDLY, "As a result, they can approach animals that would otherwise run away from them.").also { stage++ }
                170 -> npcl(FaceAnim.FRIENDLY, "This means that you will be able to befriend and raise various animals from around the world.").also { stage++ }
                171 -> playerl(FaceAnim.NEUTRAL, "So, what will I need to do to raise these pets?").also { stage++ }
                172 -> npcl(FaceAnim.FRIENDLY, "Oh, most of them will be quite content to follow you around. You just have to feed them and make sure they are generally healthy.").also { stage++ }
                173 -> npcl(FaceAnim.FRIENDLY, "It is the goal of every summoner to be able to raise a dragon, yet few have been able to perform this feat.").also { stage++ }
                174 -> playerl(FaceAnim.NEUTRAL, "Wow! Imagine riding around on a dragon - even fighting with it!").also { stage++ }
                175 -> npcl(FaceAnim.FRIENDLY, "That's not the right attitude at all!").also { stage++ }
                176 -> playerl(FaceAnim.HALF_ASKING, "What?").also { stage++ }
                177 -> npcl(FaceAnim.FRIENDLY, "When you raise a pet it becomes your responsibility - your friend.").also { stage++ }
                178 -> npcl(FaceAnim.FRIENDLY, "You can't ride into battle on your friend, or make them fight for you. These are not spiritual familiars: if they die, they die!").also { stage++ }
                179 -> playerl(FaceAnim.NEUTRAL, "I didn't realise.").also { stage++ }
                180 -> npcl(FaceAnim.FRIENDLY, "Well, now you know.").also { stage++ }
                181 -> npcl(FaceAnim.FRIENDLY, "Pets do not fight for you, cast spells or have strange abilities, and you may certainly not ride around on them.").also { stage++ }
                182 -> npcl(FaceAnim.FRIENDLY, "They are animals you raise from birth to follow you wherever you go.").also { stage++ }
                183 -> npcl(FaceAnim.FRIENDLY, "Remember that and take good care of them.").also { stage++ }
                184 -> playerl(FaceAnim.HAPPY, "I will!").also { stage++ }
                185 -> npcl(FaceAnim.FRIENDLY, "Would you like to know anything else about Summoning?").also { stage = 106 }
                34 -> npc("If you like! It's good to see you training.").also { stage++ }
                35 -> end().also { openNpcShop(player, NPCs.PIKKUPSTIX_6971) }
                400 -> npc("Of course. Skillcapes are a symbol of achievement. Only", "people who have mastered a skill and reached level 99 can", "get their hands on them and gain the benefits they carry.", "Is there something else I can help you with perhaps?").also { stage++ }
                401 -> sendDialogueOptions(player, "Choose an option:", "So, what's Summoning all about, then?", "Can I buy some Summoning supplies?", "Please tell me about skillcapes.").also { stage = 1 }
                599 -> npc("Why yes you can! I must warn you that they cost", "a total of 99000 coins. Do you wish to still", "buy a Skillcape of Summoning?").also { stage = 601 }
                600 -> when (buttonId) {
                    1 -> player(FaceAnim.HALF_ASKING,"So, what's summoning all about, then?").also { stage = 10 }
                    2 -> player(FaceAnim.HALF_ASKING,"Can I buy some summoning supplies, please?").also { stage = 34 }
                    3 -> player(FaceAnim.HALF_ASKING,"Can I buy a Skillcape of Summoning?").also { stage = 599 }

                }
                601 -> options("Yes.", "No.").also { stage++ }
                602 -> when (buttonId) {
                    1 -> player("Yes, please.").also { stage++ }
                    2 -> end()
                }
                603 -> {
                    if (freeSlots(player) < 2) {
                        player("Sorry, I don't seem to have enough inventory space.")
                        stage = 604
                        return true
                    }
                    if (!inInventory(player, Items.COINS_995, 99000)) {
                        end()
                        return true
                    }
                    if (!removeItem(player, Item(Items.COINS_995, 99000))) {
                        player("Sorry, I don't seem to have enough coins", "with me at this time.")
                        stage = 604
                        return true
                    } else {
                        end()
                        player.inventory.add(ITEMS[if (player.getSkills().masteredSkills > 1) 1 else 0], ITEMS[2])
                        player("There you go, enjoy!")
                    }
                }
                604 -> end()
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = PikkupstixDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.PIKKUPSTIX_6970, NPCs.PIKKUPSTIX_6971)
}