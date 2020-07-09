package co.agro.blockchain.jsf.company.dto;

import java.io.Serializable;

public class EmpresaDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -280002964352125833L;

	private String idEmpresa;
	private String nombreEmpresa;
	private String razonSocialEmpresa;
	private String direccionPrincipal;
	private String privateKey;
	private String publicKey;

	public EmpresaDto() {
		// constructor basico
	}

	public EmpresaDto(final String idEmpresa, String nombreEmpresa) {
		this.idEmpresa = idEmpresa;
		this.nombreEmpresa = nombreEmpresa;
	}

	public String getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(String idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public String getNombreEmpresa() {
		return nombreEmpresa;
	}

	public void setNombreEmpresa(String nombreEmpresa) {
		this.nombreEmpresa = nombreEmpresa;
	}

	public String getRazonSocialEmpresa() {
		return razonSocialEmpresa;
	}

	public void setRazonSocialEmpresa(String razonSocialEmpresa) {
		this.razonSocialEmpresa = razonSocialEmpresa;
	}

	public String getDireccionPrincipal() {
		return direccionPrincipal;
	}

	public void setDireccionPrincipal(String direccionPrincipal) {
		this.direccionPrincipal = direccionPrincipal;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

}
