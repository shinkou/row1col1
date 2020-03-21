# row1col1

row1col1 runs queries, retrieves first columns in the first rows, and put
them in a metric (CSV).  It was originally developed to verify data across
multiple databases.

## How to Compile

```
$ mvn clean package
```

## How to Run

```
$ java [ -Doptions ] -jar ./target/row1col-0.1.0.jar /path/to/some/properties/file
```
where -Doptions could be any of the followings, or other system properties

| Property Name | Description                          | Example                    |
|---------------|--------------------------------------|----------------------------|
| csv.output    | full / relative path the output CSV  | -Dcsv.output=/tmp/some.csv |
| file.encoding | character encoding of the input file | -Dfile.encoding=ISO-8859-1 |

## Configuration

Queries and connection information are organized by alias, which consists of
alphanumeric characters or underscores.  We will annotate it as *db_alias*
below to facilitate our description.  We will use *sql_alias* for similar
purpose, but for SQL statements.

| Property Name              | Description                    | Example                                    |
|----------------------------|--------------------------------|--------------------------------------------|
| *db_alias*.url             | Connection URL to the database | "jdbc:mysql://localhost:3306/my\_database" |
| *db_alias*.user            | Username (optional)            | "joe"                                      |
| *db_alias*.password        | Password (optional)            | "mysecret"                                 |
| *db_alias*.sql.*sql_alias* | SQL statement                  | "SELECT COUNT(\*) FROM some\_tbl"          |

Here is a more complete example:

```
mysql.url=jdbc:mysql://localhost:3306/gamedb
mysql.user=ro_mysql_user
mysql.password=secret
mysql.sql.user_count=SELECT COUNT(*) FROM regular_user
mysql.sql.game_count=SELECT COUNT(*) FROM game

psql.url=jdbc:postgresql://localhost:5432/gamesdb
psql.user=ro_psql_user
psql.password=ro_password
psql.sql.user_count=SELECT COUNT(*) FROM gamesdb.regular_user
psql.sql.game_count=SELECT COUNT(*) FROM gamesdb.game

hive.url=jdbc:hive2://10.10.1.42:10000/default
hive.user=ro_hive_user
hive.sql.user_count=SELECT COUNT(*) FROM gamedb.regular_user
hive.sql.game_count=SELECT COUNT(*) FROM gamedb.game
```
