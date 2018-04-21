package br.com.livro.rest;

import java.util.HashMap;
//import java.util.HashSet;
import java.util.Map;
//import java.util.Set;

import javax.ws.rs.core.Application;

//import org.glassfish.jersey.jettison.JettisonFeature;

public class MyApplication extends Application{
	
/*	@Override
	public Set<Object> getSingletons() {
		Set<Object> singletons = new HashSet<>();
		singletons.add(new JettisonFeature());
		singletons.add(new JettisonFeature());
		return singletons;
	}*/
	
	@Override
	public Map<String, Object> getProperties() {
		Map<String,Object> properties = new HashMap<>();
		properties.put("jersey.config.server.provider.packages", "br.com.livro");
		return properties;
	}
}
