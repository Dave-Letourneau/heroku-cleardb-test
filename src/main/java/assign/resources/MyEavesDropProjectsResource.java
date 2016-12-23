package assign.resources;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.List;

import javax.servlet.ServletContext;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import assign.domain.Project;
import assign.domain.Meeting;
import assign.services.MyEavesDropService;
import assign.services.MyEavesDropServiceImpl;

@Path("/projects")
public class MyEavesDropProjectsResource {
	
	MyEavesDropService projectService;
	String password;
	String username;
	String dbname;	
	String dbhost;
	AtomicInteger idCounter = new AtomicInteger();
	
	// Get resources from WEB.XML
	public MyEavesDropProjectsResource(@Context ServletContext servletContext) {
		dbname = servletContext.getInitParameter("DBNAME");
		dbhost = servletContext.getInitParameter("DBHOST");
		username = servletContext.getInitParameter("DBUSERNAME");
		password = servletContext.getInitParameter("DBPASSWORD");
		// change these? 
		this.projectService = new MyEavesDropServiceImpl(dbname, username, password, dbhost);		
	}
	
	
	// Below is content for assignment 5
	
	// POST a project 
	@POST
	@Consumes("application/xml") // don't actually know if this is needed 
	public Response createProject(String xml) throws Exception {
		Project p = unmarshallProject(xml);
		// Throw 400 if error occurs
		if (p == null) {
			throw new WebApplicationException("Bad Request", Response.Status.BAD_REQUEST);
		}
		// pass created project to hibernate
		projectService.addProject(p);
		System.out.println("Created your project");
		return Response.created(URI.create("/projects/" + p.getId())).build();
	}
	
	// Post a meeting for a project
	@POST
	@Path("/{projectId}/meetings")
	@Consumes("application/xml")
	public Response createMeeting(@PathParam("projectId") Long id, String xml) throws Exception {
		Meeting m = unmarshallMeeting(xml);
		// Throw 400 if error occurs
		if (m == null) {
			throw new WebApplicationException("Bad Request", Response.Status.BAD_REQUEST);
		}
		// pass created meeting to hibernate
		projectService.addMeetingsToProject(m, id);
		System.out.println("Created your meeting");
		return Response.created(URI.create("/projects/" + id + "/meetings/" + m.getId())).build();
	}
	
	// PUT a meeting
	@PUT
	@Path("/{projectId}/meetings/{meetingId}")
	@Consumes("application/xml")
	public Response updateMeeting(@PathParam("projectId") Long Pid, @PathParam("meetingId") Long Mid, String xml) throws Exception {
		Meeting m = unmarshallMeeting(xml);
		if (m == null) {
			// Bad request check
			throw new WebApplicationException("Bad Request", Response.Status.BAD_REQUEST);
		}
		// Check for a project and Meeting
		Project checkProject = projectService.getProject(Pid);
		Meeting checkMeeting = projectService.getMeeting(Mid);
		if (checkProject == null || checkMeeting == null) {
			throw new WebApplicationException("Not Found", Response.Status.NOT_FOUND);
		}
		projectService.updateMeeting(m, Pid, Mid);
		return Response.ok().build();
	}
	
	@GET
	@Path("/{projectId}")
	@Produces("application/xml")
	public StreamingOutput getProjectDetails(@PathParam("projectId") Long id) throws Exception {
		final Project p;
		
		p = projectService.getProject(id);
		if (p == null) {
			throw new WebApplicationException("Not Found", Response.Status.NOT_FOUND);
		} else {
			
			return new StreamingOutput() {
				
		         public void write(OutputStream outputStream) throws IOException, WebApplicationException {
		            outputProject(outputStream, p); // here and 278
		         }
		      };
		}
		
	}
	
	// DELETE A PROJECT
	@DELETE
	@Path("/{projectId}") 
	public Response Delete(@PathParam("projectId") Long id) throws Exception {
		// Try to delete project
		try {
			projectService.deleteProject(id);
		} catch (Exception e) {
			throw new WebApplicationException("Not Found", Response.Status.NOT_FOUND);
		}
		
		return Response.ok().build();
	}
	
	// Repurposed unmarshaller to be for projects
	public Project unmarshallProject(String xml) {
		try {
			JAXBContext jc = JAXBContext.newInstance(Project.class);
			Unmarshaller u = jc.createUnmarshaller();
			StringReader sr = new StringReader(xml);
			
			Project nc = (Project)u.unmarshal(sr);
			// If anything is null or empty ("") return 400 Bad Request
			if ((nc.getProjectName().equals("") || nc.getProjectName() == null)
					|| (nc.getProjectDescription().equals("") || nc.getProjectDescription() == null)) {
				return null;
			}
			return nc;
		} catch(Exception e) {
			System.out.println("Couldn't unmarshal Reader, sorry boss");
			return null;
		}	
	}
	
	public Meeting unmarshallMeeting(String xml) {
		try {
			JAXBContext jc = JAXBContext.newInstance(Meeting.class);
			Unmarshaller u = jc.createUnmarshaller();
			StringReader sr = new StringReader(xml);
			
			Meeting m = (Meeting)u.unmarshal(sr);
			// If anything is null or empty ("") return 400 Bad Request
			if ((m.getTitle().equals("") || m.getTitle() == null)
					|| (m.getYear().equals("") || m.getYear() == null)) {
				return null;
			}
			return m;
		} catch(Exception e) {
			System.out.println("Couldn't unmarshal Reader");
			return null;
		}	
	}
	
	
	@GET
	@Path("/helloworld")
	@Produces("text/html")
	public String helloWorld() {
		System.out.println("Inside helloworld");
		System.out.println("DB creds are:");
		System.out.println("DBNAME:" + dbname);
		System.out.println("DBUsername:" + username);
		System.out.println("DBPassword:" + password);
		System.out.println("DBHOST:" + dbhost);
		return "Hello world dingos";		
	}	
	
	protected void outputProject(OutputStream os, Project p) throws IOException {
		try { 
			JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	 
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(p, os);
		} catch (JAXBException jaxb) {
			jaxb.printStackTrace();
			throw new WebApplicationException();
		}
	}
	
}
