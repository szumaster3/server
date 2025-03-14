package content.global.activity.penguinhns

import content.data.GameAttributes
import core.api.*
import core.game.component.Component
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.tools.END_DIALOGUE
import core.tools.Log
import org.rs.consts.Items
import org.rs.consts.NPCs

class LarryDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        options("Can I have a spy notebook?", "Can I have a hint?", "I'd like to turn in my points.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                when (buttonId) {
                    1 -> player("Can I have a spy notebook?").also { stage++ }
                    2 -> player("Can I have a hint?").also { stage = 10 }
                    3 -> player("I'd like to turn in my points.").also { stage = 20 }
                }

            1 -> {
                npc("Sure!")
                setAttribute(player, GameAttributes.ACTIVITY_PENGUINS_HNS, true)
                addItem(player, Items.SPY_NOTEBOOK_13732, 1)
                stage = 1000
            }
            10 -> npc("Yes, give me just one moment...").also { stage++ }
            11 -> {
                val hint = Penguin.values()[PenguinManager.penguins.random()].hint
                npcl(FaceAnim.FRIENDLY, "One is $hint.")
                stage = END_DIALOGUE
            }

            20 ->
                if (player.getAttribute(GameAttributes.ACTIVITY_PENGUINS_HNS_SCORE, 0) > 0) {
                    npc("Sure thing, what would you like to be", "rewarded with?").also { stage++ }
                } else {
                    npc("Uh, you don't have any points", "to turn in.").also { stage = END_DIALOGUE }
                }

            21 -> options("Coins", "Experience").also { stage++ }
            22 ->
                when (buttonId) {
                    1 -> {
                        end()
                        addItem(
                            player,
                            Items.COINS_995,
                            6500 * player.getAttribute(GameAttributes.ACTIVITY_PENGUINS_HNS_SCORE, 0),
                        )
                        removeAttribute(player, GameAttributes.ACTIVITY_PENGUINS_HNS_SCORE)
                        player("Thanks!")
                    }

                    2 -> {
                        setAttribute(player, "caller", this)
                        player.interfaceManager
                            .open(
                                Component(134).setCloseEvent { _: Player?, _: Component? ->
                                    player.interfaceManager.openDefaultTabs()
                                    removeAttribute(player, "lamp")
                                    player.unlock()
                                    true
                                },
                            ).also { end() }
                    }
                }
        }
        return true
    }

    override fun handleSelectionCallback(
        skill: Int,
        player: Player,
    ) {
        val points = player.getAttribute(GameAttributes.ACTIVITY_PENGUINS_HNS_SCORE, 0)
        if (points == 0) {
            sendMessage(player, "Sorry, but you have no points to redeem.")
            return
        }
        val level = getStatLevel(player, skill)
        val expGained = points?.toDouble()?.times((level * 25))
        log(this.javaClass, Log.INFO, "exp: $expGained")
        player.skills.addExperience(skill, expGained!!)
        setAttribute(player, GameAttributes.ACTIVITY_PENGUINS_HNS_SCORE, 0)
    }

    override fun newInstance(player: Player?): Dialogue {
        return LarryDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.LARRY_5424)
    }
}
