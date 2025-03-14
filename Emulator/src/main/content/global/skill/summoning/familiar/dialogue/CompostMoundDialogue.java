package content.global.skill.summoning.familiar.dialogue;

import core.game.dialogue.Dialogue;
import core.game.dialogue.FaceAnim;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.Skills;
import core.plugin.Initializable;
import org.rs.consts.NPCs;

import static core.api.ContentAPIKt.*;
import static core.tools.DialogueHelperKt.END_DIALOGUE;

/**
 * The type Compost mound dialogue.
 */
@Initializable
public class CompostMoundDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new CompostMoundDialogue(player);
    }

    /**
     * Instantiates a new Compost mound dialogue.
     */
    public CompostMoundDialogue() {}

    /**
     * Instantiates a new Compost mound dialogue.
     *
     * @param player the player
     */
    public CompostMoundDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        options("Chat", "Withdraw", "Farming boost");
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                switch (buttonId) {
                    case 1:
                        switch (new java.util.Random().nextInt(6)) {
                            case 0:
                                npc(FaceAnim.CHILD_NORMAL, "Schlorp, splort, splort, splutter shclorp?", "(What we be doin' 'ere, zur?)");
                                stage = 124;
                                break;
                            case 1:
                                npcl(FaceAnim.CHILD_NORMAL, "Oi've gotta braand new comboine 'aarvester!");
                                stage = 100;
                                break;
                            case 2:
                                npcl(FaceAnim.CHILD_NORMAL, "What we be doin' 'ere, zur?");
                                stage = 104;
                                break;
                            case 3:
                                npcl(FaceAnim.CHILD_NORMAL, "Errr...are ye gonna eat that?");
                                stage = 106;
                                break;
                            case 4:
                                npcl(FaceAnim.CHILD_NORMAL, "Sigh...");
                                stage = 113;
                                break;
                            case 5:
                                npcl(FaceAnim.CHILD_NORMAL, "Oi wus just a-wonderin'...");
                                stage = 117;
                                break;
                        }
                        break;
                    case 2:
                        end();
                        content.global.skill.summoning.familiar.Forager forager = (content.global.skill.summoning.familiar.Forager) player.getFamiliarManager().getFamiliar();
                        forager.openInterface();
                        break;
                    case 3:
                        player("Can you boost my Farming stat, please?");
                        stage = 30;
                        break;
                }
                break;
            case 30:
                npc("Schlup glorp sputter!", "(Oi do believe oi can!)");
                stage++;
                break;
            case 31:
                if (getDynLevel(player, Skills.FARMING) > getStatLevel(player, Skills.FARMING)) {
                    end();
                    sendMessage(player, "Your stat cannot be boosted this way right now.");
                    return true;
                }
                player.getSkills().updateLevel(Skills.FARMING, (int) (1 + (getStatLevel(player, Skills.FARMING) * 0.02)));
                sendMessage(player, "The Compost mound has boosted your Farming stat.");
                end();
                break;
            case 100:
                playerl(FaceAnim.HALF_ASKING, "A what?");
                stage++;
                break;
            case 101:
                npcl(FaceAnim.CHILD_NORMAL, "Well, it's a flat bit a metal wi' a 'andle that I can use ta 'aarvest all combintions o' plaants.");
                stage++;
                break;
            case 102:
                playerl(FaceAnim.HALF_ASKING, "You mean a spade?");
                stage++;
                break;
            case 103:
                npcl(FaceAnim.CHILD_NORMAL, "Aye, 'aat'll be it.");
                stage = END_DIALOGUE;
                break;
            case 104:
                playerl(FaceAnim.FRIENDLY, "Oh, I have a few things to take care of here, is all.");
                stage++;
                break;
            case 105:
                npcl(FaceAnim.CHILD_NORMAL, "Aye, right ye are, zur. Oi'll be roight there.");
                stage = END_DIALOGUE;
                break;
            case 106:
                playerl(FaceAnim.HALF_ASKING, "Eat what?");
                stage++;
                break;
            case 107:
                npcl(FaceAnim.CHILD_NORMAL, "Y've got summat on yer, goin' wastin'.");
                stage++;
                break;
            case 108:
                playerl(FaceAnim.DISGUSTED, "Ewwww!");
                stage++;
                break;
            case 109:
                npcl(FaceAnim.CHILD_NORMAL, "So ye don' want it then?");
                stage++;
                break;
            case 110:
                playerl(FaceAnim.FRIENDLY, "No I do not want it! Nor do I want to put my boot in your mouth for you to clean it off.");
                stage++;
                break;
            case 111:
                npcl(FaceAnim.CHILD_NORMAL, "An' why not?");
                stage++;
                break;
            case 112:
                playerl(FaceAnim.FRIENDLY, "It'll likely come out dirtier than when I put it in!");
                stage = END_DIALOGUE;
                break;
            case 113:
                playerl(FaceAnim.HALF_ASKING, "What's the matter?");
                stage++;
                break;
            case 114:
                npcl(FaceAnim.CHILD_NORMAL, "Oi'm not 'appy carryin' round these young'uns where we're going.");
                stage++;
                break;
            case 115:
                playerl(FaceAnim.HALF_ASKING, "Young'uns? Oh, the buckets of compost! Well, those wooden containers will keep them safe.");
                stage++;
                break;
            case 116:
                npcl(FaceAnim.CHILD_NORMAL, "'Aah, that be a mighty good point, zur.");
                stage = END_DIALOGUE;
                break;
            case 117:
                playerl(FaceAnim.HALF_ASKING, "Oh! What have you been eating! Your breath is making my eyes water!");
                stage++;
                break;
            case 118:
                npcl(FaceAnim.CHILD_NORMAL, "Oi! Oi'm 'urt by thaat.");
                stage++;
                break;
            case 119:
                playerl(FaceAnim.SAD, "Sorry.");
                stage++;
                break;
            case 120:
                npcl(FaceAnim.CHILD_NORMAL, "Oi mean, oi even et some mints earlier.");
                stage++;
                break;
            case 121:
                playerl(FaceAnim.HALF_ASKING, "You did?");
                stage++;
                break;
            case 122:
                npcl(FaceAnim.CHILD_NORMAL, "'At's roight. Oi found some mint plaants in a big pile o' muck, and oi 'ad 'em fer me breakfast.");
                stage++;
                break;
            case 123:
                playerl(FaceAnim.FRIENDLY, "The mystery resolves itself.");
                stage = END_DIALOGUE;
                break;
            case 124:
                player("Oh, I have a few things to take care of here, is all.");
                stage++;
                break;
            case 125:
                npc(FaceAnim.CHILD_NORMAL, "Schorp, splutter, splutter. Schlup schorp.", "(Aye, right ye are, zur. Oi'll be roight there.)");
                stage = END_DIALOGUE;
                break;
        }

        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{NPCs.COMPOST_MOUND_6871, NPCs.COMPOST_MOUND_6872};
    }
}
