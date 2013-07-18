package com.foodspot.service;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import com.foodspot.domain.FoodTruck;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

@Stateless
public class YelpService {

	private static final String YELP_SEARCH_API = "http://api.yelp.com/v2/search";
	private static final String YELP_BIZ_API = "http://api.yelp.com/v2/business";
	private static final String BUSINESSES_KEY = "businesses";
	private static final String ADDRESS_KEY = "display_address";
	private static final String LOCATION_KEY = "location";
	private static final String CATEGORY_PARAM = "Food Trucks, Food Stands, Street Vendors";

	private OAuthService service;
	private Token accessToken;

	@PostConstruct
	public void init() {
		this.service = new ServiceBuilder().provider(YelpApi2.class)
				.apiKey("lzx5XGHC3xorr88e7YPnIQ")
				.apiSecret("mvXKWEhUP99Mlo_MfpC3ty9mtz4").build();
		this.accessToken = new Token("rER5Oqi79KQaXw_Tq_S6Pm3XLSDkVxQk",
				"-vr0hdrAWfMxLEqsmizOZuUuZ64");
	}
	
	public FoodTruck getFoodTruck(String foodTruckId){
		OAuthRequest request = new OAuthRequest(Verb.GET,
				YELP_BIZ_API + "/" + foodTruckId);
		this.service.signRequest(this.accessToken, request);
		Response response = request.send();
		JsonParser jsonParser = new JsonParser();
		JsonObject jsonObject = jsonParser.parse(response.getBody())
				.getAsJsonObject();
		Gson gson = getGson();
		return gson.fromJson(jsonObject, FoodTruck.class);
	}

	// TODO limits and offset parameters for paging
	public List<FoodTruck> searchFoodTrucks(String latitude, String longitude) {
		OAuthRequest request = new OAuthRequest(Verb.GET,
				YELP_SEARCH_API);
		request.addQuerystringParameter("term", CATEGORY_PARAM);
		request.addQuerystringParameter("ll", latitude + "," + longitude);
		this.service.signRequest(this.accessToken, request);
		Response response = request.send();
		JsonParser jsonParser = new JsonParser();
		JsonObject jsonObject = jsonParser.parse(response.getBody())
				.getAsJsonObject();
		JsonElement jsonElement = jsonObject.get(BUSINESSES_KEY);
		if (jsonElement != null) {
			Gson gson = getGson();
			return Lists.newArrayList(gson.fromJson(jsonElement,
					FoodTruck[].class));
		}
		return Collections.emptyList();
	}

	public static class YelpApi2 extends DefaultApi10a {
		@Override
		public String getAccessTokenEndpoint() {
			return null;
		}

		@Override
		public String getAuthorizationUrl(Token arg0) {
			return null;
		}

		@Override
		public String getRequestTokenEndpoint() {
			return null;
		}
	}

	private class FoodTruckTypeAdapter implements JsonDeserializer<FoodTruck> {
		@Override
		public FoodTruck deserialize(JsonElement json, Type typeOfT,
				JsonDeserializationContext context) throws JsonParseException {
			JsonObject foodTruckObj = json.getAsJsonObject();
			Gson gson = new GsonBuilder()
					.excludeFieldsWithoutExposeAnnotation().create();
			FoodTruck foodTruck = gson.fromJson(foodTruckObj, FoodTruck.class);
			foodTruck.setExternalId(foodTruckObj.get("id").getAsString());
			JsonObject locationObj = foodTruckObj.getAsJsonObject(LOCATION_KEY);
			JsonArray addressArray = locationObj.get(ADDRESS_KEY).getAsJsonArray();
			Iterator<JsonElement> addressIterator = addressArray.iterator();
			StringBuilder address = new StringBuilder();
			while(addressIterator.hasNext()){
				JsonElement addressField = addressIterator.next();
				address.append(addressField.getAsString());
				address.append(" ");
			}
			foodTruck.setAddress(address.toString());
			return foodTruck;
		}

	}

	public Gson getGson() {
		GsonBuilder gsonBuilder = new GsonBuilder()
				.excludeFieldsWithoutExposeAnnotation();
		gsonBuilder.registerTypeAdapter(FoodTruck.class,
				new FoodTruckTypeAdapter());
		return gsonBuilder.create();
	}
}
