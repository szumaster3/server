package content.region.desert.quest.desertrescue.dialogue

import core.game.dialogue.Dialogue
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.item.Item
import org.rs.consts.Quests

class MinecartDriverDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private var quest: Quest? = null

    override fun open(vararg args: Any): Boolean {
        quest = player.getQuestRepository().getQuest(Quests.THE_TOURIST_TRAP)
        when (quest!!.getStage(player)) {
            90 -> npc("Quickly, get in the back of the cart.")
            80 ->
                interpreter.sendDialogue(
                    "The cart driver seems to be fastidiously cleaning his cart.",
                    "It doesn't look as if he wants to be disturbed.",
                )

            else -> player("Hello!")
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (quest!!.getStage(player)) {
            90 ->
                when (stage) {
                    0 -> {
                        player("Okay, sorry.")
                        stage++
                    }

                    1 -> end()
                    24 -> end()
                }

            80 ->
                when (stage) {
                    0 -> {
                        player("Nice cart.")
                        stage++
                    }

                    1 -> {
                        interpreter.sendDialogue("The cart driver looks around at you and tries to weigh you up.")
                        stage++
                    }

                    2 -> {
                        npc("Hmmm.")
                        stage++
                    }

                    3 -> {
                        interpreter.sendDialogue("He tuts to himself and starts checking the wheels.")
                        stage++
                    }

                    4 -> {
                        player("One wagon wheel says to the other, 'I'll see you", "around'.")
                        stage++
                    }

                    5 -> {
                        npc("<col=08088A>-- The cart driver smirks a little. --")
                        stage++
                    }

                    6 -> {
                        interpreter.sendDialogue("He starts checking the steering on the cart.")
                        stage++
                    }

                    7 -> {
                        player("One good turn deserves another.")
                        stage++
                    }

                    8 -> {
                        interpreter.sendDialogue("The cart driver smiles a bit and then turns to you.")
                        stage++
                    }

                    9 -> {
                        npc("Are you trying to get me fired?")
                        stage++
                    }

                    10 -> {
                        player("Fired.... no, shot perhaps!")
                        stage++
                    }

                    11 -> {
                        npc(
                            "Ha ha ha! <col=08088A>-- The cart driver checks that",
                            "<col=08088A>guards aren't watching. -- </col>What're you in fer?",
                        )
                        stage++
                    }

                    12 -> {
                        player("In for a penny in for a pound.")
                        stage++
                    }

                    13 -> {
                        interpreter.sendDialogue("The cart drivers laughs at your pun...")
                        stage++
                    }

                    14 -> {
                        npc("Ha ha ha, oh stop it!")
                        stage++
                    }

                    15 -> {
                        interpreter.sendDialogue("The cart driver seems much happier now.")
                        stage++
                    }

                    16 -> {
                        npc("What can I do for you anyway?")
                        stage++
                    }

                    17 -> {
                        player("Well, you see, it's like this...")
                        stage++
                    }

                    18 -> {
                        npc("Yeah!")
                        stage++
                    }

                    19 -> {
                        player("There's ten gold in it for you if you leave now no", "questions asked.")
                        stage++
                    }

                    20 -> {
                        npc(
                            "If you're going to bribe me, at least make it worth my",
                            "while. Now, let's say 100 Gold pieces should we? Ha ha",
                            "ha!",
                        )
                        stage++
                    }

                    21 -> {
                        player("A hundred it is!")
                        stage++
                    }

                    22 -> {
                        npc("Great!")
                        stage++
                    }

                    23 -> {
                        if (!player.inventory.containsItem(COINS)) {
                            player.packetDispatch.sendMessage("You need 100 gold coins.")
                            end()
                            return true
                        }
                        if (player.inventory.remove(COINS)) {
                            quest!!.setStage(player, 90)
                            npc("Okay, get in the back of the cart then!")
                            stage++
                        }
                    }

                    24 -> end()
                }

            else ->
                when (stage) {
                    0 -> {
                        npc("Hello, what do you want?")
                        stage++
                    }

                    1 -> {
                        player("Nothing, just passing by.")
                        stage++
                    }

                    2 -> end()
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(841)
    }

    companion object {
        private val COINS = Item(995, 100)
    }
}
