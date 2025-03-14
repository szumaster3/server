package content.global.skill.summoning.familiar.dialogue.spirit;

import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs.consts.NPCs;

import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Spirit mosquito dialogue.
 */
@Initializable
public class SpiritMosquitoDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new SpiritMosquitoDialogue(player);
    }

    /**
     * Instantiates a new Spirit mosquito dialogue.
     */
    public SpiritMosquitoDialogue() {}

    /**
     * Instantiates a new Spirit mosquito dialogue.
     *
     * @param player the player
     */
    public SpiritMosquitoDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        switch ((int) (Math.random() * 4)) {
            case 0:
                npcl(FaceAnim.CHILD_NORMAL, "You have lovely ankles.");
                stage = 0;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "How about that local sports team?");
                stage = 4;
                break;
            case 2:
                npcl(FaceAnim.CHILD_NORMAL, "Have you ever tasted pirate blood?");
                stage = 9;
                break;
            case 3:
                npcl(FaceAnim.CHILD_NORMAL, "I'm soooo hungry!");
                stage = 13;
                break;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                playerl(FaceAnim.FRIENDLY, "Am I meant to be pleased by that?");
                stage++;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "Thin skin. Your delicious blood is easier to get too.");
                stage++;
                break;
            case 2:
                playerl(FaceAnim.FRIENDLY, "I knew I couldn't trust you.");
                stage++;
                break;
            case 3:
                npcl(FaceAnim.CHILD_NORMAL, "Oh come on, you won't feel a thing...");
                stage = END_DIALOGUE;
                break;

            case 4:
                playerl(FaceAnim.FRIENDLY, "Which one? The gnomeball team?");
                stage++;
                break;
            case 5:
                npcl(FaceAnim.CHILD_NORMAL, "I must confess: I have no idea.");
                stage++;
                break;
            case 6:
                playerl(FaceAnim.FRIENDLY, "Why did you ask, then?");
                stage++;
                break;
            case 7:
                npcl(FaceAnim.CHILD_NORMAL, "I was just trying to be friendly.");
                stage++;
                break;
            case 8:
                playerl(FaceAnim.FRIENDLY, "Just trying to get to my veins, more like!");
                stage = END_DIALOGUE;
                break;

            case 9:
                playerl(FaceAnim.FRIENDLY, "Why would I drink pirate blood?");
                stage++;
                break;
            case 10:
                npcl(FaceAnim.CHILD_NORMAL, "How about dwarf blood?");
                stage++;
                break;
            case 11:
                playerl(FaceAnim.FRIENDLY, "I don't think you quite understand...");
                stage++;
                break;
            case 12:
                npcl(FaceAnim.CHILD_NORMAL, "Gnome blood, then?");
                stage = END_DIALOGUE;
                break;

            case 13:
                playerl(FaceAnim.FRIENDLY, "What would you like to eat?");
                stage++;
                break;
            case 14:
                npcl(FaceAnim.CHILD_NORMAL, "Well, if you're not too attached to your elbow...");
                stage++;
                break;
            case 15:
                playerl(FaceAnim.FRIENDLY, "You can't eat my elbow! You don't have teeth!");
                stage++;
                break;
            case 16:
                npcl(FaceAnim.CHILD_NORMAL, "Tell me about it. Cousin Nigel always makes fun of me. Calls me 'No-teeth'.");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[] {NPCs.SPIRIT_MOSQUITO_7331, NPCs.SPIRIT_MOSQUITO_7332};
    }
}
