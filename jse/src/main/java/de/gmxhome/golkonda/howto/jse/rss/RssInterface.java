package de.gmxhome.golkonda.howto.jse.rss;

import java.math.BigDecimal;

/**
 * <p>Dieses Interface beschreibt alle Methoden zum Auslesen von Daten aus einem RSS-Feed.
 * Um verschiedene Implementierungen zuzulassen werden ausschließlich primitive Datentypen verwendet.
 * Es wird das RSS-Format in der <a href="https://schemas.liquid-technologies.com/W3C/RSS/2.0.1.9/?page=rss-2_0_1-rev9_xsd.html">
 * Version 2.0 (Schemabeschreibung)</a> verwendet. Ein Beispiel für eine RSS-Nachricht findet sich in
 * {@code /howto-jse/src/test/resources/de/gmxhome/golkonda/howto/jse/rss/spiegel.rss.xml}.</p>
 * <b>Hinweis</b>: das Interface ist nicht vollständig und enthält nur für einige Werte die entsprechenden Methoden.
 * @author mbeier
 */
public interface RssInterface {

	/**
	 * <p>Liefert die RSS-Version der Nachricht, wie sie im XML-Attribut {@code version} angegeben ist, z. B. <i>2.0</i>.</p>
	 * Die Version ist im XML-Tag {@code rss} enthalten. 
	 * @return Die RSS-Version der RSS-Nachricht
	 */
	public BigDecimal getVersion();

	/**
	 * <p>Liefert den Titel der RSS-Nachricht, wie sie im XML-Tag {@code /rss/channel/title} angegeben ist.</p>
	 * @return Den Titel der RSS-Nachricht
	 */
	public String getTitle();

}
