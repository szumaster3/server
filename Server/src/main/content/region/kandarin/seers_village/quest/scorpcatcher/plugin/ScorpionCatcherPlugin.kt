package content.region.kandarin.seers_village.quest.scorpcatcher.plugin

import content.data.GameAttributes
import content.region.kandarin.seers_village.quest.scorpcatcher.dialogue.SeerMirrorDialogue
import content.region.kandarin.seers_village.quest.scorpcatcher.dialogue.ThormacRequestDialogue
import core.api.*
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.config.NPCConfigParser
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.plugin.Initializable
import shared.consts.*

/**
 * Represents the Scorpion Catcher quest.
 */
@Initializable
class ScorpionCatcherPlugin : Quest(Quests.SCORPION_CATCHER, 108, 107, 1, Vars.VARP_QUEST_SCORPION_CATCHER_PROGRESS_76, 0, 1, 6), InteractionListener {
    companion object {
        const val ATTRIBUTE_MIRROR = "/save:scorpion_catcher:start-talk"
        const val ATTRIBUTE_SECRET = "/save:scorpion_catcher:secret-room"
        const val ATTRIBUTE_NPC = "/save:scorpion_catcher:dialogues"
        const val ATTRIBUTE_CAGE = "/save:scorpion_catcher:cage-dialogue"

        @JvmStatic
        fun getScorpionLocation(player: Player) {
            submitIndividualPulse(player, object : Pulse() {
                var counter = 0
                override fun pulse(): Boolean {
                    when (counter++) {
                        1 -> sendMessage(player, "The seer produces a small mirror.")
                        3 -> sendMessage(player, "The seer gazes into the mirror.")
                        6 -> sendMessage(player, "The seer smoothes his hair with his hand.")
                        9 -> {
                            setAttribute(player, ATTRIBUTE_MIRROR, true)
                            setAttribute(player, ATTRIBUTE_SECRET, true)
                            openDialogue(player, SeerMirrorDialogue())
                            return true
                        }
                    }
                    return false
                }
            })
        }

        @JvmStatic
        fun getCage(player: Player) {
            submitIndividualPulse(player, object : Pulse() {
                var counter = 0
                override fun pulse(): Boolean {
                    when (counter++) {
                        0 -> {
                            sendMessage(player, "Thormac gives you a cage.")
                            addItemOrDrop(player, Items.SCORPION_CAGE_456)
                        }
                        2 -> {
                            setAttribute(player, ATTRIBUTE_CAGE, true)
                            openDialogue(player, ThormacRequestDialogue())
                            return true
                        }
                    }
                    return false
                }
            })
        }
    }

    override fun drawJournal(player: Player, stage: Int) {
        super.drawJournal(player, stage)
        var line = 11

        if (stage == 0) {
            line(player, "I can start this quest by speaking to !!Thormac?? who is in the", line++)
            line(player, "!!Sorcerer's Tower??, south-west of the !!Catherby??.", line++)
            line++
            line(player, "Requirements:", line++)
            line(player, if (hasLevelStat(player, Skills.PRAYER, 31)) "---Level 31 Prayer/--" else "!!Level 31 Prayer??", line++)
            line++
        }

        if (stage == 10) {
            line(player, "I've spoken to !!Thormac in the Sorcerer's Tower?? south-west", line++, true)
            line(player, "of !!Catherby??. He's lost his pet !!Kharid Scorpions?? and needs", line++, true)
            line(player, "my help to find them.", line++, true)
            line++

            if (getAttribute(player, GameAttributes.LABEL_THORMAC_INTERACTION, false)) {
                line(player, "Thormac told me that I should speak with the !!Seer??,", line++, getAttribute(player, ATTRIBUTE_SECRET, false))
                line(player, "who is located !!north?? of the !!Sorcerers' Tower??.", line++, getAttribute(player, ATTRIBUTE_SECRET, false))
                line++
            }

            if (getAttribute(player, ATTRIBUTE_SECRET, false)) {
                line(player, "I've spoken to a !!Seer?? and been given the location of", line++, true)
                line(player, "one of the !!Kharid Scorpions??.", line++, true)
                line++
                line(player, "The first !!Kharid Scorpion?? is in a secret room near some", line++, getAttribute(player, GameAttributes.LABEL_SCORPION_TAVERLEY, false))
                line(player, "!!nasty spiders?? with two !!coffins?? nearby.", line++, getAttribute(player, GameAttributes.LABEL_SCORPION_TAVERLEY, false))
                line++
                line(player, "I'll need to talk to a !!Seer?? again once I've caught the first", line++, getAttribute(player, ATTRIBUTE_NPC, false))
                line(player, "!!Kharid Scorpion??.", line++, getAttribute(player, ATTRIBUTE_NPC, false))
                line++
            }

            if (getAttribute(player, ATTRIBUTE_NPC, false) || getAttribute(player, GameAttributes.LABEL_SCORPION_OUTPOST, false)) {
                line(player, "The second !!Kharid Scorpion?? has been in a !!village of??", line++, getAttribute(player, GameAttributes.LABEL_SCORPION_OUTPOST, false))
                line(player, "!!uncivilised-looking warriors in the east??. It's been picked up", line++, getAttribute(player, GameAttributes.LABEL_SCORPION_OUTPOST, false))
                line(player, "by some sort of !!merchant??.", line++, getAttribute(player, GameAttributes.LABEL_SCORPION_OUTPOST, false))
                line++
            }

            if (getAttribute(player, ATTRIBUTE_NPC, false) || getAttribute(player, GameAttributes.LABEL_SCORPION_MONASTERY, false)) {
                line(player, "The third !!Kharid Scorpion?? is in some sort of !!upstairs room??", line++, getAttribute(player, GameAttributes.LABEL_SCORPION_MONASTERY, false))
                line(player, "with !!brown clothing on a table??.", line++, getAttribute(player, GameAttributes.LABEL_SCORPION_MONASTERY, false))
                line++
            }

            if (getAttribute(player, GameAttributes.LABEL_SCORPION_TAVERLEY, false) && getAttribute(player, GameAttributes.LABEL_SCORPION_OUTPOST, false) && getAttribute(player, GameAttributes.LABEL_SCORPION_MONASTERY, false)) {
                line(player, "I need to take the !!Kharid Scorpions?? to !!Thormac??.", line++)
                line++
            }
        }

        if (stage == 100) {
            line(player, "I've spoken to !!Thormac?? and he thanked me", line++, true)
            line(player, "for finding his pet !!Kharid Scorpions??.", line++, true)
            line++
            line(player, "<col=FF0000>QUEST COMPLETE!</col>", line, false)
        }
    }

