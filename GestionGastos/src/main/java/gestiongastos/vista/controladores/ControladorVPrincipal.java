package gestiongastos.vista.controladores;


import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import javax.naming.OperationNotSupportedException;

import gestiongastos.controlador.ControladorMVC;
import gestiongastos.modelo.dominio.Gasto;
import gestiongastos.modelo.dominio.Tipo;
import gestiongastos.modelo.dominio.Usuario;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import utilidades.Dialogos;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

public class ControladorVPrincipal {
	private static ControladorMVC controladorMVC;
	private Usuario userEnTabla;

	public void setControladorMVC(ControladorMVC controlador) {
		controladorMVC=controlador;
	}
	
	/*
	 * Declaramos los ObservableList para las tablas y asignamos sus distintos atributos
	 */
	private ObservableList<Usuario> usuarios = FXCollections.observableArrayList();
	private ObservableList<Gasto> gastos = FXCollections.observableArrayList();
	
    @FXML private TableView<Usuario> tvUsuarios;
    	@FXML private TableColumn<Usuario, String> tcNombre;
    	@FXML private TableColumn<Usuario, String> tcSueldo;
    	
    @FXML private TableView<Gasto> tvGastos;
    	@FXML private TableColumn<Gasto, String> tcDescripcion;
    	@FXML private TableColumn<Gasto, String> tcFecha;
    	@FXML private TableColumn<Gasto, String> tcTipo;
    	@FXML private TableColumn<Gasto, String> tcImporte;

    /*
     * Método initialize() que inicializa las tablas asignando los valores a cada tabla y columna, los valores de los ComboBoxes, asigna los addListener
     * para comprobar el formato de los TextFields,
    */
	@FXML
	public void initialize() {
		tvUsuarios.setItems(usuarios);
			tcNombre.setCellValueFactory(usuario -> new SimpleStringProperty(usuario.getValue().getNombre()));
			tcSueldo.setCellValueFactory(usuario -> new SimpleStringProperty(String.valueOf(usuario.getValue().getSueldo()+"€")));
		
		tvGastos.setItems(gastos);
			tcDescripcion.setCellValueFactory(gasto -> new SimpleStringProperty(gasto.getValue().getDescripcion()));
			tcFecha.setCellValueFactory(gasto -> new SimpleStringProperty(gasto.getValue().getFechaFormateada()));
			tcTipo.setCellValueFactory(gasto -> new SimpleStringProperty(gasto.getValue().getTipoGasto().toString()));
			tcImporte.setCellValueFactory(gasto -> new SimpleStringProperty(String.valueOf(gasto.getValue().getImporte())+"€"));
		
		tfSueldoUser.textProperty().addListener((ob, ov, nv) -> comprobarFormato(ov, nv, tfSueldoUser));
		tfImporteGasto.textProperty().addListener((ob, ov, nv) -> comprobarFormato(ov, nv, tfImporteGasto));
		inicializarControles();
		
	}
	
	/*
	 * Método comprobarFormato, que solo permite escribir números o un punto para los TextFields de importe y sueldo
	 */
	public void comprobarFormato(String ov, String nv, TextField tf) {
		if (!nv.matches("\\d*\\.\\d*")) {
			tf.setText(nv.replaceAll("[^\\d\\.\\d]", ""));
		}
	}
	
	/*
	 * Método inicializarControles, que crea un array con los Tipo del enum Tipo y luego guarda en un ObservableList su .toString(). Después asigna
	 * dicho ObservableList a los ComboBoxes correspondientes. También asigna los meses del año a su respectivo ComboBox. Por último crea dos factorias 
	 * de valores para los spinners con el valor inicial y máximo como el año actual y las asigna.
	 */
	public void inicializarControles() {
		Tipo[] arrayListaTipos=Tipo.values();
		ObservableList<String> listaTipos = FXCollections.observableArrayList();
		for (Tipo t : arrayListaTipos) {
			listaTipos.add(t.toString());
		}
		cbTipoGasto.setItems(listaTipos);
		cbTipoPorTipos.setItems(listaTipos);
		
		cbMesPorMeses.setItems(FXCollections.observableArrayList("Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"));
		
		int anioActual=LocalDate.now().getYear();
		SpinnerValueFactory<Integer> factoriaSpinnerMeses=new SpinnerValueFactory.IntegerSpinnerValueFactory(0, anioActual, anioActual);
		spAnioPorMeses.setValueFactory(factoriaSpinnerMeses);
		SpinnerValueFactory<Integer> factoriaSpinnerTipos=new SpinnerValueFactory.IntegerSpinnerValueFactory(0, anioActual, anioActual);
		spAnioPorTipos.setValueFactory(factoriaSpinnerTipos);
		lbFraseAhorro.setVisible(false);
		tfAhorroMensual.setVisible(false);
	}
    
