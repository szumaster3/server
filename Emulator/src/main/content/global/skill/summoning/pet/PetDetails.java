package content.global.skill.summoning.pet;

/**
 * The PetDetails class holds the data for a pet's hunger, growth, and individual state.
 */
public final class PetDetails {

    private double hunger = 0.0;
    private double growth = 0.0;
    private int individual;

    /**
     * Instantiates a new PetDetails object with a given initial growth value.
     *
     * @param growth the initial growth value of the pet
     */
    public PetDetails(double growth) {
        this.growth = growth;
    }

    /**
     * Updates the hunger value of the pet by the specified amount.
     * If the hunger becomes negative, it is reset to zero.
     *
     * @param amount the amount to increase or decrease the hunger by
     */
    public void updateHunger(double amount) {
        hunger += amount;
        if (hunger < 0.0) {
            hunger = 0.0;
        }
    }

    /**
     * Updates the growth value of the pet by the specified amount.
     * The growth is clamped between 0.0 and 100.0, meaning it cannot go below 0 or above 100.
     *
     * @param amount the amount to increase or decrease the growth by
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
     * Retrieves the current hunger value of the pet.
     *
     * @return the current hunger value
     */
    public double getHunger() {
        return hunger;
    }

    /**
     * Retrieves the current growth value of the pet.
     *
     * @return the current growth value
     */
    public double getGrowth() {
        return growth;
    }

    /**
     * Sets the individual identifier for the pet.
     *
     * @param individual the individual identifier for the pet
     */
    public void setIndividual(int individual) {
        this.individual = individual;
    }

    /**
     * Retrieves the individual identifier of the pet.
     *
     * @return the individual identifier
     */
    public int getIndividual() {
        return individual;
    }
}
