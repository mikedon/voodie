package com.foodspot.service;

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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Stateless
public class YelpService {

	private static final String BUSINESSES_KEY = "businesses";
	private static final String CATEGORY_PARAM = "Food Truck";

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

	public List<FoodTruck> searchFoodTrucks(String location) {
		OAuthRequest request = new OAuthRequest(Verb.GET,
				"http://api.yelp.com/v2/search");
		request.addQuerystringParameter("term", CATEGORY_PARAM);
		request.addQuerystringParameter("location", location);
		this.service.signRequest(this.accessToken, request);
		Response response = request.send();
		JsonParser jsonParser = new JsonParser();
		JsonObject jsonObject = jsonParser.parse(response.getBody())
				.getAsJsonObject();
		JsonElement jsonElement = jsonObject.get(BUSINESSES_KEY);
		Gson gson = new Gson();
		// TODO custom mapper
		return Lists
				.newArrayList(gson.fromJson(jsonElement, FoodTruck[].class));
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
}
