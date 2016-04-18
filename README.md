# mapping-table-sql-data-generator

This is a netBeans project that will take Liferay's mapping tables and create some default data. The initial scope of the project was to help reproduce an issue with scalability in some of the queries Liferay was running. These queries were for a User's inherited group membership.

### Generate SQL

Running the project will take the files found in the classes folder under mapping.table.sql.mapping_tables and use them to build all 9 tables (both the 5 mapping tables and the 4 entities User, Group, UserGroup, Organization). There is some default data specified in the projects mapping_tables folder, but you must change/replace these files with your data and build the project in order for your data to be used. Once the project has been compiled and run, you will find the generated sql in the classes/mapping/table/sql folder.

The initial commit is for Liferay 6.2 with later versions made possible by updating the *INSERT_SQL fields inside of the Builder.java classes.