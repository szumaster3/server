package core.game.node.entity.player.link;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * The type Activity data.
 */
public final class ActivityData {
    private int pestPoints;
    private int warriorGuildTokens;
    private int bountyHunterRate;
    private int bountyRogueRate;
    private boolean[] barrowBrothers = new boolean[6];
    private int barrowKills;
    private int barrowTunnelIndex;
    private int kolodionStage;
    private int[] godCasts = new int[3];
    private int kolodionBoss;
    private boolean elnockSupplies;
    private long lastBorkBattle;
    private byte borkKills;
    private boolean lostCannon;
    private boolean startedMta;
    private boolean bonesToPeaches;
    private int solvedMazes;
    private int fogRating;
    private boolean hardcoreDeath;
    boolean topGrabbed;

    // Barbarian Firemaking
    private boolean barbarianFiremakingBow;   // Lighting fires with a bow
    private boolean barbarianFiremakingPyre;  // Lighting pyre ships

    // Barbarian Fishing
    private boolean barbarianFishingRod;      // Heavy rod fishing
    private boolean barbarianFishingBarehand; // Barehanded fishing

    // Barbarian Smithing
    private boolean barbarianSmithingSpear;   // Spear smithing
    private boolean barbarianSmithingHasta;   // Hasta smithing

    // Barbarian Herblore
    private boolean barbarianHerbloreAttackMix; // Attack potion + roe

    /**
     * Instantiates a new Activity data.
     */
    public ActivityData() {

    }

    /**
     * Parse.
     *
     * @param data the data
     */
    public void parse(JsonObject data) {
        if (data == null) return;

        pestPoints = getInt(data, "pestPoints");
        warriorGuildTokens = getInt(data, "warriorGuildTokens");
        bountyHunterRate = getInt(data, "bountyHunterRate");
        bountyRogueRate = getInt(data, "bountyRogueRate");
        barrowKills = getInt(data, "barrowKills");

        JsonArray bb = data.getAsJsonArray("barrowBrothers");
        if (bb != null && barrowBrothers != null) {
            int len = Math.min(bb.size(), barrowBrothers.length);
            for (int i = 0; i < len; i++) {
                barrowBrothers[i] = bb.get(i).getAsBoolean();
            }
        }

        barrowTunnelIndex = getInt(data, "barrowTunnelIndex");
        kolodionStage = getInt(data, "kolodionStage");

        JsonArray gc = data.getAsJsonArray("godCasts");
        if (gc != null && godCasts != null) {
            int len = Math.min(gc.size(), godCasts.length);
            for (int i = 0; i < len; i++) {
                godCasts[i] = gc.get(i).getAsInt();
            }
        }

        kolodionBoss = getInt(data, "kolodionBoss");
        elnockSupplies = getBoolean(data, "elnockSupplies");
        lastBorkBattle = getLong(data, "lastBorkBattle");
        startedMta = getBoolean(data, "startedMta");
        lostCannon = getBoolean(data, "lostCannon");
        bonesToPeaches = getBoolean(data, "bonesToPeaches");
        solvedMazes = getInt(data, "solvedMazes");
        fogRating = getInt(data, "fogRating");
        borkKills = (byte) getInt(data, "borkKills");
        hardcoreDeath = getBoolean(data, "hardcoreDeath");
        topGrabbed = getBoolean(data, "topGrabbed");
    }

    private int getInt(JsonObject obj, String memberName) {
        JsonElement el = obj.get(memberName);
        return (el != null && !el.isJsonNull()) ? el.getAsInt() : 0;
    }

    private long getLong(JsonObject obj, String memberName) {
        JsonElement el = obj.get(memberName);
        return (el != null && !el.isJsonNull()) ? el.getAsLong() : 0L;
    }

    private boolean getBoolean(JsonObject obj, String memberName) {
        JsonElement el = obj.get(memberName);
        return (el != null && !el.isJsonNull()) && el.getAsBoolean();
    }

    /**
     * Is elnock supplies boolean.
     *
     * @return the boolean
     */
    public boolean isElnockSupplies() {
        return elnockSupplies;
    }

