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

import co.agro.blockchain.jsf.company.dto.RolDto;
import co.agro.blockchain.jsf.rest.client.RestClient;
import co.agro.blockchain.jsf.utils.Utils;

@Named
@RequestScoped
public class AdminRolUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4303612504715546440L;
	private String idRol;
	private String descripcionRol;

	private List<RolDto> listaRoles;

	public AdminRolUser() {
		try {
			inicializarRoles();
		} catch (final Exception e) {

			e.printStackTrace();
		}
	}

	public void save() {
		try {
			if (idRol != null && !idRol.isEmpty() && descripcionRol != null && !descripcionRol.isEmpty()) {
				final RestClient restClient = new RestClient();
				final String serviceUrl = Utils.getProperty("endPointRoles") + "/add";
				final RolDto rol = new RolDto();
				rol.setDescripcion(descripcionRol);
				rol.setIdRol(idRol);
				final String respuestaServer = restClient.consumeRestServicePost(serviceUrl, rol);
				if (respuestaServer != null) {
					if (respuestaServer.toLowerCase().contains("correcta")) {
						PrimeFaces.current().ajax().addCallbackParam("infoCorrect", true);
						inicializarRoles();
						info("Rol guardado: " + respuestaServer);
					} else {
						PrimeFaces.current().ajax().addCallbackParam("infoCorrect", false);
						warn("No se pudo guardar el nuevo rol" + respuestaServer);
					}
				} else {
					PrimeFaces.current().ajax().addCallbackParam("infoCorrect", false);
					error("No se ha podido guardar el nuevo rol, error de conexión");
				}
				cleanFields();
			} else {
				PrimeFaces.current().ajax().addCallbackParam("infoCorrect", false);
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private void inicializarRoles() throws IOException {
		final String serviceUrl = Utils.getProperty("endPointRoles");
		final RestClient restClient = new RestClient();
		final String resultService = restClient.consumeRestServiceGet(serviceUrl, null);
		if (resultService != null) {
			listaRoles = Arrays.asList(new GsonBuilder().create().fromJson(resultService, RolDto[].class));
		}
	}

	private void cleanFields() {
		setIdRol(null);
		setDescripcionRol(null);
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

	public void onRowEdit(RowEditEvent<RolDto> event) {

		try {
			if (event.getObject() != null) {
				final String serviceUrl = Utils.getProperty("endPointRoles") + "/update";
				final RestClient restClient = new RestClient();
				final String resultService = restClient.consumeRestServicePost(serviceUrl, event.getObject());
				if (resultService != null) {
					FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Rol editado editado: " + resultService,
							"");
					FacesContext.getCurrentInstance().addMessage(null, msg);
				} else {
					FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
							"No se ha podido editar el rol, error de conexión", "");
					FacesContext.getCurrentInstance().addMessage(null, msg);
				}
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public void onRowCancel(RowEditEvent<RolDto> event) {
		FacesMessage msg = new FacesMessage("Edit Cancelled", event.getObject().getIdRol());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public String getIdRol() {
		return idRol;
	}

	public void setIdRol(String idRol) {
		this.idRol = idRol;
	}

	public String getDescripcionRol() {
		return descripcionRol;
	}

	public void setDescripcionRol(String descripcionRol) {
		this.descripcionRol = descripcionRol;
	}

	public List<RolDto> getListaRoles() {
		return listaRoles;
	}

	public void setListaRoles(List<RolDto> listaRoles) {
		this.listaRoles = listaRoles;
	}

}
