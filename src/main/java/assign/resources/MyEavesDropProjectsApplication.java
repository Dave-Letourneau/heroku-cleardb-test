package assign.resources;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/myeavesdrop")
public class MyEavesDropProjectsApplication extends Application {
	
	private Set<Object> singletons = new HashSet<Object>();
	private Set<Class<?>> classes = new HashSet<Class<?>>();
	
	public MyEavesDropProjectsApplication() {		
	}
	
	@Override
	public Set<Class<?>> getClasses() {
		classes.add(MyEavesDropProjectsResource.class);
		return classes;
	}
	
	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
	
	
}
