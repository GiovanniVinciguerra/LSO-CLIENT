package org.javafx.trismasterfx.connection;

import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.javafx.trismasterfx.App;
import org.javafx.trismasterfx.controller.ObjectAccessController;
import org.javafx.trismasterfx.entity.Match;
import org.javafx.trismasterfx.entity.Message;
import org.javafx.trismasterfx.entity.Stat;
import org.javafx.trismasterfx.entity.User;
import javafx.application.Platform;

public class HttpConnection {
	private static String URL = "http://trismaster.ddns.net:8080/";
	
	public static void set_url() {
		/* Permette il funzionamento dell'applicazione in locale o in remoto */
		if(App.isRemote())
			URL = "http://trismaster.ddns.net:8080/";
		else
			URL = "http://localhost:8080/";
	}
	
	public static int login_request() throws Exception {		
		/* Creazione del corpo della richiesta */
		ObjectMapper mapper = new ObjectMapper();
        Map<String, String> data = new LinkedHashMap<>();
        data.put("username", User.get_usr_inst().getUsername());
		data.put("password", User.get_usr_inst().getPassword());
		String json_body = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
		
		/* Creazione del client HTTP */
		CloseableHttpClient client = HttpClients.createDefault();
		
		/* Creazione della richiesta POST */
		HttpPost post = new HttpPost(URL + "login");
		post.setEntity(new StringEntity(json_body, ContentType.APPLICATION_JSON));
		post.setHeader("Accept", "application/json");
		
		@SuppressWarnings("deprecation")
		/* Invio della richiesta e attesa della risposta */
		CloseableHttpResponse response = client.execute(post);
		int status_code = response.getCode();
		
		/* Body del messaggio come stringa */
		String body = EntityUtils.toString(response.getEntity());
		
		if(status_code == 200) {
			/* Parsing del body JSON */
			JsonNode json = mapper.readTree(body);
			
			/* Aggiunta dei restanti parametri all'utente */
			User.get_usr_inst().setName(json.get("name").asText());
			User.get_usr_inst().setSurname(json.get("surname").asText());
			User.get_usr_inst().setMail(json.get("email").asText());
			User.get_usr_inst().setSessionId(json.get("session_id").asInt());
		}
		
		return status_code;
	}
	
	public static int submit_request() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
        Map<String, String> data = new LinkedHashMap<>();
        data.put("name", User.get_usr_inst().getName());
        data.put("surname", User.get_usr_inst().getSurname());
        data.put("mail", User.get_usr_inst().getMail());
        data.put("username", User.get_usr_inst().getUsername());
		data.put("password", User.get_usr_inst().getPassword());
		String json_body = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
		
		CloseableHttpClient client = HttpClients.createDefault();
		
		HttpPost post = new HttpPost(URL + "signin");
		post.setEntity(new StringEntity(json_body, ContentType.APPLICATION_JSON));
		post.setHeader("Accept", "application/json");
		
		@SuppressWarnings("deprecation")
		CloseableHttpResponse response = client.execute(post);
		int status_code = response.getCode();
		
