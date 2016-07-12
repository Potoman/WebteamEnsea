package com.potoman.webteam.loggin.deleteprofil;

import java.util.List;

import org.example.webteam.R;
import org.example.webteam.R.id;
import org.example.webteam.R.layout;

import com.potoman.webteam.eleve.ContactWebteam;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfilAdapter extends BaseAdapter implements OnCheckedChangeListener {
	List<ContactWebteam> lstProfil;
	TextView textView;
	ImageView imageView;
	CheckBox checkBoxView;
	LayoutInflater inflater;
	public String num;
	SparseBooleanArray stateOfItem = null;
	public ProfilAdapter(Context context, List<ContactWebteam> lstProfil) {//, CheckBox essaiCheckBox) {
		inflater = LayoutInflater.from(context);
		this.lstProfil = lstProfil;
		stateOfItem = new SparseBooleanArray(lstProfil.size());
		for (int i = 0; i < lstProfil.size(); i++) {
			stateOfItem.put(i, false);
		}
		num = "1";
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
		CheckBox checkBox;
	}
	
	
	public View getView(int position, View convertView, ViewGroup parent) {
	ViewHolder holder;
		if(convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.itemprofil, null);
			holder.checkBox = (CheckBox)convertView.findViewById(R.id.cbSupprimer);
			holder.checkBox.setTag((Object)position);
			convertView.setTag(holder);
		} else {
		holder = (ViewHolder) convertView.getTag();
		}
		textView = (TextView)convertView.findViewById(R.id.tvPseudo);
		textView.setText(lstProfil.get(position).getPseudo());
		holder.checkBox.setOnCheckedChangeListener(this);
	return convertView;
	}
	
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		stateOfItem.put(Integer.parseInt(buttonView.getTag().toString()), isChecked);
	}
}
