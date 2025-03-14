package core.game.system.command.sets

import content.region.kandarin.quest.arena.cutscene.JeremyRescueCutscene
import content.region.kandarin.quest.phoenix.handlers.PhoenixLairListener.Companion.weavingRibbons
import content.region.kandarin.quest.phoenix.handlers.WoundedPhoenixCutscene
import content.region.kandarin.quest.phoenix.handlers.allTwigs
import core.api.Container
import core.api.addItem
import core.api.quest.finishQuest
import core.api.sendString
import core.game.component.Component
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.QuestRepository
import core.game.system.command.Privilege
import core.game.world.repository.Repository
import core.plugin.Initializable
import org.rs.consts.Quests

@Initializable
class QuestCommandSet : CommandSet(Privilege.ADMIN) {
    override fun defineCommands() {
        define(
            name = "phoenix",
            privilege = Privilege.ADMIN,
            usage = "::phoenix",
            description = "In Pyre Need - Wounded Phoenix (Cutscene)",
        ) { player, _ ->
            WoundedPhoenixCutscene(player).start()
        }

        define(
            name = "twigs",
            privilege = Privilege.ADMIN,
            usage = "::twigs",
            description = "In Pyre Need - Twig items",
        ) { player, _ ->
            for (i in allTwigs) {
                addItem(player, i, 1, Container.INVENTORY)
            }
        }

        define(
            name = "ribbons",
            privilege = Privilege.ADMIN,
            usage = "::ribbons",
            description = "In Pyre Need - Ribbon items",
        ) { player, _ ->
            for (i in weavingRibbons) {
                addItem(player, i, 1, Container.INVENTORY)
            }
        }

        define(
            name = "balloon",
            privilege = Privilege.ADMIN,
            usage = "",
            description = "Toggle balloon travel.",
        ) { player, _ ->
            finishQuest(player, Quests.ENLIGHTENED_JOURNEY)
        }

        define(name = "allquest", Privilege.ADMIN) { player, _ ->
            for (quest in QuestRepository.getQuests().values) {
                quest.finish(player)
            }
        }

        define(name = "firstquest", Privilege.ADMIN) { player, _ ->
            JeremyRescueCutscene(player).start()
        }


        define(name = "quest", Privilege.ADMIN) { player, args ->
            if (args.size < 3) {
                val lookupP =
                    if (args.size == 1) {
                        player
                    } else if (Repository.getPlayerByName(args[1]) != null) {
                        Repository.getPlayerByName(args[1]) ?: return@define
                    } else {
                        reject(player, "ERROR: Username not found. Usage: ::quest <username>")
                        return@define
                    }
                sendQuestsDebug(player, lookupP)
            } else {
                reject(player, "Usage: ::quest || ::quest <username>")
            }
        }

        define(name = "setqueststage", Privilege.ADMIN) { player, args ->
            if (args.size < 3) {
                reject(player, "You must specify the index# of a quest, and a stage number!")
            }
            val quest = args[1].toIntOrNull() ?: reject(player, "INVALID QUEST")
            val stage = args[2].toIntOrNull() ?: reject(player, "INVALID STAGE")
            val questObject = player.questRepository.forIndex(quest as Int)
            if (questObject == null) {
                reject(player, "$quest did not match anything in the quest repository!")
            } else {
                player.questRepository.setStageNonmonotonic(questObject, stage as Int)
                if (stage == 0) {
                    questObject.reset(player)
                }
                questObject.updateVarps(player)
                notify(player, "<col=209dff>Setting " + questObject.name + " to stage $stage</col>")
            }
        }

        define(name = "quests", privilege = Privilege.ADMIN) { player, _ ->
            sendQuests(player)
        }
    }

    private fun sendQuests(player: Player?) {
        player!!.interfaceManager.open(Component(275))
        for (i in 0..310) {
            sendString(player, "", 275, i)
        }
        var lineId = 11
        sendString(player, "<col=ecf0f1>" + "Available Quests" + "</col>", 275, 2)
        for (q in QuestRepository.getQuests().toSortedMap().values) {
            // Add a space to beginning and end of string for the strikethrough
            sendString(
                player,
                "<col=ecf0f1>" + (if (q.isCompleted(player)) "<str> " else "") + q.name + " ",
                275,
                lineId++,
            )
        }
    }

    private fun sendQuestsDebug(
        admin: Player?,
        lookupUser: Player?,
    ) {
        admin!!.interfaceManager.open(Component(275))
        for (i in 0..310) {
            sendString(admin, "", 275, i)
        }
        var lineId = 11
        sendString(admin, "<col=ecf0f1>${lookupUser!!.username}'s Quest Debug</col>", 275, 2)
        for (q in QuestRepository.getQuests().values) {
            // Add a space to beginning and end of string for the strikethrough
            val stage = lookupUser.questRepository.getStage(q)
            val statusColor =
                when {
                    stage >= 100 -> "80ff00"
                    stage in 1..99 -> "ff8400"
                    else -> "ff0000"
                }
            sendString(admin, "<col=ecf0f1>${q.name}</col>", 275, lineId++)
            sendString(
                admin,
                "<col=ecf0f1>Index: </col><col=ff1f1f><shad=2>${q.index}</shad></col> | <col=ecf0f1>Stage:</col> <col=$statusColor><shad=2>${
                    lookupUser.questRepository.getStage(q)
                }</shad></col>",
                275,
                lineId++,
            )
            sendString(admin, "<str>          ", 275, lineId++)
        }
    }
}
