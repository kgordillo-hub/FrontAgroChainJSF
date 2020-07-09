package co.agro.blockchain.jsf.company.dto;

import java.io.Serializable;


public class UsuarioLoginPkDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7328544047654177555L;
	/**
	 * 
	 */

	private DatosUsuarioDto idUsuario;
	private String username;
	
	public UsuarioLoginPkDto() {
		//Constructor basico
	}


	public DatosUsuarioDto getIdUsuario() {
		return idUsuario;
	}


	public void setIdUsuario(DatosUsuarioDto idUsuario) {
		this.idUsuario = idUsuario;
	}


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
}
