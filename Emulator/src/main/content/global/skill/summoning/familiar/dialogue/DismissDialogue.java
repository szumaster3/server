package content.global.skill.summoning.familiar.dialogue;

import core.game.dialogue.Dialogue;
import core.game.dialogue.DialogueInterpreter;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;

import static core.api.ContentAPIKt.sendDialogueOptions;

/**
 * The type Dismiss dialogue.
 */
@Initializable
public class DismissDialogue extends Dialogue {

    @Override
    public Dialogue newInstance(Player player) {
        return new DismissDialogue(player);
    }

    /**
     * Instantiates a new Dismiss dialogue.
     */
    public DismissDialogue() {}

    /**
     * Instantiates a new Dismiss dialogue.
     *
     * @param player the player
     */
    public DismissDialogue(Player player) {
        super(player);
    }

    @Override
    public boolean open(Object... args) {
        if (player.getFamiliarManager().getFamiliar() instanceof content.global.skill.summoning.pet.Pet) {
            sendDialogueOptions(player, "Free pet?", "Yes", "No");
        } else {
            sendDialogueOptions(player, "Dismiss Familiar?", "Yes", "No");
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 0:
                if (buttonId == 1) {
                    if (player.getFamiliarManager().getFamiliar() instanceof content.global.skill.summoning.pet.Pet) {
                        sendDialogue("Run along; I'm setting you free.");
                        content.global.skill.summoning.pet.Pet pet = (content.global.skill.summoning.pet.Pet) player.getFamiliarManager().getFamiliar();
                        player.getFamiliarManager().removeDetails(pet.getItemIdHash());
                    } else {
                        end();
                    }
                    player.getFamiliarManager().dismiss();
                    stage = 1;
                } else if (buttonId == 2) {
                    end();
                }
                break;
            case 1:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{DialogueInterpreter.getDialogueKey("dismiss_dial")};
    }
}
