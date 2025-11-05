package content.region.misthalin.draynor.wizard_tower.plugin

import content.global.travel.EssenceTeleport
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.impl.Projectile
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.link.diary.DiaryType
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import shared.consts.Animations
import shared.consts.NPCs
import shared.consts.Quests
import shared.consts.Scenery

class WizardTowerListener : InteractionListener {
    private val WIZARD_BOOKCASE = intArrayOf(Scenery.BOOKCASE_12539, Scenery.BOOKCASE_12540)
    private val WIZARD_PORTAL = intArrayOf(Scenery.MAGIC_PORTAL_2156, Scenery.MAGIC_PORTAL_2157, Scenery.MAGIC_PORTAL_2158)

    override fun defineListeners() {

        /*
         * Handles teleporting via NPC Sedridor.
         */

        on(NPCs.SEDRIDOR_300, IntType.NPC, "teleport") { player, node ->
            if (!hasRequirement(player, Quests.RUNE_MYSTERIES)) return@on true
            EssenceTeleport.teleport((node as NPC), player)
            return@on true
        }

        /*
         * Handles climbing down the ladder to wizard basement.
         */

        on(Scenery.LADDER_2147, IntType.SCENERY, "climb-down") { player, _ ->
            teleport(player, location(3104, 9576, 0))
            return@on true
        }

        /*
         * Handles taunting a Lesser Demon through the railing.
         */

        on(Scenery.RAILING_37668, IntType.SCENERY, "taunt-through") { player, _ ->
            val demon = findLocalNPC(player, NPCs.LESSER_DEMON_82)
            if (demon == null) {
                sendMessage(player, "No demon is nearby to taunt.")
                return@on true
            }

            submitIndividualPulse(player, object : Pulse(1) {
                private var counter = 0

                override fun pulse(): Boolean {
                    when (counter++) {
                        0 -> {
                            face(player, demon, 3)
                            player.animate(Animation(Animations.HUMAN_BLOW_RASPBERRY_2110))
                        }
                        1 -> {
                            stopWalk(demon)
                            face(demon, player, 3)
                        }
                        2 -> {
                            sendChat(demon, "Graaaagh!")
                            sendMessage(player, "You taunt the demon, making it growl.")
                        }
                        3 -> {
                            finishDiaryTask(player, DiaryType.LUMBRIDGE, 1, 13)
                            return true
                        }
                    }
                    return false
                }
            })

            return@on true
        }

        /*
         * Handles entering any of the wizard tower portals.
         */

        on(WIZARD_PORTAL, IntType.SCENERY, "enter") { player, node ->
            val baseId = Scenery.MAGIC_PORTAL_2156
            val portalIndex = node.id - baseId

            val locations = arrayOf(
                Location.create(3109, 3159, 0), // Wizards' tower.
                Location.create(2907, 3333, 0), // Dark Wizards' tower.
                Location.create(2703, 3406, 0)  // Thormac the Sorcerer's house.
            )

            val descriptions = arrayOf(
                "the Wizards' tower",
                "the Dark Wizards' tower",
                "Thormac the Sorcerer's house"
            )

            if (portalIndex in locations.indices) {
                teleport(player, locations[portalIndex])
                sendMessage(player, "You enter the magic portal...")
                sendMessage(player, "You teleport to ${descriptions[portalIndex]}.")
                return@on true
            }

            return@on false
        }

        /*
         * Handles exiting the portal at location 41681.
         */

        on(Scenery.PORTAL_41681, IntType.SCENERY, "exit") { player, node ->
            Projectile.create(node.location, player.location, 109, 15, 10, 0, 10, 0, 2).send()
            lock(player, 3)
            queueScript(player, 1, QueueStrength.SOFT) {
                player.graphics(Graphics(110, 150))
                teleport(player, Location(3102, 9563, 0))
                stopExecuting(player)
            }
            return@on true
        }

        /*
         * Handles searching the wizard bookcases.
         */

        on(WIZARD_BOOKCASE, IntType.SCENERY, "search") { player, _ ->
            val books = arrayOf(
                "Living with a Wizard Husband - a Housewife's Story",
                "Wind Strike for Beginners",
                "So you think you're a Mage? Volume 28",
                "101 Ways to Impress your Mates with Magic",
                "The Life & Times of a Thingummywut by Traiborn the Wizard",
                "How to become the Ultimate Wizard of the Universe",
                "The Dark Arts of Magical Wands"
            )
            val bookName = books.random()

            sendDialogueLines(player,
                "There's a large selection of books, the majority of which look fairly",
                "old. Some very strange names... You pick one at random:"
            )

            addDialogueAction(player) { _, button ->
                if(button > 0) {
                    sendDialogue(player, bookName)
                    runTask(player, 3) {
                        sendPlayerDialogue(player, "Interesting...")
                    }
                }
            }
            return@on true
        }
    }
}
