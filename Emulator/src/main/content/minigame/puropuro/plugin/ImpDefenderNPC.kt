package content.minigame.puropuro.plugin

import content.global.skill.hunter.bnet.BNetTypes
import core.api.*
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.map.RegionManager
import core.game.world.map.zone.ZoneBorders
import core.tools.RandomFunction
import org.rs.consts.Items
import org.rs.consts.NPCs

class ImpDefenderNPC : NPCBehavior(NPCs.IMP_DEFENDER_6074) {
    override fun onCreation(self: NPC) {
        self.isWalks = true
        self.isNeverWalks = false
        self.walkRadius = 30
    }

    override fun tick(self: NPC): Boolean {
        if (!RandomFunction.roll(10)) return true

        var nextCaptureTick = getAttribute(self, "next-capture-tick", 0)
        if (getWorldTicks() < nextCaptureTick) return true

        var players = RegionManager.getLocalPlayers(self, 2)
        for (player in players) {
            var lowestTierImpling = BNetTypes.getImpling(player) ?: continue
            var jarItem = lowestTierImpling.reward
            setAttribute(self, "capture-target", player)
            setAttribute(self, "capture-item", jarItem)
            setAttribute(self, "next-capture-tick", getWorldTicks() + RandomFunction.random(25, 100))
            submitIndividualPulse(self, TryReleasePulse(self))
            break
        }

        return true
    }

    private class TryReleasePulse(
        val self: NPC,
    ) : Pulse() {
        companion object {
            const val catchPlayerLow = 35.0

            const val catchPlayerHigh = 280.0

            const val impRepellentBonus = 20.0
        }

        var counter = 0

        override fun pulse(): Boolean {
            val player: Player? = getAttribute(self, "capture-target", null)
            val jarItem: Item? = getAttribute(self, "capture-item", null)
            if (player == null || jarItem == null) return true
            when (counter++) {
                0 -> {
                    face(self, player)
                    animate(self, 6628)
                    delay = animationDuration(getAnimation(6628))
                    return false
                }

                1 -> {
                    var hasRepellent = inInventory(player, Items.IMP_REPELLENT_11262)
                    var baseRoll = RandomFunction.randomDouble(100.0)
                    var playerRoll =
                        RandomFunction.getSkillSuccessChance(
                            catchPlayerLow + if (hasRepellent) impRepellentBonus else 0.0,
                            catchPlayerHigh + if (hasRepellent) impRepellentBonus else 0.0,
                            getStatLevel(player, Skills.THIEVING),
                        )
                    if (playerRoll < baseRoll) {
                        sendChat(self, "Be free!")
                        animate(self, 6629)
                        removeItem(player, jarItem)
                        var loc =
                            ZoneBorders(
                                self.location.x - 2,
                                self.location.y - 2,
                                self.location.x + 2,
                                self.location.y + 2,
                            ).randomLoc
                        GroundItemManager.create(Item(Items.IMPLING_JAR_11260), loc, player)
                    }
                    resetFace(self)
                }
            }
            return true
        }
    }
}