    /**
     * Sets elnock supplies.
     *
     * @param elnockSupplies the elnock supplies
     */
    public void setElnockSupplies(boolean elnockSupplies) {
        this.elnockSupplies = elnockSupplies;
    }

    /**
     * Increase pest points.
     *
     * @param pestPoints the pest points
     */
    public void increasePestPoints(int pestPoints) {
        if (pestPoints + this.pestPoints > 500) {
            this.pestPoints = 500;
        } else {
            this.pestPoints += pestPoints;
        }
    }

    /**
     * Decrease pest points.
     *
     * @param pestPoints the pest points
     */
    public void decreasePestPoints(int pestPoints) {
        this.pestPoints -= pestPoints;
    }

    /**
     * Gets pest points.
     *
     * @return the pest points
     */
    public int getPestPoints() {
        return pestPoints;
    }

    /**
     * Sets pest points.
     *
     * @param pestPoints the pest points
     */
    public void setPestPoints(int pestPoints) {
        this.pestPoints = pestPoints;
    }

    /**
     * Gets warrior guild tokens.
     *
     * @return the warrior guild tokens
     */
    public int getWarriorGuildTokens() {
        return warriorGuildTokens;
    }

    /**
     * Sets warrior guild tokens.
     *
     * @param warriorGuildTokens the warrior guild tokens
     */
    public void setWarriorGuildTokens(int warriorGuildTokens) {
        this.warriorGuildTokens = warriorGuildTokens;
    }

    /**
     * Update warrior tokens.
     *
     * @param amount the amount
     */
    public void updateWarriorTokens(int amount) {
        this.warriorGuildTokens += amount;
    }

    /**
     * Gets bounty hunter rate.
     *
     * @return the bounty hunter rate
     */
    public int getBountyHunterRate() {
        return bountyHunterRate;
    }

    /**
     * Update bounty hunter rate.
     *
     * @param rate the rate
     */
    public void updateBountyHunterRate(int rate) {
        this.bountyHunterRate += rate;
    }

    /**
     * Gets bounty rogue rate.
     *
     * @return the bounty rogue rate
     */
    public int getBountyRogueRate() {
        return bountyRogueRate;
    }

    /**
     * Update bounty rogue rate.
     *
     * @param rate the rate
     */
    public void updateBountyRogueRate(int rate) {
        this.bountyRogueRate += rate;
    }

    /**
     * Get barrow brothers boolean [ ].
     *
     * @return the boolean [ ]
     */
    public boolean[] getBarrowBrothers() {
        return barrowBrothers;
    }

    /**
     * Sets barrow brothers.
     *
     * @param barrowBrothers the barrow brothers
     */
    public void setBarrowBrothers(boolean[] barrowBrothers) {
        this.barrowBrothers = barrowBrothers;
    }

    /**
     * Gets barrow kills.
     *
     * @return the barrow kills
     */
    public int getBarrowKills() {
        return barrowKills;
    }

    /**
     * Sets barrow kills.
     *
     * @param barrowKills the barrow kills
     */
    public void setBarrowKills(int barrowKills) {
        if (barrowKills > 10000) {
            barrowKills = 10000;
        }
        this.barrowKills = barrowKills;
    }

    /**
     * Gets barrow tunnel index.
     *
     * @return the barrow tunnel index
     */
    public int getBarrowTunnelIndex() {
        return barrowTunnelIndex;
    }

    /**
     * Sets barrow tunnel index.
     *
     * @param barrowTunnelIndex the barrow tunnel index
     */
    public void setBarrowTunnelIndex(int barrowTunnelIndex) {
        this.barrowTunnelIndex = barrowTunnelIndex;
    }

    /**
     * Sets kolodion stage.
     *
     * @param stage the stage
     */
    public void setKolodionStage(int stage) {
        this.kolodionStage = stage;
    }

    /**
     * Has started kolodion boolean.
     *
     * @return the boolean
     */
    public boolean hasStartedKolodion() {
        return kolodionStage == 1;
    }

    /**
     * Has killed kolodion boolean.
     *
     * @return the boolean
     */
    public boolean hasKilledKolodion() {
        return kolodionStage >= 2;
    }

