package projetBD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Driver {

	public static void main(String[] args) {
		
		Identifiant Id = new Identifiant();
		/**************************	Saisir le nom de la table*/
		//String nomTable ="";
		/**************************** */
		try {
	    	Connection conn = DriverManager.getConnection(Id.getUrlDatabase(),Id.getNomUtilisateur(),Id.getMdp());
	    	System.out.println("Connecté \n");
	    	//Statement state = conn.createStatement();
	    	
	    	System.out.println("--- Ajout & Suppression d'un personnel de vol ---");
	  	    System.out.println("1. Ajouter un nouveau pilote");
			System.out.println("2. Supression d'un pilote");
			System.out.println("3. Ajouter une nouvelle hotesse");
			System.out.println("4. Suppression d'une hotesse");
			
			int menu = LectureClavier.lireEntier("") ;
			
			switch(menu) {
				case 1 :
			    	
					// Ici on veut ajouter un pilote dans la BD
			    	Statement requeteAjoutPilote= conn.createStatement();
			    	System.out.println("Statement Crée \n");
			    	
			    	System.out.println("Ajouter un nouveau Pilote\n");
			    	
			    	System.out.print("Identifiant pilote: ");
			    	String idP = LectureClavier.lireChaine() ;
			    	
			    	System.out.print("Nom pilote: ");
			    	String nomP = LectureClavier.lireChaine() ;
			    	
			    	System.out.print("Prenom pilote : ");
			    	String prenomP = LectureClavier.lireChaine() ;
			    	
			    	System.out.print("Numero de la rue : ");
			    	String numRueP = LectureClavier.lireChaine() ;
			    	
			    	System.out.print("Rue : ");
			    	String rueP = LectureClavier.lireChaine() ;
			    	
			    	System.out.print("Code postal : ");
			    	String codePostalP = LectureClavier.lireChaine() ;
			    	
			    	System.out.print("Ville pilote : ");
			    	String villeP = LectureClavier.lireChaine() ;
			    	
			    	System.out.print("Pays pilote : ");
			    	String paysP = LectureClavier.lireChaine() ;
			    	
			    	// La requete suivante permet d'insere dans la table PILOTE 
			    	// les valeurs entrer par l'utilisateur (celles lues par LectureClavier)
			    	int reqAP = requeteAjoutPilote.executeUpdate
			    	(
			    	  "INSERT INTO PILOTE (id_pilote, nom_pilote, prenom_pilote, numeroRue_pilote, rue_pilote, code_postal_pilote, ville_pilote, pays_pilote) "
			    			+ "VALUES ('"+ idP +"', '" + nomP + "', '" + prenomP + "', '" + numRueP + "', '" + rueP + "', '" + codePostalP + "', '" + villeP + "', '" + paysP + "')"
			    	);
			    	System.out.println("Table trouvé \n");
			    	
			    	// Une fois le pilote ajouter, la BD est mise a jour, 
			    	// ainsi on obtient un entier de retour pour dire que la mise a jour s'est effectuer 
			    	System.out.println("MAJ = " + reqAP);
			    	
			    	// Maintenant on affiche le resultat de la suppression 
			  	    Statement requeteAffichePiloteApresAjout = conn.createStatement();
			 		ResultSet reqAffPAA = requeteAffichePiloteApresAjout.executeQuery
			 		(
			 				"SELECT id_pilote, nom_pilote, prenom_pilote From PILOTE"
			 				
			 		) ;
			 		
			 		// Affichage des pilotes disponibles apres suppression 
		 			System.out.println("Pilotes disponibles apres ajout :\n");
			 		while(reqAffPAA.next()) {
			 			System.out.println("id " + reqAffPAA.getString(1) + ", nom : " + reqAffPAA.getString(2) + ", prenom : " + reqAffPAA.getString(3));
			 		}
			    	
			 		requeteAffichePiloteApresAjout.close();
			 		reqAffPAA.close();
			    	requeteAjoutPilote.close();
					
					break;
				
				case 2: 
					
					// Ici on veut supprimer un pilote de la BD
			    	Statement requeteSuppPilote= conn.createStatement();
			    	System.out.println("Statement Crée \n");
			    	
			    	System.out.println("Suppression d'un pilote\n");
			    	
			    	// Cette requete, nous permet d'afficher les pilotes disponibles 
			  	    Statement requeteAffichePilote = conn.createStatement();
			 		ResultSet reqAffP = requeteAffichePilote.executeQuery
			 		(
			 				"SELECT id_pilote, nom_pilote, prenom_pilote From PILOTE"
			 				
			 		) ;
			 		
			 		// On procede a l'affichage des pilotes disponibles de la sorte : id, nom, prenom
		 			System.out.println("Liste des pilotes disponibles :\n");
			 		while(reqAffP.next()) {
			 			System.out.println("id :"+ reqAffP.getString(1) + ", nom : " + reqAffP.getString(2) + ", prenom : " + reqAffP.getString(3));
			 		}
			    	
			 		System.out.print("\n");
			    	System.out.print("Choisir l'identifiant du pilote a supprimer: ");
			    	String idPSupp = LectureClavier.lireChaine() ;
			    	
			    	// La requete suivante permet de supprimer de la table PILOTE 
			    	// les valeurs entrer par l'utilisateur (celles lues par LectureClavier)
			    	int reqSP = requeteSuppPilote.executeUpdate
			    	(
			    	  "DELETE FROM PILOTE WHERE id_pilote = '" + idPSupp +"'" 
			    	);
			    	
			    	// Une fois le pilote ajouter, la BD est mise a jour, 
			    	// ainsi on obtient un entier de retour pour dire que la mise a jour s'est effectuer
			    	System.out.println("MAJ = " + reqSP);
			    	
			    	// Maintenant on affiche le resultat de la suppression 
			  	    Statement requeteAffichePiloteApresSupp = conn.createStatement();
			 		ResultSet reqAffPAS = requeteAffichePiloteApresSupp.executeQuery
			 		(
			 				"SELECT id_pilote, nom_pilote, prenom_pilote From PILOTE"
			 				
			 		) ;
			 		
			 		// Affichage des pilotes disponibles apres suppression 
		 			System.out.println("Pilotes disponibles apres suppression :\n");
			 		while(reqAffPAS.next()) {
			 			System.out.println("id : " + reqAffPAS.getString(1) + ", nom : " + reqAffPAS.getString(2) + ", prenom : " + reqAffPAS.getString(3));
			 		}
			    	
			 		requeteAffichePiloteApresSupp.close();
			 		reqAffPAS.close();
			    	requeteAffichePilote.close();
			 		reqAffP.close();
			    	requeteSuppPilote.close();
			    	
					break; 
					
				case 3:
			    	
					// Ici on veut ajouter une hotesse dans la BD
			    	Statement requeteAjoutHotesse= conn.createStatement();
			    	System.out.println("Statement Crée \n");
			    	
			    	System.out.println("Ajouter un nouvelle Hotesse\n");
			    	
			    	System.out.print("Identifiant hotesse: ");
			    	String idH = LectureClavier.lireChaine() ;
			    	
			    	System.out.print("Nom hotesse: ");
			    	String nomH = LectureClavier.lireChaine() ;
			    	
			    	System.out.print("Prenom hotesse : ");
			    	String prenomH = LectureClavier.lireChaine() ;
			    	
			    	System.out.print("Numero de la rue : ");
			    	String numRueH = LectureClavier.lireChaine() ;
			    	
			    	System.out.print("Rue : ");
			    	String rueH = LectureClavier.lireChaine() ;
			    	
			    	System.out.print("Code postal : ");
			    	String codePostalH = LectureClavier.lireChaine() ;
			    	
			    	System.out.print("Ville hotesse : ");
			    	String villeH = LectureClavier.lireChaine() ;
			    	
			    	System.out.print("Pays hotesse : ");
			    	String paysH = LectureClavier.lireChaine() ;
			    	
			    	// La requete suivante permet d'insere dans la table HOTESSE 
			    	// les valeurs entrer par l'utilisateur (celles lues par LectureClavier)
			    	int reqAH = requeteAjoutHotesse.executeUpdate
			    	(
			    	  "INSERT INTO HOTESSE (id_hotesse, nom_hotesse, prenom_hotesse, numeroRue_hotesse, rue_hotesse, code_postal_hotesse, ville_hotesse, pays_hotesse) "
			    			+ "VALUES ('"+ idH +"', '" + nomH + "', '" + prenomH + "', '" + numRueH + "', '" + rueH + "', '" + codePostalH + "', '" + villeH + "', '" + paysH + "')"
			    	);
			    	System.out.println("Table trouvé \n");
			    	
			    	// Une fois l'hotesse ajouter, la BD est mise a jour, 
			    	// ainsi on obtient un entier de retour pour dire que la mise a jour s'est effectuer 
			    	System.out.println("MAJ = " + reqAH);
			    	
			    	// Maintenant on affiche le resultat de la suppression 
			  	    Statement requeteAfficheHotesseApresAjout = conn.createStatement();
			 		ResultSet reqAffHAA = requeteAfficheHotesseApresAjout.executeQuery
			 		(
			 				"SELECT id_hotesse, nom_hotesse, prenom_hotesse From HOTESSE"
			 				
			 		) ;
			 		
			 		// Affichage des hotesses disponibles apres suppression 
		 			System.out.println("Hotesses disponibles apres ajout :\n");
			 		while(reqAffHAA.next()) {
			 			System.out.println("id " + reqAffHAA.getString(1) + ", nom : " + reqAffHAA.getString(2) + ", prenom : " + reqAffHAA.getString(3));
			 		}
			    	
			 		requeteAfficheHotesseApresAjout.close();
			 		reqAffHAA.close();
			 		requeteAjoutHotesse.close();
					
					break;
				
				case 4: 
					
					// Ici on veut supprimer une hotesse de la BD
			    	Statement requeteSuppHotesse= conn.createStatement();
			    	System.out.println("Statement Crée \n");
			    	
			    	System.out.println("Suppression d'une hotesses\n");
			    	
			    	// Cette requete, nous permet d'afficher les hotesses disponibles 
			  	    Statement requeteAfficheHotesse = conn.createStatement();
			 		ResultSet reqAffH = requeteAfficheHotesse.executeQuery
			 		(
			 				"SELECT id_hotesse, nom_hotesse, prenom_hotesse From HOTESSE"
			 				
			 		) ;
			 		
			 		// On procede a l'affichage des hotesses disponibles de la sorte : id, nom, prenom
		 			System.out.println("Liste des hotesses disponibles :\n");
			 		while(reqAffH.next()) {
			 			System.out.println("id :"+ reqAffH.getString(1) + ", nom : " + reqAffH.getString(2) + ", prenom : " + reqAffH.getString(3));
			 		}
			    	
			 		System.out.print("\n");
			    	System.out.print("Choisir l'identifiant de l'hotesse a supprimer: ");
			    	String idHSupp = LectureClavier.lireChaine() ;
			    	
			    	// La requete suivante permet de supprimer de la table HOTESSE 
			    	// les valeurs entrer par l'utilisateur (celles lues par LectureClavier)
			    	int reqSH = requeteSuppHotesse.executeUpdate
			    	(
			    	  "DELETE FROM HOTESSE WHERE id_hotesse = '" + idHSupp +"'" 
			    	);
			    	
			    	// Une fois l'hotesse ajouter, la BD est mise a jour, 
			    	// ainsi on obtient un entier de retour pour dire que la mise a jour s'est effectuer
			    	System.out.println("MAJ = " + reqSH);
			    	
			    	// Maintenant on affiche le resultat de la suppression 
			  	    Statement requeteAfficheHotesseApresSupp = conn.createStatement();
			 		ResultSet reqAffHAS = requeteAfficheHotesseApresSupp.executeQuery
			 		(
			 				"SELECT id_hotesse, nom_hotesse, prenom_hotesse From HOTESSE"
			 				
			 		) ;
			 		
			 		// Affichage des hotesses disponibles apres suppression 
		 			System.out.println("Hotesses disponibles apres suppression :\n");
			 		while(reqAffHAS.next()) {
			 			System.out.println("id : " + reqAffHAS.getString(1) + ", nom : " + reqAffHAS.getString(2) + ", prenom : " + reqAffHAS.getString(3));
			 		}
			    	
			 		requeteAfficheHotesseApresSupp.close();
			 		reqAffHAS.close();
			 		requeteAfficheHotesse.close();
			 		reqAffH.close();
			 		requeteSuppHotesse.close();
					
					break;
			}
	    	
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}

	}

}

