package co.agro.blockchain.jsf.company.dto;

import java.io.Serializable;

public class EstadoProductoDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7983825275811285844L;
	private String idEstadoProducto;
	private String descripcion;
	
	public EstadoProductoDto() {
		//Constructor basico
	}

	public String getIdEstadoProducto() {
		return idEstadoProducto;
	}

	public void setIdEstadoProducto(String idEstadoProducto) {
		this.idEstadoProducto = idEstadoProducto;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
}