    /**
     * Has received kolodion reward boolean.
     *
     * @return the boolean
     */
    public boolean hasReceivedKolodionReward() {
        return kolodionStage == 3;
    }

    /**
     * Get god casts int [ ].
     *
     * @return the int [ ]
     */
    public int[] getGodCasts() {
        return godCasts;
    }

    /**
     * Gets kolodion boss.
     *
     * @return the kolodion boss
     */
    public int getKolodionBoss() {
        return kolodionBoss;
    }

    /**
     * Sets kolodion boss.
     *
     * @param kolodionBoss the kolodion boss
     */
    public void setKolodionBoss(int kolodionBoss) {
        this.kolodionBoss = kolodionBoss;
    }

    /**
     * Gets last bork battle.
     *
     * @return the last bork battle
     */
    public long getLastBorkBattle() {
        return lastBorkBattle;
    }

    /**
     * Sets last bork battle.
     *
     * @param lastBorkBattle the last bork battle
     */
    public void setLastBorkBattle(long lastBorkBattle) {
        this.lastBorkBattle = lastBorkBattle;
    }

    /**
     * Has killed bork boolean.
     *
     * @return the boolean
     */
    public boolean hasKilledBork() {
        return lastBorkBattle > 0;
    }

    /**
     * Is lost cannon boolean.
     *
     * @return the boolean
     */
    public boolean isLostCannon() {
        return lostCannon;
    }

    /**
     * Sets lost cannon.
     *
     * @param lostCannon the lost cannon
     */
    public void setLostCannon(boolean lostCannon) {
        this.lostCannon = lostCannon;
    }

    /**
     * Is started mta boolean.
     *
     * @return the boolean
     */
    public boolean isStartedMta() {
        return startedMta;
    }

    /**
     * Sets started mta.
     *
     * @param startedMta the started mta
     */
    public void setStartedMta(boolean startedMta) {
        this.startedMta = startedMta;
    }

    /**
     * Is bones to peaches boolean.
     *
     * @return the boolean
     */
    public boolean isBonesToPeaches() {
        return bonesToPeaches;
    }

    /**
     * Sets bones to peaches.
     *
     * @param bonesToPeaches the bones to peaches
     */
    public void setBonesToPeaches(boolean bonesToPeaches) {
        this.bonesToPeaches = bonesToPeaches;
    }

    /**
     * Gets solved mazes.
     *
     * @return the solved mazes
     */
    public int getSolvedMazes() {
        return solvedMazes;
    }

    /**
     * Sets solved mazes.
     *
     * @param solvedMazes the solved mazes
     */
    public void setSolvedMazes(int solvedMazes) {
        this.solvedMazes = solvedMazes;
    }

    /**
     * Gets fog rating.
     *
     * @return the fog rating
     */
    public int getFogRating() {
        return fogRating;
    }

    /**
     * Sets fog rating.
     *
     * @param fogRating the fog rating
     */
    public void setFogRating(int fogRating) {
        this.fogRating = fogRating;
    }

    /**
     * Gets bork kills.
     *
     * @return the bork kills
     */
    public byte getBorkKills() {
        return borkKills;
    }

    /**
     * Sets bork kills.
     *
     * @param borkKills the bork kills
     */
    public void setBorkKills(byte borkKills) {
        this.borkKills = borkKills;
    }

    /**
     * Gets hardcore death.
     *
     * @return the hardcore death
     */
    public boolean getHardcoreDeath() {
        return hardcoreDeath;
    }

    /**
     * Sets hardcore death.
     *
     * @param hardcoreDeath the hardcore death
     */
    public void setHardcoreDeath(boolean hardcoreDeath) {
        this.hardcoreDeath = hardcoreDeath;
    }

    /**
     * Sets top grabbed.
     *
     * @param topGrabbed the top grabbed
     */
    public void setTopGrabbed(boolean topGrabbed) {
        this.topGrabbed = topGrabbed;
    }

    /**
     * Is top grabbed boolean.
     *
     * @return the boolean
     */
    public boolean isTopGrabbed() {
        return topGrabbed;
    }

    /**
     * Gets kolodion stage.
     *
     * @return the kolodion stage
     */
    public int getKolodionStage() {
        return kolodionStage;
    }

