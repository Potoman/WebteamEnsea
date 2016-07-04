package potoman.webteam.constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.example.webteam.ragot.Ragot;

import android.net.Uri;

public class Webteam {
	public static List<Ragot> maListeDeRagot = new ArrayList<Ragot>();
	public static List<Ragot> maListeDeFavorisRagot = new ArrayList<Ragot>();
	public static List<Uri> maListeDUriFavoris = new ArrayList<Uri>();

	public static int CIBLE_RAGOT_MANAGER_FAVORIS = 0;
	public static int CIBLE_RAGOT_MANAGER_RAGOT = 1;

	public static final String BOITE_D_ENVOIS = "Boîte d'envois";
	public static final String BOITE_DE_RECEPTION = "Boîte de réception";
	public static final String LECTURE_MESSAGE_PRIVE_FROM_BOITE_DE_RECEPTION = "reception";
	public static final String LECTURE_MESSAGE_PRIVE_FROM_BOITE_D_ENVOIS = "envois";
	
	public static final int RESPONSE_FORCE_CLOSE_PREFERENCE = 2;
		
	private static String[] myString = new String[] {
    	"La réponse c'est forcément 4.",
    	"0x42697465",
    	"Goureau, c'est le sherif de l'ENSEA !",
    	"Et Soso tu viens à l\'after ?",
    	"Au top L.B. <3  !!!",
    	"Dans le management tu ne croiras pas. _Yoda, avant de mourir._",
    	"Chuis motard !",
    	"Il est quand même vachement fort Potoman pour faire tout ça. ;)",
    	"A poil !",
    	"C'est un chat qui rentre dans une pharmacie et il demande du sirop pour ma toux !",
    	"J'ai eu 785 au Toeic :)",
    	"Nous au moins on a amené un cochon et deux coqs pour la semaine BDE ! BDC !",
    	"Le prêt-à-quoi ???",
    	"Le gala 2008  était vraiment pas mal ^^",
    	"Va manger à la K-Fet et donne de l'argent au BDE ><",
    	"Chuck Norris approved !",
    	"Pas trop dur d'être un A ?",
    	"Scanne ta roxe si t'as une bonne note et donne la au BDE.",
    	"Et bim !",
    	"Tom-Tom, c'est un peu le mec il a une secte à lui tout seul.",
    	"Les filles c'est sale, alors pas de limousin !",
    	"Au top Corneloup !"};
	
	public static String getPhraseDAmbiance() {
		Random r = new Random();
		return myString[r.nextInt(myString.length)];
	}

	/*
	 * Key pour les préférences :
	 */
	public static final String ID = "id";
	public static final String PSEUDO = "pseudo";
	public static final String PASSWORD = "password";
	public static final String PSEUDO_WEBMAIL = "pseudo_webmail";
	public static final String PASSWORD_WEBMAIL = "password_webmail";
	public static final String PSEUDO_CLUB_PHOTO = "pseudo_club_photo";
	public static final String PASSWORD_CLUB_PHOTO = "password_club_photo";

	public static final String FOLDER_WEBTEAM = "/webteam";
	public static final String FOLDER_CLUB_PHOTO = "/webteam/club_photo";
	public static final String FOLDER_PHOTO_ENSEARQUE = "/webteam/photo_ensearque";
	
}
