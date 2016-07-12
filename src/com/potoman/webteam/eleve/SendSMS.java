package com.potoman.webteam.eleve;

import org.example.webteam.R;
import org.example.webteam.R.array;
import org.example.webteam.R.id;
import org.example.webteam.R.layout;

import com.potoman.webteam.constant.Webteam;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SendSMS extends Activity {

	TextView tvTo;
	Button btnSendSMS;
    EditText txtMessage;
    ProgressDialog myProgressDialog;
	
 
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_sms);   
	    this.setTitle("Webteam > Envoie de SMS");     
 
	    
        btnSendSMS = (Button) findViewById(R.id.btnSendSMS);
        txtMessage = (EditText) findViewById(R.id.txtMessage);
        tvTo = (TextView) findViewById(R.id.tvTo);
        tvTo.setText("Message � " + getIntent().getStringExtra("nameDestinataire"));
        
        btnSendSMS.setOnClickListener(new OnClickListener() 
        {
            public void onClick(View v) 
            {                
            	Intent myIntent = getIntent();
                String phoneNo = myIntent.getStringExtra("numPhone");
                String message = txtMessage.getText().toString();                 
                if (message.length()>0) {
                	
                	myProgressDialog = new ProgressDialog(SendSMS.this);
                	myProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                	myProgressDialog.setTitle(getResources().getStringArray(R.array.waitingForSendingSMS)[0]);
    				myProgressDialog.setMessage(Webteam.getPhraseDAmbiance());
                	myProgressDialog.setCancelable(true);
                	myProgressDialog.show();
                    sendSMS(phoneNo, message);
                }
                else
                    Toast.makeText(getBaseContext(), "Super marrant ton message vide dit moi !", Toast.LENGTH_SHORT).show();
            }

        });
    }
    
    private void sendSMS(String phoneNumber, String message)
    {        
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";
 
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
            new Intent(SENT), 0);
 
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
            new Intent(DELIVERED), 0);
 
        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                    	myProgressDialog.incrementProgressBy(50);
                    	//Le sms a bien �tait envoy�.
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Un truc général a fait planter le truc...", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "Pas de service !", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

        }, new IntentFilter(SENT));
 
        //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        myProgressDialog.incrementProgressBy(50);
                    	Toast.makeText(getBaseContext(), "SMS délivré 8)", 
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS non délivré :'(", 
                                Toast.LENGTH_SHORT).show();
                        break;           
                }
                finish();
            }
        }, new IntentFilter(DELIVERED));        
 
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);        
    }

}


