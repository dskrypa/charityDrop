package resources.eao;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import resources.entities.Address;
import resources.entities.Donation;
import resources.entities.User;
import resources.misc.AccountBean;
import resources.misc.Util;

/**
 * Session Bean implementation class RegistrationEAO
 */
@Stateless(mappedName = "accMgr")
@LocalBean
public class AccountManager {
	@PersistenceContext()
	EntityManager em;
	
    public AccountManager() {}

    public boolean validatePassword(User u, String hashedPw){
    	if(u==null){return false;}
    	//Querying DB for password because Java object is unreliable after PW change
    	try{
			String upw = (String) em.createNativeQuery("SELECT password FROM user WHERE email='" + u.getEmail() + "';").getSingleResult();
			if(upw != null){
				return upw.equals(hashedPw);
			}
    	} catch (Exception e){
			e.printStackTrace();
		}
		return false;
    }
    
    public boolean validateLogin(AccountBean aBean){
		User u = em.find(User.class, aBean.getEmail());
		return validatePassword(u, Util.hash(aBean.getPassword()));
	}
    
    @SuppressWarnings("unchecked")
	public List<Donation> getDonations(String userEmail){
    	try{
			Query q = em.createQuery("SELECT d FROM Donation d WHERE d.useremail LIKE :uemail").setParameter("uemail", userEmail);
			return q.getResultList();
    	} catch (Exception e){
			e.printStackTrace();
		}
    	return null;
	}
    
    public Address getAddressById(Integer id){
    	Address a = em.find(Address.class, id);
    	return a;
    }
    
    public boolean deleteUser(User u){
    	try{
    		em.detach(u);
    		em.createNativeQuery("DELETE FROM user WHERE email='" + u.getEmail() + "';").executeUpdate();
    		return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return false;
    }
    
    public boolean userExists(AccountBean rBean){
    	User u = em.find(User.class, rBean.getEmail());
    	return u!=null;
    }
    
    public boolean updatePassword(User u, String hashedPw){
    	try{
    		em.createNativeQuery("UPDATE user SET password='" + hashedPw + "' WHERE email='" + u.getEmail() + "';").executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
    }
    
    public Address persistAddress(AccountBean rBean){
		try {
			Address a = new Address();
			a.setAddrLine1(rBean.getAddrLine1());
			a.setAddrLine2(rBean.getAddrLine2());
			a.setCity(rBean.getCity());
			a.setUsState(rBean.getUsState());
			a.setZip(rBean.getZip());
			a.setXStreet(rBean.getxStreet());
			
			em.persist(a);	//returns void
			return a;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
    }
    
	public boolean persistUser(AccountBean rBean) {
		try {
			Address a = new Address();
			a.setAddrLine1(rBean.getAddrLine1());
			a.setAddrLine2(rBean.getAddrLine2());
			a.setCity(rBean.getCity());
			a.setUsState(rBean.getUsState());
			a.setZip(rBean.getZip());
			a.setXStreet(rBean.getxStreet());
			
			User u = new User();
			u.setEmail(rBean.getEmail());
			u.setPassword(Util.hash(rBean.getPassword()));
			u.setFirstname(rBean.getFirstname());
			u.setLastname(rBean.getLastname());
			u.setPhone(rBean.getPhone());
			u.setContactMethod(rBean.getContactMethod());
			u.setAddress(a);
			
			em.persist(u);	//returns void
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}