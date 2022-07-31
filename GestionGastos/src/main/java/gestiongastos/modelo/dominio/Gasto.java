package gestiongastos.modelo.dominio;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import utilidades.RandomIdGenerator;

public class Gasto implements Serializable,Comparable{
	private double importe;
	private Tipo tipoGasto;
	private LocalDate fecha;
	private String descripcion;
	private String id;
	public static DateTimeFormatter FORMATO_FECHA=DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
	/*
	 * Getters y Setters validando las diversas excepciones
	 */
	public double getImporte() {
		return importe;
	}
	public void setImporte(double importe) {
		if(importe<0) {
			throw new IllegalArgumentException("ERROR: El importe del gasto no puede ser negativo");
		}
		this.importe = importeFormateado(importe);
	}
	public Tipo getTipoGasto() {
		return tipoGasto;
	}
	private void setTipoGasto(Tipo tipoGasto) {
		if(tipoGasto==null) {
			throw new NullPointerException("ERROR: El tipo de gasto no puede ser nulo");
		}
		this.tipoGasto = tipoGasto;
	}
	public LocalDate getFecha() {
		return fecha;
	}
	public String getFechaFormateada() {
		return fecha.format(FORMATO_FECHA);
	}
	private void setFecha(LocalDate fecha) {
		if(fecha==null) {
			throw new NullPointerException("ERROR: La fecha del gasto no puede ser nula");
		}
		this.fecha = fecha;
	}
	public String getDescripcion() {
		return descripcion;
	}
	private void setDescripcion(String descripcion) {
		if(descripcion==null) {
			throw new NullPointerException("ERROR: La descripción del gasto no puede ser nula");
		}
		else if (descripcion.isBlank()) {
			throw new IllegalArgumentException("ERROR: La descripción del gasto no puede estar en blanco");
		}
		this.descripcion = descripcion;
	}
	public String getId() {
		return id;
	}
	private void setId(String id) {
		if(id==null) {
			throw new NullPointerException("ERROR: La id del gasto no puede ser nula");
		}
		else if (id.isBlank()) {
			throw new IllegalArgumentException("ERROR: La id del gasto no puede estar en blanco");
		}
		this.id=id;
	}
	
	/*
	 * Constructor que genera un id aleatorio y lo asigna
	 */
	public Gasto(double importe, Tipo tipoGasto, LocalDate fecha, String descripcion) {
		setImporte(importe);
		setTipoGasto(tipoGasto);
		setFecha(fecha);
		setDescripcion(descripcion);
		this.id=RandomIdGenerator.crear(10);
	}
	
	/*
	 * Constructor que recibe un id específico y lo asigna
	 */
	public Gasto(double importe, Tipo tipoGasto, LocalDate fecha, String descripcion, String id) {
		setImporte(importe);
		setTipoGasto(tipoGasto);
		setFecha(fecha);
		setDescripcion(descripcion);
		setId(id);
	}
	
	/*
	 * Constructor copia validando null
	 */
	public Gasto (Gasto g) {
		if(g==null) {
			throw new NullPointerException("ERROR: No se pude copiar un gasto nulo");
		}
		setImporte(g.getImporte());
		setTipoGasto(g.getTipoGasto());
		setFecha(g.getFecha());
		setDescripcion(g.getDescripcion());
		setId(g.getId());
	}
	
	/*
	 * Método importeFormateado(), que deja con solo 2 decimales el importe que recibe
	 */
	private double importeFormateado(double importe) {
		int importeSinDecimales=(int) (importe*100);
		double importeFinal=(double) importeSinDecimales/100;
		return importeFinal;
	}

	
	/*
	 * hashCode y equals que determinan que dos objetos son iguales cuando todos sus atributos coinciden
	 */
	@Override
	public int hashCode() {
		return Objects.hash(descripcion, fecha, id, importe, tipoGasto);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Gasto other = (Gasto) obj;
		return Objects.equals(descripcion, other.descripcion) && Objects.equals(fecha, other.fecha)
				&& Objects.equals(id, other.id)
				&& Double.doubleToLongBits(importe) == Double.doubleToLongBits(other.importe)
				&& tipoGasto == other.tipoGasto;
	}
	@Override
	public int compareTo(Object o) {
		int retorno=0;
		if (this.equals(o)){
			retorno=0;
		} else {
			retorno=1;
		}
		return retorno;
	}
	
//	@Override
//	public int compareTo(Gasto o) {
//		int retorno=0;
//		if (this.equals(o)) {
//			retorno=0;
//		} else {
//			retorno=1;
//		}
//		return retorno;
//	}


	
	
	
	
	

}
