import java.util.ArrayList;
import java.sql.*;
	
public class GestionPlanificationVol {

		// Vrai si la date et l'heure DH est avant DH1
		public static boolean estPlusPetitDH(String DH, String DH1) {
			String[] dh= DH.split(" ");
			String[] dh1 = DH1.split(" ");
			
			boolean boo=true;
			
			String[] d=dh[0].split("-");
			String[] d1=dh1[0].split("-");
			String[] h=dh[1].split(":");
			String[] h1=dh1[1].split(":");
			if(Integer.parseInt(d[0])==Integer.parseInt(d1[0]))  {
				if(Integer.parseInt(d[1])==Integer.parseInt(d1[1])){
					if(Integer.parseInt(d[2])==Integer.parseInt(d1[2])){
						if(Integer.parseInt(h[0])==Integer.parseInt(h1[0])) {
							if(Integer.parseInt(h[1])==Integer.parseInt(h1[1])){
								if(Integer.parseInt(h[2])>Integer.parseInt(h1[2])){
									boo=false;
								}
							}else if(Integer.parseInt(h[1])>Integer.parseInt(h1[1])) {
								boo=false;
							}
						}else if(Integer.parseInt(h[0])>Integer.parseInt(h1[0])) {
							boo=false;
						}
					}else if(Integer.parseInt(d[2])>Integer.parseInt(d1[2])) {
						boo=false;
					}
				}else if(Integer.parseInt(d[1])>Integer.parseInt(d1[1])) {
					boo=false;
				}
			}else if(Integer.parseInt(d[0])>Integer.parseInt(d1[0])) {
				boo=false;
			}
			
			return boo;
		}
		
		//Vrai si DH appartient à [Dh1,DH2] autrement dit : DH1<DH et DH<DH2
		public static boolean appartientIntervalleDH(String DH, String DH1,String DH2) {
			return estPlusPetitDH(DH1,DH) && estPlusPetitDH(DH,DH2);
		}
		
		/** Vrai si [DD,dh] et [DH1,DH2] sont distincts totalement (ils ne se touchent pas) **/
		public static boolean partieInclusDansIntervalleDH(String DH,String dh,String DH1,String DH2) {
			return appartientIntervalleDH(DH1,DH,dh) || appartientIntervalleDH(DH2,DH,dh) || appartientIntervalleDH(DH,DH1,DH2)|| appartientIntervalleDH(dh,DH1,DH2);
		}
			
