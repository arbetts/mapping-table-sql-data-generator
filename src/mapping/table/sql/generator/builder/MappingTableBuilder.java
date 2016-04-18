package mapping.table.sql.generator.builder;

import mapping.table.sql.generator.MappingTableSQLGenerator;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author andrewbetts
 */
public class MappingTableBuilder {
    private static final int max_allowed_packet = 1000;
    private static final AtomicInteger count = new AtomicInteger(0);
    private static final String valuesSQL = "(%s, %s)";

	public static final String GROUPS_ORGS = "groups_orgs";
	public static final String GROUPS_USERGROUPS = "groups_usergroups";
	public static final String USERS_GROUPS = "users_groups";
	public static final String USERS_ORGS = "users_orgs";
	public static final String USERS_USERGROUPS = "users_usergroups";

	public static final String GROUPS_ORGS_INSERT_SQL = 
		"INSERT INTO Groups_Orgs (groupId, organizationId) VALUES ";
	public static final String GROUPS_USERGROUPS_INSERT_SQL = 
		"INSERT INTO Groups_UserGroups (groupId, userGroupId) VALUES ";
	public static final String USERS_GROUPS_INSERT_SQL = 
		"INSERT INTO Users_Groups (userId, groupId) VALUES ";
	public static final String USERS_ORGS_INSERT_SQL = 
		"INSERT INTO Users_Orgs (userId, organizationId) VALUES ";
	public static final String USERS_USERGROUPS_INSERT_SQL = 
		"INSERT INTO Users_UserGroups (userGroupId, userId) VALUES ";

    public static void buildMappingTable(
        String sourceFileName, String destinationFileName, String addSQL) {

        File file = new File(
            MappingTableSQLGenerator.class.getResource(sourceFileName)
                .getPath());

        File fout = new File(
            MappingTableSQLGenerator.class.getResource(
				destinationFileName).getPath());

        count.set(0);

        try (FileOutputStream fos = new FileOutputStream(fout);
            BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(fos))) {

            bw.write(addSQL);
            bw.newLine();

            Files.lines(file.toPath())
                .filter(line -> line.contains("|"))
                .skip(1) // skip the column names
                .forEach(line -> {
                    try {
                        int c = count.getAndIncrement();

                        if (max_allowed_packet <= c) {
                            count.set(1);

                            bw.write(";");
                            bw.newLine();
                            bw.write(addSQL);
                            bw.newLine();
                        }
                        else if (c > 0) {
                            bw.write(',');
                            bw.newLine();                            
                        }

                        String first = 
                            line.substring(0, line.indexOf("|")).trim();
                        String second = 
                            line.substring(line.indexOf("|") + 1).trim();

                        bw.write(String.format(valuesSQL, first, second));

                        bw.flush();
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    }
                });

            bw.write(";");
            bw.flush();
            
            bw.close();
        }
        catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }

}
