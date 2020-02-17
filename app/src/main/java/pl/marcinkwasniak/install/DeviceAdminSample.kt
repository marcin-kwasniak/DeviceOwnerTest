package pl.marcinkwasniak.install

import android.app.admin.DeviceAdminReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.Toast

/**
 * Created by Marcin Kwasniak on 17/02/2020
 *
 *  Set device owner: dpm set-device-owner pl.marcinkwasniak.install/.DeviceAdminSample
 *  Clear device owner: dpm remove-active-admin pl.marcinkwasniak.install/.DeviceAdminSample
 *
 */
class DeviceAdminSample : DeviceAdminReceiver() {

    override fun onEnabled(context: Context, intent: Intent) {
        showToast(context, "Enabled")
    }
    override fun onDisableRequested(context: Context, intent: Intent): CharSequence = "Disabled"

    override fun onDisabled(context: Context, intent: Intent) =
        showToast(context, "Disabled")

    override fun onPasswordChanged(context: Context,intent: Intent) =showToast(context, "Password Change")

    private fun showToast(context: Context?, msg: String?) =
        Toast.makeText(context, "DeviceAdmin: $msg", Toast.LENGTH_SHORT).show()
}