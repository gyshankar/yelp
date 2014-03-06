package com.example.mymap;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class Geocode {

	public static Latlng geocode(String address) {
		Latlng ll = new Latlng();
		RestTemplate rt = new RestTemplate();
		//rt.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
        rt.getMessageConverters().add(new StringHttpMessageConverter());
        
        Map<String, String> vars = new HashMap<String, String>();
        vars.put("address", address);
		String url = "http://maps.googleapis.com/maps/api/geocode/json?sensor=false&address={address}";
		String str = rt.getForObject(url, String.class, vars);
		System.out.println(str);
		return ll;
	}
	
	public static void main(String[] args) {
		System.out.println(Geocode.geocode("1600 Amphitheatre Parkway, Mountain View, CA"));
	}
}
