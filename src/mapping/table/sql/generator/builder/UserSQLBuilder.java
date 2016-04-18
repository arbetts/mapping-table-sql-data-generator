package mapping.table.sql.generator.builder;

import java.util.Map;
import java.util.Set;

/**
 *
 * @author andrewbetts
 */
public class UserSQLBuilder {
    private static final String USERS = "users";

    private static final String addUserSQL = 
        "insert into User_ ("
        + "uuid_, "
            + "companyId, defaultUser, "
        + "screenName, "
        + "emailAddress, "
            + "status, "
        + "userId) values ";

    private static final String addUserSQLValuesClause = 
        "('%s', "
            + "0, false,"
        + "'user_%s', "
        + "'user_%s@liferay.com', "
            + "0, "
        + "%s)";

    public static void buildOrganizationSQL(Map<String, Set<Long>> ids) {
        SQLBuilder.buildSQL(
			USERS, addUserSQL, addUserSQLValuesClause, new Object[4], 
			ids.get("userid"));
    }
    
}
