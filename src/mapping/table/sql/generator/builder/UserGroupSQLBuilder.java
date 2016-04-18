package mapping.table.sql.generator.builder;

import java.util.Map;
import java.util.Set;

/**
 *
 * @author andrewbetts
 */
public class UserGroupSQLBuilder {
    private static final String USERGROUPS = "usergroups";

    private static final String addUserGroupSQL = 
        "insert into UserGroup ("
        + "uuid_, "
            + "companyId, parentUserGroupId, "
        + "name, "
        + "userGroupId) values ";

    private static final String addUserGroupSQLValuesClause = 
        "('%s', "
            + "0, 0,"
        + "'usergroup_%s', "
        + "%s)";

    public static void buildOrganizationSQL(Map<String, Set<Long>> ids) {
        SQLBuilder.buildSQL(
			USERGROUPS, addUserGroupSQL, addUserGroupSQLValuesClause,
			new Object[3], ids.get("usergroupid"));
    }

}
