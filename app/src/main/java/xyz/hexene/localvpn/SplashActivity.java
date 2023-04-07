package xyz.hexene.localvpn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.VpnService;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {
    private static final int VPN_REQUEST_CODE = 0x0F;
    private boolean waitingForVPNStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startVPN();
            }
        }, 2000);
    }

    private void startVPN() {
        Intent vpnIntent = VpnService.prepare(this);
        if (vpnIntent != null)
            startActivityForResult(vpnIntent, VPN_REQUEST_CODE);
        else
            onActivityResult(VPN_REQUEST_CODE, RESULT_OK, null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case VPN_REQUEST_CODE:
                switch (resultCode) {
                    case RESULT_OK:
                        waitingForVPNStart = true;
                        startService(new Intent(this, LocalVPNService.class));
                        startActivity(new Intent(this, LocalVPN.class));
                        break;
                    case RESULT_CANCELED:
                        startVPN();
                        break;
                }
                break;
        }
    }

}