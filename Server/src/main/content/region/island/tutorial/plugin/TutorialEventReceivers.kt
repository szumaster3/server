package content.region.island.tutorial.plugin

import content.data.GameAttributes
import content.global.skill.gather.fishing.FishingSpot
import content.global.skill.gather.mining.MiningNode
import content.global.skill.gather.woodcutting.WoodcuttingNode
import core.api.getAttribute
import core.api.setAttribute
import core.game.event.*
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.SpellBookManager
import shared.consts.Components
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Scenery

object TutorialButtonReceiver : EventHook<ButtonClickEvent> {
    override fun process(entity: Entity, event: ButtonClickEvent) {
        if (entity !is Player) return
        val player = entity.asPlayer()
        when (getAttribute(player, GameAttributes.TUTORIAL_STAGE, 0)) {
            0 -> if (event.iface == Components.APPEARANCE_771 && event.buttonId == 362) {
                setAttribute(player, GameAttributes.TUTORIAL_STAGE, 39)
                TutorialStage.load(player, 39)
            }

            1 -> if ((event.iface == Components.TOPLEVEL_548 && event.buttonId == 24) || (event.iface == Components.TOPLEVEL_FULLSCREEN_746 && event.buttonId == 52)) {
                setAttribute(player, GameAttributes.TUTORIAL_STAGE, 2)
                TutorialStage.load(player, 2)
            }

            5 -> if ((event.iface == Components.TOPLEVEL_548 && event.buttonId == 41) || (event.iface == Components.TOPLEVEL_FULLSCREEN_746 && event.buttonId == 44)) {
                setAttribute(player, GameAttributes.TUTORIAL_STAGE, 6)
                TutorialStage.load(player, 6)
            }

            10 -> if ((event.iface == Components.TOPLEVEL_548 && event.buttonId == 39) || (event.iface == Components.TOPLEVEL_FULLSCREEN_746 && event.buttonId == 42)) {
                setAttribute(player, GameAttributes.TUTORIAL_STAGE, 11)
                TutorialStage.load(player, 11)
            }

            21 -> if ((event.iface == Components.TOPLEVEL_548 && event.buttonId == 26) || (event.iface == Components.TOPLEVEL_FULLSCREEN_746 && event.buttonId == 54)) {
                setAttribute(player, GameAttributes.TUTORIAL_STAGE, 22)
                TutorialStage.load(player, 22)
            }

            23 -> if ((event.iface == Components.TOPLEVEL_548 && event.buttonId == 25) || (event.iface == Components.TOPLEVEL_FULLSCREEN_746 && event.buttonId == 53)) {
                setAttribute(player, GameAttributes.TUTORIAL_STAGE, 24)
                TutorialStage.load(player, 24)
            }

            24 -> if (event.iface == Components.EMOTES_464) {
                setAttribute(player, GameAttributes.TUTORIAL_STAGE, 25)
                TutorialStage.load(player, 25)
            }

            25 -> if (event.iface == Components.OPTIONS_261 && event.buttonId == 3 || event.iface == Components.TOPSTAT_RUN_750 && event.buttonId == 1) {
                setAttribute(player, GameAttributes.TUTORIAL_STAGE, 26)
                TutorialStage.load(player, 26)
            }

            27 -> if ((event.iface == Components.TOPLEVEL_548 && event.buttonId == 40) || (event.iface == Components.TOPLEVEL_FULLSCREEN_746 && event.buttonId == 43)) {
                setAttribute(player, GameAttributes.TUTORIAL_STAGE, 28)
                TutorialStage.load(player, 28)
            }

            45 -> if ((event.iface == Components.TOPLEVEL_548 && event.buttonId == 42) || (event.iface == Components.TOPLEVEL_FULLSCREEN_746 && event.buttonId == 45)) {
                setAttribute(player, GameAttributes.TUTORIAL_STAGE, 46)
                TutorialStage.load(player, 46)
            }

            49 -> if ((event.iface == Components.TOPLEVEL_548 && event.buttonId == 38) || (event.iface == Components.TOPLEVEL_FULLSCREEN_746 && event.buttonId == 41)) {
                setAttribute(player, GameAttributes.TUTORIAL_STAGE, 50)
                TutorialStage.load(player, 50)
            }

            61 -> if ((event.iface == Components.TOPLEVEL_548 && event.buttonId == 43) || (event.iface == Components.TOPLEVEL_FULLSCREEN_746 && event.buttonId == 46)) {
                setAttribute(player, GameAttributes.TUTORIAL_STAGE, 62)
                TutorialStage.load(player, 62)
            }

            63 -> if ((event.iface == Components.TOPLEVEL_548 && event.buttonId == 21) || (event.iface == Components.TOPLEVEL_FULLSCREEN_746 && event.buttonId == 49)) {
                setAttribute(player, GameAttributes.TUTORIAL_STAGE, 64)
                TutorialStage.load(player, 64)
            }

            64 -> if ((event.iface == Components.TOPLEVEL_548 && event.buttonId == 22) || (event.iface == Components.TOPLEVEL_FULLSCREEN_746 && event.buttonId == 50)) {
                setAttribute(player, GameAttributes.TUTORIAL_STAGE, 65)
                TutorialStage.load(player, 65)
            }

            68 -> if ((event.iface == Components.TOPLEVEL_548 && event.buttonId == 44) || (event.iface == Components.TOPLEVEL_FULLSCREEN_746 && event.buttonId == 47)) {
                setAttribute(player, GameAttributes.TUTORIAL_STAGE, 69)
                TutorialStage.load(player, 69)
            }
        }
    }
}

