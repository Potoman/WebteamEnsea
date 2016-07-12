package com.potoman.webteam.boitemanager.item.message;

import java.util.ArrayList;
import java.util.List;

import com.potoman.tools.L;
import com.potoman.webteam.boitemanager.message.Message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class MessageAdapter extends BaseAdapter {
	
	List<ItemMessage> maListeDItem = null;
	
	List<Message> maListeDeMessage = null;

	LayoutInflater inflater;
	
	public MessageAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		maListeDItem = new ArrayList<ItemMessage>();
	}

	@Override
	public int getCount() {
		return maListeDItem.size();
	}

	@Override
	public Object getItem(int position) {
		return maListeDItem.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {	
		if (maListeDItem != null)
			convertView = maListeDItem.get(position).getLayoutInflater(inflater);
		return convertView;
	}

	public void setListOfMessage(List<Message> boiteDeMessage) {
		maListeDeMessage = boiteDeMessage;
		notifyDataSetChanged();
	}
	
	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		maListeDItem.clear();
		if (maListeDeMessage == null) {
			return;
		}
		for (int i = 0; i < maListeDeMessage.size(); i++) {
			
			maListeDItem.add(new ItemMessageFull(maListeDeMessage.get(i)));
			
//			switch (maListeDeMessage.get(i).getEtatBoite()) {
//			case Message.ETAT_LU:
//				maListeDItem.add(new ItemMessageLu(maListeDeMessage.get(i)));
//				break;
//			case Message.ETAT_NON_LU:
//				maListeDItem.add(new ItemMessageNonLu(maListeDeMessage.get(i)));
//				break;
//			}
		}
	}
	
	public List<Message> getListMessage() {
		return maListeDeMessage;
	}
}
