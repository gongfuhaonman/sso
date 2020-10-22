package database;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import domains.Mapping;

import domains.SessionStorage;

import domains.User;

public class DB {
	private static Set<User> users = new HashSet<>();
	private static Set<SessionStorage> sessionStorages = new HashSet<>();
	private static Set<Mapping> mappings = new HashSet<>();
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


	

	//鍐呴儴浣跨敤
	public static User getUser(String id) {
		for (User u : users) {
			if (u.getId().equals(id) ) {
				return u;
			}
		}
		return null;
	}
	//楠岃瘉浣跨敤
	public static User findUser(String id, String pwd) {
		for (User u : users) {
			if (u.getId().equals(id) && u.getPwd().equals(pwd)) {
				return u;
			}
		}
		return null;
	}


	//tgc鐎电懓绨叉稉顓炵妇娴兼俺鐦絀d,sessionid娑撶pp娴兼俺鐦絠d
	public static void addSessionStorage(String LOCAL_SERVICE, String user_id, String sessionId) {
		SessionStorage sessionStorage = new SessionStorage();
		sessionStorage.setLocalService(LOCAL_SERVICE);
		sessionStorage.setUserID(user_id);
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

	public static List<SessionStorage> findSessionStorage(String user_id) {
		List<SessionStorage> list = new ArrayList<>();
		System.out.println(list!=null);
		for (SessionStorage s : sessionStorages) {
			if (s.getUserID().equals(user_id)) {
				list.add(s);
			}
		}
		return list;
	}

	public static void deleteSessionStorage(String user_id) {
		
		sessionStorages.removeAll(findSessionStorage(user_id));

	}

}