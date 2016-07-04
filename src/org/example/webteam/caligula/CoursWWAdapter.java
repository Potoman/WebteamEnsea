package org.example.webteam.caligula;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.webteam.R;
import org.example.webteam.R.id;
import org.example.webteam.R.layout;
import org.example.webteam.ragot.RagotAdapter;

import potoman.tools.L;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class CoursWWAdapter extends BaseAdapter {
	
	List<Semaine> lstSemaine;
	TextView tvMaSemaine;
	TextView tvNbrJour;
	LinearLayout llMaSemaine;
	LayoutInflater inflater;
	int semaineActuelle = 0;
	private ListView list = null;

	public int getSemaineActuelle() { return semaineActuelle;}
	
	public CoursWWAdapter(Context context, List<Semaine> lstSemaine, int semaineActuelle, ListView list) {//, CheckBox essaiCheckBox) {
		inflater = LayoutInflater.from(context);
		this.lstSemaine = lstSemaine;
		this.semaineActuelle = semaineActuelle;
		this.list = list;
	}
	public int getCount() {
		return lstSemaine.size();
	}
	public Object getItem(int position) {
		return lstSemaine.get(position);
	}
	public long getItemId(int position) {
		return position;
	}
	
//	private Map<Integer, Integer> mapPositionToHeigth = new HashMap<Integer, Integer>();
	
	public View getView(int position, View convertView, ViewGroup parent) {
	//ViewHolder holder;
		if(convertView == null) {
			//holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_ww, null);
			
			//convertView.setTag("WW");
			//holder.tvMaSemaine = (CheckBox)convertView.findViewById(R.id.tv_item_ww);
		} else {	 
			L.v("CoursWWAdapter", "" + convertView.getTag());
		//holder = (ViewHolder) convertView.getTag();
		}
		llMaSemaine = (LinearLayout)convertView.findViewById(R.id.ll_item_ww);
		if (semaineActuelle == position) {
			llMaSemaine.setBackgroundColor(Color.argb(127, RagotAdapter.R_COLOR[0], RagotAdapter.V_COLOR[0], RagotAdapter.B_COLOR[0]));
		}
		else
			llMaSemaine.setBackgroundColor(Color.argb(127, 0, 0, 0));
		tvMaSemaine = (TextView)convertView.findViewById(R.id.tv_item_ww);
		tvMaSemaine.setText("Semaine " + lstSemaine.get(position).getNumero());
		tvNbrJour = (TextView)convertView.findViewById(R.id.tv_item_nbr_jour);
		if (lstSemaine.get(position).getMalisteDeJour().size() == 1)
			tvNbrJour.setText("1 jour de cours");
		else
			tvNbrJour.setText(lstSemaine.get(position).getMalisteDeJour().size() + " jours de cours");
		//holder.checkBox.setOnCheckedChangeListener(this);
		convertView.setTag("WW");
//		L.w("CoursWWAdapter", "heigth mesure = " + convertView.getMeasuredHeight());
//		L.w("CoursWWAdapter", "heigth = " + convertView.getHeight());
//		if (convertView.getMeasuredHeight() > 0) {
//			mapPositionToHeigth.put(position, convertView.getMeasuredHeight());
//		}
	return convertView;
	}
	
	public void clear() {
		lstSemaine.clear();
	}

//	public void scrollTo(int itemToScroll) {
//		L.v("CoursWWAdapter", "itemToScroll = " + itemToScroll);
//		for (int i = 0; i <= itemToScroll; i++) {
//			do {
//				Integer scrollY = mapPositionToHeigth.get(i);
//				if (scrollY != null) {
//					L.v("CoursWWAdapter", "i = " + i + ", scroll = " + scrollY);
//					list.scrollBy(0, scrollY);
//					break;
//				}
//			}
//			while (true);
//		}
//	}
}
