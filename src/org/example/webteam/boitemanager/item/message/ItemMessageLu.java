package org.example.webteam.boitemanager.item.message;

import org.example.webteam.R;
import org.example.webteam.R.id;
import org.example.webteam.R.layout;
import org.example.webteam.boitemanager.message.Message;

import potoman.tools.L;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class ItemMessageLu extends ItemMessage {
	
	public ItemMessageLu(Message message) {
		super(message);
	}
	
	public View getLayoutInflater(LayoutInflater inflater) {
		L.v("ItemBoiteDeMessage", "ItemMessageLu");
		View myView = inflater.inflate(R.layout.item_message_lu, null);
		String ma = message.getTitre();
		((TextView)myView.findViewById(R.id.tv_titre_message)).setText(ma);
		return myView;
	}
}
