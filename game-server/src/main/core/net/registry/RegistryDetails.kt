package core.net.registry

import java.sql.Date

/**
 * Holds registration details.
 * @author Vexia
 */
class RegistryDetails(
    /**
     * Gets username.
     *
     * @return the username
     */
    val username: String,
    /**
     * Gets password.
     *
     * @return the password
     */
    val password: String,
    /**
     * Gets birth.
     *
     * @return the birth
     */
    val birth: Date,
    /**
     * Gets the country.
     *
     * @return the country.
     */
    val country: Int
)
