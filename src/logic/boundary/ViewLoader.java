package logic.boundary;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.Logger;

public class ViewLoader {
	private static final Logger logger = Logger.getLogger(ViewLoader.class.getName());
    private static Stage stage;

	private ViewLoader() {
	}
		
    public static void setStage(Stage primaryStage) {
        if (stage == null) {
        	stage = primaryStage;
        } else {
            logger.warning("Stage già impostato");
        }
    }
	public static void caricaView(ViewInfo viewInfo) {
		caricaView(viewInfo.getFxmlPath(), viewInfo.getTitle());
	}	

//	public static void caricaView2(ViewInfo viewInfo) {
//		Object controller = getControllerInstance(viewInfo);
//		if (controller == null) {
//			caricaView2(viewInfo.getFxmlPath(), viewInfo.getTitle(), controller);
//		} else {
//			logger.severe("Controller non trovato!");
//		}
//	}	

	private static void caricaView(String fxmlPath, String title) {
		try {
			FXMLLoader loader = new FXMLLoader(ViewLoader.class.getResource(fxmlPath));
			
	        if (Objects.equals(fxmlPath, ViewInfo.DETTAGLI_VIEW.getFxmlPath())) {
	            loader.setController(DettagliSegnalazioneViewController.getInstance());
	        }
			
			
			Parent root = loader.load();

			stage.setScene(new Scene(root));
			stage.setTitle(title);
			stage.show();
		} catch (IOException e) {
			logger.severe("Errore durante il caricamento della view: " + e.getMessage());
		}
	}


//	private static void caricaView2(String fxmlPath, String title, Object controller) {
//		try {
//			FXMLLoader loader = new FXMLLoader(ViewLoader.class.getResource(fxmlPath));
//			loader.setController(controller);
//			Parent root = loader.load();
//
//
//			stage.setScene(new Scene(root));
//			stage.setTitle(title);
//			stage.show();
//		} catch (IOException e) {
//			logger.severe("Errore durante il caricamento della view: " + e.getMessage());
//		}
//	}


//	private static Object getControllerInstance(ViewInfo viewInfo) {
//		switch (viewInfo) {
//		case LOGIN_VIEW:
//			return LoginViewController.getInstance();
//		case REGISTRAZIONE_VIEW:
//			return RegistrazioneViewController.getInstance();
//		case EFFETTUA_SEGNALAZIONE_VIEW:
//			return EffettuaSegnalazioneViewController.getInstance();
//		case RISCATTA_RICOMPENSA_VIEW:
//			return RiscattaRicompensaViewController.getInstance();
//		case STORICO_VIEW:
//			return StoricoViewController.getInstance();
//		case GESTISCI_SEGNALAZIONI_VIEW:
//			return GestisciSegnalazioniViewController.getInstance();
//		case ASSEGNA_PUNTI_VIEW:
//			return AssegnaPuntiViewController.getInstance();
//		case SEGNALAZIONI_ASSEGNATE_VIEW:
//			return SegnalazioniAssegnateViewController.getInstance();
//		case DETTAGLI_VIEW:
//			return DettagliSegnalazioneViewController.getInstance();
//		default:
//			return null;
//		}
//	}
}
