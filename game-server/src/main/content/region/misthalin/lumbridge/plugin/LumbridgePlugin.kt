package content.region.misthalin.lumbridge.plugin

import core.GlobalStatistics
import core.api.*
import core.game.activity.ActivityManager
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.Entity
import core.game.node.entity.combat.equipment.Ammunition
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.update.flag.context.Animation
import org.rs.consts.Animations
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.NPCs
import kotlin.math.floor

class LumbridgePlugin : InteractionListener {

    val CMB_TUTOR = intArrayOf(NPCs.MAGIC_TUTOR_4707, NPCs.RANGED_TUTOR_1861)
    var flagInUse: Boolean = false

    override fun defineListeners() {
        /*
         * Handles warning toggles.
         */

        on(NPCs.DOOMSAYER_3777, IntType.NPC, "toggle-warnings") { player, _ ->
            openInterface(player, Components.CWS_DOOMSAYER_583)
            return@on true
        }

        /*
         * Handles claiming interactions with combat tutors.
         */

        on(CMB_TUTOR, IntType.NPC, "claim") { player, node ->
            val npc = node as NPC
            openDialogue(player, npc.id, npc, true)
            return@on true
        }

        /*
         * Handles reading the gnome advertisement.
         */

        on(org.rs.consts.Scenery.ADVERTISEMENT_30037, IntType.SCENERY, "read") { player, _ ->
            sendDialogue(player, "Come check our gnome copters up north!")
            return@on true
        }

        /*
         * Handles reading the cow field signpost.
         */

        on(org.rs.consts.Scenery.SIGNPOST_31297, IntType.SCENERY, "read") { player, _ ->
            val cowDeaths = GlobalStatistics.getDailyCowDeaths()
            if (cowDeaths > 0) {
                sendDialogue(player, "Local cowherders have reported that $cowDeaths cows have been slain in this field today by passing adventurers. Farmers throughout the land fear this may be an epidemic.")
            } else {
                sendDialogue(player, "The Lumbridge cow population has been thriving today, without a single cow death to worry about!")
            }
            return@on true
        }

        /*
         * Handles reading the church signpost.
         */

        on(org.rs.consts.Scenery.SIGNPOST_31299, IntType.SCENERY, "read") { player, _ ->
            val deaths = GlobalStatistics.getDailyDeaths()
            if (deaths > 0) {
                sendDialogue(player, "So far today $deaths unlucky adventurers have died on RuneScape and been sent to their respawn location. Be careful out there.")
            } else {
                sendDialogue(player, "So far today not a single adventurer on RuneScape has met their end grisly or otherwise. Either the streets are getting safer or adventurers are getting warier.")
            }
            return@on true
        }

        /*
         * Handles reading the warning signpost.
         */

        on(org.rs.consts.Scenery.WARNING_SIGN_15566, IntType.SCENERY, "read") { player, _ ->
            openInterface(player, Components.MESSAGESCROLL_220).also {
                sendString(player, "<col=8A0808>~-~-~ WARNING ~-~-~", 220, 5)
                sendString(player, "<col=8A0808>Noxious gases vent into this cave.", 220, 7)
                sendString(player, "<col=8A0808>Naked flames may cause an explosion!", 220, 8)
                sendString(player, "<col=8A0808>Beware of vicious head-grabbing beasts!", 220, 10)
                sendString(player, "<col=8A0808>Contact a Slayer master for protective headgear.", 220, 11)
            }
            return@on true
        }

        /*
         * Handles interaction with the RFD chest for buying items or food.
         */

        on(org.rs.consts.Scenery.CHEST_12309, IntType.SCENERY, "buy-items", "buy-food") { player, _ ->
            CulinoChestPlugin.openShop(player, food = getUsedOption(player).lowercase() == "buy-food")
            return@on true
        }

        /*
         * Handles opening the bank account from the RFD chest.
         */

        on(org.rs.consts.Scenery.CHEST_12309, IntType.SCENERY, "bank") { player, _ ->
            openBankAccount(player)
            return@on true
        }

        /*
         * Handles playing the organ in the church.
         */

        on(org.rs.consts.Scenery.ORGAN_36978, IntType.SCENERY, "play") { player, _ ->
            lock(player, 10)
            ActivityManager.start(player, "organ cutscene", false)
            return@on true
        }

        /*
         * Handles ringing the church bell.
         */

        on(org.rs.consts.Scenery.BELL_36976, IntType.SCENERY, "ring") { player, _ ->
            sendMessage(player, "The townspeople wouldn't appreciate you ringing their bell.")
            return@on true
        }

        /*
         * Handles raising the castle flag.
         */

        on(org.rs.consts.Scenery.FLAG_37335, IntType.SCENERY, "raise") { player, node ->
            lock(player, 12)
            if (!flagInUse) {
                flagInUse = true
                submitIndividualPulse(
                    player,
                    object : Pulse(1, player) {
                        var counter: Int = 0

                        override fun pulse(): Boolean {
                            when (counter++) {
                                0 -> {
                                    sendMessage(player, "You start cranking the lever.")
                                    sendMessageWithDelay(player, "The flag reaches the top...", 8)
                                    sendChat(player, "All Hail the Duke!", 9)
                                    sendMessageWithDelay(player, "...and slowly descends.", 12)
                                    animateScenery(node.asScenery(), 9979)
                                    animate(player, Animations.LUMBRIDG_FLAG_CRAKING_A_9977)
                                }

                                8 -> animate(player, Animations.LUMBRIDG_FLAG_CRAKING_B_9978)
                            }
                            return counter >= 20
                        }

                        override fun stop() {
                            super.stop()
                            flagInUse = false
                        }
                    },
                )
            }

            return@on true
        }

        /*
         * Handles viewing the tutor map.
         */

        on(org.rs.consts.Scenery.LUMBRIDGE_MAP_37655, IntType.SCENERY, "view") { player, _ ->
            openInterface(player, Components.TUTOR_MAP_270)
            return@on true
        }

        /*
         * Handles shooting at the archery target.
         */

        on(org.rs.consts.Scenery.ARCHERY_TARGET_37095, IntType.SCENERY, "shoot-at") { player, node ->
            if (!anyInEquipment(player, Items.TRAINING_ARROWS_9706, Items.TRAINING_BOW_9705)) {
                sendMessage(player, "You need a training bow and arrow to practice here.")
                return@on true
            }
            player.pulseManager.run(ArcheryTargetPulse(player, node.asScenery()))
            return@on true
        }

        /*
         * Handles taking tools from the scenery.
         */

        on(org.rs.consts.Scenery.TOOLS_10375, IntType.SCENERY, "take") { player, node ->
            if (freeSlots(player) < 2) {
                sendMessage(player, "You do not have enough inventory space.")
                return@on true
            }
            addItem(player, Items.RAKE_5341)
            addItem(player, Items.SPADE_952)
            replaceScenery(node.asScenery(), 10373, 300)
            return@on true
        }
    }

