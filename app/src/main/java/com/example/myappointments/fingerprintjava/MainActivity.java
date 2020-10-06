package com.example.myappointments.fingerprintjava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView msgText = findViewById(R.id.txt_msg);
        Button login_button = findViewById(R.id.login_button);

        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate()){
            case BiometricManager.BIOMETRIC_SUCCESS:
                msgText.setText("El usuario puede autenticarse correctamente.");
                msgText.setTextColor(Color.parseColor("#Fafafa"));
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                msgText.setText("El dispositivo no tiene detector de huella");
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                msgText.setText("El detector de huella no esta disponoible");
                login_button.setVisibility(View.GONE);
                break;
             case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                 msgText.setText("Tu dispositivo no tiene ninguna hueela guardad, porfavor checa en tus ajusts de seguridad");
                 login_button.setVisibility(View.GONE);
                 break;
        }

        //biometric dialog box
        Executor executor = ContextCompat.getMainExecutor(this);

        final BiometricPrompt biometricPrompt = new BiometricPrompt(MainActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(), "Logueado Correctamente",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });

     final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
             .setTitle("Login")
             .setDescription("Usaste tu detector de huella para loguearte en tu App")
             .setNegativeButtonText("Cancel")
             .build();
     login_button.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             biometricPrompt.authenticate(promptInfo);
         }
     });
    }
}
