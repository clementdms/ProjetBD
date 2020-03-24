package projetBD;

import java.util.ArrayList;
import java.sql.*;

public class AirChance {

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
	
	//Vrai si [DD,dh] et [DH1,DH2] sont distincts totalement (ils ne se touchent pas)
	public static boolean partieInclusDansIntervalleDH(String DH,String dh,String DH1,String DH2) {
		return appartientIntervalleDH(DH1,DH,dh) || appartientIntervalleDH(DH2,DH,dh) || appartientIntervalleDH(DH,DH1,DH2)|| appartientIntervalleDH(dh,DH1,DH2);
	}
		
	public static boolean avionEstDisponible(int idAvion, String DateDepart, String HeureDepart, String Duree) {
		boolean boo=false;
		boolean avionTrouve=false;
		String DHArrive=ajoutDepart_Duree(DateDepart,HeureDepart,Duree);
		String DHDepart=DateDepart+" "+HeureDepart;
		String DHArriveeBD="";
		String DHDepartBD="";
		
		try {
	    	Connection conn = DriverManager.getConnection(Identifiant.getUrlDatabase(),Identifiant.getNomUtilisateur(),Identifiant.getMdp());
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
	
	public static boolean avionEstDanslaBase(int idAvion){
		boolean boo=false;
		try {
	    	Connection conn = DriverManager.getConnection(Identifiant.getUrlDatabase(),Identifiant.getNomUtilisateur(),Identifiant.getMdp());
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
	
	public static int getIdVille(String ville) {
		int idVille=-1;
		try {
	    	Connection conn = DriverManager.getConnection(Identifiant.getUrlDatabase(),Identifiant.getNomUtilisateur(),Identifiant.getMdp());
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
	
	public static boolean verifDistanceVol(float d, int id){
		float dmax=0;
		try {
			boolean boo=true;
	    	Connection conn = DriverManager.getConnection(Identifiant.getUrlDatabase(),Identifiant.getNomUtilisateur(),Identifiant.getMdp());
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
	
	public static int nouvelIDVol() {
		int max=0;
		try {
			
	    	Connection conn = DriverManager.getConnection(Identifiant.getUrlDatabase(),Identifiant.getNomUtilisateur(),Identifiant.getMdp());
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
	
	public static boolean AjoutVol(){
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
		int idVol=nouvelIDVol()+1;
		
		idVDep=getIdVille(VilleDepart);
		idVArr=getIdVille(VilleArrivee);
		if(Distance>0) {
			if(avionEstDanslaBase(idAvion)) {
				if(avionEstDisponible(idAvion,DateDep,HoraireDep,DureeVol)) {
					if(verifDistanceVol(Distance,idAvion)) {
						if(idVDep!=-1 || idVArr!=-1) {
							String sqlRequest = "INSERT INTO VOL (numero_vol,horaire_vol,date_vol,duree_vol, distance_vol, etat_vol, id_ville_provenir, id_ville_destiner, numero_avion) VALUES";
							sqlRequest+="('"+idVol+"','"+HoraireDep+"','"+DateDep+"','"+DureeVol+"','"+Distance+"','"+Etat+"','"+idVDep+"','"+idVArr+"','"+idAvion+"')";
							try{
								Connection conn = DriverManager.getConnection(Identifiant.getUrlDatabase(),Identifiant.getNomUtilisateur(),Identifiant.getMdp());
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

	public static boolean testAjoutVol(ArrayList<String> Saisi) {
		boolean aReussi=true;
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
		int idVol=nouvelIDVol();
		
		idVDep=getIdVille(VilleDepart);
		idVArr=getIdVille(VilleArrivee);
		if(Distance>0) {
			if(avionEstDanslaBase(idAvion)) {
				if(avionEstDisponible(idAvion,DateDep,HoraireDep,DureeVol)) {
					if(verifDistanceVol(Distance,idAvion)) {
						if(idVDep!=-1 && idVArr!=-1) {
							String sqlRequest = "INSERT INTO VOL (numero_vol,horaire_vol,date_vol,duree_vol, distance_vol, etat_vol, id_ville_provenir, id_ville_destiner, numero_avion) VALUES";
							sqlRequest+="('"+idVol+"','"+HoraireDep+"','"+DateDep+"','"+DureeVol+"','"+Distance+"','"+Etat+"','"+idVDep+"','"+idVArr+"','"+idAvion+"')";
							try{
								Connection conn = DriverManager.getConnection(Identifiant.getUrlDatabase(),Identifiant.getNomUtilisateur(),Identifiant.getMdp());
						    	Statement statement = conn.createStatement();
						    	statement.executeUpdate(sqlRequest);
							}catch(Exception e) {
								e.printStackTrace();
							}
						}else {
							aReussi=false;						
						}
					}else {
						aReussi=false;
					}
				}else {
					aReussi=false;
				}
			}else {
				aReussi=false;
			}
		}else {
			aReussi=false;
		}
		return aReussi;	
	}
					
}
