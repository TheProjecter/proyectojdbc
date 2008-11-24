package cl.chilefactory.core.util;

import java.lang.reflect.Method;
import org.apache.log4j.Logger;

public class ReflectionUtil {

	private final static Logger log = 
		Logger.getLogger(ReflectionUtil.class.toString());
	
	/**
	 * Crea nueva instancia de una clase
	 * @param className nombre de la clase
	 * @return new instance
	 */
	public static Object getNewInstance( String className ){
		try {
			Class clazz = Class.forName(className);
			return clazz.newInstance();
		} catch (ClassNotFoundException e) {
			log.error( "*** Error Clase \"" +className+"\" no existe");
		}
		  catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * Get value of Object's attribute
	 * @param attr
	 * @param javaBean
	 * @return
	 */
	public static ReflectionObject getValue( String attr, Object javaBean  ) {
		String nameMetod = "get"+StringUtil.setFirstLetterUp(attr);
		try {
			 Method method = javaBean.getClass().getMethod(nameMetod, new Class[] {} );
			 Object ret = method.invoke(javaBean, null );
			 ReflectionObject obj = new ReflectionObject();
			 obj.setType( method.getReturnType() );
			 obj.setValue( ret );
			 return obj;
		}
		catch(NoSuchMethodException e){
			log.error( "*** Not found public method: "+e.getMessage() +" in Object="+javaBean.getClass() );
		}
		catch(Exception e){
			log.error( "*** Error get  \""+attr+"\" of "+javaBean);
			log.error( "*** javaBean maybe not initialized");
		}
		return null; 
	}
	
	/**
	 * Set value in attribute of Object
	 * @param obj
	 * @param attr
	 * @param javaBean
	 * @return
	 */
	public static boolean setValue( ReflectionObject obj ,  String attr, Object javaBean ) {
		String met = "set"+StringUtil.setFirstLetterUp(attr);
		try {		 
			Method method = javaBean.getClass().getMethod(
					met, 
					obj.getArrayType() );
			 method.invoke( javaBean, obj.getArrayValue() );
			 return true;
		}
		catch(NoSuchMethodException e){
			log.error( "*** Not found public method: "+e.getMessage() +" in Object="+javaBean.getClass() );
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	

}
