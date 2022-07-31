package gestiongastos.controlador;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import javax.naming.OperationNotSupportedException;

import gestiongastos.modelo.Modelo;
import gestiongastos.modelo.dominio.Gasto;
import gestiongastos.modelo.dominio.Tipo;
import gestiongastos.modelo.dominio.Usuario;
import gestiongastos.vista.Vista;

public class ControladorMVC {
	private Modelo modelo;
	private Vista vista;
	
	public ControladorMVC(Modelo modelo, Vista vista) {
		if (modelo == null) {
			throw new NullPointerException("ERROR: El modelo no puede ser nulo.");
		}
		if (vista == null) {
			throw new NullPointerException("ERROR: La vista no puede ser nula.");
		}
		this.modelo = modelo;
		this.vista = vista;
		this.vista.setControlador(this);
	}
	
	public void comenzar() {
		modelo.comenzar();
		vista.comenzar();
	}
	
	public void terminar() {
		modelo.terminar();
	}
	
	public void insertarUsuario(Usuario user) throws OperationNotSupportedException {
		modelo.insertarUsuario(user);
	}
	
	public void borrarUsuario(Usuario user){
		modelo.borrarUsuario(user);
	}
	
	public void insertarGasto(Usuario user, Gasto gasto) throws OperationNotSupportedException {
		modelo.insertarGasto(user, gasto);
	}
	
	public void borrarGasto(Usuario user, Gasto gasto) throws OperationNotSupportedException {
		modelo.borrarGasto(user, gasto);
	}
	
	public List<Gasto> obtenerGastosUsuario (Usuario user){
		return modelo.obtenerGastosUsuario(user);
	}
	
	public List<Gasto> obtenerGastosDelMes (Usuario user, LocalDate fechaMes, LocalDate fechaAnio){
		return modelo.obtenerGastosDelMes(user, fechaMes, fechaAnio);
	}
	
	public List<Gasto> obtenerGastosDelTipo (Usuario user, Tipo tipoGasto, int anio){
		return modelo.obtenerGastosDelTipo(user, tipoGasto, anio);
	}
	
	public List<Usuario> obtenerListaUsuarios(){
		return modelo.getListaUsuarios();
	}
	
	public Usuario obtenerUsuario(Usuario userFicticio) {
		return modelo.obtenerUsuario(userFicticio);
	}
	
	
}
