package potoman.tools;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Observer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.util.EntityUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class DownloadImage {
	public static void downloadImage(final Observer observer, final String urlImageString) {
		L.v("DownloadImage", "Url>" + urlImageString + "<");
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					URL urlImage = new URL(urlImageString);
					HttpURLConnection connection = (HttpURLConnection) urlImage
							.openConnection();
					InputStream inputStream = connection.getInputStream();
					observer.update(null, BitmapFactory
							.decodeStream(new FlushedInputStream(inputStream)));
					L.v("DownloadImage", "Image chargée.");
				} catch (MalformedURLException e) {
					L.v("DownloadImage", "Erreur : MalformedURLException");
					e.printStackTrace();
					observer.update(null, null);
				} catch (IOException e) {
					L.v("DownloadImage", "Erreur : IOException");
					e.printStackTrace();
					observer.update(null, null);
				}
			}
		}).start();
	}

	public static Bitmap downloadImageWithHttpClient(String urlImageString, HttpClient httpClient) {
		L.v("DownloadImage", "Url>" + urlImageString + "<");
    	Bitmap bitmap = null;
    	try {
//			URL urlImage = new URL(urlImageString);
//			HttpURLConnection connection = (HttpURLConnection) urlImage.openConnection();
//			InputStream inputStream = connection.getInputStream();
    	    if (httpClient == null)
    	    	L.e("DownloadImage", "ANORMAL !!!");
    	    else
    			L.v("DownloadImage", "Appel de l'image...");
    	    
    	    
			HttpResponse response = httpClient.execute(new HttpGet(urlImageString));
			L.v("DownloadImage", "Lecture de la réponse de l'image : ");
			//Caligula.lireReponse(response);
			InputStream inputStream = response.getEntity().getContent();
			
			
			bitmap = BitmapFactory.decodeStream(inputStream);
			//bitmap = BitmapFactory.decodeStream(new FlushedInputStream(inputStream));
		} catch (MalformedURLException e) {
			L.v("DownloadImage", "Erreur : MalformedURLException");
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			L.v("DownloadImage", "Erreur : IOException");
			e.printStackTrace();
			return null;
		}
		L.v("DownloadImage", "Image chargée.");
		return bitmap;
    }
	
	// DOESN'T WORK
	
	public static class PlurkInputStream extends FilterInputStream {

	    protected PlurkInputStream(InputStream in) {
	        super(in);
	    }

	    @Override
	    public int read(byte[] buffer, int offset, int count)
	        throws IOException {
	        int ret = super.read(buffer, offset, count);
	        for ( int i = 6; i < buffer.length - 4; i++ ) {
	            if ( buffer[i] == 0x2c ) {
	                if ( buffer[i + 2] == 0 && buffer[i + 1] > 0
	                    && buffer[i + 1] <= 48 ) {
	                    buffer[i + 1] = 0;
	                }
	                if ( buffer[i + 4] == 0 && buffer[i + 3] > 0
	                    && buffer[i + 3] <= 48 ) {
	                    buffer[i + 3] = 0;
	                }
	            }
	        }
	        return ret;
	    }
	}
	
	// WORK
	
	static class FlushedInputStream extends FilterInputStream {
		public FlushedInputStream(InputStream inputStream) {
		    super(inputStream);
		}

		@Override
		public long skip(long n) throws IOException {
		    long totalBytesSkipped = 0L;
		    while (totalBytesSkipped < n) {
		        long bytesSkipped = in.skip(n - totalBytesSkipped);
		        if (bytesSkipped == 0L) {
		              int _byte = read();
		              if (_byte < 0) {
		                  break;  // we reached EOF
		              } else {
		                  bytesSkipped = 1; // we read one byte
		              }
		       }
		        totalBytesSkipped += bytesSkipped;
		    }
		    return totalBytesSkipped;
		}

	}
	
	
	public static Bitmap downloadImageWithHttpClientBis(String url, HttpClient httpClient) {
		try {
	        HttpUriRequest request = new HttpGet(url.toString());
	        HttpResponse response = httpClient.execute(request);
	 
	        StatusLine statusLine = response.getStatusLine();
	        int statusCode = statusLine.getStatusCode();
	        if (statusCode == 200) {
	            HttpEntity entity = response.getEntity();
	            byte[] bytes = EntityUtils.toByteArray(entity);
	 
	            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0,
	                    bytes.length);
	            return bitmap;
	        } else {
	            throw new IOException("Download failed, HTTP response code "
	                    + statusCode + " - " + statusLine.getReasonPhrase());
	        }
		}
		catch (Exception e) {
			L.v("DownloadImage", "EXCEPTION DANS LE TELECHARGEMENT DE L'IMAGE !!!");
			return null;
		}
    }
	
	
	

}
