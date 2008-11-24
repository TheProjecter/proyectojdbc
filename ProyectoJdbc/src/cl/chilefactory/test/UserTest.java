package cl.chilefactory.test;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

import cl.chilefactory.manager.user.RolManager;
import cl.chilefactory.manager.user.UserManager;
import cl.chilefactory.model.Rol;
import cl.chilefactory.model.User;

public class UserTest {

	static Logger logger = Logger.getLogger( UserTest.class.toString() );
	
	/**
	 * @param args
	 */
	public static void main(String[] args){
		
		//createRoles(); 
		//retrieveAllRol();
		
		//createUser();			
	    
	    //deleteAllUser();
	    
		//retrieveAllUser(); // sin roles
		
		
		//retrieveFullUser();
		
	}

	/**
	 * Obtiene usuario y sus roles
	 */
	public static void retrieveFullUser(){
		User u = new User();
		u.setName("akira");
		UserManager userMgr = new UserManager();
		User ret = userMgr.retrieveFullByName(u);
		if( ret!=null ){
			logger.debug( "name="+ret.getName() );
			if( ret.getRoles()!=null ){
				logger.debug( "Nº Roles="+ret.getRoles().size()  );
				for( Iterator it=ret.getRoles().iterator(); it.hasNext(); ){
					Object obj = it.next();
					Rol rol = (Rol)obj;
					logger.debug( rol.getName() );
				}
			}
		}
	}
	
	/**
	 * Crea 2 roles : admin y user
	 */
	public static void createRoles(){
		RolManager rolMgr = new RolManager();
		
		Rol rolAdmin = new Rol();
		rolAdmin.setDescription("administrator...");
		rolAdmin.setName("ADMIN");
		rolMgr.create(rolAdmin);
		
		logger.debug( rolAdmin.getId() );
		
		Rol rolUser = new Rol();
		rolUser.setDescription("user...");
		rolUser.setName("USER");
		rolMgr.create(rolUser);
		
		logger.debug( rolUser.getId() );
	}
	
	/**
	 * Crea usuario con 2 roles
	 */
	public static void createUser(){
		
		User user = new User();
		user.setExpiring( new Timestamp(System.currentTimeMillis()) );
		user.setName("akira");
		user.setPassword("123");
		
		RolManager rolMgr = new RolManager(); // 
		Set roles = new HashSet(rolMgr.retrieveAll());
		user.setRoles( roles );
		
		UserManager userMgr = new UserManager();
		userMgr.create(user);

	}
	
	/**
	 * Obtiene todos los roles
	 */
	public static void retrieveAllRol(){
		RolManager rolMgr = new RolManager();
        for( Iterator it=rolMgr.retrieveAll().iterator(); it.hasNext(); ){
        	Rol rolTmp = (Rol)it.next();
        	logger.debug( "ID:"+rolTmp.getId() + ", Name:"+rolTmp.getName() +" , Descriptiom:"+
        			rolTmp.getDescription() );
        }
	}
	
	/**
	 * Obtiene todo los usuario pero sin sus roles cargados
	 */
	public static void retrieveAllUser(){
		UserManager userMgr = new UserManager();
        for( Iterator it=userMgr.retrieveAll().iterator(); it.hasNext(); ){
        	User userTmp = (User)it.next();
        	logger.debug( "ID:"+userTmp.getId() + ", Name:"+userTmp.getName() +" , Expiration:"+
        				 userTmp.getExpiring()+" , Roles: NO CARGADOS");
        }
	}
	
	/**
	 * Elimina todos los usuarios
	 */
	public static void deleteAllUser(){
		UserManager userMgr = new UserManager();
        for( Iterator it=userMgr.retrieveAll().iterator(); it.hasNext(); ){
        	User userTmp = (User)it.next();
        	userMgr.deleteById(userTmp);
        }
	}
	
}
