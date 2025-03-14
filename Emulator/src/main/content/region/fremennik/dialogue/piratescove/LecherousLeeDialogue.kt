package content.region.fremennik.dialogue.piratescove

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class LecherousLeeDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private val conversations = arrayOf(0, 10, 13, 17, 21, 27, 31, 36, 43, 52)

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.FRIENDLY, "Hello.").also { stage = conversations.random() }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> player(FaceAnim.FRIENDLY, "So how's life as a pirate?").also { stage++ }
            1 ->
                npcl(
                    FaceAnim.LAUGH,
                    "What kind of question is that? How's life as a... I dunno. Whatever it is that you do for a living.",
                ).also {
                    stage++
                }
            2 -> player(FaceAnim.FRIENDLY, "I'm a freelance troubleshooter.").also { stage++ }
            3 -> npc(FaceAnim.FRIENDLY, "What does that entail then?").also { stage++ }
            4 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "Mostly killing things for money and delivering items around the planet for people.",
                ).also {
                    stage++
                }
            5 -> player(FaceAnim.FRIENDLY, "I collect stuff.").also { stage++ }
            6 -> npc(FaceAnim.FRIENDLY, "So how's that life?").also { stage++ }
            7 -> player(FaceAnim.FRIENDLY, "Can't complain, can't complain...").also { stage++ }
            8 -> npc(FaceAnim.FRIENDLY, "Well, there you go.").also { stage = END_DIALOGUE }
            10 -> player(FaceAnim.FRIENDLY, "Aren't you a little short for a pirate?").also { stage++ }
            11 ->
                npcl(
                    FaceAnim.LAUGH,
                    "My mother was a gnome. Apparently it was a very painful birth.",
                ).also { stage++ }
            12 -> player(FaceAnim.FRIENDLY, "More info than I wanted, thanks!").also { stage = END_DIALOGUE }
            13 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Sorry, can't stop, the Captain will have my guts for garters if he catches me slacking off talking to the stowaway.",
                ).also {
                    stage++
                }
            14 -> player(FaceAnim.FRIENDLY, "I'm not a stowaway! I was invited aboard!").also { stage++ }
            15 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Yeah, whatever guy, it doesn't really matter who you are I'll get in trouble!",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            17 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "You know, I've always wondered what life as a pirate actually entails.",
                ).also {
                    stage++
                }
            18 ->
                npcl(
                    FaceAnim.LAUGH,
                    "Well, at the moment it mostly involves being asked random questions by a stowaway.",
                ).also {
                    stage++
                }
            19 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "I'm not a stowaway! I was invited aboard! By Lokar! Ask him!",
                ).also { stage++ }
            20 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Hey, whatever pal. Just make sure the captain doesn't catch you, pirates don't like stowaways much.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            21 ->
                npcl(
                    FaceAnim.LAUGH,
                    "Ah, good day to you sirrah! Your face is unfamiliar, did you perhaps join us aboard the ship at Lunar Isle?",
                ).also {
                    stage++
                }
            22 -> playerl(FaceAnim.FRIENDLY, "No, Lokar offered me a lift in Rellekka actually.").also { stage++ }
            23 -> npcl(FaceAnim.FRIENDLY, "Oh, really? You don't look like a Fremennik to me!").also { stage++ }
            24 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "Well... I kind of am, and I kind of aren't. It's a long story.",
                ).also { stage++ }
            25 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Sorry I don't have time to hear it then! See you around young fremennik-who-is-not-really-a- fremennik!",
                ).also {
                    stage++
                }
            26 -> playerl(FaceAnim.FRIENDLY, "'Bye.").also { stage = END_DIALOGUE }
            27 -> player(FaceAnim.FRIENDLY, "Brrrr! Its cold up here!").also { stage++ }
            28 ->
                npcl(
                    FaceAnim.LAUGH,
                    "You think this is cold? Up by Acheron it gets so cold that when you talk you see the words freeze in the air in front of you!",
                ).also {
                    stage++
                }
            29 -> player(FaceAnim.FRIENDLY, "REALLY?").also { stage++ }
            30 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Nah, not really. I was exaggerating for humourous effect. It is very very cold though!",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            31 -> npc(FaceAnim.LAUGH, "Hello to you too.").also { stage++ }
            32 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "Yar! We be pirates, yar! Avast, ye scurvy land-lubbing lychee!",
                ).also { stage++ }
            33 -> npcl(FaceAnim.FRIENDLY, "Please don't talk like that, it is extremely irritating.").also { stage++ }
            34 -> npcl(FaceAnim.FRIENDLY, "Also, please don't call me a lychee, whatever that may be.").also { stage++ }
            35 -> player(FaceAnim.FRIENDLY, "Oh. Okay. Sorry.").also { stage = END_DIALOGUE }
            36 ->
                npc(
                    FaceAnim.LAUGH,
                    "ARGH!",
                    "SOUND THE ALARM!",
                    "STOWAWAY ON BOARD!",
                    "STOWAWAY ON BOARD!",
                ).also { stage++ }
            37 -> player(FaceAnim.FRIENDLY, "No! I'm not a stowaway! Honest! I was invited here!").also { stage++ }
            38 -> npc(FaceAnim.FRIENDLY, "Oh, sorry, my mistake then.").also { stage++ }
            39 -> npcl(FaceAnim.FRIENDLY, "You must admit you do look a lot like a stowaway though.").also { stage++ }
            40 -> player(FaceAnim.FRIENDLY, "Why, what do they usually look like?").also { stage++ }
            41 -> npc(FaceAnim.FRIENDLY, "Erm... I've never actually met one...").also { stage++ }
            42 -> player(FaceAnim.FRIENDLY, "Okay then...").also { stage = END_DIALOGUE }
            43 -> npc(FaceAnim.LAUGH, "Hello.").also { stage++ }
            44 -> player(FaceAnim.FRIENDLY, "So... You're a pirate, huh?").also { stage++ }
            45 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "It's what it says on my pay-packet at the end of the month.",
                ).also { stage++ }
            46 -> player(FaceAnim.FRIENDLY, "How's that working out for you?").also { stage++ }
            47 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Pretty good so far. All the grog and loot that we can plunder, plus full medical including dental.",
                ).also {
                    stage++
                }
            48 -> player(FaceAnim.FRIENDLY, "You mean you have insurance?").also { stage++ }
            49 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Not as such. If any of us get sick we kidnap a doctor and don't let him go until we're better.",
                ).also {
                    stage++
                }
            50 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "You'd be surprised what an incentive for expert health care that is.",
                ).also { stage++ }
            51 -> player(FaceAnim.FRIENDLY, "I can imagine.").also { stage = END_DIALOGUE }
            52 -> npc(FaceAnim.LAUGH, "Hello there. So what brings you aboard the Lady Zay?").also { stage++ }
            53 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "Well, I was planning on visiting the Moon Clan, but I have to say your ship is very impressive.",
                ).also {
                    stage++
                }
            54 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Aye, she's a beauty alright! The Lady Zay has been my home for many hard months, through storm and sun, and she always gets us to here we were headed!",
                ).also {
                    stage++
                }
            55 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "Yes, she's certainly one of the finest boats I've seen on my travels!",
                ).also {
                    stage++
                }
            56 -> npc(FaceAnim.FRIENDLY, "That she is lad, that she is.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return LecherousLeeDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.LECHEROUS_LEE_4556)
    }
}
