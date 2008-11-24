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

<Query id="sql_user_UserManager_create">
<sql>
		INSERT INTO TBL_USER (  
		   NAME,  
           EXPIRING,  
		   PASSWORD
		  ) 
	    VALUES( ?,?,? ) ; 
</sql>
</Query>

<Query id="sql_user_UserManager_addRolToUser">
<sql>
 			INSERT INTO TBL_ROL_USER (  
			   ID_USER,  
			   ID_ROL  
			  )  
			VALUES( ?,? ) 
</sql>
</Query>

<Query id="sql_user_UserManager_retrieveByName">
<sql>
 		 SELECT  
		 	 ID_USER as id,  
			 EXPIRING as expiring,  
		 	 NAME as name,  
		 	 PASSWORD as password  
		  FROM TBL_USER  
		  WHERE NAME = ?  
</sql>
</Query>

<Query id="sql_user_UserManager_deleteById">
<sql>
	    DELETE FROM TBL_USER  WHERE ID_USER = ? 
</sql>
</Query>

<Query id="sql_user_UserManager_deleteRol2UserByIdUser">
<sql>
	    DELETE FROM TBL_ROL_USER  WHERE ID_USER = ? 
</sql>
</Query>

</DataSql>
