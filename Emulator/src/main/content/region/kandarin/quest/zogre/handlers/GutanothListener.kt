package content.region.kandarin.quest.zogre.handlers

import content.global.skill.thieving.PickpocketListener
import content.region.kandarin.quest.zogre.handlers.BrentleVahnNPC.Companion.spawnHumanZombie
import content.region.kandarin.quest.zogre.handlers.SlashBashNPC.Companion.spawnZogreBoss
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.tools.BLUE
import core.tools.RandomFunction
import core.tools.minutesToTicks
import org.rs.consts.*

class GutanothListener : InteractionListener {
    companion object {
        val OGRE_DOORS = intArrayOf(Scenery.OGRE_STONE_DOOR_6871, Scenery.OGRE_STONE_DOOR_6872)
        val COFFIN = intArrayOf(Scenery.OGRE_COFFIN_6848, Scenery.OGRE_COFFIN_6850)
        const val STAIRS_TO_JIGGIG = Scenery.STAIRS_6842
        const val STAIRS_TO_CRYPT = Scenery.STAIRS_6841
        const val BROKEN_LECTURN = Scenery.BROKEN_LECTURN_6846
        const val SPAWNED_ZOMBIE = NPCs.ZOMBIE_1826
        const val SKELETON = Scenery.SKELETON_6893
        const val QUEST_COFFIN = Scenery.OGRE_COFFIN_6844
        const val QUEST_COFFIN_2 = Scenery.OGRE_COFFIN_6845
        const val BLACK_PRISM = Items.BLACK_PRISM_4808
        const val TORN_PAGE = Items.TORN_PAGE_4809
        const val DRAGON_TANKARD = Items.DRAGON_INN_TANKARD_4811
        const val BACKPACK = Items.RUINED_BACKPACK_4810
        const val STAND = Scenery.STAND_6897
    }

