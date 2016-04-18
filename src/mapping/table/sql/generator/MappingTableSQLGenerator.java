package mapping.table.sql.generator;

import java.io.File;

import java.net.URL;

import java.nio.file.Files;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import mapping.table.sql.generator.builder.GroupSQLBuilder;
import mapping.table.sql.generator.builder.MappingTableBuilder;
import mapping.table.sql.generator.builder.OrganizationSQLBuilder;
import mapping.table.sql.generator.builder.UserGroupSQLBuilder;
import mapping.table.sql.generator.builder.UserSQLBuilder;

/**
 *
 * @author andrewbetts
 */
public class MappingTableSQLGenerator {

    private static final Map<String, Set<Long>> ids = new HashMap<>();

    private static final String sourceGroupsOrgsFileName = 
        "../mapping_tables/Groups_Orgs";
    private static final String sourceGroupsUserGroupsFileName = 
        "../mapping_tables/Groups_UserGroups";
    private static final String sourceUsersGroupsFileName = 
        "../mapping_tables/Users_Groups";
    private static final String sourceUsersOrgsFileName = 
        "../mapping_tables/Users_Orgs";
    private static final String sourceUsersUserGroupsFileName = 
        "../mapping_tables/Users_UserGroups";

	public static final String DESTINATION_FILE_NAME = "../%s.sql";

	/**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("START");

        System.out.println("PARSE SOURCE FILES");
        addIdsFromMappingTable(sourceGroupsOrgsFileName);
        addIdsFromMappingTable(sourceGroupsUserGroupsFileName);
        addIdsFromMappingTable(sourceUsersGroupsFileName);
        addIdsFromMappingTable(sourceUsersOrgsFileName);
        addIdsFromMappingTable(sourceUsersUserGroupsFileName);
  
        System.out.println("BUILDING GROUPS_ORGS SQL");
        MappingTableBuilder.buildMappingTable(sourceGroupsOrgsFileName,
			String.format(DESTINATION_FILE_NAME, 
				MappingTableBuilder.GROUPS_ORGS),
            MappingTableBuilder.GROUPS_ORGS_INSERT_SQL);

        System.out.println("BUILDING GROUPS_USERGROUPS SQL");
        MappingTableBuilder.buildMappingTable(sourceGroupsUserGroupsFileName, 
			String.format(DESTINATION_FILE_NAME, 
				MappingTableBuilder.GROUPS_USERGROUPS), 
            MappingTableBuilder.GROUPS_USERGROUPS_INSERT_SQL);

        System.out.println("BUILDING USERS_GROUPS SQL");
        MappingTableBuilder.buildMappingTable(sourceUsersGroupsFileName,
			String.format(DESTINATION_FILE_NAME,
				MappingTableBuilder.USERS_GROUPS),
            MappingTableBuilder.USERS_GROUPS_INSERT_SQL);

        System.out.println("BUILDING USERS_ORGS SQL");
        MappingTableBuilder.buildMappingTable(sourceUsersOrgsFileName,
			String.format(DESTINATION_FILE_NAME,
				MappingTableBuilder.USERS_ORGS),
            MappingTableBuilder.USERS_ORGS_INSERT_SQL);

        System.out.println("BUILDING USERS_USERGROUPS SQL");
        MappingTableBuilder.buildMappingTable(sourceUsersUserGroupsFileName,
			String.format(DESTINATION_FILE_NAME,
				MappingTableBuilder.USERS_USERGROUPS),
            MappingTableBuilder.USERS_USERGROUPS_INSERT_SQL);

        System.out.println("BUILDING GROUP SQL");
        GroupSQLBuilder.buildGroupSQL(ids);

        System.out.println("BUILDING ORGANIZATION SQL");
        OrganizationSQLBuilder.buildOrganizationSQL(ids);

        System.out.println("BUILDING USERGROUP SQL");
        UserGroupSQLBuilder.buildOrganizationSQL(ids);

        System.out.println("BUILDING USER SQL");
        UserSQLBuilder.buildOrganizationSQL(ids);

        System.out.println("DONE");
    }

    private static void addIdsFromMappingTable(String fileName) {
        URL url = MappingTableSQLGenerator.class.getResource(fileName);

        File file = new File(url.getPath());

        try {
            Stream<String> firstColStream = 
                Files.lines(file.toPath())
                    .filter(line -> line.contains("|"))
                    .map(line -> line.substring(0, line.indexOf("|")))
                    .map(str -> str.trim());

            Stream<String> secondColStream = 
                Files.lines(file.toPath())
                    .filter(line -> line.contains("|"))
                    .map(line -> line.substring(line.indexOf("|") + 1))
                    .map(str -> str.trim());

            putIds(firstColStream);
            putIds(secondColStream);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void putIds(Stream<String> colStream) {
        final MutableString key = new MutableString();

        Set<Long> longs = colStream.filter(str -> {
            try {
                Long.parseLong(str);
            }
            catch (NumberFormatException nfe) {
                key.setString(str);

                return false;
            }

            return true;
        })
        .map(str -> Long.parseLong(str))
        .collect(Collectors.toSet());

        Set<Long> longIds = ids.get(key.getString());

        if (longIds == null) {
            longIds = new HashSet<>();
        }

        longIds.addAll(longs);

        ids.put(key.getString(), longs);
    }

    private static class MutableString {
        private String str;

        public void setString(String str) {
            this.str = str;
        }

        public String getString() {
            return str;
        }
    }

}