    private class ArcheryTargetPulse(
        private val player: Player,
        private val node: Scenery,
    ) : Pulse(1, player, node) {
        override fun pulse(): Boolean {
            if (delay == 1) {
                delay = player.properties.attackSpeed
            }

            if (player.equipment.remove(Item(Items.TRAINING_ARROWS_9706, 1))) {
                val p = Ammunition.get(Items.TRAINING_ARROWS_9706)?.projectile?.transform(player, node.location)
                p?.endLocation = node.location
                p?.endHeight = 25
                p?.send()
                player.animate(Animation(426))
                val entity: Entity = player
                val level = entity.getSkills().getLevel(Skills.RANGE)
                val bonus = entity.properties.bonuses[14]
                var prayer = 1.0
                if (entity is Player) {
                    prayer += entity.prayer.getSkillBonus(Skills.RANGE)
                }
                var cumulativeStr = floor(level * prayer)
                if (entity.properties.attackStyle!!.style == WeaponInterface.STYLE_RANGE_ACCURATE) {
                    cumulativeStr += 3.0
                } else if (entity.properties.attackStyle!!.style == WeaponInterface.STYLE_LONG_RANGE) {
                    cumulativeStr += 1.0
                }
                cumulativeStr *= 1.0
                val hit =
                    (14.0 + cumulativeStr + (bonus.toDouble() / 8) + ((cumulativeStr * bonus) * 0.016865)).toInt() / 10 +
                        1
                player.getSkills().addExperience(Skills.RANGE, ((hit * 1.33) / 10))
                return !inEquipment(player, Items.TRAINING_ARROWS_9706, 1) ||
                    !inEquipment(player, Items.TRAINING_BOW_9705, 1)
            } else {
                return true
            }
        }
    }
}
