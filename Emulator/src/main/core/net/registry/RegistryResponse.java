package core.net.registry;

/**
 * The enum Registry response.
 */
public enum RegistryResponse {
    /**
     * Success registry response.
     */
    SUCCESS(2),
    /**
     * Contact error registry response.
     */
    CONTACT_ERROR(3),
    /**
     * Server busy registry response.
     */
    SERVER_BUSY(7),
    /**
     * Cannot create registry response.
     */
    CANNOT_CREATE(9),
    /**
     * Invalid birth registry response.
     */
    INVALID_BIRTH(10),
    /**
     * Future birth registry response.
     */
    FUTURE_BIRTH(11),
    /**
     * Birth this year registry response.
     */
    BIRTH_THIS_YEAR(12),
    /**
     * Birth last year registry response.
     */
    BIRTH_LAST_YEAR(13),
    /**
     * Invalid country registry response.
     */
    INVALID_COUNTRY(14),
    /**
     * Not available user registry response.
     */
    NOT_AVAILABLE_USER(20),
    /**
     * Invalid username registry response.
     */
    INVALID_USERNAME(22),
    /**
     * Invalid pass length registry response.
     */
    INVALID_PASS_LENGTH(30),
    /**
     * Invalid pass registry response.
     */
    INVALID_PASS(31),
    /**
     * Weak pass registry response.
     */
    WEAK_PASS(32),
    /**
     * Pass similar to user registry response.
     */
    PASS_SIMILAR_TO_USER(33);

    private final int id;

    RegistryResponse(int id) {
        this.id = id;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

}
