package potoman.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.DefaultedHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import potoman.webteam.exception.ExceptionServiceConnected;
import potoman.webteam.exception.ExceptionServiceServer;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

/*
 * Service JSON.
 */

public class CallService {
	
	public static JSONObject getJsonGeth(Context context, String url) throws ExceptionServiceServer, ExceptionServiceConnected {
			L.v("CallService", "Geth:In>"+url);
			if (isConnected(context)) {
				try {
				L.v("CallService", "isConnected");
				JSONObject myjson = null;
				StringBuffer stringBuffer = null;
				try {
					URL urlPage = new URL(url);
					HttpURLConnection connection = (HttpURLConnection) urlPage.openConnection();
					InputStream inputStream = connection.getInputStream();
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
					stringBuffer = new StringBuffer();
					String ligne;
					while((ligne = bufferedReader.readLine()) != null) {
						//L.v("CallService", ligne);
						stringBuffer.append(ligne);
					}
					connection.disconnect();
					bufferedReader.close();
					inputStream.close();
				} catch (MalformedURLException e) {
						L.v("CallService", "Geth: MalformedURLException , "+e);
				} catch (IOException e) {
						L.v("CallService", "Geth: IOException , "+e);
				}
				if (stringBuffer.length() > 0) {
					String fin = stringBuffer.toString().substring(stringBuffer.toString().indexOf("{"), stringBuffer.toString().lastIndexOf("}") + 1);
					afficherString(fin);
					int i = 0;
					while (i <= 1) {
						try {
							myjson = new JSONObject(fin);
						} catch (JSONException e) {
							e.printStackTrace();
							L.v("CallService", "Geth: JSONException , " + e);
							fin = fin.replaceAll("<input type=\"hidden\" name=\"sid\" value=\"[a-z0-9]+\" />", "");
						}
						i++;
					}
				}
				else {
					myjson = null;
		
					L.v("CallService", "Geth:Out> null");
				}
				return myjson;
				}
				catch (Exception except) {
					L.v("CallService", "ExceptionServiceServer");
					throw (new ExceptionServiceServer(context));
				}
			}
			else {
				L.v("CallService", "ExceptionServiceConnected");
				throw (new ExceptionServiceConnected(context));
			}
	}
	
	public static JSONObject getJsonPost(Context context, String url, List<String> nameData, List<String> data) throws ExceptionServiceServer, ExceptionServiceConnected {
		L.v("CallServicePost", "Post:In>"+url);
		if (isConnected(context)) {
			try {
				StringBuffer stringBuffer = null;
				HttpPost httppost = new HttpPost(url);
				
				try {
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(); //On crée la liste qui contiendra tous nos paramètres
				    	for (int i = 0; i < nameData.size(); i++) {
				    		L.v("CallServicePost", nameData.get(i) + " : " + data.get(i));
				    		nameValuePairs.add(new BasicNameValuePair(nameData.get(i), replaceAccent(data.get(i))));
				    	}
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				    HttpParams params = new BasicHttpParams();
				    params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
				    HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
				    //HttpProtocolParams.setContentCharset(params, HTTP.ISO_8859_1);
				    HttpClient httpclient = new DefaultHttpClient(params);
				    HttpResponse response;
					try {
					    response = httpclient.execute(httppost);
					    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
						stringBuffer = new StringBuffer();
						String ligne;
						while((ligne = bufferedReader.readLine()) != null) {
							stringBuffer.append(ligne);
					        //L.v("CallServicePost", "ligne : " + ligne);
						}
						bufferedReader.close();
					} catch (ClientProtocolException e) {
				        L.v("CallServicePost", "Post: ClientProtocolException : " + e);
				    } catch (IOException e) {
				        L.v("CallServicePost", "Post: IOException : " + e);
				    }
		
				} catch (UnsupportedEncodingException e) {
					L.v("CallServicePost", "Post: UnsupportedEncodingException : " + e);
				}
				String fin = stringBuffer.toString().substring(stringBuffer.toString().indexOf("{"), stringBuffer.toString().lastIndexOf("}") + 1);
				JSONObject myjson = null;
				try {
					myjson = new JSONObject(fin);
				} catch (JSONException e) {
					e.printStackTrace();
					L.v("CallServicePost", "Post: JSONException , "+e);
				}
				return myjson;
			}
			catch (Exception except) {
				throw (new ExceptionServiceServer(context));
			}
		}
		else {
			throw (new ExceptionServiceConnected(context));
		}
	}
	
