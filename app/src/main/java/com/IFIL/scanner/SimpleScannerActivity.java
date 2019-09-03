package com.IFIL.scanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.Result;

import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class SimpleScannerActivity extends Activity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;
    String TAG="KHE";
    @Override
    public void onCreate(Bundle state) {
            super.onCreate(state);
            mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
            setContentView(mScannerView);                // Set the scanner view as the content view
    }

    @Override
    public void onResume() {
            super.onResume();
            mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
            mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
            super.onPause();
            mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(final Result rawResult) {
            // Do something with the result here
            Log.v(TAG, rawResult.getText()); // Prints scan results

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> usuario = new HashMap<>();
        usuario.put("Nombbre", rawResult.getText());
        usuario.put("TimeStamp", FieldValue.serverTimestamp());

        db.collection("Asistencia").add(usuario).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getApplicationContext(), "el asistente: " + rawResult.getText() + "se registro correctamente", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "hubo un error al registrar intente de nuevo", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                });
    }
}