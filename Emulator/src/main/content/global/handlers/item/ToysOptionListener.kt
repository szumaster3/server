package content.global.handlers.item

import content.global.dialogue.ZombieHeadDialogue
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.world.update.flag.context.Animation
import org.rs.consts.*

class ToysOptionListener : InteractionListener {
    override fun defineListeners() {
        on(MARIONETTES, IntType.ITEM, "jump", "walk", "bow", "dance") { player, marionette ->
            val index = MARIONETTES.indexOf(marionette.id)

            lockInteractions(player, 2)
            when (getUsedOption(player)) {
                "jump" -> visualize(player, MARIONETTE_JUMP, MARIONETTE_GFX[index][0])
                "walk" -> visualize(player, MARIONETTE_WALK, MARIONETTE_GFX[index][1])
                "bow" -> visualize(player, MARIONETTE_BOW, MARIONETTE_GFX[index][2])
                "dance" -> visualize(player, MARIONETTE_DANCE, MARIONETTE_GFX[index][3])
            }
            return@on true
        }

        on(Items.SPINNING_PLATE_4613, IntType.ITEM, "spin") { player, _ ->
            lockInteractions(player, 2)
            animate(player, SPINNING_PLATE_SPIN)
            runTask(player, 2) {
                animate(player, SPINNING_PLATE_PUT_DOWN)
            }
            return@on true
        }

        on(Items.YO_YO_4079, IntType.ITEM, "play", "loop", "walk", "crazy") { player, _ ->
            val option = getUsedOption(player)

            lockInteractions(player, 2)
            when (option) {
                "play" -> animate(player, YOYO_PLAY)
                "loop" -> animate(player, YOYO_LOOP)
                "walk" -> animate(player, YOYO_WALK)
                "crazy" -> animate(player, YOYO_CRAZY)
            }
            return@on true
        }

        on(Items.RUBBER_CHICKEN_4566, IntType.ITEM, "operate", "Dance") { player, _ ->
            lockInteractions(player, 2)
            animate(player, Animations.CHICKEN_DANCE_1835)
            playJingle(player, 99)
            playAudio(player, Sounds.CHICKEN_ATTACK_355, 100)
            return@on true
        }

        on(Items.ZOMBIE_HEAD_6722, IntType.ITEM, "talk-at", "display", "question") { player, _ ->
            val option = getUsedOption(player)
            lockInteractions(player, 2)
            when (option) {
                "talk-at" -> {
                    animate(player, ZOMBIE_HEAD_TALK_AT)
                    sendChat(player, "Alas!")
                }

                "display" -> {
                    animate(player, ZOMBIE_HEAD_DISPLAY)
                    sendChat(player, "MWAHAHAHAHAHAHAH")
                }

                "question" -> openDialogue(player, ZombieHeadDialogue())
            }
            return@on true
        }

        on(Items.REINDEER_HAT_10507, IntType.ITEM, "operate") { player, _ ->
            lockInteractions(player, 2)
            visualize(player, Animations.REINDEER_HAT_EMOTE_5059, Graphics.RHUDOLF_EMOTE_GFX_859)
            return@on true
        }

        on(Items.SNOW_GLOBE_11949, IntType.ITEM, "shake") { player, _ ->
            lockInteractions(player, 2)
            animate(player, SNOWGLOBE_SHAKE)
            runTask(player, 3) {
                animate(player, SNOWGLOBE_HOLDFACE)
                runTask(player) {
                    openInterface(player, SNOWGLOBE_INTERFACE)
                }
            }
            return@on true
        }

        on(Items.CHOCATRICE_CAPE_12645, IntType.ITEM, "operate") { player, _ ->
            lockInteractions(player, 2)
            visualize(player, 8903, Graphics.CHOCATRICE_CAPE_EASTER_EGG_EMOTE_1566)
            return@on true
        }

        on(Items.TOY_KITE_12844, IntType.ITEM, "fly", "operate") { player, _ ->
            lockInteractions(player, 2)
            animate(player, TOY_KITE_FLY)
            return@on true
        }

        on(horseyMap.keys.toIntArray(), IntType.ITEM, "play-with") { player, node ->
            stopWalk(player)
            animate(player, horseyMap[node.id])
            sendChat(player, pharses.random())
            return@on true
        }
    }

