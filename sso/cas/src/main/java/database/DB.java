package database;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import domains.Mapping;
import domains.ServiceTicket;
import domains.SessionStorage;
import domains.TGT;
import domains.User;

public class DB {
	private static Set<ServiceTicket> serviceTickets = new HashSet<>();
	private static Set<User> users = new HashSet<>();
	private static Set<SessionStorage> sessionStorages = new HashSet<>();
	private static Set<Mapping> mappings = new HashSet<>();
	private static Set<TGT> tgts = new HashSet<>();
	static {
		User u1=addUser("01", "0");
		User u2=addUser("02", "0");
		addMapping(1L,u1,"app1u1","localhost","/app1");
		addMapping(2L,u1,"app2u1","localhost","/app2");
		addMapping(1L,u2,"app1u2","localhost","/app1");
		addMapping(2L,u2,"app2u2","localhost","/app2");

	}

	public static User addUser(String id, String pwd) {
		User u = new User();
		u.setId(id);
		u.setPwd(pwd);
		users.add(u);
		return u;
	}
	//getTGCbyst,getTGTbyst


	
	public static User getUserByTGC(String TGC) {
		for (ServiceTicket s : serviceTickets) {
			if (s.getTGC().equals(TGC)) {
				return s.getUser();
			}
		}
		return null;
	}

	public static User findUser(String id, String pwd) {
		for (User u : users) {
			if (u.getId().equals(id) && u.getPwd().equals(pwd)) {
				return u;
			}
		}
		return null;
	}

	public static void addServiceTicket(String CAS_TGC,User user ) {
		ServiceTicket serviceTicket = new ServiceTicket();
		serviceTicket.setTGC(CAS_TGC);
		serviceTicket.setUser(user);
		serviceTickets.add(serviceTicket);
	}
	//tgc瀵瑰簲涓績浼氳瘽Id,sessionid涓篴pp浼氳瘽id
	public static void addSessionStorage(String LOCAL_SERVICE, String CAS_TGC, String sessionId) {
		SessionStorage sessionStorage = new SessionStorage();
		sessionStorage.setLocalService(LOCAL_SERVICE);
		sessionStorage.setTGC(CAS_TGC);
		sessionStorage.setSessionId(sessionId);
		sessionStorages.add(sessionStorage);

	}

	public static void addMapping(Long id,User casUser,String localUser, String host,String app)
	{
		Mapping m=new Mapping();
		m.setId(id);
		m.setCasUser(casUser);
		m.setLocalUser(localUser);
		m.setHost(host);
		m.setApp(app);
		mappings.add(m);
	}

	public static Mapping findMappingByHostAndAppAndCasUser(String host, String app, User user) {
		for (Mapping m : mappings) {
			if (m.getHost().equals(host) && m.getApp().equals(app) && m.getCasUser().equals(user)) {
				return m;
			}
		}
		return null;
	}

	public static List<SessionStorage> findSessionStorage(String CAS_TGC) {
		List<SessionStorage> list = new ArrayList<>();
		System.out.println(list!=null);
		for (SessionStorage s : sessionStorages) {
			System.out.println(s.getTGC());
			if (s.getTGC().equals(CAS_TGC)) {
				list.add(s);
			}
		}
		return list;
	}

	public static void deleteSessionStorage(String CAS_TGC) {
		
		sessionStorages.removeAll(findSessionStorage(CAS_TGC));

	}

	public static List<ServiceTicket> findServiceTicket(String CAS_TGC) {
		List<ServiceTicket> list = new ArrayList<>();
		for (ServiceTicket s : serviceTickets) {
			if (s.getTGC().equals(CAS_TGC)) {
				list.add(s);
			}
		}
		return list;
	}

	public static void deleteServiceTicket(String CAS_TGC) {
		serviceTickets.removeAll(findServiceTicket(CAS_TGC));
	}


	public static void addTGT(User user,String CAS_ST,String CAS_TGC) {
		// TODO Auto-generated method stub
		TGT tgt=new TGT();
		tgt.setST(CAS_ST);
		tgt.setUser(user);
		tgt.setTGC(CAS_TGC);
		tgts.add(tgt);
	}
	
	public static TGT getTGTByST(String ST) {
		for (TGT tgt : tgts) {
			if (tgt.getST().equals(ST)) {
				return tgt;
			}
		}
		return null;
	}
	public static void deleteTGTBySt(String CAS_ST) {
		// TODO Auto-generated method stub
		List<TGT> list = new ArrayList<>();
		for (TGT tgt : tgts) {
			if (tgt.getST().equals(CAS_ST)) {
				list.add(tgt);
			}
		}
		tgts.removeAll(list);
	}

}

