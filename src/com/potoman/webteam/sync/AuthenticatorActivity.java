package com.potoman.webteam.sync;

import org.example.webteam.R;

import com.potoman.tools.L;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Groups;
import android.provider.ContactsContract.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AuthenticatorActivity extends AccountAuthenticatorActivity implements OnClickListener {
	public static final String PARAM_CONFIRMCREDENTIALS = "confirmCredentials";
    public static final String PARAM_PASSWORD = "password";
    public static final String PARAM_USERNAME = "username";
    public static final String ARG_AUTH_TYPE = "authtokenType";
 
    private static final String TAG = "AuthenticatorActivity";
	public static final String ARG_ACCOUNT_TYPE = "ACCOUNT_TYPE";
	public static final String ARG_IS_ADDING_NEW_ACCOUNT = "IS_ADDING_NEW_ACCOUNT";
 
    private AccountManager mAccountManager;
    private String mAuthTokenType;
    
    /**
     * If set we are just checking that the user knows their credentials; this
     * doesn't cause the user's password to be changed on the device.
     */
//    private Boolean mConfirmCredentials = false;
    /** Was the original caller asking for an entirely new account? */
//    protected boolean mRequestNewAccount = false;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle icicle) {
        L.v(TAG, "onCreate"); //(" + icicle + ")");
        super.onCreate(icicle);
        mAccountManager = AccountManager.get(this);
        L.v(TAG, "loading data from Intent");
        final Intent intent = getIntent();
//        mUsername = intent.getStringExtra(PARAM_USERNAME);
        mAuthTokenType = intent.getStringExtra(ARG_AUTH_TYPE);
        if (mAuthTokenType == null) {
            mAuthTokenType = Constants.AUTHTOKEN_TYPE_FULL_ACCESS;
        }
//      mRequestNewAccount = mUsername == null;
//        mConfirmCredentials = intent.getBooleanExtra(PARAM_CONFIRMCREDENTIALS, false);
 
//        L.i(TAG, "    request new: " + mRequestNewAccount);
        setContentView(R.layout.account);
        
        Button mButton = (Button) findViewById(R.id.b_account_ok);
        mButton.setOnClickListener(this);
    }

	@Override
	public void onClick(View v) {
		onAuthenticationResult(true);
	}
    
    /**
     * Called when the authentication process completes (see attemptLogin()).
     */
    public void onAuthenticationResult(boolean result) {
    	L.v(TAG, "onAuthenticationResult()");
        final Account account = new Account(Constants.USERNAME_ACCOUNT, Constants.ACCOUNT_TYPE);
     
        
        if (getIntent().getBooleanExtra(ARG_IS_ADDING_NEW_ACCOUNT, false)) {
            L.v("udinic", TAG + "> finishLogin > addAccountExplicitly");
//            String authtoken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
            String authtoken = Constants.PASSWORD_ACCOUNT;
            String authtokenType = mAuthTokenType;

            // Creating the account on the device and setting the auth token we got
            // (Not setting the auth token will cause another call to the server to authenticate the user)
            mAccountManager.addAccountExplicitly(account, Constants.PASSWORD_ACCOUNT, null);
            mAccountManager.setAuthToken(account, authtokenType, authtoken);
        } else {
            L.d("udinic", TAG + "> finishLogin > setPassword");
            mAccountManager.setPassword(account, Constants.PASSWORD_ACCOUNT);
        }
        mAccountManager.addAccountExplicitly(account, Constants.PASSWORD_ACCOUNT, null);
        mAccountManager.setAuthToken(account, mAuthTokenType, Constants.PASSWORD_ACCOUNT);

        
        getContentResolver().setSyncAutomatically(account, Constants.CONTENT_AUTHORITY, true);
        getContentResolver().setIsSyncable(account, Constants.CONTENT_AUTHORITY, 1);
        
        
        ContentResolver.setSyncAutomatically(account, ContactsContract.AUTHORITY, true);
        
     // Set contacts sync for this account.
//        ContentResolver.setSyncAutomatically(account, ContactsContract.AUTHORITY, true);
        ContentProviderClient client = getContentResolver().acquireContentProviderClient(ContactsContract.AUTHORITY_URI);
        ContentValues cv = new ContentValues();
        cv.put(Settings.ACCOUNT_NAME, account.name);
        cv.put(Settings.ACCOUNT_TYPE, account.type);
        cv.put(Settings.UNGROUPED_VISIBLE, true);
        try {
        client.insert(Settings.CONTENT_URI.buildUpon().appendQueryParameter(ContactsContract.CALLER_IS_SYNCADAPTER, "true")
        .build(), cv);
        } catch (RemoteException e) {
        	L.e(TAG, "onAuthenticationResult() error");
        	e.printStackTrace();
        }
        
        
//        ContentProviderClient client = getContentResolver().acquireContentProviderClient(ContactsContract.AUTHORITY_URI);
//        ContentValues values = new ContentValues();
//        values.put(ContactsContract.Groups.ACCOUNT_NAME, account.name);
//        values.put(ContactsContract.Groups.ACCOUNT_TYPE, account.type);
//        values.put(ContactsContract.Groups.TITLE, "Test title");
////        values.put(ContactsContract.Groups.GROUP_VISIBLE, true);
//        getContentResolver().insert(ContactsContract.Groups.CONTENT_URI, values);
//        
//        values = new ContentValues();
//        values.put(ContactsContract.Settings.ACCOUNT_NAME, account.name);
//        values.put(ContactsContract.Settings.ACCOUNT_TYPE, account.type);
//        values.put(ContactsContract.Settings.UNGROUPED_VISIBLE, true);
//        getContentResolver().insert(ContactsContract.Settings.CONTENT_URI, values);
        
        final Intent intent = new Intent();
        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, Constants.USERNAME_ACCOUNT);
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, Constants.ACCOUNT_TYPE);
        intent.putExtra(AccountManager.KEY_AUTHTOKEN, Constants.PASSWORD_ACCOUNT);
        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        finish();
    }
}
