package gestiongastos.vista;

import gestiongastos.controlador.ControladorMVC;
import gestiongastos.recursos.LocalizadorRecursos;
import gestiongastos.vista.controladores.ControladorVPrincipal;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import utilidades.Dialogos;

public class Vista extends Application{
	private static ControladorMVC controladorMVC;
	
	public void setControlador(ControladorMVC controlador) {
		controladorMVC=controlador;
	}

	public void comenzar() {
		launch(this.getClass());
	}

	public void salir() {
		controladorMVC.terminar();
	}

	@Override
	public void start(Stage escenarioPrincipal) {
		try {
			FXMLLoader cargadorVentanaPrincipal=new FXMLLoader(LocalizadorRecursos.class.getResource("/vistas/VPrincipal.fxml"));
			VBox raiz = cargadorVentanaPrincipal.load();
			ControladorVPrincipal cVentanaPrincipal=cargadorVentanaPrincipal.getController();
			cVentanaPrincipal.setControladorMVC(controladorMVC);
			cVentanaPrincipal.actualizaTablaUsuarios();
			cVentanaPrincipal.refrescar();
			
			Scene escena = new Scene(raiz);
			escenarioPrincipal.setTitle("Plantilla");
			escenarioPrincipal.setScene(escena);
			escenarioPrincipal.setOnCloseRequest(e -> confirmarSalida(escenarioPrincipal, e));
			escenarioPrincipal.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void confirmarSalida(Stage escenarioPrincipal, WindowEvent e) {
    	if(Dialogos.mostrarDialogoConfirmacion("Salir", "¿Seguro que desea cerrar la aplicación?", escenarioPrincipal)) {
    		controladorMVC.terminar();
    		escenarioPrincipal.close();
    	}
    	else {
    		e.consume();
    	}
	}
	
	
	
	
	
}
