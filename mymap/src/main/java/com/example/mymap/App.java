package com.example.mymap;
 
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
 
@Controller
@RequestMapping("/mymaps")
public class App {

	@RequestMapping(value = "/{name}", method = RequestMethod.GET)
	public String search(@PathVariable String name, ModelMap model) {
	    // Update tokens here from Yelp developers site, Manage API access.
	    String consumerKey = "T9vdL6ywOKZUoewhLG9zNg";
	    String consumerSecret = "NiCem0czmbjdhpZQ9KBodAcIwPo";
	    String token = "ds17XrW7br8LGhg9pAoniPfgUuCPsMhY";
	    String tokenSecret = "CpI3orQyC6mYR9V4AbMCOaafZ24";

	    Yelp yelp = new Yelp(consumerKey, consumerSecret, token, tokenSecret);
	    String response = yelp.search(name, 30.361471, -87.164326);
		model.addAttribute("movie", response);
		return "list";
	}
}
