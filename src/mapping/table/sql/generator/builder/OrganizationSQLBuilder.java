package mapping.table.sql.generator.builder;

import java.util.Map;
import java.util.Set;

/**
 *
 * @author andrewbetts
 */
public class OrganizationSQLBuilder {
    private static final String ORGANIZATIONS = "organizations";

    private static final String addOrganizationSQL = 
        "insert into Organization_ ("
        + "uuid_, "
            + "companyId, parentOrganizationId, "
        + "name, "
        + "organizationId) values";

    private static final String addOrganizationSQLValuesClause = 
        "('%s', "
            + "0, 0,"
        + "'organization_%s', "
        + "%s)";

    public static void buildOrganizationSQL(Map<String, Set<Long>> ids) {
        SQLBuilder.buildSQL(
			ORGANIZATIONS, addOrganizationSQL, addOrganizationSQLValuesClause,
			new Object[3], ids.get("organizationid"));
    }

}
