package logic.controller;

import java.util.logging.Logger;

import logic.model.dao.DaoFactory;
import logic.model.dao.UtenteDao;
import logic.beans.CredenzialiBean;
import logic.model.domain.Ruolo;
import logic.model.domain.Utente;
import logic.model.domain.LoggedUser;

public class LoginController { 
	private static volatile LoginController instance;
	private static Utente utente = null;
	private UtenteDao utenteDao;
	private static UtenteFactory utenteFactory = new UtenteFactory();
	private static LoggedUser utenteCorrente=LoggedUser.getInstance();

	private static final Logger logger = Logger.getLogger(LoginController.class.getName());

	private LoginController() {
		try {
			utenteDao = DaoFactory.getDao(UtenteDao.class);
		} catch (Exception e) {
			logger.severe("Errore durante l'inizializzazione del DAO: " + e.getMessage());
		}
	}

	public static LoginController getInstance() {
		LoginController result = instance;

		if (instance == null) {
			// blocco sincronizzato
			synchronized (LoginController.class) {
				result = instance;
				if (result == null) {
					instance = result = new LoginController();
				}
			}
		}

		return result;
	}

	public int effettuaLogin(CredenzialiBean credenzialiBean) {
		try {
			String username = credenzialiBean.getUsername();
			String password = credenzialiBean.getPassword();
			

			int ruoloId = utenteDao.autenticazione(username, password);
			int idUtente = utenteDao.getIdByUsername(username);

			setUtente(idUtente, username, Ruolo.fromInt(ruoloId));
			return 1;

		} catch (Exception e) {
			logger.severe("Errore durante la fase di login.");
			return -1;
		}

	}

	private static void setUtente(int idUtente, String username, Ruolo ruolo) {
		if (ruolo == null) {
			throw new IllegalArgumentException("ruolo non può essere null");
		}
		utente = utenteFactory.createUtente(idUtente, username, ruolo);	
		utenteCorrente.setUtente(utente);
		
	}
	


	public String ottieniView(int interfacciaSelezionata) { // restituisce la view iniziale da caricare
		return utente.getViewIniziale(interfacciaSelezionata);
	}

}
