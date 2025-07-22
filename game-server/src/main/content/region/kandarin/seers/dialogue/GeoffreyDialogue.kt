package content.region.kandarin.seers.dialogue

import core.Util
import core.api.hasSpaceFor
import core.api.setAttribute
import core.game.dialogue.Dialogue
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class GeoffreyDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        val diary = player?.achievementDiaryManager?.getDiary(DiaryType.SEERS_VILLAGE)
        if (diary?.levelRewarded?.isNotEmpty() == true) {
            player("Hello there. Are you Geoff-erm-Flax? I've been told that", "you'll give me some flax.")
            val lastRewardTime = player.getAttribute("diary:seers:flax-timer", 0L)
            val hasFlaxTimer = lastRewardTime > System.currentTimeMillis()
            val hasSpace = hasSpaceFor(player, Item(Items.FLAX_1780, 1))
            stage = when {
                hasFlaxTimer -> 98
                !hasSpace -> 99
                else -> when (diary.reward) {
                    -1 -> 999
                    0 -> 100
                    1 -> 101
                    2 -> 102
                    else -> 999
                }
            }
        } else {
            stage = 0
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> npc("Yes, I am very busy. Picking GLORIOUS flax.", "The GLORIOUS flax won't pick itself. So I pick it.", "I pick it all day long.").also { stage++ }
            1 -> player("Wow, all that flax must really mount up. What do you do with it all?").also { stage++ }
            2 -> npc("I give it away! I love picking the GLORIOUS flax,", "but, if I let it all mount up, I wouldn't have any", "room for more GLORIOUS flax.").also { stage++ }
            3 -> player("So, you're just picking the flax for fun? You must", "really like flax.").also { stage++ }
            4 -> npc("'Like' the flax? I don't just 'like' flax. The", "GLORIOUS flax is my calling, my reason to live.", "I just love the feeling of it in my hands!").also { stage++ }
            5 -> player("Erm, okay. Maybe I can have some of your spare flax?").also { stage++ }
            6 -> npc("No. I don't trust outsiders. Who knows what depraved", "things you would do with the GLORIOUS flax? Only", "locals know how to treat it right.").also { stage++ }
            7 -> player("I know this area! It's, erm, Seers' Village. There's", "a pub and, er, a bank.").also { stage++ }
            8 -> npc("Pah! You call that local knowledge? Perhaps if you", "were wearing some kind of item from one of the", "seers, I might trust you.").also { stage = 999 }
            98 -> npc("I've already given you your GLORIOUS flax", "for the day. Come back tomorrow.").also { stage = 999 }
            99 -> npc("Yes, but your inventory is full. Come back", "when you have some space for GLORIOUS flax.").also { stage = 999 }
            100, 101, 102 -> {
                val (amount, message) = when (stage) {
                    100 -> 30 to arrayOf(
                        "Yes. The seers have instructed me to give you an",
                        "allowance of 30 GLORIOUS flax a day. I'm not going",
                        "to argue with them, so here you go."
                    )
                    101 -> 60 to arrayOf(
                        "Yes. Stankers has instructed me to give you an",
                        "allowance of 60 GLORIOUS flax a day. I'm not going",
                        "to argue with a dwarf, so here you go."
                    )
                    102 -> 120 to arrayOf(
                        "Yes. Sir Kay has instructed me to give you an",
                        "allowance of 120 GLORIOUS flax a day. I'm not going",
                        "to argue with a knight, so here you go."
                    )
                    else -> 0 to emptyArray()
                }
                npc(*message)
                player?.inventory?.add(Item(Items.FLAX_1780, amount))
                setAttribute(player, "/save:diary:seers:flax-timer", Util.nextMidnight(System.currentTimeMillis()))
                stage = 999
            }
            999 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = GeoffreyDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.GEOFFREY_8590)
}
