package com.potoman.webteam;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.util.ByteArrayBuffer;

import com.potoman.tools.L;
import com.potoman.webteam.loggin.Root;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;

public class DownloadMAJ extends AsyncTask<Root, Void, String> {
	
	private Root root = null;
	private String nomVersion = "";
	
	public DownloadMAJ(Root root, String nomVersion) {
		this.root = root;
		this.nomVersion = nomVersion;
	}
	
	@Override
	protected String doInBackground(Root... myRoot) {
		L.v("DownloadMAJ", "plop");
		
		//1.2Mb
		try {
			//String filePath = pathForSaveInternalMemory + "/" + myRoot[0].getNomNewVersion();//"/testImage.jpg";
			L.v("", "Environment.getRootDirectory() : " + Environment.getRootDirectory());
			// Affiche : "/mnt/sdcard/WebteamAndroid_v3_Gwenaell_1.apk" sous android 4.0
			// Affiche : "/sdcard/WebteamAndroid_v3_Gwenaell_1.apk" sous android 2.1
			//L.v("DownloadMAJ", filePath);
            URL url = new URL("http://webteam.ensea.fr/api/applis/android/" + nomVersion); //you can write here any link
            //File file = new File(filePath);

            long startTime = System.currentTimeMillis();
            L.d("DownloadMAJ", "download begining");
            L.d("DownloadMAJ", "download url:" + url);
           //L.d("DownloadMAJ", "downloaded file name:" + filePath);
            /* Open a connection to that URL. */
            URLConnection ucon = url.openConnection();

            /*
             * Define InputStreams to read from the URLConnection.
             */
            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);

            /*
             * Read bytes to the Buffer until there is nothing more to read(-1).
             */
            L.v("DownloadMAJ", "taille : " + ucon.getContentLength());
            ByteArrayBuffer baf = new ByteArrayBuffer(50);
            int count = 0;
            int countTotal = 0;
            int current = 0;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
                count++;
                countTotal++;
                if (count == 50000) {
                	L.v("DownloadMAJ", "pourcentage : " + 100 * countTotal / ucon.getContentLength());
//                	myRoot[0].actualizeNotification(100 * countTotal / ucon.getContentLength(), (int) (ucon.getContentLength() / 1000), countTotal/1000);
                	count = 0;
                }
            }
//            myRoot[0].actualizeNotification(100, (int) (ucon.getContentLength() / 1000), (int) (ucon.getContentLength() / 1000));
            L.v("DownloadMAJ", "taille téléchargé : " + count + ", countTotal = " + countTotal);
            /* Convert the Bytes read to a String. */
            
            FileOutputStream fos = root.openFileOutput(nomVersion,
                    Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
            
            
            fos.write(baf.toByteArray());
            fos.flush();
            fos.close();
            L.d("DownloadMAJ", "download ready in"
                            + ((System.currentTimeMillis() - startTime) / 1000)
                            + " sec");

	    } catch (IOException e) {
	    	e.printStackTrace();
	            L.e("DownloadMAJ", "Error: " + e);
	    }
		catch (Exception e) {
            L.e("DownloadMAJ", "FAIL !");
		}
		
		
		return null;
	}
	
	@Override
	protected void onPostExecute(String result) {
		L.d("DownloadMAJ", "finis");
		super.onPostExecute(result);
	}
	
	@Override
	protected void finalize() throws Throwable {
		L.d("DownloadMAJ", "finalize");
		super.finalize();
	}

}
