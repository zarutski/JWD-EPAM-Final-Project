package by.epamtc.zarutski.dao.search;

import java.util.HashMap;
import java.util.Map;

/**
 * The class {@code SearchQueryProvider} that provides access
 * to the search sql scripts based on search request
 *
 * @author Maksim Zarutski
 */
public class SearchQueryProvider {

    public static final String SELECT_USERDATA_BY_PASSPORT = "SELECT u.id, u.email, ud.surname, ud.name, " +
            "ud.patronymic, ud.phone_number, ud.passport_series, ud.passport_number, ud.date_birth, " +
            "ud.address, ud.post_code, ud.user_photo_link, ur.role_name " +
            "FROM users AS u " +
            "INNER JOIN user_details AS ud ON u.user_details_id=ud.id " +
            "INNER JOIN user_roles AS ur ON u.role_code=ur.role_code " +
            "WHERE concat(ud.passport_series, ud.passport_number) LIKE ?";

    public static final String SELECT_USERDATA_BY_DEFAULT = "SELECT u.id, u.email, ud.surname, ud.name, " +
            "ud.patronymic, ud.phone_number, ud.passport_series, ud.passport_number, ud.date_birth, " +
            "ud.address, ud.post_code, ud.user_photo_link, ur.role_name " +
            "FROM users AS u " +
            "INNER JOIN user_details AS ud ON u.user_details_id=ud.id " +
            "INNER JOIN user_roles AS ur ON u.role_code=ur.role_code " +
            "WHERE concat(ud.surname, ' ', ud.name) LIKE ?";

    private final Map<SearchPattern, String> searchQueries = new HashMap<>();

    private final static SearchQueryProvider instance = new SearchQueryProvider();

    private SearchQueryProvider() {
        searchQueries.put(SearchPattern.BY_PASSPORT, SELECT_USERDATA_BY_PASSPORT);
    }

    /**
     * Gets search query for passport only if search request full matches passport pattern
     * <p>
     * Otherwise method returns default search query
     *
     * @param searchRequest value is used to get corresponding search query
     * @return {@code String} value containing search query
     */
    public String getSearchQueryByDestination(String searchRequest) {
        String query = SELECT_USERDATA_BY_DEFAULT;

        for (SearchPattern pattern : searchQueries.keySet()) {
            if (searchRequest.matches(pattern.searchPattern)) {
                query = searchQueries.get(pattern);
            }
        }

        return query;
    }

    /**
     * {@code SearchQueryProvider} instance access point
     *
     * @return instance of the {@code SearchQueryProvider} class
     */
    public static SearchQueryProvider getInstance() {
        return instance;
    }
}
