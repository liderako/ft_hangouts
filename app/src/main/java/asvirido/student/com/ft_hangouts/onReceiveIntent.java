package asvirido.student.com.ft_hangouts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

//public class onReceiveIntent extends BroadcastReceiver {
//    static final String ACTION =
//            "android.provider.Telephony.SMS_RECEIVED";
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        Bundle extras = intent.getExtras();
//
//        String message = "";
//        String body = "";
//        String address = "";
//
//        // If receive a message
//        if (extras != null) {
//            // Get content message
//            Object[] smsExtra = (Object[]) extras.get("pdus");
//
//            // Read all message
//            for (int i = 0; i < smsExtra.length; i++) {
//                SmsMessage sms = SmsMessage.createFromPdu((byte[]) smsExtra[i]);
//                // Get body
//                body = sms.getMessageBody();
//                // Get address
//                address = sms.getOriginatingAddress();
//                message += "SMS From : " + address + "\n" + body + "\n";
//            }
//            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
//        }
//    }
//}

public class onReceiveIntent extends BroadcastReceiver {

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(SMS_RECEIVED)) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                // get sms objects
                Object[] pdus = (Object[]) bundle.get("pdus");
                if (pdus.length == 0) {
                    return;
                }
                // large message might be broken into many
                SmsMessage[] messages = new SmsMessage[pdus.length];
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < pdus.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    sb.append(messages[i].getMessageBody());
                }
                String sender = messages[0].getOriginatingAddress();
                String message = sb.toString();
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                // prevent any other broadcast receivers from receiving broadcast
                // abortBroadcast();
            }
        }
    }
}