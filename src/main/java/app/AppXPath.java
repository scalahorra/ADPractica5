package app;

import java.util.Objects;

import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.modules.XPathQueryService;

public class AppXPath {

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
				
				XMLResource n = (XMLResource) ri.nextResource();
				System.out.println("\t" + n.getContent());
			}
			
			System.out.println("********************************************************************************");
			
			System.out.println("********************************************************************************");
			System.out.println("2. Obtén los nodos de los productos que sean placas base");
			
			ri = xp.query("/productos/produc[denominacion[contains(., 'Placa Base')]]").getIterator();
			
			while(ri.hasMoreResources()) {
				
				XMLResource n = (XMLResource) ri.nextResource();
				System.out.println("\t" + n.getContent());
			}
			
			System.out.println("********************************************************************************");
			
			System.out.println("********************************************************************************");
			System.out.println("3. Obtén los nodos de los productos con precio mayor de 60€ y de la zona 20");
			
			ri = xp.query("/productos/produc[precio[text() > 60] and cod_zona[text() = 20]]").getIterator();
			
			while(ri.hasMoreResources()) {
				
				XMLResource n = (XMLResource) ri.nextResource();
				System.out.println("\t" + n.getContent());
			}
			
			System.out.println("********************************************************************************");
			
			System.out.println("********************************************************************************");
			System.out.println("4. Obtén el número de productos que sean memorias y de la zona 10");
			
			ri = xp.query("count(/productos/produc[denominacion[contains(., 'Memoria')] and cod_zona[text() = 10]])").getIterator();
			
			while(ri.hasMoreResources()) {
				
				XMLResource n = (XMLResource) ri.nextResource();
				System.out.println("\tNúmero de productos: " + n.getContent());
			}
			
			System.out.println("********************************************************************************");
			
			System.out.println("********************************************************************************");
			System.out.println("5. Obtén la media de precio de los micros");
			
			ri = xp.query("sum(/productos/produc[denominacion[contains(., 'Micro')]]/precio/text()) "
					+ "div count(/productos/produc[denominacion[contains(., 'Micro')]])").getIterator();
			
			while(ri.hasMoreResources()) {
				
				XMLResource n = (XMLResource) ri.nextResource();
				System.out.println("\tLa media es: " + n.getContent());
			}
			
			System.out.println("********************************************************************************");
			
			System.out.println("********************************************************************************");
			System.out.println("6. Obtén los datos de los productos cuyo stock mínimo sea mayor que su stock actual");
			
			ri = xp.query("/productos/produc[number(stock_minimo) > number(stock_actual)]").getIterator();
			
			while(ri.hasMoreResources()) {
				
				XMLResource n = (XMLResource) ri.nextResource();
				System.out.println("\t" + n.getContent());
			}
			
			System.out.println("********************************************************************************");
			
			System.out.println("********************************************************************************");
			System.out.println("7. Obtén el nombre del producto y el precio de aquellos cuyo stock mínimo sea mayor "
					+ "que su stock actual y sean de la zona 40");
			
			ri = xp.query("/productos/produc/*[(self::denominacion or self::precio) and number(../stock_minimo/text()) >"
					+ " number(../stock_actual/text()) and ../cod_zona/text() = 40]/text()").getIterator();
			
			while(ri.hasMoreResources()) {
				
				XMLResource n = (XMLResource) ri.nextResource();
				System.out.println("\t" + n.getContent());
			}
			
			System.out.println("********************************************************************************");
			
			System.out.println("********************************************************************************");
			System.out.println("8. Obtén el producto más caro");
			
			ri = xp.query("/productos/produc[precio = max(/productos/produc/precio)]").getIterator();
			
			while(ri.hasMoreResources()) {
				
				XMLResource n = (XMLResource) ri.nextResource();
				System.out.println("\t" + n.getContent());
			}
			
			System.out.println("********************************************************************************");
			
			System.out.println("********************************************************************************");
			System.out.println("9. Obtén el producto más barato de la zona 20");
			
			ri = xp.query("/productos/produc[precio = min(/productos/produc[cod_zona = 20]/precio)]").getIterator();
			
			while(ri.hasMoreResources()) {
				
				XMLResource n = (XMLResource) ri.nextResource();
				System.out.println("\t" + n.getContent());
			}
			
			System.out.println("********************************************************************************");
			
			System.out.println("********************************************************************************");
			System.out.println("10. Obtén el producto más caro de la zona 10");
			
			ri = xp.query("/productos/produc[precio = max(/productos/produc[cod_zona = 10]/precio)]").getIterator();
			
			while(ri.hasMoreResources()) {
				
				XMLResource n = (XMLResource) ri.nextResource();
				System.out.println("\t" + n.getContent());
			}
			
			System.out.println("********************************************************************************");
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}

	}

}
