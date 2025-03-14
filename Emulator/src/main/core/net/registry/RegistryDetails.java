package core.net.registry;

import java.sql.Date;

/**
 * The type Registry details.
 */
public class RegistryDetails {

    private final String username;

    private final String password;

    private final Date birth;

    private final int country;

    /**
     * Instantiates a new Registry details.
     *
     * @param username the username
     * @param password the password
     * @param birth    the birth
     * @param country  the country
     */
    public RegistryDetails(String username, String password, Date birth, int country) {
        this.username = username;
        this.password = password;
        this.birth = birth;
        this.country = country;
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets birth.
     *
     * @return the birth
     */
    public Date getBirth() {
        return birth;
    }

    /**
     * Gets country.
     *
     * @return the country
     */
    public int getCountry() {
        return country;
    }

}