	private static void afficherString(String toDisplay) {
		if (toDisplay.length() > 100)
			for (int j = 0; j <= toDisplay.length() / 100 - 1; j++)
				L.v("CallService.afficherString", toDisplay.substring(j * 100, (j + 1) * 100 - 1));
	L.v("CallService.afficherString", toDisplay.substring((int)Math.floor(toDisplay.length() / 100) * 100, toDisplay.length() - 1));	
	}
	
	//Détecte si la connection est active
	public static boolean isConnected(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
		.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo != null) {
			//System.out.println(networkInfo.getTypeName()); // mobile ou WIFI
			State networkState = networkInfo.getState();
			if (networkState.compareTo(State.CONNECTED) == 0) {
				return true;
			}
		}
		//Toast.makeText(context, "Aucune connection internet n'est disponible.", Toast.LENGTH_SHORT).show();
		return false;
	}	

	public static String replaceAccent(String maString) { 
		
		maString = maString.replace(" ", "&#32;");
		maString = maString.replace(" ", "&#160;");
		maString = maString.replace("¡", "&#161;");
		maString = maString.replace("¢", "&#162;");
		maString = maString.replace("£", "&#163;");
		maString = maString.replace("¤", "&#164;");
		maString = maString.replace("¥", "&#165;");
		maString = maString.replace("¦", "&#166;");
		maString = maString.replace("§", "&#167;");
		maString = maString.replace("¨", "&#168;");
		maString = maString.replace("©", "&#169;");
		maString = maString.replace("ª", "&#170;");
		maString = maString.replace("«", "&#171;");
		maString = maString.replace("¬", "&#172;");
		//maString = maString.replace("		­", "&#173;");
		maString = maString.replace("®", "&#174;");
		maString = maString.replace("¯", "&#175;");
		maString = maString.replace("°", "&#176;");
		maString = maString.replace("±", "&#177;");
		maString = maString.replace("²", "&#178;");
		maString = maString.replace("³", "&#179;");
		maString = maString.replace("´", "&#180;");
		maString = maString.replace("µ", "&#181;");
		maString = maString.replace("¶", "&#182;");
		maString = maString.replace("·", "&#183;");
		maString = maString.replace("¸", "&#184;");
		maString = maString.replace("¹", "&#185;");
		maString = maString.replace("º", "&#186;");
		maString = maString.replace("»", "&#187;");
		maString = maString.replace("¼", "&#188;");
		maString = maString.replace("½", "&#189;");
		maString = maString.replace("¾", "&#190;");
		maString = maString.replace("¿", "&#191;");
		maString = maString.replace("À", "&#192;");
		maString = maString.replace("Á", "&#193;");
		maString = maString.replace("Â", "&#194;");
		maString = maString.replace("Ã", "&#195;");
		maString = maString.replace("Ä", "&#196;");
		maString = maString.replace("Å", "&#197;");
		maString = maString.replace("Æ", "&#198;");
		maString = maString.replace("Ç", "&#199;");
		maString = maString.replace("È", "&#200;");
		maString = maString.replace("É", "&#201;");
		maString = maString.replace("Ê", "&#202;");
		maString = maString.replace("Ë", "&#203;");
		maString = maString.replace("Ì", "&#204;");
		maString = maString.replace("Í", "&#205;");
		maString = maString.replace("Î", "&#206;");
		maString = maString.replace("Ï", "&#207;");
		maString = maString.replace("Ð", "&#208;");
		maString = maString.replace("Ñ", "&#209;");
		maString = maString.replace("Ò", "&#210;");
		maString = maString.replace("Ó", "&#211;");
		maString = maString.replace("Ô", "&#212;");
		maString = maString.replace("Õ", "&#213;");
		maString = maString.replace("Ö", "&#214;");
		maString = maString.replace("×", "&#215;");
		maString = maString.replace("Ø", "&#216;");
		maString = maString.replace("Ù", "&#217;");
		maString = maString.replace("Ú", "&#218;");
		maString = maString.replace("Û", "&#219;");
		maString = maString.replace("Ü", "&#220;");
		maString = maString.replace("Ý", "&#221;");
		maString = maString.replace("Þ", "&#222;");
		maString = maString.replace("ß", "&#223;");
		maString = maString.replace("à", "&#224;");
		maString = maString.replace("á", "&#225;");
		maString = maString.replace("â", "&#226;");
		maString = maString.replace("ã", "&#227;");
		maString = maString.replace("ä", "&#228;");
		maString = maString.replace("å", "&#229;");
		maString = maString.replace("æ", "&#230;");
		maString = maString.replace("ç", "&#231;");
		maString = maString.replace("è", "&#232;");
		maString = maString.replace("é", "&#233;");
		maString = maString.replace("ê", "&#234;");
		maString = maString.replace("ë", "&#235;");
		maString = maString.replace("ì", "&#236;");
		maString = maString.replace("í", "&#237;");
		maString = maString.replace("î", "&#238;");
		maString = maString.replace("ï", "&#239;");
		maString = maString.replace("ð", "&#240;");
		maString = maString.replace("ñ", "&#241;");
		maString = maString.replace("ò", "&#242;");
		maString = maString.replace("ó", "&#243;");
		maString = maString.replace("ô", "&#244;");
		maString = maString.replace("õ", "&#245;");
		maString = maString.replace("ö", "&#246;");
		maString = maString.replace("÷", "&#247;");
		maString = maString.replace("ø", "&#248;");
		maString = maString.replace("ù", "&#249;");
		maString = maString.replace("ú", "&#250;");
		maString = maString.replace("û", "&#251;");
		maString = maString.replace("ü", "&#252;");
		maString = maString.replace("ý", "&#253;");
		maString = maString.replace("þ", "&#254;");
		maString = maString.replace("ÿ", "&#255;");
		maString = maString.replace("Œ", "&#338;");
		maString = maString.replace("œ", "&#339;");
		maString = maString.replace("Š", "&#352;");
		maString = maString.replace("š", "&#353;");
		maString = maString.replace("Ÿ", "&#376;");
		maString = maString.replace("ƒ", "&#402;");
		maString = maString.replace("–", "&#8211;");
		maString = maString.replace("—", "&#8212;");
		maString = maString.replace("‘", "&#8216;");
		maString = maString.replace("’", "&#8217;");
		maString = maString.replace("‚", "&#8218;");
		maString = maString.replace("“", "&#8220;");
		maString = maString.replace("”", "&#8221;");
		maString = maString.replace("„", "&#8222;");
		maString = maString.replace("†", "&#8224;");
		maString = maString.replace("‡", "&#8225;");
		maString = maString.replace("•", "&#8226;");
		maString = maString.replace("…", "&#8230;");
		maString = maString.replace("‰", "&#8240;");
		maString = maString.replace("€", "&#8364;");
		maString = maString.replace("™", "&#8482;");
		//Log.v("replaceAccent", "APRES : " + maString);
		return maString;
	}

