***

<div align="center">
    <b><em>howto-jse</em></b><br>
    Kleine Beispiele und Tools zur Demonstration nützlicher Java Standard-Edition-Technologien
</div>

***

# Easy Random

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
Das Beispiel in [de.gmxhome.golkonda.howto.easyrandom.DatenspeicherTest](https://github.com/mbeier1406/howto/blob/master/jse/src/test/java/de/gmxhome/golkonda/howto/jse/easyrandom/DatenspeicherTest.java)
demonstriert die Anwendung für Standardfälle.
