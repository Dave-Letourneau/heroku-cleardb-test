package assign.services;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

import assign.domain.Project;
import assign.domain.Meeting;
//import assign.domain.Meeting;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;

public class MyEavesDropServiceImpl implements MyEavesDropService {

//	String dbName = "";
//	String dbhost = "";
//	String dbUsername = "";
//	String dbPassword = "";
//	DataSource ds;
	
	private SessionFactory sessionFactory;

	// DB connection information would typically be read from a config file.
	public MyEavesDropServiceImpl(String dbName, String username, String password, String dbhost) {
//		this.dbName = dbName;
//		this.dbhost = dbhost;
//		this.dbUsername = username;
//		this.dbPassword = password;
//		
//		ds = setupDataSource();
		
		sessionFactory = new Configuration()
                .configure() // configures settings from hibernate.cfg.xml
                .buildSessionFactory();
	}
    
    public Long addProject(Project p) throws Exception {
    	Session session = sessionFactory.openSession();
    	Transaction tx = null;
    	Long projectId = null;
    	try {
    		tx = session.beginTransaction();
    		session.save(p);
    		projectId = p.getId();
    		tx.commit();
    	} catch (Exception e) {
    		if (tx != null) {
    			tx.rollback();
    			throw e;
    		}
    	}
    	finally {
    		session.close();
    	}
    	return projectId;
    }
    
 	
 	// Add a meeting into a project
 	public Long addMeetingsToProject(Meeting meeting, Long projectId) throws Exception {
 		Session session = sessionFactory.openSession();
		Transaction tx = null;
		//Long projectId = null;
		try {
			tx = session.beginTransaction();
			Project project = getProject(projectId);
			//session.save(project);
			// projectId = project.getId();
			// Meeting newMeeting = new Meeting( m, new Integer(1) );
			meeting.setProject(project);
			//newMeeting.setProject(project);
			session.save(meeting);
		    tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
				throw e;
			}
		}
		finally {
			session.close();			
		}
		return projectId;
 	}
 	
 	// Update a project
 	public Long updateMeeting(Meeting meeting, Long projectId, Long meetingId) throws Exception {
 		Session session = sessionFactory.openSession();
		Transaction tx = null;
		//Long projectId = null;
		try {
			tx = session.beginTransaction();
			Project project = getProject(projectId);
			session.update(project);
			Meeting oldMeeting = getMeeting(meetingId);
			session.update(oldMeeting);
			meeting.setProject(project);
			oldMeeting.setTitle(meeting.getTitle());
			
		    tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
				throw e;
			}
		}
		finally {
			session.close();			
		}
		return projectId;
 	}
 	
	// Get meetings for a specific project (GOOD PRACTICE ONLY)
	public List<Meeting> getMeetingsForAProject(Long id) throws Exception {
		Session session = sessionFactory.openSession();		
		session.beginTransaction();
		String query = "from Meeting m join m.project p where p.id = :pid";		
				
		List<Meeting> meetings = session.createQuery(query).setParameter("pid", id).list();
		
		return meetings;
	}
	
	// Get a meeting????
	public Meeting getMeeting(String title) throws Exception {
		Session session = sessionFactory.openSession();
		
		session.beginTransaction();
		
		Criteria criteria = session.createCriteria(Meeting.class).
        		add(Restrictions.eq("title", title));
		
		List<Meeting> meetings = criteria.list();
		
		if (meetings.size() > 0) {
			return meetings.get(0);			
		} else {
			return null;
		}
	}
	
	// Get a project
	public Project getProject(String projectName) throws Exception {
		Session session = sessionFactory.openSession();
		
		session.beginTransaction();
		
		Criteria criteria = session.createCriteria(Project.class).
        		add(Restrictions.eq("projectName", projectName));
		
		List<Project> projects = criteria.list();
		
		if (projects.size() > 0) {
			session.close();
			return projects.get(0);	
		} else {
			session.close();
			return null;
		}
	}
	
	// Delete a meeting
	public void deleteMeeting(String title) throws Exception {
		Session session = sessionFactory.openSession();		
		session.beginTransaction();
		String query = "from Meeting m where m.title = :title";		
				
		Meeting m = (Meeting)session.createQuery(query).setParameter("title", title).list().get(0);
		
        session.delete(m);

        session.getTransaction().commit();
        session.close();
	}
	
	// Delete a project (should cascade meetings?)
	public void deleteProject(Long id) throws Exception {
		Session session = sessionFactory.openSession();		
		session.beginTransaction();
		String query = "from Project p where p.id = :id";		
				
		Project p = (Project)session.createQuery(query).setParameter("id", id).list().get(0);
		
        session.delete(p);

        session.getTransaction().commit();
        session.close();
	}
	
	// Get a specific meeting? Again? 
	public Meeting getMeeting(Long meetingId) throws Exception {
		Session session = sessionFactory.openSession();
		
		session.beginTransaction();
		
		Criteria criteria = session.createCriteria(Meeting.class).
        		add(Restrictions.eq("id", meetingId));
		
		List<Meeting> meetings = criteria.list();
		
		if (meetings.size() > 0) {
			session.close();
			return meetings.get(0);	
		} else {
			session.close();
			return null;
		}
	}
	
	public Project getProject(Long projectId) throws Exception {
		Session session = sessionFactory.openSession();
		
		session.beginTransaction();
		
		Criteria criteria = session.createCriteria(Project.class).
        		add(Restrictions.eq("id", projectId));
		
		List<Project> projects = criteria.list();
		if (projects.size() > 0) {
			session.close();
			return projects.get(0);	
		} else {
			session.close();
			return null;
		}
	}

}
