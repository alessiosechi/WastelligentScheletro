package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.dao.queries.LoginQueries;

public class LoginDAOImplementazione implements LoginDAO{
	private static volatile LoginDAOImplementazione instance;



	private LoginDAOImplementazione() {
	}
	

	public static LoginDAOImplementazione getInstance() {
		LoginDAOImplementazione result = instance;

		if (instance == null) {
			synchronized (LoginDAOImplementazione.class) {
				result = instance;
				if (result == null) {
					instance = result = new LoginDAOImplementazione();
				}

			}
		}

		return result;
	}
	
	
	
	
	
	public int autenticazione(String username, String password) {

	    ResultSet rs = null;

	    try {
	        rs = LoginQueries.login(username, password);
	        
	        if (rs.next()) {
	            String ruolo = rs.getString("nome");
	            switch (ruolo) {
	                case "UTENTE_BASE":
	                    return 1;
	                case "ESPERTO_ECOLOGICO":
	                    return 2;
	                case "OPERATORE_ECOLOGICO":
	                    return 3;
	                default:
	                    throw new IllegalArgumentException("Ruolo sconosciuto: " + ruolo);
	            }
	        } else {
	            // Username o password non validi
	            return -1;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return -1;
	    } finally {
	        try {
	            if (rs != null) rs.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}

	
	
	
	
    @Override
    public int getIdByUsername(String username) {
        Connection connessione = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            connessione = DBConnection.getConnection();
            String sql = "SELECT id_utente FROM utenti WHERE username = ?";
            stmt = connessione.prepareStatement(sql);
            stmt.setString(1, username);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id_utente");
            } else {
                return -1; // Username non trovato
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public int registraUtente(String username, String password) {
        Connection connessione = null;
        PreparedStatement stmt = null;

        try {
            connessione = DBConnection.getConnection();
            String sql = "INSERT INTO utenti (username, password_hash, tipo_utente) VALUES (?, ?, ?)";

            stmt = connessione.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password); 
            stmt.setInt(3, 1); // imposto il ruolo di UTENTE_BASE

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0 ? 1 : -1;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    
    
    
    
    @Override
    public boolean isUsernameTaken(String username) {
        Connection connessione = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            connessione = DBConnection.getConnection();
            String sql = "SELECT COUNT(*) AS username_count FROM utenti WHERE username = ?";
            stmt = connessione.prepareStatement(sql);
            stmt.setString(1, username);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("username_count") > 0; // Restituisce true se il conteggio è maggiore di zero
            } else {
                return false; // Se non ci sono risultati, significa che l'username non è stato trovato
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Restituisce false in caso di errore
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    
    
    
    
    
    
    

}
