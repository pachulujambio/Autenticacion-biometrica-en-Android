<center>

# Autentificación por Biometría en Android
</center>

Proyecto que valida la autentificación de un usuario utilizando el lector de huellas biométricas del dispositivo Android. Mediante un cuadro de dialogo se le solicita al usuario sus datos biométricos y se constata si estos están o no dentro de los almacenados previamente en el teléfono. 

## Función de las clases utilizadas
* ### `BiometricManager`
Contiene las utilidades biométricas. Se utiliza para comprobar si el dispositivo permite la lectura de datos biométricos.

* ### `Executor`
Se encarga de ejecutar tareas enviadas en donde se le indique.

* ### `BiometricPrompt`
Clase principal, encargada del dialogo biométrico con el sistema. Nos otorga los métodos `onAuthenticationError`, `onAuthenticationSucceeded` y `onAuthenticationFailed`. Estos son los que utilizaremos para verificar si fue o no correcta la validación. Y además permite crear un `PromptInfo`.

* ### `PromptInfo`
Proporciona el cuadro de dialogo en el cual se ejecuta la lectura de los datos.

## Dependencias utilizadas
Dentro del `build.gradle` del proyecto añadimos las siguientes dependencias:

```java
implementation "androidx.biometric:biometric-ktx:1.2.0-alpha04"
    implementation "androidx.biometric:biometric:1.1.0"
```
## Librerías importadas
Se realizó la importación de las siguientes librerías en el MainActivity.java 

```java
import android.graphics.Color;
import android.hardware.biometrics.BiometricManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import java.util.concurrent.Executor;
```

## Código del `MainActivity`
La explicación del mismo se encuentra comentada.
```java
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
```
## Código del `activity_main.xml`
Código del layout correspondiente al Main.
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/white"
    android:gravity="center"
    android:orientation="vertical"
    tools:context=".MainActivity">

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Usa la fuerza para desbloquear el dispositivo (tu dedo es la fuerza)"
        android:layout_marginLeft="30dp"
        android:layout_marginRight="30dp"
        android:textColor="@color/black"
        android:textColorHint="@color/black"
        android:textSize="18sp"
        android:textAlignment="center"
        android:id="@+id/text01"/>

    <ImageView
        android:id="@+id/img"
        android:layout_width="168dp"
        android:layout_height="342dp"
        android:layout_marginVertical="20dp"
        android:src="@drawable/darth_art" />

    <TextView
        android:id="@+id/msgtext"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textColor="@color/black"
        android:text=""
        android:textAlignment="center"
        android:layout_marginLeft="30dp"
        android:layout_marginRight="30dp"
        android:textSize="18sp" />

    <Button
        android:id="@+id/login"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginVertical="20dp"
        android:background="@color/black"
        android:textColor="@color/white"
        android:text="Login" />

</LinearLayout>
```
