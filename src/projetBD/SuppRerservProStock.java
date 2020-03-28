package projetBD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import java.sql.*;
import java.io.*;

public class SuppRerservProStock {

	public static void main(String[] args) {
		
		Identifiant Id = new Identifiant();
		
		try {
	    	Connection conn = DriverManager.getConnection(Id.getUrlDatabase(),Id.getNomUtilisateur(),Id.getMdp());
	    	System.out.println("Connect� \n");
	    	
	    	// D�calaration des statements 
	    	Statement reqProcedureStockAffichage= conn.createStatement();
	    	Statement reqProcedureStockSuppReserve= conn.createStatement();
	    	
	    	System.out.println("Statement Cr�e \n");
			// "SELECT id_client, nom_client, prenom_client From CLIENT NATURAL JOIN RESERVATION"
			// On commence apr cr�er la procedure stock� pour afficher les clients qui ont une reservation
	    	reqProcedureStockAffichage.execute(
	    			"CREATE PROCEDURE afficher_clients_avec_reservation() "
	    			+ "BEGIN "
	    			+ "SELECT id_client, nom_client, prenom_client "
	    			+ "From CLIENT NATURAL JOIN RESERVATION; "
	    			+ "END"
	    	);
	    
	    	// Ici on recupere le resutat de la requete SQL contenu dans notre proc�dure stockee 
	    	ResultSet rPSA = reqProcedureStockAffichage.executeQuery("{CALL afficher_clients_avec_reservation()}");
	    	
	    	// Enfin, on procede a l'affichage des clients avec une reservation de la sorte : id, nom, prenom
	    	System.out.print("Liste des clients avec une reservation:\n");
	    	while(rPSA.next()) {
	    		System.out.println("id :"+ rPSA.getString(1) + ", nom : " + rPSA.getString(2) + ", prenom : " + rPSA.getString(3));
	    	}
	    	
	    	// Maintenant vu que l'on a une liste des clients avec une reservation 
	    	// l'utilisateur va pouvoir supprimer une reservation pour un client en donnant son identifiant
	    	
	    	System.out.print("Choisir l'identifiant du client pour supprimer �a r�servation : ");
	    	int idCSuppReserve = LectureClavier.lireEntier("") ;
	    	
	    	reqProcedureStockSuppReserve.executeUpdate(
	    			"CREATE PROCEDURE supprimer_clients_reservation(IN idCSR INT) "
	    			+ "BEGIN "
	    			+ "DELETE FROM RESERVATION WHERE id_client = idCSR;"
	    			+ "END"
	    	);
	    	
	    	// Ici on recupere le resutat de la requete SQL contenu dans notre proc�dure stockee 
	    	//ResultSet rPSSR = reqProcedureStockSuppReserve.executeQuery("{CALL supprimer_clients_reservation(?)}");
	    	
	    	// Appel de la procedure
	    	String sql = "{CALL supprimer_clients_reservation(?)}";
	    	CallableStatement call = conn.prepareCall(sql); 
	    	
	    	// passage de l'entier idCSuppReserve r�cup�rer comme valeur du premier param�tre
	    	call.setInt(1, idCSuppReserve);
	    	
	    	// ex�cution 
	    	call.execute();
	    	
	    	System.out.println("Reservation supprimer pour le client d'id :" + idCSuppReserve);
	    	
	    	// Fermeture 
	    	reqProcedureStockSuppReserve.close();
	    	//rPSSR.close();
	    	call.close();
	    	
	    	reqProcedureStockAffichage.close();
	    	rPSA.close();
	    	conn.close();
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}

	}

}