	/*
	 * Método actualizaTablaUsuarios, que actualiza la tabla de usuarios
	 */
	public void actualizaTablaUsuarios() {
		usuarios.setAll(controladorMVC.obtenerListaUsuarios());
	}
	
	/*
	 * Método actualizaTablaGastos, que actualiza la tabla de gastos con la lista de gastos del usuario pasado como parámetro
	 */
	public void actualizaTablaGastos(Usuario user) {
		gastos.setAll(controladorMVC.obtenerGastosUsuario(user));
	}
	
	/*
	 * Método refrescar, que actualiza los ComboBoxes que listan los usuarios disponibles, la tabla de los usuarios, limpia los
	 * TextFields y los DatePickers
	 */
	public void refrescar() {
		actualizaComboBoxUser();
		actualizaTablaUsuarios();
		tfNombreUser.clear();
		tfSueldoUser.clear();
		tfImporteGasto.clear();
		taDescripGasto.clear();
		dpFechaGasto.setValue(null);
	}
	
	/*
	 * Método actualizaComboBoxerUser, que obtiene la lista de todos los usuarios y crea un ObservableList en el que guarda sus .toString. Después
	 * asigna el ObservableList a los ComboBoxes correspondientes
	 */
	public void actualizaComboBoxUser() {
		List<Usuario> listaUsuarios=controladorMVC.obtenerListaUsuarios();
		ObservableList<String> nombresUsuarios = FXCollections.observableArrayList();
		for(Usuario u : listaUsuarios) {
			nombresUsuarios.add(u.toString());
		}
		cbUsuario.setItems(nombresUsuarios);
		cbListarGastos.setItems(nombresUsuarios);
		cbUsuarioPorMeses.setItems(nombresUsuarios);
		cbUsuarioPorTipos.setItems(nombresUsuarios);
	}
	
	/*
	 * Acción añadirUser, que dentro de un try crea un usuario con el contenido de los TextFields correspondientes, pasando a double el TexField del sueldo y
	 * después inserta dicho usuario. Captura cualquier excepción y la muestra mediante un diálogo. Por último, corre refrescar()
	 */
    @FXML
    void acAnadirUser(ActionEvent event) {
    	try {
    		if(tfSueldoUser.getText().isBlank()) {
    			throw new IllegalArgumentException("ERROR: El sueldo del usuario no puede estar en blanco");
    		}
    		Usuario user = new Usuario(tfNombreUser.getText(),Double.valueOf(tfSueldoUser.getText()));
			controladorMVC.insertarUsuario(user);
			
		} catch (Exception e) {
			Dialogos.mostrarDialogoAdvertencia("ERROR", e.getMessage());
			e.printStackTrace();
		}
    	refrescar();
    }
    
