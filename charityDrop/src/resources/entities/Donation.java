package resources.entities;

import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the donation database table.
 * 
 */
@Entity
@NamedQuery(name="Donation.findAll", query="SELECT d FROM Donation d")
public class Donation  {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer donationid;
	private Integer addressid;
	private String comments;
	@Temporal(TemporalType.DATE)
	private Date pickupDate;
	private String refMethod;
	private String size;
	private Integer statusid;
	private String useremail;

	public Donation() {}

	public Integer getDonationid() {
		return this.donationid;
	}
	public void setDonationid(Integer donationid) {
		this.donationid = donationid;
	}

	public Integer getAddressid() {
		return this.addressid;
	}
	public void setAddressid(Integer addressid) {
		this.addressid = addressid;
	}

	public String getComments() {
		return this.comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}

	public Date getPickupDate() {
		return this.pickupDate;
	}
	public void setPickupDate(Date pickupDate) {
		this.pickupDate = pickupDate;
	}

	public String getRefMethod() {
		return this.refMethod;
	}
	public void setRefMethod(String refMethod) {
		this.refMethod = refMethod;
	}

	public String getSize() {
		return this.size;
	}
	public void setSize(String size) {
		this.size = size;
	}

	public Integer getStatusid() {
		return this.statusid;
	}
	public void setStatusid(Integer statusid) {
		this.statusid = statusid;
	}

	public String getUseremail() {
		return useremail;
	}
	public void setUseremail(String useremail) {
		this.useremail = useremail;
	}
}