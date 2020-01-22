***
<div align="center">
    <b><em>howto</em></b><br>
    Kleine Beispiele und Tools zur Demonstration nützlicher Java-Technologien
</div>


<div align="center">

[Java&trade;](https://www.java.com/de/)
[Jakarta&trade; EE](https://jakarta.ee/)
[XML/XSD/XSLT](https://www.w3.org/XML/)

</div>

***

# HOWTOs

## Java&trade; Tutorials

* [Easy Random](https://github.com/j-easy/easy-random): Mit `EasyRandom` eigene Objekte mit Testdaten befüllen.


### Easy Random

Um für Unit-Tests etc. zufällige Testdaten generieren zu können, kann das [Easy Random](https://github.com/j-easy/easy-random)
Projekt verwendet werden. Eine einfache Maven-Abhängigkeit reicht:

```xml
<dependency>
    <groupId>org.jeasy</groupId>
    <artifactId>easy-random-core</artifactId>
    <version>Version</version>
</dependency>
```

Um ein Objekt `Objekt.class` mit zufälligen Daten zu befüllen, reicht folgender Aufruf: 

```java
EasyRandom easyRandom = new EasyRandom();
Person person = easyRandom.nextObject(<Objekt.class>);
```
Das Beispiel in [de.gmxhome.golkonda.howto.easyrandom.DatenspeicherTest](https://github.com/mbeier1406/howto/blob/master/jse/src/test/java/de/gmxhome/golkonda/howto/easyrandom/DatenspeicherTest.java)
demonstriert die Anwendung für Standardfälle.

## Jakarta&trade; EE

## XML, XSD, XSLT

## Maven

### Artefakte auf Maven Central bereitstellen

Das Verfahren zum Hochladen von Artefakten zum zentralen Repository ist im
[Apache Maven Guide](https://maven.apache.org/repository/guide-central-repository-upload.html) erläutert
(siehe den [OSSRH Guide](https://central.sonatype.org/pages/ossrh-guide.html) bzw.
[Mohammad Nadeem's Guide](https://dzone.com/articles/publish-your-artifacts-to-maven-central) für Details).
Für dieses Projekt gibt es den entsprechenden [Jira Issue](https://issues.sonatype.org/browse/OSSRH-54607)
Neben festgelegten Einträgen in der POM werden auch PGP-Signaturen für die hochzuladenden Artefakte benötigt.
Das Einbinden in Kurzform:

Notwendige Einträge in der `settings.xml`:

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
	<pluginGroups>
	</pluginGroups>
	<proxies>
	</proxies>
	<servers>
		<server>
			<!-- entsprechend distributionManagement und nexus-staging-maven-plugin in der POM -->
			<id>ossrh</id>
			<username>mbeier1406</username>
			<password>...</password>
		</server>
	</servers>
    	<mirrors>
    	</mirrors>
	<profiles>
		<profile>
			<id>ossrh</id>
			<activation>
				<activeByDefault>true</activeByDefault>
			</activation>
			<properties>
				<gpg.passphrase>...</gpg.passphrase>
			</properties>
		</profile>
	</profiles>
</settings>
-->
```

Benötigt wird GPG. Zunächst muss das Schlüsselpaar erzeugt werden, dann müssen die Einträge im Keyring angezeigt 
und der öffentliche Schlüssel (hier: `7BC5361C`) auf einen <em>public keyserver</em> hochgeladen werden.

```Shell
$ gpg --version
gpg (GnuPG) 2.0.24
libgcrypt 1.6.1
Copyright (C) 2013 Free Software Foundation, Inc.
License GPLv3+: GNU GPL version 3 or later <http://gnu.org/licenses/gpl.html>
This is free software: you are free to change and redistribute it.
There is NO WARRANTY, to the extent permitted by law.

Home: ~/.gnupg
Supported algorithms:
Pubkey: RSA, ELG, DSA
Cipher: IDEA, 3DES, CAST5, BLOWFISH, AES, AES192, AES256, TWOFISH,
        CAMELLIA128, CAMELLIA192, CAMELLIA256
Hash: MD5, SHA1, RIPEMD160, SHA256, SHA384, SHA512, SHA224
Compression: Uncompressed, ZIP, ZLIB, BZIP2

$ gpg --gen-key
gpg (GnuPG) 2.0.24; Copyright (C) 2013 Free Software Foundation, Inc.
This is free software: you are free to change and redistribute it.
There is NO WARRANTY, to the extent permitted by law.

gpg: directory `/home/mbeier/.gnupg' created
gpg: new configuration file `/home/mbeier/.gnupg/gpg.conf' created
gpg: WARNING: options in `/home/mbeier/.gnupg/gpg.conf' are not yet active during this run
gpg: keyring `/home/mbeier/.gnupg/secring.gpg' created
gpg: keyring `/home/mbeier/.gnupg/pubring.gpg' created
Please select what kind of key you want:
   (1) RSA and RSA (default)
   (2) DSA and Elgamal
   (3) DSA (sign only)
   (4) RSA (sign only)
Your selection? 1
RSA keys may be between 1024 and 4096 bits long.
What keysize do you want? (2048) 
Requested keysize is 2048 bits
Please specify how long the key should be valid.
         0 = key does not expire
      <n>  = key expires in n days
      <n>w = key expires in n weeks
      <n>m = key expires in n months
      <n>y = key expires in n years
Key is valid for? (0) 2y
Key expires at Sun 05 Dec 2021 08:15:01 PM CET
Is this correct? (y/N) y

GnuPG needs to construct a user ID to identify your key.

Real name: Martin Beier
Email address: Martin.Beier@gmx.de
Comment: mbeier1406 Maven Central
You selected this USER-ID:
    "Martin Beier (mbeier1406 Maven Central) <Martin.Beier@gmx.de>"

Change (N)ame, (C)omment, (E)mail or (O)kay/(Q)uit? O
You need a Passphrase to protect your secret key.

[...]

$ gpg --list-keys
/home/mbeier/.gnupg/pubring.gpg
-------------------------------
pub   2048R/7BC5361C 2019-12-06 [expires: 2021-12-05]
uid       [ultimate] Martin Beier (mbeier1406 Maven Central) <Martin.Beier@gmx.de>
[...]

$ gpg --keyserver hkp://pool.sks-keyservers.net --send-keys 7BC5361C
gpg: sending key 7BC5361C to hkp server pool.sks-keyservers.net
```

Über die Weboberfläche [OpenPGPkeyserver](http://pool.sks-keyservers.net/) kann der Schlüssel gesucht werden:
[7BC5361C ](http://pool.sks-keyservers.net/pks/lookup?op=vindex&fingerprint=on&search=0x5BD8C9F57BC5361C).

Da Maven Central nur gegen eine Signatur prüfen kann, die vom primary-key erstellt wurde, einen erzeugten
[sub-key mit Usage "S" (sign) ggf. löschen](https://central.sonatype.org/pages/working-with-pgp-signatures.html#delete-a-sub-key):

```Script
$ gpg --edit-key 7BC5361C
gpg (GnuPG) 2.0.24; Copyright (C) 2013 Free Software Foundation, Inc.
This is free software: you are free to change and redistribute it.
There is NO WARRANTY, to the extent permitted by law.

Secret key is available.

pub  2048R/7BC5361C  created: 2019-12-06  expires: 2021-12-05  usage: SC  
                     trust: ultimate      validity: ultimate
sub  2048R/332CDA19  created: 2019-12-06  expires: 2021-12-05  usage: E   
[ultimate] (1). Martin Beier (mbeier1406 Maven Central) <Martin.Beier@gmx.de>

gpg> quit
```

Kein solcher Schlüssel vorhanden, ansonsten `key <0..n>; delkey` zum Löschen verwenden.  

Durchführen des Release mit:

```shell
$ mvn clean release:prepare release:perform
$ git push–tags
$ git push origin master
```

Das Artefakt ist unter dieser URL zu finden:
[https://oss.sonatype.org/#nexus-search;quick~mbeier1406](https://oss.sonatype.org/#nexus-search;quick~mbeier1406)
