package com.example.mymap;

/*
 Example code based on code from Nicholas Smith at http://imnes.blogspot.com/2011/01/how-to-use-yelp-v2-from-java-including.html
 For a more complete example (how to integrate with GSON, etc) see the blog post above.
 */

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeSet;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

/**
 * Example for accessing the Yelp API.
 */
public class Yelp {

  OAuthService service;
  Token accessToken;

  /**
   * Setup the Yelp API OAuth credentials.
   *
   * OAuth credentials are available from the developer site, under Manage API access (version 2 API).
   *
   * @param consumerKey Consumer key
   * @param consumerSecret Consumer secret
   * @param token Token
   * @param tokenSecret Token secret
   */
  public Yelp(String consumerKey, String consumerSecret, String token, String tokenSecret) {
    this.service = new ServiceBuilder().provider(Yelp2.class).apiKey(consumerKey).apiSecret(consumerSecret).build();
    this.accessToken = new Token(token, tokenSecret);
  }

  /**
   * Search with term and location.
   *
   * @param term Search term
   * @param latitude Latitude
   * @param longitude Longitude
   * @return JSON string response
   */
  public String search(String term, double latitude, double longitude) {
    OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.yelp.com/v2/search");
    request.addQuerystringParameter("term", term);
    request.addQuerystringParameter("ll", latitude + "," + longitude);
    this.service.signRequest(this.accessToken, request);
    Response response = request.send();
    return response.getBody();
  }

  // CLI
  public static void main(String[] args) {
    // Update tokens here from Yelp developers site, Manage API access.
    String consumerKey = "T9vdL6ywOKZUoewhLG9zNg";
    String consumerSecret = "NiCem0czmbjdhpZQ9KBodAcIwPo";
    String token = "ds17XrW7br8LGhg9pAoniPfgUuCPsMhY";
    String tokenSecret = "CpI3orQyC6mYR9V4AbMCOaafZ24";

    Yelp yelp = new Yelp(consumerKey, consumerSecret, token, tokenSecret);
    String response = yelp.search("restaurant", 37.7833, -122.4167);

    System.out.println(response);
  }
  static <K,V extends Comparable<? super V>>
  SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
      SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
          new Comparator<Map.Entry<K,V>>() {
              public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
                  return e1.getValue().compareTo(e2.getValue());
              }
          }
      );
      sortedEntries.addAll(map.entrySet());
      return sortedEntries;
  }

  public static List<String> getRecommendationsForUser(String thisUserId) {
	  if (thisUserId == null) return null;
	  
	  // Get friends list for thisUserId
	  List<String> friendUserIds =  getFriendsListForUser(thisUserId);
	  if (friendUserIds == null) return null;
	  Map<String, Integer> recommendations = new HashMap<String, Integer>();
	  
	  // Get purchases made by thisUserId
	  List<String> thisUserPurchases = getPurchasesForUser(thisUserId);

	  for (String friendUserId: friendUserIds) {
		  List<String> userPurchases = getPurchasesForUser(friendUserId);
		  for (String userPurchase: userPurchases) {
			  if (thisUserPurchases.contains(userPurchase)) {
				  continue; // Ignore if already purchased by thisUserId
			  }
			  int productCount = 0;
			  if (recommendations.containsKey(userPurchase)) {
				  productCount = recommendations.get(userPurchase);
			  }
			  recommendations.put(userPurchase, productCount++);
		  }
	  }

	  // Sort map by values
	  SortedSet<Entry<String, Integer>> sortedByCount = entriesSortedByValues(recommendations);
	  List<String> recommendationList = new ArrayList<String>();
	  Iterator<Entry<String, Integer>> it = sortedByCount.iterator();
	  while (it.hasNext()) {
		  recommendationList.add(it.next().getKey());
	  }
	  return recommendationList;
  }

  public void testChecksUserId() {
	  assertsNull(Yelp.getRecommendationsForUser(null));
  }

  public void testFailsForNonExistentUserId() {
	  expect(Yelp.getPurchasesForUser("5")).andReturn(null).times(1);
	  assertsNull(Yelp.getRecommendationsForUser("5"));
  }

  public void testPassesThisUserIdToGetPurchasesForUser() {
	  List<String> purchases = {"ipad", "ipod", "kindle"}
	  expect(Yelp.getPurchasesForUser("5")).andReturn(purchases).times(1);
	  Yelp.getRecommendationsForUser("5");
  }

  public static List<String> getPurchasesForUser(String thisUserId) {
	// TODO Auto-generated method stub
	return null;
}

public static List<String> getFriendsListForUser(String thisUserId) {
	// TODO Auto-generated method stub
	return null;
}

}