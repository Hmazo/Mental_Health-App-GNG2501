package com.example.mentalhealth;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ExecutableService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

    }
    private  void DisplayAlertBox(Context context) {

        PendingIntent alertBox = PendingIntent.getActivity(context,0, new Intent(context,MainActivity.class),0);

        AlertDialog.Builder aBuilder = new AlertDialog.Builder(context);
        aBuilder.setTitle("Message");
        aBuilder.setMessage(msg);
        aBuilder.setPositiveButton("Accept",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int whichButton)
            {

            }});

        aBuilder.create();
        aBuilder.show();
    }
}
