<DataSql>

<Query id="sql_user_RolManager_retrieveAll">
<sql>
	   SELECT  
			 id_rol as id,  
			 name as name,  
			 description as description  
 	   FROM tbl_rol
</sql>
</Query>

<Query id="sql_user_RolManager_create">
<sql>
	 INSERT INTO tbl_rol (  
		   name, 
		   description 
		   )  
	 VALUES( ?,? ) ;  
	 SELECT currval('tbl_rol_id_rol_seq');
</sql>
</Query>


<Query id="sql_user_RolManager_retrieveRolesByIdUser">
<sql>
    SELECT  
		   R.id_rol as id,  
		   R.name as name,  
		   R.description as description  
		   FROM tbl_rol R, tbl_rol_user RU
	WHERE R.id_rol = RU.id_rol AND RU.id_user = ?
</sql>
</Query>

</DataSql>
