package com.potoman.webteam.caligula;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.example.webteam.R;
import org.example.webteam.R.id;
import org.example.webteam.R.layout;

import com.potoman.tools.L;
import com.potoman.webteam.ragot.RagotAdapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CoursWDAdapter extends BaseAdapter {
	
	List<Jour> lstJour;
	TextView tvMesJours;
	TextView tvNbrCours;
	LayoutInflater inflater;
	LinearLayout llMaSemaine;
	boolean brightAllowed = false;
	int jourActuel = 0;

	public int getJourActuel() { return jourActuel;};
	
	public boolean isBrightAllowed() { return brightAllowed;};
	
	public CoursWDAdapter(Context context, List<Jour> lstJour, boolean brightAllowed) {//, CheckBox essaiCheckBox) {
		inflater = LayoutInflater.from(context);
		this.lstJour = lstJour;
		this.brightAllowed = brightAllowed;
		
		final Calendar c = Calendar.getInstance();
		final GregorianCalendar myGC = new GregorianCalendar(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
		switch (myGC.get(GregorianCalendar.DAY_OF_WEEK)) {
		case Calendar.MONDAY:
			jourActuel = 0;
			break;
		case Calendar.TUESDAY:	//Dimanche
			jourActuel = 1;
			break;
		case Calendar.WEDNESDAY:
			jourActuel = 2;
			break;
		case Calendar.THURSDAY://Mardi
			jourActuel = 3;
			break;
		case Calendar.FRIDAY:
			jourActuel = 4;
			break;
		case Calendar.SATURDAY: //Jeudi
			jourActuel = 5;
			break;
		case Calendar.SUNDAY:
			jourActuel = 6;
			break;
		}
	}
	public int getCount() {
		return lstJour.size();
	}
	public Object getItem(int position) {
		return lstJour.get(position);
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
			convertView = inflater.inflate(R.layout.item_wd, null);
			
			//convertView.setTag("WW");
			//holder.tvMaSemaine = (CheckBox)convertView.findViewById(R.id.tv_item_ww);
		} else {
			L.v("CoursWDAdapter", "" + convertView.getTag());
		//holder = (ViewHolder) convertView.getTag();
		}
		llMaSemaine = (LinearLayout)convertView.findViewById(R.id.ll_item_wd);
		
		if (lstJour.get(position).getJour() == jourActuel && brightAllowed)
			llMaSemaine.setBackgroundColor(Color.argb(127, RagotAdapter.R_COLOR[0], RagotAdapter.V_COLOR[0], RagotAdapter.B_COLOR[0]));
		else
			llMaSemaine.setBackgroundColor(Color.argb(127, 0, 0, 0));
		tvMesJours = (TextView)convertView.findViewById(R.id.tv_item_wd);
		tvMesJours.setText(lstJour.get(position).toString());
		tvNbrCours = (TextView)convertView.findViewById(R.id.tv_item_nbr_cours);
//		int nbrCours = 0;
//		Cours ptr = null;
//		for (int i = 0; i < lstJour.get(position).getMaListeDeCours().size(); i++) {
//			if (ptr == null && lstJour.get(position).getMaListeDeCours().get(i) != null) {
//				ptr = lstJour.get(position).getMaListeDeCours().get(i);
//				nbrCours++;
//			}
//			if (lstJour.get(position).getMaListeDeCours().get(i) != null && ptr != null && ptr != lstJour.get(position).getMaListeDeCours().get(i)) {
//				ptr = lstJour.get(position).getMaListeDeCours().get(i);
//				nbrCours++;
//			}
//		}
//		tvNbrCours.setText(nbrCours + " cours");
		tvNbrCours.setText(lstJour.get(position).getMaListeDeCours().size() + " cours");
		convertView.setTag("WD");
	return convertView;
	}
}
