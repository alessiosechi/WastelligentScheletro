package boundary;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import controller.LoginController;
import controller.RisolviSegnalazioneController;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.domain.AssegnazioneBean;
import model.domain.SegnalazioneBean;
import model.domain.UtenteBean;

public class GestisciSegnalazioniViewController {

    @FXML
    private Button exitButton;
    @FXML
    private Button assegnaButton;
    @FXML
    private Button assegnaPuntiButton;
    @FXML
    private Button vediDettagliButton;
    @FXML
    private Button eliminaButton;
    
    @FXML
    private TableView<SegnalazioneBean> segnalazioniTable; 
    @FXML
    private TableColumn<SegnalazioneBean, Integer> idColumn;
    @FXML
    private TableColumn<SegnalazioneBean, String> descrizioneColumn;
    @FXML
    private TableColumn<SegnalazioneBean, String> posizioneColumn;
    @FXML
    private TableColumn<SegnalazioneBean, String> statoColumn;
    
    @FXML
    private ComboBox<UtenteBean> operatoriEcologiciComboBox;

	
    private static GestisciSegnalazioniViewController instance;
	private DettagliSegnalazioneViewController dettagliSegnalazioneViewController = DettagliSegnalazioneViewController.getInstance();
    private RisolviSegnalazioneController risolviSegnalazioneController = RisolviSegnalazioneController.getInstance();
    private LoginController loginController = LoginController.getInstance();
    private Stage primaryStage;
	private static final Logger logger = Logger.getLogger(GestisciSegnalazioniViewController.class.getName());
    
	private void configureButtons() {
	    exitButton.setOnAction(event -> ViewLoader.caricaView("LoginView.fxml", primaryStage));
	    vediDettagliButton.setDisable(true);
	    eliminaButton.setDisable(true);
	}
	private void configureColumns() {
	    idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdSegnalazione()).asObject());
	    descrizioneColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescrizione()));
	    posizioneColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPosizione()));
	    statoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStato()));
	}
	private void configureTableSelection() {
	    segnalazioniTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> 
	        handleTableSelection(newValue)
	    );
	}
	private void handleTableSelection(SegnalazioneBean newValue) {
	    if (newValue != null) {
	        vediDettagliButton.setDisable(false);
	        eliminaButton.setDisable(false);
	        dettagliSegnalazioneViewController.setSegnalazioneBean(newValue);
	        dettagliSegnalazioneViewController.setState(new GestisciSegnalazioniViewState());
	    } else {
	        vediDettagliButton.setDisable(true);
	    }
	}
	private void configureHandlers() {
	    vediDettagliButton.setOnAction(event -> ViewLoader.caricaView("DettagliSegnalazioneView.fxml", primaryStage));
	    eliminaButton.setOnAction(event -> handleEliminaAction());
	    assegnaButton.setOnAction(event -> handleAssegnaAction());
	    assegnaPuntiButton.setOnAction(event -> ViewLoader.caricaView("AssegnaPuntiView.fxml", primaryStage));
	}
	private void handleEliminaAction() {
	    Optional<ButtonType> result = showAlert("Conferma Eliminazione", "Sei sicuro di voler eliminare la segnalazione selezionata?");
	    if (result.isPresent() && result.get() == ButtonType.OK) {
	        SegnalazioneBean segnalazioneSelezionata = segnalazioniTable.getSelectionModel().getSelectedItem();
	        if (segnalazioneSelezionata != null) {
	            risolviSegnalazioneController.eliminaSegnalazione(segnalazioneSelezionata.getIdSegnalazione());
	            ViewLoader.caricaView("GestisciSegnalazioniView.fxml", primaryStage);
	        } else {
	            logger.info("Nessuna segnalazione selezionata.");
	        }
	    } else {
	        logger.info("Eliminazione annullata.");
	    }
	}

	private void handleAssegnaAction() {
	    UtenteBean operatoreSelezionato = operatoriEcologiciComboBox.getSelectionModel().getSelectedItem();
	    SegnalazioneBean segnalazioneSelezionata = segnalazioniTable.getSelectionModel().getSelectedItem();
	    if (operatoreSelezionato != null && segnalazioneSelezionata != null) {
	        UtenteBean utente = loginController.getUtente();

	        AssegnazioneBean assegnazioneBean = new AssegnazioneBean();
	        assegnazioneBean.setSegnalazione(segnalazioneSelezionata);
	        assegnazioneBean.setOperatore(operatoreSelezionato);
	        assegnazioneBean.setEsperto(utente);
	        
	        if (risolviSegnalazioneController.assegnaOperatore(assegnazioneBean)) {
	            logger.info("Segnalazione assegnata con successo a " + operatoreSelezionato.getUsername());
	            ViewLoader.caricaView("GestisciSegnalazioniView.fxml", primaryStage);
	        } else {
	            showAlert("Errore Assegnazione", "Si è verificato un errore durante l'assegnazione della segnalazione.");
	        }
	    } else {
	        showAlert("Selezione Mancante", "Seleziona sia un operatore che una segnalazione per procedere con l'assegnazione.");
	    }
	}
    
    public void initialize() {
    	
        configureButtons();
        configureColumns();
    	
		caricaSegnalazioniDaRisolvere();
	    caricaOperatoriEcologici();
    	

        configureTableSelection();
        configureHandlers();
    	

		



		
    }

    private void caricaSegnalazioniDaRisolvere() {
        List<SegnalazioneBean> segnalazioniDaRisolvere = risolviSegnalazioneController.getSegnalazioniRicevute();
  
        ObservableList<SegnalazioneBean> segnalazioniData = FXCollections.observableArrayList(segnalazioniDaRisolvere);
        segnalazioniTable.setItems(segnalazioniData);
    }
    

    private void caricaOperatoriEcologici() {
    	
        // ottengo la lista di operatori dal controller applicativo
        List<UtenteBean> operatoriEcologici = risolviSegnalazioneController.getOperatoriEcologiciDisponibili();

        ObservableList<UtenteBean> operatori = FXCollections.observableArrayList(operatoriEcologici);
        operatoriEcologiciComboBox.setItems(operatori);
        

        operatoriEcologiciComboBox.setCellFactory(cell -> new ListCell<UtenteBean>() {
            @Override
            protected void updateItem(UtenteBean item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getUsername());
            }
        });
        

        operatoriEcologiciComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(UtenteBean item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "Seleziona Operatore" : item.getUsername());
            }
        });

    }


    public static GestisciSegnalazioniViewController getInstance() {
        if (instance == null) {
            instance = new GestisciSegnalazioniViewController();
        }
        return instance;
    }
    
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    
    
    
    private Optional<ButtonType> showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        return alert.showAndWait();
    }

}
