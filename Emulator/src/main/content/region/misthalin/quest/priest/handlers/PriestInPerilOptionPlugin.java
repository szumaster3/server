package content.region.misthalin.quest.priest.handlers;

import org.rs.consts.NPCs;
import org.rs.consts.Quests;
import core.cache.def.impl.NPCDefinition;
import core.cache.def.impl.SceneryDefinition;
import core.game.component.Component;
import core.game.global.action.DoorActionHandler;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.quest.Quest;
import core.game.node.scenery.Scenery;
import core.game.world.map.Location;
import core.plugin.Initializable;
import core.plugin.Plugin;

import static core.api.ContentAPIKt.*;
import static core.api.quest.QuestAPIKt.getQuestStage;

/**
 * The type Priest in peril option plugin.
 */
@Initializable
public class PriestInPerilOptionPlugin extends OptionHandler {

    @Override
    public Plugin<java.lang.Object> newInstance(java.lang.Object arg) throws Throwable {
        SceneryDefinition.forId(org.rs.consts.Scenery.MONUMENT_3496).getHandlers().put("option:open", this);
        SceneryDefinition.forId(org.rs.consts.Scenery.GATE_3445).getHandlers().put("option:open", this);
        SceneryDefinition.forId(org.rs.consts.Scenery.LARGE_DOOR_30707).getHandlers().put("option:open", this);
        SceneryDefinition.forId(org.rs.consts.Scenery.LARGE_DOOR_30707).getHandlers().put("option:knock-at", this);
        SceneryDefinition.forId(org.rs.consts.Scenery.LARGE_DOOR_30708).getHandlers().put("option:open", this);
        SceneryDefinition.forId(org.rs.consts.Scenery.LARGE_DOOR_30708).getHandlers().put("option:knock-at", this);
        SceneryDefinition.forId(org.rs.consts.Scenery.CHURCH_PEW_30729).getHandlers().put("option:open", this);
        SceneryDefinition.forId(org.rs.consts.Scenery.CELL_DOOR_3463).getHandlers().put("option:open", this);
        SceneryDefinition.forId(org.rs.consts.Scenery.CELL_DOOR_3463).getHandlers().put("option:talk-through", this);
        SceneryDefinition.forId(org.rs.consts.Scenery.WELL_3485).getHandlers().put("option:search", this);
        SceneryDefinition.forId(org.rs.consts.Scenery.MONUMENT_3496).getHandlers().put("option:study", this);
        SceneryDefinition.forId(org.rs.consts.Scenery.MONUMENT_3496).getHandlers().put("option:take-from", this);
        SceneryDefinition.forId(org.rs.consts.Scenery.MONUMENT_3498).getHandlers().put("option:study", this);
        SceneryDefinition.forId(org.rs.consts.Scenery.MONUMENT_3498).getHandlers().put("option:take-from", this);
        SceneryDefinition.forId(org.rs.consts.Scenery.MONUMENT_3495).getHandlers().put("option:study", this);
        SceneryDefinition.forId(org.rs.consts.Scenery.MONUMENT_3495).getHandlers().put("option:take-from", this);
        SceneryDefinition.forId(org.rs.consts.Scenery.MONUMENT_3497).getHandlers().put("option:study", this);
        SceneryDefinition.forId(org.rs.consts.Scenery.MONUMENT_3497).getHandlers().put("option:take-from", this);
        SceneryDefinition.forId(org.rs.consts.Scenery.MONUMENT_3494).getHandlers().put("option:study", this);
        SceneryDefinition.forId(org.rs.consts.Scenery.MONUMENT_3494).getHandlers().put("option:take-from", this);
        SceneryDefinition.forId(org.rs.consts.Scenery.MONUMENT_3499).getHandlers().put("option:study", this);
        SceneryDefinition.forId(org.rs.consts.Scenery.MONUMENT_3499).getHandlers().put("option:take-from", this);
        SceneryDefinition.forId(org.rs.consts.Scenery.MONUMENT_3493).getHandlers().put("option:study", this);
        SceneryDefinition.forId(org.rs.consts.Scenery.MONUMENT_3493).getHandlers().put("option:take-from", this);
        SceneryDefinition.forId(org.rs.consts.Scenery.HOLY_BARRIER_3443).getHandlers().put("option:pass-through", this);
        NPCDefinition.forId(NPCs.TEMPLE_GUARDIAN_7711).getHandlers().put("option:attack", this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        final Quest quest = player.getQuestRepository().getQuest(Quests.PRIEST_IN_PERIL);
        int id = node.getId();
        switch (option) {
            case "study":
                player.getInterfaceManager().open(new Component(272));
                int item = 0;
                String message = "";
                if (id == 3496) {
                    if (!player.getGameAttributes().getAttribute("priest_in_peril:hammer", false)) {
                        item = 2949;
                    } else {
                        item = 2347;
                    }
                    message = "Saradomin is the hammer that crushes evil everywhere.";
                }
                if (id == 3498) {
                    if (!player.getGameAttributes().getAttribute("priest_in_peril:needle", false)) {
                        item = 2951;
                    } else {
                        item = 1733;
                    }
                    message = "Saradomin is the needle that binds our lives together.";
                }
                if (id == 3495) {
                    if (!player.getGameAttributes().getAttribute("priest_in_peril:pot", false)) {
                        item = 2948;
                    } else {
                        item = 1931;
                    }
                    message = "Saradomin is the vessel that keeps our lives from harm.";
                }
                if (id == 3497) {
                    if (!player.getGameAttributes().getAttribute("priest_in_peril:feather", false)) {
                        item = 2950;
                    } else {
                        item = 314;
                    }
                    message = "Saradomin is the delicate touch that brushes us with love.";
                }
                if (id == 3494) {
                    if (!player.getGameAttributes().getAttribute("priest_in_peril:candle", false)) {
                        item = 2947;
                    } else {
                        item = 36;
                    }
                    player.getPacketDispatch().sendAngleOnInterface(272, 4, 512, 256, 0);
                    message = "Saradomin is the light that shines throughout our lives.";
                }
                if (id == 3499) {
                    if (!player.getGameAttributes().getAttribute("priest_in_peril:key", false)) {
                        item = 2945;
                    } else {
                        item = 2944;
                    }
                    player.getPacketDispatch().sendAngleOnInterface(272, 4, 512, 256, 0);
                    message = "Saradomin is the key that unlocks the mysteries of life.";
                }
                if (id == 3493) {
                    if (!player.getGameAttributes().getAttribute("priest_in_peril:tinderbox", false)) {
                        item = 2946;
                    } else {
                        item = 590;
                    }
                    player.getPacketDispatch().sendItemZoomOnInterface(item, 320, 272, 4);
                    player.getPacketDispatch().sendAngleOnInterface(272, 4, 512, 256, 0);
                    message = "Saradomin is the spark that lights the fire in our hearts.";
                }
                player.getPacketDispatch().sendString(message, 272, 17);
                player.getPacketDispatch().sendItemZoomOnInterface(item, 512, 272, 4);
                player.getPacketDispatch().sendAngleOnInterface(272, 4, 512, 128, 0);
                break;
            case "take-from":
                player.getImpactHandler().handleImpact(player, 2, CombatStyle.MELEE);
                player.getPacketDispatch().sendMessage("A holy power prevents you stealing from the monument!");
                break;
        }
        switch (id) {
            case 3444:
                if (quest.getStage(player) <= 13) {
                    player.getDialogueInterpreter().sendDialogues(player, null, "Hmmm... from the looks of things, it seems as though", "somebody has been trying to force this door open. It's", "still securely locked however.");
                    return true;
                }
                DoorActionHandler.handleDoor(player, (Scenery) node);
                break;
            case 3443:
                if (!player.getQuestRepository().isComplete(Quests.PRIEST_IN_PERIL)) {
                    player.getPacketDispatch().sendMessage("A magic force prevents you from passing through.");
                } else {
                    player.getProperties().setTeleportLocation(Location.create(3423, 3484, 0));
                    player.getPacketDispatch().sendMessage("You pass through the holy barrier.");
                }
                break;
            case 3485:
                player.getDialogueInterpreter().sendDialogue("You look down the well and see the filthy polluted water of the river", "Salve moving slowly along.");
                break;
            case 3463:
                switch (option) {
                    case "open":
                        if (quest.getStage(player) < 15) {
                            player.getPacketDispatch().sendMessage("The door is securely locked shut.");
                        } else {
                            DoorActionHandler.handleDoor(player, (Scenery) node);
                        }
                        break;
                    case "talk-through":
                        player.getDialogueInterpreter().open(NPCs.DREZEL_7690, NPC.create(NPCs.DREZEL_7690, player.getLocation()));
                        break;
                }
                break;
            case 30728:
                player.getDialogueInterpreter().sendDialogues(player, null, "It sounds like there's something alive inside it. I don't", "think it would be a very good idea to open it...");
                break;
            case 3445:
                if (quest.getStage(player) < 17) {
                    player.getPacketDispatch().sendMessage("The door is locked shut.");
                } else {
                    DoorActionHandler.handleDoor(player, (Scenery) node);
                }
                break;
            case 30707:
            case 30708:
                switch (option) {
                    case "open":
                        if (quest.getStage(player) > 12) {
                            DoorActionHandler.handleDoor(player, (Scenery) node);
                        } else {
                            player.getPacketDispatch().sendMessage("This door is securely locked from inside.");
                        }
                        break;
                    case "knock-at":
                        if (quest.getStage(player) == 10 || quest.getStage(player) == 12 || quest.getStage(player) == 13) {
                            player.getDialogueInterpreter().open(54584, node);
                        } else {
                            if (getQuestStage(player, Quests.PRIEST_IN_PERIL) == 0) {
                                sendMessage(player, "You knock at the door...");
                                sendMessageWithDelay(player, "but nothing interesting happens.", 2);
                            }
                        }
                        break;
                }
                break;
            case 7711:
                if (quest.getStage(player) == 10) {
                    player.getPacketDispatch().sendMessage("You have no reason to attack a helpless dog!");
                    return true;
                }
                if (quest.getStage(player) > 10) {
                    player.getProperties().getCombatPulse().attack(node);
                }
                if (quest.getStage(player) == 12) {
                    player.getPacketDispatch().sendMessage("You've already killed that dog!");
                    return true;
                }
                if (quest.getStage(player) >= 13) {
                    player.getProperties().getCombatPulse().stop();
                    player.getPacketDispatch().sendMessage("I'd better not make the King mad at me again!");
                    return true;
                }
                break;
        }
        return true;
    }

}
