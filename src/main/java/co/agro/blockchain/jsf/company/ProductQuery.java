package co.agro.blockchain.jsf.company;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.google.gson.GsonBuilder;

import co.agro.blockchain.jsf.dto.bigchaindb.AssetDto;
import co.agro.blockchain.jsf.rest.client.RestClient;
import co.agro.blockchain.jsf.utils.Utils;

@Named
@ViewScoped
public class ProductQuery implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5353233180278308786L;

	private String queryParameter;

	private List<AssetDto> assetsList;

	private AssetDto selectedAsset;

	public ProductQuery() {
		// Constructor basico
	}

	public void consultarEstadoProducto() {
		try {
			System.out.println("Entra al método consulta estado producto");
			final RestClient restClient = new RestClient();
			final String serviceUrl = Utils.getProperty("endPointTransactions") + "/listRfidTransact";
			final String serviceResponse = restClient.consumeRestServiceGet(serviceUrl, queryParameter);
			if (serviceResponse != null && !serviceResponse.isEmpty()) {
				assetsList = Arrays.asList(new GsonBuilder().create().fromJson(serviceResponse, AssetDto[].class));
				for (final AssetDto assetDto : assetsList) {
					System.out.println(assetDto.getMetaData().getStatusDescription());
				}
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public String getQueryParameter() {
		return queryParameter;
	}

	public void setQueryParameter(String queryParameter) {
		this.queryParameter = queryParameter;
	}

	public List<AssetDto> getAssetsList() {
		return assetsList;
	}

	public void setAssetsList(List<AssetDto> assetsList) {
		this.assetsList = assetsList;
	}

	public AssetDto getSelectedAsset() {
		return selectedAsset;
	}

	public void setSelectedAsset(AssetDto selectedAsset) {
		this.selectedAsset = selectedAsset;
	}

}
