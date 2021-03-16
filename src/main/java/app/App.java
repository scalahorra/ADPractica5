package app;

import java.util.Objects;

import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.modules.XPathQueryService;

public class App {

	private static Database db;
	private static XPathQueryService xp;
	
	private static String URI = "exist://localhost:8080/exist/xmlrpc/db/practica5";
	private static String USUARIO = "admin";
	private static String CONTRASENA = "123456";
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		
		try {
			
			db = (Database) Class.forName("org.exist.xmldb.DatabaseImpl").newInstance();
						
			Collection col = Objects.requireNonNull(db.getCollection(URI, USUARIO, CONTRASENA));
			
			xp = Objects.requireNonNull((XPathQueryService) col.getService(XPathQueryService.SERVICE_NAME,null));
			
			ResourceIterator ri;
			
			
			System.out.println("********************************************************************************");
			System.out.println("1. Obtén los nodos denominación y precio de todos los productos");
			
			ri = xp.query("/productos/produc/*[self::denominacion or self::precio]").getIterator();
			
			while(ri.hasMoreResources()) {
				
				XMLResource res = (XMLResource) ri.nextResource();
				System.out.println("\t" + res.getContent());
			}
			
			System.out.println("********************************************************************************");
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}

	}

}
