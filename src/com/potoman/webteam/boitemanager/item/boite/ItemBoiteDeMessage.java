package com.potoman.webteam.boitemanager.item.boite;

import java.util.List;

import org.example.webteam.R;

import com.potoman.webteam.boitemanager.message.Message;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ItemBoiteDeMessage {
	
	private String nomBoite = "";
	
	protected List<Message> maListDeMessage = null;
	
	
	private boolean italic = false;
	private TextView tv = null;
	
	public ItemBoiteDeMessage(String nomBoite, List<Message> listMessage) {
		this.nomBoite = nomBoite;
		this.maListDeMessage = listMessage;
	}
	
	public int getNombreDeMessage() {
		if (maListDeMessage != null) {
			return maListDeMessage.size();
		}
		else {
			return 0;
		}
	}
	
	public View getLayoutInflater(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.item_boite, null);
		LinearLayout ll = (LinearLayout) view.findViewById(R.id.ll_which_boite);
		tv = (TextView) view.findViewById(R.id.tv_nom_boite);
		setItalic(italic);
		ll.setTag(nomBoite);
		return view;
	}

	public String getNomBoite() {
		return nomBoite;
	}

	public void setListMessage(List<Message> listMessage) {
		this.maListDeMessage = listMessage;
	}
	public List<Message> getListMessage() {
		return maListDeMessage;
	}
	
	public void setItalic(boolean italic) {
		if (tv != null) {
			if (italic) {
				tv.setTypeface(null, Typeface.ITALIC);
			}
			else {
				tv.setTypeface(null);
			}
		}
		this.italic = italic;
	}
}
