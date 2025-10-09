package content.region.misthalin.draynor.dialogue

import content.global.activity.oldman.WomDialogue
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs

/**
 * Represents the Wise Old Man dialogue.
 */
@Initializable
class WiseOldManDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        // Halloween world. Should be enabled during event.
        // (args[1] = Trick or treat option)
        /*
        if (args[1] == true) {
            npc("Don't even think about it.")
            return true
        }
        */
        npc("Greetings, " + player.username + ".")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                if (getAttribute(player, "reclaim-qp-cape", false) || getAttribute(player, "reclaim-qp-hood", false)) {
                    return if (getAttribute(player, "reclaim-qp-cape", false) && getAttribute(player, "reclaim-qp-hood", false)) {
                        npcl(FaceAnim.NEUTRAL, "I assume you're looking for your items? I've placed them " + if (freeSlots(player) < 2) "at your feet." else "in your inventory.")
                        removeAttribute(player, "reclaim-qp-cape")
                        removeAttribute(player, "reclaim-qp-hood")
                        addItemOrDrop(player, Items.QUEST_POINT_CAPE_9813, 1)
                        addItemOrDrop(player, Items.QUEST_POINT_HOOD_9814, 1)
                        stage = 505
                        true
                    } else if (getAttribute(player, "reclaim-qp-cape", false)) {
                        npcl(FaceAnim.NEUTRAL, "I assume you're looking for your Quest Point Cape? I've placed it " + if (freeSlots(player) < 1) "at your feet." else "in your inventory.")
                        removeAttribute(player, "reclaim-qp-cape")
                        addItemOrDrop(player, Items.QUEST_POINT_CAPE_9813, 1)
                        stage = 505
                        true
                    } else {
                        npcl(FaceAnim.NEUTRAL, "I assume you're looking for your Quest Point Hood? I've placed it " + if (freeSlots(player) < 1) "at your feet." else "in your inventory.")
                        removeAttribute(player, "reclaim-qp-hood")
                        addItemOrDrop(player, Items.QUEST_POINT_HOOD_9814, 1)
                        stage = 505
                        true
                    }
                }
                if (player.getQuestRepository().hasCompletedAll()) {
                    options("Quest Point Cape.", "Something else.").also { stage = 500 }
                    return true
                }
                setTitle(player, 4)
                sendOptions(player, "What would you like to say?", "Is there anything I can do for you?", "Could you check my bank for junk, please?", "I've got something I'd like you to look at.", "I'd just like to ask you something.")
                stage = 1
            }

            1 -> when (buttonId) {
                1 -> openDialogue(player, WomDialogue())
                2 -> options("Could you check my bank for junk, please?", "Could you check my inventory for junk, please?").also { stage++ }
                3 -> player("I've got something I'd like you to look at.").also { stage = 4 }
                4 -> player("I'd just like to ask you something.").also { stage = 40 }
            }

            2 -> {
                /*
                 * Removing junk items.
                 * TODO: Sources: https://runescape.wiki/w/Wise_Old_Man/Item_cleanup
                 */
                npc("Certainly, but I should warn you that I don't know about", "all items.")
                cleanupJunkItems(player, fromInventory = buttonId != 1)
                stage = END_DIALOGUE
            }
            /*
            if (player.inventory.containsAtLeastOneItem(*UNIQUE_BOOKS) &&
                       player.inventory.remove(*UNIQUE_BOOKS)
                   ) {
                       npc(FaceAnim.DISGUSTED, "That's my book! What's it doing in your inventory?").also {
                           stage =
                               END_DIALOGUE
                       }

            */
            4 -> npc("Jolly good. Give it to me, and I'll tell you anything I know", "about it.").also { stage = END_DIALOGUE }
            40 -> npc("Please, do!").also { stage++ }
            41 -> {
                setTitle(player, 5)
                sendOptions(player, "Pick a topic", "Distant lands", "Strange beasts", "days gone by", "Gods and demons", "Your hat!").also { stage++ }
            }
            42 -> when (buttonId) {
                1 -> options("The Wilderness", "Misty jungles", "Underground domains", "Mystical realms").also { stage = 52 }
                2 -> options("Biggest & Baddest", "Poison and how to survive it", "Wealth through slaughter", "Random events").also { stage = 84 }
                3 -> options("Heroic figures", "The origin of magic", "Settlements", "The Wise Old Man of Draynor Village").also { stage = 108 }
                4 -> options("Three gods?", "The wars of the gods", "The Mahjarrat", "Wielding the power of the gods").also { stage = 158 }
                5 -> player("I want to ask you about your hat.").also { stage++ }
            }
            43 -> npc("Why, thank you! I rather like it myself.").also { stage++ }
            44 -> {
                setTitle(player, 2)
                sendOptions(player, "What would you like to say?", "You should give it back, you know.", "How can I get a hat like that?").also { stage++ }
            }
            45 -> when (buttonId) {
                1 -> player("You should give it back, you know.").also { stage++ }
                2 -> player("How can I get a hat like that?").also { stage = 187 }
            }
            46 -> npc("No, I think I'll keep it.").also { stage++ }
            47 -> player("But...").also { stage++ }
            48 -> npc("Now you've got that off your chest, would you like to", "ask me about anything else?").also { stage++ }
            49 -> {
                setTitle(player, 3)
                sendOptions(player, "What would you like to say?", "How can I get a hat like that?", "Yes please.", "Thanks, maybe some other time.").also { stage++ }
            }
            50 -> when (buttonId) {
                1 -> player("How can I get a hat like that?").also { stage = 187 }
                2 -> player("Yes please.").also { stage = 44 }
                3 -> player("Thanks, maybe some other time.").also { stage++ }
            }
            51 -> npc("As you wish. Farewell, ${player.username}.").also { stage = END_DIALOGUE }
            52 -> when (buttonId) {
                1 -> player(FaceAnim.HALF_ASKING, "Could you tell me about the Wilderness, please?").also { stage++ }
                2 -> player(FaceAnim.HALF_ASKING, "What can you tell me about jungles?").also { stage = 62 }
                3 -> player(FaceAnim.HALF_ASKING, "Tell me about what's underground.").also { stage = 71 }
                4 -> player(FaceAnim.HALF_ASKING, "What mystical realms can I visit?").also { stage = 78 }
            }
            53 -> npcl(FaceAnim.HALF_GUILTY, "If Entrana is a land dedicated to the glory of Saradomin, the Wilderness is surely the land of Zamorak.").also { stage++ }
            54 -> npcl(FaceAnim.HALF_GUILTY, "It's a dangerous place, where adventurers such as yourself may attack each other, using all their combat skills in the struggle for survival.").also { stage++ }
            55 -> npcl(FaceAnim.HALF_GUILTY, "The Wilderness has different levels. In a low level area, you can only fight adventurers whose combat level is close to yours.").also { stage++ }
            56 -> npcl(FaceAnim.HALF_GUILTY, "But if you venture into the high level areas in the far north, you can be attacked by adventurers who are significantly stronger than you.").also { stage++ }
            57 -> npcl(FaceAnim.HALF_GUILTY, "Of course, you'd be able to attack considerably weaker people too, so it can be worth the risk.").also { stage++ }
            58 -> npcl(FaceAnim.HALF_GUILTY, "If you dare to go to the far north-west of the Wilderness, there's a building called the Mage Arena where you can learn to summon the power of Saradomin himself!").also { stage++ }
            59 -> npcl(FaceAnim.HALF_GUILTY, "Is there anything else you'd like to ask?").also { stage++ }
            60 -> {
                setTitle(player, 2)
                sendOptions(player, "What would you like to say?", "Yes please.", "Thanks, maybe some other time.")
                stage++
            }
            61 -> when (buttonId) {
                1 -> player("Yes please.").also { stage = 41 }
                2 -> player("Thanks, maybe some other time.").also { stage = 51 }
            }
            62 -> npcl(FaceAnim.HALF_GUILTY, "If it's jungle you want, look no further than the southern regions of Karamja.").also { stage++ }
            63 -> npcl(FaceAnim.HALF_GUILTY, "Once you get south of Brimhaven, the whole island is pretty much covered in exotic trees, creepers and shrubs.").also { stage++ }
            64 -> npcl(FaceAnim.HALF_GUILTY, "There's a small settlement called Tai Bwo Wannai Village in the middle of the island. It's a funny place; the chieftain's an unfriendly chap and his sons are barking mad.").also { stage++ }
            65 -> npcl(FaceAnim.HALF_GUILTY, "Honestly, one of them asked me to stuff a dead monkey with seaweed so he could EAT it!").also { stage++ }
            66 -> playerl(FaceAnim.HALF_GUILTY, "Yes, I've met them.").also { stage++ }
            67 -> npcl(FaceAnim.HALF_GUILTY, "Further south you'll find Shilo Village. It's been under attack by terrifying zombies in recent months, if my sources are correct.").also { stage++ }
            68 -> playerl(FaceAnim.HALF_GUILTY, "I've dealt with them, although there are a few still knocking about.").also { stage++ }
            69 -> npcl(FaceAnim.HALF_GUILTY, "The jungle's filled with nasty creatures. There are vicious spiders that you can hardly see before they try to bite your legs off, and great big jungle ogres.").also { stage++ }
            70 -> npcl(FaceAnim.HALF_ASKING, "Is there anything else you'd like to ask?").also { stage = 60 }
            71 -> npcl(FaceAnim.HALF_GUILTY, "Oh, the dwarven realms?").also { stage++ }
            72 -> npcl(FaceAnim.HALF_GUILTY, "Yes, there was a time, back in the Fourth Age, when we humans wouldn't have been able to venture underground. That was before we had magic; the dwarves were quite a threat.").also { stage++ }
            73 -> npcl(FaceAnim.HALF_GUILTY, "Still, it's much more friendly now. You can visit the vast dwarven mine if you like; the entrance is on the mountain north of Falador.").also { stage++ }
            74 -> npcl(FaceAnim.HALF_GUILTY, "If you go further west you may be able to visit the dwarven city of Keldagrim. But they were a bit cautious about letting humans in, last time I asked.").also { stage++ }
            75 -> npcl(FaceAnim.HALF_GUILTY, "On the other hand, if you go west of Brimhaven, you'll find a huge underground labyrinth full of giants, demons, dogs and dragons to fight. It's even bigger than the caves under Taverley, although the Taverley").also { stage++ }
            76 -> npcl(FaceAnim.HALF_GUILTY, "dungeon's pretty good for training your combat skills.").also { stage++ }
            77 -> npcl(FaceAnim.HALF_ASKING, "Is there anything else you'd like to ask?").also { stage = 60 }
            78 -> npcl(FaceAnim.HALF_GUILTY, "Well, you've been to Zanaris. I met a fairy years ago who said she lived there. She was an incredible warrior, considering that she wasn't even a metre tall.").also { stage++ }
            79 -> npcl(FaceAnim.HALF_GUILTY, "Also, in my research I came across ancient references to some kind of Abyss. Demons from the Abyss have already escaped into this land; Saradomin be thanked that they are very rare!").also { stage++ }
            80 -> playerl(FaceAnim.HALF_GUILTY, "I've been through a portal into the lair of some ancient demon. It was in the city of Uzer.").also { stage++ }
            81 -> npcl(FaceAnim.HALF_GUILTY, "Ah, Uzer. A city much favoured by Saradomin, destroyed by Thammaron, the elder-demon, late in the Third Age.").also { stage++ }
            82 -> playerl(FaceAnim.HALF_GUILTY, "Yeah, there isn't much left there.").also { stage++ }
            83 -> npcl(FaceAnim.HALF_ASKING, "Is there anything else you'd like to ask?").also { stage = 60 }
            84 -> when (buttonId) {
                1 -> player(FaceAnim.HALF_ASKING, "What's the biggest monster in the world?").also { stage++ }
                2 -> player(FaceAnim.HALF_ASKING, "What does poison do?").also { stage = 92 }
                3 -> player(FaceAnim.HALF_ASKING, "What monsters drop good items?").also { stage = 97 }
                4 -> player(FaceAnim.HALF_ASKING, "What are these strange monsters that keep appearing out of nowhere and attacking me when I'm training?").also { stage = 104 }
            }
            85 -> npcl(FaceAnim.HALF_GUILTY, "There's a mighty fire-breathing dragon living underground in the deep Wilderness, known as the King Black Dragon. It's a fearsome beast, with a breath that can poison you, freeze you to the ground or").also { stage++ }
            86 -> npcl(FaceAnim.HALF_GUILTY, "incinerate you where you stand.").also { stage++ }
            87 -> npcl(FaceAnim.HALF_GUILTY, "But even more deadly is the Queen of the Kalphites. As if her giant mandibles of death were not enough, she also throws her spines at her foes with deadly force. She can even cast rudimentary spells.").also { stage++ }
            88 -> npcl(FaceAnim.HALF_GUILTY, "Some dark power must be protecting her, for she can block attacks using prayer just as humans do.").also { stage++ }
            89 -> npcl(FaceAnim.HALF_GUILTY, "Another beast that's worthy of a special mention is the Shaikahan. It dwells in the eastern reaches of Karamja, and is almost impossible to kill except with specially prepared weapons.").also { stage++ }
            90 -> playerl(FaceAnim.HALF_GUILTY, "Actually, that one's dead. I've been helping a barmy hunter who'd sworn to kill it.").also { stage++ }
            91 -> npcl(FaceAnim.HALF_ASKING, "Is there anything else you'd like to ask?").also { stage = 60 }
            92 -> npcl(FaceAnim.HALF_GUILTY, "Many monsters use poison against their foes. If you get poisoned, you will not feel it at the time, but later you will begin to suffer its effects, and your life will drain slowly from you.").also { stage++ }
            93 -> npcl(FaceAnim.HALF_GUILTY, "Over time the effects dwindle to nothing, but if you had already been wounded you might die before they wear off completely.").also { stage++ }
            94 -> npcl(FaceAnim.HALF_GUILTY, "Fortunately, followers of Guthix have devised potions that can cure the poison or even give immunity to it.").also { stage++ }
            95 -> npcl(FaceAnim.HALF_GUILTY, "Should you wish to use poison against your own enemies and those of our Lord Saradomin, there is a potion that you can smear on your daggers, arrows, spears, javelins and throwing knives.").also { stage++ }
            96 -> npcl(FaceAnim.HALF_ASKING, "Is there anything else you'd like to ask?").also { stage = 60 }
            97 -> npcl(FaceAnim.HALF_GUILTY, "As a general rule, tougher monsters drop more valuable items. But even a lowly hobgoblin can drop valuable gems; it just does this extremely rarely.").also { stage++ }
            98 -> npcl(FaceAnim.HALF_GUILTY, "If you can persuade the Slayer Masters to train you as a Slayer, you will be able to fight certain monsters that drop valuable items far more often.").also { stage++ }
            99 -> npcl(FaceAnim.HALF_GUILTY, "You might care to invest in an enchanted dragonstone ring. These are said to make a monster drop its most valuable items a little more often.").also { stage = 103 }
            103 -> npcl(FaceAnim.HALF_ASKING, "Is there anything else you'd like to ask?").also { stage = 60 }
            104 -> npcl(FaceAnim.HALF_GUILTY, "Ah, I imagine you see a lot of those.").also { stage++ }
            105 -> npcl(FaceAnim.HALF_GUILTY, "Creatures such as the rock golem, river troll and tree spirit dwell in places where adventurers frequently go to train their skills. While you're training you will often disturb one by accident. It will then get angry and").also { stage++ }
            106 -> npcl(FaceAnim.HALF_GUILTY, "attack you. The safest way to deal with them is to run away immediately, but they sometimes drop valuable items if you kill them.").also { stage++ }
            107 -> npcl(FaceAnim.HALF_ASKING, "Is there anything else you'd like to ask?").also { stage = 60 }
            108 -> when (buttonId) {
                1 -> player(FaceAnim.HALF_ASKING, "Tell me about valiant heroes!").also { stage++ }
                2 -> player(FaceAnim.HALF_ASKING, "Where did humans learn to use magic?").also { stage = 118 }
                3 -> player(FaceAnim.HALF_ASKING, "I suppose you'd know about the history of today's cities?").also { stage = 125 }
                4 -> player(FaceAnim.HALF_ASKING, "Tell me about yourself, old man.")
            }
            109 -> npcl(FaceAnim.HALF_GUILTY, "Ha ha ha... There are plenty of heroes. Always have been, always will be, until the fall of the world.").also { stage++ }
            110 -> npcl(FaceAnim.HALF_GUILTY, "You're quite a noted adventurer yourself!").also { stage++ }
            111 -> npcl(FaceAnim.HALF_GUILTY, "But I suppose I could tell you of a couple...").also { stage++ }
            112 -> npcl(FaceAnim.HALF_GUILTY, "Yes, there was a man called Arrav. No-one knew where he came from, but he was a fearsome fighter, a skillful hunter and a remarkable farmer. He lived in the ancient settlement of Avarrocka, defending it from").also { stage++ }
            113 -> npcl(FaceAnim.HALF_GUILTY, "goblins, until he went forth in search of some strange artefact long desired by the dreaded Mahjarrat.").also { stage++ }
            114 -> npcl(FaceAnim.HALF_GUILTY, "Perhaps some day I shall be able to tell you what became of him.").also { stage++ }
            115 -> npcl(FaceAnim.HALF_GUILTY, "But do not let your head be turned by heroics. Randas was another great man, but he let himself be beguiled into turning to serve Zamorak, and they say he is now a mindless creature deep in the Underground Pass that").also { stage++ }
            116 -> npcl(FaceAnim.HALF_GUILTY, "leads to Isafdar.").also { stage++ }
            117 -> npcl(FaceAnim.HALF_ASKING, "Is there anything else you'd like to ask?").also { stage = 60 }
            118 -> npcl(FaceAnim.HALF_GUILTY, "Ah, that was quite a discovery! It revolutionised our way of life and jolted us into this Fifth Age of the world.").also { stage++ }
            119 -> npcl(FaceAnim.HALF_GUILTY, "They say a traveller in the north discovered the key, although no records state exactly what he found. From this he was able to summon the magic of the four elements, using magic as a tool and a weapon. He and").also { stage++ }
            120 -> npcl(FaceAnim.HALF_GUILTY, "his followers learnt how to bind the power into little stones so that others could use it.").also { stage++ }
            121 -> npcl(FaceAnim.HALF_GUILTY, "in the land south of here they constructed an immense tower where the power could be studied, but followers of Zamorak destroyed it with fire many years ago, and most of the knowledge was lost.").also { stage++ }
            122 -> npcl(FaceAnim.HALF_GUILTY, "I hear you've been very helpful to the wizards who are working on the mysteries of the rune stones.").also { stage++ }
            123 -> playerl(FaceAnim.HALF_GUILTY, "Well, I didn't do much...").also { stage++ }
            124 -> npcl(FaceAnim.HALF_ASKING, "Is there anything else you'd like to ask?").also { stage = 60 }
            125 -> npcl(FaceAnim.HALF_GUILTY, "Yes, there are fairly good records of the formation of the cities from primitive settlements.").also { stage++ }
            126 -> npcl(FaceAnim.HALF_GUILTY, "In the early part of the Fourth Age, of course, there were no permanent settlements. Tribes wandered the lands, staying where they could until the resources were exhausted.").also { stage++ }
            127 -> npcl(FaceAnim.HALF_GUILTY, "This changed as people learnt to grow crops and breed animals, and now there are very few of the old nomadic tribes. There's at least one tribe roaming between the Troll Stronghold and Rellekka, though.").also { stage++ }
            128 -> playerl(FaceAnim.HALF_GUILTY, "Yes, I've met them. They're all mad.").also { stage++ }
            129 -> npcl(FaceAnim.HALF_GUILTY, "Truly, their faith in natural spirits is rather foolish, but they're a pleasant enough people.").also { stage++ }
            130 -> npcl(FaceAnim.HALF_GUILTY, "Anyway, what was I saying? Ah, yes...").also { stage++ }
            131 -> npcl(FaceAnim.HALF_GUILTY, "One settlement was Avarrocka, a popular trading centre.").also { stage++ }
            132 -> npcl(FaceAnim.HALF_GUILTY, "In the west, Ardougne gradually formed under the leadership of the Carnillean family, despite the threat of the Mahjarrat warlord Hazeel who dwelt in that area until his downfall.").also { stage++ }
            133 -> playerl(FaceAnim.HALF_GUILTY, "He'll be back, just you wait!").also { stage++ }
            134 -> npcl(FaceAnim.HALF_GUILTY, "Oh dear, I hope you haven't done anything rash!").also { stage++ }
            135 -> npcl(FaceAnim.HALF_ASKING, "Is there anything else you'd like to ask?").also { stage = 60 }
            136 -> npcl(FaceAnim.HALF_ASKING, "Ah, so you want to know about me, eh?").also { stage++ }
            137 -> npcl(FaceAnim.HALF_GUILTY, "Mmm... what could I say about myself? Let's see what I've done...").also { stage++ }
            138 -> npcl(FaceAnim.HALF_GUILTY, "I've delved into the dungeon west of Brimhaven and heard the terrifying CRASH of the steel dragons battling each other for territory.").also { stage++ }
            139 -> npcl(FaceAnim.HALF_GUILTY, "I spent some years on Entrana, where I learnt the techniques of pure meditation.").also { stage++ }
            140 -> npcl(FaceAnim.HALF_GUILTY, "I've wandered through the vast desert that lies south of Al Kharid and seen the great walls of Menaphos and Sophanem.").also { stage++ }
            141 -> playerl(FaceAnim.HALF_GUILTY, "That feline statue you've got on your bookshelf there - I'm sure I saw something like that when I was in Sophanem.").also { stage++ }
            142 -> npcl(FaceAnim.HALF_GUILTY, "I've always liked to collect little trinkets to remind me of the time when my life was more - ah - exciting.",).also { stage++ }
            143 -> playerl(FaceAnim.HALF_GUILTY, "So you've spoken to the Sphinx, then?").also { stage++ }
            144 -> npcl(FaceAnim.HALF_GUILTY, "Spoken to a sphinx, you say? My dear young lady, you can't possibly be serious!").also { stage++ }
            145 -> playerl(FaceAnim.HALF_GUILTY, "What do you mean?").also { stage++ }
            146 -> npcl(FaceAnim.HALF_GUILTY, "Ha ha ha! The people who live deep in the shifting sands of the desert will often tell travellers tall tales of strange beasts, but that doesn't mean any of it's remotely true!").also { stage++ }
            147 -> playerl(FaceAnim.HALF_GUILTY, "But I've met the Sph...").also { stage++ }
            148 -> npcl(FaceAnim.HALF_GUILTY, "Please, adventurer, there is nothing to be gained by interfering in matters you do not understand.").also { stage++ }
            149 -> npcl(FaceAnim.HALF_GUILTY, "Even if such a creature were to exist...").also { stage++ }
            150 -> playerl(FaceAnim.HALF_GUILTY, "... which it does...").also { stage++ }
            151 -> npcl(FaceAnim.HALF_GUILTY, "EVEN IF SUCH A CREATURE WERE TO EXIST it would be terribly dangerous for you to get involved in its business.").also { stage++ }
            152 -> npcl(FaceAnim.HALF_GUILTY, "I have heard of terrible things happening to travellers in that land, and I would not wish for you to be dragged into anything like that.").also { stage++ }
            153 -> npcl(FaceAnim.HALF_GUILTY, "Indeed, I have heard of adventurers doing the most shameful and blasphemous deeds believing themselves to be acting on behalf of some false god!").also { stage++ }
            154 -> playerl(FaceAnim.HALF_ASKING, "Hmmm?").also { stage++ }
            155 -> npcl(FaceAnim.HALF_GUILTY, "So please do not speak to me of this matter again. Now, I believe I was telling you of my adventurous youth...").also { stage++ }
            156 -> npcl(FaceAnim.HALF_GUILTY, "Apart from all that, I've spent many a happy hour in dusty libraries, searching through ancient scrolls and texts for the wisdom of those who have passed on.").also { stage++ }
            157 -> npcl(FaceAnim.HALF_GUILTY, "Plus plenty of other adventures, quests, journeys... Is there anything else you'd like to know?").also { stage = 60 }
            158 -> when (buttonId) {
                1 -> player(FaceAnim.NEUTRAL, "I heard that Gielinor has three gods.").also { stage++ }
                2 -> player(FaceAnim.HALF_ASKING, "I wanna know about the wars of the gods!").also { stage = 171 }
                3 -> player(FaceAnim.HALF_ASKING, "What are the Mahjarrat?").also { stage = 176 }
                4 -> player(FaceAnim.HALF_ASKING, "Can I wield the power of Saradomin myself?").also { stage = 185 }
            }
            159 -> npcl(FaceAnim.HALF_GUILTY, "Indeed. This is correct: Saradomin, Guthix and Zamorak.").also { stage++ }
            160 -> npcl(FaceAnim.HALF_GUILTY, "Saradomin, the great and glorious, gives life to this world.").also { stage++ }
            161 -> npcl(FaceAnim.HALF_GUILTY, "Zamorak craves only death and destruction.").also { stage++ }
            162 -> npcl(FaceAnim.HALF_GUILTY, "Guthix, calling itself a god of 'balance', holds no allegiance, but simply aids whatever cause suits its shifting purpose.").also { stage++ }
            163 -> playerl(FaceAnim.HALF_ASKING, "So who's Zaros?").also { stage++ }
            164 -> npcl(FaceAnim.HALF_ASKING, "What did you say?").also { stage++ }
            165 -> playerl(FaceAnim.HALF_GUILTY, "I discovered a buried altar to a god called Zaros.").also { stage++ }
            166 -> npcl(FaceAnim.HALF_GUILTY, "Please, do not speak further of this. No good can come of meddling in such things.").also { stage++ }
            167 -> playerl(FaceAnim.HALF_ASKING, "So Zaros is real?").also { stage++ }
            168 -> npcl(FaceAnim.HALF_GUILTY, "Adventurer, my patience is limited. Speak to me no further of this - this - this foolishness.").also { stage++ }
            169 -> playerl(FaceAnim.HALF_GUILTY, "Does that mean I shouldn't ask you about the ancient magicks I learnt in that pyramid north of Sophanem?").also { stage++ }
            170 -> npcl(FaceAnim.HALF_GUILTY, "ENOUGH! Begone from me, you foul trickster!").also { stage = END_DIALOGUE }
            171 -> npcl(FaceAnim.HALF_GUILTY, "Ah, that was a terrible time. The armies of Saradomin fought gloriously against the minions of Zamorak, but many brave warriors and noble cities were overthrown and destroyed utterly.").also { stage++ }
            172 -> npcl(FaceAnim.HALF_GUILTY, "You have visited Uzer, and seen for yourself the damage that could be wrought by an elder-demon in its wrath.").also { stage++ }
            173 -> playerl(FaceAnim.HALF_ASKING, "How did it all end?").also { stage++ }
            174 -> npcl(FaceAnim.HALF_GUILTY, "Before the Zamorakian forces could be utterly routed, Lord Saradomin took pity on them and the battle- scarred world, and allowed a truce.").also { stage++ }
            175 -> npcl(FaceAnim.HALF_GUILTY, "Is there anything else you'd like to ask?").also { stage++ }
            176 -> npcl(FaceAnim.HALF_GUILTY, "Very little is written about the tribe of the Mahjarrat. They are believed to be from the realm of Freneskae, or Frenaskrae - the spelling in this tongue is only approximate.").also { stage++ }
            177 -> npcl(FaceAnim.HALF_GUILTY, "One of them, the foul Zamorak, has achieved godhood, although none knows how this came about.").also { stage++ }
            178 -> npcl(FaceAnim.HALF_GUILTY, "Other Mahjarrat who have been particularly active upon this plane are Hazeel, Lucien, Azzanadra and Zemouregal.").also { stage++ }
            179 -> playerl(FaceAnim.HALF_GUILTY, "Hazeel will rise again, you'll see!").also { stage++ }
            180 -> npcl(FaceAnim.HALF_GUILTY, "Adventurer, your actions fill me with concern!").also { stage++ }
            181 -> playerl(FaceAnim.HALF_GUILTY, "I've defeated Lucien.").also { stage++ }
            182 -> npcl(FaceAnim.HALF_GUILTY, "Truly, your bravery exceeds your stature. But I fear his defeat will not be permanent.").also { stage++ }
            183 -> playerl(FaceAnim.HALF_GUILTY, "Azzanadra taught me some ancient magical powers after I unlocked the pyramid where he had been trapped.").also { stage++ }
            184 -> npcl(FaceAnim.HALF_GUILTY, "Leave me, traveller; I am old, and your words fill me with horror.").also { stage = 60 }
            185 -> npcl(FaceAnim.HALF_GUILTY, "I see you are already learning to summon the power of Saradomin.").also { stage++ }
            186 -> npcl(FaceAnim.HALF_GUILTY, "Is there anything else you'd like to ask?").also { stage = 60 }
            187 -> npcl(FaceAnim.HALF_GUILTY, "You could buy one off another player, or wait until they're next made available by the Council.").also { stage++ }
            188 -> playerl(FaceAnim.HALF_ASKING, "Can I buy your hat?").also { stage++ }
            189 -> npcl(FaceAnim.HALF_GUILTY, "Ohhh no, I don't intend to part with this. Would you like to ask me about something else?").also { stage = 41 }
            500 -> when (buttonId) {
                1 -> player("I believe you are the person to talk to if I want to buy", "a Quest Point Cape?").also { stage++ }
                2 -> {
                    setTitle(player, 4)
                    sendOptions(player, "What would you like to say?", "Is there anything I can do for you?", "Could you check my bank for junk, please?", "I've got something I'd like you to look at.", "I'd just like to ask you something.").also { stage = 1 }
                }
            }
            501 -> npc("Indeed you believe rightly, " + player.username + ", and if you know that", "then you'll also know that they cost 99000 coins.").also { stage++ }
            502 -> options("No, I hadn't heard that!", "Yes, so I was lead to believe.").also { stage++ }
            503 -> when (buttonId) {
                1 -> player("No, I hadn't heard that!").also { stage += 1 }
                2 -> player("Yes, so I was lead to believe.").also { stage += 2 }
            }
            504 -> npc("Well that's the cost, and it's not changing.").also { stage = END_DIALOGUE }
            505 -> {
                end()
                if (player.inventory.freeSlots() < 2) {
                    player("I don't seem to have enough inventory space.")
                    return true
                }
                if (!player.inventory.containsItem(COINS)) {
                    player("I don't seem to have enough coins with", "me at this time.")
                    return true
                }
                if (player.inventory.remove(COINS) && player.inventory.add(*ITEMS)) {
                    npc("Have fun with it.")
                    return true
                } else {
                    player("I don't seem to have enough coins with", "me at this time.")
                }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.WISE_OLD_MAN_2253, NPCs.TORTOISE_3820)

    companion object {
        private val ITEMS = arrayOf(Item(Items.QUEST_POINT_CAPE_9813), Item(Items.QUEST_POINT_HOOD_9814))
        private val COINS = Item(Items.COINS_995, 99000)
        private val QUEST_ITEMS = intArrayOf(Items.HOLY_TABLE_NAPKIN_15, Items.GRAIL_BELL_17, Items.MAGIC_GOLD_FEATHER_18, Items.HOLY_GRAIL_19, Items.WHITE_COG_20, Items.BLACK_COG_21, Items.BLUE_COG_22, Items.RED_COG_23, Items.KHAZARD_CELL_KEYS_76, Items.KHALI_BREW_77, Items.LEVER_83, Items.CHILDS_BLANKET_90, Items.PRESSURE_GAUGE_271, Items.FISH_FOOD_272, Items.POISONED_FISH_FOOD_274, Items.KEY_275, Items.RUBBER_TUBE_276, Items.OIL_CAN_277, Items.RESEARCH_PACKAGE_290, Items.NOTES_291, Items.BOOK_ON_BAXTORIAN_292, Items.GLARIALS_URN_296, Items.GLARIALS_URN_297, Items.A_KEY_298, Items.RATS_TAIL_300, Items.ETHENEA_415, Items.LIQUID_HONEY_416, Items.SULPHURIC_BROLINE_417, Items.PLAGUE_SAMPLE_418, Items.TOUCH_PAPER_419, Items.DISTILLATOR_420, Items.BIRD_FEED_422, Items.KEY_423, Items.PIGEON_CAGE_424, Items.PIGEON_CAGE_425, Items.CHEST_KEY_432, Items.PIRATE_MESSAGE_433, Items.SCORPION_CAGE_456, Items.SCORPION_CAGE_457, Items.SCORPION_CAGE_458, Items.SCORPION_CAGE_459, Items.SCORPION_CAGE_460, Items.SCORPION_CAGE_461, Items.SCORPION_CAGE_462, Items.SCORPION_CAGE_463, Items.ENCHANTED_BEEF_522, Items.ENCHANTED_RAT_MEAT_523, Items.ENCHANTED_BEAR_MEAT_524, Items.ENCHANTED_CHICKEN_525, Items.ORB_OF_PROTECTION_587, Items.ORBS_OF_PROTECTION_588, Items.LENS_MOULD_602, Items.OBSERVATORY_LENS_603, Items.PORTRAIT_666, Items.BLURITE_SWORD_667, Items.BLURITE_ORE_668, Items.RADIMUS_NOTES_714, Items.RADIMUS_NOTES_715, Items.SCRAWLED_NOTE_717, Items.A_SCRIBBLED_NOTE_718, Items.SCRUMPLED_NOTE_719, Items.SKETCH_720, Items.SHAMANS_TOME_729, 750, 753, 755, 756, 761, 763, 765, 769, 773, 774, 783, 784, 785, 786, 787, 788, 789, 790, 791, 792, 793, 794, 964, 1466, 1481, 1482, 1483, 1484, 1486, 1487, 1488, 1489, 1490, 1493, 1494, 1496, 1497, 1498, 1499, 1500, 1501, 1502, 1503, 1508, 1509, 1510, 1535, 1536, 1537, 1538, 1542, 1543, 1544, 1545, 1546, 1547, 1548, 1549, 1584, 1822, 1841, 1842, 1849, 1851, 1852, 1855, 1856, 1857, 1858, 2373, 2374, 2375, 2377, 2378, 2379, 2380, 2381, 2382, 2383, 2384, 2385, 2386, 2387, 2388, 2393, 2394, 2395, 2397, 2399, 2400, 2401, 2403, 2404, 2408, 2409, 2410, 2411, 2418, 2419, 2421, 2423, 2424, 2529, 2888, 2889, 2953, 2954, 2967, 3102, 3103, 3104, 3109, 3110, 3111, 3112, 3113, 3206, 3207, 3208, 3267, 3268, 3395, 3845, 3846, 3847, 3894, 3895, 3896, 4073, 4189, 4190, 4191, 4192, 4193, 4204, 4205, 4206, 4238, 4247, 4248, 4249, 4272, 4273, 4415, 4490, 4496, 4568, 4597, 4598, 4606, 4615, 4616, 4617, 4619, 4623, 4684, 4686, 4814, 4815, 4817, 5507, 5508, 6072, 6073, 6075, 6077, 6079, 6083, 6104, 6545, 6546, 7464, 7956, 7957, 9589, 9590, 9591, 9665, 9716, 9904, 10830, 10831, 10832, 10833, 10834, 10835, 10842, 11196, 11197, 11198, 11202, 11203, 11204, 11210, 11211, 12546, 14062, 14064, 14065, 14066, 14067, 14068, 14069, 14070, 14071, 14072, 14073, 14074, 14075)

        /**
         * Removes quest-related junk items from a inventory or bank.
         *
         * @param player The player whose items should be checked and cleaned.
         * @param fromInventory boolean If `true`, items in the inventory; if `false`, in the bank.
         */
        private fun cleanupJunkItems(player: Player, fromInventory: Boolean) {
            val items = if (fromInventory) player.inventory.toArray() else player.bank.toArray()
            for (item in items) {
                if (item != null && item.id in QUEST_ITEMS) {
                    removeAll(player, item, if (fromInventory) Container.INVENTORY else Container.BANK)
                    sendItemDiscardMessage(player, item.id)
                }
            }
        }

        /**
         * Sends a message to the player informing them that an item has been removed.
         *
         * @param player the player to send the message to.
         * @param item the item id.
         */
        private fun sendItemDiscardMessage(player: Player, item: Int) {
            sendMessage(
                player, when (item) {
                    7464, 5507, 5508 -> "That's my book! What's it doing in your bank?"
                    2399, 2400, 2401 -> "You've killed Delrith, so you don't need any of the little grey keys that you collected to get Silverlight."
                    1536, 1537, 1538, 1535 -> {
                        if (getVarbit(player, 3741) == 1) {
                            "You've opened a secret passageway to Crandor Island, so you won't need the map that showed you how to get there by sea."
                        } else {
                            "Ned knows the way to Crandor now, so you don't need the map any more. Also, if you go there, look around for secret passages. There might be another way to get back to Crandor."
                        }
                    }

                    1548, 1544, 1545, 1546, 1547, 1543 -> "The coloured keys from Melzar's maze are very pretty, but you don't really need to keep them. If you ever go there again, the creatures in the maze will drop more coloured keys for you."
                    1542 -> "You don't really need to keep the key to Melzar's Maze. If you ever go there again, you can ask for a new one from the Guildmaster of the Champions' Guild."
                    271 -> "Since you've already helped to repair Professor Oddenstein's machine, you can get rid of the pressure gauge."
                    274, 272 -> "You really don't need to keep fish food in your bank."
                    275 -> "The little key that opens a closet in Draynor Manor? I think you can afford to get rid of it."
                    276 -> "You aren't going to need that rubber tube again."
                    277 -> "No need to keep that oil can."
                    666 -> "That's a nice picture of Sir Vyvin, but you don't really need it."
                    668 -> "Blurite ore's handy for making ceremonial swords, but you've already done that quest. Members can make crossbows and bolts out of it, but it has no other use."
                    667 -> "You don't really need a copy of Sir Vyvin's sword, although it's a fairly nice weapon."
                    432 -> "I see you've got the key to One-Eyed Hector's chest. That's not much use to you."
                    433 -> "You've already found the pirate's treasure, so you can get rid of the pirate's message."
                    7956 -> "You've already found the pirate's treasure, so you can get rid of the pirate's casket."
                    7957 -> "You've already found the pirate's treasure, so you can get rid of the pirate's apron."
                    2418 -> "You aren't going to need to get back into Prince Ali's cell."
                    2421, 2419 -> "You don't need a wig. Your head looks fine."
                    2423 -> "I can't imagine why you've kept the imprint of Lady Keli's key."
                    2424 -> "If you want to change your appearance, go to the Makeover Mage. You don't need this skin paste."
                    964 -> "I suggest you get rid of the skull. It's unhygienic."
                    755 -> "You have a message from Juliet to Romeo. There's no point in keeping that now that they've split up."
                    756 -> "A potion made from cadava berries? There's not much you can do with one of those."
                    753 -> "If you get rid of those cadava berries, you'll still be able to pick more near Varrock, and you'll save bank space."
                    290 -> "Sedridor's research into the mysteries of the runes is very interesting, but you don't need it."
                    291 -> "Aubury's notes won't be of any further use to you. You can't even read them!"
                    761 -> "You won't need Jonny the Beard's intelligence report now that you've found the Shield of Arrav."
                    763, 765 -> "You don't need to keep bits of the Shield of Arrav now that you've completed that quest."
                    769 -> "You can get rid of the little certificate from the Museum of Varrock. You've already been rewarded for taking one to the King."
                    1549 -> "Now that you've slain Count Draynor, you don't need that stake. It's not much use against other vampyres."
                    300 -> "Ugh... A rat's tail! Get rid of it!"
                    14062 -> "I don't think you'll need the broom ointment."
                    14064 -> "That newt doesn't look very useful."
                    14065 -> "You probably won't need the 'Newts' label."
                    14066 -> "You probably won't need the 'Toads' label."
                    14067 -> "You certainly won't need the 'Newts & Toads' label."
                    14068 -> "Betty's wand? I don't think you should be messing around with that."
                    14069 -> "There's no point in hanging on to that slate."
                    14070 -> "You shouldn't be keeping that reptile in there."
                    14071 -> "You shouldn't be keeping that blackbird in there."
                    14072 -> "You shouldn't be keeping that bat in there."
                    14073 -> "You shouldn't be keeping that spider in there."
                    14074 -> "You shouldn't be keeping that rat in there."
                    14075 -> "You shouldn't be keeping that snail in there."
                    9590, 9589 -> "You don't need to keep the dossier from the White Knight."
                    9591 -> "You completed the Black Knights' fortress you don't need this old cauldron anymore."
                    524, 525, 522, 523, 12546 -> "You have completed the druidic ritual. You don't need the enchanted meat any longer."
                    2409 -> "You don't need the witch's door key any longer."
                    2408 -> "You don't need to keep the witch's diary. You can get it from the bookshelf again, if you need to."
                    2410 -> "You don't need the magnet. You opened the witch's back door."
                    2411 -> "You don't really need the witch's shed key any more."
                    11211, 11210, 11203, 11202, 11204, 11198, 11196, 11197 -> "You don't need that grim looking item."
                    1584 -> "You've already given the id papers to Grip. This must be a forgery!"
                    463, 462, 461, 460, 459, 458, 457, 456 -> "You don't need that scorpion cage anymore."
                    774, 773 -> "You've obtained Avan's part of the crest. You don't need the perfect ruby jewellery."
                    1856 -> "You've already read from the Ardougne tourist guide."
                    1858, 1857 -> "You've finished the Tribal Totem quest. Why do you still have this?"
                    90 -> "You've given this blanket to the monk already. Did you steal it back again?"
                    83 -> "You already fixed the lever in The Temple of Ikov."
                    21, 20, 23, 22 -> "You've finished the Clock Tower quest. You don't need that cog."
                    15 -> "You completed the Holy Grail quest. You don't need Sir Galahad's table napkin."
                    17 -> "You completed the Holy Grail quest. You don't need the grail bell anymore."
                    18 -> "You completed the Holy Grail quest. You have no need of King Arthur's golden feather."
                    19 -> "You completed the Holy Grail quest. Why do you have the grail?"
                    587, 588 -> "You have finished with Tree Gnome Village. You don't need the orbs."
                    77 -> "You already got the keys from the lazy guard. You don't need the brew."
                    76 -> "You have already used the keys from the lazy guard."
                    2403 -> "You already completed the Hazeel Cult Quest no need to hang onto one of those scrolls."
                    2404 -> "You have already opened the chest in the Carnillean household."
                    1508 -> "You have already made Bravek a hangover cure. You don't need his scrawled note."
                    1503 -> "You have no need of a warrant to the plague house."
                    1509 -> "You have already given this book to Ted Rehnison. You don't need it."
                    1510 -> "You have already shown Elena's picture to Jethick."
                    1466 -> "Yuck! A sea slug! Get rid of it!"
                    9665 -> "A sea slug torch... Fascinating! You don't need it lit, unlit, smouldering or otherwise."
                    292 -> "You have completed the Waterfall quest, so you don't need the book about Baxtorian."
                    298 -> "You have completed the Waterfall quest, so you don't need this key."
                    296, 297 -> "You have finished the Waterfall quest, so you certainly don't need that urn empty or full."
                    423 -> "You've freed Elena already, so you don't need the key."
                    425, 424, 422 -> "You have already distracted the guards with the pigeons."
                    420 -> "You've already retreived Elena's distillator."
                    415, 417, 416, 419, 418 -> "Guidor has already tested the plague sample. you don't need that any more."
                    783 -> "You have given the bark sample to Hazelmere already."
                    784 -> "You have already translated the ancient message told to you by Hazelmere."
                    785 -> "You have completed the Grand Tree quest. You have no need of Glough's Journal."
                    786 -> "You have completed the Grand Tree quest. You have no need of Hazelmere's scroll."
                    793 -> "You have completed the Grand Tree quest. You don't need this rock."
                    788 -> "You have already searched the Glough's chest. You have no possible use for the key."
                    794 -> "You have already given the invasion plans to the King."
                    787 -> "You have completed the Grand Tree quest. You don't need to hold on to this lumber order."
                    791, 790, 789, 792 -> "You have already opened the watchtower entrance you don't need twigs."
                    2529, 1484, 1483, 1482, 1481 -> "The orbs are pretty, but you have already found your way down the well in the underground pass."
                    1486 -> "Railings... Useful for poking undead in cages, I guess, but of no value otherwise."
                    1493, 1490, 1488, 1489, 1487 -> "You have already passed the gate of Zamorak. You don't need this."
                    1494 -> "You have killed Iban. You have little use for his journal."
                    1500 -> "You have already done all you can with Iban's shadow."
                    1502, 1501, 1498, 1499, 1496, 1497 -> "You have completed the four tasks with the doll."
                    603, 602 -> "The professor has already fixed his telescope. You don't need this."
                    1852 -> "You have already opened the Captain's chest with the copied key."
                    1851 -> "A pineapple! How wonderful... You don't need it."
                    1841 -> "You have already returned Ana to the Shantay Pass. You don't need to carry the barrel around."
                    1842 -> "You have already returned Ana to the Shantay Pass. You don't need to carry the barrel around... or Ana."
                    // 1849 -> ""
                    1855 -> "You don't need that rock unless you plan on being caught by the guards agasin."
                    9904 -> "The book on sailing is rather dull."
                    2388, 2387, 2386, 2385, 2384 -> "You have already given evidence to the wizard."
                    2374, 2375, 2373 -> "You have already received the ogre relic. You don't need these relic parts."
                    2382, 2383, 2380, 2381 -> "You have already used the powering crystals."
                    2378, 2379, 2377, 2397, 2395, 2394, 2393 -> "You have already received the powering crystals from this item."
                    0 -> "You have already returned the dwarf remains to Captain Lawgof."
                    1 -> "You have already returned the dwarven toolkit to Captain Lawgof."
                    1822 -> "You have already solved the murder mystery. You don't need this fingerprint."
                    715, 714 -> "You have already presented the Radimus notes."
                    750 -> "You have already presented the gilded totem."
                    717, 719, 718, 729, 720 -> "You have freed Ungadulu from possession. You don't have any use for the scrawled notes, books, tomes or pictures of dirty old bowls."
                    9716 -> "What are you going to do with that rock?"
                    2888, 2889 -> "You do not need the Elemental workshop bowl."
                    2954, 2953 -> "You have doused the vampyre's coffin already. You don't need this old bucket of stale water."
                    2967 -> "You don't need Filliman's Journal."
                    3103, 3102 -> "You have already recovered the combination for Denulf."
                    3104 -> "You have already recovered the secret way map for Denulf."
                    3112, 3113, 3110, 3111, 3109 -> "You don't need those coloured cannonballs"
                    3207, 3206 -> "You don't need the king's summons or messages."
                    3208 -> "You can get rid of the crystal pendant. It has served it's purpose."
                    3267 -> "You don't need that dirty old druid's robe."
                    3268 -> "Argh! Is that a man in your backpack? Wait...no! It's a fake man, you crafty so-and-so. Still, you don't need it."
                    3395 -> "You have already sold the apothecary this book."
                    10830 -> "You've already given King Sorvott's decree to Burgher."
                    10835, 10834, 10833, 10832, 10831 -> "You have already collected King Sorvott IV's window taxes."
                    10842 -> "You have already handed in the troll's talking head to the Burgher."
                    3846, 3847, 3845 -> "You have completed the Horror from the Deep quest. You probably don't need this book."
                    3895, 3894 -> "You have already given the Etceteria anthem to Queen Sigrid."
                    3896 -> "You have already given the treaty to King Vargas to sign."
                    4073 -> "A damp tinderbox not at all useful for anything."
                    4193, 4192, 4189, 4190, 4191 -> "You already retrieved the lightning conductor mould from the chimney. You don't need this brush."
                    4204 -> "You don't need the dusty old letter from the clock."
                    4206, 4205 -> "You have already planted this seed for Eluned."
                    4238 -> "Yuck! An ectoplasm puddle."
                    4272, 4249, 4248, 4247 -> "You have already given that to the crone."
                    4273 -> "You have already made the toy boat. You don't need this key."
                    4415 -> "You already have an axe that's been sharpened by Brian. You don't need this blunt one."
                    4490 -> "You have already spread enough mud over that poor tree."
                    4496 -> "A broken stick. Really? You kept a broken stick?"
                    4568 -> "You have already retrieved the schematic from that book."
                    4597, 4598 -> "You have already solved the safe combination."
                    4606 -> "You have already caught the snake with this basket."
                    4615, 4616 -> "You have already found out about the golem. You don't need the note or letter."
                    4617 -> "You have already retrieved the statuette from the display ."
                    4623 -> "You have already reprogrammed the golem. You don't need this pen."
                    4619 -> "You have already opened the golem's head. You don't need this key."
                    4684 -> "You don't need more linen for mummification, however much you might like to."
                    4686 -> "You don't need the book on embalming."
                    4814, 4815, 4817 -> "You have already shown the portrait to Zavistic Rarve."
                    6072 -> "You have already washed the mourner's top."
                    6077, 6079, 6073, 6075 -> "You have finished the Mourning's End quest. You don't need the mourner's books."
                    6104, 6083 -> "You have finished the Mourning's End quest. You don't need that key."
                    6546, 6545 -> "You have already completed all the chores for Bob."
                    else -> "You should get rid of the " + getItemName(item) + "."
                }
            )
        }
    }
}