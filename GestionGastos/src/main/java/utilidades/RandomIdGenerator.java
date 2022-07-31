package utilidades;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RandomIdGenerator {
	private static String[] letras = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z" };
	private static String[] caracteres = new String[] { ".", ",", "+", "-", "/", "*" };
	private static List<String> pass;
	private static String patron="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()�-[{}]:.;',?/*~$^+=<>]).+";
	
	public static String crear(int longitud) {
		String passFinal;
		boolean bandera = false;
		do {
			pass = new ArrayList<>();
			for (int i = 1; i <= longitud; i++) {
				int selector = (int) Math.floor(Math.random() * (4 - 1 + 1) + 1);
				switch (selector) {
				case 1:
					pass.add(letra());
					break;
				case 2:
					pass.add(letra().toUpperCase());
					break;
				case 3:
					pass.add(numero());
					break;
				case 4:
					pass.add(caracter());
					break;
				}
			}
			passFinal = String.join("", pass);
			Pattern pat = Pattern.compile(patron);
			Matcher mat = pat.matcher(passFinal);
			if (mat.matches()) {
				bandera = true;
			}
		} while (bandera == false);
		return passFinal;
	}
	
	private static String letra() {
		int selector = (int) Math.floor(Math.random() * (25 - 0 + 1) + 0);
		String letra = letras[selector];
		return letra;
	}

	private static String numero() {
		int selector = (int) Math.floor(Math.random() * (9 - 0 + 1) + 0);
		String numero = String.valueOf(selector);
		return numero;
	}

	private static String caracter() {
		int selector = (int) Math.floor(Math.random() * ((caracteres.length - 1) - 0 + 1) + 0);
		String caracter = caracteres[selector];
		return caracter;
	}
	
	
}
