package co.agro.blockchain.jsf.company.dto;

import java.io.Serializable;

public class DatosUsuarioDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6964224541387288417L;
	private String idUsuario;
	private EmpresaDto empresa;
	private String nombreUsario;
	private String direccion;
	private String telefono;
	private String codDepartamento;
	private String codMunicipio;
	private String descripcion;
	private String fechaStr;
	private Short estadoUsuario;
	
	public DatosUsuarioDto() {
		//constructor basico
	}

	public String getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getNombreUsario() {
		return nombreUsario;
	}

	public void setNombreUsario(String nombreUsario) {
		this.nombreUsario = nombreUsario;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getCodDepartamento() {
		return codDepartamento;
	}

	public void setCodDepartamento(String codDepartamento) {
		this.codDepartamento = codDepartamento;
	}

	public String getCodMunicipio() {
		return codMunicipio;
	}

	public void setCodMunicipio(String codMunicipio) {
		this.codMunicipio = codMunicipio;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


	public String getFechaStr() {
		return fechaStr;
	}

	public void setFechaStr(String fechaStr) {
		this.fechaStr = fechaStr;
	}

	public Short getEstadoUsuario() {
		return estadoUsuario;
	}

	public void setEstadoUsuario(Short estadoUsuario) {
		this.estadoUsuario = estadoUsuario;
	}

	public EmpresaDto getEmpresa() {
		return empresa;
	}

	public void setEmpresa(EmpresaDto empresa) {
		this.empresa = empresa;
	}
	
}
