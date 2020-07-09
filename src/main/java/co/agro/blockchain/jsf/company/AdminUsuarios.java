package co.agro.blockchain.jsf.company;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.event.RowEditEvent;

import com.google.gson.GsonBuilder;

import co.agro.blockchain.jsf.company.dto.DatosUsuarioDto;
import co.agro.blockchain.jsf.company.dto.EmpresaDto;
import co.agro.blockchain.jsf.company.dto.RolDto;
import co.agro.blockchain.jsf.company.dto.UsuarioLoginDto;
import co.agro.blockchain.jsf.company.dto.UsuarioLoginPkDto;
import co.agro.blockchain.jsf.rest.client.RestClient;
import co.agro.blockchain.jsf.utils.Utils;

@Named
@RequestScoped
public class AdminUsuarios implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5206596734028433163L;
	private DatosUsuarioDto datosUsuario;
	private Boolean estadoUsuario = true;
	private List<EmpresaDto> listaEmpresas;
	private List<RolDto> listaRoles;
	private String selectedRol;
	private String selectedEmpresa;
	private String username;

	private Map<String, EmpresaDto> mapEmpresas;
	private Map<String, RolDto> mapRoles;

	private List<DatosUsuarioDto> listaUsuariosSistema;

	public AdminUsuarios() {

	}

	@PostConstruct
	public void init() {
		inicializarListas();
	}

	public void inicializarListas() {
		try {
			datosUsuario = new DatosUsuarioDto();
			datosUsuario.setEmpresa(new EmpresaDto());
			inicializarUsuarios();
			if (listaEmpresas == null || listaEmpresas.isEmpty()) {
				listaEmpresas = inicializarEmpresas();
				mapEmpresas = new HashMap<String, EmpresaDto>();
				for (final EmpresaDto empresaDto : listaEmpresas) {
					mapEmpresas.put(empresaDto.getIdEmpresa(), empresaDto);
				}
			}
			if (listaRoles == null || listaRoles.isEmpty()) {
				listaRoles = inicializarRoles();
				mapRoles = new HashMap<String, RolDto>();
				for (final RolDto rolDto : listaRoles) {
					mapRoles.put(rolDto.getIdRol(), rolDto);
				}
			}

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private void inicializarUsuarios() throws IOException {
		final String serviceUrl = Utils.getProperty("endPointDatosUsuario");
		final RestClient restClient = new RestClient();
		final String resultService = restClient.consumeRestServiceGet(serviceUrl, null);
		if (resultService != null) {
			listaUsuariosSistema = Arrays
					.asList(new GsonBuilder().create().fromJson(resultService, DatosUsuarioDto[].class));
		}
	}

	private List<EmpresaDto> inicializarEmpresas() throws IOException {
		List<EmpresaDto> listaEmpresas = new ArrayList<EmpresaDto>();
		final String serviceUrl = Utils.getProperty("endPointEmpresas");
		final RestClient restClient = new RestClient();
		final String resultService = restClient.consumeRestServiceGet(serviceUrl, null);
		if (resultService != null) {
			listaEmpresas = Arrays.asList(new GsonBuilder().create().fromJson(resultService, EmpresaDto[].class));
		}
		return listaEmpresas;
	}

	private List<RolDto> inicializarRoles() throws IOException {
		List<RolDto> listaRoles = new ArrayList<>();
		final String serviceUrl = Utils.getProperty("endPointRoles");
		final RestClient restClient = new RestClient();
		final String resultService = restClient.consumeRestServiceGet(serviceUrl, null);
		if (resultService != null) {
			listaRoles = Arrays.asList(new GsonBuilder().create().fromJson(resultService, RolDto[].class));
		}
		return listaRoles;
	}

	public void save() {
		try {
			if (datosUsuario.getIdUsuario() != null && !datosUsuario.getIdUsuario().isEmpty() && selectedEmpresa != null
					&& datosUsuario.getNombreUsario() != null && !datosUsuario.getNombreUsario().isEmpty()) {
				guardarDatosUsuario();
				cleanFields();
				inicializarListas();
			} else {
				PrimeFaces.current().ajax().addCallbackParam("infoCorrect", false);
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}

	}

	private void guardarDatosUsuario() throws IOException {
		final RestClient restClient = new RestClient();
		final String serviceUrl = Utils.getProperty("endPointDatosUsuario") + "/add";
		datosUsuario.setEstadoUsuario(estadoUsuario ? Short.valueOf("1") : Short.valueOf("0"));
		datosUsuario.setEmpresa(mapEmpresas.get(selectedEmpresa));
		final String respuestaServer = restClient.consumeRestServicePost(serviceUrl, datosUsuario);
		if (respuestaServer != null && guardarDatosLogin() != null) {
			if (respuestaServer.toLowerCase().contains("correcta")) {
				PrimeFaces.current().ajax().addCallbackParam("infoCorrect", true);
				inicializarEmpresas();
				info("Datos de usuario guardados: " + respuestaServer);
			} else {
				PrimeFaces.current().ajax().addCallbackParam("infoCorrect", false);
				warn("No se pudo guardar el usuario: " + respuestaServer);
			}
		} else {
			error("No se ha podido guardar el usuario, error de conexión");
		}
	}

	private String guardarDatosLogin() throws IOException {
		final RestClient restClient = new RestClient();
		final String serviceUrl = Utils.getProperty("endPointLoginUsuario") + "/add";
		final UsuarioLoginDto usuarioLogin = new UsuarioLoginDto();
		usuarioLogin.setRol(mapRoles.get(selectedRol));
		final UsuarioLoginPkDto userPk = new UsuarioLoginPkDto();
		userPk.setIdUsuario(datosUsuario);
		userPk.setUsername(username);
		usuarioLogin.setUsuarioLoginPk(userPk);
		return restClient.consumeRestServicePost(serviceUrl, usuarioLogin);
	}

	private void cleanFields() {
		setEstadoUsuario(true);
		setSelectedEmpresa(null);
		setDatosUsuario(new DatosUsuarioDto());
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

	public void onRowEdit(RowEditEvent<DatosUsuarioDto> event) {
		try {
			if (event.getObject() != null) {
				final String serviceUrl = Utils.getProperty("endPointDatosUsuario") + "/update";
				final RestClient restClient = new RestClient();
				final String resultService = restClient.consumeRestServicePost(serviceUrl, event.getObject());
				if (resultService != null) {
					FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuario Editado: " + resultService,
							"Usuario editado correctamente");
					FacesContext.getCurrentInstance().addMessage(null, msg);
				} else {
					FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
							"No se ha podido editar el usuario, error de conexión",
							"No se ha podido editar el usuario, error de conexión");
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

	public DatosUsuarioDto getDatosUsuario() {
		return datosUsuario;
	}

	public void setDatosUsuario(DatosUsuarioDto datosUsuario) {
		this.datosUsuario = datosUsuario;
	}

	public Boolean getEstadoUsuario() {
		return estadoUsuario;
	}

	public void setEstadoUsuario(Boolean estadoUsuario) {
		this.estadoUsuario = estadoUsuario;
	}

	public List<DatosUsuarioDto> getListaUsuariosSistema() {
		return listaUsuariosSistema;
	}

	public void setListaUsuariosSistema(List<DatosUsuarioDto> listaUsuariosSistema) {
		this.listaUsuariosSistema = listaUsuariosSistema;
	}

	public List<RolDto> getListaRoles() {
		return listaRoles;
	}

	public String getSelectedRol() {
		return selectedRol;
	}

	public void setSelectedRol(String selectedRol) {
		this.selectedRol = selectedRol;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSelectedEmpresa() {
		return selectedEmpresa;
	}

	public void setSelectedEmpresa(String selectedEmpresa) {
		this.selectedEmpresa = selectedEmpresa;
	}

	public List<EmpresaDto> getListaEmpresas() {
		return listaEmpresas;
	}

	public void setListaEmpresas(List<EmpresaDto> listaEmpresas) {
		this.listaEmpresas = listaEmpresas;
	}

	public void setListaRoles(List<RolDto> listaRoles) {
		this.listaRoles = listaRoles;
	}

}
