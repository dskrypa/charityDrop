package resources.eao;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import resources.entities.Address;
import resources.entities.Donation;
import resources.entities.User;
import resources.misc.DonationBean;
import resources.misc.SessionBean;
import resources.misc.Util;


@Stateless(mappedName = "donationMgr")
@LocalBean
public class DonationManager {
	@PersistenceContext()
	EntityManager em;
	public DonationManager(){}
	
	public Address getAddressForId(Integer id){
		return em.find(Address.class, id);
	}
	
	public Donation getDonationForId(Integer id){
		return em.find(Donation.class, id);
	}
	
	public Integer getDonationId(Donation d){
		Integer dId = d.getDonationid();
		if(dId == null){
			try{
				String qs = "SELECT donationid FROM donation WHERE "
					+ "pickupDate='" + d.getPickupDate() + "'"
					+ " AND addressid='" + d.getAddressid() + "'"
					+ " AND refMethod='" + d.getRefMethod() + "'"
					+ " AND size='" + d.getSize() + "'"
					+ " AND comments='" + d.getComments() + "'";
				dId = (Integer) em.createNativeQuery(qs).getSingleResult();
				d.setDonationid(dId);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return dId;
	}
	
	public boolean deleteDonataion(Donation d){
		try{
			em.detach(d);
    		em.createNativeQuery("DELETE FROM donation WHERE donationid='" + d.getDonationid() + "';").executeUpdate();
		} catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean updateDonation(DonationBean dBean){
		try{
			SessionBean sb = Util.getBean("sessionBean", SessionBean.class); 
			Donation d = em.find(Donation.class, sb.getDonationId());
			d.setAddressid(dBean.getAddress().getAddressid());
			
			java.sql.Date sdate = new java.sql.Date(dBean.getPickupDate().getTime());
			d.setPickupDate(sdate);
			
			d.setRefMethod(dBean.getRefMethod());
			d.setSize(dBean.getSize());
			d.setComments(dBean.getComments());
			
			em.persist(d);	//returns void
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean persistDonation(DonationBean dBean){
		try{
			Donation d = new Donation();
			d.setAddressid(dBean.getAddress().getAddressid());
			
			SessionBean sb = Util.getBean("sessionBean", SessionBean.class); 
			if(sb.getIsLoggedIn()){
				User u = sb.getUser();
				if(u!=null){
					d.setUseremail(u.getEmail());
				}
			}
			
			java.sql.Date sdate = new java.sql.Date(dBean.getPickupDate().getTime());
			d.setPickupDate(sdate);
			d.setRefMethod(dBean.getRefMethod());
			d.setSize(dBean.getSize());
			d.setComments(dBean.getComments());
			
			em.persist(d);	//returns void
			sb.setDonationId(getDonationId(d));
			dBean.setDonationid(getDonationId(d));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
