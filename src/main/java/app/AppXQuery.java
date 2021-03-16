package app;

import java.util.Objects;

import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.modules.XQueryService;

public class AppXQuery {

	private static Database db;
	private static XQueryService xq;
	
	private static String URI = "exist://localhost:8080/exist/xmlrpc/db/practica5";
	private static String USUARIO = "admin";
	private static String CONTRASENA = "123456";
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		
		try {
			
			db = (Database) Class.forName("org.exist.xmldb.DatabaseImpl").newInstance();
			
			Collection col = Objects.requireNonNull(db.getCollection(URI, USUARIO, CONTRASENA));
			
			xq = Objects.requireNonNull((XQueryService) col.getService(XQueryService.SERVICE_NAME,null));
			
			ResourceIterator ri;
			
			System.out.println("****CONSULTAS PRODUCTOS****");
			System.out.println("********************************************************************************");
			System.out.println("1. Obtén por cada zona el número de productos que tiene");
			
			ri = xq.query("for $v in distinct-values(/productos/produc/cod_zona) return ($v, count(/productos/produc[cod_zona = $v]))").getIterator();

			while (ri.hasMoreResources()) {
				XMLResource n = ((XMLResource) ri.nextResource());
				System.out.print("\tZona " + n.getContent());
				if (ri.hasMoreResources()) {
					XMLResource n2 = ((XMLResource) ri.nextResource());
					System.out.println(": " + n2.getContent() + " productos");
				}
			}			
			System.out.println("********************************************************************************");
			
			System.out.println("********************************************************************************");
			System.out.println("2. Obtén la denominación de los productos entre las etiquetas <zona10></zona10>\r\n"
					+ "si son del código de zona 10, <zona20></zona20> si son del código de zona 20,\r\n"
					+ "<zona30></zona30> si son del código de zona 30 y <zona40></zona40> si son del\r\n"
					+ "código de zona 40.");
			
			ri = xq.query("for $v in distinct-values(/productos/produc/cod_zona) return element{ 'zona' || $v }{ /productos/produc[cod_zona = "
					+ "$v]/denominacion }").getIterator();

			while (ri.hasMoreResources()) {
				XMLResource n = ((XMLResource) ri.nextResource());
				System.out.println("\t" + n.getContent());
			}			
			System.out.println("********************************************************************************");
			
			System.out.println("********************************************************************************");
			System.out.println("3. Obtén por cada zona la denominación del o de los productos más caros");
			
			ri = xq.query("for $v in distinct-values(/productos/produc/cod_zona) return ($v, /productos/produc[precio ="
					+ " max(/productos/produc[cod_zona = $v]/precio)]/denominacion/text())").getIterator();

			while (ri.hasMoreResources()) {
				XMLResource n = ((XMLResource) ri.nextResource());
				System.out.print("\tZona " + n.getContent());
				if (ri.hasMoreResources()) {
					XMLResource n2 = ((XMLResource) ri.nextResource());
					System.out.println(": " + n2.getContent() + " es el producto más caro");
				}
			}			
			System.out.println("********************************************************************************");
			
			System.out.println("********************************************************************************");
			System.out.println("4. Obtén la denominación de los productos contenida entra las etiquetas <placa></pla-\r\n"
					+ "ca> para los productos en cuya denominación aparece la palabra Placa Base, <me-\r\n"
					+ "moria></memoria>, para los que contienen la palabra Memoria <micro></micro>,\r\n"
					+ "\r\n"
					+ "para los que contienen la palabra Micro y <otros></otros> para el resto de pro-\r\n"
					+ "ductos.");
			
			ri = xq.query("(<placa>{/productos/produc/denominacion[contains(., 'Placa Base')]}</placa>,"
					+ "<micro>{/productos/produc/denominacion[contains(., 'Micro')]}</micro>,"
					+ "<memoria>{/productos/produc/denominacion[contains(., 'Memoria')]}</memoria>,"
					+ "<otros>{/productos/produc/denominacion[not(contains(., 'Memoria') or contains(., 'Micro')"
					+ " or contains(., 'Placa Base'))]}</otros>)").getIterator();

			while (ri.hasMoreResources()) {
				XMLResource n = ((XMLResource) ri.nextResource());
				System.out.println("\t" + n.getContent());	
			}
			System.out.println("********************************************************************************");
			
			System.out.println("****CONSULTAS SUCURSALES****");
			System.out.println("********************************************************************************");
			System.out.println("1. Devuelve el código de sucursal y el número de cuentas que tiene de tipo AHORRO y de tipo pensiones.");
			
			ri = xq.query("for $suc in /sucursales/sucursal return (data($suc/@codigo), count($suc/cuenta[data(@tipo)='AHORRO']),"
					+ " count($suc/cuenta[data(@tipo)='PENSIONES']))").getIterator();

			while (ri.hasMoreResources()) {
				XMLResource n = ((XMLResource) ri.nextResource());
				System.out.print("Sucursal " + n.getContent());

				if (ri.hasMoreResources()) {
					XMLResource n2 = (XMLResource) ri.nextResource();
					System.out.print(": " + n2.getContent() + " cuentas de ahorro y ");
				}

				if (ri.hasMoreResources()) {
					XMLResource n2 = (XMLResource) ri.nextResource();
					System.out.println(n2.getContent() + " cuentas de pensiones");
				}
			}			
			System.out.println("********************************************************************************");
			
			System.out.println("********************************************************************************");
			System.out.println("5. Devuelve la cuenta del tipo PENSIONES que ha hecho más aportación.");
			
			ri = xq.query("/sucursales/sucursal/cuenta[data(@tipo) = 'PENSIONES' and aportacion ="
					+ " max(/sucursales/sucursal/cuenta/aportacion)]").getIterator();

			while(ri.hasMoreResources()) {
				
				XMLResource n = (XMLResource) ri.nextResource();
				System.out.println("\t" + n.getContent());
			}			
			System.out.println("********************************************************************************");

			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}
}
