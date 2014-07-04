package net.virifi.android.docomomailnotifier;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class SendNotificationReceiver extends BroadcastReceiver {
    public SendNotificationReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        SettingStore store = new SettingStore(context);
        boolean notifies = store.loadNotifiyWhenReceiveIntent();
        if (notifies) {
            Toast.makeText(context, "Intentを受信しました", Toast.LENGTH_SHORT).show();
        }

        String address = store.loadAddress();
        if (address == null) {
            showAddressNotSetNotification(context);
            return;
        }

        DocomoMailNotifier notifier = new DocomoMailNotifier(context);
        notifier.sendNotification(address);
    }

    private void showAddressNotSetNotification(Context context) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(context, MyActivity.class);
        PendingIntent pending = PendingIntent.getActivity(context, 0, intent, 0);
        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setTicker("アドレス未設定")
                .setContentTitle("アドレス未設定")
                .setContentText("ドコモメールへ通知を送信するにはアドレスを設定する必要があります")
                .setContentIntent(pending)
                .setAutoCancel(true)
                .build();
        manager.notify(1, notification);
    }
}
