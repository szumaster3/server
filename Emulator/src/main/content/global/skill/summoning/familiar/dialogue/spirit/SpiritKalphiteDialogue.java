package content.global.skill.summoning.familiar.dialogue.spirit;

import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs.consts.Items;
import org.rs.consts.NPCs;

import static core.api.ContentAPIKt.hasAnItem;
import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Spirit kalphite dialogue.
 */
@Initializable
public class SpiritKalphiteDialogue extends Dialogue {

    private static final int[] KERIS = {
        Items.KERIS_10581, Items.KERISP_10582, Items.KERISP_PLUS_10583, Items.KERISP_PLUS_PLUS_10584
    };

    @Override
    public Dialogue newInstance(Player player) {
        return new SpiritKalphiteDialogue(player);
    }

    /**
     * Instantiates a new Spirit kalphite dialogue.
     */
    public SpiritKalphiteDialogue() {}

    /**
     * Instantiates a new Spirit kalphite dialogue.
     *
     * @param player the player
     */
    public SpiritKalphiteDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        boolean hasKeris = hasAnItem(player, KERIS).getContainer() == player.getInventory();
        if (hasKeris) {
            playerl(FaceAnim.ASKING, "How dare I what?");
            stage = 0;
            return true;
        }
        switch ((int) (Math.random() * 4)) {
            case 0:
                npcl(FaceAnim.CHILD_NORMAL, "This activity is not optimal for us.");
                stage = 4;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "We are growing infuriated. What is our goal?");
                stage = 6;
                break;
            case 2:
                npcl(FaceAnim.CHILD_NORMAL, "We find this to be wasteful of our time.");
                stage = 9;
                break;
            case 3:
                npcl(FaceAnim.CHILD_NORMAL, "We grow tired of your antics, biped.");
                stage = 11;
                break;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                npcl(FaceAnim.CHILD_NORMAL, "That weapon offends us!");
                stage++;
                break;
            case 1:
                playerl(FaceAnim.HALF_ASKING, "What weapon?");
                stage++;
                break;
            case 2:
                playerl(FaceAnim.FRIENDLY, "Oh...");
                stage++;
                break;
            case 3:
                playerl(FaceAnim.FRIENDLY, "Awkward.");
                stage = END_DIALOGUE;
                break;

            case 4:
                playerl(FaceAnim.FRIENDLY, "Well, you'll just have to put up with it for now.");
                stage++;
                break;
            case 5:
                npcl(FaceAnim.CHILD_NORMAL, "We would not have to 'put up' with this in the hive.");
                stage = END_DIALOGUE;
                break;

            case 6:
                playerl(FaceAnim.FRIENDLY, "Well, I haven't quite decided yet.");
                stage++;
                break;
            case 7:
                npcl(FaceAnim.CHILD_NORMAL, "There is no indecision in the hive.");
                stage++;
                break;
            case 8:
                playerl(FaceAnim.FRIENDLY, "Or a sense of humour or patience, it seems.");
                stage = END_DIALOGUE;
                break;

            case 9:
                playerl(FaceAnim.FRIENDLY, "Maybe I find you wasteful...");
                stage++;
                break;
            case 10:
                npcl(FaceAnim.CHILD_NORMAL, "We would not face this form of abuse in the hive.");
                stage = END_DIALOGUE;
                break;

            case 11:
                playerl(FaceAnim.FRIENDLY, "What antics? I'm just getting on with my day.");
                stage++;
                break;
            case 12:
                npcl(FaceAnim.CHILD_NORMAL, "In an inefficient way. In the hive, you would be replaced.");
                stage++;
                break;

            case 13:
                playerl(FaceAnim.FRIENDLY, "In the hive this, in the hive that...");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[] {NPCs.SPIRIT_KALPHITE_6994, NPCs.SPIRIT_KALPHITE_6995};
    }
}
