package cl.chilefactory.core.util;

public class StringUtil {

	/**
	 * Cambia primera letra del string de entrada a mayúscula
	 * @param str
	 * @return
	 */
	public static String setFirstLetterUp( String str ){
		String firstLetterUp = String.valueOf( str.charAt(0) ).toUpperCase();
		return firstLetterUp + str.substring(1,str.length());
	}
	
	/**
	 * Remueve primer elemento de parametro "str" separados por "token"
	 * @param str  : String con elementos separados por token
	 * @param token  : token separador	
	 * @return
	 */
	public static String removeFirstToken( String str, String token ){
		return str.substring( str.indexOf( token )+1, str.length() );
	}
	
	/**
	 * Obtiene primer elemento de parametro "str" separados por "token"
	 * @param str  : String con elementos separados por token
	 * @param token  : token separador	
	 * @return
	 */
	public static String getFirstToken( String str, String token ){
		return str.substring(0,str.indexOf( token ));
	}
	
}
