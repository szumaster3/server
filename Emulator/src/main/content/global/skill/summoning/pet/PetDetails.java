package content.global.skill.summoning.pet;

/**
 * The type Pet details.
 */
public final class PetDetails {

    private double hunger = 0.0;

    private double growth = 0.0;

    private int individual;

    /**
     * Instantiates a new Pet details.
     *
     * @param growth the growth
     */
    public PetDetails(double growth) {
        this.growth = growth;
    }

    /**
     * Update hunger.
     *
     * @param amount the amount
     */
    public void updateHunger(double amount) {
        hunger += amount;
        if (hunger < 0.0) {
            hunger = 0.0;
        }
    }

    /**
     * Update growth.
     *
     * @param amount the amount
     */
    public void updateGrowth(double amount) {
        growth += amount;
        if (growth < 0.0) {
            growth = 0.0;
        } else if (growth > 100.0) {
            growth = 100.0;
        }
    }

    /**
     * Gets hunger.
     *
     * @return the hunger
     */
    public double getHunger() {
        return hunger;
    }

    /**
     * Gets growth.
     *
     * @return the growth
     */
    public double getGrowth() {
        return growth;
    }

    /**
     * Sets individual.
     *
     * @param individual the individual
     */
    public void setIndividual(int individual) {
        this.individual = individual;
    }

    /**
     * Gets individual.
     *
     * @return the individual
     */
    public int getIndividual() {
        return individual;
    }
}
