

public class Identifiant {

	private static String nomUtilisateur="dumaclem";	//Chacun met ce qui lui correspond
	private static String mdp="Etgfc7438";
	private static String urlDatabase="jdbc:mysql://localhost:3306/ProjetBD"+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	
	Identifiant(){}
	
	public static String getNomUtilisateur() {
		return nomUtilisateur;
	}

	public static String getMdp() {
		return mdp;
	}

	public static String getUrlDatabase() {
		return urlDatabase;
	}

	
	
}
