package co.agro.blockchain.jsf.company;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.PrimeFaces;
import org.primefaces.event.FlowEvent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import co.agro.blockchain.jsf.company.dto.EstadoProductoDto;
import co.agro.blockchain.jsf.dto.bigchaindb.AssetDto;
import co.agro.blockchain.jsf.dto.bigchaindb.AssetWrapper;
import co.agro.blockchain.jsf.dto.bigchaindb.MetaDataDto;
import co.agro.blockchain.jsf.dto.bigchaindb.MetadataWrapper;
import co.agro.blockchain.jsf.dto.bigchaindb.ProductDto;
import co.agro.blockchain.jsf.rest.client.RestClient;
import co.agro.blockchain.jsf.session.SessionLogin;
import co.agro.blockchain.jsf.utils.Utils;

@Named
@ViewScoped
public class ProductRegistration implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5655938800360695542L;
	private AssetDto asset;

	private List<EstadoProductoDto> listaEstadosProducto;
	private Map<String, EstadoProductoDto> estadosProd;
	private String selectedProductStatus;
	private String genesisAssetId=null;
	private boolean editableAsset = true;
	private String rfidId;
	@Inject
	private SessionLogin sessionBean;

	public ProductRegistration() {
		try {
			if (asset == null) {
				System.out.println("Entra a crear asset");
				asset = new AssetDto(new MetaDataDto(), new ProductDto(), null);
			}
			if (listaEstadosProducto == null) {
				estadosProd = new HashMap<String, EstadoProductoDto>();
				inicializarEstadosProd();
				for (final EstadoProductoDto estadoProductoDto : listaEstadosProducto) {
					estadosProd.put(estadoProductoDto.getIdEstadoProducto(), estadoProductoDto);
				}
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
			listaEstadosProducto = Arrays
					.asList(new GsonBuilder().create().fromJson(resultService, EstadoProductoDto[].class));
		}
	}

	public void escanearEtiquetaRFID() {
		System.out.println(sessionBean.getLoggedUser().getRol().getDescripcion());
	}

	public void guardarActualizar() {
		try {
			if (selectedProductStatus != null && !selectedProductStatus.isEmpty()) {
				asset.getMetaData().setStatusId(estadosProd.get(selectedProductStatus).getIdEstadoProducto());
				asset.getMetaData().setStatusDescription(estadosProd.get(selectedProductStatus).getDescripcion());
			}
			enviarABlockchain();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private void enviarABlockchain() throws IOException, InterruptedException {
		if (editableAsset) {
			agregarAsset();
		} else {
			actualizarInfo();
		}
	}

	private void agregarAsset() throws IOException, InterruptedException {
		final String serviceUrl = Utils.getProperty("endPointTransactions") + "/create";
		final RestClient restClient = new RestClient();
		final AssetWrapper assetWrapper = new AssetWrapper();
		System.out.println("Asset data product id: " + asset.getProductData().getIdProduct());
		assetWrapper.setAsset(asset);
		assetWrapper.setPrivateKey(
				sessionBean.getLoggedUser().getUsuarioLoginPk().getIdUsuario().getEmpresa().getPrivateKey());
		assetWrapper.setUserId(sessionBean.getLoggedUser().getUsuarioLoginPk().getIdUsuario().getIdUsuario());
		assetWrapper.getAsset().getProductData().setCreationDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		final String response = restClient.consumeRestServicePost(serviceUrl, assetWrapper);
		if (response != null) {
			if (response.toLowerCase().contains("correcta")) {
				PrimeFaces.current().ajax().addCallbackParam("infoCorrect", true);
				info("Transacción guardada: " + response);
				asset = new AssetDto(new MetaDataDto(), new ProductDto(), null);
				selectedProductStatus = null;
				TimeUnit.SECONDS.sleep(2);
				reload();
			} else {
				PrimeFaces.current().ajax().addCallbackParam("infoCorrect", false);
				warn("No se pudo guardar la transacción: " + response);
			}
		} else {
			PrimeFaces.current().ajax().addCallbackParam("infoCorrect", false);
			error("No se ha podido guardar la transacción error de conexión");
		}
	}

	private void actualizarInfo() throws IOException, InterruptedException {
		final String serviceUrl = Utils.getProperty("endPointTransactions") + "/update";
		final RestClient restClient = new RestClient();
		final MetadataWrapper metadata = new MetadataWrapper();
		metadata.setDestinationPublicKey(sessionBean.getLoggedUser().getUsuarioLoginPk().getIdUsuario().getEmpresa().getPublicKey());
		metadata.setGenesisAssetId(genesisAssetId);
		metadata.setMetaData(asset.getMetaData());
		metadata.setPrivateKey(sessionBean.getLoggedUser().getUsuarioLoginPk().getIdUsuario().getEmpresa().getPrivateKey());
		metadata.setRfidTag(rfidId);
		metadata.setTransactionId(asset.getIdTransaccion());
		metadata.setUserId(sessionBean.getLoggedUser().getUsuarioLoginPk().getIdUsuario().getIdUsuario());
		final String response = restClient.consumeRestServicePost(serviceUrl, metadata);
		if (response != null) {
			if (response.toLowerCase().contains("correcta")) {
				PrimeFaces.current().ajax().addCallbackParam("infoCorrect", true);
				info("Transacción actualizada: " + response);
				asset = new AssetDto(new MetaDataDto(), new ProductDto(), null);
				selectedProductStatus = null;
				TimeUnit.SECONDS.sleep(2);
				reload();
			} else {
				PrimeFaces.current().ajax().addCallbackParam("infoCorrect", false);
				warn("No se pudo actualizar la transacción: " + response);
			}
		} else {
			PrimeFaces.current().ajax().addCallbackParam("infoCorrect", false);
			error("No se ha podido actualizar la transacción error de conexión");
		}
	}

	public String onFlowProcess(FlowEvent event) {
		final String step = event.getNewStep();
		System.out.println(step);
		if (step.equalsIgnoreCase("blockAsset")) {
			final AssetWrapper existingAsset = consultarTransaccion(asset.getProductData().getIdRfidTag());
			if (existingAsset != null) {
				System.out.println("Hay una transacción con ese RFID id");
				rfidId = asset.getProductData().getIdRfidTag();
				asset = existingAsset.getAsset();
				editableAsset = false;
				genesisAssetId = existingAsset.getGenesisAssetId();
			} else {
				editableAsset = true;
			}
		}
		return step;
	}

	private AssetWrapper consultarTransaccion(final String rfidTag) {
		try {
			final String serviceUrl = Utils.getProperty("endPointTransactions") + "/rfidTransact/";
			final RestClient restClient = new RestClient();
			final String response = restClient.consumeRestServiceGet(serviceUrl, rfidTag);
			if (response != null && !response.isEmpty()) {
				final AssetWrapper asset = new Gson().fromJson(response, AssetWrapper.class);
				return asset;
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return null;
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

	public AssetDto getAsset() {
		return asset;
	}

	public void setAsset(AssetDto asset) {
		this.asset = asset;
	}

	public List<EstadoProductoDto> getListaEstadosProducto() {
		return listaEstadosProducto;
	}

	public void setListaEstadosProducto(List<EstadoProductoDto> listaEstadosProducto) {
		this.listaEstadosProducto = listaEstadosProducto;
	}

	public String getSelectedProductStatus() {
		return selectedProductStatus;
	}

	public void setSelectedProductStatus(String selectedProductStatus) {
		this.selectedProductStatus = selectedProductStatus;
	}

	public boolean isEditableAsset() {
		return editableAsset;
	}

	public void setEditableAsset(boolean editableAsset) {
		this.editableAsset = editableAsset;
	}

	private void reload() throws IOException {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
	}

}
