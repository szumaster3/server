package content.region.misthalin.dialogue.varrock

import core.api.getStatLevel
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.shops.Shops.Companion.openId
import core.game.world.GameWorld
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class SaniDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc("Greetings, " + player.username, "I sell weapons and armour.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                options(
                    "Let me see your stock.",
                    "Where do I get other items, like gloves?",
                    "How do I open these box sets?",
                ).also { stage++ }

            1 ->
                when (buttonId) {
                    1 -> player("Let me see your stock.").also { stage++ }
                    2 -> player("Where do I get other items, like gloves and capes?").also { stage = 6 }
                    3 -> player("How exactly do I open these box sets that you", "are selling?").also { stage = 10 }
                }

            2 -> options("Weapons", "Armour").also { stage++ }
            3 ->
                when (buttonId) {
                    1 ->
                        if (getStatLevel(player, Skills.ATTACK) < 30) {
                            openWeaponShop(player, 204)
                            end()
                        } else {
                            options("Weapons (Bronze - Mithril)", "Weapons (Adamant - Dragon)").also { stage++ }
                        }

                    2 -> end()
                }

            4 ->
                when (buttonId) {
                    1 -> {
                        openWeaponShop(player, 204)
                        end()
                    }

                    2 -> {
                        openWeaponShop(player, 205)
                        end()
                    }
                }
            6 ->
                npc(
                    "Gloves of all kinds may be feely purchased from the",
                    "Culinaromancer's chest below the Lumbridge Castle.",
                    "On the other hand, high-tier armours can be obtained",
                    "by defeating high-levelled bosses, like the Corporeal Beast.",
                ).also {
                    stage++
                }
            7 ->
                npc(
                    "Bossing is not the only source of high-tier armour.",
                    "Try Slayer or Barrows, catching Dragon Implings or",
                    "perhaps even defeating monsters inside the TzHaar",
                    "cave for fabled onyx equipment is more your style.",
                ).also {
                    stage++
                }
            8 ->
                player(
                    "I see...",
                    "So this means that you only sell common armours",
                    "to help adventurers like myself get started in the world",
                    "of ${GameWorld.settings!!.name}.",
                ).also {
                    stage++
                }
            9 ->
                npc(
                    "Indeed, that is true. If you have any more questions",
                    "The ${GameWorld.settings!!.name} guide near the green portal should be able",
                    "to assist you further.",
                ).also {
                    stage =
                        0
                }
            10 ->
                npc(
                    "Take the box set to the Grand Exchange clerk. Right",
                    "click her and select 'sets'. Next, right-click the box",
                    "item that is in your inventory to exchange the set for",
                    "the items that are inside the box.",
                ).also {
                    stage++
                }
            11 ->
                npc(
                    "You may always convert the individual items back into",
                    "a box set at any time in order to save banking",
                    "space.",
                ).also {
                    stage =
                        0
                }
        }
        return true
    }

    private fun openWeaponShop(
        player: Player,
        uid: Int,
    ) {
        openId(player, uid)
    }

    override fun newInstance(player: Player): Dialogue {
        return SaniDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SMITHING_TUTOR_4905)
    }
}
