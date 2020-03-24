package projetBD;

import java.util.ArrayList;

public class TestAjoutVol {

	
	public static void main(String[] args) {

		
		// TEST de la fonction estPlusPetitDH
		
//		ArrayList<String> DH = new ArrayList<String>();
//		DH.add("2020-1-1 12:30:0");
//		DH.add("2020-1-1 12:30:1");
//		DH.add("2020-1-1 12:31:0");
//		DH.add("2020-1-1 13:30:0");
//		DH.add("2020-1-2 12:30:0");
//		DH.add("2020-2-1 12:30:0");
//		DH.add("2021-1-1 12:30:0");
//		
//		int compteurMauvais=0;
//		int compteurBon=0;
//		int i=1;
//		for(String s:DH) {
//			if(AirChance.estPlusPetitDH(s,"2020-1-1 12:30:0")) {
//				compteurMauvais+=1;
//				System.out.println("Le mauvais test : "+i+" est pas bon");
//			}
//			if(!AirChance.estPlusPetitDH("2020-1-1 12:30:0",s)) {
//				compteurBon+=1;
//				System.out.println("Le bon test : "+i+" est pas bon");
//			}
//			i++;
//		}
//		System.out.println(((float)(i-compteurMauvais)/(float)(i))*100 +"% des mauavis tests qui ne devait pas passés ne sont passés...");
//		System.out.println(((float)(i-compteurBon)/(float)(i))*100 +"% des bons tests qui ne devait pas passés ne sont passés...");
//		
		
		
//	}
//		

		// TEST de la fonction testAjoutVol
		
		ArrayList< ArrayList<String>> NeDoisPasEtreBon = new ArrayList< ArrayList<String>>();
		System.out.println("Début des tests");
		
		ArrayList<String> pasBonneDistance = new ArrayList<String>(); 
		pasBonneDistance.add("0:0:0");//HeureDepart
		pasBonneDistance.add("2020-1-1");//DateDepart
		pasBonneDistance.add("0:0:0");//Duree
		pasBonneDistance.add("0");//Distance
		pasBonneDistance.add("att");//Etat
		pasBonneDistance.add("A");//VDep
		pasBonneDistance.add("A");//Varr
		pasBonneDistance.add("1");//Numéro avion
		NeDoisPasEtreBon.add(pasBonneDistance);
		
		ArrayList<String> pasBonneDistance2 = new ArrayList<String>(); 
		pasBonneDistance2.add("0:0:0");//HeureDepart
		pasBonneDistance2.add("2020-1-1");//DateDepart
		pasBonneDistance2.add("0:0:0");//Duree
		pasBonneDistance2.add("-15");//Distance
		pasBonneDistance2.add("att");//Etat
		pasBonneDistance2.add("A");//VDep
		pasBonneDistance2.add("A");//Varr
		pasBonneDistance2.add("1");//Numéro avion		
		NeDoisPasEtreBon.add(pasBonneDistance2);
		
		ArrayList<String> avionPasDansBase = new ArrayList<String>(); 
		avionPasDansBase.add("0:0:0");//HeureDepart
		avionPasDansBase.add("2020-1-1");//DateDepart
		avionPasDansBase.add("0:0:0");//Duree
		avionPasDansBase.add("1");//Distance
		avionPasDansBase.add("att");
		avionPasDansBase.add("A");
		avionPasDansBase.add("A");
		avionPasDansBase.add("0");
		NeDoisPasEtreBon.add(avionPasDansBase);
		
		ArrayList<String> avionPasDansBase2 = new ArrayList<String>(); 
		avionPasDansBase2.add("0:0:0");//HeureDepart
		avionPasDansBase2.add("2020-1-1");//DateDepart
		avionPasDansBase2.add("0:0:0");//Duree
		avionPasDansBase2.add("1");//Distance
		avionPasDansBase2.add("att");
		avionPasDansBase2.add("A");
		avionPasDansBase2.add("A");
		avionPasDansBase2.add("1511");
		NeDoisPasEtreBon.add(avionPasDansBase2);
		
		ArrayList<String> avionPasDispo = new ArrayList<String>(); 
		avionPasDispo.add("19:0:0");//HeureDepart
		avionPasDispo.add("2020-02-8");
		avionPasDispo.add("72:0:0");
		avionPasDispo.add("1");//Distance
		avionPasDispo.add("att");
		avionPasDispo.add("A");
		avionPasDispo.add("A");
		avionPasDispo.add("1");
		NeDoisPasEtreBon.add(avionPasDispo);
		
		ArrayList<String> avionPasDispo1 = new ArrayList<String>(); 
		avionPasDispo1.add("19:0:0");//HeureDepart
		avionPasDispo1.add("2020-02-10");
		avionPasDispo1.add("0:0:30");
		avionPasDispo1.add("1");//Distance
		avionPasDispo1.add("att");
		avionPasDispo1.add("A");
		avionPasDispo1.add("A");
		avionPasDispo1.add("1");
		NeDoisPasEtreBon.add(avionPasDispo1);
		
		ArrayList<String> avionPasDispo2 = new ArrayList<String>(); 
		avionPasDispo2.add("19:0:0");//HeureDepart
		avionPasDispo2.add("2020-02-10");//DateDepart
		avionPasDispo2.add("72:0:0");//Duree
		avionPasDispo2.add("1");//Distance
		avionPasDispo2.add("att");//Etat
		avionPasDispo2.add("A");//VDep
		avionPasDispo2.add("A");//Varr
		avionPasDispo2.add("1");//Numéro avion
		NeDoisPasEtreBon.add(avionPasDispo2);
		
		ArrayList<String> pasBonneDistanceAvion = new ArrayList<String>(); 
		pasBonneDistanceAvion.add("19:0:0");//HeureDepart
		pasBonneDistanceAvion.add("2020-02-10");//DateDepart
		pasBonneDistanceAvion.add("0:0:30");//Duree
		pasBonneDistanceAvion.add("35.1");//Distance
		pasBonneDistanceAvion.add("att");//Etat
		pasBonneDistanceAvion.add("A");//VDep
		pasBonneDistanceAvion.add("A");//Varr
		pasBonneDistanceAvion.add("8");//Numéro avion
		NeDoisPasEtreBon.add(pasBonneDistanceAvion);
		
		ArrayList<String> pasBonneDistanceAvion1 = new ArrayList<String>(); 
		pasBonneDistanceAvion1.add("19:0:0");//HeureDepart
		pasBonneDistanceAvion1.add("2020-02-10");//DateDepart
		pasBonneDistanceAvion1.add("0:0:30");//Duree
		pasBonneDistanceAvion1.add("500.1");//Distance
		pasBonneDistanceAvion1.add("att");//Etat
		pasBonneDistanceAvion1.add("A");//VDep
		pasBonneDistanceAvion1.add("A");//Varr
		pasBonneDistanceAvion1.add("4");//Numéro avion
		NeDoisPasEtreBon.add(pasBonneDistanceAvion1);
		
		ArrayList<String> pasBonneDistanceAvion2 = new ArrayList<String>(); 
		pasBonneDistanceAvion2.add("19:0:0");//HeureDepart
		pasBonneDistanceAvion2.add("2020-02-10");//DateDepart
		pasBonneDistanceAvion2.add("0:0:30");//Duree
		pasBonneDistanceAvion2.add("35.1");//Distance
		pasBonneDistanceAvion2.add("att");//Etat
		pasBonneDistanceAvion2.add("A");//VDep
		pasBonneDistanceAvion2.add("A");//Varr
		pasBonneDistanceAvion2.add("2");//Numéro avion
		NeDoisPasEtreBon.add(pasBonneDistanceAvion2);
		
		ArrayList<String> vDepPasBonne = new ArrayList<String>(); 
		vDepPasBonne.add("19:0:0");//HeureDepart
		vDepPasBonne.add("2020-02-10");//DateDepart
		vDepPasBonne.add("0:0:30");//Duree
		vDepPasBonne.add("500");//Distance
		vDepPasBonne.add("att");//Etat
		vDepPasBonne.add("A");//VDep
		vDepPasBonne.add("Grenoble");//Varr
		vDepPasBonne.add("4");//Numéro avion
		NeDoisPasEtreBon.add(vDepPasBonne);
		
		ArrayList<String> vArrPasBonne = new ArrayList<String>(); 
		vArrPasBonne.add("19:0:0");//HeureDepart
		vArrPasBonne.add("2020-02-10");//DateDepart
		vArrPasBonne.add("0:0:30");//Duree
		vArrPasBonne.add("500");//Distance
		vArrPasBonne.add("att");//Etat
		vArrPasBonne.add("Grenoble");//VDep
		vArrPasBonne.add("A");//Varr
		vArrPasBonne.add("4");//Numéro avion
		NeDoisPasEtreBon.add(vArrPasBonne);
		
		int i=1,compteur=0;;
		for(ArrayList<String> s : NeDoisPasEtreBon) {
			if(AirChance.testAjoutVol(s)) {
				System.out.println("Le test : " + i +"n'est pas passée..");
				compteur++;
			}
			i++;
		}
		System.out.println(((float)(i-compteur)/(float)(i))*100 +"% des tests qui ne devait pas passés ne sont passés...");
		
		ArrayList<String> DoitPasse = new ArrayList<String>(); 
		DoitPasse.add("19:0:0");//HeureDepart
		DoitPasse.add("2020-02-1");//DateDepart
		DoitPasse.add("0:29:0");//Duree
		DoitPasse.add("400");//Distance
		DoitPasse.add("att");//Etat
		DoitPasse.add("Grenoble");//VDep
		DoitPasse.add("Paris");//Varr
		DoitPasse.add("5");//Numéro avion
		if(!AirChance.testAjoutVol(DoitPasse)){
			System.out.println("Aie... C'était censé marché ca par contre...");
		}
		System.out.println("Fin des tests");
		
	}

}
