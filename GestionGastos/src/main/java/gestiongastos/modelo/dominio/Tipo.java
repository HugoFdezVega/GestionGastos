package gestiongastos.modelo.dominio;

public enum Tipo {
	ALQUILER("Alquiler"), CAPRICHOS("Caprichos"), ALIMENTACION("Alimentacion"), RESTAURANTES("Restaurantes"), BARES("Bares"), OCIO("Ocio"), PARKING("Parking"), 
	GASOLINA("Gasolina"), COCHE("Coche"), ARREGLOS("Arreglos"), VIAJES("Viajes"), HOTEL("Hotel"), SERVICIOS("Servicios"), METALICO("Metálico"),MASCOTAS("Mascotas"),
	LUZ("Luz"),AGUA("Agua");
	
private String cadenaAMostrar;
	
	private Tipo(String cadenaAMostrar) {
		this.cadenaAMostrar=cadenaAMostrar;
	}
	
	public String toString() {
		return cadenaAMostrar;
	}
	
}
