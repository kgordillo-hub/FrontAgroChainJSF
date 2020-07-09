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

import co.agro.blockchain.jsf.company.dto.EstadoProductoDto;
import co.agro.blockchain.jsf.rest.client.RestClient;
import co.agro.blockchain.jsf.utils.Utils;

@Named
@RequestScoped
public class AdminProductStatus implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7939373825612083938L;
	/**
	 * 
	 */
	private String idStatusProd;
	private String statusDescription;

	private List<EstadoProductoDto> listaEstadosProd;

	public AdminProductStatus() {
		try {
			inicializarEstadosProd();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void save() {
		try {
			if (idStatusProd != null && !idStatusProd.isEmpty() && statusDescription != null
					&& !statusDescription.isEmpty()) {
				final RestClient restClient = new RestClient();
				final String serviceUrl = Utils.getProperty("endPointEstadosProd") + "/add";
				final EstadoProductoDto estadoProd = new EstadoProductoDto();
				estadoProd.setDescripcion(statusDescription);
				estadoProd.setIdEstadoProducto(idStatusProd);
				final String respuestaServer = restClient.consumeRestServicePost(serviceUrl, estadoProd);
				if (respuestaServer != null) {
					if (respuestaServer.toLowerCase().contains("correcta")) {
						PrimeFaces.current().ajax().addCallbackParam("infoCorrect", true);
						inicializarEstadosProd();
						info("Estado guardado: " + respuestaServer);
					} else {
						PrimeFaces.current().ajax().addCallbackParam("infoCorrect", false);
						warn("No se pudo guardar el estado del producto: " + respuestaServer);
					}
				} else {
					PrimeFaces.current().ajax().addCallbackParam("infoCorrect", false);
					error("No se ha podido guardar el estado del producto, error de conexión");
				}
				cleanFields();
			} else {
				PrimeFaces.current().ajax().addCallbackParam("infoCorrect", false);
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}

	}

	private void inicializarEstadosProd() throws IOException {
		final String serviceUrl = Utils.getProperty("endPointEstadosProd");
		final RestClient restClient = new RestClient();
		final String resultService = restClient.consumeRestServiceGet(serviceUrl, null);
		if (resultService != null) {
			listaEstadosProd = Arrays
					.asList(new GsonBuilder().create().fromJson(resultService, EstadoProductoDto[].class));
		}
	}

	private void cleanFields() {
		setIdStatusProd(null);
		setStatusDescription(null);
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

	public void onRowEdit(RowEditEvent<EstadoProductoDto> event) {
		try {
			if (event.getObject() != null) {
				final String serviceUrl = Utils.getProperty("endPointEstadosProd") + "/update";
				final RestClient restClient = new RestClient();
				final String resultService = restClient.consumeRestServicePost(serviceUrl, event.getObject());
				if (resultService != null) {
					FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Estado editado: " + resultService,
							"");
					FacesContext.getCurrentInstance().addMessage(null, msg);
				} else {
					FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
							"No se ha podido editar el estado, error de conexión", "");
					FacesContext.getCurrentInstance().addMessage(null, msg);
				}
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public void onRowCancel(RowEditEvent<EstadoProductoDto> event) {
		FacesMessage msg = new FacesMessage("Edit Cancelled", event.getObject().getIdEstadoProducto());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public String getIdStatusProd() {
		return idStatusProd;
	}

	public void setIdStatusProd(String idStatusProd) {
		this.idStatusProd = idStatusProd;
	}

	public String getStatusDescription() {
		return statusDescription;
	}

	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}

	public List<EstadoProductoDto> getListaEstadosProd() {
		return listaEstadosProd;
	}

	public void setListaEstadosProd(List<EstadoProductoDto> listaEstadosProd) {
		this.listaEstadosProd = listaEstadosProd;
	}

}
