![http://proyectojdbc.googlecode.com/svn/trunk/images/logo_opensource.jpg](http://proyectojdbc.googlecode.com/svn/trunk/images/logo_opensource.jpg)
![http://proyectojdbc.googlecode.com/svn/trunk/images/gnu-gpl.png](http://proyectojdbc.googlecode.com/svn/trunk/images/gnu-gpl.png)
![http://proyectojdbc.googlecode.com/svn/trunk/images/logo_mysql_sun.gif](http://proyectojdbc.googlecode.com/svn/trunk/images/logo_mysql_sun.gif)
![http://proyectojdbc.googlecode.com/svn/trunk/images/logo_postgresql.jpg](http://proyectojdbc.googlecode.com/svn/trunk/images/logo_postgresql.jpg)

## Proyecto Simple JDBC Layer ##

source code :
http://code.google.com/p/proyectojdbc/source/browse/#svn/trunk/ProyectoJdbc

Para no usar framework's complejos ... sobre todo cuando nuestros proyectos
son pequeños implementamos una simple capa jdbc .

Finalidad programar de una forma sencilla el acceso a los datos sin complicarnos la vida.

Cualquier duda o comentario serán bien recibidos. yerko.bravo@gmail.com

Sample code:

` UserManager.java:  `

...

```
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
```
...


` user.sql: `

```
 
<DataSql>

<Query id="sql_user_UserManager_retrieveAll">
<sql>
		 SELECT 	
		   ID_USER AS id,  
		   EXPIRING AS expiring,  
		   NAME AS name, 
		   PASSWORD AS password  
		 FROM TBL_USER
</sql>
</Query>
 
```

` User.java: `

```
public class User {

	private Long id;
	private String name;
	private String password;
	private Timestamp expiring;
	private Set roles = new HashSet();

        // sets y gets

}
```