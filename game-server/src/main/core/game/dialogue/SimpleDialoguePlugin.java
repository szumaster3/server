package core.game.dialogue;

import core.game.node.entity.player.Player;
import core.plugin.Initializable;

/**
 * The type Simple dialogue plugin.
 */
@Initializable
public class SimpleDialoguePlugin extends Dialogue {

    /**
     * Instantiates a new Simple dialogue plugin.
     */
    public SimpleDialoguePlugin() {

	}

    /**
     * Instantiates a new Simple dialogue plugin.
     *
     * @param player the player
     */
    public SimpleDialoguePlugin(Player player) {
		super(player);
	}

	@Override
	public int[] getIds() {
		return new int[] { 70099 };
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {
		end();
		return true;
	}

	@Override
	public Dialogue newInstance(Player player) {
		return new SimpleDialoguePlugin(player);
	}

	@Override
	public boolean open(Object... args) {
		String[] messages = new String[args.length];
		for (int i = 0; i < messages.length; i++)
			messages[i] = (String) args[i];
		interpreter.sendDialogue(messages);
		return true;
	}

}
