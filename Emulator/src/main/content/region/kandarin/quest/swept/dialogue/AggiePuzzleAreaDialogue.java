package content.region.kandarin.quest.swept.dialogue;

import content.data.GameAttributes;
import content.region.kandarin.quest.swept.handlers.SweptUtils;
import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs.consts.NPCs;
import org.rs.consts.Quests;

import static core.api.ContentAPIKt.setAttribute;
import static core.api.quest.QuestAPIKt.getQuestStage;
import static core.api.quest.QuestAPIKt.setQuestStage;
import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Aggie puzzle area dialogue.
 */
@Initializable
public class AggiePuzzleAreaDialogue extends Dialogue {

    /**
     * Instantiates a new Aggie puzzle area dialogue.
     */
    public AggiePuzzleAreaDialogue() {
    }

    /**
     * Instantiates a new Aggie puzzle area dialogue.
     *
     * @param player the player
     */
    public AggiePuzzleAreaDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        if (getQuestStage(player, Quests.SWEPT_AWAY) == 3) {
            player("Wow, that was impressive.");
            stage = 12;
        } else {
            player("Woah! Where are we and what are we doing here?");
            stage = 0;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                npc("Oh, this is just a little place that some of us witches use", 
                    "on occasion. It's rather convenient for the occasional", 
                    "ritual or spell.");
                stage++;
                break;
            case 1:
                npc("Not only is it infused with a bit of magical peace, but", 
                    "it's out of the way enough that we don't get a lot of", 
                    "unnecessary interruptions.");
                stage++;
                break;
            case 2:
                player("Ah, right. Which leaves the question of what we're doing", "here.");
                stage++;
                break;
            case 3:
                npc("You want that broom of yours enchanted, right?");
                stage++;
                break;
            case 4:
                player("Right.");
                stage++;
                break;
            case 5:
                npc("Well the best way to enchant that hunk of dead wood", 
                    "is to harness the power latent in this magical symbol", 
                    "here.");
                stage++;
                break;
            case 6:
                npc("Do you see that pattern of 16 lines thrown out of sand", "on the ground?");
                stage++;
                break;
            case 7:
                player("How could I miss it?");
                stage++;
                break;
            case 8:
                npc("In order to enchant the broom, you need to sweep", 
                    "away 4 lines of those 16 lines, such that you leave only 4", 
                    "small triangles on the ground - and nothing else.");
                stage++;
                break;
            case 9:
                npc("If you run into any trouble, let me know and I'll", 
                    "reconfigure the original pattern for you. I can also", 
                    "teleport you back to Draynor when you're ready to", 
                    "leave.");
                stage++;
                break;
            case 10:
                player("Okay, thanks. I'll give it a try.");
                end();
                setAttribute(player, GameAttributes.QUEST_SWEPT_AWAY_LINE, 0);
                setQuestStage(player, Quests.SWEPT_AWAY, 2);
                stage = END_DIALOGUE;
                break;
            case 12:
                npc("Yes, there is a lot of power in these types of magical", "symbols.");
                stage++;
                break;
            case 13:
                options("Is there anything else that needs to be done here?", 
                        "Where are we?", 
                        "I'd like to go back to Draynor, please.");
                stage++;
                break;
            case 14:
                switch (buttonId) {
                    case 1:
                        player("Is there anything else that needs to be done here?");
                        stage = 20;
                        break;
                    case 2:
                        player("Where are we?");
                        stage = 21;
                        break;
                    case 3:
                        player("I'd like to go back to Draynor, please.");
                        stage++;
                        break;
                }
                break;
            case 15:
                npc("Sure thing! Just hold on to your hat and you'll be back", 
                    "in Draynor before you can wiggle your nose.");
                stage++;
                break;
            case 16:
                end();
                SweptUtils.teleport(player);
                break;
            case 20:
                npc(FaceAnim.HALF_GUILTY, 
                    "Not everything you were supposed to do has been done. [Transcript missing]");
                stage = 13;
                break;
            case 21:
                npc("Oh, this is just a little place that some of us witches use", 
                    "on occasion. It's rather convenient for the occasional", 
                    "ritual or spell.");
                stage++;
                break;
            case 22:
                npc("Not only is it infused with a bit of magical peace, but", 
                    "it's out of the way enough that we don't get a lot of", 
                    "unnecessary interruptions.");
                stage = 13;
                break;
        }
        return true;
    }

    @Override
    public Dialogue newInstance(Player player) {
        return new AggiePuzzleAreaDialogue(player);
    }

    @Override
    public int[] getIds() {
        return new int[]{NPCs.AGGIE_8207};
    }
}
