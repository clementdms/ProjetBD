package projetBD;

import java.sql.*;


public class Reservation {

	
	public static int getIdClient(Connection conn) {
		int idClient=-1;
		String NumPasseport=LectureClavier.lireChaine("Veuillez saisir le numero de passeport du client : ");
		try{	
			String sql=	
					"SELECT * "+
					"FROM CLIENT "+
					"WHERE numeropassport_client='"+NumPasseport+"'";
			Statement statement = conn.createStatement();
	    	ResultSet rs=statement.executeQuery(sql);
	    	if(rs.next()) {
	    		idClient=rs.getInt("id_client");
	    	}
		}catch(Exception e) {
			e.printStackTrace();
		}
	
		return idClient;
	}
		
	public static void affichageCommandeClient(ResultSet rs) {
		
		try {
				String affichage="";
				String dateD=rs.getString("date_vol");
				String HeureD=rs.getString("horaire_vol");
				String Duree=rs.getString("duree_vol");
				String horaireA=AirChance.ajoutDepart_Duree(rs.getString("date_vol"),rs.getString("horaire_vol"),rs.getString("duree_vol"));
				String[] HoraireA=horaireA.split(" ");
				String DateA=HoraireA[0];
				String HeureA=HoraireA[1];
				
				affichage=	"N° Reservation : "+ rs.getString("id_reservation") 	+ "\n" +
							"Auteur de la reservation : "+ rs.getString("nom") + " "+rs.getString("prenom") + "\n" +
							"Date de la réservation	: "+ rs.getString("date_reservation") 	+ "\n" +
							"N° Vol	: "+ rs.getInt("numero_vol") 			+ "\n" +
							"Avion numéro : "+ rs.getInt("numero_avion") 			+ "\n" +
							"Modèle :" + rs.getString("code_modele_avion") 	+ "\n" +
							"Départ : "+ rs.getString("nom_ville_dep") 		+ "\n" +
							"Prévu le : "+ dateD + "\tA : "+ HeureD + "\n" +
							"Durée de vol : "+ Duree 								+ "\n" +
							"Arrivée : "+rs.getString("nom_ville_arr") 		+ "\n" +
							"Prévu le	: "+ DateA + "\tA : "+ HeureA 						+ "\n" +
							"N° Place	: "+rs.getInt("numero_place") 			+ "\n" +
							"Prix de la place	: "+rs.getFloat("prixplace_reserve") 	+ "\n" +
							"Etat du vol	: "+rs.getString("etat_vol") 			+ "\n" +
							"------------------------------------------------------------------\n" ;
			System.out.println(affichage);
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}

	}
	
	public static void CommandeClient(Connection conn) {
		try {	
			int idClient=getIdClient(conn);
			if(idClient!=-1) {
				CallableStatement cs = conn.prepareCall("{call AFFICHAGE_COMMANDE_CLIENT}");
				ResultSet Aff = cs.executeQuery();
				while(Aff.next()) {
					if (Aff.getInt("id_client")==idClient) {
						affichageCommandeClient(Aff);
					}
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try{
			Connection conn = DriverManager.getConnection(Identifiant.getUrlDatabase(),Identifiant.getNomUtilisateur(),Identifiant.getMdp());
			CommandeClient(conn);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