object TutorialInteractionReceiver : EventHook<InteractionEvent> {
    override fun process(entity: Entity, event: InteractionEvent) {
        if (entity !is Player) return
        val player = entity.asPlayer()
        when (getAttribute(player, GameAttributes.TUTORIAL_STAGE, 0)) {
            6 -> if ((WoodcuttingNode.forId(event.target.id)?.identifier ?: -1) == 1.toByte()) {
                setAttribute(player, GameAttributes.TUTORIAL_STAGE, 7)
                TutorialStage.load(player, 7)
            }

            12 -> if (FishingSpot.forId(event.target.id) != null) {
                setAttribute(player, GameAttributes.TUTORIAL_STAGE, 13)
                TutorialStage.load(player, 13)
            }

            31 -> if (MiningNode.forId(event.target.id)?.identifier?.equals(2.toByte()) == true && event.option == "prospect") {
                setAttribute(player, GameAttributes.TUTORIAL_STAGE, 32)
                TutorialStage.load(player, 32)
            }

            33 -> if (MiningNode.forId(event.target.id)?.identifier?.equals(1.toByte()) == true && event.option == "prospect") {
                setAttribute(player, GameAttributes.TUTORIAL_STAGE, 34)
                TutorialStage.load(player, 34)
            }

            35 -> if (MiningNode.forId(event.target.id)?.identifier?.equals(2.toByte()) == true && event.option == "mine") {
                setAttribute(player, GameAttributes.TUTORIAL_STAGE, 36)
                TutorialStage.load(player, 36)
            }

            46 -> if (event.target.id == Items.BRONZE_DAGGER_1205 && event.option == "equip") {
                setAttribute(player, GameAttributes.TUTORIAL_STAGE, 47)
                TutorialStage.load(player, 47)
            }

            48 -> {
                if (event.target.id == Items.BRONZE_SWORD_1277 && event.option == "equip") {
                    setAttribute(player, "/save:tutorial:sword", true)
                } else if (event.target.id == Items.WOODEN_SHIELD_1171 && event.option == "equip") {
                    setAttribute(player, "/save:tutorial:shield", true)
                }
                if (getAttribute(player, "tutorial:shield", false) && getAttribute(player, "tutorial:sword", false)) {
                    setAttribute(player, GameAttributes.TUTORIAL_STAGE, 49)
                    TutorialStage.load(player, 49)
                }
            }

            51 -> if (event.target.id == NPCs.GIANT_RAT_950 && event.option == "attack") {
                setAttribute(player, GameAttributes.TUTORIAL_STAGE, 52)
                TutorialStage.load(player, 52)
            }

            56 -> {}
        }
    }
}

