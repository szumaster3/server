package content.global.dialogue

import core.api.*
import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.RegionManager
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Components
import org.rs.consts.NPCs

@Initializable
class PetshopOwnerDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private var dogName: String? = null
    private var puppy: Item? = null

    override fun open(vararg args: Any?): Boolean {
        if (args.size > 1) {
            val npcs: List<NPC> = RegionManager.getLocalNpcs(player)
            for (n in npcs) if (n.id == 6893) npc = n
            dogName = args[0].toString()
            puppy = args[1] as Item
            player("No, the $dogName.")
            stage = 699
            return true
        }
        npc = args[0] as NPC
        options(
            "Can I see your shop, please?",
            "How much is that puppy in the window?",
            "So, what sorts of pets are available?",
        )
        stage = 1
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            1 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HAPPY, "Can I see your shop please?").also { stage = 100 }
                    2 -> player(FaceAnim.HALF_ASKING, "How much is that puppy in the window?").also { stage = 200 }
                    3 -> player(FaceAnim.HALF_ASKING, "So, what sorts of pets are available?").also { stage = 300 }
                }
            100 -> {
                end()
                openNpcShop(player, npc.id)
            }
            200 -> npc(FaceAnim.HALF_ASKING, "The one with the waggly tail?").also { stage++ }
            201 -> {
                end()
                openInterface(player, Components.PICK_A_PUPPY_668)
            }
            300 ->
                npc(
                    FaceAnim.FRIENDLY,
                    "Well, here we sell dogs, but we also have supplies for any",
                    "other creatures you might want to raise.",
                ).also {
                    stage++
                }
            301 -> player(FaceAnim.HALF_ASKING, "Such as?").also { stage++ }
            302 ->
                npc(
                    FaceAnim.FRIENDLY,
                    "Well, we sell nuts. Those can be used to feed squirrels. If",
                    "you want to capture a squirrel, you'll need to use the nuts",
                    "on the trap you set, as the little scamps won't be fooled",
                    "by anything else.",
                ).also {
                    stage++
                }
            303 -> player(FaceAnim.FRIENDLY, "I'll bear that in mind.").also { stage++ }
            304 ->
                npc(
                    FaceAnim.FRIENDLY,
                    "There are also a number of fabulous and exotic lizards in",
                    "Karamja. Some can be caught easily in a box trap, while",
                    "other will need to be raised from an egg.",
                ).also {
                    stage++
                }
            305 -> player(FaceAnim.HAPPY, "Thanks a lot! You've been very helpfull.").also { stage++ }
            306 ->
                npc(
                    FaceAnim.HAPPY,
                    "It's always a pleasure to help a fellow animal-lover. Come",
                    "back and visit soon.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            699 -> npc(FaceAnim.FRIENDLY, "500 gold.").also { stage++ }
            700 -> player(FaceAnim.HALF_ASKING, "Isn't that a little steep?").also { stage++ }
            701 ->
                npc(
                    FaceAnim.FRIENDLY,
                    "Well, if we gave them away for free then people would",
                    "just buy them and dump them without a care.",
                ).also {
                    stage++
                }
            702 -> npc(FaceAnim.FRIENDLY, "Dogs are a big responsibility and should be cared for.").also { stage++ }
            703 ->
                npc(
                    FaceAnim.FRIENDLY,
                    "If a person in unwilling to invest 500 coins, then they",
                    "don't deserve to have the puppy in the first place.",
                ).also {
                    stage++
                }
            704 -> npc(FaceAnim.HALF_ASKING, "So, do you still want one?").also { stage++ }
            705 -> options("Okay, I'll take the $dogName.", "No thanks.").also { stage++ }
            706 ->
                when (buttonId) {
                    1 -> player(FaceAnim.FRIENDLY, "Okay, I'll take the $dogName.").also { stage = 707 }
                    2 -> end()
                }
            707 -> {
                if (freeSlots(player) == 0) {
                    end()
                    npc(
                        FaceAnim.ASKING,
                        "Where are you going to put it, on your head? You can't",
                        "buy a puppy unless you have space to hold it.",
                    )
                    sendMessage(player, "You don't have enough inventory space.")
                    return true
                }
                if (!removeItem(player, (Item(995, 500)))) {
                    end()
                    sendMessage(player, "You don't the required coins in order to do this.")
                } else {
                    end()
                    addItem(player, puppy!!.id)
                    npc(FaceAnim.HAPPY, "There you go! I hope you two get on.")
                }
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(
            NPCs.PET_SHOP_OWNER_6750,
            NPCs.PET_SHOP_OWNER_6751,
            NPCs.PET_SHOP_OWNER_6892,
            NPCs.PET_SHOP_OWNER_6893,
            NPCs.PET_SHOP_OWNER_6898,
        )
    }
}
