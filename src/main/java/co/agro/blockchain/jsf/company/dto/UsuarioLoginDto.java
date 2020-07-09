package co.agro.blockchain.jsf.company.dto;

import java.io.Serializable;

public class UsuarioLoginDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8277413743062150702L;
	private UsuarioLoginPkDto usuarioLoginPk;
	private RolDto rol;
	private String fechaCreacion;

	public UsuarioLoginPkDto getUsuarioLoginPk() {
		return usuarioLoginPk;
	}

	public void setUsuarioLoginPk(UsuarioLoginPkDto usuarioLoginPk) {
		this.usuarioLoginPk = usuarioLoginPk;
	}

	public RolDto getRol() {
		return rol;
	}

	public void setRol(RolDto rol) {
		this.rol = rol;
	}

	public String getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

}
