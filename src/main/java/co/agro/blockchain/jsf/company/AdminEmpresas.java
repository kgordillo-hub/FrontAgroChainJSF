package co.agro.blockchain.jsf.company;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.event.RowEditEvent;

import com.google.gson.GsonBuilder;

import co.agro.blockchain.jsf.company.dto.EmpresaDto;
import co.agro.blockchain.jsf.rest.client.RestClient;
import co.agro.blockchain.jsf.utils.Utils;

@Named
@RequestScoped
public class AdminEmpresas implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4501837320890501721L;
	private String idEmpresa;
	private String nombreEmpresa;
	private String razonSocialEmpresa;
	private String direccionPrincipal;

	private List<EmpresaDto> listaEmpresas;

	public void guardarEmpresa() {

	}

	public AdminEmpresas() {
		try {
			inicializarEmpresas();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void inicializarEmpresas() throws IOException {
		final String serviceUrl = Utils.getProperty("endPointEmpresas");
		final RestClient restClient = new RestClient();
		final String resultService = restClient.consumeRestServiceGet(serviceUrl, null);
		if (resultService != null) {
			listaEmpresas = Arrays.asList(new GsonBuilder().create().fromJson(resultService, EmpresaDto[].class));
		}

	}

	public void save() {
		try {
			if (idEmpresa != null && !idEmpresa.isEmpty() && nombreEmpresa != null && !nombreEmpresa.isEmpty()) {

				final RestClient restClient = new RestClient();
				final String serviceUrl = Utils.getProperty("endPointEmpresas") + "/add";
				final EmpresaDto empresa = new EmpresaDto();
				empresa.setDireccionPrincipal(direccionPrincipal);
				empresa.setIdEmpresa(idEmpresa);
				empresa.setNombreEmpresa(nombreEmpresa);
				empresa.setRazonSocialEmpresa(razonSocialEmpresa);
				final String respuestaServer = restClient.consumeRestServicePost(serviceUrl, empresa);
				if (respuestaServer != null) {
					if (respuestaServer.toLowerCase().contains("correcta")) {
						PrimeFaces.current().ajax().addCallbackParam("infoCorrect", true);
						inicializarEmpresas();
						info("Empresa guardada: " + respuestaServer);
					}else {
						PrimeFaces.current().ajax().addCallbackParam("infoCorrect", false);
						warn("No se pudo guardar la empresa: " + respuestaServer);
					}
				} else {
					PrimeFaces.current().ajax().addCallbackParam("infoCorrect", false);
					error("No se ha podido guardar la empresa error de conexión");
				}

			} else {
				PrimeFaces.current().ajax().addCallbackParam("infoCorrect", false);
			}
			cleanFields();
		} catch (final Exception e) {
			e.printStackTrace();
		}

	}

	private void cleanFields() {
		setIdEmpresa(null);
		setNombreEmpresa(null);
		setDireccionPrincipal(null);
		setRazonSocialEmpresa(null);
	}

	public void info(final String message) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", message));
	}

	public void warn(final String message) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning!", message));
	}

	public void error(final String message) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", message));
	}

	public void fatal() {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal!", "System Error"));
	}

	public void onRowEdit(RowEditEvent<EmpresaDto> event) {
		try {
			if (event.getObject() != null) {
				final String serviceUrl = Utils.getProperty("endPointEmpresas") + "/update";
				final RestClient restClient = new RestClient();
				final String resultService = restClient.consumeRestServicePost(serviceUrl, event.getObject());
				if (resultService != null) {
					FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Empresa Editada: " + resultService,
							"Id de la empresa: " + event.getObject().getIdEmpresa());
					FacesContext.getCurrentInstance().addMessage(null, msg);
				} else {
					FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
							"No se ha podido editar la empresa error de conexión",
							"Id de la empresa: " + event.getObject().getIdEmpresa());
					FacesContext.getCurrentInstance().addMessage(null, msg);
				}
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}

	}

	public void onRowCancel(RowEditEvent<EmpresaDto> event) {
		FacesMessage msg = new FacesMessage("Edit Cancelled", event.getObject().getIdEmpresa());
		FacesContext.getCurrentInstance().addMessage(null, msg);
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

	public List<EmpresaDto> getListaEmpresas() {
		return listaEmpresas;
	}

	public void setListaEmpresas(List<EmpresaDto> listaEmpresas) {
		this.listaEmpresas = listaEmpresas;
	}

}
