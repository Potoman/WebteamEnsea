package com.potoman.webteam.anniversaire;

import java.util.List;

import org.example.webteam.R;
import org.example.webteam.R.id;
import org.example.webteam.R.layout;

import com.potoman.tools.L;
import com.potoman.webteam.eleve.ContactWebteam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AnniversaireAdapter extends BaseAdapter {
	List<ContactWebteam> lstProfil;
	TextView textViewPseudo;
	LinearLayout ll;
	LayoutInflater inflater;

	public AnniversaireAdapter(Context context, List<ContactWebteam> lstProfil) {//, CheckBox essaiCheckBox) {
		inflater = LayoutInflater.from(context);
		this.lstProfil = lstProfil;
		L.v("AnniversaireAdapter", "Constructeur... Taille : " + lstProfil.size());
		//checkBoxView = essaiCheckBox;
	}
	public int getCount() {
		return lstProfil.size();
	}

	public Object getItem(int position) {
		return lstProfil.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	private class ViewHolder {
		//LinearLayout ll;
		//TextView tvPseudo;
		TextView tvPrenomNom;
	}
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null) {
			holder = new ViewHolder();
					convertView = inflater.inflate(R.layout.itemanniversaire, null);
					//holder.ll = (LinearLayout)convertView.findViewById(R.id.llCorpRagot);
					//holder.tvPseudo = (TextView)convertView.findViewById(R.id.tvPseudo);
					holder.tvPrenomNom = (TextView)convertView.findViewById(R.id.tvPrenomNom);
					/*holder.tvTime = (TextView)convertView.findViewById(R.id.tvTime);
					holder.tvDate = (TextView)convertView.findViewById(R.id.tvDate);
					holder.ivCoinHautGauche = (ImageView)convertView.findViewById(R.id.ivCoinHautGauche);
					holder.ivCoinHautDroite = (ImageView)convertView.findViewById(R.id.ivCoinHautDroite);
					holder.ivCoinBasGauche = (ImageView)convertView.findViewById(R.id.ivCoinBasGauche);
					holder.ivCoinBasDroite = (ImageView)convertView.findViewById(R.id.ivCoinBasDroite);*/
			convertView.setTag(holder);
		} else {
		holder = (ViewHolder) convertView.getTag();
		}
    	holder.tvPrenomNom.setText(lstProfil.get(position).toStringResultatTrombi());

    	return convertView;
	}
}
