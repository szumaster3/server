package content.region.misthalin.draynor.wizard_tower.plugin

import content.global.travel.EssenceTeleport
import core.api.*
import core.api.hasRequirement
import core.game.dialogue.SequenceDialogue.dialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.impl.Projectile
import core.game.node.entity.npc.NPC
import core.game.world.map.Location
import core.game.world.update.flag.context.Graphics
import core.tools.RandomFunction
import org.rs.consts.*

class WizardTowerListener : InteractionListener {
    private val WIZARD_BOOKCASE = intArrayOf(Scenery.BOOKCASE_12539, Scenery.BOOKCASE_12540)
    private val WIZARD_PORTAL = intArrayOf(Scenery.MAGIC_PORTAL_2156, Scenery.MAGIC_PORTAL_2157, Scenery.MAGIC_PORTAL_2158)
    private val booksContent = arrayOf("Living with a Wizard Husband - a Housewife's Story", "Wind Strike for Beginners", "So you think you're a Mage? Volume 28", "101 Ways to Impress your Mates with Magic", "The Life & Times of a Thingummywut by Traiborn the Wizard", "How to become the Ultimate Wizard of the Universe", "The Dark Arts of Magical Wands")
    private val bookName = booksContent[RandomFunction.random(booksContent.size)]

    override fun defineListeners() {
        on(NPCs.SEDRIDOR_300, IntType.NPC, "teleport") { player, node ->
            if (!hasRequirement(player, Quests.RUNE_MYSTERIES)) return@on true
            EssenceTeleport.teleport((node as NPC), player)
            return@on true
        }

        on(Scenery.LADDER_2147, IntType.SCENERY, "climb-down") { player, _ ->
            teleport(player, location(3104, 9576, 0))
            return@on true
        }

        on(Scenery.RAILING_37668, IntType.SCENERY, "taunt-through") { player, _ ->
            val demon = findLocalNPC(player, NPCs.LESSER_DEMON_82) ?: return@on true
            lock(player, 3)
            runTask(player, 1) {
                forceWalk(demon, player.location, "smart")
                face(player, demon, 3)
                sendMessage(player, "You taunt the demon, making it growl.")
                sendChat(demon, "Graaaagh!")
                face(demon, player, 3)
                animate(player, Animations.RASPBERRY_2110)
            }
            return@on true
        }

        on(WIZARD_PORTAL, IntType.SCENERY, "enter") { player, node ->
            when (node.id) {
                Scenery.MAGIC_PORTAL_2156 -> {
                    teleport(player, Location.create(3109, 3159, 0))
                    sendMessage(player, "You enter the magic portal...")
                    sendMessage(player, "You teleport to the Wizards' tower.")
                }
                Scenery.MAGIC_PORTAL_2157 -> {
                    teleport(player, Location.create(2907, 3333, 0))
                    sendMessage(player, "You enter the magic portal...")
                    sendMessage(player, "You teleport to the Dark Wizards' tower.")
                }
                Scenery.MAGIC_PORTAL_2158 -> {
                    teleport(player, Location.create(2703, 3406, 0))
                    sendMessage(player, "You enter the magic portal...")
                    sendMessage(player, "You teleport to Thormac the Sorcerer's house.")
                }
            }
            return@on true
        }

        on(Scenery.CABINET_33062, IntType.SCENERY, "open") { player, node ->
            replaceScenery(node.asScenery(), Scenery.CABINET_33063, 100)
            playAudio(player, Sounds.OPEN_CABINET_44)
            return@on true
        }

        on(Scenery.CABINET_33063, IntType.SCENERY, "close") { player, node ->
            when (getUsedOption(player)) {
                "close" -> replaceScenery(node.asScenery(), Scenery.CABINET_33062, -1)
                "search" -> sendMessage(player, "You search the cabinet but find nothing.")
                else -> return@on false
            }
            return@on true
        }

        on(Scenery.PORTAL_41681, IntType.SCENERY, "exit") { player, node ->
            Projectile.create(node.location, player.location, 109, 15, 10, 0, 10, 0, 2).send()
            lock(player, 3)
            queueScript(player, 1,QueueStrength.SOFT) {
                player.graphics(Graphics(110, 150))
                teleport(player, location(3102, 9563, 0))
                return@queueScript stopExecuting(player)
            }
            return@on true
        }

        on(WIZARD_BOOKCASE, IntType.SCENERY, "search") { player, _ ->
            dialogue(player) {
                message("There's a large selection of books, the majority of which look fairly", "old. Some very strange names... You pick one at random:")
                message(bookName)
                end {
                    sendPlayerDialogue(player, "Interesting...")
                }
            }
            return@on true
        }
    }
}
