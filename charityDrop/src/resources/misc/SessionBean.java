package resources.misc;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import resources.eao.AccountManager;
import resources.entities.Address;
import resources.entities.Donation;
import resources.entities.User;

@ManagedBean(name="sessionBean")
@SessionScoped()
public class SessionBean {
	private boolean isLoggedIn = false;
	private String userEmail = "guest";
	private User user;
	private List<SelectItem> addresses;
	private List<SelectItem> donations;
	private Integer donationId;
	private String prevPage;
	
	@EJB AccountManager am;
	
	@PersistenceContext()
	EntityManager em;
	
	public SessionBean(){
		addresses = new ArrayList<>();
		donations = new ArrayList<>();
	}
	
	public String goHome(){
		if(isLoggedIn){
			return "userPanel";
		}
		return "index";
	}
	
	public void updateDonationList(){
		donations = new ArrayList<>();
		List<Donation> dons = am.getDonations(userEmail);
		for(Donation d : dons){
			addDonation(d);
		}
	}
	
	public boolean getIsLoggedIn(){			return isLoggedIn;}
	public String getUserEmail() {			return userEmail;}
	public User getUser() {					return user;}
	public Integer getDonationId() {		return donationId;}
	public List<SelectItem> getAddresses() {return addresses;}
	public List<SelectItem> getDonations() {return donations;}
	public String getPrevPage() {			return prevPage;}
	
	public void setUser(User u) {			this.user = u;}
	public void setDonationId(Integer dId) {this.donationId = dId;}
	public void setPrevPage(String pPage) {	this.prevPage = pPage;}
	public void fromDonate(){				prevPage = "_d";}
	public void fromEdit(){					prevPage = "_e";}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
		isLoggedIn = true;
		
		User u = em.find(User.class, userEmail);
		user = u;
		addAddress(user.getAddress());
	}
	
	public void addAddress(Address a){
		addresses.add(new SelectItem(a.getAddressid(), a.toString()));
	}
	public void addDonation(Donation d){
		donations.add(new SelectItem(d.getDonationid(), "[ID " + d.getDonationid() + "] " + d.getPickupDate()));
	}
	
	public void reset(){
		isLoggedIn = false;
		userEmail = "guest";
		user = null;
		addresses = new ArrayList<>();
		donations = new ArrayList<>();
		donationId = null;
		prevPage = "";
	}
}