    override fun defineListeners() {

        /*
         * Handles jail doors.
         */

        on(Scenery.DOOR_31838, IntType.SCENERY, "open") { player, node ->
            if (!inInventory(player, Items.JAIL_KEY_1591, 1)) {
                sendMessage(player, "The doors won't open.")
                return@on true
            }
            sendMessage(player, "You unlock the door.")
            DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            if (player.location.y < 9690 || player.location.y > 9694)
                sendMessage(player, "The door locks shut behind you.")

            return@on true
        }

        /*
         * Handles interaction with old wall.
         */

        on(Scenery.OLD_WALL_2117, IntType.SCENERY, "search") { player, node ->
            if (getAttribute(player, ATTRIBUTE_SECRET, false)) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                if (player.location.y > 9798)
                    sendMessage(player, "You've found a secret door.")
                return@on true
            }
            return@on false
        }

        /*
         * Scorpion data.
         */

        val baseCageId = Items.SCORPION_CAGE_456

        fun scorpionBit(scorpionId: Int): Int = when (scorpionId) {
            NPCs.KHARID_SCORPION_385 -> 1 shl 0 // Taverley
            NPCs.KHARID_SCORPION_386 -> 1 shl 1 // Outpost
            NPCs.KHARID_SCORPION_387 -> 1 shl 2 // Monastery
            else -> 0
        }

        fun maskFromCage(itemId: Int): Int = itemId - baseCageId

        fun cageFromMask(mask: Int): Int = baseCageId + mask

        fun scorpionAttribute(scorpionId: Int): String? = when (scorpionId) {
            NPCs.KHARID_SCORPION_385 -> GameAttributes.LABEL_SCORPION_TAVERLEY
            NPCs.KHARID_SCORPION_386 -> GameAttributes.LABEL_SCORPION_OUTPOST
            NPCs.KHARID_SCORPION_387 -> GameAttributes.LABEL_SCORPION_MONASTERY
            else -> null
        }

        fun catchScorpion(player: Player, item: Node, scorpion: Node): Boolean {
            val scorpionId = scorpion.id
            val bit = scorpionBit(scorpionId)
            if (bit == 0) return false

            val currentMask = maskFromCage(item.id)
            val alreadyCaught = (currentMask and bit) != 0

            val newMask = currentMask or bit
            val newItemId = cageFromMask(newMask)

            val attribute = scorpionAttribute(scorpionId) ?: return false

            if (!alreadyCaught) {
                if (removeItem(player, Item(item.id, 1)) && addItem(player, newItemId)) {
                    setAttribute(player, attribute, true)
                    sendMessage(player, "You catch a scorpion!")
                    runTask(player, 1) {
                        val npc = scorpion.asNpc()
                        npc.respawnTick = GameWorld.ticks + npc.definition.getConfiguration(NPCConfigParser.RESPAWN_DELAY, 100)
                    }
                    return true
                }
            } else {
                sendMessage(player, "You already have this scorpion in your cage.")
            }

            return true
        }


        val scorpionIds = listOf(NPCs.KHARID_SCORPION_385, NPCs.KHARID_SCORPION_386, NPCs.KHARID_SCORPION_387)
        val cageIds = (Items.SCORPION_CAGE_456..Items.SCORPION_CAGE_463)

        for (cageId in cageIds) {
            for (scorpionId in scorpionIds) {

                /*
                 * Handles scorpion catching.
                 */

                onUseWith(IntType.NPC, cageId, scorpionId) { player, usedCage, usedScorp ->
                    return@onUseWith catchScorpion(player, usedCage, usedScorp)
                }
            }
        }
    }

    override fun finish(player: Player) {
        super.finish(player)
        var ln = 10
        sendItemZoomOnInterface(player, Components.QUEST_COMPLETE_SCROLL_277, 5, Items.SCORPION_CAGE_460)
        drawReward(player, "1 Quest Point", ln++)
        drawReward(player, "6625 Strength XP", ln)
        rewardXP(player, Skills.STRENGTH, 6625.00)
        removeAttributes(player, ATTRIBUTE_SECRET, ATTRIBUTE_NPC, ATTRIBUTE_MIRROR, ATTRIBUTE_CAGE)
    }

    override fun newInstance(`object`: Any?): Quest = this
}