    /*
     * Acción añadirGasto, que dentro de un try crea un Array con los Tipo del enum Tipo y luego guarda el índice del Tipo seleccionado en su ComboBox,
     * después crea un usuario con el que se seleccione de su ComboBox y con un 0 en el sueldo, y crea un gasto recogiendo y formateando los valores de
     * los diversos TextFields y ComboBoxes. El Tipo lo obtiene del array creado previamente mediante el índice obtenido. Después procede a insertar el gasto
     * creado para el usuario creado(que solo servirá para comparar y que el modelo sepa en qué usuario insertar el gasto) y muestra un diálogo de confirmación 
     * si todo sale bien. Captura cualquier excepción y la muestra mediante un diálogo. Por último, corre refrescar()
     */
    @FXML
    void acAnadirGasto(ActionEvent event) {
    	try {
    		if(tfImporteGasto.getText().isBlank()) {
    			throw new IllegalArgumentException("ERROR: El importe del gasto no puede estar en blanco");
    		}
    		else if(cbTipoGasto.getSelectionModel().getSelectedIndex()<0) {
    			throw new IllegalArgumentException("ERROR: El tipo de gasto no puede ser nulo");
    		}
    		Tipo[] arrayListaTipos=Tipo.values();
    		int indiceTipo=cbTipoGasto.getSelectionModel().getSelectedIndex();
    		Usuario userGasto = new Usuario (cbUsuario.getSelectionModel().getSelectedItem(),0.0);
    		Gasto nuevoGasto = new Gasto (Double.valueOf(tfImporteGasto.getText()),arrayListaTipos[indiceTipo],dpFechaGasto.getValue(),taDescripGasto.getText());
    		controladorMVC.insertarGasto(userGasto, nuevoGasto);
    		if(userGasto.equals(userEnTabla)) {
    			actualizaTablaGastos(userEnTabla);
    		}
        	Dialogos.mostrarDialogoInformacion("Conseguido", "Gasto añadido con éxito");
        	refrescar();
    	} catch (Exception e) {
    		Dialogos.mostrarDialogoAdvertencia("ERROR", e.getMessage());
			e.printStackTrace();
    	}
    }
    
    /*
     * Acción listarGastos, que crea un nuevo usuario con el seleccionado de su ComboBox y sueldo 0, guarda dicho usuario en userEnTabla para poder recuperarlo
     * en el futuro y corre actualizaTablaGastos con el usuario creado. Captura cualquier excepción y la muestra mediante un diálogo. Por último, corre refrescar()
     */
    @FXML
    void acListarGastos(ActionEvent event) {
    	try {
    		Usuario userGasto = new Usuario(cbListarGastos.getSelectionModel().getSelectedItem(),0.0);
    		userEnTabla=userGasto;
    		actualizaTablaGastos(userGasto);
    	} catch (Exception e) {
    		Dialogos.mostrarDialogoAdvertencia("ERROR", e.getMessage());
			e.printStackTrace();
    	}

    }
    
    @FXML
    void acBorrarUsuario(ActionEvent event) {
    	try {
    		if(tvUsuarios.getSelectionModel().getSelectedItem()==null) {
    			throw new NullPointerException("ERROR: Ningún usuario seleccionado");
    		}
    		else if (Dialogos.mostrarDialogoConfirmacion("Borrar usuario", "¿Estás seguro de que quieres borrar este usuario?", null)) {
    			Usuario userBorrado=tvUsuarios.getSelectionModel().getSelectedItem();
    			controladorMVC.borrarUsuario(userBorrado);
    			if(userBorrado.equals(userEnTabla)) {
    				gastos.clear();
    			}
    			refrescar();
    		}
    	} catch (Exception e) {
    		Dialogos.mostrarDialogoAdvertencia("ERROR", e.getMessage());
			e.printStackTrace();
    	}
    	
    }
    
    @FXML
    void acBorrarGasto(ActionEvent event) {
    	try {
    		if(tvGastos.getSelectionModel().getSelectedItem()==null) {
    			throw new NullPointerException("ERROR: Ningún gasto seleccionado");
    		}
    		else if (Dialogos.mostrarDialogoConfirmacion("Borrar gasto", "¿Estás seguro de que quieres borrar este gasto?", null)) {
    			Gasto gastoBorrado=tvGastos.getSelectionModel().getSelectedItem();
    			Usuario usuarioPropietario=userEnTabla;
    			controladorMVC.borrarGasto(usuarioPropietario, gastoBorrado);
    			actualizaTablaGastos(userEnTabla);
    	    	refrescar();
    		}
    	} catch (Exception e) {
    		Dialogos.mostrarDialogoAdvertencia("ERROR", e.getMessage());
			e.printStackTrace();
    	}
    	
    }
    
