package com.potoman.webteam.boitemanager.item.boite;

import java.util.List;

import org.example.webteam.R;
import org.example.webteam.R.id;

import com.potoman.tools.L;
import com.potoman.webteam.boitemanager.message.Message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BoiteDeMessageAdapter extends BaseAdapter {
	
	List<ItemBoiteDeMessage> maListeDItem = null;

	LayoutInflater inflater;
	
	public BoiteDeMessageAdapter(Context context, List<ItemBoiteDeMessage> maListeDItem) {
		inflater = LayoutInflater.from(context);
		this.maListeDItem = maListeDItem;
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
		//L.v("BoiteDeMessageAdapter", "position : " + position);
		convertView = maListeDItem.get(position).getLayoutInflater(inflater);
		
		int countNotRead = 0;
		for (Message message : maListeDItem.get(position).getListMessage()) {
			if (message.getEtatBoite() == Message.ETAT_NON_LU) {
				countNotRead++;
			}
		}
		String titreBoite = maListeDItem.get(position).getNomBoite();
		if (countNotRead > 0) {
			titreBoite = titreBoite.concat(" (" + countNotRead + ")");
		}
		((TextView) convertView.findViewById(R.id.tv_nom_boite)).setText(titreBoite);
		if (maListeDItem.get(position).getNombreDeMessage() > 1) {
			((TextView) convertView.findViewById(R.id.tv_nombre_message_in_boite)).setText("" + maListeDItem.get(position).getNombreDeMessage() + " messages");
		}
		else {
			((TextView) convertView.findViewById(R.id.tv_nombre_message_in_boite)).setText("" + maListeDItem.get(position).getNombreDeMessage() + " message");
		}
		return convertView;
	}
}
