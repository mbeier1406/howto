package com.github.mbeier1406.howto.ausbildung.gof.creational;

import lombok.Getter;
import lombok.ToString;

/**
 * GoF - Creational Pattern: Builder<p/>
 * Erstellt verschiedene Versionen eines Objekts mit vielen
 * optionalen Attributen/Feldern. Zerlegt den "Bauprozess" in
 * verschiedene Schritte.<p/>
 * Diese Version des Builder-Patterns verwendet die "fluent API".
 */
@Getter
@ToString
public class BuildObject {

	/* Erzeugung der Objekte nur über den Builder - Felder als NULL initialisiert */
	private BuildObject() {
	}

	private Integer zahl1 = null;
	private Long zahl2 = null;
	private String text;
	private Boolean wahr;

	/** Liefert den Builder zum Bau eines Objektes */
	public static Builder newBuilder() {
		return new BuildObject().new Builder();
	}

	public class Builder {
		/* Den Builder über newBuilder() erzeugen */
		private Builder() {
		}
		public Builder withZahl1(Integer zahl1) {
			BuildObject.this.zahl1 = zahl1;
			return this;
		}
		public Builder withZahl2(Long zahl2) {
			BuildObject.this.zahl2 = zahl2;
			return this;
		}
		public Builder withText(String text) {
			BuildObject.this.text = text;
			return this;
		}
		public Builder withWahr(Boolean wahr) {
			BuildObject.this.wahr = wahr;
			return this;
		}
		public BuildObject build() {
			return BuildObject.this;
		}
	}

}