public static String replaceAccentHtmlToText(String maString) { 
		
		maString = maString.replace("&#32;", " ");
		maString = maString.replace("&#160;", " ");
		maString = maString.replace("&#161;", "¡");
		maString = maString.replace("&#162;", "¢");
		maString = maString.replace("&#163;", "£");
		maString = maString.replace("&#164;", "¤");
		maString = maString.replace("&#165;", "¥");
		maString = maString.replace("&#166;", "¦");
		maString = maString.replace("&#167;", "§");
		maString = maString.replace("&#168;", "¨");
		maString = maString.replace("&#169;", "©");
		maString = maString.replace("&#170;", "ª");
		maString = maString.replace("&#171;", "«");
		maString = maString.replace("&#172;", "¬");
		//maString = maString.replace("&#173;", "		­");
		maString = maString.replace("&#174;", "®");
		maString = maString.replace("&#175;", "¯");
		maString = maString.replace("&#176;", "°");
		maString = maString.replace("&#177;", "±");
		maString = maString.replace("&#178;", "²");
		maString = maString.replace("&#179;", "³");
		maString = maString.replace("&#180;", "´");
		maString = maString.replace("&#181;", "µ");
		maString = maString.replace("&#182;", "¶");
		maString = maString.replace("&#183;", "·");
		maString = maString.replace("&#184;", "¸");
		maString = maString.replace("&#185;", "¹");
		maString = maString.replace("&#186;", "º");
		maString = maString.replace("&#187;", "»");
		maString = maString.replace("&#188;", "¼");
		maString = maString.replace("&#189;", "½");
		maString = maString.replace("&#190;", "¾");
		maString = maString.replace("&#191;", "¿");
		maString = maString.replace("&#192;", "À");
		maString = maString.replace("&#193;", "Á");
		maString = maString.replace("&#194;", "Â");
		maString = maString.replace("&#195;", "Ã");
		maString = maString.replace("&#196;", "Ä");
		maString = maString.replace("&#197;", "Å");
		maString = maString.replace("&#198;", "Æ");
		maString = maString.replace("&#199;", "Ç");
		maString = maString.replace("&#200;", "È");
		maString = maString.replace("&#201;", "É");
		maString = maString.replace("&#202;", "Ê");
		maString = maString.replace("&#203;", "Ë");
		maString = maString.replace("&#204;", "Ì");
		maString = maString.replace("&#205;", "Í");
		maString = maString.replace("&#206;", "Î");
		maString = maString.replace("&#207;", "Ï");
		maString = maString.replace("&#208;", "Ð");
		maString = maString.replace("&#209;", "Ñ");
		maString = maString.replace("&#210;", "Ò");
		maString = maString.replace("&#211;", "Ó");
		maString = maString.replace("&#212;", "Ô");
		maString = maString.replace("&#213;", "Õ");
		maString = maString.replace("&#214;", "Ö");
		maString = maString.replace("&#215;", "×");
		maString = maString.replace("&#216;", "Ø");
		maString = maString.replace("&#217;", "Ù");
		maString = maString.replace("&#218;", "Ú");
		maString = maString.replace("&#219;", "Û");
		maString = maString.replace("&#220;", "Ü");
		maString = maString.replace("&#221;", "Ý");
		maString = maString.replace("&#222;", "Þ");
		maString = maString.replace("&#223;", "ß");
		maString = maString.replace("&#224;", "à");
		maString = maString.replace("&#225;", "á");
		maString = maString.replace("&#226;", "â");
		maString = maString.replace("&#227;", "ã");
		maString = maString.replace("&#228;", "ä");
		maString = maString.replace("&#229;", "å");
		maString = maString.replace("&#230;", "æ");
		maString = maString.replace("&#231;", "ç");
		maString = maString.replace("&#232;", "è");
		maString = maString.replace("&#233;", "é");
		maString = maString.replace("&#234;", "ê");
		maString = maString.replace("&#235;", "ë");
		maString = maString.replace("&#236;", "ì");
		maString = maString.replace("&#237;", "í");
		maString = maString.replace("&#238;", "î");
		maString = maString.replace("&#239;", "ï");
		maString = maString.replace("&#240;", "ð");
		maString = maString.replace("&#241;", "ñ");
		maString = maString.replace("&#242;", "ò");
		maString = maString.replace("&#243;", "ó");
		maString = maString.replace("&#244;", "ô");
		maString = maString.replace("&#245;", "õ");
		maString = maString.replace("&#246;", "ö");
		maString = maString.replace("&#247;", "÷");
		maString = maString.replace("&#248;", "ø");
		maString = maString.replace("&#249;", "ù");
		maString = maString.replace("&#250;", "ú");
		maString = maString.replace("&#251;", "û");
		maString = maString.replace("&#252;", "ü");
		maString = maString.replace("&#253;", "ý");
		maString = maString.replace("&#254;", "þ");
		maString = maString.replace("&#255;", "ÿ");
		maString = maString.replace("&#338;", "Œ");
		maString = maString.replace("&#339;", "œ");
		maString = maString.replace("&#352;", "Š");
		maString = maString.replace("&#353;", "š");
		maString = maString.replace("&#376;", "Ÿ");
		maString = maString.replace("&#402;", "ƒ");
		maString = maString.replace("&#8211;", "–");
		maString = maString.replace("&#8212;", "—");
		maString = maString.replace("&#8216;", "‘");
		maString = maString.replace("&#8217;", "’");
		maString = maString.replace("&#8218;", "‚");
		maString = maString.replace("&#8220;", "“");
		maString = maString.replace("&#8221;", "”");
		maString = maString.replace("&#8222;", "„");
		maString = maString.replace("&#8224;", "†");
		maString = maString.replace("&#8225;", "‡");
		maString = maString.replace("&#8226;", "•");
		maString = maString.replace("&#8230;", "…");
		maString = maString.replace("&#8240;", "‰");
		maString = maString.replace("&#8364;", "€");
		maString = maString.replace("&#8482;" ,"™" );
		//Log.v("replaceAccent", "APRES : " + maString);
		return maString;
	}
	
	public static void setParam(HttpClient myDefaultHttpClient, boolean isUTF8) {
		HttpParams params = new BasicHttpParams();
	    params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
	    if (isUTF8) {
	    	HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
	    }
	    else {
	    	HttpProtocolParams.setContentCharset(params, HTTP.ISO_8859_1);
	    }
	    //httpClient.setParams(params);
	    ((DefaultHttpClient) myDefaultHttpClient).setParams(params);	
	}

	public static void setParam(HttpPost httpPost, boolean isUTF8) {
		HttpParams params = new BasicHttpParams();
	    params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
	    if (isUTF8) {
		    HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
	    }
	    else {
	    	HttpProtocolParams.setContentCharset(params, HTTP.ISO_8859_1);
	    }
	    httpPost.setParams(params);
	}
	
	public static void setParam(HttpResponse httpResponse, boolean isUTF8) {
		HttpParams params = new BasicHttpParams();
	    params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
	    if (isUTF8) {
	    	HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
	    }
	    else {
		    HttpProtocolParams.setContentCharset(params, HTTP.ISO_8859_1);
	    }
	    httpResponse.setParams(params);
	}

	public static HttpResponse askForSomething(String url, HttpClient myDefaultHttpClient, boolean isUTF8) {
	    //L.v("CallSerive", "AskForSomething : " + url);
		HttpPost httpPost = new HttpPost(url);
		setParam(httpPost, isUTF8);
	    try {
	    	setParam(myDefaultHttpClient, isUTF8);
	    	HttpResponse httpResponse = myDefaultHttpClient.execute(httpPost);
	    	return httpResponse;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return null;
	}
	
	public static HttpResponse askForSomethingWithPost(String url, HttpClient myDefaultHttpClient, List<String> nameData, List<String> valeurData, boolean isUTF8) {
	    L.v("CallSerive", "AskForSomethingWithPost : " + url);
	    try {
		    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(); //On crée la liste qui contiendra tous nos paramètres
		    L.v("CallService", "size post = " + nameData.size() + ", " + valeurData.size());
	    	for (int i = 0; i < nameData.size(); i++) {
	    		L.v("CallService", "Ajout des données post : " + nameData.get(i) + " : " + valeurData.get(i));
	    		nameValuePairs.add(new BasicNameValuePair(nameData.get(i), valeurData.get(i)));
	    	}
	    	setParam(myDefaultHttpClient, isUTF8);
	    	HttpPost httppost = new HttpPost(url);
			//setParam(httppost);
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		    
		    return myDefaultHttpClient.execute(httppost);
	    } catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return null;
	}
	

	
	public static HttpResponse askForResponse(String url, HttpClient myDefaultHttpClient, boolean isUTF8, String... dataPost) {
	    L.v("CallSerive", "AskForSomethingWithPost : " + url);
	    try {
	    	HttpPost httppost = new HttpPost(url);
		    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	    	if (dataPost != null && dataPost.length != 0 && (dataPost.length % 2) == 0) {
	    		for (int i = 0; i < dataPost.length / 2; i++) {
		    		nameValuePairs.add(new BasicNameValuePair(dataPost[i* 2], dataPost[i* 2 + 1]));
	    		}
	    		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	    	}
	    	setParam(myDefaultHttpClient, isUTF8);
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		    
		    return myDefaultHttpClient.execute(httppost);
	    } catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return null;
	}
	
	public static JSONObject askForJSON(String url, HttpClient myDefaultHttpClient, boolean isUTF8, String... dataPost) {
	    L.v("CallSerive", "AskForSomethingWithPost : " + url);
	    try {
	    	HttpPost httppost = new HttpPost(url);
		    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	    	if (dataPost != null && dataPost.length != 0 && (dataPost.length % 2) == 0) {
	    		for (int i = 0; i < dataPost.length / 2; i++) {
		    		nameValuePairs.add(new BasicNameValuePair(dataPost[i* 2], dataPost[i* 2 + 1]));
	    		}
	    		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	    	}
	    	setParam(myDefaultHttpClient, isUTF8);
		    
			HttpResponse response = myDefaultHttpClient.execute(httppost);
		    
		    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer stringBuffer = new StringBuffer();
			String ligne;
			while((ligne = bufferedReader.readLine()) != null) {
				stringBuffer.append(ligne);
			}
			bufferedReader.close();
		    
		    if (stringBuffer.length() > 0) {
				String fin = stringBuffer.toString().substring(stringBuffer.toString().indexOf("{"), stringBuffer.toString().lastIndexOf("}") + 1);
				try {
					return new JSONObject(fin);
				} catch (JSONException e) {
					e.printStackTrace();
					L.v("CallService", "Geth: JSONException , " + e);
					fin = fin.replaceAll("<input type=\"hidden\" name=\"sid\" value=\"[a-z0-9]+\" />", "");
				}
			}
	    } catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return null;
	}
	
}

