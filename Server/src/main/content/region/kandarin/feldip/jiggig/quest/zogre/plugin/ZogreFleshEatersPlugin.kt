package content.region.kandarin.feldip.jiggig.quest.zogre.plugin

import content.region.kandarin.feldip.jiggig.quest.zogre.dialogue.*
import content.region.kandarin.feldip.jiggig.quest.zogre.npc.ZavisticRarveNPC.Companion.spawnWizard
import core.api.*
import core.api.openNpcShop
import core.api.removeGroundItem
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.GroundItem
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.map.Direction
import core.game.world.map.Location
import core.tools.BLUE
import shared.consts.*

class ZogreFleshEatersPlugin : InteractionListener {
    companion object {
        const val SITHIK = Scenery.SITHIK_INTS_6888
        const val SITHIK_OGRE = Scenery.SITHIK_INTS_6889
        const val CUPBOARD = Scenery.CUPBOARD_6876
        const val WARDROBE = Scenery.WARDROBE_6877
        const val DRAWERS = Scenery.DRAWERS_6875
    }

    override fun defineListeners() {
        on(intArrayOf(Scenery.CRUSHED_BARRICADE_6881, Scenery.CRUSHED_BARRICADE_6882), IntType.SCENERY, "climb-over") { player, _ ->
            if(getVarbit(player, Vars.VARBIT_QUEST_ZOGRE_GATE_PASSAGE_496) != 1) return@on false
            lock(player, 3)
            submitIndividualPulse(
                player,
                object : Pulse() {
                    var counter = 0

                    override fun pulse(): Boolean {
                        when (counter++) {
                            0 ->
                                forceMove(
                                    player,
                                    player.location,
                                    player.location.transform(
                                        if (player.location.x < 2457) Direction.EAST else Direction.WEST,
                                        2,
                                    ),
                                    20,
                                    60,
                                    null,
                                    10980,
                                )

                            1 -> {
                                resetAnimator(player)
                                return true
                            }
                        }
                        return false
                    }
                },
            )
            return@on true
        }

        onUseWith(IntType.NPC, ZogreUtils.QUEST_ITEMS, NPCs.GRISH_2038) { player, _, with ->
            openDialogue(player, NPCs.GRISH_2038, with.id)
            return@onUseWith true
        }

        onUseWith(IntType.NPC, ZogreUtils.QUEST_ITEMS, NPCs.BARTENDER_739) { player, _, with ->
            openDialogue(player, NPCs.BARTENDER_739, with.id)
            return@onUseWith true
        }

        onUseWith(IntType.NPC, ZogreUtils.QUEST_ITEMS, NPCs.ZAVISTIC_RARVE_2059) { player, _, with ->
            openDialogue(player, NPCs.ZAVISTIC_RARVE_2059, with.id)
            return@onUseWith true
        }

        on(SITHIK, IntType.SCENERY, "talk-to") { player, _ ->
            openDialogue(player, NPCs.SITHIK_INTS_2061, NPCs.SITHIK_INTS_2061)
            return@on true
        }

        onUseWith(IntType.SCENERY, ZogreUtils.QUEST_ITEMS, SITHIK) { player, _, _ ->
            openDialogue(player, NPCs.SITHIK_INTS_2061, NPCs.SITHIK_INTS_2061)
            return@onUseWith true
        }

        on(SITHIK_OGRE, IntType.SCENERY, "talk-to") { player, _ ->
            openDialogue(player, NPCs.SITHIK_INTS_2062, NPCs.SITHIK_INTS_2062)
            return@on true
        }

        on(Scenery.BELL_6847, IntType.SCENERY, "ring") { player, _ ->
            if (getAttribute(player, ZogreUtils.NPC_ACTIVE, false)) {
                sendMessage(player, "You can't do that right now.")
                return@on true
            }

            if (getVarbit(player, Vars.VARBIT_QUEST_ZORGE_FLESH_EATERS_PROGRESS_487) in 4..12) {
                playGlobalAudio(player.location, Sounds.ZOGRE_BELL_1959)
                spawnWizard(player)
            }
            return@on true
        }

        on(Items.SIGNED_PORTRAIT_4816, IntType.ITEM, "look-at") { player, _ ->
            sendItemDialogue(player, Items.SIGNED_PORTRAIT_4816, "You see an image of Sithik with a message underneath$BLUE'I, the bartender of the Dragon Inn, do swear that this is the true likeness of the wizzy who was talking to Brentle Vahn, my customer the other day.'")
            return@on true
        }

        on(NPCs.UGLUG_NAR_2039, IntType.NPC, "trade") { player, _ ->
            if (getVarbit(player, Vars.VARBIT_QUEST_ZORGE_FLESH_EATERS_PROGRESS_487) < 7) {
                sendNPCDialogue(player, NPCs.UGLUG_NAR_2039, "Me's not got no glug-glugs to sell, yous bring me da sickies glug-glug den me's open da stufsies for ya.", FaceAnim.OLD_DEFAULT)
            } else {
                openNpcShop(player, NPCs.UGLUG_NAR_2039)
            }
            return@on true
        }

        onUseWith(IntType.NPC, Items.RELICYMS_BALM3_4844, NPCs.UGLUG_NAR_2039) { player, _, _ ->
            if (getVarbit(player, Vars.VARBIT_QUEST_ZORGE_FLESH_EATERS_PROGRESS_487) < 7) {
                sendNPCDialogue(player, NPCs.UGLUG_NAR_2039, "Me's not got no glug-glugs to sell, yous bring me da sickies glug-glug den me's open da stufsies for ya.", FaceAnim.OLD_DEFAULT)
            } else {
                openDialogue(player, UglugNarDialogue())
            }
            return@onUseWith true
        }

        listOf(
            CUPBOARD to ZogreUtils.NECROMANCY_BOOK,
            WARDROBE to ZogreUtils.HAM_BOOK
        ).forEach { (furniture, item) ->

            on(furniture, IntType.SCENERY, "search") { player, _ ->
                if (!getAttribute(player, ZogreUtils.ASK_SITHIK_AGAIN, false))
                    return@on sendMessage(player, "You search but find nothing.").let { true }

                val hasItem = inInventory(player, item)
                val name = if (item == ZogreUtils.HAM_BOOK) "a book on Philosophy" else "a book on Necromancy"

                when {
                    !hasItem && freeSlots(player) < 1 ->
                        sendDialogue(player, "You see $name but don't have enough inventory space.")

                    !hasItem -> {
                        sendItemDialogue(player, item, "You find $name.")
                        addItemOrDrop(player, item, 1)
                    }

                    else -> sendMessage(player, "You search but find nothing.")
                }

                return@on true
            }
        }

        on(DRAWERS, IntType.SCENERY, "search") { player, _ ->
            if (!getAttribute(player, ZogreUtils.ASK_SITHIK_AGAIN, false)) {
                return@on sendDialogue(player, "You search but find nothing.").let { true }
            }

            val hasCharcoal = inInventory(player, Items.CHARCOAL_973)
            val hasPapyrus = inInventory(player, Items.PAPYRUS_970)
            val hasBook = inInventory(player, ZogreUtils.PORTRAI_BOOK)
            val free = freeSlots(player)

            fun need(slots: Int) = if (free < slots) {
                sendDialogue(player, "You need ${if (slots == 1) "free inventory space" else "$slots free inventory spaces."}")
                true
            } else false

            when {
                hasCharcoal && hasPapyrus && !hasBook && !need(1) -> {
                    sendItemDialogue(player, ZogreUtils.PORTRAI_BOOK, "You find a book on portraiture.")
                    addItemOrDrop(player, ZogreUtils.PORTRAI_BOOK, 1)
                }

                hasCharcoal && hasPapyrus -> sendDialogue(player, "You search but find nothing.")

                hasCharcoal && !need(1) -> {
                    sendItemDialogue(player, Items.PAPYRUS_970, "You find some papyrus.")
                    addItemOrDrop(player, Items.PAPYRUS_970, 1)
                }

                hasPapyrus && !need(1) -> {
                    sendItemDialogue(player, Items.CHARCOAL_973, "You find some charcoal.")
                    addItemOrDrop(player, Items.CHARCOAL_973, 1)
                }

                !need(3) -> {
                    sendDoubleItemDialogue(player, Items.CHARCOAL_973, Items.PAPYRUS_970, "You find some charcoal and papyrus.")
                    addItemOrDrop(player, Items.CHARCOAL_973, 1)
                    addItemOrDrop(player, Items.PAPYRUS_970, 1)
                }
            }

            return@on true
        }

        onUseWith(IntType.ITEM, Items.TORN_PAGE_4809, ZogreUtils.NECROMANCY_BOOK) { player, used, _ ->
            if (removeItem(player, used.asItem())) {
                sendDoubleItemDialogue(player, ZogreUtils.NECROMANCY_BOOK, Items.TORN_PAGE_4809, "The torn page matches the book.")
                setAttribute(player, ZogreUtils.TORN_PAGE_ON_NECRO_BOOK, true)
            }
            return@onUseWith true
        }

        on(Items.CUP_OF_TEA_4838, IntType.GROUND_ITEM, "take") { player, node ->
            val cup = node as GroundItem
            if (cup.location == Location(2593, 3103, 1)) {
                sendNPCDialogue(player, NPCs.SITHIK_INTS_2061, "Hey! What do you think you're doing? Leave my tea alone!", FaceAnim.ANNOYED)
            } else if (freeSlots(player) < 1) {
                sendMessage(player, "You don't have enough inventory space.")
            } else {
                removeGroundItem(cup)
                addItem(player, Items.CUP_OF_TEA_4838)
            }
            return@on true
        }

        onUseWith(IntType.GROUND_ITEM, ZogreUtils.STRANGE_POTION, Items.CUP_OF_TEA_4838) { player, used, _ ->
            lock(player, 2)
            animate(player, 537)
            replaceSlot(player, used.asItem().index, Item(Items.SAMPLE_BOTTLE_3377))
            sendItemDialogue(player, ZogreUtils.STRANGE_POTION, "You pour some of the potion into the cup.")
            setAttribute(player, ZogreUtils.SITHIK_TURN_INTO_OGRE, true)
            return@onUseWith true
        }
    }

    override fun defineDestinationOverrides() {
        setDest(IntType.SCENERY, intArrayOf(Scenery.BELL_6847), "ring") { _, _ ->
            return@setDest Location(2598, 3086, 0)
        }
    }
}
