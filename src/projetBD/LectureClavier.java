package projetBD;

/*package tplogo;*/
import java.io.*;

/**
 *
 *   Cette classe a pour role de faciliter la lecture de donnees
 *   a partir du clavier. <BR>
 *   Elle definit une methode de lecture pour les types de base
 *   les plus courramment utilises (int, float, double, boolean, String).<BR>
 *   <BR>
 *   La lecture d'une valeur au clavier se fait en tapant celle-ci suivie
 *   d'un retour chariot.
 *   <BR>
 *   En cas d'erreur de lecture (par exemple un caractere a ete tape
 *   lors de la lecture d'un entier) un message d'erreur est affiche
 *   sur la console et l'execution du programme est interrompue.
 *   <BR><BR><BR>
 *   <B>exemples d'utilisation de cette classe</B><BR>
 *   <PRE>
 *      System.out.print("entrez un entier : ");
 *      System.out.flush();
 *      int i = LectureClavier.lireEntier();
 *      System.out.println("entier lu : " + i);
 *
 *      System.out.print("entrez une chaine :");
 *      System.out.flush();
 *      String s = LectureClavier.lireChaine();
 *      System.out.println("chaine lue : " + s);
 *
 *      System.out.print("entrez une reel (float) : ");
 *      System.out.flush();
 *      float f = LectureClavier.lireFloat();
 *      System.out.println("reel (float) lu : " + f);
 *
 *      System.out.print("entrez une reel (double) : ");
 *      System.out.flush();
 *      double d = LectureClavier.lireDouble();
 *      System.out.println("reel (double) lu : " + d);
 *
 *      System.out.print("entrez une reponse O/N : ");
 *      System.out.flush();
 *      boolean b = LectureClavier.lireOuiNon();
 *      System.out.println("booleen lu : " + b);
 *   </PRE>
 *
 *   @author Philippe Genoud	
 *   @version 13/10/98
 */
public class LectureClavier {
    
    private static BufferedReader stdin = new BufferedReader(
            new InputStreamReader(System.in));
    
    
    /**
     * lecture au clavier d'un horaire
     * @return la chaine de caractère heures:minutes:secondes 
     * @param ce que l'utilisateur souhaite ecrire avant et un booléen qui est vrai si on se restreint à une journée (24h maximum) ou si on a pas de restriction sur les heures
     */
    public static String lireHeure(String msg,boolean b) {
    	System.out.println(msg);
    	int h=LectureClavier.lireEntier("Heure : ");
		while(b&&(h>=24 || h<0)) {
			System.out.println("Merci de saisir un nombre compris entre 0 et 23");
			h=LectureClavier.lireEntier("Heure : ");
		}
		while(h<0) {
			System.out.println("Merci de saisir un nombre compris plus grand que 0");
			h=LectureClavier.lireEntier("Heure : ");
		}
		
		int m=LectureClavier.lireEntier("Minutes : ");
		while(m>=60 || m<0) {
			System.out.println("Merci de saisir un nombre compris entre 0 et 59");
			m=LectureClavier.lireEntier("Minutes : ");
		}
		
		int s=LectureClavier.lireEntier("Secondes : ");
		while(s>=60 || s<0) {
			System.out.println("Merci de saisir un nombre compris entre 0 et 59");
			s=LectureClavier.lireEntier("Secondes : ");
		}
	  	return h+":"+m+":"+s;
    }

    /**
     * lecture au clavier d'une annee
     * @return la chaine de caractère annee:mois:jour 
     */
    public static String lireDate(String msg) {
    	System.out.println(msg);
    	// Lecture de l'année
    	int a=LectureClavier.lireEntier("Année : ");
    	// Lecture du mois
    	int m=LectureClavier.lireEntier("Mois : ");
		while(m>12 || m<1) {
			System.out.println("Merci de saisir un nombre compris entre 1 et 12");
			m=LectureClavier.lireEntier("Mois : ");
		}
		//Lecture du jour
		int j=LectureClavier.lireEntier("Jours : ");
		if(m==2) {
			if(a%4==0) {
				while(j>29 || j<1) {
					System.out.println("Merci de saisir un nombre compris entre 1 et 29");
					j=LectureClavier.lireEntier("Jours : ");
				}
			}
			else {
				while(j>28 || j<1) {
					System.out.println("Merci de saisir un nombre compris entre 1 et 28");
					j=LectureClavier.lireEntier("Jours : ");
				}
			}
		}else if(m==4||m==6||m==9||m==11) {
			while(j>30 || j<1) {
				System.out.println("Merci de saisir un nombre compris entre 1 et 30");
				j=LectureClavier.lireEntier("Jours : ");
			}
		}
		else {
			while(j>31 || j<1) {
				System.out.println("Merci de saisir un nombre compris entre 1 et 30");
				j=LectureClavier.lireEntier("Jours : ");
			}
		}
	  	return m+"-"+m+"-"+j;
    }
    
