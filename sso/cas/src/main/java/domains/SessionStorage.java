package domains;

import java.sql.Timestamp;


public class SessionStorage {
	private Long id;
	private String CAS_TGC;
	private String sessionId;
	private String localService;
	private Timestamp created=new Timestamp(System.currentTimeMillis());
	public Timestamp getCreated() {
		return created;
	}
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getLocalService() {
		return localService;
	}
	public void setLocalService(String localService) {
		this.localService = localService;
	}
	public String getTGC() {
		return CAS_TGC;
	}
	public void setTGC(String CAS_TGC) {
		this.CAS_TGC = CAS_TGC;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((localService == null) ? 0 : localService.hashCode());
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
		SessionStorage other = (SessionStorage) obj;
		if (localService == null) {
			if (other.localService != null)
				return false;
		} else if (!localService.equals(other.localService))
			return false;
		if (CAS_TGC == null) {
			if (other.CAS_TGC != null)
				return false;
		} else if (!CAS_TGC.equals(other.CAS_TGC))
			return false;
		return true;
	}
	
}
