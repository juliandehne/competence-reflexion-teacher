package uzuzjmd.competence.reflexion;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.primefaces.context.RequestContext;

import uzuzjmd.competence.reflexion.util.AppUtil;
 
@ViewScoped
@ManagedBean(name="userLoginView")
public class UserLoginView implements Serializable {
     
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	@ManagedProperty(value="#{activityCompetenceView}")
	private ActivityCompetenceView activityCompetenceView;
	
	@ManagedProperty(value="#{courseCompetenceView}")
	private CourseCompetenceView courseCompetenceView;
	
	public void setActivityCompetenceView(
			ActivityCompetenceView activityCompetenceView) {
		this.activityCompetenceView = activityCompetenceView;
	}
	
	public ActivityCompetenceView getActivityCompetenceView() {
		return activityCompetenceView;
	}
	
	public void setCourseCompetenceView(
			CourseCompetenceView courseCompetenceView) {
		this.courseCompetenceView = courseCompetenceView;
	}
	
	public CourseCompetenceView getCourseCompetenceView() {
		return courseCompetenceView;
	}
	
	@ManagedProperty(value = "#{loggedIn}")
    private Boolean loggedIn = false;
	
	@ManagedProperty(value = "#{username}")
    private String username;    
	
	@ManagedProperty(value = "#{password}")
	private String password;
 
    public String getUsername() {
        return username;
    }
 
    public void setUsername(String username) {
        this.username = username;
    }
 
    public String getPassword() {
        return password;
    }
 
    public void setPassword(String password) {
        this.password = password;
    }
    
    
    public void setLoggedIn(Boolean loggedIn) {
		this.loggedIn = loggedIn;
	}
    
    public Boolean getLoggedIn() {
		return loggedIn;
	}
    
    public void login(ActionEvent event) {
    	System.err.println("Trying to login");
    	
        RequestContext context = RequestContext.getCurrentInstance();
        FacesMessage message = null;
   
        
        final StringBuilder url = new StringBuilder();
		url.append(AppUtil.getBaseUrl())
		.append("/api1/users/").append(username).append("/exists");
		
        Client client = ClientBuilder.newClient();
		Boolean result = client.target(url.toString())
				.queryParam("password", password)
				.request()
				.get(Boolean.class);
		loggedIn = result;
		
		System.err.println("loggged in: "+ loggedIn);
        
        if(loggedIn) {            
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Welcome", username);
            courseCompetenceView.update(username, password);
            activityCompetenceView.update(username, password);
        } else {
            loggedIn = false;
            message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Loggin Error", "Invalid credentials");
        }
         
        FacesContext.getCurrentInstance().addMessage(null, message);
        context.addCallbackParam("loggedIn", loggedIn);
    
        //CourseCompetenceView.update();
        
    }   
}
