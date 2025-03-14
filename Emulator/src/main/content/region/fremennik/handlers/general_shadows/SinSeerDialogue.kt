package content.region.fremennik.handlers.general_shadows

import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class SinSeerDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        if (getAttribute(player, GeneralShadowUtils.GS_SIN_SEER_TALK, false)) {
            options("I lost the note.", "Yes, I should get to that.").also { stage = 26 }
            return true
        }
        if (getAttribute(player, GeneralShadowUtils.START_GENERAL_SHADOW, false)) {
            player("I'm looking for the Sin Seer.")
            return true
        }
        npcl(
            FaceAnim.SCARED,
            "My inner eye is clouded from the wave of darkness; give me a moment. What do you want? Augh! Your sins! They BLIND me!",
        ).also {
            stage =
                END_DIALOGUE
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npc(
                    FaceAnim.THINKING,
                    "Are you another one of those 'holier than thou' fools",
                    "wanting to redeem themselves?",
                ).also {
                    stage++
                }
            1 ->
                player(
                    FaceAnim.CALM,
                    "Uh, no actually. I'm trying to join a really evil guy's",
                    "team.",
                ).also { stage++ }
            2 -> npcl(FaceAnim.FRIENDLY, "Really? Finally, something worthwhile.").also { stage++ }
            3 ->
                npc(
                    "It was fun messing with those devotees the first few",
                    "times but, after a while, condemning people to an",
                    "eternity of damnation gets a bit dull.",
                ).also {
                    stage++
                }
            4 ->
                npcl(
                    FaceAnim.HALF_ASKING,
                    "So, whom are you trying to hook up with? Dark priest? Warlord? Penguin?",
                ).also {
                    stage++
                }
            5 -> playerl(FaceAnim.FRIENDLY, "Uh, I want to prove to General Khazard I'm trustworthy.").also { stage++ }
            6 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Oh, that old fart? Is he still around? Humph. I dunno if I can be bothered.",
                ).also {
                    stage++
                }
            7 -> options("Bribe", "Plead").also { stage++ }
            8 ->
                when (buttonId) {
                    1 ->
                        player(
                            "What if I pay you for the trouble - would you then? I",
                            "can't imagine you make much money, seeing people's",
                            "sins and all.",
                        ).also {
                            stage++
                        }
                    2 -> player("PLEASE! I want to know what he's up to.").also { stage = 18 }
                }
            9 -> npc("Oh, there's money in it. I just don't get invited to", "many parties.").also { stage++ }
            10 ->
                npc(
                    "You got me all excited about evil plots, and now it's just",
                    "a matter of trust. I really can't be bothered to strain",
                    "myself on that account.",
                ).also {
                    stage++
                }
            11 ->
                player(
                    "Fine, you don't actually have to see my sins, just",
                    "pronounce me to be trustworthy. I'll pay you and be",
                    "on my way.",
                ).also {
                    stage++
                }
            12 ->
                npc(
                    "He'll know it's a lie. But I do need the cash, so I'll do",
                    "it. Gimme 40 gold coins and you'll be as",
                    "trustworthy as a newborn.",
                ).also {
                    stage++
                }
            13 -> options("Here's the money.", "Never mind.").also { stage++ }
            14 ->
                when (buttonId) {
                    1 ->
                        if (!removeItem(player, Item(Items.COINS_995, 40))) {
                            sendDialogue(player!!, "You don't have enough money.").also { stage = END_DIALOGUE }
                        } else {
                            player("Here's the money.").also { stage++ }
                        }
                    2 -> player("Never mind.").also { stage = END_DIALOGUE }
                }
            15 ->
                npc(
                    "Fine. I declare you trustworthy. If you consider bribing",
                    "a seer a trustworthy transaction.",
                ).also {
                    stage++
                }
            16 ->
                npc(
                    "Give this note to Khazard it will prove you have been",
                    "to see me. You receive a note from the Sin Seer.",
                ).also {
                    stage++
                }
            17 -> {
                end()
                sendItemDialogue(player, Items.SIN_SEERS_NOTE_10856, "You receive a note from the Sin Seer.")
                addItemOrDrop(player, Items.SIN_SEERS_NOTE_10856)
                setAttribute(player, GeneralShadowUtils.GS_SIN_SEER_TALK, true)
            }
            18 ->
                npcl(
                    FaceAnim.HALF_ASKING,
                    "Ooooh, you're going to learn of his plans and then stab him in the back?",
                ).also {
                    stage++
                }
            19 -> player("Absolutely.").also { stage++ }
            20 -> npc("Fine, give me 40 gold coins and I'll take a look at", "your sins.").also { stage++ }
            21 -> options("Here's the money.", "Never mind.").also { stage++ }
            22 ->
                when (buttonId) {
                    1 -> player("Here's the money.").also { stage++ }
                    2 -> player("Never mind.").also { stage = END_DIALOGUE }
                }
            23 ->
                if (!removeItem(player, Item(Items.COINS_995, 40))) {
                    sendDialogue(player!!, "You don't have enough money.").also { stage = END_DIALOGUE }
                } else {
                    npcl(
                        FaceAnim.NEUTRAL,
                        "You have done many great deeds...but many terrible deeds. Sometimes you do them by accident, but they are still sins.",
                    ).also {
                        stage++
                    }
                }
            24 -> npc("No, I would not trust you to walk my dog.").also { stage++ }
            25 -> npc("Give this note to Khazard it will prove you have been to see me.").also { stage++ }
            26 -> {
                end()
                sendItemDialogue(player, Items.SIN_SEERS_NOTE_10856, "You receive a note from the Sin Seer.")
                addItemOrDrop(player, Items.SIN_SEERS_NOTE_10856)
                setAttribute(player, GeneralShadowUtils.GS_SIN_SEER_TALK, true)
            }
            27 ->
                when (buttonId) {
                    1 -> player("I lost the note.").also { stage++ }
                    2 -> player("Yes, I should get to that.").also { stage = END_DIALOGUE }
                }

            28 -> {
                val hasNote = hasAnItem(player, Items.SIN_SEERS_NOTE_10856).container != null
                end()
                if (!hasNote) {
                    addItemOrDrop(player, Items.SIN_SEERS_NOTE_10856)
                    sendItemDialogue(player, Items.SIN_SEERS_NOTE_10856, "You receive a note from the Sin Seer.")
                } else {
                    npc("You already have one. Get on with you!")
                }
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SIN_SEER_5571)
    }
}
