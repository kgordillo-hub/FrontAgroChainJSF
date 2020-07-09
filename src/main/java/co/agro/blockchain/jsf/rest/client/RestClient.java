package co.agro.blockchain.jsf.rest.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

public final class RestClient {

	private final Client client = ClientBuilder.newClient();

	public String consumeRestServicePost(final String serviceUrl, final Object entity) {
		try {
			final Response response = client.target(serviceUrl).request(MediaType.APPLICATION_JSON)
					.post(Entity.entity(new Gson().toJson(entity), MediaType.APPLICATION_JSON));
			System.out.println("REST response to URL '" + serviceUrl + "': " + response.getStatus());
			return response.getStatus() == 200 ? "Transacción correcta"
					: response.getStatus() == 405 ? "El registro que intenta actualizar/crear ya existe"
							: "Error en el servidor";
		} catch (final Exception e) {
			System.out.println("Error al consumir el servicio POST: " + serviceUrl);
			e.printStackTrace();
		}
		return null;
	}

	public String consumePlainPost(final String serviceUrl, final Object entity) {
		final Response response = client.target(serviceUrl).request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(new Gson().toJson(entity), MediaType.APPLICATION_JSON));
		return response.readEntity(String.class);
	}

	public String consumeRestServiceGet(final String serviceUrl, String parameter) {
		try {
			if (parameter != null && !parameter.isEmpty()) {
				return client.target(serviceUrl.endsWith("/") ? serviceUrl + parameter : serviceUrl + "/" + parameter)
						.request(MediaType.APPLICATION_JSON).get(String.class);
			}
			return client.target(serviceUrl).request(MediaType.APPLICATION_JSON).get(String.class);
		} catch (final Exception e) {
			System.out.println("Error al consumir el servicio GET: " + serviceUrl);
			e.printStackTrace();
		}
		return null;
	}
}
