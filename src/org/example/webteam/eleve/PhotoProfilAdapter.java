package org.example.webteam.eleve;

import java.io.File;

import potoman.tools.L;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class PhotoProfilAdapter extends BaseAdapter {
    private Context mContext;
    
    String pathFile = null;

    public PhotoProfilAdapter(Context c, String pathFile) {
        mContext = c;
        this.pathFile = pathFile;
    }

    public int getCount() {
        //return mThumbIds.length;
    	L.v("PhotoProfilAdapter", pathFile);
    	File f = new File(pathFile);
    	if (f == null || f.list() == null) {
    		return 0;
    	}
    	return f.list().length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }
    	File f = new File(pathFile);
        L.v("PhotoProfilAdapter", "url image = '" + pathFile + "/" + f.list()[position] + "'");
        imageView.setImageBitmap(BitmapFactory.decodeFile(pathFile + "/" + f.list()[position]));
        return imageView;
    }
}