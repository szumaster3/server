package content.region.fremennik.quest.viking.dialogue

import content.region.kandarin.quest.murder.dialogue.PoisonSalesmanDialogueFile
import core.api.addItemOrDrop
import core.api.openDialogue
import core.api.quest.getQuestStage
import core.api.removeItem
import core.game.dialogue.Dialogue
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class PoisonSalesmanDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        options("Talk about the Murder Mystery Quest", "Talk about the Fremennik Trials")
        stage = START_DIALOGUE
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        val murderMysteryStage = getQuestStage(player, Quests.MURDER_MYSTERY)
        val fremennikTrialsStage = getQuestStage(player, Quests.THE_FREMENNIK_TRIALS)
        when (stage) {
            START_DIALOGUE ->
                when (buttonId) {
                    1 -> player("Hello.").also { stage = 1 }
                    2 -> player("Hello.").also { stage = 10 }
                }

            1 ->
                if (murderMysteryStage >= 2) {
                    openDialogue(player, PoisonSalesmanDialogueFile())
                } else {
                    end()
                }

            10 -> {
                if (fremennikTrialsStage == 0) {
                    npc("Come see me if you ever need low-alcohol beer!").also { stage = END_DIALOGUE }
                } else if (fremennikTrialsStage > 30) {
                    npc("Thanks for buying out all that low-alcohol beer!").also { stage = END_DIALOGUE }
                } else if (fremennikTrialsStage > 0) {
                    npc(
                        "Howdy! You seem like someone with discerning taste!",
                        "Howsabout you try my brand new range of alcohol?",
                    ).also { stage++ }
                }
            }

            11 -> player("Didn't you used to sell poison?").also { stage++ }
            12 ->
                npc(
                    "That I did indeed! Peter Potter's Patented",
                    "Multipurpose poison! A miracle of modern apothecarys!",
                    "My exclusive concoction has been tested on...",
                ).also { stage++ }

            13 -> player("Uh, yeah. I've already heard the sales pitch.").also { stage++ }
            14 -> npc("Sorry stranger, old habits die hard I guess.").also { stage++ }
            15 -> player("So you don't sell poison anymore?").also { stage++ }
            16 ->
                npc(
                    "Well I would, but I ran out of stock. Business wasn't",
                    "helped with that stuff that happened up at the Sinclair",
                    "Mansion much either, I'll be honest.",
                ).also { stage++ }

            17 ->
                npc(
                    "So, being the man of enterprise that I am I decided to",
                    "branch out a little bit!",
                ).also { stage++ }

            18 -> player("Into alcohol?").also { stage++ }
            19 ->
                npc(
                    "Absolutely! The basic premise between alcohol and poison",
                    "is pretty much the same, after all! The difference is that",
                    "my alcohol has a unique property others do not!",
                ).also { stage++ }

            20 -> player("And what is that?").also { stage++ }
            21 -> sendDialogue("The salesman takes a deep breath.").also { stage++ }
            22 ->
                npc(
                    "Ever been too drunk to find your own home? Ever",
                    "wished that you could party away all night long, and",
                    "still wake up fresh as a daisy the next morning?",
                ).also { stage++ }

            23 ->
                npc(
                    "Thanks to the miracles of modern magic we have come",
                    "up with just the solution you need! Peter Potter's",
                    "Patented Party Potions!",
                ).also { stage++ }

            24 ->
                npc(
                    "It looks just like beer! It tastes just like beer! It smells",
                    "just like beer! But... it's not beer!",
                ).also { stage++ }

            25 ->
                npc(
                    "Our mages have mused for many moments to bring",
                    "you this miracle of modern magic! It has all the great",
                    "tastes you'd expect, but contains absolutely no alcohol!",
                ).also { stage++ }

            26 ->
                npc(
                    "That's right! You can drink Peter Potter's Patented",
                    "Party Potion as much as you want, and suffer",
                    "absolutely no ill effects whatsoever!",
                ).also { stage++ }

            27 ->
                npc(
                    "The clean fresh taste you know you can trust, from",
                    "the people who brought you: Peter Potters Patented",
                    "multipurpose poison, Pete Potters peculiar paint packs",
                ).also { stage++ }

            28 ->
                npc(
                    "and Peter Potters paralyzing panic pins. Available now",
                    "from all good stockists! Ask your local bartender now,",
                    "and experience the taste revolution of the century!",
                ).also { stage++ }

            29 -> sendDialogue("He seems to have finished for the time being.").also { stage++ }
            30 -> player("So.. when you say 'all good stockists'...").also { stage++ }
            31 -> npc("Yes?").also { stage++ }
            32 -> player("How many inns actually sell this stuff?").also { stage++ }
            33 ->
                npc(
                    "Well.. nobody has actually bought any yet. Everyone I",
                    "try and sell it to always asks me what exactly the point",
                    "of beer that has absolutely no effect on you is.",
                ).also { stage++ }

            34 -> player("So what is the point?").also { stage++ }
            35 -> npc("Well... Um... Er... Hmmm. You, er, don't get drunk.").also { stage++ }
            36 -> player("I see...").also { stage++ }
            37 ->
                npc(
                    "Aw man.. you don't want any now do you? I've really",
                    "tried to push this product, but I just don't think the",
                    "world is ready for beer that doesn't get you drunk.",
                ).also { stage++ }

            38 ->
                npc(
                    "I'm a man ahead of my time I tell you! It's not that",
                    "my products are bad, it's that they're too good for the",
                    "market!",
                ).also { stage++ }

            39 -> player("Actually, I would like some. How much do you want for it?").also { stage++ }
            40 ->
                npc(
                    "Y-you would??? Um, okay! I knew I still had the old",
                    "salesmans skills going on!",
                ).also { stage++ }

            41 -> npc("I will sell you a keg of it for only 250 gold pieces! So", "What do you say?").also { stage++ }
            42 -> options("Yes", "No").also { stage++ }
            43 ->
                when (buttonId) {
                    1 -> player("Yes please!").also { stage++ }
                    2 -> player("No, thanks.").also { stage = END_DIALOGUE }
                }

            44 ->
                if (!removeItem(player, Item(Items.COINS_995, 250))) {
                    player("I don't seem to have enough.")
                    stage++
                } else {
                    npc("Here you go.")
                    addItemOrDrop(player, Items.LOW_ALCOHOL_KEG_3712, 1)
                    stage = END_DIALOGUE
                }

            45 -> npc("Well come back when you do!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.POISON_SALESMAN_820)
    }
}
