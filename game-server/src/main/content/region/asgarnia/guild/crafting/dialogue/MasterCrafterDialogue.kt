package content.region.asgarnia.guild.crafting.dialogue

import core.api.Container
import core.api.addItemOrDrop
import core.api.hasLevelStat
import core.api.removeItem
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

/**
 * The Master crafter dialogue.
 */
@Initializable
class MasterCrafterDialogue : Dialogue {

    constructor()

    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.FRIENDLY, "Hello, and welcome to the Crafting Guild. We're running a master crafting event currently, we're inviting crafters from all over the land to come here and use our top notch workshops!")
        stage = if (npc.id != NPCs.MASTER_CRAFTER_805) END_DIALOGUE else 0
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                options("Skillcape of Crafting", "Nevermind.")
                stage++
            }

            1 -> if (buttonId == 1) {
                if (hasLevelStat(player, Skills.CRAFTING, 99)) {
                    player(FaceAnim.ASKING, "Hey, could I buy a Skillcape of Crafting?")
                    stage = 10
                } else {
                    player(FaceAnim.ASKING, "Hey, what is that cape you're wearing? I don't recognize it.")
                    stage++
                }
            } else {
                player("Nevermind.")
                stage = END_DIALOGUE
            }

            2 -> {
                npcl(
                    FaceAnim.FRIENDLY,
                    "This? This is a Skillcape of Crafting. It is a symbol of my ability and standing here in the Crafting Guild."
                )
                stage++
            }

            3 -> {
                npcl(
                    FaceAnim.FRIENDLY,
                    "If you should ever achieve level 99 Crafting come and talk to me and we'll see if we can sort you out with one."
                )
                stage = END_DIALOGUE
            }

            10 -> {
                npcl(FaceAnim.HAPPY, "Certainly! Right after you pay me 99000 coins.")
                stage++
            }

            11 -> {
                options("Okay, here you go.", "No thanks.")
                stage++
            }

            12 -> if (buttonId == 1) {
                player(FaceAnim.FRIENDLY, "Okay, here you go.")
                stage++
            } else {
                player(FaceAnim.HALF_THINKING, "No, thanks.")
                stage = END_DIALOGUE
            }

            13 -> if (!removeItem<Item>(player, Item(COINS, 99000), Container.INVENTORY)) {
                npcl(FaceAnim.NEUTRAL, "You don't have enough coins for a cape.")
                stage = END_DIALOGUE
            } else {
                addItemOrDrop(
                    player,
                    if (player.getSkills().masteredSkills >= 1) CRAFTING_SKILL_CAPE_TRIMMED else CRAFTING_SKILL_CAPE,
                    1
                )
                addItemOrDrop(player, SKILL_CAPE_HOOD, 1)
                npcl(FaceAnim.HAPPY, "There you go! Enjoy.")
                stage = END_DIALOGUE
            }

            20 -> {
                npcl(FaceAnim.NEUTRAL, "Where's your brown apron? You can't come in here unless you're wearing one.")
                stage++
            }

            21 -> {
                player(FaceAnim.HALF_GUILTY, "Err... I haven't got one.")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(
            NPCs.MASTER_CRAFTER_805,
            NPCs.MASTER_CRAFTER_2732,
            NPCs.MASTER_CRAFTER_2733
        )
    }

    companion object {
        private val CRAFTING_SKILL_CAPE: Int = Items.CRAFTING_CAPE_9780
        private val CRAFTING_SKILL_CAPE_TRIMMED: Int = Items.CRAFTING_CAPET_9781
        private val SKILL_CAPE_HOOD: Int = Items.CRAFTING_HOOD_9782
        private val COINS: Int = Items.COINS_995
    }
}
