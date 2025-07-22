package content.region.morytania.swamp.quest.druidspirit.plugin

import content.region.morytania.swamp.npc.GhastNPC
import core.api.*
import core.game.dialogue.FaceAnim
import core.game.dialogue.SequenceDialogue.dialogue
import core.game.global.action.PickupHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.GroundItem
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.shops.Shops
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.tools.Log
import org.rs.consts.*

class NatureSpiritPlugin : InteractionListener {

    companion object {
        val MIRROR_TAKEN = "/save:ns:mirror_taken"
        val GROTTO_SEARCHED = "/save:ns:grotto_searched"

        private val JOURNAL = Items.JOURNAL_2967
        private val WASHING_BOWL = Items.WASHING_BOWL_2964
        private val MIRROR = Items.MIRROR_2966
        private val SPELLCARD = Items.DRUIDIC_SPELL_2968
        private val USED_SPELLCARD = Items.A_USED_SPELL_2969
        private val FUNGUS = Items.MORT_MYRE_FUNGUS_2970
        private val STEM = Items.MORT_MYRE_STEM_2972
        private val PEAR = Items.MORT_MYRE_PEAR_2974
        private val DRUID_POUCH = Items.DRUID_POUCH_2958
        private val DRUID_POUCH_EMPTY = Items.DRUID_POUCH_2957
        private val DRUID_POUCHES = intArrayOf(DRUID_POUCH, DRUID_POUCH_EMPTY)

        private val NATURE_STONE = Scenery.STONE_3527
        private val FAITH_STONE = Scenery.STONE_3528
        private val FREELY_GIVEN_STONE = Scenery.STONE_3529
        private val GROTTO_TREE = Scenery.GROTTO_TREE_3517
        private val GROTTO_ENTRANCE = Scenery.GROTTO_3516
        private val GROTTO_EXIT = intArrayOf(Scenery.GROTTO_3525, Scenery.GROTTO_3526)
        private val GROTTO_ALTAR = Scenery.GROTTO_3520
        private val NATURE_ALTAR = Scenery.ALTAR_OF_NATURE_3521
        private val WISHING_WELL = Scenery.WISHING_WELL_28715

        val stones = intArrayOf(NATURE_STONE, FAITH_STONE, FREELY_GIVEN_STONE)
        val items = intArrayOf(USED_SPELLCARD, FUNGUS)
    }