    public LocalDate obtenerFechaMes(int mes) {
    	String cadenaMes=null;
    	LocalDate fechaMes=null;
    	if (mes<10) {
    		cadenaMes="2022-0"+String.valueOf(mes)+"-10";
    	} else {
    		cadenaMes="2022-"+String.valueOf(mes)+"-10";
    	}
    	fechaMes=LocalDate.parse(cadenaMes);
    	return fechaMes;
    }
    
    public LocalDate obtenerFechaAnio(String anio) {
    	String cadenaAnio=anio+"-10-10";
    	LocalDate fechaAnio=LocalDate.parse(cadenaAnio);
    	return fechaAnio;
    }
    
    @FXML
    void acGraficoTarta(ActionEvent event) {
    	try {
    		if(cbMesPorMeses.getSelectionModel().getSelectedIndex()<0) {
    			throw new IllegalArgumentException("ERROR: El mes no puede ser nulo");
    		}
    		Usuario user = new Usuario (cbUsuarioPorMeses.getSelectionModel().getSelectedItem(),0.0);
    		LocalDate fechaMes=obtenerFechaMes(cbMesPorMeses.getSelectionModel().getSelectedIndex()+1);
    		LocalDate fechaAnio=obtenerFechaAnio(String.valueOf(spAnioPorMeses.getValue()));
    		List<Gasto> conjuntoGastos=controladorMVC.obtenerGastosDelMes(user, fechaMes, fechaAnio);
    		int totalGastos=0;
    		for(Gasto g : conjuntoGastos) {
    			totalGastos=(int) (totalGastos+g.getImporte());
    		}
    		PieChart graficoTarta=obtenerGraficoTarta(conjuntoGastos, totalGastos);
    		graficoTarta.setTitle("Gastos del mes "+cbMesPorMeses.getSelectionModel().getSelectedItem()+" del año "+String.valueOf(spAnioPorMeses.getValue()));
    		graficoTarta.setLegendVisible(false);
    		vbGrafico.getChildren().setAll(graficoTarta);
    		calcularAhorroMensual(user, totalGastos);
    	} catch (Exception e) {
    		Dialogos.mostrarDialogoAdvertencia("ERROR", e.getMessage());
			e.printStackTrace();
    	}
    }
    
    public PieChart obtenerGraficoTarta(List<Gasto> conjuntoGastos, int totalGastos) {
    	PieChart graficoTarta=null;
    	try {
    		ObservableList<PieChart.Data> datos = FXCollections.observableArrayList();
    		for(Gasto g : conjuntoGastos) {
    			Data quesito = new PieChart.Data(g.getTipoGasto().toString(),(int) (g.getImporte()*100/totalGastos));
    			datos.add(quesito);
    			quesito.setName(g.getTipoGasto().toString()+": "+String.valueOf(g.getImporte()+"€"));
    		}
    		graficoTarta= new PieChart(datos);
    	}catch (Exception e) {
    		Dialogos.mostrarDialogoAdvertencia("ERROR", e.getMessage());
			e.printStackTrace();
    	}
    	return graficoTarta;
    }
    
    public void calcularAhorroMensual(Usuario user, int totalGastos) {
		try {
	    	double ahorroMensual=controladorMVC.obtenerUsuario(user).getSueldo()-totalGastos;
			tfAhorroMensual.setText(String.valueOf(ahorroMensual)+"€");
			if(ahorroMensual<0) {
				tfAhorroMensual.setTextFill(Color.RED);
			} else {
				tfAhorroMensual.setTextFill(Color.GREEN);
			}
			lbFraseAhorro.setVisible(true);
			tfAhorroMensual.setVisible(true);
		}catch (Exception e) {
    		Dialogos.mostrarDialogoAdvertencia("ERROR", e.getMessage());
			e.printStackTrace();
    	}
    }
    
