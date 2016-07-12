package com.potoman.webteam.trombinoscope;

import java.util.ArrayList;
import java.util.List;

import org.example.webteam.R;
import org.example.webteam.R.id;
import org.example.webteam.R.layout;

import com.potoman.tools.L;
import com.potoman.webteam.eleve.ContactWebteam;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TrombiResultAdapter extends BaseAdapter {

	public static final int ACTION_MAIN = 0;
	public static final int ACTION_UNMAIN = 1;
	public static final int ACTION_VISIBLE = 2;
	public static final int ACTION_UNVISIBLE = 3;
	
	public static final int ITEM_NOM_PRENOM = 0;
	public static final int ITEM_PSEUDO = 1;
	public static final int ITEM_EMAIL = 2;
	public static final int ITEM_CLASSE = 3;
	public static final int ITEM_TELEPHONE = 4; 
	public static final int ITEM_TELEPHONE_FIXE = 5;
	public static final int ITEM_TELEPHONE_PARENT = 6;
	
	public class ItemInfo {
		public int typeInfo;
		public String infoProfil;
		public int indexListProfil;
		
		public ItemInfo(int typeInfo, String infoProfil,int indexListProfil) {
			this.typeInfo = typeInfo;
			this.infoProfil = infoProfil;
			this.indexListProfil = indexListProfil;
		}
	}
	
	public List<ContactWebteam> lstProfil;
	public List<ItemInfo> lstInfoProfil;
	TextView textViewPseudo;
	LinearLayout ll;
	LayoutInflater inflater;

	public TrombiResultAdapter(Context context, List<ContactWebteam> lstProfil) {//, CheckBox essaiCheckBox) {
		inflater = LayoutInflater.from(context);
		this.lstProfil = lstProfil;
		lstInfoProfil = new ArrayList<ItemInfo>();
		//param = new ArrayList<Integer>();
		synchNewProfil();
		//checkBoxView = essaiCheckBox;
	}
	
	public void synchNewProfil() {
		lstInfoProfil.clear();
		for (int i = 0; i < lstProfil.size(); i++)
			lstInfoProfil.add(new ItemInfo(ITEM_NOM_PRENOM, lstProfil.get(i).getPrenom() + " " + lstProfil.get(i).getNom(), i));
		notifyDataSetChanged();
	}
	
	public int getCount() {
		return lstInfoProfil.size();
	}

	public Object getItem(int position) {
		return lstInfoProfil.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	private class ViewHolder {
		//LinearLayout ll;
		//TextView tvPseudo;
		TextView tvName;
		TextView tvData;
		ImageView ivCoinHautGauche;
		ImageView ivCoinBasGauche;
		ImageView ivCoinHautDroite;
		ImageView ivCoinBasDroite;
		ImageView ivFlecheBas;
		ImageView ivFlecheHaut;
		LinearLayout llCorpItem;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.itemfiche, null);
			//holder.ll = (LinearLayout)convertView.findViewById(R.id.llCorpRagot);
			//holder.tvPseudo = (TextView)convertView.findViewById(R.id.tvPseudo);
			holder.tvName = (TextView)convertView.findViewById(R.id.tvName);
			holder.tvData = (TextView)convertView.findViewById(R.id.tvData);
			
			/*holder.tvTime = (TextView)convertView.findViewById(R.id.tvTime);
			holder.tvDate = (TextView)convertView.findViewById(R.id.tvDate);*/
			holder.ivCoinHautGauche = (ImageView)convertView.findViewById(R.id.ivCoinHautGauche);
			holder.ivCoinHautDroite = (ImageView)convertView.findViewById(R.id.ivCoinHautDroite);
			holder.ivCoinBasGauche = (ImageView)convertView.findViewById(R.id.ivCoinBasGauche);
			holder.ivCoinBasDroite = (ImageView)convertView.findViewById(R.id.ivCoinBasDroite);
			holder.ivFlecheBas = (ImageView)convertView.findViewById(R.id.ivFlecheBas);
			holder.ivFlecheHaut = (ImageView)convertView.findViewById(R.id.ivFlecheHaut);
			holder.llCorpItem = (LinearLayout)convertView.findViewById(R.id.llCorpItem);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
	
		switch (lstInfoProfil.get(position).typeInfo) {
			case ITEM_NOM_PRENOM:
				holder.tvName.setVisibility(View.GONE);
				holder.ivFlecheHaut.setVisibility(View.GONE);
				if (lstInfoProfil.size() == position + 1) {
					//On se trouve à la fin, il est ferm�...
					holder.ivCoinHautGauche.setVisibility(View.VISIBLE);
					holder.ivCoinHautDroite.setVisibility(View.VISIBLE);
					holder.ivCoinBasGauche.setVisibility(View.VISIBLE);
					holder.ivCoinBasDroite.setVisibility(View.VISIBLE);
					holder.llCorpItem.setBackgroundColor(Color.argb(0, 255, 255, 255));
					holder.ivFlecheBas.setVisibility(View.VISIBLE);
				}
				else {
				//Il n'est pas à la fin...
					if (lstInfoProfil.get(position + 1).typeInfo == ITEM_NOM_PRENOM) {
						//On n'est pas ouvert...
						holder.ivCoinHautGauche.setVisibility(View.VISIBLE);
						holder.ivCoinHautDroite.setVisibility(View.VISIBLE);
						holder.ivCoinBasGauche.setVisibility(View.VISIBLE);
						holder.ivCoinBasDroite.setVisibility(View.VISIBLE);
						holder.llCorpItem.setBackgroundColor(Color.argb(0, 255, 255, 255));
						holder.ivFlecheBas.setVisibility(View.VISIBLE);
					}
					else {
						//On est ouvert...
						holder.ivCoinHautGauche.setVisibility(View.VISIBLE);
						holder.ivCoinHautDroite.setVisibility(View.VISIBLE);
						holder.ivCoinBasGauche.setVisibility(View.GONE);
						holder.ivCoinBasDroite.setVisibility(View.GONE);
						holder.llCorpItem.setBackgroundColor(Color.argb(127, 238, 95, 156));
						holder.ivFlecheBas.setVisibility(View.GONE);
					}
				}
				break;
			case ITEM_PSEUDO:
				holder.tvName.setText("Pseudo :");
				holder.llCorpItem.setBackgroundColor(Color.argb(127, 238, 95, 156));
				holder.ivCoinHautGauche.setVisibility(View.GONE);
				holder.ivCoinHautDroite.setVisibility(View.GONE);
				holder.ivCoinBasGauche.setVisibility(View.GONE);
				holder.ivCoinBasDroite.setVisibility(View.GONE);
				holder.ivFlecheBas.setVisibility(View.GONE);
				holder.ivFlecheHaut.setVisibility(View.GONE);
				holder.tvName.setVisibility(View.VISIBLE);
				break;
			case ITEM_EMAIL:
				holder.tvName.setText("Email :");
				holder.llCorpItem.setBackgroundColor(Color.argb(127, 238, 95, 156));
				holder.ivCoinHautGauche.setVisibility(View.GONE);
				holder.ivCoinHautDroite.setVisibility(View.GONE);
				holder.ivCoinBasGauche.setVisibility(View.GONE);
				holder.ivCoinBasDroite.setVisibility(View.GONE);
				holder.ivFlecheBas.setVisibility(View.GONE);
				holder.ivFlecheHaut.setVisibility(View.GONE);
				holder.tvName.setVisibility(View.VISIBLE);
				break;
			case ITEM_CLASSE:
				holder.tvName.setText("Classe :");
				holder.llCorpItem.setBackgroundColor(Color.argb(127, 238, 95, 156));
				holder.ivCoinHautGauche.setVisibility(View.GONE);
				holder.ivCoinHautDroite.setVisibility(View.GONE);
				holder.ivCoinBasGauche.setVisibility(View.GONE);
				holder.ivCoinBasDroite.setVisibility(View.GONE);
				holder.ivFlecheBas.setVisibility(View.GONE);
				holder.ivFlecheHaut.setVisibility(View.GONE);
				holder.tvName.setVisibility(View.VISIBLE);
				break;
			case ITEM_TELEPHONE:
				holder.tvName.setText("Tél :");
				holder.llCorpItem.setBackgroundColor(Color.argb(127, 238, 95, 156));
				holder.ivCoinHautGauche.setVisibility(View.GONE);
				holder.ivCoinHautDroite.setVisibility(View.GONE);
				holder.ivCoinBasGauche.setVisibility(View.GONE);
				holder.ivCoinBasDroite.setVisibility(View.GONE);
				holder.ivFlecheBas.setVisibility(View.GONE);
				holder.ivFlecheHaut.setVisibility(View.GONE);
				holder.tvName.setVisibility(View.VISIBLE);
				break;
			case ITEM_TELEPHONE_FIXE:
				holder.tvName.setText("Tél fixe :");
				holder.llCorpItem.setBackgroundColor(Color.argb(127, 238, 95, 156));
				holder.ivCoinHautGauche.setVisibility(View.GONE);
				holder.ivCoinHautDroite.setVisibility(View.GONE);
				holder.ivCoinBasGauche.setVisibility(View.GONE);
				holder.ivCoinBasDroite.setVisibility(View.GONE);
				holder.ivFlecheBas.setVisibility(View.GONE);
				holder.ivFlecheHaut.setVisibility(View.GONE);
				holder.tvName.setVisibility(View.VISIBLE);
				break;
			case ITEM_TELEPHONE_PARENT:
				holder.tvName.setText("Tél parent :");
				holder.llCorpItem.setBackgroundColor(Color.argb(127, 238, 95, 156));
				holder.ivCoinHautGauche.setVisibility(View.GONE);
				holder.ivCoinHautDroite.setVisibility(View.GONE);
				holder.ivCoinBasGauche.setVisibility(View.VISIBLE);
				holder.ivCoinBasDroite.setVisibility(View.VISIBLE);
				holder.ivFlecheBas.setVisibility(View.GONE);
				holder.ivFlecheHaut.setVisibility(View.VISIBLE);
				holder.tvName.setVisibility(View.VISIBLE);
				break;
			default :
				break;	
		};
		holder.tvData.setText(lstInfoProfil.get(position).infoProfil);
    	return convertView;
	}

	//On affiche ou pas le profil que l'on a cliqu� :
	public void switchOpenClose(int index) {
		//this.openIfTrue.append(index, !this.openIfTrue.get(index));
			L.v("TrombiResultAdapter", "index onclick : " + index);
			
		switch (lstInfoProfil.get(index).typeInfo){
			case  ITEM_NOM_PRENOM:
				if (lstInfoProfil.size() - 1 == index)
					ouvreItem(index);
				else
					if (lstInfoProfil.get(index + 1).typeInfo == ITEM_NOM_PRENOM)
						ouvreItem(index);
					else
						fermeItem(index + 6);
				break;
			case ITEM_PSEUDO:
				fermeItem(index + 5);
				break;
			case ITEM_EMAIL:
				fermeItem(index + 4);
				break;
			case ITEM_CLASSE:
				fermeItem(index + 3);
				break;
			case ITEM_TELEPHONE:
				fermeItem(index + 2);
				break;
			case ITEM_TELEPHONE_FIXE:
				fermeItem(index + 1);
				break;
			case ITEM_TELEPHONE_PARENT:
				fermeItem(index);
				break;
			default:
				break;
		}
	}
	
	public void ouvreItem(int index) {
		lstInfoProfil.add(index + 1, new ItemInfo(ITEM_PSEUDO, lstProfil.get(lstInfoProfil.get(index).indexListProfil).getPseudo(), lstInfoProfil.get(index).indexListProfil));
		lstInfoProfil.add(index + 2, new ItemInfo(ITEM_EMAIL, lstProfil.get(lstInfoProfil.get(index).indexListProfil).getEmail(), lstInfoProfil.get(index).indexListProfil));
		lstInfoProfil.add(index + 3, new ItemInfo(ITEM_CLASSE, lstProfil.get(lstInfoProfil.get(index).indexListProfil).getClasse(), lstInfoProfil.get(index).indexListProfil));
		lstInfoProfil.add(index + 4, new ItemInfo(ITEM_TELEPHONE, lstProfil.get(lstInfoProfil.get(index).indexListProfil).getTelephone(), lstInfoProfil.get(index).indexListProfil));
		lstInfoProfil.add(index + 5, new ItemInfo(ITEM_TELEPHONE_FIXE, lstProfil.get(lstInfoProfil.get(index).indexListProfil).getTelephoneFixe(), lstInfoProfil.get(index).indexListProfil));
		lstInfoProfil.add(index + 6, new ItemInfo(ITEM_TELEPHONE_PARENT, lstProfil.get(lstInfoProfil.get(index).indexListProfil).getTelephoneParent(), lstInfoProfil.get(index).indexListProfil));
		notifyDataSetChanged();
	}
	
	public void fermeItem(int index) {
		lstInfoProfil.remove(index - 5);
		lstInfoProfil.remove(index - 5);
		lstInfoProfil.remove(index - 5);
		lstInfoProfil.remove(index - 5);
		lstInfoProfil.remove(index - 5);
		lstInfoProfil.remove(index - 5);
		notifyDataSetChanged();
	}
}
