<DataSql>

<Query id="sql_user_RolManager_retrieveAll">
<sql>
	   SELECT  
			 ID_ROL as id,  
			 NAME as name,  
			 DESCRIPTION as description  
 	   FROM TBL_ROL  
</sql>
</Query>

<Query id="sql_user_RolManager_create">
<sql>
	 INSERT INTO TBL_ROL (  
		   NAME, 
		   DESCRIPTION 
		   )  
	 VALUES( ?,? ) ;  
</sql>
</Query>


<Query id="sql_user_RolManager_retrieveRolesByIdUser">
<sql>
    SELECT  
		   R.ID_ROL as id,  
		   R.NAME as name,  
		   R.DESCRIPTION as description  
		   FROM TBL_ROL R, TBL_ROL_USER RU
	WHERE R.ID_ROL = RU.ID_ROL AND RU.ID_USER = ?
</sql>
</Query>

</DataSql>
