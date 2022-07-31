package gestiongastos.modelo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.naming.OperationNotSupportedException;

import gestiongastos.modelo.dominio.Gasto;
import gestiongastos.modelo.dominio.Tipo;
import gestiongastos.modelo.dominio.Usuario;

public class Modelo {
	private List<Usuario> listaUsuarios;

	/*
	 * Getters y setters validando excepciones. Nos aseguramos de devolver una copia en el caso de getListaUsuarios() para evitar aliasing
	 */
	public List<Usuario> getListaUsuarios() {
		List<Usuario> copiaListaUsuarios= new ArrayList<>();
		for(Usuario u : listaUsuarios) {
			copiaListaUsuarios.add(new Usuario(u));
		}
		return copiaListaUsuarios;
	}
	private void setListaUsuarios(List<Usuario> listaUsuarios) {
		if(listaUsuarios==null) {
			throw new NullPointerException("ERROR: No se puede asignar una lista de usuarios nula");
		}
		this.listaUsuarios = listaUsuarios;
	}
	
	/*
	 * Constructor por defecto
	 */
	public Modelo() {
		listaUsuarios=new ArrayList<>();
	}
	
	public int buscarUsuario(Usuario user) {
		if(user==null) {
			throw new NullPointerException("ERROR: No se puede operar con usuarios nulos");
		}
		int indice=-1;
		for (int i=0;i<listaUsuarios.size();i++) {
			if(user.equals(listaUsuarios.get(i))) {
				indice=i;
			}
		}
		return indice;
	}
	
	public void insertarUsuario(Usuario user) throws OperationNotSupportedException {
		if(buscarUsuario(user)!=-1) {
			throw new OperationNotSupportedException("ERROR: Ya existe el usuario a insertar");
		}
		listaUsuarios.add(user);
	}
	
	public void borrarUsuario(Usuario user) {
		int indice=buscarUsuario(user);
		listaUsuarios.remove(indice);

	}
	
	public void insertarGasto(Usuario user, Gasto gasto) throws OperationNotSupportedException {
		int indice=buscarUsuario(user);
		listaUsuarios.get(indice).insertarGasto(gasto);
	}
	
	public void borrarGasto(Usuario user, Gasto gasto) throws OperationNotSupportedException {
		int indice=buscarUsuario(user);
		listaUsuarios.get(indice).borrarGasto(gasto);
	}
	
	public List<Gasto> obtenerGastosUsuario(Usuario user){
		int indice=buscarUsuario(user);
		return listaUsuarios.get(indice).getListaGastos();
	}
	
	public List<Gasto> obtenerGastosDelMes(Usuario user, LocalDate fechaMes, LocalDate fechaAnio){
		int indice=buscarUsuario(user);
		return listaUsuarios.get(indice).obtenerGastosMes(fechaMes, fechaAnio);
	}
	
	public List<Gasto> obtenerGastosDelTipo(Usuario user, Tipo tipoGasto, int anio){
		int indice=buscarUsuario(user);
		return listaUsuarios.get(indice).obtenerGastosTipo(tipoGasto, anio);
	}
	
	public Usuario obtenerUsuario(Usuario userFicticio) {
		int indice=buscarUsuario(userFicticio);
		return listaUsuarios.get(indice);
	}
	
	public void comenzar() {
		File archivoListaUsuarios=new File("datos/listaUsuarios.dat");
		try {
				FileInputStream fileIn = new FileInputStream(archivoListaUsuarios);
				ObjectInputStream dataIS=new ObjectInputStream(fileIn);
				Usuario user;
				do {
					user= (Usuario) dataIS.readObject();
					if (user!=null) {
						listaUsuarios.add(user);
					}
				} while (user!=null);
				dataIS.close();
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: No se pudo abrir el fichero de lista de usuarios");
		} catch (IOException e) {
			// System.out.println("ERROR inesperado de Entrada/Salida en lectura de lista de usuarios");
		} catch (ClassNotFoundException e) {
			System.out.println("ERROR: No se pudo encontrar la clase a leer");
		}
	}
	
	public void terminar() {
		File archivoListaUsuarios=new File("datos/listaUsuarios.dat");
		try {
			FileOutputStream fileOut=new FileOutputStream(archivoListaUsuarios);
			ObjectOutputStream dataOS=new ObjectOutputStream(fileOut);
			for(Usuario u : listaUsuarios) {
				dataOS.writeObject(u);
			}
			dataOS.close();
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: No se pudo encontrar el fichero de lista de usuarios");
		} catch (IOException e) {
			System.out.println("ERROR inesperado de Entrada/Salida en la lista de usuarios");
			e.printStackTrace();
		}
		
	}
	

	
}
