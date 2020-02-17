package pl.marcinkwasniak.install

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInstaller
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    val TAG = "[INSTALL]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        Log.d(TAG, "onCreate: ${intent?.action}")
        fab.setOnClickListener { view ->
            install(context = this)
        }
        versionName.text = "Version name: ${BuildConfig.VERSION_NAME}\n" +
                "Version code: ${BuildConfig.VERSION_CODE}"
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ${intent?.action}")
        helloText.text = "Intent: ${intent?.action}"
    }

    /**
     * Command to set device owner in DeviceAdminSample class
     *
     * Adb output after install without device owner:
     *
     * I/PersonaServiceHelper: isCallerApprovedToInstall(uid:10394 userHandle:0)
     * I/PersonaServiceHelper: DO is not enabled. no Knox app install enforcement
     * I/PackageInstaller:  getting through the check android.content.pm.PackageInstaller$SessionParams@ae6dc62 user id 0
     * I/PackageInstaller: UserManager.DISALLOW_NON_MARKET_APP_BY_KNOX : false
     * D/PackageInstaller: InstallLogger: PackageInstallerSession(): createInstallTimeInfo(753728782)
     * I/ActivityManager: START u0 {act=pl.marcinkwasniak.install.APK_INSTALLATION_ACTION typ=null flg=0x0 cmp=ComponentInfo{pl.marcinkwasniak.install/pl.marcinkwasniak.install.MainActivity}} from uid 10394
     */
    private fun install(context: Context) {

        // First, create a package installer session.
        val packageInstaller = context.packageManager.packageInstaller
        val params = PackageInstaller.SessionParams(
            PackageInstaller.SessionParams.MODE_FULL_INSTALL
        )
        val sessionId = packageInstaller.createSession(params)
        val session = packageInstaller.openSession(sessionId)

        // Add the APK binary to the session. The APK is included in our kotlintest binary
        // and is read from res/raw but file storage is a more typical location.
        // The I/O streams can't be open when installation begins.
        session.openWrite("apk", 0, -1).use { output ->
            context.resources.openRawResource(R.raw.kotlintest).use { input ->
                input.copyTo(output, 2048)
            }
        }

        // Create a status receiver to report progress of the installation.
        // We'll use the current activity.
        // Here we're requesting status feedback to our Activity but this can be a
        // service or broadcast receiver.
        val intent = Intent(context, MainActivity::class.java)
        intent.action = "pl.marcinkwasniak.install.APK_INSTALLATION_ACTION"
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        val statusReceiver = pendingIntent.intentSender

        // Start the installation. Because we're an admin of a fully managed device,
        // there isn't any user interaction.
        session.commit(statusReceiver)
    }

}
