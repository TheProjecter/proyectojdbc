Ejecutar test:
Primero crear roles createRoles(); una sola vez

Usuario-muchos->Roles

Los querys se deben dejar en archivos en la carpeta sql/
para llevar un orden se le pondra el mismo nombre del manager donde se ocupa.

 Con Manager.init() se puede ejecuta la carga de los xml con los querys inicialmente
 sino se hara la primera vez q se ocupe un manager. 
 
 script.sql tiene el script a cargar en la base de datos para las pruebas
 
 La clase Manager tiene un metodo getConnection() donde obtiene la conexion 
 para test se tiene un metodo con los datos de conexion standalone ... en ambientes
 web se usara obtiene la conexion de un Datasource. el cual usa el getConnection() comentado en la misma clase .
 
 Probado con Jdbc3
 
 yerko.bravo@gmail.com
 
 