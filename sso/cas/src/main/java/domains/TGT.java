package domains;

public class TGT {
	User user;
	String CAS_ST;
	String CAS_TGC;
	public void setTGC(String CAS_TGC) {
		this.CAS_TGC=CAS_TGC;
	}
	public String getTGC() {
		return CAS_TGC;
	}
	public void setUser(User user) {
		this.user=user;
	}
	public void setST(String  CAS_ST) {
		this. CAS_ST= CAS_ST;
	}
	public User getUser() {
		return user;
	}

	public String getST() {
		return CAS_ST;
	}
}