object TutorialResourceReceiver : EventHook<ResourceProducedEvent> {
    override fun process(entity: Entity, event: ResourceProducedEvent) {
        if (entity !is Player) return
        val player = entity.asPlayer()
        when (getAttribute(player, GameAttributes.TUTORIAL_STAGE, 0)) {
            7 -> if (event.itemId == Items.LOGS_1511) {
                setAttribute(player, GameAttributes.TUTORIAL_STAGE, 8)
                TutorialStage.load(player, 8)
            }

            13 -> if (event.itemId == Items.RAW_SHRIMPS_317) {
                setAttribute(player, GameAttributes.TUTORIAL_STAGE, 14)
                TutorialStage.load(player, 14)
            }

            14, 15 -> if (event.itemId == Items.BURNT_SHRIMP_7954) {
                setAttribute(player, GameAttributes.TUTORIAL_STAGE, 15)
                TutorialStage.load(player, 15)
            } else if (event.itemId == Items.SHRIMPS_315) {
                setAttribute(player, GameAttributes.TUTORIAL_STAGE, 16)
                TutorialStage.load(player, 16)
            }

            19 -> if (event.itemId == Items.BREAD_DOUGH_2307) {
                setAttribute(player, GameAttributes.TUTORIAL_STAGE, 20)
                TutorialStage.load(player, 20)
            }

            20 -> if (event.itemId == Items.BREAD_2309 || event.itemId == Items.BURNT_BREAD_2311) {
                setAttribute(player, GameAttributes.TUTORIAL_STAGE, 21)
                TutorialStage.load(player, 21)
            }

            36 -> if (event.itemId == Items.TIN_ORE_438) {
                setAttribute(player, GameAttributes.TUTORIAL_STAGE, 37)
                TutorialStage.load(player, 37)
            }

            37 -> if (event.itemId == Items.COPPER_ORE_436) {
                setAttribute(player, GameAttributes.TUTORIAL_STAGE, 38)
                TutorialStage.load(player, 38)
            }

            38 -> if (event.itemId == Items.BRONZE_BAR_2349) {
                setAttribute(player, GameAttributes.TUTORIAL_STAGE, 40)
                TutorialStage.load(player, 40)
            }

            42 -> if (event.itemId == Items.BRONZE_DAGGER_1205) {
                setAttribute(player, GameAttributes.TUTORIAL_STAGE, 43)
                TutorialStage.load(player, 43)
            }
        }
    }
}

object TutorialFireReceiver : EventHook<LitFireEvent> {
    override fun process(entity: Entity, event: LitFireEvent) {
        if (entity !is Player) return
        val player = entity.asPlayer()
        when (getAttribute(player, GameAttributes.TUTORIAL_STAGE, 0)) {
            9 -> {
                setAttribute(player, GameAttributes.TUTORIAL_STAGE, 10)
                TutorialStage.load(player, 10)
            }
        }
    }
}

object TutorialUseWithReceiver : EventHook<UseWithEvent> {
    override fun process(entity: Entity, event: UseWithEvent) {
        if (entity !is Player) return
        val player = entity.asPlayer()
        when (getAttribute(player, GameAttributes.TUTORIAL_STAGE, 0)) {
            8 -> if (event.used == Items.TINDERBOX_590 && event.with == Items.LOGS_1511) {
                setAttribute(player, GameAttributes.TUTORIAL_STAGE, 9)
                TutorialStage.load(player, 9)
            }

            41 -> if (event.used == Items.BRONZE_BAR_2349 && event.with == Scenery.ANVIL_2783) {
                setAttribute(player, GameAttributes.TUTORIAL_STAGE, 42)
                TutorialStage.load(player, 42)
            }
        }
    }
}

object TutorialKillReceiver : EventHook<NPCKillEvent> {
    override fun process(entity: Entity, event: NPCKillEvent) {
        if (entity !is Player) return
        val player = entity.asPlayer()
        when (getAttribute(player, GameAttributes.TUTORIAL_STAGE, 0)) {
            52 -> if (event.npc.id == NPCs.GIANT_RAT_950) {
                setAttribute(player, GameAttributes.TUTORIAL_STAGE, 53)
                TutorialStage.load(player, 53)
            }

            54 -> if (event.npc.id == NPCs.GIANT_RAT_950) {
                setAttribute(player, GameAttributes.TUTORIAL_STAGE, 55)
                TutorialStage.load(player, 55)
            }
        }
    }
}

object TutorialCastReceiver : EventHook<SpellCastEvent> {
    override fun process(entity: Entity, event: SpellCastEvent) {
        if (entity !is Player) return
        val player = entity.asPlayer()
        when (getAttribute(player, GameAttributes.TUTORIAL_STAGE, 0)) {
            70 -> if (event.spellId == 1 && event.spellBook == SpellBookManager.SpellBook.MODERN && event.target?.id == NPCs.CHICKEN_951) {
                setAttribute(player, GameAttributes.TUTORIAL_STAGE, 71)
                TutorialStage.load(player, 71)
            }
        }
    }
}
