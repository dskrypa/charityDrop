package resources.entities;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;


/**
 * The persistent class for the address database table.
 * 
 */
@Entity
@NamedQuery(name="Address.findAll", query="SELECT a FROM Address a")
public class Address  
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer addressid;
	private String addrLine1;
	private String addrLine2;
	private String city;
	private String usState;
	private String xStreet;
	private String zip;

	//bi-directional many-to-one association to User
	@OneToMany(mappedBy="address", cascade={CascadeType.ALL})
	private List<User> users;

	public Address() {
		users = new ArrayList<>();
	}

	public String toString(){
		String asString = addrLine1;
		if(addrLine2.length() > 0){
			asString += ", " + addrLine2;
		}
		asString += ", " + city + ", " + usState + ", " + zip;
		return asString;
	}
	
	public Integer getAddressid() {
		return this.addressid;
	}

	public void setAddressid(Integer addressid) {
		this.addressid = addressid;
	}

	public String getAddrLine1() {
		return this.addrLine1;
	}

	public void setAddrLine1(String addrLine1) {
		this.addrLine1 = addrLine1;
	}

	public String getAddrLine2() {
		return this.addrLine2;
	}

	public void setAddrLine2(String addrLine2) {
		this.addrLine2 = addrLine2;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getUsState() {
		return this.usState;
	}

	public void setUsState(String usState) {
		this.usState = usState;
	}

	public String getXStreet() {
		return this.xStreet;
	}

	public void setXStreet(String xStreet) {
		this.xStreet = xStreet;
	}

	public String getZip() {
		return this.zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public List<User> getUsers() {
		return this.users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public User addUser(User user) {
		getUsers().add(user);
		user.setAddress(this);

		return user;
	}

	public User removeUser(User user) {
		getUsers().remove(user);
		user.setAddress(null);

		return user;
	}

}