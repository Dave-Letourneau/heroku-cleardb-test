package assign.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlAttribute;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table( name = "projects" )
@XmlRootElement(name = "project")
@XmlAccessorType(XmlAccessType.FIELD)
public class Project {
	
	//@XmlAttribute
	private Long id;
    private String name;
    private String description;
    @XmlElementWrapper
    private Set<Meeting> meetings;

    public Project() {
    	// this form used by Hibernate
    }
    
    public Project(String name, String description) {
    	this.name = name;
    	this.description = description;
    }
    
    @Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
    public Long getId() {
		return id;
    }

    private void setId(Long id) {
		this.id = id;
    }
    
    @Column(name="project")
    public String getProjectName() {
		return name;
    }
    
//    @Column(name="project")
    public String getProjectDescription() {
    	return description;
    }

    public void setProjectName(String name) {
		this.name = name;
    }
    
    public void setProjectDescription(String description) {
    	this.description = description;
    } 
    
    @OneToMany(fetch=FetchType.EAGER,mappedBy="project")
    @Cascade({CascadeType.DELETE})
    public Set<Meeting> getMeetings() {
    	return this.meetings;
    }
    
    public void setMeetings(Set<Meeting> meetings) {
    	this.meetings = meetings;
    }
}