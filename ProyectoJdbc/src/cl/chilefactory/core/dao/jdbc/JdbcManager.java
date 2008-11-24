package cl.chilefactory.core.dao.jdbc;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import cl.chilefactory.core.util.ReflectionObject;
import cl.chilefactory.core.util.ReflectionUtil;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 *  Nomenclatura SQL KEY para implementaciones de manager's : sql_<MODULO>_<CLASS>_<METHOD>
 *  
 * @author Yerko
 *
 */
public abstract class JdbcManager {
	
	private static final Logger log = Logger.getLogger(JdbcManager.class);

	public static DataSource dataSource = null;
	
	public static boolean INIT = true;
	
	public static Map querys;

	
	static {
		init();
	}
	
	/**
	 * Load XML con querys
	 *
	 */
	public static void init(){
		if( INIT ){
			
			log.debug("--- Load QUERYS ---");
			INIT = false;
			try {
				XStream xs = new XStream(new DomDriver());
				xs.alias("DataSql", DataSql.class);
				xs.alias("Query", Query.class);
				xs.useAttributeFor(String.class);
				xs.addImplicitCollection(DataSql.class, "querys");
				
				File file = new File("sql");
				String files[] = file.list();
				String path = file.getAbsolutePath();
				
				querys = new HashMap();
				for (int i = 0; i < files.length; i++) {
				  if( files[i].endsWith(".sql") ){
					DataSql dataSql = new DataSql();
					FileInputStream fis = new FileInputStream(path+"/"+files[i]);
					xs.fromXML(fis, dataSql);
					for (int j = 0; j < dataSql.getQuerys().size(); j++) {
						Query q = (Query) dataSql.getQuerys().get(j);
						//log.debug( q.getId() + " "+q.getSql());
						if( querys.containsKey(q.getId()) ){
							log.error(" ***** SQL KEY REPETIDO : "+q.getId() );
						}
						querys.put(q.getId(), q.getSql() );
					}
				  }
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Para test
	 * @param args
	 */
	public static void main ( String args[] ){
		System.out.println("test");
	}
	
	protected boolean closeResultSet( ResultSet res ){
	    try {
	    	if( res!=null )
	    		res.close();
	    	return true;
	    }
	    catch (Exception e) {
	    	return false;
	    }
	}
	
	protected boolean closeConnection( Connection conn ){
	    try {
	    	if( conn!=null )
	    		conn.close();
	    	return true;
	    }
	    catch (Exception e) {
	    	return false;
	    }
	}
	
	protected boolean closePreparedStatement( PreparedStatement pstmt ){
	    try {
	    	if( pstmt!=null )
	    		pstmt.close();
	    	return true;
	    }
	    catch (Exception e) {
	    	return false;
	    }
	}

	/**
	 * standalone
	 * @return
	 */
    private Connection getJdbcConnection(){ 
    	// TODO: revisar multiples conexiones.
    	try {
    		ResourceBundle res =  ResourceBundle.getBundle("db");
    		String driver = res.getString("driver");
    		String url = res.getString("url");
    		String user = res.getString("user");
    		String passwd = res.getString("password");
			Class.forName(driver);
			return DriverManager.getConnection(url, user, passwd );
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}
    }
    
    
    public Connection getConnection(){
    	try {
    		if( dataSource==null ){
    			return getJdbcConnection();
    		}
			return  dataSource.getConnection();
		} catch (SQLException e) {
			log.error(e.getMessage());
			return null;
		}
    }
    
    
    /**
     * Set in initial web class ej:  extend ServletContextListener
     * 
     * JdbcManager.init();
     * setDataSource(  servletContextEvent.getServletContext().getInitParameter("dataSourceName")  );
     * 
     * Set web.xml :
     * 
      	<listener>
		<listener-class>package.Startup</listener-class>
		</listener>
	
      	<resource-ref>
			<description>datasource test...</description>
			<res-ref-name>jdbc/mysqlDStest</res-ref-name>
			<res-type>javax.sql.DataSource</res-type>
			<res-auth>Container</res-auth>
		</resource-ref>

		<context-param>
			<param-name>dataSourceName</param-name>
			<param-value>jdbc/mysqlDStest</param-value>
		</context-param>
	 *	
     */
	private static void setDataSource( String dsName ){
		try {
			Context init = new InitialContext();
			Context ctx = (Context) init.lookup("java:comp/env");
			DataSource ds = (DataSource) ctx.lookup( dsName );
			ds.getConnection(); // initial test
			dataSource = ds;
		}catch( Exception e ){
			log.error( e.getMessage() );
			e.printStackTrace();
		}
	}

	protected Long getLastId( PreparedStatement pstmt ) throws SQLException {
		Long idInserted = new Long(-1);
		ResultSet resultSet = null;
		try {
			resultSet = pstmt.getGeneratedKeys(); // MYSQL
			idInserted = getIdInserted(resultSet);
		}catch( Exception e){
		}
		//ELSE
		if (idInserted.longValue() == -1) {
			if (pstmt.getUpdateCount() == 1 && pstmt.getMoreResults()) { // POSTGRESQL ...
				resultSet = pstmt.getResultSet();
				idInserted = getIdInserted(resultSet);
			}
		}
		closeResultSet(resultSet);
	    return idInserted;
	}
	
	private Long getIdInserted( ResultSet resultSet ) throws SQLException{
        if( resultSet != null && resultSet.next() ){
	    	Long newId = new Long( resultSet.getLong(1) );
	    	return newId;
        }
        return new Long(-1);
	}
    
	protected void printExceptions(Object obj,Exception e){
		if( e instanceof SQLException ){
			do {
				log.error( obj.getClass()+" : SQL_ERROR_CODE = "+((SQLException)e).getErrorCode());
				log.error( obj.getClass()+" : SQL_ERROR = "+e);
				log.error(obj,e);
				e = ((SQLException)e).getNextException();
			} while (e != null);
		}
		else
			log.error(e.getMessage());
	}
	
	
	private Class getClassType( int type ){
		switch (type) {
			case Types.CHAR:
				return String.class;
			case Types.VARCHAR:
				return String.class;
			case Types.LONGVARCHAR:
				return String.class;
			case Types.NUMERIC:
				return java.math.BigDecimal.class;
			case Types.DECIMAL:
				return java.math.BigDecimal.class;
			case Types.BIT:
				return Boolean.class;
			case Types.TINYINT:
				return Byte.class;
			case Types.SMALLINT:
				return Short.class;
			case Types.INTEGER:
				return Integer.class;
			case Types.BIGINT:
				return Long.class;
			case Types.REAL:
				return Float.class;
			case Types.FLOAT:
				return Double.class;
			case Types.DOUBLE:
				return Double.class;
			case Types.BINARY:
				return Byte[].class;
			case Types.VARBINARY:
				return Byte[].class;
			case Types.LONGVARBINARY:
				return Byte[].class;
			case Types.DATE:
				return java.sql.Date.class;
			case Types.TIME:
				return java.sql.Time.class;
			case Types.TIMESTAMP:
				return java.sql.Timestamp.class;
			
		}
		System.err.println("\nERROR "+this.getClass().toString()+".getClassType type="+type+" no Found.\n");
		return null;
	}
	
	/**
	 * Ejecuta query sin parametros
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	protected Long execute( String sql )throws Exception{
		return _execute(sql,null,null);
	}
	
	/**
	 * Ejecuta query con parametros
	 * @param sql
	 * @param params
	 * @return
	 * @throws Exception
	 */
	protected Long execute( String sql, Object params[] )throws Exception{
		return _execute(sql,params,null);
	}
	
	/**
	 * Ejecuta query sin parametros, no cierra conexion, deja manejar Transaccionalidad.
	 * @param sql
	 * @param conn
	 * @return
	 * @throws Exception
	 */
	protected Long executeTx( String sql, Connection conn )throws Exception{
		return _execute(sql,null,conn);
	}
	
	/**
	 * Ejecuta query con parametros, no cierra conexion, deja manejar Transaccionalidad.
	 * @param sql
	 * @param params
	 * @param conn
	 * @return
	 * @throws Exception
	 */
	protected Long executeTx( String sql, Object params[], Connection conn  )throws Exception{
		return _execute(sql,params,conn);
	}
	
	/**
	 * Ejecuta query con o sin parametros.
	 * Si viene conexion no la cierra para dejar manajer Transaccionalodad.
	 * @param sql
	 * @param params
	 * @param connTx
	 * @return
	 * @throws Exception
	 */	
	private Long _execute( String sql, Object params[], Connection connTx ) throws Exception{
		PreparedStatement pstmt = null;
		Connection conn = null;
		if( connTx==null ){
			conn = getConnection();
			conn.setAutoCommit(true);
		}
		else {
			conn = connTx;
			conn.setAutoCommit(false);//asi puede manejar commit y rollback
		}
		Long newId = new Long(-1);
		try {
			pstmt = conn.prepareStatement(sql);
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					pstmt.setObject(i + 1, params[i]);
				}
			}
			log.debug(pstmt);
			pstmt.execute();
			newId = getLastId(pstmt);
		} catch (Exception e) {
			throw e;
		}
        finally {
        	closePreparedStatement(pstmt);
        	if( connTx==null ){
        		closeConnection(conn);
        	}
        }
        return newId;
	}
	
	/**
	 * Ejecuta query (SELECT) no Transaccional, sin parametros
	 * @param sql
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	protected List queryList( String sql, Class clazz ) throws Exception{
		return _queryList(sql, null, clazz);
	}
	
	/**
	 * Ejecuta query (SELECT) no Transaccional, con parametros
	 * @param sql
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	protected List queryList( String sql, Object params[], Class clazz ) throws Exception{
		return _queryList(sql, params, clazz);
	}
	
	/**
	 * Ejecuta query (SELECT) no Transaccional, con o sin parametros
	 * @param sql
	 * @param params
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	private List _queryList( String sql,  Object params[], Class clazz ) throws Exception{
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		Connection conn = getConnection();
		conn.setAutoCommit(true);//TODO: o false revisar posible rendimiendo
		List list = new ArrayList();
        try {
        	pstmt = conn.prepareStatement(sql);
        	if (params!=null){
	        	for( int i=0;i<params.length; i++){
	        		pstmt.setObject(i+1, params[i]);
	        	}
        	}
        	log.debug( pstmt );
        	pstmt.execute();
        	rst = pstmt.getResultSet();
    		ResultSetMetaData metaData = rst.getMetaData();
        	while( rst.next() ){
        		Object pojo = ReflectionUtil.getNewInstance(clazz.getName());
        		int count = metaData.getColumnCount();
        		for( int i=1; i<=count; i++){
        			//String label = metaData.getColumnLabel(i);
        			String name = metaData.getColumnName(i);
        			String typeName = metaData.getColumnTypeName(i);
        			int type= metaData.getColumnType(i);
        			log.debug( "MetaData: name="+name+ " typeName="+typeName+" type="+type);
        			Object value = rst.getObject(i);
        			ReflectionObject rObj = new ReflectionObject();
        			rObj.setType( getClassType(type) );
        			rObj.setValue(value);
        			ReflectionUtil.setValue(rObj, name, pojo);
        		}
        		list.add( pojo );
        	}
        
        }catch(Exception e){
        	throw e;
        }
        finally {
        	closeResultSet(rst);
        	closePreparedStatement(pstmt);
        	closeConnection(conn);
        }
        return list;
	}
	
	/**
	 * 
	 * @param conn
	 */
	protected void rollback(Connection conn ){
		try {
			conn.rollback();
		} catch (SQLException e) {
			printExceptions(this,e);
		}
	}


}
