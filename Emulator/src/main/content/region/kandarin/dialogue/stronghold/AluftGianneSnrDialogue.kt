package content.region.kandarin.dialogue.stronghold

import content.minigame.gnomecook.handlers.*
import core.api.*
import core.game.component.Component
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.colorize
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.rs.consts.Items
import org.rs.consts.NPCs
import java.util.concurrent.TimeUnit

val gnomeItems =
    arrayOf(
        Items.FRUIT_BATTA_2277,
        Items.TOAD_BATTA_2255,
        Items.CHEESE_PLUSTOM_BATTA_2259,
        Items.WORM_BATTA_2253,
        Items.VEGETABLE_BATTA_2281,
        Items.CHOCOLATE_BOMB_2185,
        Items.VEG_BALL_2195,
        Items.TANGLED_TOADS_LEGS_2187,
        Items.WORM_HOLE_2191,
        Items.TOAD_CRUNCHIES_2217,
        Items.WORM_CRUNCHIES_2205,
        Items.CHOCCHIP_CRUNCHIES_2209,
        Items.SPICY_CRUNCHIES_2213,
        Items.FRUIT_BLAST_9514,
        Items.DRUNK_DRAGON_2092,
        Items.CHOC_SATURDAY_2074,
        Items.SHORT_GREEN_GUY_9510,
        Items.BLURBERRY_SPECIAL_9520,
        Items.PINEAPPLE_PUNCH_9512,
        Items.WIZARD_BLIZZARD_9508,
    )
val ALUFT_ALOFT_BOX = Item(Items.ALUFT_ALOFT_BOX_9477)

