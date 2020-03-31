package projetBD;

import Utilitaires.LectureClavier;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;



public class Terminaison_vol {
	public static void main(String[] args) {
		
		Identifiant Id = new Identifiant();

		try 
		{
			// Connexion a la base de donnee
	    	Connection bd = DriverManager.getConnection(Id.getUrlDatabase(),Id.getNomUtilisateur(),Id.getMdp());
	    	System.out.println("------ Connexion OK ------\n\n");
	    	
	    	int choix = 1;
	    	do
		    {
	    	
		    	// Traitemant : Confirmation de la terminaison d'un vol
		    	Statement requete1 ; 
		    	requete1 = bd.createStatement() ; // Creation d'un descripteur de requete
		    	
		    	// Affichage de la liste des vols
		    	ResultSet resultat1 = requete1.executeQuery
				(
						"SELECT * " +
						"FROM VOL"
				);
		    	
		    	System.out.println("\nListe des vols planifiés : \n");
		    	
		    	System.out.println("numero_vol\t"
		    					+ "horaire_vol\t"
		    					+ "date_vol\t"
		    					+ "duree_vol\t"
		    					+ "distance_vol\t"
		    					+ "etat_vol\t"
		    					+ "id_ville_provenir\t"
		    					+ "id_ville_destiner\t"
		    					+ "numero_avion");
		    	
		    	while(resultat1.next())
		    	{
		    		System.out.println(	  resultat1.getString(1) + "\t\t" + resultat1.getString(2) + "\t" 
		    							+ resultat1.getString(3) + "\t" + resultat1.getString(4)+ "\t"
		    							+ resultat1.getString(5) + "\t\t" + resultat1.getString(6)+ "\t\t" 
		    							+ resultat1.getString(7) + "\t\t\t" + resultat1.getString(8)+ "\t\t\t"
		    							+ resultat1.getString(9));
		    		
		    	}
		    	
		    	
		    	
		    	System.out.println("\nSaisir le numero de vol : ");
		    	System.out.println("0 pour Quitter");
    			int numV = LectureClavier.lireEntier("") ;
    			
    			if(numV == 0) {
    				System.out.println("BIG UP");
    				return ;
    			}
		    	
		    	requete1.close();
		    	resultat1.close();
	    	
		    	// Gestion des modification	
		    	
		    	Statement requete2 = bd.createStatement() ;
		    	int nb = requete2.executeUpdate
		    			( 
		    					"UPDATE vol SET etat_vol = 'OK'"
		    					+ "WHERE numero_vol = " + numV
		    					
    					);
		    	
		    	nb += requete2.executeUpdate
		    			( 
		    					"DELETE from affecter "
		    					+ "WHERE numero_vol = " + numV
		    					
    					);
		    	
		    	nb += requete2.executeUpdate
		    			( 
		    					"DELETE from piloter "
		    					+ "WHERE numero_vol = " + numV
		    					
    					);
		    	
		    	nb += requete2.executeUpdate
		    			( 
		    					"DELETE from reserve "
		    					+ "WHERE numero_vol = " + numV
		    					
    					);
		    	
		    	System.out.println("Nombres de lignes mise a jour : " + nb) ;
		    	requete2.close();
		    	
		    	
		    	
		    	
		    }while(choix != 0);
	    	
	    	bd.close();
	    	
    	}	catch(Exception e) 
			{
	    		e.printStackTrace();
	    	}

	}

}
