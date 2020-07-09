package co.agro.blockchain.jsf.session;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import com.google.gson.Gson;

import co.agro.blockchain.jsf.company.dto.DatosUsuarioDto;
import co.agro.blockchain.jsf.company.dto.UsuarioLoginDto;
import co.agro.blockchain.jsf.company.dto.UsuarioLoginPkDto;
import co.agro.blockchain.jsf.rest.client.RestClient;
import co.agro.blockchain.jsf.utils.Utils;

@Named
@SessionScoped
public class SessionLogin implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1748683385093171439L;
	private UsuarioLoginDto loggedUser;

	public SessionLogin(){
		validateUser();
	}
	
	public String validateUser() {
		try {
			if (loggedUser == null) {
				final RestClient restClient = new RestClient();
				final String serviceUrl = Utils.getProperty("endPointLoginUsuario") + "/getUser";
				final UsuarioLoginPkDto usuarioPk = new UsuarioLoginPkDto();
				final DatosUsuarioDto pkDatos = new DatosUsuarioDto();
				pkDatos.setIdUsuario("1");
				usuarioPk.setIdUsuario(pkDatos);
				usuarioPk.setUsername("operario1@plazaubaque.com");
				String clientResponse = restClient.consumePlainPost(serviceUrl, usuarioPk);
				System.out.println(clientResponse);
				if(clientResponse!=null && !clientResponse.isEmpty()) {
					clientResponse = clientResponse.replaceAll("\"estadoUsuario\": 1", "\"estadoUsuario\": true").replaceAll("\"estadoUsuario\": 1", "\"estadoUsuario\": false");
					System.out.println(clientResponse);
					loggedUser = new Gson().fromJson(clientResponse, UsuarioLoginDto.class);
				}
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public UsuarioLoginDto getLoggedUser() {
		return loggedUser;
	}

}
