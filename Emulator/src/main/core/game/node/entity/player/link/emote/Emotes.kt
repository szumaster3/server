package core.game.node.entity.player.link.emote

import content.data.GameAttributes
import core.api.*
import core.game.container.impl.EquipmentContainer
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.Rights
import core.game.node.entity.player.link.diary.DiaryType
import core.game.system.task.Pulse
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.RegionManager.getLocalNpcs
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

enum class Emotes(
    val buttonId: Int,
    val animation: Int? = -1,
    val graphics: Int? = -1,
    val lockedMessage: String? = null,
) {
    YES(buttonId = 2, animation = Animations.NOD_HEAD_855),
    NO(buttonId = 3, animation = Animations.SHAKE_HEAD_856),
    BOW(buttonId = 4, animation = Animations.BOW_858) {
        override fun play(player: Player) {
            val hat = player.equipment[EquipmentContainer.SLOT_LEGS]
            if (hat?.id == Items.PANTALOONS_10396) {
                forceEmote(player, Animations.HUMAN_CURTSY_5312, -1)
            } else {
                super.play(player)
            }
        }
    },
    ANGRY(buttonId = 5, animation = Animations.ANGRY_859) {
        override fun play(player: Player) {
            val hat = player.equipment[EquipmentContainer.SLOT_HAT]
            if (hat?.id == Items.A_POWDERED_WIG_10392) {
                forceEmote(player, Animations.POWDERED_WIG_ANGRY_EMOTE_E_5315, -1)
            } else {
                super.play(player)
            }
        }
    },
    THINK(buttonId = 6, animation = Animations.THINK_857),
    WAVE(buttonId = 7, animation = Animations.WAVE_863) {
        override fun play(player: Player) {
            val weapon = player.equipment[EquipmentContainer.SLOT_WEAPON]
            if (weapon?.id == Items.SLED_4084) {
                forceEmote(player, Animations.WAVE_ON_SLED_1483, -1)
            } else {
                super.play(player)
            }
        }
    },
    SHRUG(buttonId = 8, animation = Animations.SHRUG_2113),
    CHEER(buttonId = 9, animation = Animations.CHEER_862) {
        override fun play(player: Player) {
            val weapon = player.equipment[EquipmentContainer.SLOT_WEAPON]
            if (weapon?.id == Items.SLED_4084) {
                forceEmote(player, Animations.CHEER_ON_SLED_1482, -1)
            } else {
                super.play(player)
            }
        }
    },
    BECKON(buttonId = 10, animation = Animations.BECKON_864) {
        override fun play(player: Player) {
            val weapon = player.equipment[EquipmentContainer.SLOT_WEAPON]
            when {
                weapon?.id == Items.SLED_4084 -> forceEmote(player, Animations.BECKON_ON_SLED_1484, -1)
                anyInEquipment(
                    player,
                    Items.HARD_HAT_10862,
                    Items.BUILDERS_SHIRT_10863,
                    Items.BUILDERS_TROUSERS_10864,
                    Items.BUILDERS_BOOTS_10865,
                    Items.PLAIN_SATCHEL_10877,
                    Items.GREEN_SATCHEL_10878,
                    Items.RED_SATCHEL_10879,
                    Items.BLACK_SATCHEL_10880,
                    Items.GOLD_SATCHEL_10881,
                    Items.RUNE_SATCHEL_10882,
                ) -> forceEmote(player, Animations.EMOTE_BECKON_5845, -1)
                else -> super.play(player)
            }
        }
    },
    JUMP_FOR_JOY(buttonId = 11, animation = Animations.JUMP_FOR_JOY_2109),
    LAUGH(buttonId = 12, animation = Animations.LAUGH_861),
    YAWN(buttonId = 13, animation = Animations.YAWN_2111) {
        override fun play(player: Player) {
            val hat = player.equipment[EquipmentContainer.SLOT_HAT]
            if (hat?.id == Items.SLEEPING_CAP_10398) {
                forceEmote(player, Animations.HUMAN_YAWN_ENHANCED_5313, -1)
            } else {
                super.play(player)
            }
        }
    },
    DANCE(buttonId = 14, animation = Animations.DANCE_866) {
        override fun play(player: Player) {
            val legs = player.equipment[EquipmentContainer.SLOT_LEGS]
            if (legs?.id == Items.FLARED_TROUSERS_10394) {
                forceEmote(player, Animations.HUMAN_DANCE_ENHANCED_5316, -1)
            } else {
                super.play(player)
            }
        }
    },
    JIG(buttonId = 15, animation = Animations.JIG_EMOTE_2106),
    SPIN(buttonId = 16, animation = Animations.HUMAN_TWIRL_2107),
    HEADBANG(buttonId = 17, animation = Animations.HEADBANG_EMOTE_2108),
    CRY(buttonId = 18, animation = Animations.CRY_860),
    BLOW_KISS(buttonId = 19, animation = Animations.NULL_1366, graphics = org.rs.consts.Graphics.HEART_574),
    PANIC(buttonId = 20, animation = Animations.PANIC_2105),
    RASPBERRY(buttonId = 21, animation = Animations.RASPBERRY_2110),
    CLAP(buttonId = 22, animation = Animations.CLAP_865) {
        override fun play(player: Player) {
            val weapon = player.equipment[EquipmentContainer.SLOT_WEAPON]
            if (weapon?.id == Items.SLED_4084) {
                forceEmote(player, Animations.CLAP_ON_SLED_1485, -1)
            } else {
                super.play(player)
            }
        }
    },
    SALUTE(buttonId = 23, animation = Animations.SALUTE_2112) {
        override fun play(player: Player) {
            if (isEligibleForSalute(player)) {
                forceEmote(player, Animations.SALUTE_2112, -1)
                finishDiaryTask(player, DiaryType.FALADOR, 1, 8)
            } else {
                super.play(player)
            }
        }
    },
    GOBLIN_BOW(
        buttonId = 24,
        animation = Animations.GOBLIN_BOW_EMOTE_E_2127,
        lockedMessage = "This emote can be unlocked during the Lost Tribe quest.",
    ) {
        override fun play(player: Player) {
            if (isEligibleForGoblinBow(player)) {
                getLocalNpcs(player).forEach { npc ->
                    if (npc.id == NPCs.MISTAG_2084 &&
                        withinDistance(
                            npc,
                            player.location,
                            3,
                        ) &&
                        player.getQuestRepository().getQuest(Quests.THE_LOST_TRIBE).getStage(player) == 45
                    ) {
                        player.dialogueInterpreter.open(NPCs.MISTAG_2084, npc, "greeting")
                        setAttribute(player, "/save:mistag-greeted", true)
                    }
                }
            }
            super.play(player)
        }
    },
    GOBLIN_SALUTE(
        buttonId = 25,
        animation = Animations.GOBLIN_SALUTE_EMOTE_E_2128,
        lockedMessage = "This emote can be unlocked during the Lost Tribe quest.",
    ),
    GLASS_BOX(
        buttonId = 26,
        animation = Animations.GLASS_BOX_EMOTE_1131,
        lockedMessage = "This emote can be unlocked during the Mime random event.",
    ),
    CLIMB_ROPE(
        buttonId = 27,
        animation = Animations.CLIMB_ROPE_EMOTE_1130,
        lockedMessage = "This emote can be unlocked during the Mime random event.",
    ),
    LEAN_ON_AIR(
        buttonId = 28,
        animation = Animations.LEAN_EMOTE_1129,
        lockedMessage = "This emote can be unlocked during the Mime random event.",
    ),
    GLASS_WALL(
        buttonId = 29,
        animation = Animations.HUMAN_GLASS_WALL_1128,
        lockedMessage = "This emote can be unlocked during the Mime random event.",
    ),
    IDEA(
        buttonId = 33,
        animation = Animations.HUMAN_IDEA_4276,
        graphics = org.rs.consts.Graphics.LIGHT_BULB_IDEA_EMOTE_712,
        lockedMessage = "You can't use this emote yet. Visit the Stronghold of Security to unlock it.",
    ),
    STOMP(
        buttonId = 31,
        animation = Animations.HUMAN_STOMP_4278,
        lockedMessage = "You can't use this emote yet. Visit the Stronghold of Security to unlock it.",
    ),
    FLAP(
        buttonId = 32,
        animation = Animations.FLAP_EMOTE_E_4280,
        lockedMessage = "You can't use this emote yet. Visit the Stronghold of Security to unlock it.",
    ) {
        override fun play(player: Player) {
            if (isWearingChickenOutfit(player)) {
                forceEmote(player, Animations.HUMAN_FLAP_ENHANCED_3859, -1)
            } else {
                super.play(player)
            }
        }
    },
    SLAP_HEAD(
        buttonId = 30,
        animation = Animations.HUMAN_SLAP_HEAD_4275,
        lockedMessage = "You can't use this emote yet. Visit the Stronghold of Security to unlock it.",
    ),
    ZOMBIE_WALK(
        buttonId = 34,
        animation = Animations.HUMAN_ZOMBIE_WALK_3544,
        lockedMessage = "This emote can be unlocked during the Gravedigger random event.",
    ),
    ZOMBIE_DANCE(
        buttonId = 35,
        animation = Animations.HUMAN_ZOMBIE_DANCE_3543,
        lockedMessage = "This emote can be unlocked during the Gravedigger random event.",
    ),
    ZOMBIE_HAND(
        buttonId = 36,
        animation = Animations.HUMAN_ZOMBIE_HAND_7272,
        graphics = org.rs.consts.Graphics.ZOMBIE_HAND_EMOTE_TAB_1244,
        lockedMessage = "This emote can be unlocked during the Gravedigger random event.",
    ),
    SCARED(
        buttonId = 37,
        animation = Animations.HUMAN_SCARED_2836,
        lockedMessage = "This emote can be unlocked by playing a Halloween holiday event.",
    ),
    BUNNY_HOP(
        buttonId = 38,
        animation = Animations.HUMAN_BUNNY_HOP_6111,
        lockedMessage = "This emote can be unlocked by playing an Easter holiday event.",
    ),
    SKILLCAPE(buttonId = 39) {
        override fun play(player: Player) {
            val cape = player.equipment[EquipmentContainer.SLOT_CAPE]
            if (cape == null) {
                sendMessage(player, "You need to be wearing a skillcape in order to perform this emote.")
                return
            }
            val capeId = cape.id
            SKILLCAPE_INFO.firstOrNull { capeId == it[0] || capeId == it[1] }?.let { info ->
                visualize(player, info[3], info[2])
                player.locks.lock("emote", Animation.create(info[3]).duration)
            } ?: sendMessage(player, "You need to be wearing a skillcape in order to perform this emote.")
        }
    },
    SNOWMAN_DANCE(
        buttonId = 40,
        animation = Animations.HUMAN_SNOWMAN_DANCE_7531,
        lockedMessage = "This emote can be unlocked by playing a Christmas holiday event.",
    ),
    AIR_GUITAR(
        buttonId = 41,
        animation = Animations.HUMAN_AIR_GUITAR_2414,
        graphics = 1537,
        lockedMessage = "This emote can be accessed by unlocking 200 pieces of music.",
    ) {
        override fun play(player: Player) {
            playJingle(player, 302)
            super.play(player)
        }
    },
    SAFETY_FIRST(
        buttonId = 42,
        animation = Animations.HUMAN_SAFETY_FIRST_8770,
        graphics = org.rs.consts.Graphics.SAFETY_FIRST_1553,
        lockedMessage = "You can't use this emote yet. Visit the Stronghold of Player safety to unlock it.",
    ),
    EXPLORE(
        buttonId = 43,
        animation = Animations.HUMAN_EXPLORE_9990,
        graphics = org.rs.consts.Graphics.EXPLORE_EMOTE_1734,
        lockedMessage = "You can't use this emote yet. You must complete all the Lumbridge and Draynor beginner tasks to unlock it.",
    ),
    TRICK(
        buttonId = 44,
        animation = Animations.HUMAN_TRICK_10530,
        graphics = org.rs.consts.Graphics.TRICK_1863,
        lockedMessage = "This emote can be unlocked by playing a Halloween holiday event.",
    ),
    FREEZE(
        buttonId = 45,
        animation = Animations.HUMAN_FREEZE_MELT_11044,
        graphics = org.rs.consts.Graphics.FREEZE_EMOTE_1973,
        lockedMessage = "This emote can be unlocked by playing a Christmas holiday event.",
    ),
    GIVE_THANKS(
        buttonId = 46,
        lockedMessage = "This emote can be unlocked by playing a Thanksgiving holiday event.",
    ) {
        override fun play(player: Player) {
            lock(player, 17)
            submitWorldPulse(
                object : Pulse(1, player) {
                    var counter: Int = 0

                    override fun pulse(): Boolean {
                        when (counter++) {
                            1 ->
                                forceEmote(
                                    player,
                                    Animations.GIVE_THANKS_BEGIN_10994,
                                    org.rs.consts.Graphics.RANDOM_EVENT_PUFF_OF_SMOKE_86,
                                )
                            3 ->
                                player.appearance.transformNPC(NPCs.THANKSGIVING_TURKEY_8499).also {
                                    forceEmote(player, Animations.HUMAN_TURKEY_DANCE_10996, -1)
                                }
                            13 -> player.appearance.transformNPC(NPCs.TURKEY_8501)
                            16 ->
                                player.appearance.transformNPC(-1).also {
                                    forceEmote(
                                        player,
                                        Animations.GIVE_THANKS_END_10995,
                                        org.rs.consts.Graphics.RANDOM_EVENT_PUFF_OF_SMOKE_86,
                                    )
                                }
                        }
                        return false
                    }
                },
            )
        }
    }, ;

    constructor(buttonId: Int, animation: Int?, lockedMessage: String?) : this(
        buttonId = buttonId,
        animation = animation,
        graphics = -1,
        lockedMessage = lockedMessage,
    )

    constructor(buttonId: Int, lockedMessage: String?) : this(
        buttonId = buttonId,
        animation = -1,
        graphics = -1,
        lockedMessage = lockedMessage,
    )

    open fun play(player: Player) {
        forceEmote(player = player, animation = animation, graphic = graphics)
    }

    companion object {
        private val SKILLCAPE_INFO =
            arrayOf(
                intArrayOf(
                    Items.ATTACK_CAPE_9747,
                    Items.ATTACK_CAPET_9748,
                    org.rs.consts.Graphics.ATTACK_SKILLCAPE_823,
                    4959,
                ),
                intArrayOf(
                    Items.STRENGTH_CAPE_9750,
                    Items.STRENGTH_CAPET_9751,
                    org.rs.consts.Graphics.STRENGTH_SKILLCAPE_828,
                    4981,
                ),
                intArrayOf(
                    Items.DEFENCE_CAPE_9753,
                    Items.DEFENCE_CAPET_9754,
                    org.rs.consts.Graphics.DEFENCE_SKILLCAPE_824,
                    4961,
                ),
                intArrayOf(
                    Items.RANGING_CAPE_9756,
                    Items.RANGING_CAPET_9757,
                    org.rs.consts.Graphics.RANGED_SKILLCAPE_832,
                    4973,
                ),
                intArrayOf(
                    Items.PRAYER_CAPE_9759,
                    Items.PRAYER_CAPET_9760,
                    org.rs.consts.Graphics.PRAYER_SKILLCAPE_829,
                    4979,
                ),
                intArrayOf(
                    Items.MAGIC_CAPE_9762,
                    Items.MAGIC_CAPET_9763,
                    org.rs.consts.Graphics.MAGIC_SKILLCAPE_813,
                    4939,
                ),
                intArrayOf(
                    Items.RUNECRAFT_CAPE_9765,
                    Items.RUNECRAFT_CAPET_9766,
                    org.rs.consts.Graphics.RC_SKILLCAPE_817,
                    4947,
                ),
                intArrayOf(
                    Items.HITPOINTS_CAPE_9768,
                    Items.HITPOINTS_CAPET_9769,
                    org.rs.consts.Graphics.HP_SKILLCAPE_MALE_833,
                    4971,
                ),
                intArrayOf(
                    Items.AGILITY_CAPE_9771,
                    Items.AGILITY_CAPET_9772,
                    org.rs.consts.Graphics.AGILITY_SKILLCAPE_830,
                    4977,
                ),
                intArrayOf(
                    Items.HERBLORE_CAPE_9774,
                    Items.HERBLORE_CAPET_9775,
                    org.rs.consts.Graphics.HERBLORE_SKILLCAPE_835,
                    4969,
                ),
                intArrayOf(
                    Items.THIEVING_CAPE_9777,
                    Items.THIEVING_CAPET_9778,
                    org.rs.consts.Graphics.THIEVING_SKILLCAPE_826,
                    4965,
                ),
                intArrayOf(
                    Items.CRAFTING_CAPE_9780,
                    Items.CRAFTING_CAPET_9781,
                    org.rs.consts.Graphics.CRAFTING_SKILLCAPE_818,
                    4949,
                ),
                intArrayOf(
                    Items.FLETCHING_CAPE_9783,
                    Items.FLETCHING_CAPET_9784,
                    org.rs.consts.Graphics.FLETCHING_SKILLCAPE_812,
                    4937,
                ),
                intArrayOf(
                    Items.SLAYER_CAPE_9786,
                    Items.SLAYER_CAPET_9787,
                    org.rs.consts.Graphics.SLAYER_ACCOMPLISHMENT_CAPE_1656,
                    4967,
                ),
                intArrayOf(
                    Items.CONSTRUCT_CAPE_9789,
                    Items.CONSTRUCT_CAPET_9790,
                    org.rs.consts.Graphics.CON_SKILLCAPE_820,
                    4953,
                ),
                intArrayOf(
                    Items.MINING_CAPE_9792,
                    Items.MINING_CAPET_9793,
                    org.rs.consts.Graphics.MINING_SKILLCAPE_814,
                    4941,
                ),
                intArrayOf(
                    Items.SMITHING_CAPE_9795,
                    Items.SMITHING_CAPET_9796,
                    org.rs.consts.Graphics.SMITHING_SKILLCAPE_815,
                    4943,
                ),
                intArrayOf(
                    Items.FISHING_CAPE_9798,
                    Items.FISHING_CAPET_9799,
                    org.rs.consts.Graphics.FISHING_SKILLCAPE_819,
                    4951,
                ),
                intArrayOf(
                    Items.COOKING_CAPE_9801,
                    Items.COOKING_CAPET_9802,
                    org.rs.consts.Graphics.COOKING_SKILLCAPE_821,
                    4955,
                ),
                intArrayOf(
                    Items.FIREMAKING_CAPE_9804,
                    Items.FIREMAKING_CAPET_9805,
                    org.rs.consts.Graphics.FIREMAKING_SKILLCAPE_831,
                    4975,
                ),
                intArrayOf(
                    Items.WOODCUTTING_CAPE_9807,
                    Items.WOODCUT_CAPET_9808,
                    org.rs.consts.Graphics.WC_SKILLCAPE_822,
                    4957,
                ),
                intArrayOf(
                    Items.FARMING_CAPE_9810,
                    Items.FARMING_CAPET_9811,
                    org.rs.consts.Graphics.FARMING_SKILLCAPE_825,
                    4963,
                ),
                intArrayOf(
                    Items.SUMMONING_CAPE_12169,
                    Items.SUMMONING_CAPET_12170,
                    org.rs.consts.Graphics.SUMMONING_SKILLCAPE_EMOTE_1515,
                    8525,
                ),
                intArrayOf(Items.QUEST_POINT_CAPE_9813, -1, org.rs.consts.Graphics.QUEST_SKILLCAPE_816, 4945),
                intArrayOf(
                    Items.HUNTER_CAPE_9948,
                    Items.HUNTER_CAPET_9949,
                    org.rs.consts.Graphics.HUNTER_SKILLCAPE_EMOTE_907,
                    5158,
                ),
            )

        fun handle(
            player: Player,
            buttonId: Int,
        ) {
            val completeTutorial = getAttribute(player, GameAttributes.TUTORIAL_COMPLETE, false)
            if (player.locks.isLocked("emote")) {
                if (!completeTutorial) {
                    sendTutorialMessage(player, "You're already doing an emote!")
                } else {
                    sendMessage(player, "You're already doing an emote!")
                }
                return
            }
            if (player.properties.combatPulse.isAttacking || player.inCombat()) {
                sendMessage(player, "You can't perform an emote while being in combat.")
                return
            }
            val emote =
                forId(buttonId) ?: run {
                    player.debug("Unhandled emote for button id - $buttonId")
                    return
                }
            if (!player.emoteManager.isUnlocked(emote)) {
                if (player.rights == Rights.ADMINISTRATOR) {
                    player.emoteManager.unlock(emote)
                    player.pulseManager.clear()
                    emote.play(player)
                    return
                }
                sendDialogue(player, emote.lockedMessage ?: "You can't use this emote.")
                return
            }
            if (!player.achievementDiaryManager.getDiary(DiaryType.VARROCK).isComplete(1, 6) && (buttonId in 30..33)) {
                if (!player.getAttribute("emote-$buttonId", false)) {
                    setAttribute(player, "emote-$buttonId", true)
                }
                val good = (30..33).all { player.getAttribute("emote-$it", false) }
                player.achievementDiaryManager.getDiary(DiaryType.VARROCK).updateTask(player, 1, 6, good)
            }
            player.pulseManager.clear()
            emote.play(player)
        }

        private fun forceEmote(
            player: Player,
            animation: Int?,
            graphic: Int?,
        ) {
            animate(player, animation)
            Graphics.send(graphic?.let { Graphics.create(it) }, player.location)
            animation?.let { Animation.create(it).duration }?.let { player.locks.lock("emote", it) }
        }

        @JvmStatic
        fun forId(buttonId: Int): Emotes? {
            return values().find { it.buttonId == buttonId }
        }

        private fun isWearingChickenOutfit(player: Player): Boolean {
            val head = player.equipment[EquipmentContainer.SLOT_HAT]
            val wings = player.equipment[EquipmentContainer.SLOT_CHEST]
            val legs = player.equipment[EquipmentContainer.SLOT_LEGS]
            val feet = player.equipment[EquipmentContainer.SLOT_FEET]
            return head?.id == Items.CHICKEN_HEAD_11021 &&
                wings?.id == Items.CHICKEN_WINGS_11020 &&
                legs?.id == Items.CHICKEN_LEGS_11022 &&
                feet?.id == Items.CHICKEN_FEET_11019
        }

        private fun isEligibleForSalute(player: Player): Boolean {
            return !player.achievementDiaryManager.hasCompletedTask(DiaryType.FALADOR, 1, 8) &&
                player.location == Location(2997, 3374, 0) &&
                player.direction == Direction.SOUTH &&
                anyInEquipment(
                    player,
                    Items.INITIATE_SALLET_5574,
                    Items.INITIATE_HAUBERK_5575,
                    Items.INITIATE_CUISSE_5576,
                )
        }

        private fun isEligibleForGoblinBow(player: Player): Boolean {
            return player.location.regionId == 13206 && !player.getAttribute("mistag-greeted", false)
        }
    }
}
