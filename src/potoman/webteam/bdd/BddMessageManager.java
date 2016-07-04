package potoman.webteam.bdd;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.example.webteam.boitemanager.boite.BoiteDeMessage;
import org.example.webteam.boitemanager.boite.messageprive.BoiteDeMessagePrive;
import org.example.webteam.boitemanager.message.Message;
import org.example.webteam.boitemanager.message.messageprive.MessagePrive;
import org.example.webteam.loggin.Root;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public abstract class BddMessageManager {

	public abstract void open();
 
	public abstract void close();
 
	public abstract SQLiteDatabase getBDD();
 
	public abstract void insert(Message message);
 
	public abstract Message getMessage(int id, String nomBoite);
	
	public abstract void deleteBoite(String nomBoite);
	
	public abstract void deleteMessage(int id, String nomBoite);
	
	public abstract int updateMessageContenuLoad(Message message);
	
	public abstract int updateMessageState(Message message);
	
	public abstract void clear();

	public abstract void getAllMessageInBoite(String nomBoite, List<Message> maListeDeMessageBoiteDeReception);

	public abstract void getAllMessage(Map<String, List<Message>> mapListMessageByBoite);
}
