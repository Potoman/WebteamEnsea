package com.potoman.webteam.ragot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.example.webteam.R;
import org.example.webteam.R.drawable;
import org.example.webteam.R.id;
import org.example.webteam.R.layout;

import com.potoman.tools.L;
import com.potoman.tools.ObjectToDay;
import com.potoman.webteam.exception.ExceptionCaligula;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RagotAdapter extends BaseAdapter {
	public List<Ragot> lstRagot;
	public LayoutInflater inflater;
	public int firstHistory = 0;
	public RagotManager myRagots = null;
	public static int NBR_ECHELON = 8;
	public static int NBR_STEP = 1;
	public boolean ifNotAllLoad = true;
	public final Handler uiThreadCallback = new Handler();
	public boolean isPortrait = false;
	
	private ViewHolderRagot myViewHolderRagot = null;
	private ViewHolderSeparateur myViewHolderSeparateur = null;
	
	/*static int R_COLOR = 207;
	static int [] V_COLOR = {101,  91,  81,  70,  60,  50,  39,  29,  19,   8,   0};
	static int [] B_COLOR = {153, 148, 143, 137, 132, 127, 122, 116, 111, 106, 102};*/
	
	public static int [] R_COLOR = {204, 222, 240, 255, 205, 155, 105, 55, 5};
	public static int [] V_COLOR = {102, 146, 199, 255, 205, 155, 105, 55, 5};
	public static int [] B_COLOR = {152, 184, 219, 255, 205, 155, 105, 55, 5};
	
	//Si myRagots est null, on désactive la liste infinie.
	//C'est grâce à cette référence que l'on peut charger les anciens ragots via la methode getOldRagot qui utilise "ragotBdd".
	public RagotAdapter(Context context, List<Ragot> lstRagot, Ragots myRagots, boolean isPortrait) {
		inflater = LayoutInflater.from(context);
		this.lstRagot = lstRagot;
		this.firstHistory = lstRagot.get(0).getHistory();
		this.myRagots = myRagots;
		this.isPortrait = isPortrait;
	}
	public int getCount() {
		return lstRagot.size();
	}
	public Object getItem(int position) {
		return lstRagot.get(position);
	}
	public long getItemId(int position) {
		return position;
	}

	private class ViewHolder {
		ImageView ivMissRagot;
		ImageView ivCoinBasGauche;
		ImageView ivCoinBasDroite;
	}
	
	private class ViewHolderRagot extends ViewHolder {
		LinearLayout ll;
		TextView tvRagot;
		TextView tvDate;
		TextView tvTime;
		ImageView ivCoinHautGauche;
		ImageView ivCoinHautDroite;
		ImageView ivCoinBasGauche;
		ImageView ivCoinBasDroite;
		ImageView ivFavoris;
	}
	
	private class ViewHolderSeparateur extends ViewHolder {
		LinearLayout ll;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		//******************************************************CENSURE******************************************
		if (ifNotAllLoad && myRagots != null) //Si on a pas tout chargé et que l'on est autorisé à faire une liste infinie...
			if (position == lstRagot.size() - 1) {				
				new Thread() {
		       		@Override public void run() {
						uiThreadCallback.post(new Runnable() {public void run() {ifNotAllLoad = myRagots.loadOldRagot();notifyDataSetChanged();}});
		       	    }
		       	}.start();
			}
		
		
		ViewHolder holder = null;
		if (lstRagot.get(position).getSeparateur() == 0) {
			
//			if (myViewHolderRagot == null) {
//				myViewHolderRagot = new ViewHolderRagot();
//				myViewHolderRagot.ll = (LinearLayout) convertView.findViewById(R.id.llCorpRagot);
//				myViewHolderRagot.tvRagot = (TextView) convertView.findViewById(R.id.tvRagot);
//				myViewHolderRagot.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
//				myViewHolderRagot.tvDate = (TextView) convertView.findViewById(R.id.tvDate);
//				myViewHolderRagot.ivCoinHautGauche = (ImageView) convertView.findViewById(R.id.ivCoinHautGauche);
//				myViewHolderRagot.ivCoinHautDroite = (ImageView) convertView.findViewById(R.id.ivCoinHautDroite);
//				myViewHolderRagot.ivCoinBasGauche = (ImageView) convertView.findViewById(R.id.ivCoinBasGauche);
//				myViewHolderRagot.ivCoinBasDroite = (ImageView) convertView.findViewById(R.id.ivCoinBasDroite);
//				myViewHolderRagot.ivFavoris = (ImageView) convertView.findViewById(R.id.ivFavoris);
//			}
			
			holder = new ViewHolderRagot();
			convertView = inflater.inflate(R.layout.itemragot, null);
			((ViewHolderRagot) holder).ll = (LinearLayout) convertView.findViewById(R.id.llCorpRagot);
			((ViewHolderRagot) holder).tvRagot = (TextView) convertView.findViewById(R.id.tvRagot);
			((ViewHolderRagot) holder).tvTime = (TextView) convertView.findViewById(R.id.tvTime);
			((ViewHolderRagot) holder).tvDate = (TextView) convertView.findViewById(R.id.tvDate);
			((ViewHolderRagot) holder).ivCoinHautGauche = (ImageView) convertView.findViewById(R.id.ivCoinHautGauche);
			((ViewHolderRagot) holder).ivCoinHautDroite = (ImageView) convertView.findViewById(R.id.ivCoinHautDroite);
			((ViewHolderRagot) holder).ivCoinBasGauche = (ImageView) convertView.findViewById(R.id.ivCoinBasGauche);
			((ViewHolderRagot) holder).ivCoinBasDroite = (ImageView) convertView.findViewById(R.id.ivCoinBasDroite);
			((ViewHolderRagot) holder).ivFavoris = (ImageView) convertView.findViewById(R.id.ivFavoris);
		}
		else  {
			holder = new ViewHolderSeparateur();
			convertView = inflater.inflate(R.layout.item_ragot_separateur, null);
			((ViewHolderSeparateur) holder).ivMissRagot = (ImageView) convertView.findViewById(R.id.ivMissRagot);
			((ViewHolderSeparateur) holder).ivCoinBasGauche = (ImageView) convertView.findViewById(R.id.ivCoinBasGauche);
			((ViewHolderSeparateur) holder).ivCoinBasDroite = (ImageView) convertView.findViewById(R.id.ivCoinBasDroite);
		}
		convertView.setTag(holder);
			
		if (lstRagot.get(position).getSeparateur() == 0) {
			SimpleDateFormat sdfTime = new SimpleDateFormat("HH'h'mm");
	    	SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
	    	SimpleDateFormat sdfDay = new SimpleDateFormat("E");

	    	((ViewHolderRagot) holder).tvDate.setText(ObjectToDay.displayDay(sdfDay.format(new Date(lstRagot.get(position).getDate()*1000))) + " " + sdfDate.format(new Date(lstRagot.get(position).getDate()*1000)));
	    	((ViewHolderRagot) holder).tvTime.setText(sdfTime.format(new Date(lstRagot.get(position).getDate()*1000)));
			//holder.tvPseudo.setText(lstRagot.get(position).getPseudo());
	    	((ViewHolderRagot) holder).tvRagot.setText(Html.fromHtml("<b>" + lstRagot.get(position).getPseudo() + ":</b> " + lstRagot.get(position).getRagotAfterParse(), 
					new ImageGetter() {
						public Drawable getDrawable(String source) {
							Drawable bmp = null;
								if (source.equals("smile.png"))
									bmp = inflater.getContext().getResources().getDrawable(R.drawable.smile);
								if (source.equals("lien.png"))
									bmp = inflater.getContext().getResources().getDrawable(R.drawable.lien);
								if (source.equals("youtube.png"))
									bmp = inflater.getContext().getResources().getDrawable(R.drawable.youtube);
								if (source.equals("dailymotion.png"))
									bmp = inflater.getContext().getResources().getDrawable(R.drawable.dailymotion);
								if (source.equals("coeur.png"))
									bmp = inflater.getContext().getResources().getDrawable(R.drawable.coeur);
								if (source.equals("user.png"))
									bmp = inflater.getContext().getResources().getDrawable(R.drawable.user);
	
								if (source.equals("emo_im_happy.png"))
									bmp = inflater.getContext().getResources().getDrawable(R.drawable.emo_im_happy);
								if (source.equals("emo_im_laughing.png"))
									bmp = inflater.getContext().getResources().getDrawable(R.drawable.emo_im_laughing);
								if (source.equals("emo_im_money_mouth.png"))
									bmp = inflater.getContext().getResources().getDrawable(R.drawable.emo_im_money_mouth);
								if (source.equals("emo_im_angel.png"))
									bmp = inflater.getContext().getResources().getDrawable(R.drawable.emo_im_angel);
								if (source.equals("emo_im_cool.png"))
									bmp = inflater.getContext().getResources().getDrawable(R.drawable.emo_im_cool);
								if (source.equals("emo_im_tongue_sticking_out.png"))
									bmp = inflater.getContext().getResources().getDrawable(R.drawable.emo_im_tongue_sticking_out);
								if (source.equals("emo_im_winking.png"))
									bmp = inflater.getContext().getResources().getDrawable(R.drawable.emo_im_winking);
								if (source.equals("emo_im_wtf.png"))
									bmp = inflater.getContext().getResources().getDrawable(R.drawable.emo_im_wtf);
								if (source.equals("emo_im_crying.png"))
									bmp = inflater.getContext().getResources().getDrawable(R.drawable.emo_im_crying);
								if (source.equals("emo_im_sad.png"))
									bmp = inflater.getContext().getResources().getDrawable(R.drawable.emo_im_sad);
								if (source.equals("emo_im_lips_are_sealed"))
									bmp = inflater.getContext().getResources().getDrawable(R.drawable.emo_im_lips_are_sealed);
	
								if (bmp == null) {
									bmp = inflater.getContext().getResources().getDrawable(R.drawable.emo_im_lips_are_sealed);
									L.v("RagotAdapter", "|" + source + "|");
								}
							bmp.setBounds(0,0,bmp.getIntrinsicWidth(), bmp.getIntrinsicHeight());
							return bmp;
						}
					}
			,null));
			
	    	if (lstRagot.get(position).getNews().equals("*"))
	    		((ViewHolderRagot)holder).tvRagot.setTypeface(null, Typeface.BOLD);
	    	
			//***************************************************CENSURE**********************************************
			if (lstRagot.get(position).getFavoris() == 0 || myRagots == null) {
				((ViewHolderRagot)holder).ivFavoris.setVisibility(View.GONE);
				((ViewHolderRagot)holder).tvRagot.setPadding(4, 0, 4, 4);
			}
			else {
				((ViewHolderRagot)holder).ivFavoris.setVisibility(View.VISIBLE);
				((ViewHolderRagot)holder).tvRagot.setPadding(4, 0, 35, 4);
			}
			
			if (myRagots != null) {
				int ecart = (firstHistory - lstRagot.get(position).getHistory()) * NBR_STEP;
				if (ecart > NBR_ECHELON * NBR_STEP)
					ecart = NBR_ECHELON * NBR_STEP;
				((ViewHolderRagot)holder).ll.setBackgroundColor(Color.argb(127, R_COLOR[ecart], V_COLOR[ecart], B_COLOR[ecart]));
				//L.v("RagotAdapter", "pas null");
			}
			else {
				((ViewHolderRagot)holder).ll.setBackgroundColor(Color.argb(127, 255, 255, 255));
				//L.v("RagotAdapter", "null");
			}
			if (!lstRagot.get(position).getJustDate()) {
				((ViewHolderRagot)holder).tvDate.setVisibility(View.GONE);
				if (position < lstRagot.size() - 1) {
					if (lstRagot.get(position + 1).getSeparateur() == 0) {
						if (!lstRagot.get(position + 1).getJustDate()) {
							((ViewHolderRagot)holder).ivCoinHautGauche.setVisibility(View.GONE);
							((ViewHolderRagot)holder).ivCoinHautDroite.setVisibility(View.GONE);
							((ViewHolderRagot)holder).ivCoinBasGauche.setVisibility(View.GONE);
							((ViewHolderRagot)holder).ivCoinBasDroite.setVisibility(View.GONE);
						}
						else {
							((ViewHolderRagot)holder).ivCoinHautGauche.setVisibility(View.GONE);
							((ViewHolderRagot)holder).ivCoinHautDroite.setVisibility(View.GONE);
							((ViewHolderRagot)holder).ivCoinBasGauche.setVisibility(View.VISIBLE);
							((ViewHolderRagot)holder).ivCoinBasDroite.setVisibility(View.VISIBLE);
						}
					}
					else {
						((ViewHolderRagot)holder).ivCoinHautGauche.setVisibility(View.GONE);
						((ViewHolderRagot)holder).ivCoinHautDroite.setVisibility(View.GONE);
						((ViewHolderRagot)holder).ivCoinBasGauche.setVisibility(View.GONE);
						((ViewHolderRagot)holder).ivCoinBasDroite.setVisibility(View.GONE);
					}
				}
				else {
					((ViewHolderRagot)holder).ivCoinHautGauche.setVisibility(View.GONE);
					((ViewHolderRagot)holder).ivCoinHautDroite.setVisibility(View.GONE);
					((ViewHolderRagot)holder).ivCoinBasGauche.setVisibility(View.VISIBLE);
					((ViewHolderRagot)holder).ivCoinBasDroite.setVisibility(View.VISIBLE);
				}
			}
			else {
				((ViewHolderRagot)holder).tvDate.setVisibility(View.VISIBLE);
				((ViewHolderRagot)holder).ivCoinHautGauche.setVisibility(View.VISIBLE);
				((ViewHolderRagot)holder).ivCoinHautDroite.setVisibility(View.VISIBLE);
				if (position < lstRagot.size() - 1) {
					if (lstRagot.get(position + 1).getSeparateur() == 0) {
						if (lstRagot.get(position + 1).getJustDate()) {
							((ViewHolderRagot)holder).ivCoinBasGauche.setVisibility(View.VISIBLE);
							((ViewHolderRagot)holder).ivCoinBasDroite.setVisibility(View.VISIBLE);	
						}
						else {
							((ViewHolderRagot)holder).ivCoinBasGauche.setVisibility(View.GONE);
							((ViewHolderRagot)holder).ivCoinBasDroite.setVisibility(View.GONE);
						}
					}
					else {
						((ViewHolderRagot)holder).ivCoinBasGauche.setVisibility(View.GONE);
						((ViewHolderRagot)holder).ivCoinBasDroite.setVisibility(View.GONE);
					}
				}
				else {
					((ViewHolderRagot)holder).ivCoinBasGauche.setVisibility(View.VISIBLE);
					((ViewHolderRagot)holder).ivCoinBasDroite.setVisibility(View.VISIBLE);
				}
			}
		}
		else {
			if (isPortrait)
				((ViewHolderSeparateur)holder).ivMissRagot.setImageResource(R.drawable.miss_ragot);
			else
				((ViewHolderSeparateur)holder).ivMissRagot.setImageResource(R.drawable.miss_ragot_landscape);
			
			if (lstRagot.get(position + 1).getJustDate()) {
				((ViewHolderSeparateur)holder).ivCoinBasGauche.setVisibility(View.VISIBLE);
				((ViewHolderSeparateur)holder).ivCoinBasDroite.setVisibility(View.VISIBLE);
			}
			else {
				((ViewHolderSeparateur)holder).ivCoinBasGauche.setVisibility(View.GONE);
				((ViewHolderSeparateur)holder).ivCoinBasDroite.setVisibility(View.GONE);
			}
		}
		return convertView;
	}

}



