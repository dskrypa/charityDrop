package resources.misc;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import resources.eao.AccountManager;
import resources.entities.Address;
import resources.entities.User;

@ManagedBean(name="accBean")
@SessionScoped()
public class AccountBean {
	private static String[] validStates = {"AL","AK","AZ","AR","CA","CO","CT","DE","FL","GA","HI","ID","IL","IN","IA","KS","KY","LA","ME","MD","MA","MI","MN","MS","MO","MT","NE","NV","NH","NJ","NM","NY","NC","ND","OH","OK","OR","PA","RI","SC","SD","TN","TX","UT","VT","VA","WA","WV","WI","WY"};
	private static List<String> validStateList = Arrays.asList(validStates);
	private static String phoneNumPattern = "^\\(?(\\d{3})\\s*[) -]?\\s*(\\d{3})\\s*-?\\s*(\\d{4})$";
	
	private String email, password, confirmpassword, oldpassword;
	private String addrLine1, addrLine2, city, usState, zip, xStreet;
	private String firstname, lastname;
	private String phone, contactMethod;
	
	@EJB AccountManager am;
	
	public AccountBean(){}
	
	private void addMessage(FacesMessage message){
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	public void zipValidator(FacesContext context, UIComponent component, Object value){
		String zip = (String)value;
		Matcher m = Pattern.compile("^\\d{5}$").matcher(zip);
		if(!m.matches()){
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid zip code. Must be 5 digits.", null));
		}
	}
	
	public void stateValidator(FacesContext context, UIComponent component, Object value){
		String usState = (String) value;
		if(!validStateList.contains(usState.toUpperCase())){
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid state. Use the 2 letter abbreviation.", null));
		}
	}
	
	public String validateLogin(){
		if(am.validateLogin(this)){
			String uemail = this.getEmail();
			SessionBean sb = Util.getBean("sessionBean", SessionBean.class);
			sb.setUserEmail(uemail);
			sb.updateDonationList();
			return "success";
		}
		addMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR, "The email or password that you entered is invalid.", null));
		return "failure";
	}
	
	public String changePassword(){
		SessionBean sb = Util.getBean("sessionBean", SessionBean.class);
		User u = sb.getUser();
		String hOld = Util.hash(oldpassword);
		String hNew = Util.hash(password);
		oldpassword = null;
		password = null;
		confirmpassword = null;
		if(am.validatePassword(u, hOld)){
			if(am.updatePassword(u, hNew)){
				u.setPassword(hNew);
				addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Password successfully changed.", null));
				return "success";
			}
			addMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to change password.", null));
			return "failure";
		}
		addMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Incorrect password.", null));
		return "failure";
	}
	
	public String deleteAccount(){
		SessionBean sb = Util.getBean("sessionBean", SessionBean.class);
		User u = sb.getUser();
		String hpw = Util.hash(password);
		if(am.validatePassword(u, hpw)){
			if(am.deleteUser(u)){
				addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Account deleted.", null));
				return "success";
			}
			addMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to delete account.", null));
			return "failure";
		}
		addMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Incorrect password.", null));
		return "failure";
	}
	
	public String addAddress(){
		Address a = am.persistAddress(this);
		if(a != null){
			SessionBean sb = Util.getBean("sessionBean", SessionBean.class);
			sb.addAddress(a);
			return "success" + sb.getPrevPage();
		}
		return "failure";
	}
	
	public String addUser(){
		if(!am.userExists(this)){
			if(am.persistUser(this)){
				addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "User registration successful", null));
				return "success";
			}
		} else {
			addMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR, "That username is already in use.", null));
			return "failure";
		}
		addMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR, "User registration failed", null));
		return "failure";
	}
	
	public void pwValidator(FacesContext context, UIComponent component, Object value){
		UIInput passwordComponent = (UIInput) component.getAttributes().get("password");
		String password = (String) passwordComponent.getValue();
		String confirm = (String) value;

		if(!password.equals(confirm)){
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Passwords dont match!!!", null));
		}
	}
	
	public void emailValidator(FacesContext context, UIComponent component, Object value){
		String email = (String) value;
		if(email.indexOf("@") < 0 || email.indexOf("@") == email.length()-1){
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid email address. Should be in the form user@domain.ext", null));
		}
	}
	
	public void phoneValidator(FacesContext context, UIComponent component, Object value){
		String phoneNum = (String) value;
		Matcher m = Pattern.compile(phoneNumPattern).matcher(phoneNum.trim());
		if(!m.matches()){
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid phone number.  Must consist of 10 digits.", null));
		}
	}
	
	public String getPassword() {		return password;}
	public String getConfirmpassword() {return confirmpassword;}
	public String getLastname() {		return lastname;}
	public String getFirstname() {		return firstname;}
	public String getEmail() {			return email;}
	public String getPhone() {			return phone;}
	public String getContactMethod() {	return contactMethod;}
	public String[] getValidStates() {	return validStates;}
	public String getOldpassword() {	return oldpassword;}
	public String getAddrLine1() {		return addrLine1;}
	public String getAddrLine2() {		return addrLine2;}
	public String getCity() {			return city;}
	public String getUsState() {		return usState;}
	public String getZip() {			return zip;}
	public String getxStreet() {		return xStreet;}
	
	
	public void setPassword(String password) {	this.password = password;}
	public void setConfirmpassword(String cpw) {this.confirmpassword = cpw;}
	public void setLastname(String lastname) {	this.lastname = lastname;}
	public void setFirstname(String firstname) {this.firstname = firstname;}
	public void setEmail(String email) {		this.email = email;}
	public void setContactMethod(String cm) {	this.contactMethod = cm;}
	public void setOldpassword(String opw) {	this.oldpassword = opw;}
	public void setAddrLine1(String addrLine1) {this.addrLine1 = addrLine1;}
	public void setAddrLine2(String addrLine2) {this.addrLine2 = addrLine2;}
	public void setCity(String city) {			this.city = city;}
	public void setUsState(String usState) {	this.usState = usState;}
	public void setZip(String zip) {			this.zip = zip;}
	public void setxStreet(String xStreet) {	this.xStreet = xStreet;}
	
	public void setPhone(String phone) {
		Matcher m = Pattern.compile(phoneNumPattern).matcher(phone.trim());
		if(m.matches()){
			this.phone = m.group(1) + m.group(2) + m.group(3);
		}
	}	
}