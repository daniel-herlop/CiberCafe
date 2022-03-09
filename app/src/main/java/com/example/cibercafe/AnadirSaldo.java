package com.example.cibercafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;

import java.math.BigDecimal;

public class AnadirSaldo extends AppCompatActivity {

    //nuestro id de paypal
    public static final String clientKey = "AUIfGxONzuE5HtxEAQpD3QG4qtnT7wDuJxO8rD5rmbVZ990TsCleU0_Bd5JNnCUc1M10mna22gpa4jJW";

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)
            .clientId(clientKey);
    private EditText entradaSaldo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.anadir_saldo);
        entradaSaldo = findViewById(R.id.entradaSaldo);

    }

    public void abrirPaypal(View view) {
        //TODO comprobar que el editText no sea nulo
        // Getting the amount from editText
        String saldo = entradaSaldo.getText().toString();

        // Establecemos que va a ser un pago
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(saldo)), "EUR", "Course Fees",
                PayPalPayment.PAYMENT_INTENT_SALE);

        // Se crea el intent a PayPal
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        secondActivityResultLauncher.launch(intent);
    }

    //recogemos el intent de paypal
    ActivityResultLauncher<Intent> secondActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    //Si ha sido correcto añadimos el saldo al usuario
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        //TODO añadir el saldo al usuario
                        Toast.makeText(AnadirSaldo.this, "saldo añadido: "+ entradaSaldo.getText().toString(), Toast.LENGTH_SHORT).show();
                    } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                        Toast.makeText(AnadirSaldo.this, "Pago cancelado", Toast.LENGTH_LONG).show();
                    } else if (result.getResultCode() == PaymentActivity.RESULT_EXTRAS_INVALID) {
                        Toast.makeText(AnadirSaldo.this, "Pago no válido", Toast.LENGTH_LONG).show();
                    }
                }
            });
}
