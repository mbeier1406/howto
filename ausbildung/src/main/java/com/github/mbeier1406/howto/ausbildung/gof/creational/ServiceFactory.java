package com.github.mbeier1406.howto.ausbildung.gof.creational;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Liefert eine neue Instanz einer spezifische Implementierung des {@linkplain Service}.
 * Abstrahiert von der Objekterzeugung.
 * @see ServiceImpl1
 * @see ServiceImpl2
 */
public class ServiceFactory {

	/** Einthält die Konstruktoren für die bekannten Implementierungen und ihre Kennung */
	private static Map<String, Supplier<Service>> serviceMap = new HashMap<>();

	/** Initalisierung {@linkplain #serviceMap} */
	static {
		serviceMap.put("Service1", ServiceImpl1::new);
		serviceMap.put("Service2", ServiceImpl2::new);
	}

	/**
	 * Liefert zu einer Kennung eine neue Instanz einer Implementierung des {@linkplain Service}.
	 * @param service die Kennung der gewünschten Implementierung
	 * @return das neue Onjekt des Services
	 */
	public static Service getService(String service) {
		return Optional.ofNullable(serviceMap.get(service)).orElseThrow(IllegalArgumentException::new).get();
	}

}
