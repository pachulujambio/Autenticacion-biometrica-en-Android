package com.authentication.biometricauthentication;

import android.graphics.Color;
import android.hardware.biometrics.BiometricManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import org.w3c.dom.Text;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicialización de los elementos del layout
        TextView msgtex = findViewById(R.id.msgtext);
        final Button loginbutton = findViewById(R.id.login);
        ImageView imageView = findViewById(R.id.img);
        TextView text01 = findViewById(R.id.text01);

        /**
         *  Creación del BiometricManager y comprobación de si el usuario puede o no usar biometría
         */
        androidx.biometric.BiometricManager biometricManager = androidx.biometric.BiometricManager.from(this);
        switch (biometricManager.canAuthenticate()) {

            // El dispositivo este habilitado para usar el lector biométrico
            case BiometricManager.BIOMETRIC_SUCCESS:
                msgtex.setText("Dispositivo habilitado para utilizar datos biométricos.");
                break;

            // El dispositivo no contiene lector biométrico
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                msgtex.setText("Este dispositivo no contiene lector de datos biométricos.");
                loginbutton.setVisibility(View.GONE);
                break;

            // El dispositivo cuenta con lector biométrico pero este no se encuentra disponible
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                msgtex.setText("El lector de datos biométricos no se encuentra disponible.");
                loginbutton.setVisibility(View.GONE);
                break;

            // El dispositivo no cuenta con un dato biométrico cargado para realizar la autentificación
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                msgtex.setText("El dispositivo no cuenta con datos biométricos cargados, por favor corrobore sus opciones de seguridad.s");
                loginbutton.setVisibility(View.GONE);
                break;
        }

        /**
         * Creación del Executor
         */
        Executor executor = ContextCompat.getMainExecutor(this);

        /**
         * Otorga el resultado de la validación
         */
        final BiometricPrompt biometricPrompt = new BiometricPrompt(MainActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            /**
             * Método que se ejecuta al ser correcta la validación
             * @param result
             */
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

                //Toast que muestra un mensaje confirmando la lectura
                Toast.makeText(getApplicationContext(), "Acceso correcto", Toast.LENGTH_SHORT).show();

                //Cambios en el layout y sus elementos
                imageView.setImageDrawable(getDrawable(R.drawable.qlfta01));
                loginbutton.setVisibility(View.INVISIBLE);
                msgtex.setVisibility(View.INVISIBLE);
                text01.setVisibility(View.INVISIBLE);

            }

            /**
             * Método que se ejecuta al ser incorrecta la validación
             */
            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });

        /**
         * Creación del PrompInfo y cuadro de diálogo que solicita la autenticación del usuario
         */
        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("Verifica tu identidad")
                .setDescription("Coloca el dedo sobre el lector de huella").setNegativeButtonText("Cancelar").build();

        //Acción del botón creado e inicializado previamente que se encuentra en el main
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometricPrompt.authenticate(promptInfo);

            }
        });

    }

}