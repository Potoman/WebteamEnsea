package com.potoman.webteam.boitemanager.item.message;

import com.potoman.tools.L;
import com.potoman.webteam.R;
import com.potoman.webteam.boitemanager.message.Message;

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
