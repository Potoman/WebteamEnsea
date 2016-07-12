package com.potoman.tools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.potoman.webteam.loggin.Root;


public class UrlService {
	public static final String urlRootWebteam = "http://webteam.ensea.fr/api/";

	private static final String VERSION = "WebteamAndroid_v4_Julia_1.apk";
	
	public final static String createUrlVersion()	{
		return urlRootWebteam + "?page=versionForAndroid";
	}
	
	public final static String createUrlConnexion(String pseudo, String password)	{
		return urlRootWebteam + "?page=login&pseudo=" + pseudo + "&mdp=" + encode(password) + "&appli=" + VERSION + "";
	}

	public final static String createUrlRagot(String pseudo, String password)	{
		return urlRootWebteam + "?page=ragots&pseudo=" + pseudo + "&mdp=" + encode(password) + "&appli=" + VERSION + "";
	}

	public final static String createUrlFicheEleve(String pseudo, String password)	{
		return urlRootWebteam + "?page=ficheEleve&pseudo=" + pseudo + "&mdp=" + encode(password) + "&appli=" + VERSION + "";
	}
	
	public final static String createUrlTrombinoscope(String pseudo, String password)	{
		return urlRootWebteam + "?page=trombinoscope&pseudo=" + pseudo + "&mdp=" + encode(password) + "&appli=" + VERSION + "";
	}
	
	public final static String createUrlAnniversaire(String pseudo, String password) {
		return urlRootWebteam + "?page=anniversaire&pseudo=" + pseudo + "&mdp=" + encode(password) + "&appli=" + VERSION + "";
	}
	
	public final static String createUrlBoiteDEnvoie(String pseudo, String password) {
		return urlRootWebteam + "?page=boite_d_envois&pseudo=" + pseudo + "&mdp=" + encode(password) + "&appli=" + VERSION + "";
	}

	public final static String createUrlBoiteDeReception(String pseudo, String password) {
		return urlRootWebteam + "?page=boite_de_reception&pseudo=" + pseudo + "&mdp=" + encode(password) + "&appli=" + VERSION + "";
	}
	
	public final static String createUrlLireMessagePrive(String pseudo, String password) {
		return urlRootWebteam + "?page=boite_messagerie_services&action=lire&pseudo=" + pseudo + "&mdp=" + encode(password) + "&appli=" + VERSION + "";
	}
	
	public final static String createUrlSupprimerMessagePrive(String pseudo, String password) {
		return urlRootWebteam + "?page=boite_messagerie_services&action=supprimer&pseudo=" + pseudo + "&mdp=" + encode(password) + "&appli=" + VERSION + "";
	}
	
	public final static String createUrlEcrireMessagePrive(String pseudo, String password) {
		return urlRootWebteam + "?page=boite_messagerie_services&action=ecrire&pseudo=" + pseudo + "&mdp=" + encode(password) + "&appli=" + VERSION + "";
	}

	//&iso88591
	public final static String URL_CALIGULA_ONE = "http://caligula.ensea.fr/ade/standard/index.jsp";
	
	public final static String URL_CALIGULA_TWO = "http://caligula.ensea.fr/ade/interface.css";
	
	public final static String URL_CALIGULA_TREE = "http://caligula.ensea.fr/ade/button?text=Ok&red=false&cssClass=okbutton";

	public final static String URL_CALIGULA_FOR__CLICK = "http://caligula.ensea.fr/ade/standard/gui/menu.jsp";

	public final static String URL_CALIGULA_FIVE = "http://caligula.ensea.fr/ade/standard/redirectProjects.jsp";
		
	public final static String URL_CONNECTION_GO = "http://caligula.ensea.fr/ade/standard/projects.jsp";
	
	public final static String createUrlCaligulaLogin = "http://caligula.ensea.fr/ade/standard/gui/menu.jsp?projectId=";
	public final static String createUrlCaligulaAfficher() {
		return "http://caligula.ensea.fr/ade/standard/gui/menu.jsp?on=Planning&sub=Afficher&href=/custom/modules/plannings/plannings.jsp&target=visu&tree=true";
	}
	
	private static String encode(String password)
    {
        byte[] uniqueKey = password.getBytes();
        byte[] hash      = null;

        try
        {
            hash = MessageDigest.getInstance("MD5").digest(uniqueKey);
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new Error("No MD5 support in this VM.");
        }

        StringBuilder hashString = new StringBuilder();
        for (int i = 0; i < hash.length; i++)
        {
            String hex = Integer.toHexString(hash[i]);
            if (hex.length() == 1)
            {
                hashString.append('0');
                hashString.append(hex.charAt(hex.length() - 1));
            }
            else
                hashString.append(hex.substring(hex.length() - 2));
        }
        return hashString.toString();
    }

}
