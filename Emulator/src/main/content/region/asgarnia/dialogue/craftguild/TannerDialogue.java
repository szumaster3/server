package content.region.asgarnia.dialogue.craftguild;

import content.global.skill.crafting.Tanning;
import org.rs.consts.NPCs;
import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;

import static core.api.ContentAPIKt.inInventory;
import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Tanner dialogue.
 */
@Initializable
public class TannerDialogue extends Dialogue {

    /**
     * Instantiates a new Tanner dialogue.
     */
    public TannerDialogue() {

    }

    /**
     * Instantiates a new Tanner dialogue.
     *
     * @param player the player
     */
    public TannerDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        npcl(FaceAnim.NEUTRAL, "Greetings friend. I am a manufacturer of leather.");
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                boolean hasHides = false;
                for (Tanning tanningProduct : Tanning.values()) {
                    if (inInventory(player, tanningProduct.getItem(), 1)) {
                        hasHides = true;
                        break;
                    }
                }

                if (hasHides) {
                    npcl(FaceAnim.FRIENDLY, "I see you have brought me some hides. Would you like me to tan them for you?");
                    stage = 10;
                } else {
                    options("Can I buy some leather then?", "Leather is rather weak stuff.");
                    stage = 20;
                }
                break;

            case 10:
                options("Yes please.", "No thanks.");
                stage++;
                break;

            case 11:
                if (buttonId == 1) {
                    playerl(FaceAnim.HAPPY, "Yes please.");
                    stage = 12;
                } else {
                    playerl(FaceAnim.NEUTRAL, "No thanks.");
                    stage = 13;
                }
                break;

            case 12:
                end();
                Tanning.open(player, NPCs.TANNER_804);
                break;

            case 13:
                npcl(FaceAnim.FRIENDLY, "Very well, @g[sir,madam], as you wish.");
                stage = END_DIALOGUE;
                break;

            case 20:
                if (buttonId == 1) {
                    playerl(FaceAnim.ASKING, "Can I buy some leather?");
                    stage = 21;
                } else {
                    playerl(FaceAnim.SUSPICIOUS, "Leather is rather weak stuff.");
                    stage = 22;
                }
                break;

            case 21:
                npcl(FaceAnim.FRIENDLY, "I make leather from animal hides. Bring me some cowhides and one gold coin per hide, and I'll tan them into soft leather for you.");
                stage = END_DIALOGUE;
                break;

            case 22:
                npc(FaceAnim.NOD_YES, "Normal leather may be quite weak, but it's very cheap -",
                        "I make it from cowhides for only 1 gp per hide - and",
                        "it's so easy to craft that anyone can work with it.");
                stage++;
                break;

            case 23:
                npc(FaceAnim.HALF_THINKING, "Alternatively you could try hard leather. It's not so",
                        "easy to craft, but I only charge 3gp per cowhide to",
                        "prepare it, and it makes much studier armour.");
                stage++;
                break;

            case 24:
                npc(FaceAnim.HAPPY, "I can also tan snake hides and dragonhides, suitable for",
                        "crafting into the highest quality armour for rangers.");
                stage++;
                break;

            case 25:
                player(FaceAnim.HALF_GUILTY, "Thanks, I'll bear it in mind.");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public Dialogue newInstance(Player player) {
        return new TannerDialogue(player);
    }

    @Override
    public int[] getIds() {
        return new int[]{NPCs.TANNER_804};
    }
}
