package content.region.kandarin.ardougne.quest.sheepherder.npc;

import core.game.interaction.MovementPulse;
import core.game.node.entity.Entity;
import core.game.node.entity.npc.NPC;
import core.game.world.GameWorld;
import core.game.world.map.Location;
import core.game.world.map.path.Pathfinder;

/**
 * The type Herder sheep npc.
 */
public class HerderSheepNPC extends NPC {

    /**
     * The Ticks til return.
     */
    public int ticksTilReturn = 0;

    /**
     * The Spawn location.
     */
    public Location spawnLocation;

    /**
     * Instantiates a new Herder sheep npc.
     *
     * @param id       the id
     * @param location the location
     */
    public HerderSheepNPC(int id, Location location) {
        super(id, location);
        this.spawnLocation = location;
        this.setNeverWalks(false);
        this.unlock();
        this.setRespawn(true);
    }

    @Override
    public void handleTickActions() {
        if (getAttribute("recently-prodded", false)) {
            if (getLocation().withinDistance(Location.create(2593, 3362, 0), 2)) {
                getProperties().setTeleportLocation(Location.create(2599, 3360, 0));
            }
            if (ticksTilReturn < GameWorld.getTicks()) {
                sendChat("Baa");
                this.getPulseManager().run(new MovementPulse(this, spawnLocation) {
                    @Override
                    public boolean pulse() {
                        return true;
                    }
                });
                this.removeAttribute("recently-prodded");
            }
        } else {
            if (nextWalk < GameWorld.getTicks() && !getPulseManager().hasPulseRunning()) {
                setNextWalk();
                Location to = getMovementDestination();
                if (canMove(to)) {
                    Pathfinder.find(this, to, true, Pathfinder.DUMB).walk(this);
                }
            }
        }
    }

    /**
     * Move to.
     *
     * @param l the l
     */
    public void moveTo(Location l) {
        this.getPulseManager().run(new MovementPulse(this, l) {
            @Override
            public boolean pulse() {
                return true;
            }
        });
    }

    @Override
    public void finalizeDeath(Entity killer) {
        this.setRespawnTick(GameWorld.getTicks() + 100);
        super.finalizeDeath(killer);
    }
}