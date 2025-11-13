package content.region.asgarnia.crafting_guild

import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.plugin.ClassScanner
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Scenery

class CraftingGuildPlugin : InteractionListener {
    override fun defineListeners() {
        ClassScanner.definePlugin(MasterCrafterDialogue())

        /*
         * Handles access to the Crafting Guild.
         */

        on(GUILD_DOORS, IntType.SCENERY, "open") { player, door ->
            if (!hasLevelStat(player, Skills.CRAFTING, 40)) {
                sendNPCDialogueLines(
                    player,
                    NPCs.MASTER_CRAFTER_805,
                    FaceAnim.NEUTRAL,
                    false,
                    "Sorry, only experienced crafters are allowed in here.",
                    "You must be level 40 or above to enter."
                )
                return@on false
            }

            val doorScenery = door.asScenery()

            if (player.location.y < 3289) {
                DoorActionHandler.handleAutowalkDoor(player, doorScenery)
                return@on true
            }

            val hasApron = inEquipment(player, BROWN_APRON)
            val hasCape = anyInEquipment(player, Items.CRAFTING_CAPE_9780, Items.CRAFTING_CAPET_9781)

            if (hasApron || hasCape) {
                DoorActionHandler.handleAutowalkDoor(player, doorScenery)
                sendNPCDialogue(
                    player,
                    NPCs.MASTER_CRAFTER_805,
                    "Welcome to the Guild of Master Craftsmen.",
                    FaceAnim.HAPPY
                )
            } else {
                sendNPCDialogue(
                    player,
                    NPCs.MASTER_CRAFTER_805,
                    "Where's your brown apron? You can't come in here unless you're wearing one.",
                    FaceAnim.HALF_ASKING
                )
                addDialogueAction(player) { _, _ ->
                    sendPlayerDialogue(player, "Err... I haven't got one.", FaceAnim.HALF_THINKING)
                }
            }

            return@on true
        }
    }

    companion object {
        private const val GUILD_DOORS = Scenery.GUILD_DOOR_2647
        private const val BROWN_APRON = Items.BROWN_APRON_1757

        private val CRAFTING_SKILL_CAPE: Int = Items.CRAFTING_CAPE_9780
        private val CRAFTING_SKILL_CAPE_TRIMMED: Int = Items.CRAFTING_CAPET_9781
        private val SKILL_CAPE_HOOD: Int = Items.CRAFTING_HOOD_9782
        private val COINS: Int = Items.COINS_995
    }


    /**
     * The Master crafter dialogue.
     */
    inner class MasterCrafterDialogue : Dialogue {

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

    }

}