@Initializable
class AluftGianneSnrDialogue(
    player: Player? = null,
) : Dialogue(player) {
    var tutorialProgress = -1
    var tutorialComplete = false

    override fun open(vararg args: Any?): Boolean {
        tutorialComplete = player.getAttribute("$GC_BASE_ATTRIBUTE:$GC_TUT_FIN", false)
        tutorialProgress = player.getAttribute("$GC_BASE_ATTRIBUTE:$GC_TUT_PROG", -1)

        if (tutorialComplete) {
            npc("Hello, adventurer. How can I help you?").also { stage = 300 }
            return true
        }

        if (tutorialProgress == -1) {
            npc("Who are you and what do you want?").also { stage = END_DIALOGUE }
            return true
        }

        if (tutorialProgress == 0) {
            npc("Hello, adventurer. I heard from my son", "that you'd like to do some work.").also { stage = 0 }
            return true
        }

        npc("Hello, adventurer. How goes the training?")
        stage = tutorialProgress
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> player(FaceAnim.HAPPY, "Yes, how do I get started?").also { stage++ }
            1 -> npc("Well first thing's first I need to teach", "you how to cook!").also { stage++ }
            2 -> player(FaceAnim.THINKING, "But I already-").also { stage++ }
            3 ->
                npc(
                    "Stop whatever it is you're saying, no one knows",
                    "how to cook gnome food except gnomes!",
                ).also { stage++ }
            4 -> player("Alright, go on...").also { stage++ }
            5 ->
                npc(
                    "Alright, first thing I want you to do is",
                    "make me a toad batta. Here's all the",
                    "ingredients, now get to work!",
                ).also {
                    stage++
                }
            6 -> {
                end()
                val items =
                    arrayOf(
                        Item(Items.BATTA_TIN_2164),
                        Item(Items.GIANNE_DOUGH_2171),
                        Item(Items.EQUA_LEAVES_2128),
                        Item(Items.GNOME_SPICE_2169),
                        Item(Items.CHEESE_1985),
                        Item(Items.TOADS_LEGS_2152),
                    )
                if (!player.inventory.hasSpaceFor(*items)) {
                    player.dialogueInterpreter.sendDialogue("You don't have space for the items.")
                } else {
                    player.inventory.add(*items)
                    setAttribute(player, "/save:$GC_BASE_ATTRIBUTE:$GC_TUT_PROG", 10)
                }
            }

            10 ->
                if (player.inventory.containsItem(Item(Items.TOAD_BATTA_2255))) {
                    player("Very well! I have the batta right here!").also { stage = 11 }
                } else {
                    player("Not well, I haven't got the batta yet.").also { stage = END_DIALOGUE }
                }
            11 -> npc("Very well, hand it over then!").also { stage++ }
            12 -> {
                player.inventory.remove(Item(Items.TOAD_BATTA_2255))
                setAttribute(player, "/save:$GC_BASE_ATTRIBUTE:$GC_TUT_PROG", 13)
                sendDialogue(player, "You hand over the toad batta.").also { stage++ }
            }
            13 ->
                npc(
                    "Very nicely done. Now I would like you to make me",
                    "toad crunchies. Here's everything you need.",
                ).also {
                    stage++
                }
            14 -> {
                end()
                val items =
                    arrayOf(
                        Item(Items.CRUNCHY_TRAY_2165),
                        Item(Items.EQUA_LEAVES_2128),
                        Item(Items.GIANNE_DOUGH_2171),
                        Item(Items.TOADS_LEGS_2152, 2),
                    )
                if (!player.inventory.hasSpaceFor(*items)) {
                    sendDialogue(player, "You don't have enough space for the items.")
                } else {
                    player.inventory.add(*items)
                    setAttribute(player, "/save:$GC_BASE_ATTRIBUTE:$GC_TUT_PROG", 15)
                }
            }

            15 ->
                if (inInventory(player, Items.TOAD_CRUNCHIES_2217)) {
                    player("Very well! I have the crunchies right here!").also { stage = 16 }
                } else {
                    player("Not well, I haven't got the crunchies yet.").also { stage = END_DIALOGUE }
                }

            16 -> npc("Very well, hand it over then!").also { stage++ }
            17 -> {
                player.inventory.remove(Item(Items.TOAD_CRUNCHIES_2217))
                setAttribute(player, "/save:$GC_BASE_ATTRIBUTE:$GC_TUT_PROG", 18)
                player.dialogueInterpreter.sendDialogue("You hand over the toad crunchies.").also { stage++ }
            }

            18 ->
                npc("Very nice indeed. Now I'd like you to go see my friend", "Blurberry at the bar.").also {
                    stage = END_DIALOGUE
                }

            300 -> options("I'd like to take on a hard job.", "I'd like an easy job please.").also { stage++ }
            301 ->
                end().also {
                    when (buttonId) {
                        1 -> getJob(GnomeCookingTipper.LEVEL.HARD)
                        2 -> getJob(GnomeCookingTipper.LEVEL.EASY)
                    }
                }

            else -> player("Uhhhh, good.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = AluftGianneSnrDialogue(player)

    override fun npc(vararg messages: String?): Component = super.npc(FaceAnim.OLD_NORMAL, *messages)

    override fun getIds(): IntArray = intArrayOf(NPCs.ALUFT_GIANNE_SNR_850)

    private fun getJob(level: GnomeCookingTipper.LEVEL) {
        if (!player.inventory.containsItem(ALUFT_ALOFT_BOX) && !player.bank.containsItem(ALUFT_ALOFT_BOX)) {
            player.inventory.add(ALUFT_ALOFT_BOX)
        }
        if (getAttribute(player, "$GC_BASE_ATTRIBUTE:$GC_JOB_ORDINAL", -1) != -1) {
            sendDialogue(player, "You already have a job.")
        } else {
            GlobalScope.launch {
                var job = GnomeCookingTask.values().random()
                while (job.level != level) {
                    job = GnomeCookingTask.values().random()
                }
                val item = Item(gnomeItems.random())
                setAttribute(player, "$GC_BASE_ATTRIBUTE:$GC_JOB_ORDINAL", job.ordinal)
                setAttribute(player, "$GC_BASE_ATTRIBUTE:$GC_NEEDED_ITEM", item)
                sendDialogueLines(
                    player,
                    "I need to deliver a ${item.name.lowercase()} to ${NPC(job.npc_id).name.lowercase()},",
                    "who is ${job.tip}",
                )
                GameWorld.Pulser.submit(GnomeRestaurantPulse(player, if (level == GnomeCookingTipper.LEVEL.HARD) 11L else 6L))
            }
        }
    }

    internal class GnomeRestaurantPulse(
        val player: Player,
        val minutes: Long,
    ) : Pulse() {
        var endTime = 0L
        var timerMsgSent = false

        init {
            endTime = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(minutes)
        }

        override fun pulse(): Boolean {
            val isComplete = player.getAttribute("$GC_BASE_ATTRIBUTE:$GC_JOB_ORDINAL", -1) == -1

            val minsLeft = TimeUnit.MILLISECONDS.toMinutes(endTime - System.currentTimeMillis())

            if (minsLeft % 2L == 0L && !timerMsgSent) {
                timerMsgSent = true
                sendMessage(player, colorize("%RYou have $minsLeft minutes remaining on your job."))
            } else if (minsLeft % 2L != 0L) {
                timerMsgSent = false
            }

            if (System.currentTimeMillis() >= endTime) {
                sendMessage(player, colorize("%RYou have run out of time and your job has expired."))
                removeAttribute(player, "$GC_BASE_ATTRIBUTE:$GC_JOB_ORDINAL")
                removeAttribute(player, "$GC_BASE_ATTRIBUTE:$GC_JOB_COMPLETE")
                removeAttribute(player, "$GC_BASE_ATTRIBUTE:$GC_NEEDED_ITEM")
                return true
            }

            return isComplete
        }
    }
}
