***
<div align="center">
    <b><em>howto</em></b><br>
    Kleine Beispiele und Tools zur Demonstration nützlicher Java-Technologien
</div>


<div align="center">

[![Java&trade;](https://de.wikipedia.org/w/index.php?title=Datei:Java-Logo.svg)](https://www.java.com/de/)
[Jakarta&trade; EE](https://jakarta.ee/)
[XML/XSD/XSLT](https://www.w3.org/XML/)

</div>

***

# HOWTOs

##Java&trade; Tutorials

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
Das Beispiel in `de.gmxhome.golkonda.howto.easyrandom.DatenspeicherTest` demonstriert die Anwendung für Standardfälle.

## Jakarta&trade; EE

## XML, XSD, XSLT
