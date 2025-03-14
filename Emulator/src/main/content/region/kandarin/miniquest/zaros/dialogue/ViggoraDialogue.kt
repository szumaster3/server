package content.region.kandarin.miniquest.zaros.dialogue

import content.region.kandarin.miniquest.zaros.CurseOfZaros
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class ViggoraDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (!inEquipment(player, Items.GHOSTSPEAK_AMULET_552)) {
            player("All your base are belong to us.")
            return true
        }
        if (CurseOfZaros.hasTag(player, npc.id) || CurseOfZaros.hasComplete(player)) {
            val item = Items.GHOSTLY_CLOAK_6111
            if (!CurseOfZaros.hasItems(player, item)) {
                player(FaceAnim.SAD, "Can I have that cloak back?").also { stage = 106 }
            } else {
                sendDialogue(player, "You have already talked to this NPC.").also { stage = END_DIALOGUE }
            }
            return true
        }
        player("So...", "You must be the infamous Viggora.")
        stage = 1
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            1 ->
                npc(
                    "Hold thy tongue varlet!",
                    "Speak fast, how come you find me here, and how",
                    "doth you understand mine speech?",
                ).also {
                    stage++
                }
            2 -> player("You want me to hold my tongue and tell you how I", "found you?").also { stage++ }
            3 -> npc("Cease thy chatter and respond to my demand!").also { stage++ }
            4 -> player("Cease my chatter AND respond to your demand?").also { stage++ }
            5 ->
                npc(
                    "I warn thee knave, this curse upon me hath not",
                    "improved my temper, these centuries past...",
                ).also {
                    stage++
                }
            6 -> player("Well, it's actually about the curse that I have come to", "speak to you.").also { stage++ }
            7 ->
                npc(
                    "Oh, be that so?",
                    "Then forgive my swift anger, and speak to me of how",
                    "you plan to break this curse.",
                ).also {
                    stage++
                }
            8 -> player("Erm...", "I didn't actually mention anything about breaking the", "curse...").also { stage++ }
            9 -> npc("Then what lets you dare speak to me?").also { stage++ }
            10 ->
                player(
                    "Well, I heard of your name from a mage called Dhalak,",
                    "and I'm trying to find out what exactly caused the",
                    "curse to befall him, and you as well apparently.",
                ).also {
                    stage++
                }
            11 -> npc("Ha!", "So the weak-willed mage was cursed along with me?").also { stage++ }
            12 ->
                npc(
                    "Well now, that is an interesting turn of events...",
                    "Then am I to assume that Valdez, Rennard, Kharrim",
                    "and Lennissa were also cursed along with me?",
                ).also {
                    stage++
                }
            13 -> player("...How did you know that?").also { stage++ }
            14 -> npc("Ha ha ha!", "Oh, the curse cut deeper than I had previously thought!").also { stage++ }
            15 ->
                npc(
                    "Stranger, this news has brought me a ray of sunshine",
                    "in an otherwise dreary millennia!",
                    "Please, ask any question you wish!",
                ).also {
                    stage++
                }
            16 -> options("tell em your story", "Goodbye then").also { stage++ }
            17 ->
                when (buttonId) {
                    1 ->
                        player(
                            "Erm...",
                            "Thanks, I think.",
                            "So what happened on that day you were all",
                            "cursed?",
                        ).also { stage++ }
                    2 ->
                        player("Well, that's all fascinating, but I just don't particularly care.", "Bye-bye.").also {
                            stage =
                                END_DIALOGUE
                        }
                }
            18 ->
                player(
                    "I know that Valdez discovered the Staff of Armadyl, was",
                    "robbed by Rennard, who then sent Kharrim to tell",
                    "Zamorak of it.",
                ).also {
                    stage++
                }
            19 ->
                player(
                    "Meanwhile Lennissa heard of the sale, and informed",
                    "Dhalak who placed an enchantment upon it so that",
                    "its power would be hidden.",
                ).also {
                    stage++
                }
            20 ->
                player(
                    "I still don't know what happened with the staff to cause",
                    "this curse, or what you had to do with it though...",
                ).also {
                    stage++
                }
            21 ->
                npc(
                    "Well stranger, rest yourself awhile, and I will recount a",
                    "tale of the events of that day, for I was one of the few",
                    "actually there when it happened...",
                ).also {
                    stage++
                }
            22 -> player("When what happened?").also { stage++ }
            23 -> npc("When my Lord Zamorak got his first taste of godhood!").also { stage++ }
            24 -> player("Wow.", "Sounds like quite the dinner party anecdote.").also { stage++ }
            25 ->
                npc(
                    "You can take the snide venom out of your voice whelp,",
                    "you came to me;",
                    "'twas not the other way round.",
                ).also {
                    stage++
                }
            26 -> player("Okay, okay.", "Please continue.").also { stage++ }
            27 ->
                npc(
                    "Well now, let us see...",
                    "As you may have heard tell, my affiliation lay with",
                    "General Zamorak, a mighty warrior of the Mahjarrat",
                    "tribe, and my skill on the battlefield had quickly brought",
                ).also {
                    stage++
                }
            28 -> npc(" me to his attention.").also { stage++ }
            29 ->
                npc(
                    "So pleased was he with my bloodthirst that he promoted",
                    "me on the battlefield once to serve in his honour guard,",
                    "and let me tell you, this was a rare honour indeed, for",
                    "I was the only human chosen to take such a position.",
                ).also {
                    stage++
                }
            30 -> player("Really?").also { stage++ }
            31 ->
                npc(
                    "Oh yes, the dragon riders, Mahjarrat, demons and",
                    "vampire warriors made up the bulk of the force, but I",
                    "wager I was equal in all ways of combat.",
                ).also {
                    stage++
                }
            32 ->
                npc(
                    "Ha, when I think of someone like Lucien struggling to",
                    "lift a blade, in some ways I was even their better!",
                ).also {
                    stage++
                }
            33 -> player("Please continue.").also { stage++ }
            34 ->
                npc(
                    "Well, anyway...",
                    "Myself and the rest of Zamoraks honour guard were",
                    "formulating stratagems in our battle-tent, when that",
                    "sneaky messenger Kharrim came in offering to sell us",
                ).also {
                    stage++
                }
            35 -> npc("the god-staff of Armadyl!").also { stage++ }
            36 ->
                npc(
                    "Naturally, we suspected that this was some trick by our",
                    "Lord to test our loyalty...",
                ).also { stage++ }
            37 -> player("Yes, who was your lord?", "Everyone has been evasive about that...").also { stage++ }
            38 ->
                npc(
                    "Quiet fool, all things in their course;",
                    "You are disrupting my train of thought!",
                ).also { stage++ }
            39 -> player("Sorry...").also { stage++ }
            40 ->
                npc(
                    "Well, anyway, we thought it was too good to be true,",
                    "yet when we visited this scummy tavern we were",
                    "amazed to discover there was no trick, no test of",
                    "loyalty, no hidden trap:",
                ).also {
                    stage++
                }
            41 -> npc("Somehow this fool had actually managed to obtain the", "god-staff of Armadyl!").also { stage++ }
            42 ->
                npc(
                    "Its power was incredible, you could almost feel the",
                    "energies crackling around it in the air!",
                ).also {
                    stage++
                }
            43 -> player("So what happened then?").also { stage++ }
            44 ->
                npc(
                    "Well, with such a weapon, the plans we had been",
                    "developing for a rebellion against out lord could finally",
                    "be put into action, but we knew that we would have to",
                    "act swiftly, before he heard that we had a weapon",
                ).also {
                    stage++
                }
            45 ->
                npc(
                    "capable of defeating him, and we would have to act",
                    "decisively, for even amongst our group there were still",
                    "those loyal to the lord - such as that pathetic fool",
                    "Azzanadra.",
                ).also {
                    stage++
                }
            46 -> player("So JUST WHO WAS this lord you speak of?").also { stage++ }
            47 ->
                npc(
                    "And I tell thee again, I will say when it is appropriate,",
                    "now do not disrupt my tale!",
                ).also { stage++ }
            48 -> player("Okay, carry on then...").also { stage++ }
            49 ->
                npc(
                    "So anyway, Lord Zamorak and his trusted",
                    "compatriots, namely myself, Hazeel, Drakan,",
                    "Thammaron and Zemouregal made plans to overthrow",
                    "our lord using the god-weapon, and by pledging",
                ).also {
                    stage++
                }
            50 ->
                npc(
                    "allegiance to Zamorak as our master, were each to be",
                    "given a large piece of land as our own in return.",
                ).also {
                    stage++
                }
            51 ->
                npc(
                    "We decided to move immediately, before anyone got",
                    "cold feet, or any other parties could interfere in our",
                    "works, and made haste towards the castle where our",
                    "Lord lived.",
                ).also {
                    stage++
                }
            52 ->
                npc(
                    "If Lucien had not been otherwise occupied, he would",
                    "have probably accompanied us with his magicks, but it",
                    "turned out the foolish Dhalak had made his involvement",
                    "unnecessary with some spells of his own allowing us to",
                ).also {
                    stage++
                }
            53 ->
                npc(
                    "get close to the castle with the staff without the",
                    "Empty Lord being able to sense its presence.",
                ).also {
                    stage++
                }
            54 -> player("So your lord was the one who cursed you?").also { stage++ }
            55 ->
                npc(
                    "I am coming to that...",
                    "So anyway, we made our way to the castle, under the",
                    "pretence that we had war plans against Saradomin and",
                    "the other deities to discuss.",
                ).also {
                    stage++
                }
            56 ->
                npc(
                    "As usual, our lord was guarded well, but this was why",
                    "Zamorak had brought his most trusted fighters with him.",
                ).also {
                    stage++
                }
            57 ->
                npc(
                    "While we distracted the Empty Lord with our feints",
                    "and attacks, and kept his bodyguards busy, Lord",
                    "Zamorak outflanked him, unsheathed the staff and",
                    "plunged it into his back!",
                ).also {
                    stage++
                }
            58 ->
                npc(
                    "Ah, it was a glorious sight...",
                    "At that moment I was reminded for whom I fought,",
                    "and why General Zamorak had earned his nickname 'the",
                    "scourge' upon the battlefield...",
                ).also {
                    stage++
                }
            59 -> player("And the next thing you know you were cursed?").also { stage++ }
            60 ->
                npc(
                    "No, it was not quite that simple...",
                    "The Empty Lord turned away from our battle, eyes",
                    "burning with hatred, and towards Zamorak instead.",
                    "Seeing this, we all fought with extra vigour, so that",
                ).also {
                    stage++
                }
            61 ->
                npc(
                    "General Zamorak would not face our lord alone, but we",
                    "were outnumbered by many hundreds of warriors and",
                    "demons, and could not reach him to assist him!",
                ).also {
                    stage++
                }
            62 -> player("So what then?").also { stage++ }
            63 ->
                npc(
                    "Why, the Empty Lord versus Zamorak, in single",
                    "combat!",
                    "And the sight of the battle will be with me forever",
                    "more...",
                ).also {
                    stage++
                }
            64 ->
                npc(
                    "The Empty Lord was a powerful god, stronger than",
                    "any of the others awake at the time, possibly even as",
                    "strong as Guthix is, and Zamorak was but a mortal:",
                    "a Mahjarrat warrior all the same, with all of the",
                ).also {
                    stage++
                }
            65 ->
                npc(
                    "strength and power that that entails, but mortal",
                    "nonetheless, but to see him fight, you would not think of",
                    "him as a 'mere' anything...",
                ).also {
                    stage++
                }
            66 ->
                npc(
                    "He was war itself!",
                    "Flurry after flurry of blows he rained upon the Empty",
                    "Lord, and the very castle walls shook and quivered with",
                    "their power, but the Empty Lord would not fall!",
                ).also {
                    stage++
                }
            67 ->
                npc(
                    "Even with the weapon of a god embedded in his back,",
                    "he fought on, and with each blow our victory seemed",
                    "less and less certain...",
                ).also {
                    stage++
                }
            68 -> player("So what then?").also { stage++ }
            69 -> npc("Well, then a miracle happened.", "Or luck.", "Or natural justice.").also { stage++ }
            70 ->
                npc(
                    "You can call it what you want, but the Empty Lords",
                    "hand wrapped tightly around Zamoraks throat, Lord",
                    "Zamorak, kicking and screaming defiantly and radiant",
                    "in his anger until the very last, plunged towards the",
                ).also {
                    stage++
                }
            71 ->
                npc(
                    "Empty Lord, who seemed to lose his footing slightly, and",
                    "fell in such a way so that the staff plunged deeper into",
                    "his body, but also impaled Lord Zamorak with it at the",
                    "same time...",
                ).also {
                    stage++
                }
            72 -> npc("And then...").also { stage++ }
            73 -> player("And then what?").also { stage++ }
            74 ->
                npc(
                    "And then nothing.",
                    "There was a sudden flash of bright light, and then a",
                    "sudden blink of cold darkness, and it was over.",
                ).also {
                    stage++
                }
            75 ->
                npc(
                    "Zamorak stood over the Empty Lord who was slowly...",
                    "Fading from existence...",
                    "And as he faded, it seemed though...",
                    "It almost seemed as though Zamorak became more real,",
                ).also {
                    stage++
                }
            76 -> npc("more solid than he had been before...").also { stage++ }
            77 ->
                npc(
                    "And as the Empty Lord faded from this world",
                    "completely, I heard a voice, almost a whisper upon the",
                    "wind, cursing all who had helped Zamorak in his victory,",
                    "which as you now tell me seems to have been all who",
                ).also {
                    stage++
                }
            78 -> npc("were responsible for the staff ending up in Zamorak's", "hands at the castle.").also { stage++ }
            79 ->
                npc(
                    "As I heard it, I saw that I too was fading, just as the",
                    "Empty Lord had, and I called my brethren for their",
                    "assistance, but they could no longer hear my words,",
                    "nor see my form.",
                ).also {
                    stage++
                }
            80 ->
                npc(
                    "It was then that the other gods appeared and banished",
                    "Zamorak from the world completely for daring to kill",
                    "one of their kind, although as it turned out it didn't",
                    "quite work out that way for them, when he returned",
                ).also {
                    stage++
                }
            81 -> npc("stronger than ever, a god himself.").also { stage++ }
            82 -> npc("But the God Wars were another story entirely...").also { stage++ }
            83 ->
                player(
                    "But...",
                    "I don't understand...",
                    "If it was Zamorak who used the weapon, then why was",
                    "it only you who were cursed?",
                ).also {
                    stage++
                }
            84 ->
                player(
                    "And the other people who were cursed, why them?",
                    "Why not the other Mahjarrat for example?",
                ).also {
                    stage++
                }
            85 -> player("And just who was this 'Empty Lord' you keep speaking", "of?").also { stage++ }
            86 ->
                npc(
                    "Well, in my life I was nothing but a warrior.",
                    "I had no hidden knowledge, I didn't especially care",
                    "about the gods or their magics, and I certainly didn't",
                    "respect them.",
                ).also {
                    stage++
                }
            87 ->
                npc(
                    "Now in my... I suppose this is my death.",
                    "I am but a shade on the wind, unnoticed by all who",
                    "pass me, until today anyway, and the only answer I",
                    "can give you is that the others who were with me, the",
                ).also {
                    stage++
                }
            88 -> npc("Mahjarrats and the Vampire, they were beings of magic.").also { stage++ }
            89 -> npc("It runs through their very veins, and ebbs through their", "bones.").also { stage++ }
            90 ->
                npc(
                    "Who knows why the curse fell as it did?",
                    "Perhaps a mere human I was more susceptible to it,",
                    "when they were not.",
                    "Perhaps they too are cursed, but their life spans are so",
                ).also {
                    stage++
                }
            91 ->
                npc(
                    "long that it will be millennia before they feel it upon",
                    "them. Perhaps because it did not affect them, it extended",
                    "backwards through time, to the moment the staff was",
                ).also {
                    stage++
                }
            92 ->
                npc(
                    "taken from its rightful place, and all who had known of",
                    "its theft were cursed too.",
                    "Perhaps there are others also cursed, who played no",
                    "part in this tale, and were merely unlucky enough to be",
                ).also {
                    stage++
                }
            93 -> npc("at the wrong place at the wrong time...").also { stage++ }
            94 ->
                npc(
                    "I don't know.",
                    "Things do not always happen for a reason, just as tales",
                    "do not always end with all of the loose ends neatly tied",
                    "up and all answers supplied.",
                ).also {
                    stage++
                }
            95 -> npc("I have simply told you the events I was witness to, for", "I can do no more.").also { stage++ }
            96 ->
                npc(
                    "You have cheered me no end to let me know that this",
                    "curse that has afflicted me has not left me alone here,",
                    "in this void between worlds.",
                ).also {
                    stage++
                }
            97 ->
                npc(
                    "Perhaps I will hunt down these others who have also",
                    "been cursed as I was, but I feel I must reward you",
                    "for your efforts;",
                    "Here, take my cloak, it is drenched in the blood of a",
                ).also {
                    stage++
                }
            98 ->
                npc("thousand foes, and may bring you luck in battle.").also {
                    CurseOfZaros.tagDialogue(player, npc.id)
                    addItemOrDrop(player, Items.GHOSTLY_CLOAK_6111, 1)
                    stage++
                }
            99 -> player("So how can I break this curse?").also { stage++ }
            100 ->
                npc(
                    FaceAnim.SAD,
                    "Who knows?",
                    "If it was the death curse of the Empty Lord, there may",
                    "be no way to break it.",
                ).also {
                    stage++
                }
            101 ->
                npc(
                    "If it was not his death curse, and he is still alive but not",
                    "on this world, then the only way to break it may be to",
                    "bring him back here;",
                ).also {
                    stage++
                }
            102 -> npc("But I would rather stay here cursed than suffer under his", "rule again...").also { stage++ }
            103 -> player("But WHO was this 'Empty Lord'?", "WHAT was his NAME?").also { stage++ }
            104 -> npc(FaceAnim.SAD, "You do not know?", "You have not guessed yet?").also { stage++ }
            105 -> npc(FaceAnim.NEUTRAL, "He was Zaros.").also { stage = END_DIALOGUE }
            106 -> {
                end()
                if (freeSlots(player) == 0) {
                    sendDialogue(player, "You don't have space for the reward.")
                    return true
                }
                npc(FaceAnim.CALM, "Humph.", "I suppose.")
                addItem(player!!, Items.GHOSTLY_CLOAK_6111, 1)
            }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return ViggoraDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MYSTERIOUS_GHOST_2402)
    }
}
