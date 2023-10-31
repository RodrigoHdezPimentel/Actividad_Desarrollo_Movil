package com.example.miaplicacionxd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miaplicacionxd.database.CreateDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Insertar extends AppCompatActivity {
    Random rm = new Random();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertar);
    }

    public void changeToMain(View view) {
        Intent nuevoIntent = new Intent(Insertar.this, MainActivity.class);
        startActivity(nuevoIntent);
    }

    FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    public void InsertValues(View v) {
        //Leemos variables
        TextView nameTextView = findViewById(R.id.DB_Name);
        TextView surnameTextView = findViewById(R.id.DB_Surname);

        Map<String, String> user = new HashMap<>();
        user.put("name", nameTextView.getText().toString());
        user.put("surname", surnameTextView.getText().toString());
        user.put("point", Integer.toString(rm.nextInt(10)));

        firestoreDB.collection("miaplicacionxD")
                .document(nameTextView.getText() + "_" + surnameTextView.getText())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Insertar.this, "Me gusta el penne boloñesa", Toast.LENGTH_SHORT).show();
                        Log.d("_Debug","TODO ON");
                        nameTextView.setText("");
                        surnameTextView.setText("");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Insertar.this, "Odio el penne boloñesa", Toast.LENGTH_SHORT).show();
                        Log.d("ERROR",e.getMessage());
                    }
                });
    }

    public void DeleteValues(View v) {
        //Leemos variables
        TextView nameTextView = findViewById(R.id.DB_Name);
        TextView surnameTextView = findViewById(R.id.DB_Surname);

        //Guardamos variables
        String nameString = nameTextView.getText().toString();
        String surnameString = surnameTextView.getText().toString();

        //Creamos base de datos
        CreateDatabase cdb = new CreateDatabase(Insertar.this);
        SQLiteDatabase db = cdb.getReadableDatabase();
        firestoreDB.collection("miaplicacionxD")
                .document(nameTextView.getText() + "_" + surnameTextView.getText())
                .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(Insertar.this, "Registro eliminado con exito", Toast.LENGTH_SHORT).show();
                        nameTextView.setText("");
                        surnameTextView.setText("");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Insertar.this, "El registro no se ha podido eliminar", Toast.LENGTH_SHORT).show();
                    }
                });

        if (db != null) {

            long resultadoDel = db.delete("Users", "name ='" + nameString + "' and surname = '" + surnameString + "'", null);
            if (resultadoDel != 0) {
                Toast.makeText(this, "Registro eliminado", Toast.LENGTH_SHORT).show();
                nameTextView.setText("");
                surnameTextView.setText("");
            } else {
                Toast.makeText(this, "Registro no encontrado", Toast.LENGTH_SHORT).show();
            }
            db.close();
        } else {
            Toast.makeText(this, "Valores vacios o base de datos no existe", Toast.LENGTH_SHORT).show();
        }
    }
}


