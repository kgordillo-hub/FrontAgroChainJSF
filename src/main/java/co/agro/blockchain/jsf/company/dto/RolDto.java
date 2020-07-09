package co.agro.blockchain.jsf.company.dto;

import java.io.Serializable;

public class RolDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 804251577574847897L;
	private String idRol;
	private String descripcion;

	public RolDto() {
		// Constructor por defecto
	}

	public String getIdRol() {
		return idRol;
	}

	public void setIdRol(String idRol) {
		this.idRol = idRol;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}
