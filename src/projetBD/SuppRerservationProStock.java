package projetBD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import java.sql.*;
import java.io.*;

public class SuppRerservationProStock {

	public static void main(String[] args) {
		
		Identifiant Id = new Identifiant();
		
		try {
	    	Connection conn = DriverManager.getConnection(Id.getUrlDatabase(),Id.getNomUtilisateur(),Id.getMdp());
	    	System.out.println("Connecté \n");
	    	
	    	// Décalaration des statements 
	    	//Statement reqProcedureStockAffichage= conn.createStatement();
	    	Statement reqProcedureStockSuppReserve= conn.createStatement();
	    	
	    	// Cette requete, nous permet d'afficher les clients qui ont une reservation
	  	    Statement requeteAfficheClientReservation = conn.createStatement();
	 		ResultSet reqAffCR = requeteAfficheClientReservation.executeQuery
	 		(
	 				"SELECT id_client, nom_client, prenom_client From CLIENT NATURAL JOIN RESERVATION"
	 				
	 		) ;
	 		
	 		// On procede a l'affichage des clients avec une reservation de la sorte : id, nom, prenom
 			System.out.println("Liste des clients avec une reservation:\n");
	 		while(reqAffCR.next()) {
	 			System.out.println("id :"+ reqAffCR.getString(1) + ", nom : " + reqAffCR.getString(2) + ", prenom : " + reqAffCR.getString(3));
	 		}
	    	
	    	// Maintenant vu que l'on a une liste des clients avec une reservation 
	    	// l'utilisateur va pouvoir supprimer une reservation pour un client en donnant son identifiant
	    	
	    	System.out.print("Choisir l'identifiant du client pour supprimer ça réservation : ");
	    	int idCSuppReserve = LectureClavier.lireEntier("") ;
	    
	    	
	    	// Ici on recupere le resutat de la requete SQL contenu dans notre procédure stockee 
	    	//ResultSet rPSSR = reqProcedureStockSuppReserve.executeQuery("{CALL supprimer_clients_reservation(?)}");
	    	
	    	// Appel de la procedure
	    	String sql = "{CALL supprimer_clients_reservation(?)}";
	    	CallableStatement call = conn.prepareCall(sql); 
	    	
	    	// passage de l'entier idCSuppReserve récupérer comme valeur du premier paramètre
	    	call.setInt(1, idCSuppReserve);
	    	
	    	// exécution 
	    	call.execute();
	    	
	    	System.out.println("Reservation supprimer pour le client d'id :" + idCSuppReserve);
	    	
	    	// Fermeture 
	    	reqProcedureStockSuppReserve.close();
	    	//rPSSR.close();
	    	call.close();
	    	
	    	requeteAfficheClientReservation.close();
	    	reqAffCR.close();
	    	conn.close();
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}

	}

}
