package assign.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table( name = "meetings" )
@XmlRootElement(name = "meeting")
@XmlAccessorType(XmlAccessType.NONE)
public class Meeting {
	
	private Long id;
	@XmlElement
    private String name;
	@XmlElement
    private String year;
    private Project project; // course or something else
    
    public Meeting() {
    	// this form used by Hibernate
    }
    
    public Meeting(String name, String year) {
    	// for application use, to create new meeting
    	this.name = name;
    	this.year = year;
    }
    
    public Meeting(String name, String year, Long providedId) {
    	// for application use, to create new meeting
    	this.name = name;
    	this.year = year;
    	this.id = providedId;
    }    
    
    @Id    
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Long getId() {
		return id;
    }

    private void setId(Long id) {
		this.id = id;
    }

	@Column(name = "MEETING_YEAR")
    public String getYear() {
		return year;
    }

    public void setYear(String year) {
		this.year = year;
    }
    
    @ManyToOne
    @JoinColumn(name="project_id")
    public Project getProject() {
    	return this.project;
    }
    
    
    
    public void setProject(Project c) {
    	this.project = c;
    }

    public String getTitle() {
		return name;
    }

    public void setTitle(String name) {
		this.name = name;
    }
}
