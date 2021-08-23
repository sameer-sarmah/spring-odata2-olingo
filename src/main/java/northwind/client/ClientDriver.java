package northwind.client;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpMethod;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import northwind.exception.CoreException;

public class ClientDriver {
	private static final String userService = "https://localhost:8080/api/user";
	private static final String permissionService = "https://localhost:8080/api/user/%s/%s";
	private static final String READ = "READ";
	private static final String WRITE = "WRITE";

	public static void main(String[] args) throws IOException {
		InputStream in = ClientDriver.class.getClassLoader().getResourceAsStream("customer.json");
		String json = IOUtils.toString(in, Charset.defaultCharset());
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(json);
		System.out.println();
	}

	private static void executeHttpClient(SpringHttpClient httpClient, String service, HttpMethod method,
			Map<String, String> headers, Map<String, String> queryParams, String payload) {
		try {
			String jsonResponse = httpClient.request(service, method, headers, queryParams, payload);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			JsonParser jp = new JsonParser();
			JsonElement je = jp.parse(jsonResponse);
			String prettyJsonString = gson.toJson(je);
			System.out.println(prettyJsonString);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
}
