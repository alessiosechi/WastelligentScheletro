package logic.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import logic.model.domain.EspertoEcologico;
import logic.model.domain.OperatoreEcologico;
import logic.model.domain.Ruolo;
import logic.model.domain.Utente;
import logic.model.domain.UtenteBase;

public class UtenteDaoInMemory implements UtenteDao {

	private static final Map<String, Utente> utentiInMemory = new HashMap<>();
	private static final Map<String, String> usernamePasswordMap = new HashMap<>();

//	private static final Map<String, String> OPERATORI_ECOLOGICI = Map.of("luca.bianchi11", "luca.bianchi",
//			"mario.rossi9", "mariorossi9", "giovanni.verdi7", "giovanni7");
//
//	private static final Map<String, String> ESPERTI_ECOLOGICI = Map.of("luca2", "2", "sara.verdi3", "sara3",
//			"alessandro.rossi5", "alex5");
//
//	static {
//
//		for (Map.Entry<String, String> entry : ESPERTI_ECOLOGICI.entrySet()) {
//			String username = entry.getKey();
//			String password = entry.getValue();
//			EspertoEcologico esperto = new EspertoEcologico(utentiInMemory.size() + 1, username);
//			utentiInMemory.put(username, esperto);
//			usernamePasswordMap.put(username, password);
//		}
//
//		for (Map.Entry<String, String> entry : OPERATORI_ECOLOGICI.entrySet()) {
//			String username = entry.getKey();
//			String password = entry.getValue();
//			OperatoreEcologico operatore = new OperatoreEcologico(utentiInMemory.size() + 1, username);
//			utentiInMemory.put(username, operatore);
//			usernamePasswordMap.put(username, password);
//		}
//	}

	@Override
	public int autenticazione(String username, String password) {
		// controllo se l'utente esiste nella mappa e se la password è corretta
		if (usernamePasswordMap.containsKey(username) && usernamePasswordMap.get(username).equals(password)) {
			Utente utente = utentiInMemory.get(username);
			if (utente instanceof UtenteBase) {
				return Ruolo.UTENTE_BASE.getId();
			} else if (utente instanceof EspertoEcologico) {
				return Ruolo.ESPERTO_ECOLOGICO.getId();
			} else if (utente instanceof OperatoreEcologico) {
				return Ruolo.OPERATORE_ECOLOGICO.getId();
			}
		}
		return -1;
	}

	@Override
	public int getIdByUsername(String username) {

		Utente utente = utentiInMemory.get(username);
		if (utente != null) {
			return utente.getIdUtente();
		}
		return -1;
	}

	@Override
	public boolean registraUtente(String username, String password) {
		// controllo se lo username è già stato preso
		if (isUsernameTaken(username)) {
			return false;
		}

		Utente nuovoUtente = new UtenteBase(utentiInMemory.size() + 1, username);
		utentiInMemory.put(username, nuovoUtente);
		usernamePasswordMap.put(username, password);
		return true;
	}

	@Override
	public boolean isUsernameTaken(String username) {
		return utentiInMemory.containsKey(username);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Utente> List<T> getUtentiByRuolo(Ruolo ruolo) {
	    List<T> utentiFiltrati = new ArrayList<>();

	    // mappo il ruolo e la classe corrispondente
	    Map<Ruolo, Class<? extends Utente>> ruoloToClasse = Map.of(
	        Ruolo.UTENTE_BASE, UtenteBase.class,
	        Ruolo.ESPERTO_ECOLOGICO, EspertoEcologico.class,
	        Ruolo.OPERATORE_ECOLOGICO, OperatoreEcologico.class
	    );

	    // trovo la classe corrispondente al ruolo
	    Class<? extends Utente> classeCorrispondente = ruoloToClasse.get(ruolo);
	    if (classeCorrispondente == null) {
	        throw new IllegalArgumentException("Ruolo sconosciuto: " + ruolo);
	    }

	    // filtro gli utenti in base alla classe
	    for (Utente utente : utentiInMemory.values()) {
	        if (classeCorrispondente.isInstance(utente)) {
	            utentiFiltrati.add((T) utente); // cast a T
	        }
	    }

	    return utentiFiltrati;
	}




}
