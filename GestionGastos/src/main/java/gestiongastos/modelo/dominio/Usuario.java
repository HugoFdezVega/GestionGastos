package gestiongastos.modelo.dominio;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import javax.naming.OperationNotSupportedException;

public class Usuario implements Serializable{
	private String nombre;
	private double sueldo;


	private List<Gasto> listaGastos;
	

	/*
	 * Getters y setters validando excepciones. Nos aseguramos de devolver una copia en el caso de getListaGastos() para evitar aliasing
	 */
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		if(nombre==null) {
			throw new NullPointerException("ERROR: El usuario no puede ser nulo");
		}
		else if (nombre.isBlank()) {
			throw new IllegalArgumentException("ERROR: El nombre de un usuario no puede estar en blanco");
		}
		this.nombre = nombre;
	}
	public double getSueldo() {
		return sueldo;
	}
	private void setSueldo(double sueldo) {
		if(sueldo<0) {
			throw new IllegalArgumentException("ERROR: El sueldo de un usuario no puede ser negativo");
		}
		this.sueldo = sueldoFormateado(sueldo);
	}
	public List<Gasto> getListaGastos() {
		List<Gasto> copiaListaGastos=new ArrayList<>();
		for(Gasto g : listaGastos) {
			copiaListaGastos.add(new Gasto(g));
		}
		return copiaListaGastos;
	}
	public void setListaGastos(List<Gasto> listaGastos) {
		if(listaGastos==null) {
			throw new NullPointerException("ERROR: No se puede asignar una lista de gastos nula");
		}
		this.listaGastos = listaGastos;
	}
	private double sueldoFormateado(double sueldo) {
		int sueldoSinDecimales=(int) (sueldo*100);
		double sueldoFinal=(double) sueldoSinDecimales/100;
		return sueldoFinal;
	}
	
	/*
	 * Constructor con parámetros
	 */
	public Usuario (String nombre, double sueldo) {
		setNombre(nombre);
		setSueldo(sueldo);
		listaGastos=new ArrayList<>();
	}
	
	/*
	 * Constructor copia validando null
	 */
	public Usuario (Usuario u) {
		if(u==null) {
			throw new NullPointerException("ERROR: No se puede copiar un usuario nulo");
		}
		setNombre(u.getNombre());
		setSueldo(u.getSueldo());
		setListaGastos(u.getListaGastos());
	}
	
	/*
	 * hashCode y equals que determinan que dos objetos son iguales si sus nombres coinciden
	 */
	@Override
	public int hashCode() {
		return Objects.hash(nombre);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		return Objects.equals(nombre, other.nombre);
	}
	
	/*
	 * toString que retorna el nombre del usuario
	 */
	@Override
	public String toString() {
		return nombre;
	}
	
	/*
	 * Método insertarGasto, que recibe un gasto, valida null y después busca en listaGastos si existe. De no ser así, lo inserta en la lista
	 */
	public void insertarGasto(Gasto gasto) throws OperationNotSupportedException {
		if (gasto==null) {
			throw new NullPointerException("ERROR: No se puede insertar un gasto nulo");
		}
		for (Gasto g : listaGastos) {
			if(gasto.equals(g)) {
				throw new OperationNotSupportedException("ERROR: Ya existe el gasto a insertar");
			}
		}
		listaGastos.add(gasto);
	}
	
	/*
	 * Método borrarGasto, que recibe un gasto y valida null. Después recorre listaGastos para comprobar si existe alguno igual. Si es así, pone la
	 * bandera en true y guarda una copia de dicho gasto en gastoBorrado. Si la bandera está en false, lanza excepción y si no borra el gasto guardado
	 */
	public void borrarGasto(Gasto gasto) throws OperationNotSupportedException {
		boolean borrado=false;
		Gasto gastoBorrado=null;
		if (gasto==null) {
			throw new NullPointerException("ERROR: No se puede borrar un gasto nulo");
		}
		for (Gasto g : listaGastos) {
			if(gasto.equals(g)) {
				gastoBorrado=new Gasto(g);
				borrado=true;
			}
		}
		if(borrado==false) {
			throw new OperationNotSupportedException("ERROR: No existe el gasto a borrar");
		} else {
			listaGastos.remove(gastoBorrado);
		}
	}
	
	/*
	 * Método obtenerGastosMes, que recibe una fecha para el mes y otra para el año. Después genera una serie de atributos falsos y crea un Set para
	 * ir guardando en él los diversos gastos. Recorre listaGastos, y cuando un gasto coincide con el mes y el año de los parámetros, inserta un gasto
	 * ficticio con los atributos falsos, pero con el Tipo del gasto coincidente. Al ser un Set, solo quedarán guardados en él los Gastos de Tipo distintos,
	 * obteniendo así una lista de los Tipo de gastos que se dieron en el mes y año de los parámetros. Después pasa el Set a un ArrayList para iterarlo con
	 * mayor facilidad. Recorre de nuevo listaGastos y el ArrayList, comparando cada Tipo de los gastos con los Tipo guardados en el ArrayList. Si coinciden,
	 * guarda el importe del Tipo coincidente en una variable auxiliar, borra dicho Tipo mediante el índice del for y crea un nuevo objeto con el Tipo correcto,
	 * y siendo su importe la suma de la variable auxiliar y el importe del gasto coincidente. Después sale del for mediante una bandera para evitar duplicidades.
	 */
	public List<Gasto> obtenerGastosMes (LocalDate fechaMes, LocalDate fechaAnio){
		double falsoImporte=0.0;
		LocalDate falsaFecha=LocalDate.now();
		String falsaDescripcion="Falsa descripcion";
		String falsaId="0000";
		
		List<Gasto> gastosDelMes=new ArrayList<>();
		for (Gasto g : listaGastos) {
			if(fechaMes.getMonth().equals(g.getFecha().getMonth())&&fechaAnio.getYear()==g.getFecha().getYear()) {
				gastosDelMes.add(new Gasto(g));
			}
		}
		
		Set<Gasto> gastosTipo = new HashSet<>();
		for(Gasto g : gastosDelMes) {
			gastosTipo.add(new Gasto(falsoImporte,g.getTipoGasto(),falsaFecha,falsaDescripcion,falsaId));
		}
		
		List<Gasto> gastosAcumulados = new ArrayList<>();
		for(Gasto g : gastosTipo) {
			gastosAcumulados.add(g);
		}
		Collections.shuffle(gastosAcumulados);
		for(Gasto g : gastosDelMes) {
			boolean agregado=false;
			for(int i=0;i<gastosAcumulados.size()&&agregado==false;i++) {
				if(g.getTipoGasto().equals(gastosAcumulados.get(i).getTipoGasto())) {
//					double gastoAnterior=gastosAcumulados.get(i).getImporte();
//					gastosAcumulados.remove(i);
//					gastosAcumulados.add(new Gasto (gastoAnterior+g.getImporte(),g.getTipoGasto(),falsaFecha,falsaDescripcion,falsaId));
//					agregado=true;
					double gastoAnterior=gastosAcumulados.get(i).getImporte();
					gastosAcumulados.get(i).setImporte(gastoAnterior+g.getImporte());
					agregado=true;
				}
			}
		}
		return gastosAcumulados;
	}
	
	public List<Gasto> obtenerGastosTipo (Tipo tipoGasto, int anio){
		List<Gasto> gastosDelTipo=new ArrayList<>();
		for(Gasto g : listaGastos) {
			if(anio==g.getFecha().getYear()&&tipoGasto.equals(g.getTipoGasto())) {
				gastosDelTipo.add(new Gasto(g));
			}
		}
		
		List<Gasto> gastosAcumulados=listarGastosDelTipo(tipoGasto, anio);
		for(Gasto g : gastosDelTipo) {
			boolean agregado=false;
			for(int i=0;i<gastosAcumulados.size()&&agregado==false;i++) {
				if(g.getFecha().getMonth().equals(gastosAcumulados.get(i).getFecha().getMonth())) {
					double gastoAnterior=gastosAcumulados.get(i).getImporte();
					gastosAcumulados.get(i).setImporte(gastoAnterior+g.getImporte());
					agregado=true;
				}
			}
		}
		return gastosAcumulados;
	}
	
	private List<Gasto> listarGastosDelTipo(Tipo tipoGasto, int anio) {
		double falsoImporte=0.0;
		String falsaDescripcion="Falsa descripcion";
		String falsaId="0000";
		List<Gasto> listaGastosDelTipo=new ArrayList<>();
		for(int i=1;i<13;i++) {
			String fechaString=null;
			if(i<10) {
				fechaString=String.valueOf(anio)+"-0"+String.valueOf(i)+"-10";
			} else {
				fechaString=String.valueOf(anio)+"-"+String.valueOf(i)+"-10";
			}
			LocalDate fechaGasto=LocalDate.parse(fechaString);
			listaGastosDelTipo.add(new Gasto(falsoImporte,tipoGasto,fechaGasto,falsaDescripcion,falsaId));
		}
		return listaGastosDelTipo;
	}

}
