package core.game.node.entity.combat;

import core.game.container.Container;
import core.game.container.ContainerType;
import core.game.event.SelfDeathEvent;
import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.impl.Animator;
import core.game.node.entity.impl.PulseType;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.IronmanMode;
import core.game.node.entity.player.link.audio.Audio;
import core.game.node.entity.player.link.prayer.PrayerType;
import core.game.node.item.Item;
import core.game.system.task.NodeTask;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.game.world.map.Location;

import static core.api.ContentAPIKt.playGlobalAudio;

/**
 * The type Death task.
 */
public final class DeathTask extends NodeTask {

    private static final DeathTask SINGLETON = new DeathTask();

    private DeathTask() {
        super(1);
    }

    @Override
    public void start(Node node, Node... n) {
        Entity e = (Entity) node;
        e.getWalkingQueue().reset();
        e.setAttribute("state:death", true);
        e.setAttribute("tick:death", GameWorld.getTicks());
        e.lock(50);
        e.face(null);
        Entity killer = n.length > 0 ? (Entity) n[0] : e;
        if (e instanceof NPC) {
            killer.removeAttribute("combat-time");
            Audio audio = e.asNpc().getAudio(2);
            if (audio != null) {
                playGlobalAudio(e.getLocation(), audio.id);
            }
        }
        e.graphics(Animator.RESET_G);
        e.visualize(e.getProperties().getDeathAnimation(), e.getProperties().deathGfx);
        e.getAnimator().forceAnimation(e.getProperties().getDeathAnimation());
        e.commenceDeath(killer);
        e.getImpactHandler().setDisabledTicks(50);
    }

    @Override
    public boolean exec(Node node, Node... n) {
        Entity e = (Entity) node;
        int ticks = e.getProperties().getDeathAnimation().getDuration();
        if (ticks < 1 || ticks > 30) {
            ticks = 6;
        }
        return e.getAttribute("tick:death", -1) <= GameWorld.getTicks() - ticks;
    }

    @Override
    public void stop(Node node, Node... n) {
        Entity e = (Entity) node;
        Entity killer = n.length > 0 ? (Entity) n[0] : e;
        e.removeAttribute("state:death");
        e.removeAttribute("tick:death");
        Location spawn = e.getProperties().isSafeZone() ? e.getProperties().safeRespawn : e.getProperties().getSpawnLocation();
        e.getAnimator().forceAnimation(Animator.RESET_A);
        e.getProperties().setTeleportLocation(spawn);
        e.unlock();
        e.finalizeDeath(killer);
        e.getImpactHandler().getNpcImpactLog().clear();
        e.getImpactHandler().getPlayerImpactLog().clear();
        e.getImpactHandler().setDisabledTicks(4);
        e.dispatch(new SelfDeathEvent(killer));
    }

    @Override
    public boolean removeFor(String s, Node node, Node... n) {
        return false;
    }

    /**
     * Get containers container [ ].
     *
     * @param player the player
     * @return the container [ ]
     */
    public static Container[] getContainers(Player player) {
        Container[] containers = new Container[2];
        Container wornItems = new Container(42, ContainerType.ALWAYS_STACK);
        wornItems.addAll(player.getInventory());
        wornItems.addAll(player.getEquipment());
        int count = 3;
        if (player.getSkullManager().isSkulled()) {
            count -= 3;
        }
        if (player.getPrayer().get(PrayerType.PROTECT_ITEMS)) {
            count += 1;
        }
        Container keptItems = new Container(count, ContainerType.NEVER_STACK);
        containers[0] = keptItems;
        if (player.getIronmanManager().getMode() != IronmanMode.ULTIMATE) {
            for (int i = 0; i < count; i++) {
                for (int j = 0; j < 42; j++) {
                    Item item = wornItems.get(j);
                    if (item != null) {
                        for (int x = 0; x < count; x++) {
                            Item kept = keptItems.get(x);
                            if (kept == null || kept != null && kept.getDefinition().getAlchemyValue(true) <= item.getDefinition().getAlchemyValue(true)) {
                                keptItems.replace(new Item(item.getId(), 1, item.getCharge()), x);
                                x++;
                                while (x < count) {
                                    Item newKept = keptItems.get(x);
                                    keptItems.replace(kept, x++);
                                    kept = newKept;
                                }
                                if (kept != null) {
                                    wornItems.add(kept, false);
                                }
                                item = wornItems.get(j);
                                wornItems.replace(new Item(item.getId(), item.getAmount() - 1, item.getCharge()), j);
                                break;
                            }
                        }
                    }
                }
            }
        }
        containers[1] = new Container(42, ContainerType.DEFAULT);
        containers[1].addAll(wornItems);
        return containers;
    }

    /**
     * Start death.
     *
     * @param entity the entity
     * @param killer the killer
     */
    @SuppressWarnings("deprecation")
    public static void startDeath(Entity entity, Entity killer) {
        if (!isDead(entity)) {
            if (killer == null) {
                killer = entity;
            }
            Pulse pulse = new DeathTask().schedule(entity, killer);
            entity.getPulseManager().run(pulse, PulseType.STRONG);
        }
    }

    /**
     * Is dead boolean.
     *
     * @param e the e
     * @return the boolean
     */
    public static boolean isDead(Entity e) {
        if (e instanceof NPC) return ((NPC) e).getRespawnTick() > GameWorld.getTicks() || e.getAttribute("state:death", false);
        else return e.getAttribute("state:death", false);
    }

    /**
     * Gets singleton.
     *
     * @return the singleton
     */
    public static DeathTask getSingleton() {
        return SINGLETON;
    }

}
