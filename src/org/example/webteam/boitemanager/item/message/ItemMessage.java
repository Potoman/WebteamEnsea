package org.example.webteam.boitemanager.item.message;

import org.example.webteam.boitemanager.message.Message;

import android.view.LayoutInflater;
import android.view.View;

public abstract class ItemMessage {
	
	protected Message message = null;
	//protected int whichBoite = BoiteDeMessage.BOITE_D_ENVOIS;
	
	public abstract View getLayoutInflater(LayoutInflater inflater);
		
	public ItemMessage(Message message) {
		this.message = message;
	}
	
	public Message getMessage() {
		return message;
	}
}