    override fun defineListeners() {
        on(STAIRS_TO_CRYPT, IntType.SCENERY, "climb-down") { player, node ->
            submitWorldPulse(
                object : Pulse() {
                    var counter = 0

                    override fun pulse(): Boolean {
                        when (counter++) {
                            0 -> openInterface(player, Components.FADE_TO_BLACK_115)
                            3 -> {
                                openInterface(player, Components.FADE_FROM_BLACK_170)
                                sendMessage(player, "You climb down the steps.")
                                playAudio(player, Sounds.DOWN_STONE_STAIRS_1952, 1)
                                teleport(
                                    player,
                                    if (node.location == Location(2443, 9417, 2)) {
                                        Location(2442, 9418, 0)
                                    } else {
                                        Location(
                                            2477,
                                            9437,
                                            2,
                                        )
                                    },
                                )
                            }
                        }
                        return false
                    }
                },
            )
            return@on true
        }

        on(STAIRS_TO_JIGGIG, IntType.SCENERY, "climb-up") { player, node ->
            teleport(
                player,
                if (node.location == Location(2443, 9417, 0)) Location(2446, 9417, 2) else Location(2485, 3045, 0),
            )
            sendMessage(player, "You climb up the steps.")
            return@on true
        }

        on(BROKEN_LECTURN, IntType.SCENERY, "search") { player, _ ->
            if (player.inCombat()) {
                sendMessage(player, "You're in mortal danger, you don't have time to search!")
                return@on true
            }

            animate(player, Animation(Animations.HUMAN_BURYING_BONES_827))
            sendMessage(player, "You search the broken down lecturn.")
            if (inInventory(player, TORN_PAGE) || getAttribute(player, ZUtils.TORN_PAGE_ACQUIRED, false)) {
                sendMessage(player, "You find nothing here this time.")
            } else {
                setAttribute(player, ZUtils.TORN_PAGE_ACQUIRED, true)
                sendDoubleItemDialogue(
                    player,
                    -1,
                    TORN_PAGE,
                    "You find a half torn page...it has spidery writing all over it.",
                )
                addItemOrDrop(player, TORN_PAGE)
            }
            return@on true
        }

        on(SKELETON, IntType.SCENERY, "search") { player, _ ->
            if (getAttribute(player, ZUtils.ZOMBIE_NPC_ACTIVE, false)) {
                sendMessage(player, "You find nothing on the corpse.")
            } else if (player.inCombat()) {
                sendMessage(player, "You're in mortal danger, you don't have time to search!")
            } else {
                sendMessage(player, "Something screams into life right in front of you.")
                spawnHumanZombie(player)
            }
            return@on true
        }

        on(SPAWNED_ZOMBIE, IntType.NPC, "talk-to") { _, node ->
            val npc = node.asNpc()
            sendChat(npc, "Raaarrrggghhh")
            return@on true
        }

        on(COFFIN, IntType.SCENERY, "pick-lock") { player, node ->
            val sceneryId = node.asScenery()
            val loot = OgreCoffin.forId(sceneryId.id)?.roll()?.firstOrNull() ?: return@on false

            if (getVarbit(player, Vars.VARBIT_QUEST_ZORGE_FLESH_EATERS_PROGRESS_487) < 13) {
                sendNPCDialogue(
                    player,
                    NPCs.OGRE_GUARD_2042,
                    "Yous best not touch dem till yous chat wiv Grish - use look at dem again more when yous do stuff for Grish.",
                    FaceAnim.OLD_DEFAULT,
                )
                return@on false
            }

            if (player.inCombat()) {
                sendMessage(player, "You're in mortal danger, thieving is the last thing on your mind.")
                return@on false
            }

            if (freeSlots(player) == 0) {
                sendMessage(player, "You have no space in your inventory for any items you might find.")
                return@on false
            }

            if (getStatLevel(player, Skills.THIEVING) < 20) {
                sendDialogue(player, "You need an Thieving level of at least 20 to do this.")
                return@on false
            }

            lock(player, 3)
            animate(player, Animations.HUMAN_MULTI_USE_832)
            sendMessage(player, "You attempt to pick the lock on the coffin.")
            submitIndividualPulse(
                player,
                object : Pulse(2) {
                    var table = PickpocketListener.pickpocketRoll(player, 84.0, 240.0, OgreCoffin.OGRE_COFFIN.table)

                    override fun pulse(): Boolean {
                        if (table != null) {
                            sendMessage(player, "You unlock the coffin...")
                            if (inInventory(player, Items.LOCKPICK_1523)) sendMessage(player, "Your lockpick snaps.")
                            replaceScenery(
                                sceneryId,
                                if (sceneryId.id ==
                                    Scenery.OGRE_COFFIN_6848
                                ) {
                                    Scenery.OGRE_COFFIN_6890
                                } else {
                                    Scenery.OGRE_COFFIN_6851
                                },
                                2,
                            )
                            addItem(player, loot.id, loot.amount)
                            rewardXP(player, Skills.THIEVING, 1.0)
                            if (loot.id in
                                intArrayOf(
                                    Items.FAYRG_BONES_4830,
                                    Items.RAURG_BONES_4832,
                                    Items.OURG_BONES_4834,
                                    Items.ZOGRE_BONES_4812,
                                )
                            ) {
                                sendDoubleItemDialogue(
                                    player,
                                    -1,
                                    loot.id,
                                    "You find some ancestral ${getItemName(loot.id)}.",
                                )
                                sendMessage(player, "You find some ancestral ${getItemName(loot.id)}.")
                            } else {
                                sendItemDialogue(
                                    player,
                                    if (loot.id == Items.COINS_995) Items.COINS_8897 else loot.id,
                                    "You find something...",
                                )
                            }
                        } else {
                            sendMessage(
                                player,
                                "You fail to pick the lock - your fingers get numb from fumbling with the lock.",
                            )
                            val disease = RandomFunction.random(100) <= 4
                            if (disease) {
                                sendMessage(player, "Your clumsiness releases a disease ridden spore cloud.")
                                registerTimer(player, spawnTimer("disease", minutesToTicks(15)))
                            }
                            val rollDamage = (1..4).random()
                            val fingernumb = RandomFunction.roll(1)
                            if (fingernumb) {
                                impact(player, rollDamage, ImpactHandler.HitsplatType.NORMAL)
                            }
                            rewardXP(player, Skills.THIEVING, 1.0)
                        }
                        return true
                    }
                },
            )
            return@on true
        }

        on(COFFIN, IntType.SCENERY, "open") { player, node ->
            val sceneryId = node.asScenery()
            val loot = OgreCoffin.forId(sceneryId.id)?.roll()?.firstOrNull() ?: return@on false

            if (getVarbit(player, Vars.VARBIT_QUEST_ZORGE_FLESH_EATERS_PROGRESS_487) < 13) {
                sendNPCDialogue(
                    player,
                    NPCs.OGRE_GUARD_2042,
                    "Yous best not touch dem till yous chat wiv Grish - use look at dem again more when yous do stuff for Grish.",
                    FaceAnim.OLD_DEFAULT,
                )
                return@on false
            }

            if (player.inCombat()) {
                sendMessage(player, "You're in mortal danger, thieving is the last thing on your mind.")
                return@on false
            }
            if (freeSlots(player) == 0) {
                sendMessage(player, "You have no space in your inventory for any items you might find.")
                return@on false
            }

            if (!removeItem(player, Items.OGRE_COFFIN_KEY_4850)) {
                sendMessage(player, "You need an ogre key to open the coffin.")
            } else {
                lock(player, 3)
                animate(player, Animations.HUMAN_MULTI_USE_832)
                sendMessage(player, "You unlock the coffin with your zogre coffin key...")
                submitIndividualPulse(
                    player,
                    object : Pulse(2) {
                        override fun pulse(): Boolean {
                            sendMessage(player, "The key crumbles in the lock!")
                            replaceScenery(
                                sceneryId,
                                if (sceneryId.id ==
                                    Scenery.OGRE_COFFIN_6848
                                ) {
                                    Scenery.OGRE_COFFIN_6890
                                } else {
                                    Scenery.OGRE_COFFIN_6851
                                },
                                2,
                            )
                            addItem(player, loot.id, loot.amount)
                            if (loot.id in
                                intArrayOf(
                                    Items.FAYRG_BONES_4830,
                                    Items.RAURG_BONES_4832,
                                    Items.OURG_BONES_4834,
                                    Items.ZOGRE_BONES_4812,
                                )
                            ) {
                                sendDoubleItemDialogue(
                                    player,
                                    -1,
                                    loot.id,
                                    "You find some ancestral ${getItemName(loot.id)}.",
                                )
                                sendMessage(player, "You find some ancestral ${getItemName(loot.id)}.")
                            } else {
                                sendItemDialogue(
                                    player,
                                    if (loot.id == Items.COINS_995) Items.COINS_8897 else loot.id,
                                    "You find something...",
                                )
                            }
                            return true
                        }
                    },
                )
            }
            return@on true
        }

        on(QUEST_COFFIN, IntType.SCENERY, "search") { player, _ ->
            openDialogue(
                player,
                object : DialogueFile() {
                    override fun handle(
                        componentID: Int,
                        buttonID: Int,
                    ) {
                        when (stage) {
                            0 ->
                                sendDialogueLines(
                                    player,
                                    "You search the coffin and find a small geometrically shaped hole in",
                                    "the side. It looks as if this hole was made with a considerable amount",
                                    "of force, maybe the thing which made the hole is still inside?",
                                ).also { stage++ }

                            1 ->
                                sendDialogueLines(
                                    player,
                                    "The lock looks quite crude, with some skill and a slender blade, you",
                                    "may be able to force it.",
                                ).also { stage++ }

                            2 -> {
                                end()
                                setAttribute(player, "/save:${ZUtils.ZOMBIE_NPC_ACTIVE}", true)
                            }
                        }
                    }
                },
            )
            return@on true
        }

        onUseWith(IntType.SCENERY, Items.KNIFE_946, QUEST_COFFIN) { player, _, _ ->
            openDialogue(
                player,
                object : DialogueFile() {
                    override fun handle(
                        componentID: Int,
                        buttonID: Int,
                    ) {
                        when (stage) {
                            0 ->
                                sendItemDialogue(
                                    player,
                                    Items.KNIFE_5605,
                                    "With some skill you manage to slide the blade along the lock edge and click into place the teeth of the primitive mechanism.",
                                ).also { stage++ }

                            1 ->
                                sendDialogue(
                                    player,
                                    "The lid looks heavy, but now that you've unlocked it, you may be able to lift it. You prepare yourself.",
                                ).also { stage++ }

                            2 -> {
                                player("Urrrgggg.")
                                sendChat(player, "Urrrgggg.")
                                stage++
                            }

                            3 -> {
                                player("Aarrrgghhh!")
                                sendChat(player, "Aarrrgghhh!")
                                stage++
                            }

                            4 -> {
                                player("Raarrrggggg! Yes!")
                                sendChat(player, "Raarrrggggg! Yes!")
                                stage++
                            }

                            5 -> {
                                end()
                                sendDialogue(player, "You eventually manage to lift the lid.")
                                setAttribute(player, "/save:${ZUtils.BLACK_PRISM_ACQUIRED}", 0)
                                setVarbit(player, Vars.VARBIT_QUEST_ZOGRE_COFFIN_TRANSFORM_488, 3, true)
                            }
                        }
                    }
                },
            )
            return@onUseWith true
        }

        on(QUEST_COFFIN_2, IntType.SCENERY, "search") { player, _ ->
            if (inInventory(player, BLACK_PRISM) || getAttribute(player, ZUtils.BLACK_PRISM_ACQUIRED, 0) > 3) {
                sendMessage(player, "You find nothing inside this time.")
            } else {
                sendItemDialogue(player, BLACK_PRISM, "You find a creepy looking black prism inside.")
                player.incrementAttribute(ZUtils.BLACK_PRISM_ACQUIRED)
                setVarbit(player, Vars.VARBIT_QUEST_ZORGE_FLESH_EATERS_PROGRESS_487, 3, true)
                addItemOrDrop(player, BLACK_PRISM)
            }
            return@on true
        }

        on(BACKPACK, IntType.ITEM, "open") { player, _ ->
            if (freeSlots(player) < 3) {
                sendMessage(player, "You don't have enough room in your inventory for the contents of this bag.")
                return@on false
            }
            openDialogue(
                player,
                object : DialogueFile() {
                    override fun handle(
                        componentID: Int,
                        buttonID: Int,
                    ) {
                        when (stage) {
                            0 -> {
                                sendDoubleItemDialogue(
                                    player,
                                    -1,
                                    BACKPACK,
                                    "Just before you open the backpack, you notice a small leather patch with the monkier: 'B.Uahn', on it.",
                                ).also { stage++ }
                                player.inventory.remove(Item(BACKPACK))
                            }

                            1 -> {
                                sendItemDialogue(
                                    player,
                                    DRAGON_TANKARD,
                                    "You find an interesting looking tankard.",
                                ).also { stage++ }
                                addItem(player, DRAGON_TANKARD, 1)
                                addItem(player, Items.KNIFE_946, 1)
                                addItem(player, Items.ROTTEN_FOOD_2959, 1)
                                setAttribute(player, ZUtils.DRAGON_TANKARD_ACQUIRED, true)
                            }

                            2 -> {
                                end()
                                sendMessage(player, "You find a knife and some rotten food.")
                                sendMessage(player, "You find an interesting looking tankard.")
                                sendDoubleItemDialogue(
                                    player,
                                    Items.KNIFE_5605,
                                    Items.ROTTEN_FOOD_2959,
                                    "You find a knife and some rotten food, the backpack is ripped to shreds.",
                                )
                            }
                        }
                    }
                },
            )
            return@on true
        }

        on(BLACK_PRISM, IntType.ITEM, "look-at") { player, _ ->
            sendDialogue(
                player,
                "It looks like a smokey black gem of some sort...very creepy. Some magical force must have prevented it from being shattered when it hit the coffin.",
            )
            return@on true
        }

        on(DRAGON_TANKARD, IntType.ITEM, "look-at") { player, _ ->
            sendDialogue(
                player,
                "A stout ceramic tankard with a Dragon Emblem on the side, the words, $BLUE'Ye Olde Dragon Inn' are inscribed in the bottom.'</col>.",
            )
            return@on true
        }

        on(TORN_PAGE, IntType.ITEM, "read") { player, _ ->
            openDialogue(
                player,
                object : DialogueFile() {
                    override fun handle(
                        componentID: Int,
                        buttonID: Int,
                    ) {
                        when (stage) {
                            0 ->
                                sendDialogue(
                                    player,
                                    "You don't manage to understand all of it as there is only a half page here. But it seems the spell was used to place a curse on an area and for all time raise the dead.",
                                ).also { stage++ }

                            1 ->
                                sendDialogue(
                                    player,
                                    "If you look very carefully, you see what looks like a guild emblem.",
                                ).also { stage++ }

                            2 -> end()
                        }
                    }
                },
            )
            return@on true
        }

        on(OGRE_DOORS, IntType.SCENERY, "open") { player, node ->
            if (!inInventory(player, Items.OGRE_GATE_KEY_4839) ||
                !getAttribute(
                    player,
                    ZUtils.RECEIVED_KEY_FROM_GRISH,
                    false,
                )
            ) {
                sendMessage(player, "These gates are locked, you don't seem to be able to open them.")
            } else {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                when {
                    player.location.y < 9434 -> sendMessage(player, "You push the gates open.")
                    else -> sendMessage(player, "You use the Ogre Tomb Key to unlock the door.")
                }
            }
            return@on true
        }

        on(STAND, IntType.SCENERY, "search") { player, _ ->
            sendDialogue(player, "You search the plinth...")
            when {
                getAttribute(player, ZUtils.SLASH_BASH_ACTIVE, false) -> return@on false
                else ->
                    if (getVarbit(player, Vars.VARBIT_QUEST_ZORGE_FLESH_EATERS_PROGRESS_487) >= 8) {
                        spawnZogreBoss(
                            player,
                        )
                    }
            }
            return@on true
        }
    }
}
