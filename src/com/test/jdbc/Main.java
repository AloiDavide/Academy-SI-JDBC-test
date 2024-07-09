package com.test.jdbc;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;




public class Main {

	public static void main(String[] args) {

		
		Database db = new Database();
		try {
			db.initializeDB("eserciziodbfinale");
			db.createTable("utente");
		} catch (SQLException e) {
			e.printStackTrace();
		}
			

		
		//Popolazione database
		List<String> names = List.of("Mario", "Andrea", "Massimo", "Sara", "Marco", "Marzia");
		List<String> lastNames = List.of("Rossi", "Verdi", "Bianchi", "Vallieri", "Graviglia", "Esposito");
			
		try {
			for (int i = 0; i < names.size(); i++) {
				db.insertUser(i+1, names.get(i), lastNames.get(i));
			}
		}
		catch (SQLException e) {
			System.out.println("Tabella utenti già popolata.");
		}
					
		
		
		//Queries
		try {
			String query1 = "SELECT l.* FROM libri l JOIN prestito p ON L.id = P.L_id JOIN utente u ON P.U_id = U.id WHERE U.cognome = \"Vallieri\" ORDER BY P.inizio";
			printResultSet("1", db.doQuery(query1));
			
			String query2 = "SELECT u.id, u.nome, u.cognome, COUNT(p.l_id) AS libri_letti FROM utente u LEFT JOIN prestito p ON u.id = p.u_id GROUP BY u.id, u.nome, u.cognome ORDER BY COUNT(p.l_id) DESC LIMIT 3;";
			printResultSet("2", db.doQuery(query2));
			
			String query3 = "SELECT u.*, l.titolo FROM utente u JOIN prestito p ON u.id = p.u_id JOIN libri l ON p.l_id = l.id WHERE p.fine IS NULL;";
			printResultSet("3", db.doQuery(query3));
			
			String query4 = "SELECT l.titolo, p.inizio, p.fine, DATEDIFF(p.fine, p.inizio) AS Durata FROM utente u JOIN prestito p ON u.id = p.u_id JOIN libri l ON p.l_id = l.id";
			printResultSet("4", db.doQuery(query4));
			
			String query5 = "SELECT l.titolo, COUNT(*) AS numero_prestiti FROM libri l JOIN prestito p ON l.id = p.l_id GROUP BY l.id ORDER BY COUNT(*) DESC;";
			printResultSet("5", db.doQuery(query5));
			
			String query6 = "SELECT id, DATEDIFF(fine, inizio) AS Durata FROM prestito WHERE DATEDIFF(fine, inizio) > 15";
			printResultSet("6", db.doQuery(query6));
			
			db.closeConnection();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	
	
	public static void printResultSet(String queryNumber, ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        System.out.println("\n-------------");
 
        System.out.println("QUERY " + queryNumber);
        while (rs.next()) {
            for (int i = 1; i <= columnCount; i++) {
    
                System.out.print(rs.getObject(i)+ ", ");
                
            }
            System.out.println();
        }
        
       
        
        rs.close();
    }

}
