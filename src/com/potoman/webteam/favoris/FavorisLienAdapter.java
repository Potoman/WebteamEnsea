package com.potoman.webteam.favoris;

import java.util.ArrayList;
import java.util.List;

import org.example.webteam.R;
import org.example.webteam.R.id;
import org.example.webteam.R.layout;

import com.potoman.tools.L;

import android.content.Context;
import android.net.Uri;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class FavorisLienAdapter extends BaseAdapter implements OnClickListener {
	List<Uri> lstUri;
	List<CheckBox> lstCheckBox;
	TextView textView;
	ImageView imageView;
	CheckBox checkBoxView;
	LayoutInflater inflater;
	public String num;
	SparseBooleanArray stateOfItem = null;
	public FavorisLienAdapter(Context context, List<Uri> lstUri) {//, CheckBox essaiCheckBox) {
		inflater = LayoutInflater.from(context);
		this.lstUri = lstUri;
		stateOfItem = new SparseBooleanArray(lstUri.size());
		lstCheckBox = new ArrayList<CheckBox>();
		//for (int i = 0; i < lstUri.size(); i++)
			//lstCheckBox.add(null);
		actualiseSparseBooleanArray();
		num = "1";
	}
	
	public void actualiseSparseBooleanArray() {
		stateOfItem.clear();
		for (int i = 0; i < lstUri.size(); i++) {
			stateOfItem.put(i, false);
		}
	}
	
	public int getCount() {
		return lstUri.size();
	}
	public Object getItem(int position) {
		return lstUri.get(position);
	}
	public long getItemId(int position) {
		return position;
	}
	
	private class ViewHolder {
		CheckBox checkBox;
	}
	
	
	public View getView(int position, View convertView, ViewGroup parent) {
	ViewHolder holder;
		if(convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_lien, null);
			holder.checkBox = (CheckBox)convertView.findViewById(R.id.cbSupprimerLienFavoris);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.checkBox.setTag(position);
		textView = (TextView)convertView.findViewById(R.id.tvLien);
		textView.setText(lstUri.get(position).toString());
		L.v("FavorisLienAdapter", "getView : " + position);
		holder.checkBox.setChecked(stateOfItem.get(position));
		holder.checkBox.setOnClickListener(this);//setOnCheckedChangeListener(this);
	return convertView;
	}
	
	
	
//	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//		stateOfItem.put(Integer.parseInt(buttonView.getTag().toString()), isChecked);
//		L.v("FavorisLienAdapter", "Bolt" + Integer.parseInt(buttonView.getTag().toString()));
//	}
	
	@Override
	public void notifyDataSetChanged() {
		//actualiseSparseBooleanArray();
		
		super.notifyDataSetChanged();
	}
//	public void setSparseBooleanArray(int position) {
//		stateOfI
//	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		L.v("FavorisLienAdapter", "onClick, Tag : " + v.getTag().toString() + " baseline : " + v.getBaseline() + ", getTop : " + v.getTop());
		stateOfItem.put(Integer.parseInt(v.getTag().toString()), !stateOfItem.get(Integer.parseInt(v.getTag().toString())));
		notifyDataSetChanged();
	}
}