    @FXML
    void acGraficoBarras(ActionEvent event) {
    	try {
    		if(cbTipoPorTipos.getSelectionModel().getSelectedIndex()<0) {
    			throw new IllegalArgumentException("ERROR: El tipo de gasto no puede ser nulo");
    		}
    		Tipo[] arrayListaTipos=Tipo.values();
    		Usuario user = new Usuario (cbUsuarioPorTipos.getSelectionModel().getSelectedItem(),0.0);
    		Tipo tipoGasto=arrayListaTipos[cbTipoPorTipos.getSelectionModel().getSelectedIndex()];
    		int anio=spAnioPorTipos.getValue();
    		List<Gasto> conjuntoGastos=controladorMVC.obtenerGastosDelTipo(user, tipoGasto, anio);
    		BarChart graficoBarras=obtenerGraficoBarras(conjuntoGastos);
    		graficoBarras.setLegendVisible(false);
    		graficoBarras.setTitle("Gastos en "+tipoGasto.toString().toLowerCase()+" para el año "+String.valueOf(anio));
			lbFraseAhorro.setVisible(false);
			tfAhorroMensual.setVisible(false);
    		vbGrafico.getChildren().setAll(graficoBarras);
    	} catch (Exception e) {
    		Dialogos.mostrarDialogoAdvertencia("ERROR", e.getMessage());
			e.printStackTrace();
    	}
    }
    
    public BarChart obtenerGraficoBarras(List<Gasto> conjuntoGastos) {
    	CategoryAxis xAxis = new CategoryAxis();
		NumberAxis yAxis = new NumberAxis();
		BarChart graficoBarras = new BarChart(xAxis, yAxis);
    	try {
    		ObservableList<XYChart.Series<String, Double>> datos = FXCollections.observableArrayList();
    		XYChart.Series<String, Double> meses = new XYChart.Series<>();
    		int indice=0;
    		for(Gasto g : conjuntoGastos) {
    			meses.getData().addAll(new XYChart.Data<>(mesCastellano(indice), g.getImporte()));
    			indice++;
    		}
    		datos.add(meses);
    		graficoBarras.setData(datos);
    	} catch (Exception e) {
    		Dialogos.mostrarDialogoAdvertencia("ERROR", e.getMessage());
			e.printStackTrace();
    	}
    	return graficoBarras;
    }
    
    public String mesCastellano(int indice) {
    	String mesCastellano=null;
    	switch (indice) {
    	case 0:
    		mesCastellano="Enero";
    		break;
    	case 1:
    		mesCastellano="Febrero";
    		break;
    	case 2:
    		mesCastellano="Marzo";
    		break;
    	case 3:
    		mesCastellano="Abril";
    		break;
    	case 4:
    		mesCastellano="Mayo";
    		break;
    	case 5:
    		mesCastellano="Junio";
    		break;
    	case 6:
    		mesCastellano="Julio";
    		break;
    	case 7:
    		mesCastellano="Agosto";
    		break;
    	case 8:
    		mesCastellano="Septiembre";
    		break;
    	case 9:
    		mesCastellano="Octubre";
    		break;
    	case 10:
    		mesCastellano="Noviembre";
    		break;
    	case 11:
    		mesCastellano="Diciembre";
    		break;
    	}
    	return mesCastellano;
    }


	
	@FXML
    private ComboBox<String> cbListarGastos;

    @FXML
    private ComboBox<String> cbMesPorMeses;

    @FXML
    private ComboBox<String> cbTipoGasto;

    @FXML
    private ComboBox<String> cbTipoPorTipos;

    @FXML
    private ComboBox<String> cbUsuario;

    @FXML
    private ComboBox<String> cbUsuarioPorMeses;

    @FXML
    private ComboBox<String> cbUsuarioPorTipos;

    @FXML
    private DatePicker dpFechaGasto;

    @FXML
    private Label lbFraseAhorro;

    @FXML
    private Spinner<Integer> spAnioPorMeses;

    @FXML
    private Spinner<Integer> spAnioPorTipos;

    @FXML
    private TextArea taDescripGasto;

    @FXML
    private Label tfAhorroMensual;

    @FXML
    private TextField tfImporteGasto;

    @FXML
    private TextField tfNombreUser;

    @FXML
    private TextField tfSueldoUser;

    @FXML
    private VBox vbGrafico;

    






    
    
    
    
  
}

