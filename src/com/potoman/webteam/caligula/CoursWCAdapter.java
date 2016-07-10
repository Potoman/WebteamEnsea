
package org.example.webteam.caligula;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.example.webteam.R;
import org.example.webteam.R.id;
import org.example.webteam.R.layout;
import org.example.webteam.ragot.RagotAdapter;

import potoman.tools.L;
import android.content.Context;
import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CoursWCAdapter extends BaseAdapter {
	
	List<Cours> lstCours;
	LinearLayout llMaJournee;
	TextView tvCoursHeure;
	//TextView tvCoursDate;
	TextView tvCoursDuree;
	TextView tvCoursMatiere;
	TextView tvCoursSalle;
	TextView tvCoursProf;
	ImageView ivCoinHautGauche;
	ImageView ivCoinHautDroite;
	ImageView ivCoinBasGauche;
	ImageView ivCoinBasDroite;
//	TextView tvCoursHeure;
//	TextView tvCoursDate;
//	TextView tvCoursDuree;
//	TextView tvCoursMatiere;
//	TextView tvCoursSalle;
//	TextView tvCoursProf;
	LayoutInflater inflater;
	SparseBooleanArray displayInfo;
	//LinearLayout llMaJournee;
	boolean brightAllowed = false;

	public CoursWCAdapter(Context context, List<Cours> lstCours, SparseBooleanArray displayInfo, boolean brightAllowed) {//, CheckBox essaiCheckBox) {
		inflater = LayoutInflater.from(context);
		this.displayInfo = displayInfo;
		this.lstCours = lstCours;
		this.brightAllowed = brightAllowed;
	}
	public int getCount() {
		return lstCours.size();
	}
	public Object getItem(int position) {
		return lstCours.get(position);
	}
	public long getItemId(int position) {
		return position;
	}
	
//	private class ViewHolder {
//	}
	
	
	public View getView(int position, View convertView, ViewGroup parent) {
	//ViewHolder holder;
		if(convertView == null) {
			//holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_wc, null);
			
			//convertView.setTag(holder);
			//convertView.setTag("WW");
			//holder.tvMaSemaine = (CheckBox)convertView.findViewById(R.id.tv_item_ww);
		} else {
			L.v("CoursWDAdapter", "" + convertView.getTag());
		//holder = (ViewHolder) convertView.getTag();
		}

		llMaJournee = (LinearLayout)convertView.findViewById(R.id.ll_item_wc);
		tvCoursHeure = (TextView)convertView.findViewById(R.id.tv_item_wc_heure);
		//tvCoursDate = (TextView)convertView.findViewById(R.id.tv_item_wc_date);
		tvCoursDuree = (TextView)convertView.findViewById(R.id.tv_item_wc_duree);
		tvCoursMatiere = (TextView)convertView.findViewById(R.id.tv_item_wc_matiere);
		tvCoursSalle = (TextView)convertView.findViewById(R.id.tv_item_wc_salle);
		tvCoursProf = (TextView)convertView.findViewById(R.id.tv_item_wc_prof);
		ivCoinHautGauche = (ImageView)convertView.findViewById(R.id.ivCoinHautGauche);
		ivCoinHautDroite = (ImageView)convertView.findViewById(R.id.ivCoinHautDroite);
		ivCoinBasGauche = (ImageView)convertView.findViewById(R.id.ivCoinBasGauche);
		ivCoinBasDroite = (ImageView)convertView.findViewById(R.id.ivCoinBasDroite);
		
		//if (lstCours.get(position) != null) {
//			llMaJournee = (LinearLayout)convertView.findViewById(R.id.ll_item_wc);
//			tvCoursHeure = (TextView)convertView.findViewById(R.id.tv_item_wc_heure);
//			tvCoursDate = (TextView)convertView.findViewById(R.id.tv_item_wc_date);
//			tvCoursDuree = (TextView)convertView.findViewById(R.id.tv_item_wc_duree);
//			tvCoursMatiere = (TextView)convertView.findViewById(R.id.tv_item_wc_matiere);
//			tvCoursSalle = (TextView)convertView.findViewById(R.id.tv_item_wc_salle);
//			tvCoursProf = (TextView)convertView.findViewById(R.id.tv_item_wc_prof);
			tvCoursHeure.setText(lstCours.get(position).getHeure());
			//tvCoursDate.setText(lstCours.get(position).getDate());
			tvCoursDuree.setText(lstCours.get(position).getDuree());
			tvCoursMatiere.setText(lstCours.get(position).getActivite());
			tvCoursSalle.setText(lstCours.get(position).getSalle());
			tvCoursProf.setText(lstCours.get(position).getFormateur());
			
			final Calendar c = Calendar.getInstance();
			
			GregorianCalendar firstHour = new GregorianCalendar(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
					Integer.parseInt(lstCours.get(position).getHeure().substring(0, lstCours.get(position).getHeure().indexOf("h"))), Integer.parseInt(lstCours.get(position).getHeure().substring(lstCours.get(position).getHeure().indexOf("h") + 1)));
			
			int minute = 0;
			if (!lstCours.get(position).getDuree().substring(lstCours.get(position).getDuree().indexOf("h") + 1).equals(""))
				minute = Integer.parseInt(lstCours.get(position).getDuree().substring(lstCours.get(position).getDuree().indexOf("h") + 1));
			GregorianCalendar lastHour = new GregorianCalendar(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
					Integer.parseInt(lstCours.get(position).getHeure().substring(0, lstCours.get(position).getHeure().indexOf("h"))) +
					Integer.parseInt(lstCours.get(position).getDuree().substring(0, lstCours.get(position).getDuree().indexOf("h"))), minute);
			
			if (c.compareTo(firstHour) >= 0 && c.compareTo(lastHour) <= 0 && brightAllowed)
				llMaJournee.setBackgroundColor(Color.argb(127, RagotAdapter.R_COLOR[0], RagotAdapter.V_COLOR[0], RagotAdapter.B_COLOR[0]));
			else
				llMaJournee.setBackgroundColor(Color.argb(127, 255, 255, 255));
	
			if (displayInfo.get(0))
				tvCoursHeure.setVisibility(View.VISIBLE);
			else
				tvCoursHeure.setVisibility(View.GONE);
//			if (displayInfo.get(1))
//				tvCoursDate.setVisibility(View.VISIBLE);
//			else
//				tvCoursDate.setVisibility(View.GONE);
			if (displayInfo.get(1))
				tvCoursDuree.setVisibility(View.VISIBLE);
			else
				tvCoursDuree.setVisibility(View.GONE);
			if (displayInfo.get(2))
				tvCoursMatiere.setVisibility(View.VISIBLE);
			else
				tvCoursMatiere.setVisibility(View.GONE);
			if (displayInfo.get(3))
				tvCoursSalle.setVisibility(View.VISIBLE);
			else
				tvCoursSalle.setVisibility(View.GONE);
			if (displayInfo.get(4))
				tvCoursProf.setVisibility(View.VISIBLE);
			else
				tvCoursProf.setVisibility(View.GONE);
			
			convertView.setTag("D");
//		}
	return convertView;
	}
}
