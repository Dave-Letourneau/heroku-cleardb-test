package assign.services;

import java.util.List;

// import assign.domain.Assignment;
import assign.domain.Meeting;
import assign.domain.Project;
//import assign.domain.UTCourse;

public interface MyEavesDropService {

	
	// We need to change this to follow DBLoader's example.
	// Add new functionality below 
	
	
	// Add a project by itself 
	public Long addProject(Project p) throws Exception;
	
	//public Long addMeeting(String title) throws Exception;
	
	// Add project with a Meeting
	//public Long addMeetingAndProject(String title, String projectTitle) throws Exception;
	
	// Update a meeting into a project
	public Long addMeetingsToProject(Meeting meeting, Long projectId) throws Exception;
	
	// Get meetings for a specific project (GOOD PRACTICE ONLY)
	public List<Meeting> getMeetingsForAProject(Long id) throws Exception;
	
	// Get a meeting????
	public Meeting getMeeting(String title) throws Exception;
	
	// Get a project
	public Project getProject(String projectName) throws Exception;
	
	// Delete a meeting
	public void deleteMeeting(String title) throws Exception;
	
	// Delete a project (should cascade meetings?)
	public void deleteProject(Long id) throws Exception;
	
	// Get a specific meeting? Again? 
	public Meeting getMeeting(Long meetingId) throws Exception;
	
	public Project getProject(Long projectId) throws Exception;
	
	public Long updateMeeting(Meeting meeting, Long projectId, Long meetingId) throws Exception;
}
