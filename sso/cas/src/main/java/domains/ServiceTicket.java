package domains;

import java.sql.Timestamp;

public class ServiceTicket {
	private String CAS_TGC;
	private User user;
	private Timestamp created=new Timestamp(System.currentTimeMillis());
	public String getTGC() {
		return CAS_TGC;
	}
	public void setTGC(String CAS_TGC) {
		this.CAS_TGC = CAS_TGC;
	}

	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Timestamp getCreated() {
		return created;
	}
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((CAS_TGC == null) ? 0 : CAS_TGC.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ServiceTicket other = (ServiceTicket) obj;
		if (CAS_TGC== null) {
			if (other.CAS_TGC != null)
				return false;
		} else if (!CAS_TGC.equals(other.CAS_TGC))
			return false;
		return true;
	}
	
}