		public static boolean avionEstDisponible(int idAvion, String DateDepart, String HeureDepart, String Duree,Connection conn) {
			boolean boo=false;
			boolean avionTrouve=false;
			String DHArrive=ajoutDepart_Duree(DateDepart,HeureDepart,Duree);
			String DHDepart=DateDepart+" "+HeureDepart;
			String DHArriveeBD="";
			String DHDepartBD="";
			
			try {
		    	Statement statement = conn.createStatement();
		    	ResultSet rs=statement.executeQuery("select * from VOL");
		    	while(!boo && rs.next()) {
		    		if(rs.getInt("numero_avion")==idAvion) {
		    			avionTrouve=true;
		    			DHArriveeBD=ajoutDepart_Duree(rs.getString("date_vol"),rs.getString("horaire_vol"),rs.getString("duree_vol"));
		    			DHDepartBD=rs.getString("date_vol")+" "+rs.getString("duree_vol");
		    			if(!partieInclusDansIntervalleDH(DHDepart,DHArrive,DHDepartBD,DHArriveeBD)) {
		    				boo=true;
		    			}
		    		}
		    	}
		    	if(!avionTrouve) {
		    		boo=true;
		    	}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			return boo;
		}
		
		public static boolean avionEstDanslaBase(int idAvion,Connection conn){
			boolean boo=false;
			try {
		    	Statement statement = conn.createStatement();
		    	ResultSet rs=statement.executeQuery("select * from AVION");
		    	while(rs.next()) {
		    		if(rs.getInt("numero_avion")==idAvion) {
		    			boo=true;
		    		}
		    	}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			return boo;
		}
		
		public static String ajoutDepart_Duree(String DateD,String HeureD,String Duree) {

			String[] dateD=DateD.split("-");
			String[] heureD=HeureD.split(":");
			String[] duree=Duree.split(":");
			int s=Integer.parseInt(heureD[2])+Integer.parseInt(duree[2]);
			int m=s/60;
			s%=60;
			m+=Integer.parseInt(heureD[1])+Integer.parseInt(duree[1]);
			int h=m/60;
			m%=60;
			h+=Integer.parseInt(heureD[0])+Integer.parseInt(duree[0]);
			int j=h/24;
			h%=24;
			j+=Integer.parseInt(dateD[2]);
			int mois=Integer.parseInt(dateD[1]);
			int a=Integer.parseInt(dateD[0]);		
			while(	(mois==2 && ((a%4==0 && j>29)||(a%4!=0 && j>28))) || ( (mois==4||mois==6||mois==9||mois==11) && (j>30) ) || ((mois==1||mois==3||mois==5||mois==7||mois==8||mois==10||mois==12)&& (j>31))) {
				if(mois==2) {
					if(a%4==0) {
						j-=29;
					}else {
						j-=28;
					}
				}else if(mois==4||mois==6||mois==9||mois==11) {
					j-=30;
				}else {
					j-=31;
				}
				mois+=1;
				a+=mois/12;
				if(mois==13) {
					mois=1;
				}
				
			}
			return a+"-"+mois+"-"+j+" "+h+":"+m+":"+s;
		}
		
		public static ArrayList<String> InterfaceAjoutVol(){
			ArrayList<String> Exigences = new ArrayList<String>();
			Exigences.add(LectureClavier.lireHeure("----------------- Horaire de départ 	-----------------",true));
			Exigences.add(LectureClavier.lireDate("----------------- Date de départ 		-----------------"));
			Exigences.add(LectureClavier.lireHeure("----------------- Durée de vol 		-----------------",false));
			Exigences.add(""+LectureClavier.lireFloat("----------------- Distance de vol   	-----------------"));
			Exigences.add(LectureClavier.lireEtat("----------------- Etat du vol 		-----------------"));
			Exigences.add(LectureClavier.lireChaine("----------------- Ville de départ 	-----------------"));
			Exigences.add(LectureClavier.lireChaine("----------------- Ville d'arrivée 	-----------------"));
			Exigences.add(""+LectureClavier.lireEntier("----------------- Numéro de l'avion  	-----------------"));
			return Exigences;
		}
		
		public static int getIdVille(String ville, Connection conn) {
			int idVille=-1;
			try {
		    	Statement statement = conn.createStatement();
		    	ResultSet rs=statement.executeQuery("select * from VILLE");// On va verifier que les ville saisi sont bonnes et récuper leurs iD
		    	boolean boo=true;
		    	while((boo)&&rs.next()) {
		    		if(rs.getString("nom_ville").equals(ville)) {
		    			idVille=rs.getInt("id_ville");
		    			boo=false;
		    		}
		    	}
			}
			catch(Exception e) {
				e.printStackTrace();
				System.exit(-1);
			}
			return idVille;		
		}
		
		public static boolean verifDistanceVol(float d, int id, Connection conn){
			float dmax=0;
			try {
				boolean boo=true;
		    	Statement statement = conn.createStatement();
		    	ResultSet rsA=statement.executeQuery("select * from AVION");// On va verifier que les ville saisi sont bonnes et récuper leurs iD
		    	String modele="";
		    	while((boo)&&rsA.next()) {
		    		if(rsA.getInt("numero_avion")==id) {
		    			modele=rsA.getString("code_modele_avion");
		    			boo=false;
		    		}
		    	}
		    	boo=true;
		    	ResultSet rsMA=statement.executeQuery("select * from MODELE_AVION");
		    	while((boo)&&rsMA.next()) {
		    		if(rsMA.getString("code_modele_avion").equals(modele)) {
		    			dmax=rsMA.getFloat("distancemax_avion");
		    			boo=false;
		    		}
		    	}
			}
			catch(Exception e) {
				e.printStackTrace();
				System.exit(-1);
			}	
			return (dmax>=d);
		}
		
		public static int nouvelIDVol(Connection conn) {
			int max=0;
			try {
		    	Statement statement = conn.createStatement();
		    	ResultSet rs=statement.executeQuery("select * from VOL");
		    	while(rs.next()) {
		    		if(max<rs.getInt("numero_vol")) {
		    			max=rs.getInt("numero_vol");
		    		}
		    	}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			return max+1;
		}
		
		
		//Requete 1
		public static boolean AjoutVol(Connection conn){
			boolean aReussi=true;
			ArrayList<String> Saisi=InterfaceAjoutVol();
			String HoraireDep = Saisi.get(0);
			String DateDep = Saisi.get(1);
			String DureeVol=Saisi.get(2);
			Float Distance=Float.parseFloat(Saisi.get(3));
			String Etat=Saisi.get(4);
			String VilleDepart=Saisi.get(5);
			String VilleArrivee=Saisi.get(6);
			int idAvion=Integer.parseInt(Saisi.get(7));
			int idVDep=-1;
			int idVArr=-1;
			int idVol=nouvelIDVol(conn)+1;
			
			idVDep=getIdVille(VilleDepart,conn);
			idVArr=getIdVille(VilleArrivee,conn);
			if(Distance>0) {
				if(avionEstDanslaBase(idAvion,conn)) {
					if(avionEstDisponible(idAvion,DateDep,HoraireDep,DureeVol,conn)) {
						if(verifDistanceVol(Distance,idAvion,conn)) {
							if(idVDep!=-1 || idVArr!=-1) {
								String sqlRequest = "INSERT INTO VOL (numero_vol,horaire_vol,date_vol,duree_vol, distance_vol, etat_vol, id_ville_provenir, id_ville_destiner, numero_avion) VALUES";
								sqlRequest+="('"+idVol+"','"+HoraireDep+"','"+DateDep+"','"+DureeVol+"','"+Distance+"','"+Etat+"','"+idVDep+"','"+idVArr+"','"+idAvion+"')";
								try{
							    	Statement statement = conn.createStatement();
							    	statement.executeUpdate(sqlRequest);
								}catch(Exception e) {
									e.printStackTrace();
								}
							}else {
								aReussi=false;
								System.out.println("Erreur sur la saisi nous n'avons pas pu créer le vol :");
								if(idVDep==-1) {
									System.out.println("La ville de départ "+ VilleDepart +" n'est pas renseigné dans la base de donnée");
								}
								if(idVArr==-1) {
									System.out.println("La ville d'arrivee "+ VilleArrivee +" n'est pas renseigné dans la base de donnée");
								}						
							}
						}else {
							aReussi=false;
							System.out.println("Erreur sur la saisi nous n'avons pas pu créer le vol :");
							System.out.println("L'avion numero : "+idAvion+" ne permet pas de parcourir " + Distance + "km");
						}
					}else {
						aReussi=false;
						System.out.println("Erreur sur la saisi nous n'avons pas pu créer le vol :");
						System.out.println("L'avion numero : "+idAvion+" n'est pas disponible pour les dates indiquees");
					}
				}else {
					aReussi=false;
					System.out.println("Erreur sur la saisi nous n'avons pas pu créer le vol :");
					System.out.println("L'avion numero : "+idAvion+" n'est pas enregistrer dans la base de donnée");
				}
			}else {
				aReussi=false;
				System.out.println("Erreur sur la saisi nous n'avons pas pu créer le vol :");
				System.out.println("Aucun avion ne peut parcourir de distance inférieur ou égal à 0km");
			}
			return aReussi;	
		}

		//Requete 2
		public static void GestionVol(Connection conn) {
			try 
			{
		    	int choix = 0;
		    	do
			    {
		    	
			    	// Traitemant : Modification de la planification d'un vol existant
			    	Statement requete1 ; 
			    	requete1 = conn.createStatement() ; // Creation d'un descripteur de requete
			    	
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
	    			int numV = LectureClavier.lireEntier("0 pour Quitter") ;
	    			
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
			    			Statement requete2 = conn.createStatement() ;
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
			    			Statement requete3 = conn.createStatement() ; 
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
			    	    	Statement requete4 = conn.createStatement() ;
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
			    			
			    			Statement requete5 = conn.createStatement() ;
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
				    			id = LectureClavier.lireEntier("\nSaisir le numero du pilote : ");
				    			
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
				    			id = LectureClavier.lireEntier("\\nSaisir le numero de l'hotesse : ");
				    			
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
			    			Statement requete6 = conn.createStatement();
			    			int nb3 ;
			    			String change = "" ;
			    			
			    			switch (choix3)
			    			{
			    				case 1:
			    					change = LectureClavier.lireChaine("Saisir les nouveaux horaires : ") ;
			    					
			    					nb3 = requete6.executeUpdate
			    							(
			    									"UPDATE vol "
			    									+ "SET horaire_vol = '" + change + "'" 
			    									+ " WHERE numero_vol = " + numV 
	    									);
			    					
			    					System.out.println("Nombre de lignes mise a jour : " + nb3);
			    					
			    					break;
			    					
			    				case 2:
			    					change = LectureClavier.lireChaine("Saisir la nouvelle date : ") ;
			    					
			    					nb3 = requete6.executeUpdate
			    							(
			    									"UPDATE vol "
			    									+ "SET date_vol = '" + change + "'" 
			    									+ " WHERE numero_vol = " + numV 
	    									);
			    					
			    					System.out.println("Nombre de lignes mise a jour : " + nb3);
			    					break;
			    					
			    				case 3:
			    					change = LectureClavier.lireChaine("Saisir la nouvelle duree : ") ;
			    					
			    					nb3 = requete6.executeUpdate
			    							(
			    									"UPDATE vol "
			    									+ "SET duree_vol = '" + change + "'" 
			    									+ " WHERE numero_vol = " + numV 
	    									);
			    					
			    					System.out.println("Nombre de lignes mise a jour : " + nb3);
			    					
			    					break;
			    					
			    				case 4:
			    					double dist = LectureClavier.lireDouble("Saisir la nouvelle distance : ") ;
			    					
			    					nb3 = requete6.executeUpdate
			    							(
			    									"UPDATE vol "
			    									+ "SET distance_vol = " + dist  
			    									+ " WHERE numero_vol = " + numV 
	    									);
			    					
			    					System.out.println("Nombre de lignes mise a jour : " + nb3);
			    					
			    					break;
			    					
			    				case 5:
			    					change = LectureClavier.lireChaine("Saisir le nouvel etat : ") ;
			    					
			    					nb3 = requete6.executeUpdate
			    							(
			    									"UPDATE vol "
			    									+ "SET etat_vol = '" + change + "'" 
			    									+ " WHERE numero_vol = " + numV 
	    									);
			    					
			    					System.out.println("Nombre de lignes mise a jour : " + nb3);
			    					
			    					break;
			    					
			    				case 6:
			    					int id_ville = LectureClavier.lireEntier("Saisir le nouvel id de la ville de depart : ");
			    					
			    					nb3 = requete6.executeUpdate
			    							(
			    									"UPDATE vol "
			    									+ "SET id_ville_provenir = " + id_ville 
			    									+ " WHERE numero_vol = " + numV 
	    									);
			    					
			    					System.out.println("Nombre de lignes mise a jour : " + nb3);
			    					
			    					break;
			    					
			    				case 7:
			    					id_ville = LectureClavier.lireEntier("Saisir le nouvel id de la ville de destination : ");
			    					
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
		    	
		    	conn.close();
		    	
	    	}	catch(Exception e) 
				{
		    		e.printStackTrace();
		    	}

		}

		//Requete 3
		public static void SupprimerVol(Connection conn) {
			try {
				Statement statement = conn.createStatement() ;
				System.out.println("*------------------------------*");
				System.out.println("Voici tout les vols qui n'ont pas encore décollé");
				ResultSet resultSet = statement.executeQuery("SELECT numero_vol, horaire_vol, date_vol, duree_vol, V1.nom_ville, V2.nom_ville\r\n" + 
																"FROM VOL\r\n" + 
																"INNER JOIN VILLE V1 ON V1.id_ville = VOL.id_ville_provenir\r\n" + 
																"INNER JOIN VILLE V2 ON V2.id_ville = VOL.id_ville_destiner\r\n" + 
																"WHERE etat_vol = 'att'") ;
				
				System.out.println("| Numero du Vol | Horaire du Vol | Date du Vol | Durée du Vol | Ville de départ | Ville de destination |");
				ArrayList<Integer> lesVols = new ArrayList<Integer>();
				while (resultSet.next()) {
					//Récuperation des données de la BDD
			        int numeroVol = resultSet.getInt("numero_vol");
			        String horaireVol = resultSet.getString("horaire_vol");
			        String dateVol = resultSet.getString("date_vol");
			        String dureeVol = resultSet.getString("duree_vol");
			        String villeDepart = resultSet.getString("V1.nom_ville");
			        String villeDestination = resultSet.getString("V2.nom_ville");
			        //Affichage des vols
			        System.out.println("| " + numeroVol + " | " + horaireVol + " | " + dateVol + " | " + dureeVol + " | " + villeDepart + " | " + villeDestination + " |");
			        
			        lesVols.add(numeroVol);
				}
				int choixSupprime = 0;
				while(!lesVols.contains(choixSupprime))
				{
					System.out.println("*------------------------------*");
	    			System.out.println("     Quel vol est supprimé ? ");
	    			System.out.println("*------------------------------*");
	    			choixSupprime = LectureClavier.lireEntier("\t-");
				}
				int i = statement.executeUpdate("UPDATE VOL "
												+ "SET etat_vol = 'suppr'"
												+ "WHERE numero_vol = " + choixSupprime);
				
				if(i != 0) {
					System.out.println("Le vol est bien supprimé");
					System.out.println("Les clients sur ce vol vont etre automatique déplacé é des vols futurs");
					//Requete que permet de recuperé les clients à deplacer
					resultSet = statement.executeQuery("SELECT CLIENT.id_client\r\n" + 
				    	        						"FROM CLIENT\r\n" + 
				    	        						"INNER JOIN RESERVATION ON RESERVATION.id_client = CLIENT.id_client\r\n" + 
				    	        						"INNER JOIN RESERVE ON RESERVE.id_reservation = RESERVATION.id_reservation\r\n" + 
				    	        						"WHERE RESERVE.numero_vol = '" + choixSupprime+"'");
					
					while (resultSet.next()) {
						int idClient = resultSet.getInt("id_client");	    	        					
						//Création d'un nouveau Statement
						Statement statement2 = conn.createStatement();
						//Requete qui recupére  les reservations du client et sa place
						ResultSet resultSet2 = statement2.executeQuery("SELECT RESERVE.id_reservation, RESERVE.numero_place, PLACE.id_classe\r\n" + 
								"							FROM RESERVE\r\n" + 
								"							INNER JOIN RESERVATION ON RESERVE.id_reservation = RESERVATION.id_reservation\r\n" +
								"                           INNER JOIN PLACE ON PLACE.numero_place = RESERVE.numero_place\r\n"+
								"							WHERE RESERVE.numero_vol = "+ choixSupprime +"\r\n" + 
								"							AND RESERVATION.id_client = " + idClient + "");
						
						while (resultSet2.next()) {
							int idReservation = resultSet2.getInt("id_reservation");
							int numPlace = resultSet2.getInt("numero_place");
							int idClasse = resultSet2.getInt("id_classe");
							//Création d'un nouveau Statement
							Statement statement3 = conn.createStatement();
							
							//Procédure/Fonction
							ResultSet resultSet3 = statement3.executeQuery("SELECT deplaceClient(" + choixSupprime + ", " + numPlace + ", " + numPlace + ", " + idClasse + ", " + idReservation + ")");
	    					resultSet3.next();
	    					statement3.close();
						}
						statement2.close();
					}
				}
				else {
					System.out.println("Erreur dans la suppression du vol numero : " + choixSupprime);
				}
			
			}
			catch (Exception e) {
				System.out.println("BUG : " + e);
			}
		}
		
		//Requete 4
		
		//Requete 5 
		public static void Ajout_Suppr_Personnel(Connection conn) {
			try {    	
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
				    	int idP = LectureClavier.lireEntier("Identifiant pilote: ") ;
				    	String nomP = LectureClavier.lireChaine("Nom pilote: ") ;
				    	String prenomP = LectureClavier.lireChaine("Prenom pilote : ") ;
				    	int numRueP = LectureClavier.lireEntier("Numero de la rue : ") ;
				    	String rueP = LectureClavier.lireChaine("Rue : ") ;
				    	int codePostalP = LectureClavier.lireEntier("Code postal : ") ;
				    	String villeP = LectureClavier.lireChaine("Ville pilote : ") ;
				    	String paysP = LectureClavier.lireChaine("Pays pilote : ") ;
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
				    	int idPSupp = LectureClavier.lireEntier("Choisir l'identifiant du pilote a supprimer: ") ;
				    	
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
				    	int idH = LectureClavier.lireEntier("Identifiant hotesse: ") ;
				    	String nomH = LectureClavier.lireChaine("Nom hotesse: ") ;
				    	String prenomH = LectureClavier.lireChaine("Prenom hotesse : ") ;
				    	int numRueH = LectureClavier.lireEntier("\"Numero de la rue : \"") ;
				    	String rueH = LectureClavier.lireChaine("Rue : ") ;
				    	int codePostalH = LectureClavier.lireEntier("Code postal : ") ;
				    	String villeH = LectureClavier.lireChaine("Ville hotesse : ") ;
				    	String paysH = LectureClavier.lireChaine("Pays hotesse : ") ;
				    	
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
				    	int idHSupp = LectureClavier.lireEntier("Choisir l'identifiant de l'hotesse a supprimer: ") ;
				    	
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