    /**
     * lecture au clavier d'un entier simple precision (int)
     * @return l'entier lu
     * @param invite une chaine d'invite 
     */
    public static int lireEntier(String invite) {
        int res = 0;
        boolean ok = false;
        System.out.println(invite + " ");
        do {
            try {
                res = Integer.parseInt(stdin.readLine());
                ok = true;
            } catch (NumberFormatException nbfe) {
                System.out.println("entrez un entier");
                System.out.println(invite + " ");
            } catch (Exception e) {
                erreurEntree(e,"entier");
            }
        } while (! ok);
        return res;
    }
    
    /**
     * lecture au clavier d'une chaine de caracteres
     * @return la chaine lue
     */
    public static String lireChaine(String msg) {
    	System.out.println(msg);
        try {
            return(stdin.readLine());
        } catch (Exception e) {
            erreurEntree(e,"chaine de caracteres");
            
            return null;
        }
    }
    
    public static String lireEtat(String msg) {
    	System.out.println(msg);
        try {
            String s = stdin.readLine();
            while (!s.equals("att")&&!s.equals("vol")&&!s.equals("arrive")&&!s.equals("suppr")) {
            	System.out.println("Attendu : 'att' ou 'vol' ou 'arrive' ou 'suppr'\n");
            	s = stdin.readLine();
            }
            return s;
        } catch (Exception e) {
            erreurEntree(e,"chaine de caracteres");
            
            return null;
        }
    }
    
    /**
     * lecture au clavier d'un reel simple precision (float)
     * @return le float lu
     * @param invite une chaine d'invite 
     */
    public static float lireFloat(String invite) {
        System.out.println(invite + " ");
        try {
            return(Float.valueOf(stdin.readLine()).floatValue());
        } catch (Exception e) {
            erreurEntree(e,"reel (float)");
            
            return (float) 0.0;
        }
    }
    
    /**
     * lecture au clavier d'un reel double precision (double)
     * le double lu
     * @param invite une chaine d'invite 
     * @return le double lu
     */
    public static double lireDouble(String invite) {
        System.out.println(invite + " ");
        try {
            return(Double.valueOf(stdin.readLine()).doubleValue());
        } catch (Exception e) {
            erreurEntree(e,"reel (double)");
            return 0.0;
        }
    }
    
    /**
     *  lecture au clavier d'une reponse de type oui/non. Si la valeur tapee est
     *  "o" ou "O" cette methode renvoie <code>true</code>, sinon elle renvoie
     *  <code>false</code>.
     *  @return <code>true</code> si la chaine lue est "o" ou "O",
     *          <code>false</code> sinon
     * @param invite une chaine d'invite 
     */
    public static boolean lireOuiNon(String invite) {
        System.out.println(invite + " ");
        String ch;
        ch = lireChaine("");
        return (ch.equals("o") || ch.equals("O"));
    }
    
    /**
     *  lecture au clavier d'une reponse de type oui/non. Si la valeur tapee est
     *  "o" ou "O" cette methode renvoie <code>true</code>, sinon elle renvoie
     *  <code>false</code>.
     *  @return <code>true</code> si la chaine lue est "o" ou "O",
     *          <code>false</code> sinon
     */
    public static char lireChar(String invite) {
        System.out.println(invite + " ");
        String ch;
        ch = lireChaine("");
        return ch.charAt(0);
    }
    
    /**
     * en cas d'erreur lors d'une lecture, affiche sur la sortie standard
     * (System.out) un message indiquant le type de l'erreur ainsi que
     * la pile des appels de methodes ayant conduit a cette erreur.
     * @param e l'exception decrivant l'erreur.
     * @param message le message d'erreur a afficher.
     */
    private static void erreurEntree(Exception e, String message) {
        System.out.println("Erreur lecture " + message);
        System.out.println(e);
        e.printStackTrace();
        System.exit(1);
    }
    
} // LectureClavier