package mapping.table.sql.generator.builder;

import mapping.table.sql.generator.MappingTableSQLGenerator;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author andrewbetts
 */
public class SQLBuilder {
    private static final int max_allowed_packet = 1000;
    private static final AtomicInteger count = new AtomicInteger(0);

    public static void buildSQL(
        String tableName, String addSQL, String valueSQL, Object[] valueArgs,
        Set<Long> ids) {

        URL url = MappingTableSQLGenerator.class.getResource(
			String.format(
				MappingTableSQLGenerator.DESTINATION_FILE_NAME, tableName));

        File fout = new File(url.getPath());

        count.set(0);

        try (FileOutputStream fos = new FileOutputStream(fout);
            BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(fos))) {

            bw.write(addSQL);
            bw.newLine();

            ids.stream()
                .forEach(id -> {
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

                        for (int i = 0; i < valueArgs.length; i++) {
                            if (i == 0) {
                                valueArgs[i] = UUID.randomUUID().toString();
                            }
                            else {
                                valueArgs[i] = id;
                            }
                        }

                        bw.write(String.format(valueSQL, valueArgs));

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