		return status_code;
	}
	
	public static int logout_request() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
        Map<String, String> data = new LinkedHashMap<>();
        data.put("session_id", Integer.toString(User.get_usr_inst().getSessionId()));
		data.put("username", User.get_usr_inst().getUsername());
		String json_body = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
		
		CloseableHttpClient client = HttpClients.createDefault();
		
		HttpPost post = new HttpPost(URL + "logout");
		post.setEntity(new StringEntity(json_body, ContentType.APPLICATION_JSON));
		post.setHeader("Accept", "application/json");
		
		@SuppressWarnings("deprecation")
		CloseableHttpResponse response = client.execute(post);
		int status_code = response.getCode();
		
		return status_code;
	}
	
	public static int message_request() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
        Map<String, String> data = new LinkedHashMap<>();
        data.put("session_id", Integer.toString(User.get_usr_inst().getSessionId()));
		data.put("username", User.get_usr_inst().getUsername());
		String json_body = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
		
		CloseableHttpClient client = HttpClients.createDefault();
		
		HttpPost post = new HttpPost(URL + "messages");
		post.setEntity(new StringEntity(json_body, ContentType.APPLICATION_JSON));
		post.setHeader("Accept", "application/json");
		
		@SuppressWarnings("deprecation")
		CloseableHttpResponse response = client.execute(post);
		int status_code = response.getCode();
		
		String body = EntityUtils.toString(response.getEntity());
		
		if(status_code == 200) {
			JsonNode json = mapper.readTree(body);
			
			/* Pulizia dei vecchi messaggi */
			ObjectAccessController.getMessages().clear();
			
			/* Aggiunta dei messaggi ottenuti alla lista dei messaggi gestiti da ObjectAccessController */
			json.forEach(node -> ObjectAccessController.getMessages().add(new Message(node.get("label").asText(), node.get("body").asText(), node.get("timestamp").asText())));
		}
		
		return status_code;
	}
	
	public static int stat_request() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
        Map<String, String> data = new LinkedHashMap<>();
        data.put("session_id", Integer.toString(User.get_usr_inst().getSessionId()));
		data.put("username", User.get_usr_inst().getUsername());
		String json_body = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
		
		CloseableHttpClient client = HttpClients.createDefault();
		
		HttpPost post = new HttpPost(URL + "stat");
		post.setEntity(new StringEntity(json_body, ContentType.APPLICATION_JSON));
		post.setHeader("Accept", "application/json");
		
		@SuppressWarnings("deprecation")
		CloseableHttpResponse response = client.execute(post);
		int status_code = response.getCode();
		
		String body = EntityUtils.toString(response.getEntity());
		
		ObjectAccessController.getStats().clear();
		
		if(status_code == 200) {
			JsonNode json = mapper.readTree(body);
			
			json.forEach(node -> ObjectAccessController.getStats().add(new Stat(node.get("player_1").asText(), node.get("player_2").asText(), node.get("result").asText())));
		}
		
		return status_code;
	}
	
	public static int new_game_request() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
        Map<String, String> data = new LinkedHashMap<>();
        data.put("session_id", Integer.toString(User.get_usr_inst().getSessionId()));
		data.put("username", User.get_usr_inst().getUsername());
		String json_body = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
		
		CloseableHttpClient client = HttpClients.createDefault();
		
		HttpPost post = new HttpPost(URL + "new-game");
		post.setEntity(new StringEntity(json_body, ContentType.APPLICATION_JSON));
		post.setHeader("Accept", "application/json");
		
		@SuppressWarnings("deprecation")
		CloseableHttpResponse response = client.execute(post);
		int status_code = response.getCode();
		
		String body = EntityUtils.toString(response.getEntity());
		
		if(status_code == 200) {
			JsonNode json = mapper.readTree(body);
			
			ObjectAccessController.getMatches().add(
				new Match(
					Integer.parseInt(json.get("match_id").asText()),
					json.get("player_1").asText(),
					json.get("player_2").asText(),
					json.get("status").asText()
				)
			);
			
			Match match = ObjectAccessController.getMatches().getLast();
			if(match.getPlayer_1().compareTo(User.get_usr_inst().getUsername()) == 0)
				match.setSeed(json.get("seed_1").asText());
			else
				match.setSeed(json.get("seed_2").asText());
		}
		
		return  status_code;
	}
	
	public static int waiting_request(Match match) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
        Map<String, String> data = new LinkedHashMap<>();
        data.put("session_id", Integer.toString(User.get_usr_inst().getSessionId()));
		data.put("username", User.get_usr_inst().getUsername());
		data.put("match_id", Integer.toString(match.getMatch_id()));
		String json_body = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
		
		CloseableHttpClient client = HttpClients.createDefault();
		
		HttpPost post = new HttpPost(URL + "waiting");
		post.setEntity(new StringEntity(json_body, ContentType.APPLICATION_JSON));
		post.setHeader("Accept", "application/json");
		
		@SuppressWarnings("deprecation")
		CloseableHttpResponse response = client.execute(post);
		int status_code = response.getCode();
		
		String body = EntityUtils.toString(response.getEntity());
		
		if(status_code == 200) {
			JsonNode json = mapper.readTree(body);
			
			match.setSeed(json.get("seed").asText());
		}
		
		return status_code;
	}
	
	public static int update_request(Match match) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
        Map<String, String> data = new LinkedHashMap<>();
        data.put("session_id", Integer.toString(User.get_usr_inst().getSessionId()));
		data.put("username", User.get_usr_inst().getUsername());
		data.put("match_id", Integer.toString(match.getMatch_id()));
		String json_body = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
		
		CloseableHttpClient client = HttpClients.createDefault();
		
		HttpPost post = new HttpPost(URL + "update");
		post.setEntity(new StringEntity(json_body, ContentType.APPLICATION_JSON));
		post.setHeader("Accept", "application/json");
		
		@SuppressWarnings("deprecation")
		CloseableHttpResponse response = client.execute(post);
		int status_code = response.getCode();
		
		String body = EntityUtils.toString(response.getEntity());
		
		if(status_code == 200) {
			JsonNode json = mapper.readTree(body);
			
			/* Qui si istruisce un thread secondario a gestire la risposta asincrona di HttpConnection */
			Platform.runLater(() -> {
				match.setMatch_id(Integer.parseInt(json.get("match_id").asText()));
				match.setPlayer_1(json.get("player_1").asText());
				match.setPlayer_2(json.get("player_2").asText());
				if(match.getPlayer_1().compareTo(User.get_usr_inst().getUsername()) == 0)
					match.setSeed(json.get("seed_1").asText());
				else
					match.setSeed(json.get("seed_2").asText());
				match.setResult(json.get("result").asText());
				match.setStatus(json.get("status").asText());
				JsonNode json_steps = json.get("steps");
				if(json_steps != null)
					json_steps.forEach(node -> match.translate(node.get("step").asText()));
			});
		}
		
		return status_code;
	}
	
	public static int step_request(int match_id, String step) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
        Map<String, String> data = new LinkedHashMap<>();
        data.put("session_id", Integer.toString(User.get_usr_inst().getSessionId()));
		data.put("username", User.get_usr_inst().getUsername());
		data.put("match_id", Integer.toString(match_id));
		data.put("step", step);
		String json_body = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
		
		CloseableHttpClient client = HttpClients.createDefault();
		
		HttpPost post = new HttpPost(URL + "step");
		post.setEntity(new StringEntity(json_body, ContentType.APPLICATION_JSON));
		post.setHeader("Accept", "application/json");
		
		@SuppressWarnings("deprecation")
		CloseableHttpResponse response = client.execute(post);
		int status_code = response.getCode();
		
		if(status_code == 200) {
			Match match = ObjectAccessController.getProgressMatch();
			if(match != null)
				match.translate(step);
		}
		
		return status_code;
	}
	
	public static int winner_request(int match_id) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
        Map<String, String> data = new LinkedHashMap<>();
        data.put("session_id", Integer.toString(User.get_usr_inst().getSessionId()));
		data.put("username", User.get_usr_inst().getUsername());
		data.put("match_id", Integer.toString(match_id));
		
		String json_body = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
		
		CloseableHttpClient client = HttpClients.createDefault();
		
		HttpPost post = new HttpPost(URL + "winner");
		post.setEntity(new StringEntity(json_body, ContentType.APPLICATION_JSON));
		post.setHeader("Accept", "application/json");
		
		@SuppressWarnings("deprecation")
		CloseableHttpResponse response = client.execute(post);
		int status_code = response.getCode();
		
		return status_code;
	}
	
	public static int tie_request(int match_id) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
        Map<String, String> data = new LinkedHashMap<>();
        data.put("session_id", Integer.toString(User.get_usr_inst().getSessionId()));
		data.put("username", User.get_usr_inst().getUsername());
		data.put("match_id", Integer.toString(match_id));
		
		String json_body = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
		
		CloseableHttpClient client = HttpClients.createDefault();
		
		HttpPost post = new HttpPost(URL + "tie");
		post.setEntity(new StringEntity(json_body, ContentType.APPLICATION_JSON));
		post.setHeader("Accept", "application/json");
		
		@SuppressWarnings("deprecation")
		CloseableHttpResponse response = client.execute(post);
		int status_code = response.getCode();
		
		return status_code;
	}
	
	public static int progress_request(int match_id) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
        Map<String, String> data = new LinkedHashMap<>();
        data.put("session_id", Integer.toString(User.get_usr_inst().getSessionId()));
		data.put("username", User.get_usr_inst().getUsername());
		data.put("match_id", Integer.toString(match_id));
		
		String json_body = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
		
		CloseableHttpClient client = HttpClients.createDefault();
		
		HttpPost post = new HttpPost(URL + "progress");
		post.setEntity(new StringEntity(json_body, ContentType.APPLICATION_JSON));
		post.setHeader("Accept", "application/json");
		
		@SuppressWarnings("deprecation")
		CloseableHttpResponse response = client.execute(post);
		int status_code = response.getCode();
		
		return status_code;
	}
	
	public static int matches_request() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
        Map<String, String> data = new LinkedHashMap<>();
        data.put("session_id", Integer.toString(User.get_usr_inst().getSessionId()));
		data.put("username", User.get_usr_inst().getUsername());
		
		String json_body = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
		
		CloseableHttpClient client = HttpClients.createDefault();
		
		HttpPost post = new HttpPost(URL + "matches");
		post.setEntity(new StringEntity(json_body, ContentType.APPLICATION_JSON));
		post.setHeader("Accept", "application/json");
		
		@SuppressWarnings("deprecation")
		CloseableHttpResponse response = client.execute(post);
		int status_code = response.getCode();
		
		String body = EntityUtils.toString(response.getEntity());
		
		ObjectAccessController.getMatches().clear();
		
		if(status_code == 200) {
			JsonNode json = mapper.readTree(body);
			
			json.forEach(match -> {
				Match load_match = new Match(match.get("match_id").asInt(), match.get("player_1").asText(), match.get("player_2").asText(), match.get("status").asText());
				if(load_match.getPlayer_1().compareTo(User.get_usr_inst().getUsername()) == 0)
					load_match.setSeed(match.get("seed_1").asText());
				else
					load_match.setSeed(match.get("seed_2").asText());
				load_match.setResult(match.get("result").asText());
				JsonNode stepsNode = json.get("steps");
				if(stepsNode != null)
					stepsNode.forEach(node -> load_match.translate(node.get("step").asText()));
				ObjectAccessController.getMatches().add(load_match);
			});
		}
		
		return status_code;
	}
}
