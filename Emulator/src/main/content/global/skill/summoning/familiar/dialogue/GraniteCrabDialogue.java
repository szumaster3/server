package content.global.skill.summoning.familiar.dialogue;

import content.global.skill.gathering.fishing.Fish;
import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs.consts.NPCs;

import static core.api.ContentAPIKt.anyInInventory;
import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Granite crab dialogue.
 */
@Initializable
public class GraniteCrabDialogue extends Dialogue {

    private static final int[] fishes = Fish.getFishMap().values().stream().mapToInt(fish -> fish.getId()).toArray();

    @Override
    public Dialogue newInstance(Player player) {
        return new GraniteCrabDialogue(player);
    }

    /**
     * Instantiates a new Granite crab dialogue.
     */
    public GraniteCrabDialogue() {}

    /**
     * Instantiates a new Granite crab dialogue.
     *
     * @param player the player
     */
    public GraniteCrabDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        if (anyInInventory(player, fishes)) {
            npcl(FaceAnim.CHILD_NORMAL, "That is not a rock fish...");
            stage = END_DIALOGUE;
            return true;
        }

        switch ((int) (Math.random() * 4)) {
            case 0:
                npcl(FaceAnim.CHILD_NORMAL, "Can I have some fish?");
                stage = 0;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "Rock fish now, please?");
                stage = 5;
                break;
            case 2:
                npcl(FaceAnim.CHILD_NORMAL, "When can we go fishing? I want rock fish.");
                stage = 6;
                break;
            case 3:
                npcl(FaceAnim.CHILD_NORMAL, "I'm stealthy!");
                stage = 7;
                break;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                playerl(FaceAnim.FRIENDLY, "No, I have to cook these for later.");
                stage++;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "Free fish, please?");
                stage++;
                break;
            case 2:
                playerl(FaceAnim.FRIENDLY, "No...I already told you you can't.");
                stage++;
                break;
            case 3:
                npcl(FaceAnim.CHILD_NORMAL, "Can it be fish time soon?");
                stage++;
                break;
            case 4:
                playerl(FaceAnim.FRIENDLY, "Great...I get stuck with the only granite crab in existence that can't take no for an answer...");
                stage = END_DIALOGUE;
                break;

            case 5:
                playerl(FaceAnim.FRIENDLY, "Not right now. I don't have any rock fish.");
                stage = END_DIALOGUE;
                break;

            case 6:
                playerl(FaceAnim.FRIENDLY, "When I need some fish. It's not that hard to work out, right?");
                stage = END_DIALOGUE;
                break;

            case 7:
                playerl(FaceAnim.FRIENDLY, "Errr... of course you are.");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{NPCs.GRANITE_CRAB_6796, NPCs.GRANITE_CRAB_6797};
    }
}
