<DataSql>

<Query id="sql_user_UserManager_retrieveAll">
<sql>
		 SELECT 	
		    id_user  AS id,  
		    expiring  AS expiring,  
		    name  AS name, 
		    password  AS password  
		 FROM  tbl_user
</sql>
</Query>

<Query id="sql_user_UserManager_create">
<sql>
		INSERT INTO  tbl_user (  
		    name,  
            expiring,  
		    password
		  ) 
	    VALUES( ?,?,? ) ; 
		SELECT currval('tbl_user_id_user_seq');
</sql>
</Query>

<Query id="sql_user_UserManager_addRolToUser">
<sql>
 			INSERT INTO tbl_rol_user (  
			   id_user,  
			   id_rol  
			  )  
			VALUES( ?,? ) 
</sql>
</Query>

<Query id="sql_user_UserManager_retrieveByName">
<sql>
 		 SELECT  
		 	  id_user  as id,  
			  expiring  as expiring,  
		 	  name  as name,  
		 	  password  as password  
		  FROM  tbl_user   
		  WHERE name = ?  
</sql>
</Query>

<Query id="sql_user_UserManager_deleteById">
<sql>
	    DELETE FROM  tbl_user   WHERE  id_user  = ? 
</sql>
</Query>

<Query id="sql_user_UserManager_deleteRol2UserByIdUser">
<sql>
	    DELETE FROM  tbl_rol_user   WHERE  id_user  = ? 
</sql>
</Query>

</DataSql>
