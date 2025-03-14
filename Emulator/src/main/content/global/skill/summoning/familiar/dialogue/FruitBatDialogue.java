package content.global.skill.summoning.familiar.dialogue;

import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs.consts.Items;
import org.rs.consts.NPCs;

import static core.api.ContentAPIKt.*;
import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Fruit bat dialogue.
 */
@Initializable
public class FruitBatDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new FruitBatDialogue(player);
    }

    /**
     * Instantiates a new Fruit bat dialogue.
     */
    public FruitBatDialogue() {}

    /**
     * Instantiates a new Fruit bat dialogue.
     *
     * @param player the player
     */
    public FruitBatDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        if (amountInInventory(player, Items.PAPAYA_FRUIT_5972) >= 5) {
            npc(FaceAnim.CHILD_NORMAL, "Squeeksqueekasqueeksquee?", "(Can I have a papaya?)");
            stage = 0;
            return true;
        }
        if (inZone(player, "karamja")) {
            npc(FaceAnim.CHILD_NORMAL, "Squeesqueak squeak!", "(I smell fruit!)");
            stage = 9;
            return true;
        }
        switch ((int) (Math.random() * 4)) {
            case 0:
                npc(FaceAnim.CHILD_NORMAL, "Squeekasqueek squeek?", "(How much longer do you want me for?)");
                sendChat(npc, "Squeeeak!", 0);
                stage = 3;
                break;
            case 1:
                npc(FaceAnim.CHILD_NORMAL, "Squeakdqueesqueak.", "(This place is fun!)");
                stage = 4;
                break;
            case 2:
                npc(FaceAnim.CHILD_NORMAL, "Squeeksqueekasqueek?", "(Where are we going?)");
                sendChat(npc, "Squeee!", 0);
                stage = 5;
                break;
            case 3:
                npc(FaceAnim.CHILD_NORMAL, "Squeeksqueekasqueek squee?", "(Can you smell lemons?)");
                stage = 6;
                break;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                playerl(FaceAnim.FRIENDLY, "No, I have a very specific plan for them.");
                stage++;
                break;
            case 1:
                npc(FaceAnim.CHILD_NORMAL, "Squeek?", "(What?)");
                stage++;
                break;
            case 2:
                playerl(FaceAnim.FRIENDLY, "I was just going to grate it over some other vegetables and eat it. Yum.");
                stage = END_DIALOGUE;
                break;
            case 3:
                playerl(FaceAnim.FRIENDLY, "I don't really know at the moment, it all depends what I want to do today.");
                stage = END_DIALOGUE;
                break;
            case 4:
                playerl(FaceAnim.FRIENDLY, "Glad you think so!");
                stage = END_DIALOGUE;
                break;
            case 5:
                playerl(FaceAnim.FRIENDLY, "Oh, we're likely to go to a lot of places today.");
                stage = END_DIALOGUE;
                break;
            case 6:
                npc(FaceAnim.CHILD_NORMAL, "Squeeksqueekasqueek squee?", "(Can you smell lemons?)");
                stage++;
                break;
            case 7:
                playerl(FaceAnim.HALF_ASKING, "No, why do you ask?");
                stage++;
                break;
            case 8:
                npc(FaceAnim.CHILD_NORMAL, "Squeaksqueak squeaksqueesqueak.", "(Must just be thinking about them.)");
                stage = END_DIALOGUE;
                break;
            case 9:
                playerl(FaceAnim.FRIENDLY, "Well, there is likely to be some up in those trees, if you go looking for it.");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{NPCs.FRUIT_BAT_6817};
    }
}