    /**
     * Checks if the player has completed bow firemaking.
     *
     * @return true if bow firemaking is done, false otherwise
     */
    public boolean isBarbarianFiremakingBow() { return barbarianFiremakingBow; }

    /**
     * Sets the completion status of bow firemaking.
     *
     * @param barbarianFiremakingBow true if bow firemaking is done
     */
    public void setBarbarianFiremakingBow(boolean barbarianFiremakingBow) { this.barbarianFiremakingBow = barbarianFiremakingBow; }

    /**
     * Checks if the player has completed pyre ship firemaking.
     *
     * @return true if pyre ship firemaking is done, false otherwise
     */
    public boolean isBarbarianFiremakingPyre() { return barbarianFiremakingPyre; }

    /**
     * Sets the completion status of pyre ship firemaking.
     *
     * @param barbarianFiremakingPyre true if pyre ship firemaking is done
     */
    public void setBarbarianFiremakingPyre(boolean barbarianFiremakingPyre) { this.barbarianFiremakingPyre = barbarianFiremakingPyre; }

    /**
     * Checks if the player has completed rod fishing.
     *
     * @return true if rod fishing is done, false otherwise
     */
    public boolean isBarbarianFishingRod() { return barbarianFishingRod; }

    /**
     * Sets the completion status of rod fishing.
     *
     * @param barbarianFishingRod true if rod fishing is done
     */
    public void setBarbarianFishingRod(boolean barbarianFishingRod) { this.barbarianFishingRod = barbarianFishingRod; }

    /**
     * Checks if the player has completed barehanded fishing.
     *
     * @return true if barehanded fishing is done, false otherwise
     */
    public boolean isBarbarianFishingBarehand() { return barbarianFishingBarehand; }

    /**
     * Sets the completion status of barehanded fishing.
     *
     * @param barbarianFishingBarehand true if barehanded fishing is done
     */
    public void setBarbarianFishingBarehand(boolean barbarianFishingBarehand) { this.barbarianFishingBarehand = barbarianFishingBarehand; }

    /**
     * Checks if the player has completed spear smithing.
     *
     * @return true if spear smithing is done, false otherwise
     */
    public boolean isBarbarianSmithingSpear() { return barbarianSmithingSpear; }

    /**
     * Sets the completion status of spear smithing.
     *
     * @param barbarianSmithingSpear true if spear smithing is done
     */
    public void setBarbarianSmithingSpear(boolean barbarianSmithingSpear) { this.barbarianSmithingSpear = barbarianSmithingSpear; }

    /**
     * Checks if the player has completed hasta smithing.
     *
     * @return true if hasta smithing is done, false otherwise
     */
    public boolean isBarbarianSmithingHasta() { return barbarianSmithingHasta; }

    /**
     * Sets the completion status of hasta smithing.
     *
     * @param barbarianSmithingHasta true if hasta smithing is done
     */
    public void setBarbarianSmithingHasta(boolean barbarianSmithingHasta) { this.barbarianSmithingHasta = barbarianSmithingHasta; }

    /**
     * Checks if the player has completed herblore (attack mix).
     *
     * @return true if herblore attack mix is done, false otherwise
     */
    public boolean isBarbarianHerbloreAttackMix() { return barbarianHerbloreAttackMix; }

    /**
     * Sets the completion status of herblore (attack mix).
     *
     * @param barbarianHerbloreAttackMix true if herblore attack mix is done
     */
    public void setBarbarianHerbloreAttackMix(boolean barbarianHerbloreAttackMix) { this.barbarianHerbloreAttackMix = barbarianHerbloreAttackMix; }

    /**
     * Checks if the player has completed all Barbarian training activities.
     *
     * @return true if all barbarian activities are completed, false otherwise
     */
    public boolean hasCompleteBarbarianTraining() {
        return barbarianFiremakingBow &&
                barbarianFiremakingPyre &&
                barbarianFishingRod &&
                barbarianFishingBarehand &&
                barbarianSmithingSpear &&
                barbarianSmithingHasta &&
                barbarianHerbloreAttackMix;
    }
}