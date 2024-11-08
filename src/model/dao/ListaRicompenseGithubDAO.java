package model.dao;

import model.domain.Ricompensa;
import java.util.List;

import exceptions.CodiceRiscattoNonTrovatoException;
import exceptions.ConnessioneAPIException;

public interface ListaRicompenseGithubDAO {
	List<Ricompensa> getRicompense()throws ConnessioneAPIException;
	String getCodiceRiscatto(int idRicompensa)throws ConnessioneAPIException, CodiceRiscattoNonTrovatoException;

}