    companion object {
        private val pharses =
            arrayOf("Come-on Dobbin, we can win the race!", "Hi-ho Silver, and away", "Neaahhhyyy! Giddy-up horsey!")

        private val horseyMap =
            mapOf(
                Items.TOY_HORSEY_2520 to Animations.HUMAN_PLAY_WITH_BROWN_HORSE_918,
                Items.TOY_HORSEY_2522 to Animations.HUMAN_PLAY_WITH_WHITE_HORSE_919,
                Items.TOY_HORSEY_2524 to Animations.HUMAN_PLAY_WITH_BLACK_HORSE_920,
                Items.TOY_HORSEY_2526 to Animations.HUMAN_PLAY_WITH_GRAY_HORSE_921,
            )

        private val SNOWGLOBE_SHAKE = Animation(Animations.SNOWGLOBE_SHAKE_7535)
        private val SNOWGLOBE_HOLDFACE = Animation(Animations.SNOWGLOBE_HOLD_FACE_7536)
        private val SNOWGLOBE_INTERFACE = Components.SNOWGLOBE_INTERFACE_659

        private val SPINNING_PLATE_SPIN = Animation(Animations.SPINNING_PLATE_1902)
        private val SPINNING_PLATE_PUT_DOWN = Animation(Animations.PLATE_1904)

        private val TOY_KITE_FLY = Animation(Animations.FLY_KITE_8990)

        private val YOYO_PLAY = Animation(Animations.YO_YO_1457)
        private val YOYO_LOOP = Animation(Animations.YO_YO_1458)
        private val YOYO_WALK = Animation(Animations.YO_YO_1459)
        private val YOYO_CRAZY = Animation(Animations.YO_YO_1460)

        private val ZOMBIE_HEAD_TALK_AT = Animation(Animations.ZOMBIE_HEAD_ALAS_2840)
        private val ZOMBIE_HEAD_DISPLAY = Animation(Animations.ZOMBIE_HEAD_2844)

        private val MARIONETTES =
            intArrayOf(Items.RED_MARIONETTE_6867, Items.BLUE_MARIONETTE_6865, Items.GREEN_MARIONETTE_6866)
        private val MARIONETTE_JUMP = Animation(Animations.MARIONETTE_JUMP_3003)
        private val MARIONETTE_WALK = Animation(Animations.MARIONETTE_WALK_3004)
        private val MARIONETTE_BOW = Animation(Animations.MARIONETTE_BOW_3005)
        private val MARIONETTE_DANCE = Animation(Animations.MARIONETTE_DANCE_3006)
        private val MARIONETTE_GFX =
            arrayOf(
                intArrayOf(
                    Graphics.JUMP_MARIONETTE_RED_507,
                    Graphics.WALK_MARIONETTE_RED_508,
                    Graphics.BOW_MARIONETTE_RED_509,
                    Graphics.DANCE_MARIONETTE_RED_510,
                ),
                intArrayOf(
                    Graphics.JUMP_MARIONETTE_BLUE_511,
                    Graphics.WALK_MARIONETTE_BLUE_512,
                    Graphics.BOW_MARIONETTE_BLUE_513,
                    Graphics.DANCE_MARIONETTE_BLUE_514,
                ),
                intArrayOf(
                    Graphics.JUMP_MARIONETTE_GREEN_515,
                    Graphics.WALK_MARIONETTE_GREEN_516,
                    Graphics.BOW_MARIONETTE_GREEN_517,
                    Graphics.DANCE_MARIONETTE_GREEN_518,
                ),
            )
    }
}
