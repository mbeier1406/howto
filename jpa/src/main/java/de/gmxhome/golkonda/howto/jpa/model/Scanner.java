package de.gmxhome.golkonda.howto.jpa.model;

import static javax.persistence.GenerationType.SEQUENCE;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Definiert ein {@index Scanner}-Objekt zum Speichern in der Datenbank. Beispiel:
 * <pre><code>
 * mysql> select * from SCANNER;
 * +----+-------------+-----------+
 * | ID | BEZEICHNUNG | SCANNERID |
 * +----+-------------+-----------+
 * |  1 | Scanner 1   |         1 |
 * +----+-------------+-----------+
 * </code></pre>
 * @author mbeier
 */
@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"scanner_id"})})
public class Scanner {

	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "scanner_generator")
	@SequenceGenerator(name="scanner_generator", sequenceName = "scanner_seq", allocationSize=1, initialValue=1)
	private Long id;

	public Scanner() {}
	public Scanner(Integer scannerId, String bezeichnung) {
		this.scannerId = scannerId;
		this.bezeichnung = bezeichnung;
	}

	/** Fachliche ID des Scanners */
	@Column(name="scanner_id", unique=true)
	private Integer scannerId;

	/** Fachliche Bezeichnung des Scanners */
	private String bezeichnung;

	public Long getId() {
		return id;
	}

	public Integer getScannerId() {
		return scannerId;
	}

	public void setScannerId(Integer scannerId) {
		this.scannerId = scannerId;
	}

	public String getBezeichnung() {
		return bezeichnung;
	}

	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	@Override
	public String toString() {
		return "Scanner [id=" + id + ", scannerId=" + scannerId + ", bezeichnung=" + bezeichnung + "]";
	}

}
