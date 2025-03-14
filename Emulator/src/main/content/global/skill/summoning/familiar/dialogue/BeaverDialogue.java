package content.global.skill.summoning.familiar.dialogue;

import content.global.skill.firemaking.Log;
import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs.consts.NPCs;

import java.util.Random;

import static core.api.ContentAPIKt.anyInInventory;
import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Beaver dialogue.
 */
@Initializable
public class BeaverDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new BeaverDialogue(player);
    }

    /**
     * Instantiates a new Beaver dialogue.
     */
    public BeaverDialogue() {}

    /**
     * Instantiates a new Beaver dialogue.
     *
     * @param player the player
     */
    public BeaverDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        if (anyInInventory(player, logs)) {
            npcl(FaceAnim.CHILD_NORMAL, "'Ere, you 'ave ze logs, now form zem into a mighty dam!");
            stage = 0;
            return true;
        }

        Random rand = new Random();
        switch (rand.nextInt(4)) {
            case 0:
                npcl(FaceAnim.CHILD_NORMAL, "Vot are you doing 'ere when we could be logging and building mighty dams, alors?");
                stage = 2;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "Pardonnez-moi - you call yourself a lumberjack?");
                stage = 5;
                break;
            case 2:
                npcl(FaceAnim.CHILD_NORMAL, "Paul Bunyan 'as nothing on moi!");
                stage = 7;
                break;
            case 3:
                npcl(FaceAnim.CHILD_NORMAL, "Zis is a fine day make some lumber.");
                stage = 10;
                break;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                playerl(FaceAnim.FRIENDLY, "Well, I was thinking of burning, selling, or fletching them.");
                stage++;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "Sacre bleu! Such a waste.");
                stage = END_DIALOGUE;
                break;
            case 2:
                playerl(FaceAnim.HALF_ASKING, "Why would I want to build a dam again?");
                stage++;
                break;
            case 3:
                npcl(FaceAnim.CHILD_NORMAL, "Why wouldn't you want to build a dam again?");
                stage++;
                break;
            case 4:
                playerl(FaceAnim.FRIENDLY, "I can't argue with that logic.");
                stage = END_DIALOGUE;
                break;
            case 5:
                playerl(FaceAnim.FRIENDLY, "No");
                stage++;
                break;
            case 6:
                npcl(FaceAnim.CHILD_NORMAL, "Carry on zen.");
                stage = END_DIALOGUE;
                break;
            case 7:
                playerl(FaceAnim.FRIENDLY, "Except several feet in height, a better beard, and opposable thumbs.");
                stage++;
                break;
            case 8:
                npcl(FaceAnim.CHILD_NORMAL, "What was zat?");
                stage++;
                break;
            case 9:
                playerl(FaceAnim.FRIENDLY, "Nothing.");
                stage = END_DIALOGUE;
                break;
            case 10:
                playerl(FaceAnim.FRIENDLY, "That it is!");
                stage++;
                break;
            case 11:
                npcl(FaceAnim.CHILD_NORMAL, "So why are you talking to moi? Get chopping!");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{NPCs.BEAVER_6808};
    }

    private static final int[] logs;

    static {
        logs = new int[Log.values().length];
        for (int i = 0; i < Log.values().length; i++) {
            logs[i] = Log.values()[i].getLogId();
        }
    }
}
