package resources.entities;

import javax.persistence.*;


/**
 * The persistent class for the donationstatus database table.
 * 
 */
@Entity
@NamedQuery(name="Donationstatus.findAll", query="SELECT d FROM Donationstatus d")
public class Donationstatus  {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer statusid;
	private String statusDescription;

	public Donationstatus() {
	}

	public Integer getStatusid() {
		return this.statusid;
	}

	public void setStatusid(Integer statusid) {
		this.statusid = statusid;
	}

	public String getStatusDescription() {
		return this.statusDescription;
	}

	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}
}