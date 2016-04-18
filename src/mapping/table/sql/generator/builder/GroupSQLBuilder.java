package mapping.table.sql.generator.builder;

import java.util.Map;
import java.util.Set;

/**
 *
 * @author andrewbetts
 */
public class GroupSQLBuilder {
    private static final String GROUPS = "groups";

    private static final String addGroupSQL = 
        "insert into Group_ ("
        + "uuid_, "
            + "companyId, creatorUserId, classNameId, "
        + "classPK, "
            + "parentGroupId, liveGroupId, "
        + "treePath, "
        + "name, "
            + "type_, membershipRestriction, "
        + "friendlyURL, "
            + "remoteStagingGroupCount, "
        + "groupId) "
            + "values ";

    private static final String addGroupSQLValuesClause = 
        "('%s', "
            + "0, 0, 0, "
        + "%s, "
            + "0, 0, "
        + "'/%s/', "
        + "'group_%s', "
            + "0, 0, "
        + "'friendlyURL_%s', "
            + "0, "
        + "%s)";

    public static void buildGroupSQL(Map<String, Set<Long>> ids) {
        SQLBuilder.buildSQL(
			GROUPS, addGroupSQL, addGroupSQLValuesClause, new Object[6],
			ids.get("groupid"));
    }

}
