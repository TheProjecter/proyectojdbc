package cl.chilefactory.manager.user;

import java.util.List;

import org.apache.log4j.Logger;

import cl.chilefactory.core.dao.jdbc.JdbcManager;
import cl.chilefactory.model.Rol;


public class RolManager extends JdbcManager {

	private final static Logger log = Logger.getLogger( RolManager.class.toString() );
	
	/**
	 * Obtiene todos los Roles.
	 * @return List : roles encontrados
	 */
	public List retrieveAll(){
		String SQL = (String) querys.get( "sql_user_RolManager_retrieveAll" );
		try {
			return queryList(SQL, null, Rol.class);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}
	
	/**
	 * Obtiene todos los perfiles de un usuario
	 * @param idUser : id del usuario
	 * @return 
	 */
	public List retrieveRolesByIdUser( Long idUser ){
		String SQL = (String) querys.get( "sql_user_RolManager_retrieveRolesByIdUser" );
		Object params[] = { idUser };
		try {
			return queryList(SQL, params, Rol.class);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}
	
	/**
	 * Crea un nuevo rol
	 * @param rol  : rol a crear
	 * @return newId : id creado;
	 */
	public Long create(Rol rol) {
        String SQL = (String) querys.get( "sql_user_RolManager_create" );
		Long newId = null;
		try {
			Object params[] = { rol.getName(), rol.getDescription() };
			newId  = execute( SQL, params );
		}catch(Exception e){
			printExceptions(this,e);
		}
		return newId;
	}

	
}
