package content.global.skill.summoning.familiar.dialogue;

import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import org.rs.consts.NPCs;

import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Forge regent dialogue.
 */
@Initializable
public class ForgeRegentDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new ForgeRegentDialogue(player);
    }

    /**
     * Instantiates a new Forge regent dialogue.
     */
    public ForgeRegentDialogue() {}

    /**
     * Instantiates a new Forge regent dialogue.
     *
     * @param player the player
     */
    public ForgeRegentDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        switch ((int) (Math.random() * 4)) {
            case 0:
                npc(FaceAnim.CHILD_NORMAL, "Crackley spit crack sizzle?", "(Can we go Smithing?)");
                stage = 0;
                break;
            case 1:
                npc(FaceAnim.CHILD_NORMAL, "Hiss.", "(I'm happy.)");
                stage = 6;
                break;
            case 2:
                npc(FaceAnim.CHILD_NORMAL, "Sizzle!", "(I like logs.)");
                stage = 19;
                break;
            case 3:
                npc(FaceAnim.CHILD_NORMAL, "Sizzle...", "(I'm bored.)");
                stage = 22;
                break;
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                playerl(FaceAnim.FRIENDLY, "Maybe.");
                stage++;
                break;
            case 1:
                npc(FaceAnim.CHILD_NORMAL, "Hiss?", "(Can we go smelt something?)");
                stage++;
                break;
            case 2:
                playerl(FaceAnim.FRIENDLY, "Maybe.");
                stage++;
                break;
            case 3:
                npc(FaceAnim.CHILD_NORMAL, "Flicker crackle sizzle?", "(Can we go mine something to smelt?)");
                stage++;
                break;
            case 4:
                playerl(FaceAnim.FRIENDLY, "Maybe.");
                stage++;
                break;
            case 5:
                npc(FaceAnim.CHILD_NORMAL, "Sizzle flicker!", "(Yay! I like doing that!)");
                stage = END_DIALOGUE;
                break;
            case 6:
                playerl(FaceAnim.FRIENDLY, "Good.");
                stage++;
                break;
            case 7:
                npc(FaceAnim.CHILD_NORMAL, "Crackle.", "(Now I'm sad.)");
                stage++;
                break;
            case 8:
                playerl(FaceAnim.HALF_ASKING, "Oh dear, why?");
                stage++;
                break;
            case 9:
                npc(FaceAnim.CHILD_NORMAL, "Hiss-hiss.", "(Happy again.)");
                stage++;
                break;
            case 10:
                playerl(FaceAnim.FRIENDLY, "Glad to hear it.");
                stage++;
                break;
            case 11:
                npc(FaceAnim.CHILD_NORMAL, "Crackley-crick.", "(Sad now.)");
                stage++;
                break;
            case 12:
                playerl(FaceAnim.FRIENDLY, "Um.");
                stage++;
                break;
            case 13:
                npc(FaceAnim.CHILD_NORMAL, "Hiss.", "(Happy.)");
                stage++;
                break;
            case 14:
                playerl(FaceAnim.FRIENDLY, "Right...");
                stage++;
                break;
            case 15:
                npc(FaceAnim.CHILD_NORMAL, "Crackle.", "(Sad.)");
                stage++;
                break;
            case 16:
                playerl(FaceAnim.FRIENDLY, "You're very strange.");
                stage++;
                break;
            case 17:
                npc(FaceAnim.CHILD_NORMAL, "Sizzle hiss?", "(What makes you say that?)");
                stage++;
                break;
            case 18:
                playerl(FaceAnim.FRIENDLY, "Oh...nothing in particular.");
                stage = END_DIALOGUE;
                break;
            case 19:
                playerl(FaceAnim.FRIENDLY, "They are useful for making planks.");
                stage++;
                break;
            case 20:
                npc(FaceAnim.CHILD_NORMAL, "Sizzley crack hiss spit.", "(No, I just like walking on them. They burst into flames.)");
                stage++;
                break;
            case 21:
                playerl(FaceAnim.FRIENDLY, "It's a good job I can use you as a firelighter really!");
                stage = END_DIALOGUE;
                break;
            case 22:
                playerl(FaceAnim.HALF_ASKING, "Are you not enjoying what we're doing?");
                stage++;
                break;
            case 23:
                npc(FaceAnim.CHILD_NORMAL, "Crackley crickle sizzle.", "(Oh yes, but I'm still bored.)");
                stage++;
                break;
            case 24:
                playerl(FaceAnim.FRIENDLY, "Oh, I see.");
                stage++;
                break;
            case 25:
                npc(FaceAnim.CHILD_NORMAL, "Sizzle hiss?", "(What's that over there?)");
                stage++;
                break;
            case 26:
                playerl(FaceAnim.HALF_ASKING, "I don't know. Should we go and look?");
                stage++;
                break;
            case 27:
                npc(FaceAnim.CHILD_NORMAL, "Hiss crackle spit sizzle crack?", "(Nah, that's old news - I'm bored of it now.)");
                stage++;
                break;
            case 28:
                npc(FaceAnim.CHILD_NORMAL, "Crackle crickle spit hiss?", "(Oooooh ooooh oooooh, what's that over there?)");
                stage++;
                break;
            case 29:
                playerl(FaceAnim.HALF_ASKING, "But...wha...where now?");
                stage++;
                break;
            case 30:
                npc(FaceAnim.CHILD_NORMAL, "Sizzle crack crickle.", "(Oh no matter, it no longer interests me.)");
                stage++;
                break;
            case 31:
                playerl(FaceAnim.FRIENDLY, "You're hard work.");
                stage = END_DIALOGUE;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{NPCs.FORGE_REGENT_7335, NPCs.FORGE_REGENT_7336};
    }
}
