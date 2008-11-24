package cl.chilefactory.manager.user;

import java.sql.Connection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import cl.chilefactory.core.dao.jdbc.JdbcManager;
import cl.chilefactory.model.Rol;
import cl.chilefactory.model.User;

/**
 *
 * @author Yerko
 *
 */
public class UserManager  extends JdbcManager {
	
	private final static Logger log = Logger.getLogger( UserManager.class.toString() );
	
	
	
	/**
	 * Obtiene todos los Usuarios (No carga Roles).
	 * @return List : usuarios encontrados.
	 */
	public List retrieveAll(){
		String SQL = (String) querys.get( "sql_user_UserManager_retrieveAll" );
		try {
			return queryList(SQL, User.class);
		} catch (Exception e) {
			printExceptions(this, e);
		}
		return null;
	}
	
	/**
	 * Crea un nuevo Usuario (Inserta Roles si los trae).
	 * @param user  : Usuario creado.
	 * @return newId : Id del Usuario creado, Si id = -1 no se creo Usuario.
	 */
	public Long create(User user) {
		String SQL = (String) querys.get( "sql_user_UserManager_create" );
		Long newId = null;
		Connection conn = null;
		try {
			Object params[] = { user.getName(), user.getExpiring(), user.getPassword() };
			conn = getConnection();
			newId  = executeTx( SQL, params, conn);
			addRolesToUser(newId, user.getRoles(), conn);
			conn.commit();
		} catch (Exception e) {
			printExceptions(this, e);
			rollback(conn);
		}
        finally {
        	closeConnection(conn);
        }
		return newId;
	}
	
	/**
	 * Agrega Roles a un Usuario dado su Id.
	 * @param user : Usuario.
	 * @param roles : Roles agregados.
	 * @throws Exception 
	 */
	 private void addRolesToUser(Long idUser, Set roles, Connection conn) throws Exception {
		if (roles != null) {
		 try {	
			for (Iterator it = roles.iterator(); it.hasNext();) {
				Rol rol = (Rol) it.next();
				String SQL = (String) querys.get( "sql_user_UserManager_addRolToUser" );
				Object params[] = { idUser, rol.getId() };
				executeTx(SQL, params, conn);
			}
		 }catch(Exception e) {
			 printExceptions(this, e);
			 throw new Exception("Error agregando Rol");
		 }
		}
	}

	/**
	 * Obtiene Usuario Sin Roles dado un Name
	 * @param user : Usa Name.
	 * @return Usuario obtenido, Null si no pudo obtenerlo
	 */
	public User retrieveByName( User user){
		String SQL = (String) querys.get( "sql_user_UserManager_retrieveByName" );
		try {
			Object params[] = { user.getName() }; 
			List list = queryList(SQL, params, User.class);
			if( list!=null && list.size()>0 ){
				return (User)list.get(0);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}
	
	/**
	 * Obtiene Usuario con sus Roles dado un Name.
	 * @param user : Usa Name.
	 * @return Usuario obtenido, Null si no pudo obtenerlo
	 */
	public User retrieveFullByName( User user ){
		User retUser = retrieveByName(user);
		if( retUser==null ){
			return null;
		}
		RolManager rolMgr = new RolManager();
		List roles = rolMgr.retrieveRolesByIdUser( retUser.getId() );
		Set rolesSet = new HashSet();
		if( roles!=null ){
			rolesSet.addAll(roles);
			retUser.setRoles(rolesSet);
		}
		return retUser;
	}
	
	public boolean update( User user ){
		//TODO: implementar
		return false;
	}
	
	/**
	 * Elimina un Usuario dado un Id .
	 * @param user 	: Usa id.
	 * @return flag : si fue o no borrado.
	 */
	public boolean deleteById( User user ){
        String SQL = (String) querys.get( "sql_user_UserManager_deleteById" );
        Connection conn = null;
		try {
			Object params[] = { user.getId() };
			conn = getConnection();
			deleteRol2UserById(user, conn);//eliminar 1º la asociacion rol-user
			executeTx( SQL, params , conn );// luego elimina user			
			conn.commit();
			log.debug( "ID DELETE = "+user.getId() );
		}catch(Exception e){
			rollback(conn);
			printExceptions(this,e);
		}
        finally {
        	closeConnection(conn);
        }
		return false;	
	}
	
	/**
	 * Delete asociacion de usuario a roles dado su Id
	 * @param user : usa Id
	 * @param conn : para manejar Transaccionalidad
	 * @throws Exception
	 */
	public void deleteRol2UserById( User user, Connection conn ) throws Exception{
		String SQL = (String) querys.get( "sql_user_UserManager_deleteRol2UserByIdUser" );
		Object params[] = { user.getId() };
		executeTx( SQL, params, conn);
		log.debug( "DELETE ROL_USER BY ID_USER = "+user.getId() );
	}
	
	
	
}