    override fun defineListeners() {
        on(GROTTO_TREE, IntType.SCENERY, "look-at") { player, _ ->
            sendMessage(player, "It looks like a tree on a large rock with roots trailing down to the ground.")
            return@on true
        }

        on(GROTTO_TREE, IntType.SCENERY, "search") { player, _ ->
            if (!getAttribute(player, GROTTO_SEARCHED, false) ||
                !(inInventory(player, JOURNAL) || inBank(player, JOURNAL))
            ) {
                player.dialogueInterpreter.sendItemMessage(
                    JOURNAL,
                    "You search the strange rock. You find a knot and", "inside of it you discover a small tome. The words on", "the front are a bit vague, but you can make out the words", "'Tarlock' and 'journal'.",
                )
                addItemOrDrop(player, JOURNAL, 1)
                setAttribute(player, GROTTO_SEARCHED, true)
                return@on true
            }
            return@on false
        }

        on(GROTTO_ENTRANCE, IntType.SCENERY, "enter") { player, _ ->
            val questStage = getQuestStage(player, Quests.NATURE_SPIRIT)
            sendMessage(player, "You prepare to enter the Druid's grotto.")

            when {
                questStage < 55 -> {
                    val location = Location.create(3440, 3336, 0)
                    val existing = findNPC(location, NPCs.FILLIMAN_TARLOCK_1050)
                    if (existing == null) {
                        val npc = core.game.node.entity.npc.NPC.create(NPCs.FILLIMAN_TARLOCK_1050, location)
                        npc.init()
                    }
                    sendMessage(player, "The aura of Filliman's camp protects you from the swamp.")
                }
                questStage in 55..59 -> {
                    player.teleport(Location.create(3442, 9734, 0))
                    sendMessage(player, "You see a beautifully tended small grotto area.")
                }
                else -> {
                    player.teleport(Location.create(3442, 9734, 1))
                    sendMessage(player, "You see a beautifully tended small grotto area.")
                }
            }

            return@on true
        }

        on(GROTTO_EXIT, IntType.SCENERY, "exit") { player, _ ->
            sendMessage(player, "You prepare to exit the grotto.")
            player.teleport(Location.create(3439, 3337, 0), 1)
            sendMessageWithDelay(player, "You crawl back out of the grotto.", 1)
            return@on true
        }

        on(GROTTO_ALTAR, IntType.SCENERY, "search") { player, _ ->
            val stage = getQuestStage(player, Quests.NATURE_SPIRIT)
            if (stage == 55) {
                dialogue(player) {
                    npc(NPCs.FILLIMAN_TARLOCK_1050,FaceAnim.NEUTRAL, "Well, hello there again. I was just enjoying the grotto. Many thanks for your help, I couldn't have become a Spirit of nature without you.")
                    npc(NPCs.FILLIMAN_TARLOCK_1050,FaceAnim.NEUTRAL, "I must complete the transformation now. Just stand there and watch the show, apparently it's quite good!")
                    end {
                        lock(player, 10)
                        submitWorldPulse(CompleteSpellPulse(player))
                    }
                }
            }
            return@on false
        }

        on(WISHING_WELL, IntType.SCENERY, "make-wish") { player, _ ->
            if (isQuestComplete(player, Quests.NATURE_SPIRIT) && isQuestComplete(player, Quests.WOLF_WHISTLE)) {
                sendMessage(player, "The wishing well has a number of Summoning items in it.")
                sendMessage(player, "If you toss in some coins, you can take the items in return.")
                Shops.openId(player, 239)
            } else {
                sendMessage(
                    player,
                    "The wishing well is connected to the grotto, so it will not function until the grotto has been completed.",
                )
            }
            return@on true
        }

        on(JOURNAL, IntType.ITEM, "read") { player, _ ->
            sendDialogue(
                player,
                "Most of the writing is pretty uninteresting, but something inside refers to a nature spirit. The requirements for which are,",
            )
            addDialogueAction(player) { _, _ ->
                sendDialogue(
                    player,
                    "'Something from nature', 'something with faith' and 'something of the spirit-to-become freely given'. It's all pretty vague.",
                )

            }
            return@on true
        }

        on(WASHING_BOWL, IntType.GROUND_ITEM, "take") { player, node ->
            log(this::class.java, Log.FINE, "Running listener")
            GroundItemManager.create(Item(MIRROR), node.location, player)
            PickupHandler.take(player, node as GroundItem)
            return@on true
        }

        on(MIRROR, IntType.GROUND_ITEM, "take") { player, node ->
            if (getAttribute(player, MIRROR_TAKEN, false) && (inInventory(player, MIRROR) || inBank(player, MIRROR))) {
                sendDialogue(player, "I don't need another one of these.")
                return@on true
            }
            setAttribute(player, MIRROR_TAKEN, true)
            PickupHandler.take(player, node as GroundItem)
            return@on true
        }

        on(SPELLCARD, IntType.ITEM, "cast") { player, node ->
            if (!inBorders(player, getRegionBorders(13620)) || !inBorders(player, getRegionBorders(13621))) {
                sendMessage(player, "This spell has no effect outside of Mort Myre swamp.")
                return@on false
            }
            if (NSUtils.castBloom(player)) {
                sendMessage(player, "You cast this spell in the swamp.")
                removeItem(player, node.asItem(), Container.INVENTORY)
                addItem(player, Items.A_USED_SPELL_2969)
            }
            return@on true
        }

        on(DRUID_POUCHES, IntType.ITEM, "fill") { player, node ->

            if (getQuestStage(player, Quests.NATURE_SPIRIT) < 75) {
                sendDialogue(player, "I don't know how to use that yet.")
                return@on true
            }

            val pear   = amountInInventory(player, PEAR)
            val stem   = amountInInventory(player, STEM)
            val fungus = amountInInventory(player, FUNGUS)

            val (itemId, pouchAmount) = when {
                pear   >= 3 -> PEAR   to 9
                stem   >= 3 -> STEM   to 6
                fungus >= 3 -> FUNGUS to 3
                else -> {
                    sendMessage(player, "You need 3 fungus before you can do that.")
                    return@on true
                }
            }

            if (node.id != Items.DRUID_POUCH_2958) {
                removeItem(player, node, Container.INVENTORY)
            }
            removeItem(player, Item(itemId, 3), Container.INVENTORY)
            addItem(player, Items.DRUID_POUCH_2958, pouchAmount)
            sendMessage(player, "You add 3 natures harvests to your druid pouch.")
            return@on true
        }

        onUseWith(IntType.SCENERY, Items.SILVER_SICKLE_2961, NATURE_ALTAR) { player, _, _ ->
            if (removeItem(player, Items.SILVER_SICKLE_2961, Container.INVENTORY)) {
                sendItemDialogue(player, Items.SILVER_SICKLEB_2963, "You dump the sickle into the waters.")
                addItem(player, Items.SILVER_SICKLEB_2963, 1)
            }
            return@onUseWith true
        }

        onUseWith(IntType.NPC, DRUID_POUCH, NPCs.GHAST_1052) { player, _, with ->
            NSUtils.activatePouch(player, with as GhastNPC)
        }

        on(NATURE_STONE, IntType.SCENERY, "search") { player, _ ->
            sendDialogueLines(player, "You search the stone and find that it has some sort of nature symbol", "scratched into it.")
            return@on true
        }

        on(FAITH_STONE, IntType.SCENERY, "search") { player, _ ->
            sendDialogueLines(player, "You search the stone and find that it has some sort of faith symbol", "scratched into it.")
            return@on true
        }

        on(FREELY_GIVEN_STONE, IntType.SCENERY, "search") { player, _ ->
            sendDialogueLines(player, "You search the stone and find it has some sort of spirit symbol", "scratched into it.")
            return@on true
        }

        onUseWith(IntType.SCENERY, items, *stones) { player, used, with ->

            fun absorbItem(message: String, onSuccess: () -> Unit) {
                if (!removeItem(player, used, Container.INVENTORY)) {
                    sendMessage(player, "You try to put the item on the stone, but it just moves off.")
                    return
                }
                val anim = Animations.HUMAN_BURYING_BONES_827
                animate(player, anim)
                runTask(player, animationDuration(Animation(anim))) {
                    produceGroundItem(player, used.id, 1, with.location)
                    sendNPCDialogue(player, NPCs.FILLIMAN_TARLOCK_1050, "Aha, yes, that seems right well done!")
                    sendMessage(player, message)
                    onSuccess()
                }
            }

            if (getQuestStage(player, Quests.NATURE_SPIRIT) == 0) {
                sendMessage(player, "You try to put the item on the stone, but it just moves off.")
                return@onUseWith true
            }

            when {
                used.id == USED_SPELLCARD && with.id == FREELY_GIVEN_STONE -> {
                    absorbItem("The stone seems to absorb the used spell scroll.") {
                        NSUtils.flagCardPlaced(player)
                    }
                }

                used.id == FUNGUS && with.id == NATURE_STONE -> {
                    absorbItem("The stone seems to absorb the fungus.") {
                        NSUtils.flagFungusPlaced(player)
                    }
                }

                else -> {
                    sendMessage(player, "You try to put the item on the stone, but it just moves off.")
                }
            }

            return@onUseWith true
        }

        onUseWith(IntType.NPC, Items.SECATEURS_5329, NPCs.NATURE_SPIRIT_1051) { player, used, _ ->
            if (!hasRequirement(player, Quests.FAIRYTALE_I_GROWING_PAINS)) {
                return@onUseWith true
            }
            if (amountInInventory(player, Items.COINS_995) < 40000) {
                sendDialogue(player, "You need 40,000 coins to do this.")
                return@onUseWith true
            }
            if (removeItem(player, used) && removeItem(player, Item(Items.COINS_995, 40000))) {
                sendItemDialogue(
                    player,
                    Items.MAGIC_SECATEURS_7409,
                    "Your secateurs are enchanted into magic secateurs",
                )
                addItem(player, Items.MAGIC_SECATEURS_7409)
            }
            return@onUseWith true
        }
    }
}
