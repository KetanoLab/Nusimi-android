package com.ketanolab.simidic;

import static com.ketanolab.simidic.CommonUtilities.SENDER_ID;
import static com.ketanolab.simidic.CommonUtilities.displayMessage;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "GCMIntentService";

	public GCMIntentService() {
		super(SENDER_ID);
	}

	/**
	 * Method called on device registered
	 **/
	@Override
	protected void onRegistered(Context context, String registrationId) {
		Log.i(TAG, "Device registered: regId = " + registrationId);
		displayMessage(context, "Your device registred with GCM");
		ServerUtilities.register(context, registrationId);
	}

	/**
	 * Method called on Receiving a new message
	 * */
	 public static String elir_n(String eli) {
	        String aux = "";
	        for (int i = 0; i < eli.length() - 4; i++) {
	            if (eli.substring(i, i + 4).equals(" r n")) {
	                aux += " ";
	                i = i + 3;
	                //             System.out.println("1");
	            } else {
	                aux += eli.charAt(i);
	            }
	        }
	        String temp = "";
	        try {

	            temp = eli.substring(eli.length() - 4, eli.length());
	            if (temp.equals(" r n")) {
	                temp = "";
	            }
	        } catch (Exception e) {
	            temp = "";
	        }
	        aux = aux + temp;
	        return aux;
	    }
	public static String elin(String eli) {
        String aux = "";
        for (int i = 0; i < eli.length() - 2; i++) {
            if (eli.substring(i, i + 2).equals("\\n")) {
                aux += " ";
                i = i + 1;
                //             System.out.println("1");
            } else {
                aux += eli.charAt(i);
            }
        }

        String temp = "";
        try {

            temp = eli.substring(eli.length() - 2, eli.length());
            if (temp.equals("\\n")) {
                temp = "";
            }
        } catch (Exception e) {
            temp = "";
        }
        aux = aux + temp;
        return aux;
    }

    public static String elir(String eli) {
        String aux = "";
        for (int i = 0; i < eli.length() - 2; i++) {
            if (eli.substring(i, i + 2).equals("\\r")) {
                aux += " ";
                i = i + 1;
                //             System.out.println("1");
            } else {
                aux += eli.charAt(i);
            }
        }

        String temp = "";
        try {

            temp = eli.substring(eli.length() - 2, eli.length());
            if (temp.equals("\\r")) {
                temp = "";
            }
        } catch (Exception e) {
            temp = "";
        }
        aux = aux + temp;
        return aux;
    }

    public static String elirn(String eli) {
        String aux = "";
        for (int i = 0; i < eli.length() - 4; i++) {
            if (eli.substring(i, i + 4).equals("\\r\\n")) {
                aux += " ";
                i = i + 3;
                //             System.out.println("1");
            } else {
                aux += eli.charAt(i);
            }
        }
        String temp = "";
        try {

            temp = eli.substring(eli.length() - 4, eli.length());
            if (temp.equals("\\r\\n")) {
                temp = "";
            }
        } catch (Exception e) {
            temp = "";
        }
        aux = aux + temp;
        return aux;
    }
	// recibir mensaje push
	@Override
	protected void onMessage(Context context, Intent intent) {
		Log.i(TAG, "Received message");
		String message = intent.getExtras().getString("SimiDic");
		// method is detected json 
		//{"SimiDic":"sadasdasdasd"}
		//
		String aux="";
		  while(message.indexOf("SimiDic") >= 0) {
				
				if (message.substring(message.length() - 6, message.length() - 2)
						.equals("\\r\\n")) {

					aux = "";
					for (int i = 0; i < message.length() - 4; i++) {
						if (message.substring(i, i + 4).equals("\\r\\n")) {
							aux += " ";
							i = i + 3;
							//System.out.println("1");
						} else {
							aux += message.charAt(i);
						}

					}
					message = aux;
					message = message.substring(12, message.length() - 2);
				}

				else {

					message = message.substring(12, message.length() - 2);
				}

				
			}
		message = elirn(message); 
		message = elir(message); 
		message = elin(message); 
		message = elir_n(message);
		
		displayMessage(context, message);
		// notifies user
		generateNotification(context, message);
	}

	/**
	 * Method called on receiving a deleted message
	 * */
	@Override
	protected void onDeletedMessages(Context context, int total) {
		Log.i(TAG, "Received deleted messages notification");
		String message = getString(R.string.gcm_deleted, total);
		displayMessage(context, message);
		// notifies user
		generateNotification(context, message);
	}

	/**
	 * Method called on Error
	 * */
	@Override
	public void onError(Context context, String errorId) {
		Log.i(TAG, "Received error: " + errorId);
		displayMessage(context, getString(R.string.gcm_error, errorId));
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		// log message
		Log.i(TAG, "Received recoverable error: " + errorId);
		displayMessage(context,
				getString(R.string.gcm_recoverable_error, errorId));
		return super.onRecoverableError(context, errorId);
	}

	/**
	 * Issues a notification to inform the user that server has sent a message.
	 * Method deprecated
	 */
	private static void generateNotification(Context context, String message) {
		int icon = R.drawable.ic_launcher;
		long when = System.currentTimeMillis();
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(icon, message, when);

		String title = context.getString(R.string.app_name);

		Intent notificationIntent = new Intent(context, MainActivity.class);
		// set intent so it does not start a new activity
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent intent = PendingIntent.getActivity(context, 0,
				notificationIntent, 0);
		notification.setLatestEventInfo(context, title, message, intent);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		// Play default notification sound
		notification.defaults |= Notification.DEFAULT_SOUND;

		// notification.sound = Uri.parse("android.resource://" +
		// context.getPackageName() + "your_sound_file_name.mp3");

		// Vibrate if vibrate is enabled
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		notificationManager.notify(0, notification);

	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
		// TODO Auto-generated method stub

	}

}
