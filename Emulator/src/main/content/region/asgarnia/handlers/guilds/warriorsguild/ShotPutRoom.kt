package content.region.asgarnia.handlers.guilds.warriorsguild

import core.api.animate
import core.api.removeAttribute
import core.cache.def.impl.ItemDefinition
import core.cache.def.impl.SceneryDefinition
import core.game.container.impl.EquipmentContainer
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueInterpreter
import core.game.dialogue.FaceAnim
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.combat.ImpactHandler.HitsplatType
import core.game.node.entity.impl.Projectile
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.GroundItem
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.ClassScanner.definePlugin
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.RandomFunction
import org.rs.consts.Animations
import org.rs.consts.Items
import kotlin.math.ceil

@Initializable
class ShotPutRoom(
    player: Player? = null,
) : Dialogue(player) {
    private var lowWeight = false

    override fun open(vararg args: Any): Boolean {
        lowWeight = args[0] as Boolean
        interpreter.sendOptions("Choose your style", "Standing throw", "Step and throw", "Spin and throw")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (buttonId) {
            1 -> {
                animate(player, Animation.create(Animations.HIT_WITH_DRAGON_PICKAXE_4181))
                throwShotPut(player, 5, lowWeight, "You throw the shot as hard as you can.")
            }

            2 -> {
                animate(player, Animation.create(Animations.HIT_WITH_RUNE_PICKAXE_4182))
                throwShotPut(player, 2, lowWeight, "You take a step and throw the shot as hard as you can.")
            }

            3 -> {
                animate(player, Animation.create(Animations.HIT_WITH_ADAMANT_PICKAXE_4183))
                throwShotPut(player, 5, lowWeight, "You spin around and release the shot.")
            }

            else -> return false
        }
        end()
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(DialogueInterpreter.getDialogueKey("shot_put"))
    }

    companion object {
        init {
            definePlugin(
                object : OptionHandler() {
                    override fun handle(
                        player: Player,
                        node: Node,
                        option: String,
                    ): Boolean {
                        val lowWeight = node.id == 15664
                        if (node is GroundItem) {
                            player.dialogueInterpreter.sendDialogues(
                                4300,
                                FaceAnim.FURIOUS,
                                "Hey! You can't take that, it's guild property. Take one",
                                "from the pile.",
                            )
                            return true
                        }
                        if (player.equipment[EquipmentContainer.SLOT_WEAPON] != null ||
                            player.equipment[EquipmentContainer.SLOT_SHIELD] != null ||
                            player.equipment[EquipmentContainer.SLOT_HANDS] != null
                        ) {
                            player.dialogueInterpreter.sendDialogue("To throw the shot you need your hands free!")
                            return true
                        }
                        if (player.settings.runEnergy < 10) {
                            player.dialogueInterpreter.sendDialogue(
                                "You're too exhausted to throw the shot at this time. Take a break.",
                            )
                            return true
                        }
                        player.lock(4)
                        player.animate(Animation.create(Animations.MULTI_BEND_OVER_827))
                        Pulser.submit(
                            object : Pulse(2) {
                                override fun pulse(): Boolean {
                                    player.faceLocation(player.location.transform(3, 0, 0))
                                    player.dialogueInterpreter.open("shot_put", lowWeight)
                                    return true
                                }
                            },
                        )
                        return true
                    }

                    override fun getDestination(
                        n: Node,
                        node: Node,
                    ): Location? {
                        if (node is Scenery) {
                            return node.getLocation().transform(0, -1, 0)
                        }
                        return null
                    }

                    override fun newInstance(arg: Any?): Plugin<Any> {
                        SceneryDefinition.forId(15664).handlers["option:throw"] = this
                        SceneryDefinition.forId(15665).handlers["option:throw"] = this
                        ItemDefinition.forId(Items.EIGHTEEN_LB_SHOT_8858).handlers["option:take"] = this
                        ItemDefinition.forId(Items.TWENTY_TWO_LB_SHOT_8859).handlers["option:take"] = this
                        return this
                    }
                },
            )
        }

        private fun throwShotPut(
            player: Player,
            delay: Int,
            lowWeight: Boolean,
            message: String,
        ) {
            player.lock()
            var cost = if (lowWeight) 6 else 12
            if (player.getAttribute<Boolean>("hand_dust", false)) {
                removeAttribute(player, "hand_dust")
                cost = 0
            }
            var distance = 1
            if (RandomFunction.randomize(25 - cost) != 0) {
                val mod = (RandomFunction.randomize(cost) * (1 - (player.settings.runEnergy / (100 + cost)))).toInt()
                distance += RandomFunction.randomize(12 - mod)
            }
            val failed = distance < 2
            val tiles = distance
            player.packetDispatch.sendMessage("You take a deep breath and prepare yourself.")
            Pulser.submit(
                object : Pulse(delay, player) {
                    var loc: Location = player.location
                    var thrown: Boolean = false

                    override fun pulse(): Boolean {
                        if (!thrown) {
                            player.settings.updateRunEnergy(10.0)
                            player.packetDispatch.sendMessage(message)
                            if (failed) {
                                player.packetDispatch.sendMessage("You fumble and drop the shot on your toe. Ow!")
                                player.impactHandler.manualHit(player, 1, HitsplatType.NORMAL, 2)
                            }
                            val speed = 30 + (tiles * 10)
                            val projectile =
                                Projectile.create(
                                    loc,
                                    loc.transform(tiles, 0, 0).also { loc = it },
                                    690,
                                    40,
                                    0,
                                    if (getDelay() == 5) 5 else 12,
                                    speed,
                                    20,
                                    11,
                                )
                            projectile.send()
                            setDelay(1 + ceil(tiles * 0.3).toInt())
                            thrown = true
                            return false
                        }
                        player.unlock()
                        if (!failed) {
                            player.getSavedData().activityData.updateWarriorTokens(tiles + (if (!lowWeight) 2 else 0))
                            player.getSkills().addExperience(Skills.STRENGTH, tiles.toDouble())
                            player.dialogueInterpreter.sendDialogues(
                                if (lowWeight) 4299 else 4300,
                                FaceAnim.HALF_GUILTY,
                                "Well done. You threw the shot " + (tiles - 1) + " yard" +
                                    (if (tiles > 2) "s!" else "!"),
                            )
                        }
                        GroundItemManager.create(GroundItem(Item(if (lowWeight) Items.EIGHTEEN_LB_SHOT_8858 else Items.TWENTY_TWO_LB_SHOT_8859), loc, 20, player))
                        return true
                    }
                },
            )
        }
    }
}
