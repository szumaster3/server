package content.global.skill.summoning.familiar.dialogue;

import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs.consts.Items;
import org.rs.consts.NPCs;

import static core.api.ContentAPIKt.anyInEquipment;
import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Desert wyrm dialogue.
 */
@Initializable
public class DesertWyrmDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new DesertWyrmDialogue(player);
    }

    /**
     * Instantiates a new Desert wyrm dialogue.
     */
    public DesertWyrmDialogue() {}

    /**
     * Instantiates a new Desert wyrm dialogue.
     *
     * @param player the player
     */
    public DesertWyrmDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        if (anyInEquipment(player, PICKAXE)) {
            npcl(FaceAnim.CHILD_NORMAL, "If you have that pick, why make me dig?");
            stage = 0;
            return true;
        }

        switch (new java.util.Random().nextInt(4)) {
            case 0:
                npcl(FaceAnim.CHILD_NORMAL, "This is so unsafe...I should have a hard hat for this work...");
                stage = 6;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "You can't touch me, I'm part of the union!");
                stage = 8;
                break;
            case 2:
                npcl(FaceAnim.CHILD_NORMAL, "You know, you might want to register with the union.");
                stage = 10;
                break;
            case 3:
                npcl(FaceAnim.CHILD_NORMAL, "Why are you ignoring that good ore seam, " + player.getUsername() + "?");
                stage = 12;
                break;
        }

        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                playerl(FaceAnim.FRIENDLY, "Because it's a little quicker and easier on my arms.");
                stage++;
                break;
            case 1:
                npcl(FaceAnim.CHILD_NORMAL, "I should take industrial action over this...");
                stage++;
                break;
            case 2:
                playerl(FaceAnim.FRIENDLY, "You mean you won't work for me any more?");
                stage++;
                break;
            case 3:
                npcl(FaceAnim.CHILD_NORMAL, "No. It means me and the lads feed you legs-first into some industrial machinery, maybe the Blast Furnace.");
                stage++;
                break;
            case 4:
                playerl(FaceAnim.FRIENDLY, "I'll just be over here, digging.");
                stage++;
                break;
            case 5:
                npcl(FaceAnim.CHILD_NORMAL, "That's the spirit, lad!");
                stage = END_DIALOGUE;
                break;
            case 6:
                playerl(FaceAnim.FRIENDLY, "Well, I could get you a rune helm if you like - those are pretty hard.");
                stage++;
                break;
            case 7:
                npcl(FaceAnim.CHILD_NORMAL, "Keep that up and you'll have the union on your back, " + player.getUsername() + ".");
                stage = END_DIALOGUE;
                break;
            case 8:
                playerl(FaceAnim.HALF_ASKING, "Is that some official no touching policy or something?");
                stage++;
                break;
            case 9:
                npcl(FaceAnim.CHILD_NORMAL, "You really don't get it, do you " + player.getUsername() + "?");
                stage = END_DIALOGUE;
                break;
            case 10:
                npcl(FaceAnim.CHILD_NORMAL, "I stop bugging you to join the union.");
                stage++;
                break;
            case 11:
                playerl(FaceAnim.FRIENDLY, "Ask that again later; I'll have to consider that generous proposal.");
                stage = END_DIALOGUE;
                break;
            case 12:
                playerl(FaceAnim.HALF_ASKING, "Which ore seam?");
                stage++;
                break;
            case 13:
                npcl(FaceAnim.CHILD_NORMAL, "There's a good ore seam right underneath us at this very moment.");
                stage++;
                break;
            case 14:
                playerl(FaceAnim.HALF_ASKING, "Great! How long will it take for you to get to it?");
                stage++;
                break;
            case 15:
                npcl(FaceAnim.CHILD_NORMAL, "Five years, give or take.");
                stage++;
                break;
            case 16:
                playerl(FaceAnim.FRIENDLY, "Five years!");
                stage++;
                break;
            case 17:
                npcl(FaceAnim.CHILD_NORMAL, "That's if we go opencast, mind. I could probably reach it in three if I just dug.");
                stage++;
                break;
            case 18:
                playerl(FaceAnim.FRIENDLY, "Right. I see. I think I'll skip it thanks.");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{NPCs.DESERT_WYRM_6831, NPCs.DESERT_WYRM_6832};
    }

    /**
     * The constant PICKAXE.
     */
    public static final int[] PICKAXE = {
        Items.BRONZE_PICKAXE_1265,
        Items.IRON_PICKAXE_1267,
        Items.STEEL_PICKAXE_1269,
        Items.MITHRIL_PICKAXE_1273,
        Items.ADAMANT_PICKAXE_1271,
        Items.RUNE_PICKAXE_1275,
        Items.INFERNO_ADZE_13661
    };
}
