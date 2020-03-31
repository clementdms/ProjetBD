import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class GestionReservation {
	
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
				String horaireA=GestionPlanificationVol.ajoutDepart_Duree(rs.getString("date_vol"),rs.getString("horaire_vol"),rs.getString("duree_vol"));
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

	//Requete 6
	public static void AfficheCommandeClient(Connection conn) {
		try{
			CommandeClient(conn);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	//Requete 7
	public static void ReserveVol(Connection conn) {
		try {
			Statement statement = conn.createStatement() ;
			int choixVilleDepart = -1;
			ArrayList<Integer> listeVille = new ArrayList<Integer>();
			while(!listeVille.contains(choixVilleDepart)) {
				listeVille.clear();
				System.out.println("*------------------------------*");
				System.out.println("\tD'oé partez vous ?");
				ResultSet resultSet = statement.executeQuery("SELECT VILLE.id_ville, VILLE.nom_ville\r\n"
						+ "									FROM VILLE\r\n"
						+ "									INNER JOIN VOL ON VILLE.id_ville = VOL.id_ville_provenir\r\n");
				
				while (resultSet.next()) {
					int idVille = resultSet.getInt("id_ville");
					String nomVille = resultSet.getString("nom_ville");
					
					listeVille.add(idVille);
					System.out.println("\t" +idVille + " - "+ nomVille);
				}
		        System.out.println("*------------------------------*");
		        choixVilleDepart = LectureClavier.lireEntier("\t-");
			}
			
			int choixVilleArrive = -1;
			while(!listeVille.contains(choixVilleDepart)) {
				listeVille.clear();
				System.out.println("*------------------------------*");
				System.out.println("\tOé voulez vous aller ?");
				ResultSet resultSet = statement.executeQuery("SELECT VILLE.id_ville, VILLE.nom_ville\r\n"
						+ "									FROM VILLE\r\n"
						+ "									INNER JOIN VOL ON VILLE.id_ville = VOL.id_ville_destiner"
						+ "									WHERE VOL.id_ville_provenir = " + choixVilleDepart + "\r\n");
				while (resultSet.next()) {
					int idVille = resultSet.getInt("id_ville");
					String nomVille = resultSet.getString("nom_ville");
					
					listeVille.add(idVille);
					System.out.println("\t" +idVille + " - "+ nomVille);
				}
		        System.out.println("*------------------------------*");
		        choixVilleArrive = LectureClavier.lireEntier("\t-");
			}
			
			int choixClasse = -1;
			ArrayList<Integer> listeClasse = new ArrayList<Integer>();
			while(!listeClasse.contains(choixClasse)) {
				listeClasse.clear();
				System.out.println("*------------------------------*");
				System.out.println("\tEn quel classe souhaitez vous voyager ?");
				ResultSet resultSet = statement.executeQuery("SELECT id_classe, nom_classe \r\n"
						+ "										FROM CLASSE \r\n"
						+ "										INNER JOIN PLACE ON CLASSE.id_classe = PLACE.id_classe\r\n"
						+ "										INNER JOIN AVION ON AVION.numero_avion = AVION.numero_avion\r\n"
						+"                                      INNER JOIN VOL ON VOL.numero_avion = AVION.numero_avion"
						+ "										WHERE id_ville_provenir = " + choixVilleDepart + ""
						+ "										AND id_ville_destiner = " + choixVilleArrive + "");
				while (resultSet.next()) {
					int idClasse = resultSet.getInt("id_ville");
					String nomClasse = resultSet.getString("nom_ville");
					
					listeClasse.add(idClasse);
					System.out.println("\t" +idClasse + " - "+ nomClasse);
				}
		        System.out.println("*------------------------------*");
		        choixVilleArrive = LectureClavier.lireEntier("\t-");
			}
			int choixVol =-1;
			ArrayList<Integer> listeVol= new ArrayList<Integer>();
			while(!listeVol.contains(choixVol)) {
				listeVol.clear();
				System.out.println("*------------------------------*");
				System.out.println("\tQuel vol choisissez vous ?");
				ResultSet resultSet = statement.executeQuery("SELECT TMP.numero_vol, TMP.date_vol, TMP.horaire_vol, TMP.duree_vol, TMP.ville_depart, TMP.ville_arrive, TMP.nb_place_max- COALESCE(TMP2.nb_place,0) AS place_restante\r\n" + 
								        					"FROM (SELECT VOL.numero_vol, VOL.date_vol, VOL.horaire_vol, VOL.duree_vol, VP.nom_ville AS ville_depart , VD.nom_ville AS ville_arrive, COUNT(PLACE.numero_place) AS nb_place_max\r\n" + 
								        					"		FROM VOL\r\n" + 
								        					"		INNER JOIN AVION ON AVION.numero_avion = VOL.numero_avion\r\n" + 
								        					"		INNER JOIN PLACE ON PLACE.numero_avion = AVION.numero_avion\r\n" + 
								        					"		INNER JOIN VILLE VP ON VP.id_ville = VOL.id_ville_provenir\r\n" + 
								        					"		INNER JOIN VILLE VD ON VD.id_ville = VOL.id_ville_destiner\r\n" + 
								        					"		WHERE VOL.id_ville_destiner = " + choixVilleDepart + "\r\n" + 
								        					"		AND VOL.id_ville_provenir = " + choixVilleArrive + "\r\n" + 
								        					"		AND ((VOL.date_vol > NOW())\r\n" + 
								        					"				OR (VOL.horaire_vol >= NOW())\r\n" + 
								        					"					AND VOL.date_vol = NOW())\r\n" + 
								        					"		AND VOL.etat_vol = 'att'\r\n" + 
								        					"		AND PLACE.id_classe = " + choixClasse + "\r\n" + 
								        					"		GROUP BY numero_vol) TMP\r\n" + 
								        					"LEFT OUTER JOIN (SELECT VOL.numero_vol, COUNT(PLACE.numero_place) AS nb_place\r\n" + 
								        					"					FROM VOL\r\n" + 
								        					"					INNER JOIN RESERVE ON RESERVE.numero_vol = VOL.numero_vol\r\n" + 
								        					"					INNER JOIN PLACE ON PLACE.numero_place = RESERVE.numero_place AND VOL.numero_avion = PLACE.numero_avion\r\n" + 
								        					"					WHERE PLACE.id_classe = " + choixClasse + "\r\n" + 
								        					"					) TMP2 ON TMP2.numero_vol = TMP.numero_vol");
				while (resultSet.next()) {
					int numVol = resultSet.getInt("numero_vol");
					Date dateVol = resultSet.getDate("date_vol");
					Time horaireVol = resultSet.getTime("horaire_vol");
					Time dureeVol = resultSet.getTime("duree_vol");
					String villeDepart = resultSet.getString("ville_depart");
					String villeArrive = resultSet.getString("ville_arrive");
					int placeRestante = resultSet.getInt("place_restante");
					
					listeVol.add(numVol);
					if(placeRestante > 0) {
						System.out.println("\t" +numVol + " - "+ dateVol + " - " + horaireVol + " - " + dureeVol + " - " + villeDepart + " - " + villeArrive + " - Places restantes : " + placeRestante);
					}
				}
		        System.out.println("*------------------------------*");
		        System.out.print("\t-");
		        choixVol = LectureClavier.lireEntier("\t-");
			}
			
			int choixNbReserve = -1;
			int nbPlaceRestante = -2;
			while(choixNbReserve < nbPlaceRestante && choixNbReserve > 0) {
				System.out.println("*------------------------------*");
				System.out.println("\tPour combien reservé vous ?");
				ResultSet resultSet = statement.executeQuery("SELECT TMP.nb_place_max- COALESCE(TMP2.nb_place,0) AS place_restante\r\n" + 
							        					"FROM (SELECT VOL.numero_vol, VOL.date_vol, VOL.horaire_vol, VOL.duree_vol, VP.nom_ville AS ville_depart , VD.nom_ville AS ville_arrive, COUNT(PLACE.numero_place) AS nb_place_max\r\n" + 
							        					"		FROM VOL\r\n" + 
							        					"		INNER JOIN AVION ON AVION.numero_avion = VOL.numero_avion\r\n" + 
							        					"		INNER JOIN PLACE ON PLACE.numero_avion = AVION.numero_avion\r\n" + 
							        					"		INNER JOIN VILLE VP ON VP.id_ville = VOL.id_ville_provenir\r\n" + 
							        					"		INNER JOIN VILLE VD ON VD.id_ville = VOL.id_ville_destiner\r\n" + 
							        					"		WHERE VOL.id_ville_destiner = " + choixVilleDepart + "\r\n" + 
							        					"		AND VOL.id_ville_provenir = " + choixVilleArrive + "\r\n" + 
							        					"		AND ((VOL.date_vol > NOW())\r\n" + 
							        					"				OR (VOL.horaire_vol >= NOW())\r\n" + 
							        					"					AND VOL.date_vol = NOW())\r\n" + 
							        					"		AND VOL.etat_vol = 'att'\r\n" + 
							        					"		AND PLACE.id_classe = " + choixClasse + "\r\n" + 
							        					"		GROUP BY numero_vol) TMP\r\n" + 
							        					"LEFT OUTER JOIN (SELECT VOL.numero_vol, COUNT(PLACE.numero_place) AS nb_place\r\n" + 
							        					"					FROM VOL\r\n" + 
							        					"					INNER JOIN RESERVE ON RESERVE.numero_vol = VOL.numero_vol\r\n" + 
							        					"					INNER JOIN PLACE ON PLACE.numero_place = RESERVE.numero_place AND VOL.numero_avion = PLACE.numero_avion\r\n" + 
							        					"					WHERE PLACE.id_classe = " + choixClasse + "\r\n" + 
							        					"					) TMP2 ON TMP2.numero_vol = TMP.numero_vol \r\n" +
							        					"WHERE TMP.numero_vol = " + choixVol + "");
				while (resultSet.next()) {
					nbPlaceRestante = resultSet.getInt("place_restante");
				}
				System.out.println("*------------------------------*");
		        System.out.print("\t-");
		        choixNbReserve = LectureClavier.lireEntier("\t-");
			}
			boolean dejaClient;
			System.out.println("*------------------------------*");
			System.out.println("\tEtes vous déjé client ? (o/n)");
			System.out.println("*------------------------------*");
	        if(LectureClavier.lireChaine("\t-") == "o") {
	        	dejaClient = true;
	        }
	        else {
	        	dejaClient = false;
	        }
	        
	        System.out.println("*------------------------------*");
			System.out.println("\tQuel est votre nom ?");
			System.out.println("*------------------------------*");
	        String nomClient = LectureClavier.lireChaine("\t-");
	        
	        System.out.println("*------------------------------*");
			System.out.println("\tQuel est votre prénom ?");
			System.out.println("*------------------------------*");
	        String prenomClient = LectureClavier.lireChaine("\t-");
	        
	        int nbFidelite = 0;
	        boolean choixFidelite;
	        int idClient = -1;
	        if(dejaClient) {
	        	ResultSet resultSet = statement.executeQuery("SELECT id_client \r\n"+
	        												"FROM CLIENT \r\n" +
	        												"WHERE prenom_client = " + prenomClient + "\r\n"+
	        												"AND nom_client = " + nomClient + "");
	        	
	        	while (resultSet.next()) {
	        		idClient = resultSet.getInt("id_client");
	        	}
				
				System.out.println("*------------------------------*");
				System.out.println("\tVoulez vous utilier vos points de fidelité ? (o/n)");
				resultSet = statement.executeQuery("SELECT COALESCE(SUM(duree_vol),0)/50 - CLIENT.nbFideliteUtilise_client  AS point_fidelite\r\n" + 
							        						"FROM CLIENT\r\n" + 
							        						"INNER JOIN RESERVATION ON CLIENT.id_client = RESERVATION.id_client\r\n" + 
							        						"INNER JOIN RESERVE ON RESERVATION.id_reservation = RESERVE.id_reservation\r\n" + 
							        						"INNER JOIN VOL ON RESERVE.numero_vol = VOL.numero_vol\r\n" + 
							        						"WHERE CLIENT.id_client = " + idClient + "\r\n" + 
							        						"AND ((VOL.date_vol < NOW())O(VOL.date_vol = NOW() AND VOL.horaire_vol < NOW()))");
				while (resultSet.next()) {
					nbFidelite = resultSet.getInt("point_fidelite");
					System.out.println("\tVos points de fidlité : " + nbFidelite);
				}
				System.out.println("*------------------------------*");
		        if(LectureClavier.lireChaine("\t-") == "o") {
		        	choixFidelite = true;
		        }
		        else {
		        	choixFidelite = false;
		        }
	        }
	        else {
	        	choixFidelite = false;
	        	//créer compte client
	        	System.out.println("*------------------------------*");
				String numeroRueClient = LectureClavier.lireChaine("\tVotre numéro de rue : ");
				String rueClient = LectureClavier.lireChaine("\tVotre nom de rue : ");
				String cpClient = LectureClavier.lireChaine("\tVotre code postal : ");
				String villeClient = LectureClavier.lireChaine("\tVotre ville : ");
				String paysClient = LectureClavier.lireChaine("\tVotre pays : ");
				String passportClient = LectureClavier.lireChaine("\tVotre numéro de passport : ");
				System.out.println("*------------------------------*");
				ResultSet resultSet = statement.executeQuery("SELECT id_client"
						+ "									FROM CLIENT"
						+ "									ORDER BY id_client DESC"
						+ "									LIMIT 1");
				int idClientMax = -1;
				while (resultSet.next()) {
					idClientMax = resultSet.getInt("id_client");
				}
				statement.execute("INSERT INTO CLIENT (id_client, nom_client, prenom_client, numeroRue_client, rue_client, code_postal_client, ville_client, pays_client, nbFideliteUtilise_client, numeropassport_client) VALUES ('" + idClientMax + 1 + "', '" + nomClient + "' , '" + prenomClient + "', " + numeroRueClient + ", '" + rueClient + "', " + cpClient + ", '" + villeClient + "', '" + paysClient +"',0 ,'" + passportClient + "');");
				idClient = idClientMax+1;
	        }
	        
	        ResultSet resultSet = statement.executeQuery("SELECT prix_base_vol "
	        		+ "				FROM VOL"
	        		+ "				WHERE numero_vol = " + choixVol + "");
	        double prix = 0.0;
	        while(resultSet.next()){
	        	prix = resultSet.getDouble("prix_base_vol");
	        }
	        
	        //Prix selon la classe
	        if(choixClasse == 3) {
	        	prix *=15;
	        }
	        else if(choixClasse == 2) {
	        	prix *=5;
	        }
	        
	        
	        //Reduction car bientot vol
	        resultSet = statement.executeQuery("SELECT date_vol "
	        		+ "				FROM VOL"
	        		+ "				WHERE numero_vol = " + choixVol + "");
	        Date dateVol = null;
	        while(resultSet.next()){
	        	dateVol = resultSet.getDate("date_vol");
	        }
	        
	        if(new Date().compareTo(dateVol) < 30 ) {
	        	if(new Date().compareTo(dateVol) < 15){
	        		if(new Date().compareTo(dateVol) < 7){
		        		prix *= 0.80;
		        	}
	        		else {
		        		prix *= 0.90;
		        	}
	        	}
	        	else {
	        		prix *= 0.95;
	        	}
	        }
	        
	        //Augmentation car rempli
	        resultSet = statement.executeQuery("SELECT COALESCE(TMP2.nb_place,0)/ TMP.nb_place_max *100 AS taux\\r\\n\" + \r\n" + 
	        		"												        					\"FROM (SELECT VOL.numero_vol, VOL.date_vol, VOL.horaire_vol, VOL.duree_vol, COUNT(PLACE.numero_place) AS nb_place_max\\r\\n\" + \r\n" + 
	        		"												        					\"	FROM VOL\\r\\n\" + \r\n" + 
	        		"												        					\"	INNER JOIN AVION ON AVION.numero_avion = VOL.numero_avion\\r\\n\" + \r\n" + 
	        		"												        					\"	INNER JOIN PLACE ON PLACE.numero_avion = AVION.numero_avion\\r\\n\" + \r\n" + 
	        		"												        					\"	AND ((VOL.date_vol > NOW())\\r\\n\" + \r\n" + 
	        		"												        					\"	OR (VOL.horaire_vol = NOW())\\r\\n\" + \r\n" + 
	        		"												        					\"	AND VOL.date_vol = NOW())\\r\\n\" + \r\n" + 
	        		"												        					\"	AND VOL.etat_vol = 'att'\\r\\n\" + \r\n" + 
	        		"												        					\"	GROUP BY numero_vol) TMP\\r\\n\" + \r\n" + 
	        		"												        					\"	LEFT OUTER JOIN (SELECT VOL.numero_vol, COUNT(PLACE.numero_place) AS nb_place\\r\\n\" + \r\n" + 
	        		"												        					\"	FROM VOL\\r\\n\" + \r\n" + 
	        		"												        					\"	INNER JOIN RESERVE ON RESERVE.numero_vol = VOL.numero_vol\\r\\n\" + \r\n" + 
	        		"												        					\"	INNER JOIN PLACE ON PLACE.numero_place = RESERVE.numero_place AND VOL.numero_avion = PLACE.numero_avion)\\r\\n\" + \r\n" + 
	        		"												        					\"TMP2 ON TMP2.numero_vol = TMP.numero_vol \\r\\n\" + \r\n" + 
	        		"												        					\"WHERE TMP.numero_vol = " + choixVol + "");
	        double tauxRemplissage = -0;
	        while(resultSet.next()){
	        	tauxRemplissage = resultSet.getDouble("taux_remplissage");
	        }
	        
	        if(tauxRemplissage < 50.0){
	        	if(tauxRemplissage < 70.0){
	        		if(tauxRemplissage < 80.0){
	        			if(tauxRemplissage < 90.0){
	    		        	prix *= 1.5;
	    		        }
	        			else {
			        		prix *= 1.4;
			        	}
			        }
	        		else {
		        		prix *= 1.2;
		        	}
		        }
	        	else {
	        		prix *= 1.1;
	        	}
	        }
	        if(choixFidelite) {
	        	prix = prix - prix*(nbFidelite/100);
	        }
	        
	        
	        System.out.println("*------------------------------*");
	        System.out.println("\tLe prix de chaque place est de : " + prix + "");
	        System.out.println("*------------------------------*");
	        
	        resultSet = statement.executeQuery("SELECT id_reservation INTO idReservation\r\n" + 
	        		"    FROM RESERVATION\r\n" + 
	        		"    ORDER BY id_reservation DESC\r\n" + 
	        		"    LIMIT 1;");
	        int idReservation =-1;
	        while(resultSet.next()) {
	        	idReservation = resultSet.getInt("id_reservation");
	        }
	        statement.execute("INSERT INTO RESERVATION (id_reservation, date_reservation, id_client) VALUES (" + idReservation + ", NOW(), " + idClient + ");");
	        for(int i =0 ; i < choixNbReserve; i++) {
	        	statement.execute("SELECT reservePlace(" + choixClasse + ", " + idReservation + ", " + choixVol + ", '" + prix + "')");
	        }
		}
		catch (Exception e) {
			System.out.println("BUG : " + e);
		}
	}
	
	//Requete 8
	public static void SuppressionCommandeClient(Connection conn) {
		try {
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