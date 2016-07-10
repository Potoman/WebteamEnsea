package org.example.webteam.boitemanager.item.message;

import org.example.webteam.R;
import org.example.webteam.boitemanager.message.Message;

import potoman.tools.L;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class ItemMessageFull extends ItemMessage {

	private boolean italic = false;
	private boolean bold = false;
	private TextView tv = null;
	
	public ItemMessageFull(Message message) {
		super(message);
	}

	public View getLayoutInflater(LayoutInflater inflater) {
		View myView = inflater.inflate(R.layout.item_message, null);
		tv = ((TextView) myView.findViewById(R.id.tv_titre_message));
		tv.setText(message.getTitre());
		italic = message.getContenuLoad() == Message.CONTENU_NOT_LOAD;
		bold = message.getEtatBoite() == Message.ETAT_NON_LU;
		refresh();
		return myView;
	}
	
	public void setItalic(boolean italic) {
		this.italic = italic;
		refresh();
	}
	
	public void setBold(boolean bold) {
		this.bold = bold;
		refresh();
	}
	
	private void refresh() {
		if (tv != null) {
			if (bold) {
				if (italic) {
					tv.setTypeface(null, Typeface.BOLD_ITALIC);
				}
				else {
					tv.setTypeface(null, Typeface.BOLD);
				}
			}
			else {
				if (italic) {
					tv.setTypeface(null, Typeface.ITALIC);
				}
				else {
					tv.setTypeface(null);
				}
			}
		}
	}
	
}
