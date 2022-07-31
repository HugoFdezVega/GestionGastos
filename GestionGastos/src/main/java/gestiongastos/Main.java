package gestiongastos;

import gestiongastos.controlador.ControladorMVC;
import gestiongastos.modelo.Modelo;
import gestiongastos.vista.Vista;

public class Main{
	public static void main(String[] args) {
		Modelo modelo=new Modelo();
		Vista vista=new Vista();
		ControladorMVC controladorMVC=new ControladorMVC(modelo, vista);
		
		controladorMVC.comenzar();
		
	}

}
