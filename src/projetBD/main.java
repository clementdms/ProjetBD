package projetBD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class main {

	public static void main(String[] args) {
		
		Identifiant Id = new Identifiant();
		/**************************	Saisir le nom de la table*/
		String nomTable ="";
		/**************************** */
		try {
	    	Connection conn = DriverManager.getConnection(Id.getUrlDatabase(),Id.getNomUtilisateur(),Id.getMdp());
	    	System.out.println("Connecté \n");
	    	Statement state = conn.createStatement();
	    	System.out.println("Statement Crée \n");
	    	ResultSet rs=state.executeQuery("select * from "+nomTable);
	    	System.out.println("Table trouvé \n");
	    	
	    	//traitement
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}

	}

}
