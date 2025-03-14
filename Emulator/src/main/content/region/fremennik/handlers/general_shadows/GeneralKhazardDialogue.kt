package content.region.fremennik.handlers.general_shadows

import core.api.*
import core.api.EquipmentSlot
import core.api.quest.hasRequirement
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class GeneralKhazardDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        val hasWeapon = getItemFromEquipment(player, EquipmentSlot.WEAPON)
        val hasShield = getItemFromEquipment(player, EquipmentSlot.SHIELD)
        val hasGhostSpeakAmulet = inEquipment(player, Items.GHOSTSPEAK_AMULET_552)
        val hasSinSeersNote = inInventory(player, Items.SIN_SEERS_NOTE_10856)

        if (!hasRequirement(player, Quests.DESERT_TREASURE)) return true

        if (getAttribute(player, GeneralShadowUtils.GS_COMPLETE, false)) {
            player("Your dog attacked me. AGAIN!").also { stage = 100 }
            return true
        }

        if (hasWeapon != null || hasShield != null || !hasGhostSpeakAmulet) {
            npc(
                FaceAnim.OLD_DISTRESSED2,
                "You can see into the Shadow Realm and yet you are",
                "not of it. Oh well, you will be of no use.",
            ).also {
                stage =
                    END_DIALOGUE
            }
            return true
        }

        // Handle Sin Seer note.
        if (hasSinSeersNote) {
            playerl(FaceAnim.FRIENDLY, "I have been to the Sin Seer. She has made her verdict.").also { stage = 23 }
            return true
        }

        // Handle General shadows progress.
        when {
            getAttribute(player, GeneralShadowUtils.START_GENERAL_SHADOW, false) -> {
                player("Err, what was I supposed to do again?").also { stage = 21 }
            }
            getAttribute(player, GeneralShadowUtils.GS_START, false) -> {
                npcl(FaceAnim.OLD_NORMAL, "Do you make progress in finding my scouts?").also { stage = 34 }
            }
            getAttribute(player, GeneralShadowUtils.GS_PROGRESS, 0) == 4 -> {
                player(
                    "I did it, sir! I found all the scouts and delivered",
                    "your message. The last one said I should report back to",
                    "you.",
                ).also {
                    stage =
                        41
                }
            }
            else -> {
                player("Hello, General.")
            }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        val progress = getAttribute(player, GeneralShadowUtils.GS_PROGRESS, 0)
        when (stage) {
            0 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "What? You are not dead and yet you walk and talk in",
                    "the Shadow Realm. Explain yourself.",
                ).also {
                    stage++
                }
            1 ->
                player(
                    "I am a being of great power and experience. I can see",
                    "and speak to ghosts. For my assistance they clothed me",
                    "in their garb. Can I offer you assistance, General?",
                ).also {
                    stage++
                }
            2 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "This intrigues me. How do you know me? You look",
                    "familiar but I can't place it.",
                ).also {
                    stage++
                }
            3 ->
                options(
                    "Uh, well, you don't really have eyes, so how can you tell?",
                    "I just have one of those faces.",
                ).also {
                    stage++
                }
            4 ->
                when (buttonId) {
                    1 -> player("Uh, well, you don't really have eyes, so how can you", "tell?").also { stage = 7 }
                    2 -> player("I just have one of those faces.").also { stage++ }
                }
            5 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "True, I never can tell you humans apart. You're all",
                    "ugly creatures, anyway.",
                ).also {
                    stage++
                }
            6 -> player("Hey!").also { stage = END_DIALOGUE }
            7 -> npc(FaceAnim.OLD_NORMAL, "There are ways to see that you humans cannot grasp.").also { stage++ }
            8 -> player("Oh.").also { stage++ }
            9 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "It doesn't matter anyway. You are not of the blood",
                    "and I cannot trust you.",
                ).also {
                    stage++
                }
            10 ->
                options(
                    "I'm a very trustworthy person.",
                    "You're right. I'm completely untrustworthy.",
                ).also { stage++ }
            11 ->
                when (buttonId) {
                    1 -> player("I'm a very trustworthy person.").also { stage++ }
                    2 -> player("You're right. I'm completely untrustworthy.").also { stage = END_DIALOGUE }
                }
            12 -> npcl(FaceAnim.OLD_NORMAL, "That is not something I can take your word on.").also { stage++ }
            13 -> player("So who's word would you take?").also { stage++ }
            14 ->
                npc(
                    FaceAnim.OLD_CALM_TALK1,
                    " Hmm... There is one person's word I would believe. If",
                    "you visited the Sin Seer, and she judged you",
                    "trustworthy, then I would have use for you.",
                ).also {
                    stage++
                }
            15 -> player("Where would I find her?").also { stage++ }
            16 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "Well, considering I told you she was a seer, of sorts, I",
                    "would imagine you would find her in Seer's Village. It",
                    "is to the south of here not far.",
                ).also {
                    stage++
                }
            17 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "Go and see her, return with her report and I will see if",
                    "I have use for you. You will be well rewarded.",
                ).also {
                    stage++
                }
            18 ->
                player(
                    "Okay! that sounds reasonable. Sin Seer, huh? How do",
                    "you know someone like her?",
                ).also { stage++ }
            19 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "It's a long story, and I do not have the time to tell it.",
                ).also { stage++ }
            20 -> {
                end()
                setAttribute(player, GeneralShadowUtils.START_GENERAL_SHADOW, true)
            }
            21 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "You goldfish! Go find the Sin Seer - she will determine if",
                    "you are trustworthy. She lives in Seer's village to the",
                    "south of here.",
                ).also {
                    stage++
                }
            22 -> end()
            23 -> npc(FaceAnim.OLD_NORMAL, "And?").also { stage++ }
            24 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "She said to give you this note to prove I went to see her.",
                ).also { stage++ }
            25 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "The note says you have committed many sins and atrocities in your life. I'm impressed; I will use you.",
                ).also {
                    stage++
                }
            26 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "It has been a long time since I have left my home in the south. I have lost track of what is going on in the world, but now it is imperative that I know.",
                ).also {
                    stage++
                }
            27 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "So I sent out four scouts to gather information for me. I brought them to walk like me in the Shadow Realm, so they would be safe from my enemies.",
                ).also {
                    stage++
                }
            28 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "I sent them on four missions. One was to go to the land of the gnomes and explore their holdings. One was to go to a jungle to the sound. One was to explore where the White Knights rule.",
                ).also {
                    stage++
                }
            29 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "And one was to the place that I once called home - a place of great heat.",
                ).also {
                    stage++
                }
            30 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "I am going to entrust you with a very important message, which you must deliver to each one of them.",
                ).also {
                    stage++
                }
            31 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "Tell them: The planets are nearing alignment; we will meet in the place of half light and ice soon. Beware the others, for though they are weak and few, they are cunning.",
                ).also {
                    stage++
                }
            32 -> player("You can trust me to do it, sir!").also { stage++ }
            33 -> {
                end()
                removeItem(player, Items.SIN_SEERS_NOTE_10856)
                setAttribute(player, GeneralShadowUtils.GS_START, true)
                setAttribute(player, GeneralShadowUtils.GS_PROGRESS, 0)
            }

            34 ->
                if (progress == 0) {
                    playerl(FaceAnim.HALF_ASKING, "I haven't found any. Where were they headed?").also { stage++ }
                } else {
                    player(
                        "I've found $progress, but they're kind of hard to find.",
                        "Where were they headed again?",
                    ).also { stage++ }
                }

            35 -> npc(FaceAnim.OLD_NORMAL, "One was to go to a jungle to the south.").also { stage++ }
            36 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "One was to go to the land of the gnomes and explore",
                    "their holdings.",
                ).also {
                    stage++
                }
            37 -> npc(FaceAnim.OLD_NORMAL, "One was to explore where the White Knights rule.").also { stage++ }
            38 -> npc(FaceAnim.OLD_NORMAL, "One was to go to a place of great heat.").also { stage++ }
            39 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "If I knew more of their locations I would go myself.",
                    "Now go and finish the job.",
                ).also {
                    stage++
                }

            40 -> end()
            41 -> npc(FaceAnim.OLD_NORMAL, "Oh, and what did he say that for?").also { stage++ }
            42 ->
                player(
                    "He said to tell you, 'We of the blood are still loyal.",
                    "Pavlov is the messenger.'",
                ).also { stage++ }
            43 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "Ah. Interesting. Well, you have served your purpose.",
                    "Now to award you with something appropriate.",
                ).also {
                    stage++
                }
            44 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "There is a cave near where the fish-men gather. Go to",
                    "the east branch of this cave and seek out the one who lives",
                    "in the Shadow Realm. He will reward you well.",
                ).also {
                    stage++
                }
            45 -> player("Right! Fish-men. Cave. Ghost. Thanks, General!").also { stage++ }
            46 -> npc(FaceAnim.OLD_NORMAL, "Wait! Take this, it is the key to your reward.").also { stage++ }
            47 ->
                sendItemDialogue(
                    player,
                    Items.SEVERED_LEG_10857,
                    "The General hands you a severed leg.",
                ).also { stage++ }
            48 -> player(FaceAnim.SCARED, "Ugh, well, at least it's not another hand.").also { stage++ }
            49 -> npcl(FaceAnim.OLD_NORMAL, "Ah, you have dealt with severed limbs before?").also { stage++ }
            50 -> player("It's a long story.").also { stage++ }
            51 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "Don't worry if you lose it, I can always get more to",
                    "replace it.",
                ).also { stage++ }
            52 -> {
                end()
                addItemOrDrop(player, Items.SEVERED_LEG_10857)
                setAttribute(player, GeneralShadowUtils.GS_SEVERED_LEG, true)
            }
            100 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "He was angry that you killed him.",
                    "Quite natural, really.",
                ).also { stage++ }
            101 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "Now get out of here, you filthy skinbag.",
                    "I will not waste my time on you.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GENERAL_KHAZARD_5566)
    }
}
