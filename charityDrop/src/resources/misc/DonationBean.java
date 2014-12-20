package resources.misc;

import java.util.Date;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import resources.eao.DonationManager;
import resources.entities.Address;
import resources.entities.Donation;

@ManagedBean(name="donBean")
@SessionScoped()
public class DonationBean {
	private Integer statusid, userid, addressid, donationid;
	private String donId, addrId;
	private Address address;
	private String refMethod, size, comments;
	private String addrInfo;
	private Date pickupDate;
	@EJB DonationManager dm;
	
	public DonationBean(){}
	
	//TODO: Add transaction type message for confirmation page (new/update)
	
	private void addMessage(FacesMessage message){
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	public String initDonationEdit(){
		if(getDonationid()!=null){
			Donation d = dm.getDonationForId(getDonationid());
			SessionBean sb = Util.getBean("sessionBean", SessionBean.class);
			String donationEmail = d.getUseremail();
			String loggedInUserEmail = sb.getUserEmail();
			if((donationEmail != null) && (!donationEmail.equals(loggedInUserEmail))){
				addMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR, "You must log in with the account used to make this donation", null));
				return "failure";
			}
			sb.setDonationId(getDonationid());
			
			if(!sb.getIsLoggedIn()){
				Address a = dm.getAddressForId(d.getAddressid());
				sb.addAddress(a);
			}
			
			java.util.Date udate = new java.util.Date(d.getPickupDate().getTime());
			
			setAddressid(d.getAddressid());
			setPickupDate(udate);
			setRefMethod(d.getRefMethod());
			setSize(d.getSize());
			setComments(d.getComments());
			return "success";
		}
		addMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to edit donation", null));
		return "failure";
	}
	
	public String cancelDonation(){
		Donation d = dm.getDonationForId(getDonationid());
		if(dm.deleteDonataion(d)){
			SessionBean sb = Util.getBean("sessionBean", SessionBean.class);
			sb.updateDonationList();
			addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Donation successfully cancelled", null));
			return "success";
		}
		addMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to cancel donation", null));
		return "failure";
	}
	
	public void addressValidator(FacesContext context, UIComponent component, Object value){
		Address a = dm.getAddressForId(Integer.parseInt((String) value));
		if(a!=null){
			address = a;
		} else {
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to fetch address", null));
		}
	}
	
	public void dateValidator(FacesContext context, UIComponent component, Object value){
		Date d = (Date) value;
		if((d!=null) && d.before(new Date(System.currentTimeMillis()))){
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot choose a date in the past.", null));
		}
	}
	
	public String addDonation(){
		if(dm.persistDonation(this)){
			setAddrInfo();
			SessionBean sb = Util.getBean("sessionBean", SessionBean.class);
			sb.updateDonationList();
			addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Donation scheduling successful", null));
			return "success";
		}
		addMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Donation scheduling failed", null));
		return "failure";
	}
	
	public String updateDonation(){
		if(dm.updateDonation(this)){
			setAddrInfo();
			SessionBean sb = Util.getBean("sessionBean", SessionBean.class);
			sb.updateDonationList();
			addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully updated donation", null));
			return "success";
		}
		addMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to update donation", null));
		return "failure";
	}
	
	public void setAddrInfo(){
		Address a = dm.getAddressForId(getAddressid());
		addrInfo = a.toString();
	}
	
	public Integer getStatusid() {			return statusid;}
	public Integer getUserid() {			return userid;}
	public String getRefMethod() {			return refMethod;}
	public String getSize() {				return size;}
	public String getComments() {			return comments;}
	public Date getPickupDate() {			return pickupDate;}
	public String getAddrId() {				return addrId;}
	public String getDonId() {				return donId;}
	public String getAddrInfo() {			return addrInfo;}
	public Integer getAddressid() {
		if((addressid == null) && (addrId != null)){
			addressid = Integer.parseInt(addrId);
		}
		return addressid;
	}
	public Integer getDonationid() {
		if((donationid == null) && (donId != null)){
			donationid = Integer.parseInt(donId);
		}
		return donationid;
	}
	
	public Address getAddress() {
		if(address == null){
			address = dm.getAddressForId(addressid);
		}
		return address;
	}
	
	public void setStatusid(Integer statusid) {		this.statusid = statusid;}
	public void setUserid(Integer userid) {			this.userid = userid;}
	public void setAddressid(Integer addressid) {	this.addressid = addressid;}
	public void setRefMethod(String refMethod) {	this.refMethod = refMethod;}
	public void setSize(String size) {				this.size = size;}
	public void setComments(String comments) {		this.comments = comments;}
	public void setPickupDate(Date pickupDate) {	this.pickupDate = pickupDate;}
	public void setAddress(Address address) {		this.address = address;}
	public void setAddrId(String addrId) {			this.addrId = addrId;}
	public void setDonId(String donId) {			this.donId = donId;}
	public void setDonationid(Integer donationid) {	this.donationid = donationid;}
}