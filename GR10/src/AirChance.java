import java.sql.*;
import java.sql.DriverManager;

public class AirChance {

	private static final String configurationFile = "src/BD.properties";
	static Connection conn; 
	
	public static void main(String[] args) {
		try {
	  	    // Enregistrement du driver Oracle
	  	    System.out.print("Loading Oracle driver... "); 
	  	    DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());  	    
	  	    System.out.println("loaded");
	  	 
	  	    // Etablissement de la connection
		    DatabaseAccessProperties dap = new DatabaseAccessProperties(configurationFile);
	  	    System.out.print("Connecting to the database... "); 
	 	    conn = DriverManager.getConnection(dap.getDatabaseUrl(), dap.getUsername(),dap.getPassword());
	   	    System.out.println("connected");
	  	    conn.setAutoCommit(true);
			
			System.out.println("----Connecté---");
			int choixUtilisateur=LectureClavier.lireEntier("Que souhaitez vous faire ?\n"+
			"1-\tPlannifier un nouveau vol\n"+
			"2-\tModifier la plannification d'un nouveau vol\n"+
			"3-\tSupprimer un vol\n"+
			"4-\tConfirmer la terminaison d'un vol\n"+
			"5-\tAjouter/Supprimer du personnel de vol\n"+
			"6-\tAffichage de la commande d'un client\n"+
			"7-\tReserver un vol (pour un client)\n"+
			"8-\tSupprimer une reservation (client)\n"+
			"Tout autre nombre engendrera la fermeture du programme\n");
			switch(choixUtilisateur) {
				case 1:
					if(GestionPlanificationVol.AjoutVol(conn)) {
						System.out.println("Vol ajouter avec succes");
					}
					break;
				case 2:
					GestionPlanificationVol.GestionVol(conn);
					break;
				case 3:	
					GestionPlanificationVol.SupprimerVol(conn);
					break;
				case 4:
					GestionPlanificationVol.TerminaisonVol(conn);
					break;
				case 5:
					GestionPlanificationVol.Ajout_Suppr_Personnel(conn);
					break;
				case 6:
					GestionReservation.AfficheCommandeClient(conn);
					break;
				case 7:
					GestionReservation.ReserveVol(conn);
					break;
				case 8:
					GestionReservation.SuppressionCommandeClient(conn);
					break;
				default:
					System.out.println("Au revoir");
					break;
			}			
		conn.close();	
		} catch(Exception e) {
    		e.printStackTrace();
    	}
	}
	
}
