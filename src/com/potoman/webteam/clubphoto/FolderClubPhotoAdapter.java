package org.example.webteam.clubphoto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.webteam.R;

import potoman.webteam.bdd.data.FolderClubPhoto;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FolderClubPhotoAdapter extends BaseAdapter {

	private final List<Integer> listFolderDisplay = new ArrayList<Integer>();
	
	private final Map<Integer, FolderClubPhoto> mapFolder = new HashMap<Integer, FolderClubPhoto>();
	
	public FolderClubPhotoAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		initRoot();
	}

	private void initRoot() {
		for (FolderClubPhoto folder : mapFolder.values()) {
			if (folder.getCategoryContainer() == null) {
				listFolderDisplay.add(folder.getCategory());
			}
		}
	}
	
	@Override
	public int getCount() {
		return listFolderDisplay.size();
	}

	@Override
	public FolderClubPhoto getItem(int position) {
		return mapFolder.get(listFolderDisplay.get(position));
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	LayoutInflater inflater;
	
	private class ViewHolder {
		TextView tvTitreCategorie;
		TextView tvCountPhotoInFolder;
		TextView tvCountSubFolder;
		TextView tvCountPhotoInSubFolder;
		LinearLayout llCorpItem;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_folder_club_photo, null);
			holder.llCorpItem = (LinearLayout)convertView.findViewById(R.id.ll_item_categorie);
			//holder.tvPseudo = (TextView)convertView.findViewById(R.id.tvPseudo);
			holder.tvTitreCategorie = (TextView) convertView.findViewById(R.id.tv_titre_categorie);
			holder.tvCountPhotoInFolder = (TextView) convertView.findViewById(R.id.tv_count_photo_in_folder);
			holder.tvCountSubFolder = (TextView) convertView.findViewById(R.id.tv_count_sub_folder);
			holder.tvCountPhotoInSubFolder = (TextView) convertView.findViewById(R.id.tv_count_photo_in_sub_folder);
			

			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		FolderClubPhoto folder = (FolderClubPhoto) getItem(position);
		
//		holder.llCorpItem.setPadding(folder.getDepth() * 5, 0, 0, 0);
		holder.tvTitreCategorie.setText(folder.getTitle());
		holder.tvTitreCategorie.setPadding(folder.getDepth() * 10, 0, 0, 0);
		holder.tvCountPhotoInFolder.setText("nbr photo = " + folder.getCountPhotoInFolder());
		holder.tvCountPhotoInFolder.setPadding(folder.getDepth() * 10, 0, 0, 0);
		holder.tvCountSubFolder.setText("nbr Folder = " + folder.getCountSubFolder());
		holder.tvCountSubFolder.setPadding(folder.getDepth() * 10, 0, 0, 0);
		holder.tvCountPhotoInSubFolder.setText("nbr PhotoInFolder = " + folder.getCountPhotoInSubFolder());
		holder.tvCountPhotoInSubFolder.setPadding(folder.getDepth() * 10, 0, 0, 0);
    	return convertView;
	}

	@Override
	public void notifyDataSetChanged() {
		if (listFolderDisplay.isEmpty()) {
			initRoot();
		}
		
		super.notifyDataSetChanged();
	}
	
	public void openFolder(Integer idCategory) {
		int indexToInsert = listFolderDisplay.indexOf(idCategory);
		indexToInsert++;
		for (FolderClubPhoto folder : mapFolder.values()) { 
			if (idCategory.equals(folder.getCategoryContainer())) {
				listFolderDisplay.add(indexToInsert, folder.getCategory());
				indexToInsert++;
			}
		}
		notifyDataSetChanged();
	}
	
	public void closeFolder(int position) {
		closeFolderRecurrent(position);
		notifyDataSetChanged();
	}
	
	private void closeFolderRecurrent(int position) {
		Integer idCategoryParent = mapFolder.get(listFolderDisplay.get(position)).getCategory();
		position++;
		for (int indexToRemove = position; indexToRemove < listFolderDisplay.size(); indexToRemove++) {
			Integer categoryContainerToRemove = mapFolder.get(listFolderDisplay.get(indexToRemove)).getCategoryContainer();
			if (idCategoryParent.equals(categoryContainerToRemove)) {
				if (isOpen(indexToRemove)) {
					closeFolderRecurrent(indexToRemove);
				}
				else {
					listFolderDisplay.remove(indexToRemove);
					indexToRemove--;
				}
			}
			else {
				return;
			}
		}
	}
	
	public boolean isOpen(int position) {
		FolderClubPhoto folder = mapFolder.get(listFolderDisplay.get(position));
		
		position++;
		if (listFolderDisplay.size() > position) {
			Integer catCont = mapFolder.get(listFolderDisplay.get(position)).getCategoryContainer();
			if (catCont != null && catCont.equals(folder.getCategory())) {
				/*
				 * On se trouve dans le cas où le dossier est ouvert. 
				 */
				return true;
			}
		}
		return false;
	}

	public void putAll(Map<Integer, FolderClubPhoto> mapFolder) {
		this.mapFolder.clear();
		listFolderDisplay.clear();
		this.mapFolder.putAll(mapFolder);
		notifyDataSetChanged();
	}

	public void clear() {
		this.mapFolder.clear();
		listFolderDisplay.clear();
		notifyDataSetChanged();
	}
}
