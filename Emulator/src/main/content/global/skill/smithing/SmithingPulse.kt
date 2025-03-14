package content.global.skill.smithing

import core.api.quest.isQuestComplete
import core.api.sendDialogue
import core.cache.def.impl.ItemDefinition
import core.game.event.ResourceProducedEvent
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.tools.StringUtils.isPlusN
import org.rs.consts.Animations
import org.rs.consts.Quests
import java.util.*

class SmithingPulse(
    player: Player?,
    item: Item?,
    private val bar: Bars,
    private var amount: Int,
) : SkillPulse<Item?>(player, item) {
    override fun checkRequirements(): Boolean {
        if (!player.inventory.contains(bar.barType.barType, bar.smithingType.required * amount)) {
            amount = player.inventory.getAmount(Item(bar.barType.barType))
        }
        player.interfaceManager.close()
        if (player.getSkills().getLevel(Skills.SMITHING) < bar.level) {
            player.dialogueInterpreter.sendDialogue(
                "You need a Smithing level of " + bar.level + " to make a " +
                    ItemDefinition
                        .forId(
                            bar.product,
                        ).name + ".",
            )
            return false
        }
        if (!player.inventory.contains(bar.barType.barType, bar.smithingType.required)) {
            player.dialogueInterpreter.sendDialogue(
                "You don't have enough " +
                    ItemDefinition.forId(bar.barType.barType).name.lowercase(
                        Locale.getDefault(),
                    ) + "s to make a " +
                    bar.smithingType.name
                        .replace("TYPE_", "")
                        .replace("_", " ")
                        .lowercase(Locale.getDefault()) + ".",
            )
            return false
        }
        if (!player.inventory.contains(2347, 1)) {
            player.dialogueInterpreter.sendDialogue("You need a hammer to work the metal with.")
            return false
        }
        if (!isQuestComplete(player, Quests.THE_TOURIST_TRAP) && bar.smithingType == SmithingType.TYPE_DART_TIP) {
            player.dialogueInterpreter.sendDialogue("You need to complete Tourist Trap to smith dart tips.")
            return false
        }
        if (!isQuestComplete(player, Quests.DEATH_PLATEAU) && bar.smithingType == SmithingType.TYPE_CLAWS) {
            sendDialogue(player, "You need to complete Death Plateau to smith claws.")
            return false
        }
        return true
    }

    override fun animate() {
        player.animate(ANIMATION)
    }

    override fun reward(): Boolean {
        if (delay == 1) {
            delay = 4
            return false
        }
        player.lock(4)
        player.inventory.remove(Item(bar.barType.barType, bar.smithingType.required))
        val item = Item(node!!.id, bar.smithingType.productAmount)
        player.inventory.add(item)
        player.dispatch(ResourceProducedEvent(item.id, 1, player, bar.barType.barType))
        player.getSkills().addExperience(Skills.SMITHING, bar.barType.experience * bar.smithingType.required, true)
        val message = if (isPlusN(ItemDefinition.forId(bar.product).name.lowercase(Locale.getDefault()))) "an" else "a"
        player.packetDispatch.sendMessage(
            "You hammer the " +
                bar.barType.barName.lowercase(Locale.getDefault()).replace(
                    "smithing",
                    "",
                ) + "and make " + message + " " +
                ItemDefinition.forId(bar.product).name.lowercase(
                    Locale.getDefault(),
                ) + ".",
        )

        if (bar == Bars.BLURITE_CROSSBOW_LIMBS && player.location.withinDistance(Location(3000, 3145, 0), 10)) {
            player.achievementDiaryManager.finishTask(player, DiaryType.FALADOR, 1, 9)
        }

        if (bar == Bars.STEEL_LONGSWORD && player.location.withinDistance(Location.create(3112, 9688, 0))) {
            player.achievementDiaryManager.finishTask(player, DiaryType.LUMBRIDGE, 2, 0)
        }

        if (bar == Bars.ADAMANT_MEDIUM_HELM && player.location.withinDistance(Location.create(3247, 3404, 0))) {
            player.achievementDiaryManager.finishTask(player, DiaryType.VARROCK, 2, 3)
        }

        amount--
        return amount < 1
    }

    override fun message(type: Int) {
    }

    companion object {
        private val ANIMATION = Animation(Animations.SMITH_HAMMER_898)
    }
}
