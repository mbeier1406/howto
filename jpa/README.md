# Projekt "database"

Einfaches [CRUD-Beispiel](https://de.wikipedia.org/wiki/CRUD) für JPA und JDBC. Es wird eine Instanz einer Testdatenbank benötigt.
Diese wird als [Docker-Container](http://www.docker.com/) bereitgestellt. Es wird entsprechend vorausgesetzt,
dass Docker auf der lokalen Maschine installiert ist.

Das Vorgehen besteht aus folgenden Schritten:
- `Einrichten des Containers`: der Container betreibt die MySql-Datenbank und ist über einen externen Port erreichbar.
- `Installieren einer SQL-GUI`: es wird die SQL-Workbench verwendet um direkt SQL auszuführen.
- `Einrichten der Testdatenbank`: eine einfache Tabelle wird angelegt und mit wenigen Datensätzen befüllt.
- `Testen der Datenbank`: die Datenbank wird über die im Container mitgelieferte CLI getestet.

__ACHTUNG__: Das Beispiel ist nicht ThreadSafe! Es handelt sich nur um die einfache Demonstration von SQL-Basisfunktionen!

Einrichten des Containers
-

Zunächst wird das [MySql-Image](https://hub.docker.com/r/mysql/mysql-server/) lokal installiert und ein
Container erzeugt. In diesem Beispiel wird die Version 5.7 des Images verwendet.
Damit die MySql-Datenbank von externen Programmen erreichbar ist, wird der Start mit einem Port-Mapping eingerichtet
und die Datenbank für externen Zugriff freigeschaltet.

```
$ docker pull mysql/mysql-server:5.7
$ docker run --name=mysql -p3306:3306 -d mysql/mysql-server:5.7
$ docker logs mysql 2>&1 | grep GENERATED
$ docker exec -it mysql mysql -uroot -p
mysql> ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'password';
mysql> use mysql;
mysql> UPDATE user SET host='%' WHERE user='root';
mysql> GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' [ggf. WITH GRANT OPTION];
mysql> FLUSH PRIVILEGES;
mysql> QUIT;
```

Installation und Test einer SQL-GUI
-

Auf dem Client muss eine SQL-GUI wie [SQL-Workbench](https://www.sql-workbench.eu/) installiert werden.
Der Treiber muss in der Anwendung eingerichtet werden ([Downloads](https://downloads.mysql.com/archives/c-j/)).

```
$ rpm --upgrade --verbose --hash ./Downloads/mysql-workbench-community-8.0.12-1.el7.x86_64.rpm
$ ln -s /usr/share/java/mysql-connector-java-8.0.12.jar <SQL_WORKBENCH_DIR>/mysql-connector-java-8.0.12.jar
$ <SQL_WORKBENCH_DIR>/sqlworkbench.sh
```

Treiber in der Anwendung für mysql einbinden. Connection-URL ist `jdbc:mysql://localhost:3306/mysqld`.



Einrichten der Testdatenbank
-

Über die GUI wird folgendes SQL ausgeführt:

```sql
use mysql;

drop table auto_klassen;
create table auto_klassen (
  id_auto_klassen int         not null auto_increment,
  klasse          varchar(15) not null,
  preis           int         not null,
  primary key ( id_auto_klassen )
)
auto_increment=1;

insert into auto_klassen (
  klasse, preis
)
values (
  'Limousine', 1000
);
insert into auto_klassen (
  id_auto_klassen, klasse, preis
)
values (
  1, 'Limousine', 1000
);
-- Doppelter Primary Key
-- Duplicate entry '1' for key 'PRIMARY'
-- 1 statement failed.
insert into auto_klassen (
  klasse, preis
)
values (
  'Mittelklasse', 100
);
insert into auto_klassen (
  klasse, preis
)
values (
  'Kleineagen', 10
);
SELECT LAST_INSERT_ID();
SET SESSION query_cache_type=0;
RESET QUERY CACHE;
select SQL_NO_CACHE *, NOW() from auto_klassen;
-- Noch nicht in anderen Sessions zu sehen
-- rollback; -- INSERT zurücknehmen 
commit;
-- Ab hier auch in anderen Sessions zu sehen
```

Testen der Datenbank
-

Über das im Container mitgeliefert CLI wird die Datenbank ebenfalls getestet.

```
$ docker exec -it <Container-ID> /bin/bash
# mysql -P3306 -uroot -pxxx
mysql> select * from mysql.auto_klassen;
```

Für das Java-Testprogramm JDBC wird Maven zum Dependency-Management verwendet. Folgendes Artifakt
wird konfiguriert:

```xml
<!-- https://mvnrepository.com/artifact/mysql/mysql-connector-java -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.12</version>
</dependency>
```
