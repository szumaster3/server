package content.region.karamja.dialogue.brimhaven

import content.global.travel.charter.Charter
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.item.Item
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class CustomsOfficerDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (args.size > 1) {
            if (player.getQuestRepository().isComplete(Quests.PIRATES_TREASURE)) {
                if (inInventory(player, Items.KARAMJAN_RUM_431)) {
                    npc("Aha, trying to smuggle rum are we?").also { stage = 900 }
                    return true
                }
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Well you've got some odd stuff, but it's all legal. Now",
                    "you need to pay a boarding charge of 30 coins.",
                ).also {
                    stage =
                        121
                }
                return true
            }
        }
        npc(FaceAnim.HALF_GUILTY, "Can I help you?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        var amount = if (isDiaryComplete(player!!, DiaryType.KARAMJA, 0)) 15 else 30
        when (stage) {
            0 -> options("Can I journey on this ship?", "Does Karamja have unusual customs then?").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_GUILTY, "Can I journey on this ship?").also { stage = 10 }
                    2 -> player(FaceAnim.HALF_GUILTY, "Does Karamja have unusual customs then?").also { stage = 20 }
                }

            10 -> npc(FaceAnim.HALF_GUILTY, "You need to be searched before you can board?").also { stage++ }
            11 ->
                options(
                    "Why?",
                    "Search away, I have nothing to hide.",
                    "You're not putting your hands on my things!",
                ).also {
                    stage++
                }

            12 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_GUILTY, "Why?").also { stage = 110 }
                    2 -> player(FaceAnim.HALF_GUILTY, "Search away, I have nothing to hide.").also { stage = 120 }
                    3 ->
                        player(
                            FaceAnim.HALF_GUILTY,
                            "You're not putting your hands on my things!",
                        ).also { stage = 130 }
                }

            20 -> npc(FaceAnim.HALF_GUILTY, "I'm not that sort of customs officer.").also { stage = END_DIALOGUE }
            110 ->
                npc(FaceAnim.HALF_GUILTY, "Because Asgarnia has banned the import of intoxicating", "spirits.").also {
                    stage =
                        END_DIALOGUE
                }

            120 -> {
                if (inInventory(player, Items.KARAMJAN_RUM_431)) {
                    npc("Aha, trying to smuggle rum are we?").also { stage = 900 }
                    return true
                }
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Well you've got some odd stuff, but it's all legal. Now",
                    "you need to pay a boarding charge of 30 coins.",
                ).also {
                    stage++
                }
            }

            121 ->
                if (isDiaryComplete(player!!, DiaryType.KARAMJA, 0)) {
                    sendNPCDialogue(
                        player,
                        NPCs.CUSTOMS_OFFICER_380,
                        "Hang on a sec, didn't you earn Karamja gloves? I thought I'd seen you helping around the island. You can go on half price, mate.",
                    ).also {
                        stage++
                    }
                } else {
                    options("Ok.", "Oh, I'll not bother then.").also { stage = 123 }
                }

            122 -> options("Ok.", "Oh, I'll not bother then.").also { stage++ }

            123 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_GUILTY, "Ok.").also { stage = 210 }
                    2 -> player(FaceAnim.HALF_GUILTY, "Oh, I'll not bother then.").also { stage = END_DIALOGUE }
                }

            130 -> npc(FaceAnim.HALF_GUILTY, "You're not getting on this ship then.").also { stage = END_DIALOGUE }

            210 -> {
                if (!inInventory(player, Items.COINS_995, amount)) {
                    sendMessage(player!!, "You can not afford that.").also { stage = END_DIALOGUE }
                    return true
                }
                if (!removeItem(player, Item(Items.COINS_995, amount))) {
                    sendMessage(player!!, "You can not afford that.").also { stage = END_DIALOGUE }
                    return true
                } else {
                    end()
                    var charter: Charter? = null
                    if (player.location.getDistance(LOCATIONS[0]) < 40) {
                        charter = Charter.BRIMHAVEN_TO_ARDOUGNE
                    }
                    if (player.location.getDistance(LOCATIONS[1]) < 40) {
                        charter = Charter.MUSA_POINT_TO_PORT_SARIM
                    }

                    sendMessage(player, "You pay $amount coins and board the ship.")
                    charter!!.sail(player)
                }
            }

            900 -> player(FaceAnim.HALF_GUILTY, "Umm... it's for personal use?").also { stage = 901 }
            901 -> {
                var i = 0
                while (i < amountInInventory(player, Items.KARAMJAN_RUM_431)) {
                    removeItem(player, Items.KARAMJAN_RUM_431)
                    i++
                }
                sendMessage(player, "The customs officer confiscates your rum.")
                sendMessage(player, "You will need to find some way to smuggle it off the island...")
                end()
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return CustomsOfficerDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.CUSTOMS_OFFICER_380)
    }

    companion object {
        private val LOCATIONS = arrayOf(Location.create(2771, 3227, 0), Location.create(2954, 3147, 0))
    }
}
