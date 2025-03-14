package content.region.misthalin.quest.anma.dialogue

import content.region.misthalin.quest.anma.AnimalMagnetism
import core.api.quest.setQuestStage
import core.game.dialogue.Dialogue
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.item.Item
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

class WitchDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private var quest: Quest? = null

    override fun open(vararg args: Any): Boolean {
        quest = player.getQuestRepository().getQuest(Quests.ANIMAL_MAGNETISM)
        when (quest!!.getStage(player)) {
            25 -> npc("Hello, hello, my poppet. What brings you to my little", "room?")
            26 ->
                if (!player.inventory.containsItem(IRON_BARS)) {
                    player("I am back.")
                } else {
                    npc("Great, you'll go far! I made some nice painted metal", "toys for you, smookums.")
                }

            27 ->
                if (player.hasItem(AnimalMagnetism.SELECTED_IRON)) {
                    npc(
                        "You were sent to try my patience, weren't you? Go",
                        "away and make that magnet, then hand it to Ava.",
                    ).also { stage = END_DIALOGUE }
                } else {
                    player("I have lost the selected iron...").also { stage++ }
                }

            else -> npc("Hello there, deary. Don't worry; I'm friendly.")
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (quest!!.getStage(player)) {
            25 ->
                when (stage) {
                    0 ->
                        player(
                            "Ava told me to ask you about making magnets.",
                            "Something about natural fields and other stuff. Sounded",
                            "like she needed a farmer, to be honest.",
                        ).also { stage++ }

                    1 ->
                        npc(
                            "Don't worry, deary, I can tell you just what to do and",
                            "you won't have to worry your pretty head about the",
                            "complicated bits.",
                        ).also { stage++ }

                    2 -> player("No need to patronise me quite so much, you know.").also { stage++ }
                    3 ->
                        npc(
                            "I went to anger management classes, my lambkin; that's",
                            "why I was treating you so kindly. It's either this way",
                            "or talking or I'll go back to shoving children into ovens.",
                        ).also { stage++ }

                    4 ->
                        npc(
                            "Just bring me 5 iron bars, though, and you've well on",
                            "the way to never having to talk to me again.",
                        ).also { stage++ }

                    5 -> player("I'll be back.").also { stage++ }
                    6 -> {
                        end()
                        setQuestStage(player, Quests.ANIMAL_MAGNETISM, 26)
                    }
                }

            26 ->
                when (stage) {
                    0 ->
                        if (!player.inventory.containsItem(IRON_BARS)) {
                            npc(
                                "Oh, but sugarpie, I need 5 iron bars, you don't have",
                                "enough. Come back to me quickly with all 5 of them.",
                            ).also { stage++ }
                        } else {
                            player(
                                "Toys? Snookums? What are you on about, you",
                                "deranged old bar?",
                            ).also { stage += 2 }
                        }

                    2 ->
                        npc(
                            "Oh, forget it, then. If you won't react to kindness, I'm",
                            "back to luring infants into my oven. You'll have it on",
                            "your conscience.",
                        ).also { stage++ }

                    3 ->
                        npc(
                            "Go to the iron mine just north-east of Rimmington and",
                            "hit the bar with a plain old smithing hammer while facing",
                            "north. Then take your new magnet to Ava. Poor girl,",
                            "having to deal with whippersnappers like you.",
                        ).also { stage++ }

                    4 ->
                        if (player.inventory.remove(IRON_BARS)) {
                            player.inventory.add(AnimalMagnetism.SELECTED_IRON)
                            setQuestStage(player, Quests.ANIMAL_MAGNETISM, 27)
                            end()
                        }

                    1 -> end()
                }

            27 ->
                when (stage) {
                    0 -> end()
                    1 ->
                        npc(
                            "Oh, deary, deary... I had a feeling. I can",
                            "make you another for five more iron bars.",
                        ).also { stage++ }

                    2 -> {
                        end()
                        if (!player.inventory.containsItem(IRON_BARS)) {
                            player("Okay, I'll go get some.")
                        }
                        if (player.inventory.remove(IRON_BARS)) {
                            player.inventory.add(AnimalMagnetism.SELECTED_IRON)
                        }
                    }
                }

            else ->
                when (stage) {
                    0 ->
                        player(
                            "You look very familiar but I can't remember chatting to",
                            "you before.",
                        ).also { stage++ }
                    1 ->
                        npc(
                            "Well, my sisters used to live here but folk kept on",
                            "killing them. A terrible affair, it was. I knew that no one",
                            "would want to kill a chatty old dear like me, though.",
                        ).also { stage++ }

                    2 -> player("Oh, well...").also { stage++ }
                    3 ->
                        npc(
                            "We still have a terrible problem with local louts, though.",
                            "Poisoning my fish, spilling my compost all over the place",
                            "and causing a nuisance for the dear count.",
                        ).also { stage++ }

                    4 -> player("I think...").also { stage++ }
                    5 ->
                        npc(
                            "I'm sure a nice young sort like you won't be any",
                            "trouble though. In any case, the Professor will soon",
                            "share the secret of chickenisation with me... Not that I'd",
                            "use it these days of course.",
                        ).also { stage++ }

                    6 -> player("I have to go now.").also { stage = END_DIALOGUE }
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.WITCH_5200)
    }

    companion object {
        private val IRON_BARS = Item(Items.IRON_BAR_2351, 5)
    }
}
