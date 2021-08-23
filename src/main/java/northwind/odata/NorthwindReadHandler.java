package northwind.odata;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import northwind.documents.Customer;

@Component
public class NorthwindReadHandler {

	private Gson gson = new GsonBuilder().setPrettyPrinting().create();

	public Map<String, Object> readEntity(String id, Class klass) {
		Map<String, Object> entity = new HashMap<>();
		if (klass.equals(Customer.class)) {
			List<Customer> customersDeserialized = deserializeIntoCustomers();
			Optional<Customer> customerOptional = customersDeserialized.stream()
			.filter(customer -> customer.getCustomerID().equals(id))
			.findFirst();
			if(customerOptional.isPresent()) {
				String json = gson.toJson(customerOptional.get());
				Type listType = new TypeToken<Map<String, Object>>() {
				}.getType();
				Map<String, Object> customer = gson.fromJson(json, listType);
				return customer;
			}
			else {
				return entity;
			}
		}
		return entity;
	}

	public List<Map<String, Object>> readEntitySet(Class klass) {
		List<Map<String, Object>> customers = new ArrayList<>();
		if (klass.equals(Customer.class)) {
			List<Customer> customersDeserialized = deserializeIntoCustomers();
			String json = gson.toJson(customersDeserialized);
			return deserializeIntoMap(json);
		}
		return customers;
	}

	private String getJsonString() {
		String json = null;
		try {
			InputStream in = this.getClass().getClassLoader().getResourceAsStream("customer.json");
			json = IOUtils.toString(in, Charset.defaultCharset());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}

	private List<Map<String, Object>> deserializeIntoMap(String json) {
		Type listType = new TypeToken<List<Map<String, Object>>>() {
		}.getType();
		List<Map<String, Object>> customers = gson.fromJson(json, listType);
		return customers;
	}

	private List<Customer> deserializeIntoCustomers()  {
		String json = getJsonString();
		JsonParser parser = null;
		try {
			 parser = new JsonParser();
		}catch(Exception e) {
			e.printStackTrace();
		}
		JsonElement rootElement = parser.parse(json);
		if (rootElement instanceof JsonArray) {
			JsonArray customersJson = rootElement.getAsJsonArray();
			List<Customer> customers = StreamSupport.stream(
					Spliterators.spliteratorUnknownSize(customersJson.iterator(), Spliterator.ORDERED), false)
					.filter(JsonElement::isJsonObject)
					.map(JsonElement::getAsJsonObject)
					.filter(element -> element.has("CustomerID"))
					.map(NorthwindReadHandler::convertJsonObjectToCustomer)
					.collect(Collectors.toList());
			System.out.println(customers.size());
			return customers;
		}
		return Collections.<Customer>emptyList();
	}

	public static Customer convertJsonObjectToCustomer(JsonObject json) {
		Customer customer = new Customer();
		customer.setCity(json.get("City").getAsString());
		customer.setCountry(json.get("Country").getAsString());
		customer.setCustomerID(json.get("CustomerID").getAsString());
		customer.setCustomerName(json.get("ContactName").getAsString());
		customer.setPhone(json.get("Phone").getAsString());
		JsonElement regionNode = json.get("Region");
		if (!(regionNode instanceof JsonNull)) {
			customer.setRegion(json.get("Region").getAsString());
		}
		customer.setZip(json.get("PostalCode").getAsString());
		return customer;
	}
}
