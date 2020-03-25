package projetBD;

import Utilitaires.LectureClavier;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;




public class Gestion_vol {

	public static void main(String[] args) {
		
		Identifiant Id = new Identifiant();

		try 
		{
			// Connexion a la base de donnee
	    	Connection bd = DriverManager.getConnection(Id.getUrlDatabase(),Id.getNomUtilisateur(),Id.getMdp());
	    	System.out.println("------ Connexion OK ------\n\n");
	    	
	    	int choix = 0;
	    	do
		    {
	    	
		    	// Traitemant : Modification de la planification d'un vol existant
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

		    	System.out.println("\n\nQuelle modification voulez vous apporter ? \n");
		    	System.out.println("1.\tRemplacement d'un avion par un  autre ");
		    	System.out.println("2.\tRemplacement d'un membre de l'equipage ");
		    	System.out.println("3.\tModification des donnees de configuration d'un vol ");
		    	System.out.println("4.\tQuitter ");

		    	
		    	choix = LectureClavier.lireEntier("") ;
		    	
	
		    	switch (choix)
		    	{
		    		case 1: //----------------------------------------------------------------------------------------------------------------------------------------
		    			
		    			// Nombre de places du vol
		    			Statement requete2 = bd.createStatement() ;
		    			ResultSet resultat2 = requete2.executeQuery
						(
								"SELECT numero_avion, COUNT(numero_place) AS nb_place " + 
								"FROM place " + 
								"WHERE numero_avion = (SELECT numero_avion FROM vol WHERE numero_vol = "+ numV +") " + 
								"GROUP BY numero_avion "
								
						);
		    			
		    			String nb_places = "";
		    			String nA = "";
		    			
		    			while (resultat2.next())
		    			{
		    				//System.out.println(resultat2.getString(1) + "\t\t" + resultat2.getString(2));
		    				
		    				nA = resultat2.getString(1) ;
		    				nb_places = resultat2.getString(2) ;
		   
		    			}
		    			
		    			
		    	    	requete2.close();
		    	    	resultat2.close();
		    					
		    			
		    			// Liste des avions compatibles 
		    			Statement requete3 = bd.createStatement() ; 
		    			ResultSet resultat3 = requete3.executeQuery
						(
							  "SELECT numero_avion, count(numero_place) as nb_place "
							+ "FROM place "
							+ "WHERE numero_avion != " + nA 
							+ " GROUP BY numero_avion "
							+ "HAVING count(numero_place) = " + nb_places	
						); 
		    			
		    			System.out.println("Liste des avions compatibles : \n");
		    			
		    			int nb_avion = 0;
		    			
		    			System.out.println("numero_avion\tnb_places") ;
		    			while (resultat3.next())
		    			{
		    				System.out.println(resultat3.getString(1) + "\t\t" + resultat3.getString(2));
		    				nb_avion ++ ;
		    			}
		    			
		    			int numA = -9 ;
		    			
		    			if(nb_avion > 0)
		    			{
		    				numA = LectureClavier.lireEntier("\nVeuillez choisir un numero d'avion ") ;
		    			}
		    			else
		    			{
		    				System.out.println("----\n\n");
		    				System.out.println("Nous allons proceder a la supression du vol car il n'y a pas d'avion compatible");
		    			}
		    			
		    			
		    			
		    			
		    	    	requete3.close();
		    	    	resultat3.close();
		    	    	
		    	    	
		    	    	
		    	    	// Mofication
		    	    	Statement requete4 = bd.createStatement() ;
		    	    	int nb = -1; // Pour connaitre le nombre de lignes mises a jour
		    	    	
		    	    	
		    	    	// Si il y a des avions disponibles, on effectue le remplacement
		    			// Sinon on supprime le vol
		    			if(nb_avion > 0)
		    			{
		    				nb = requete4.executeUpdate
	    						(
									  "UPDATE vol SET numero_avion = " + numA
									+ " WHERE numero_vol = " + numV
	    									
								);
		    				
		    				System.out.println("Nombres de lignes mise a jour : " + nb) ;
		    			}
		    			else
		    			{	// SUPPRESSION DE VOL  a modifier avec la fonctionalite de SUPRESSION de la question 3
		    				nb = requete4.executeUpdate
	    						(
	    								"DELETE "
	    								+ "FROM vol "
	    								+ "WHERE numero_vol = " + numV
	    						);
		    				
		    				System.out.println("Nombres de lignes supprimees : " + nb) ;
		    			}
		    				
		    	    	requete4.close();
		    			
		    			break;
		    			// ------------------------------------------------------------------------------------------------------------------------------------------
		    		
		    		case 2: // --------------------------------------------------------------------------------------------------------------------------------------
		    			System.out.println("1\tPilote");
		    			System.out.println("2\tHotesse");
		    			
		    			int choix2 = LectureClavier.lireEntier("") ;
		    			
		    			Statement requete5 = bd.createStatement() ;
		    			ResultSet resultat5 = null ;
		    			int id;
		    			int new_id ;
		    			int nb2 ;
		    			
		    			
		    			if(choix2 == 1) 
		    			{
		    				// Liste des pilotes de l'equipage
			    			resultat5 = requete5.executeQuery
			    					(
		    							"SELECT id_pilote AS id, nom_pilote, prenom_pilote " + 
		    							"FROM piloter NATURAL JOIN pilote " + 
		    							"WHERE numero_vol = " + numV 	
	    							);
			    			
			    			System.out.println("id_pilote\t"
			    							  + "nom_pilote\t"
			    							  + "prenom_pilote");
			    			while(resultat5.next()) 
			    			{
			    				System.out.println(	  resultat5.getString(1) + "\t\t" + resultat5.getString(2) + "\t\t" 
		    							+ resultat5.getString(3));
			    				
			    			}
			    			
			    			System.out.println("\nSaisir le numero du pilote : ");
			    			id = LectureClavier.lireEntier("");
			    			
			    			System.out.println("Par qui voulez vous le remplacer ?") ;
			    			// Liste des pilotes disponibles
			    			resultat5 = requete5.executeQuery
			    					(
			    							"SELECT id_pilote AS id, nom_pilote, prenom_pilote " + 
			    							"FROM pilote " + 
			    							"WHERE id_pilote != " + id 
			    							
			    					);
			    			
			    			System.out.println("id_pilote\t\t"
	    							  + "nom_pilote\t\t"
	    							  + "prenom_pilote");
			    			
			    			while(resultat5.next()) 
			    			{
			    				System.out.println(	  resultat5.getString(1) + "\t\t\t" + resultat5.getString(2) + "\t\t\t" 
		  							+ resultat5.getString(3));
			    				
			    			}
			    			
			    			new_id = LectureClavier.lireEntier("");
			    			
			    			// Mise a jour de la table Pilote
			    			nb2 = requete5.executeUpdate
			    					(
			    							"UPDATE piloter "
			    							+ "SET  id_pilote = " + new_id 
			    							+ " WHERE id_pilote = " + id + " AND numero_vol = " + numV	
			    							
			    					);
			    			
			    			System.out.println("Nombre de lignes mise a jour : " + nb2);
			    			
			    			
			    			
			    			
		    			}
		    			else if(choix2 == 2)
		    			{
		    				// Liste des hotesses de l'equipage
			    			resultat5 = requete5.executeQuery
			    					(
		    							"SELECT id_hotesse AS id, nom_hotesse, prenom_hotesse " + 
		    							"FROM affecter NATURAL JOIN hotesse " + 
		    							"WHERE numero_vol = " + numV 	
	    							);
			    			
			    			System.out.println("id_hotesse\t"
			    							  + "nom_hotesse\t"
			    							  + "prenom_hotesse");
			    			while(resultat5.next()) 
			    			{
			    				System.out.println(	  resultat5.getString(1) + "\t\t" + resultat5.getString(2) + "\t" 
		    							+ resultat5.getString(3));
			    			}
			    			
			    			// Liste des hotesses disponibles
			    			System.out.println("\nSaisir le numero de l'hotesse : ");
			    			id = LectureClavier.lireEntier("");
			    			
			    			System.out.println("Par qui voulez vous la remplacer ?") ;
			    			// Liste des hotesses disponibles
			    			resultat5 = requete5.executeQuery
			    					(
			    							"SELECT id_hotesse AS id, nom_hotesse, prenom_hotesse " + 
			    							"FROM hotesse " + 
			    							"WHERE id_hotesse != " + id 
			    							
			    					);
			    			
			    			System.out.println("id_hotesse\t"
	    							  + "nom_hotesse\t"
	    							  + "prenom_hotesse");
			    			
			    			while(resultat5.next()) 
			    			{
			    				System.out.println(resultat5.getString(1) + "\t\t" + resultat5.getString(2) + "\t\t" 
		  							+ resultat5.getString(3));
			    				
			    			}
			    			
			    			new_id = LectureClavier.lireEntier("");
			    			
			    			// Mise a jour de la table Pilote
			    			nb2 = requete5.executeUpdate
			    					(
			    							"UPDATE affecter "
			    							+ "SET  id_hotesse = " + new_id 
			    							+ " WHERE id_hotesse = " + id + " AND numero_vol = " + numV
			    							
			    					);
			    			
			    			System.out.println("Nombre de lignes mise a jour : " + nb2);
			    			
			    			
		    			}
		    			
		    			
		    			
		    	    	requete5.close();
		    	    	resultat5.close();
		    			
		    			
		    			break;
		    			// ------------------------------------------------------------------------------------------------------------------------------------------
		    		
		    		case 3: // --------------------------------------------------------------------------------------------------------------------------------------
		    			System.out.println("Quelles donnees voulez vous mofidier ? ");
		    			System.out.println("1.\tHoraire");
		    			System.out.println("2.\tDate");
		    			System.out.println("3.\tDuree");
		    			System.out.println("4.\tDistance");
		    			System.out.println("5.\tEtat");
		    			System.out.println("6.\tVille");
		    			System.out.println("7.\tDestination");
		    			
		    			int choix3 = LectureClavier.lireEntier("") ;
		    			Statement requete6 = bd.createStatement();
		    			int nb3 ;
		    			String change = "" ;
		    			
		    			switch (choix3)
		    			{
		    				case 1:
		    					System.out.println("Saisir les nouveaux horaires : ");
		    					change = LectureClavier.lireChaine() ;
		    					
		    					nb3 = requete6.executeUpdate
		    							(
		    									"UPDATE vol "
		    									+ "SET horaire_vol = '" + change + "'" 
		    									+ " WHERE numero_vol = " + numV 
    									);
		    					
		    					System.out.println("Nombre de lignes mise a jour : " + nb3);
		    					
		    					break;
		    					
		    				case 2:
		    					System.out.println("Saisir la nouvelle date : ");
		    					change = LectureClavier.lireChaine() ;
		    					
		    					nb3 = requete6.executeUpdate
		    							(
		    									"UPDATE vol "
		    									+ "SET date_vol = '" + change + "'" 
		    									+ " WHERE numero_vol = " + numV 
    									);
		    					
		    					System.out.println("Nombre de lignes mise a jour : " + nb3);
		    					break;
		    					
		    				case 3:
		    					System.out.println("Saisir la nouvelle duree : ");
		    					change = LectureClavier.lireChaine() ;
		    					
		    					nb3 = requete6.executeUpdate
		    							(
		    									"UPDATE vol "
		    									+ "SET duree_vol = '" + change + "'" 
		    									+ " WHERE numero_vol = " + numV 
    									);
		    					
		    					System.out.println("Nombre de lignes mise a jour : " + nb3);
		    					
		    					break;
		    					
		    				case 4:
		    					System.out.println("Saisir la nouvelle distance : ");
		    					double dist = LectureClavier.lireDouble("") ;
		    					
		    					nb3 = requete6.executeUpdate
		    							(
		    									"UPDATE vol "
		    									+ "SET distance_vol = " + dist  
		    									+ " WHERE numero_vol = " + numV 
    									);
		    					
		    					System.out.println("Nombre de lignes mise a jour : " + nb3);
		    					
		    					break;
		    					
		    				case 5:
		    					System.out.println("Saisir le nouvel etat : ");
		    					change = LectureClavier.lireChaine() ;
		    					
		    					nb3 = requete6.executeUpdate
		    							(
		    									"UPDATE vol "
		    									+ "SET etat_vol = '" + change + "'" 
		    									+ " WHERE numero_vol = " + numV 
    									);
		    					
		    					System.out.println("Nombre de lignes mise a jour : " + nb3);
		    					
		    					break;
		    					
		    				case 6:
		    					System.out.println("Saisir le nouvel id de la ville de depart : ");
		    					int id_ville = LectureClavier.lireEntier("");
		    					
		    					nb3 = requete6.executeUpdate
		    							(
		    									"UPDATE vol "
		    									+ "SET id_ville_provenir = " + id_ville 
		    									+ " WHERE numero_vol = " + numV 
    									);
		    					
		    					System.out.println("Nombre de lignes mise a jour : " + nb3);
		    					
		    					break;
		    					
		    				case 7:
		    					System.out.println("Saisir le nouvel id de la ville de destination : ");
		    					id_ville = LectureClavier.lireEntier("");
		    					
		    					nb3 = requete6.executeUpdate
		    							(
		    									"UPDATE vol "
		    									+ "SET id_ville_destiner = " + id_ville 
		    									+ " WHERE numero_vol = " + numV 
    									);
		    					
		    					System.out.println("Nombre de lignes mise a jour : " + nb3);
		    					
		    					break;
		    			
		    			}
		    			
		    			requete6.close();
		    			
		    			break;
		    			// ------------------------------------------------------------------------------------------------------------------------------------------
		    			
		    		case 4:
		    			System.out.println("BIG UP");
		    			break;
		    	}
	    	}while(choix != 4);
	    	
	    	bd.close();
	    	
    	}	catch(Exception e) 
			{
	    		e.printStackTrace();
	    	}

	}

}
