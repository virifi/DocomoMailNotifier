package net.virifi.android.docomomailnotifier;

import android.content.res.Resources;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnTextChanged;


public class MyActivity extends ActionBarActivity {
    @InjectView(R.id.address_edit) EditText mAddressEdit;
    @InjectView(R.id.address_save_status) TextView mSaveStatusView;
    @InjectView(R.id.notify_when_receive_intent_switch) Switch mNotifyWhenReceiveSwitch;

    private DocomoMailNotifier mNotifier;
    private SettingStore mSettingStore;
    boolean mIsAddressStored = false;
    boolean mHasAddressChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        ButterKnife.inject(this);

        mNotifier = new DocomoMailNotifier(this);
        mSettingStore = new SettingStore(this);

        restoreAddress();
        restoreNotifyWhenReceiveIntent();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.save_address_button) void onSaveAddressClick() {
        String address = mAddressEdit.getText().toString();
        if (address.isEmpty()) {
            showAddressNotInputError();
            return;
        }
        storeAddress(address);
    }

    @OnClick(R.id.test_notification_button) void onSendTestNotificationClick() {
        String address = mAddressEdit.getText().toString();
        if (address.isEmpty()) {
            showAddressNotInputError();
            return;
        }
        mNotifier.sendNotification(address);
    }

    @OnCheckedChanged(R.id.notify_when_receive_intent_switch)
    void onNotifyWhenReceiveIntentSwitchChanged(boolean isChecked) {
        mSettingStore.storeNotifiyWhenReceiveIntent(isChecked);
    }

    @OnTextChanged(R.id.address_edit) void onAddressEditTextChanged() {
        if (!mHasAddressChanged) {
            mHasAddressChanged = true;
            updateAddressSaveStatus();
        }
    }


    private void showAddressNotInputError() {
        Resources res = getResources();
        Toast.makeText(this, res.getString(R.string.address_not_input_error), Toast.LENGTH_SHORT).show();
    }

    private void restoreAddress() {
        String address = mSettingStore.loadAddress();
        if (address == null) {
            mIsAddressStored = false;
        } else {
            mIsAddressStored = true;
            mAddressEdit.setText(address);
        }
        mHasAddressChanged = false;
        updateAddressSaveStatus();
    }

    private void storeAddress(String address) {
        mSettingStore.storeAddress(address);
        mIsAddressStored = true;
        mHasAddressChanged = false;
        updateAddressSaveStatus();
    }

    private void restoreNotifyWhenReceiveIntent() {
        mNotifyWhenReceiveSwitch.setChecked(mSettingStore.loadNotifiyWhenReceiveIntent());
    }

    private void updateAddressSaveStatus() {
        String status;
        int statusColorId;

        Resources res = getResources();
        if (mHasAddressChanged) {
            status = res.getString(R.string.address_not_saved_status);
            statusColorId = res.getColor(R.color.invalid_status);
        } else {
            if (mIsAddressStored) {
                status = res.getString(R.string.address_set_status);
                statusColorId = res.getColor(R.color.valid_status);
            } else {
                status = res.getString(R.string.address_not_set_status);
                statusColorId = res.getColor(R.color.invalid_status);
            }
        }
        mSaveStatusView.setText(status);
        mSaveStatusView.setBackgroundColor(statusColorId);
    }
}
