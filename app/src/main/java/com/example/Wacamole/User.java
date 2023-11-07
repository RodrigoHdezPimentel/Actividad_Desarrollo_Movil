package com.example.Wacamole;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class User extends AppCompatActivity {
String username;
FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
//Cosas por hacer: DESPUESD EL UPDATE Y DEVOLVERSE DE LA CLASE SHOWCUENTA,
// MANTERNER CAMBIOS EN LA CLASE USER.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Intent usernamerecibido = getIntent();
        username = usernamerecibido.getStringExtra("Nombre");
        EditText textUsername= findViewById(R.id.DB_UserName);
        EditText textEmial = findViewById(R.id.DB_Email);
        Button goCuenta = findViewById(R.id.newAccountBut);
        ImageView mod = findViewById(R.id.ModAccount);
        Button countselect = findViewById(R.id.SelectCuenta);
        Button confirmUpdate = findViewById(R.id.backMenu);
        ImageView delete = findViewById(R.id.delete);
        ImageView flecha = findViewById(R.id.flecha);
        flecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(User.this, menu.class);
                newIntent.putExtra("Nombre",username);
                startActivity(newIntent);
            }
        });
        //ACÁ SOLO FALTA HACER EL UPDATE AL USUARIO.
        //CAMBIOS MIOS (diego)--------------- ACÄ LO QUE HAGO ES PARA COLOCAR EL EMAIL EN EDIT TEXT
        firestoreDB.collection("Usuarios")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for(QueryDocumentSnapshot document : task.getResult()){
                            if(document.get("Username").equals(username)){
                            textEmial.setText(document.get("Email").toString());
                            textEmial.setEnabled(false);
                            //ACÁ LE COLOCO EL USERNAME EN EL EDITTEXT
                            textUsername.setText(username);
                            textUsername.setEnabled(false);
                        }
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        //Boton eliminar
        delete.setVisibility(View.INVISIBLE);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteUser();
            }
        });

        //Boton de confirmar update
        confirmUpdate.setVisibility(View.INVISIBLE);
        confirmUpdate.setEnabled(false);
        confirmUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textUsername.setEnabled(false);
                textEmial.setEnabled(false);
                countselect.setVisibility(View.VISIBLE);
                countselect.setEnabled(true);
                goCuenta.setVisibility(View.VISIBLE);
                goCuenta.setEnabled(true);
                confirmUpdate.setVisibility(View.INVISIBLE);
                confirmUpdate.setEnabled(false);
                delete.setVisibility(View.INVISIBLE);
                mod.setVisibility(View.VISIBLE);
                mod.setEnabled(true);
                delete.setEnabled(false);
                delete.setVisibility(View.INVISIBLE);
                updateUser(textUsername, textEmial);
            }
        });
        //BOTON DE IR A LA CLASE CUENTA CUANDO LE DAN CLICK AÑADIR CUENTA
        goCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean newCuenta=false;
                Intent goCuenta = new Intent(User.this, Cuenta.class);
                goCuenta.putExtra("Nombre",username);
                goCuenta.putExtra("userNew", newCuenta);
                startActivity(goCuenta);
            }
        });
        //Ir a tus cuentas
        countselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goShow = new Intent(User.this, showCuenta.class);
                goShow.putExtra("Nombre", username);
                startActivity(goShow);
            }
        });
        //Boton para modificar
        mod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textUsername.setEnabled(true);
                textEmial.setEnabled(true);
                countselect.setVisibility(View.INVISIBLE);
                countselect.setEnabled(false);
                goCuenta.setVisibility(View.INVISIBLE);
                goCuenta.setEnabled(false);
                confirmUpdate.setVisibility(View.VISIBLE);
                confirmUpdate.setEnabled(true);
                delete.setVisibility(View.VISIBLE);
                mod.setVisibility(View.INVISIBLE);
                mod.setEnabled(false);
                delete.setEnabled(true);


            }
        });


        // DAR CLICK A LAS IMGANES PEQUEÑAS TE LLEVA A LA CLASE SHOWCUENTAS
        ImageView cuentas = findViewById(R.id.cuenta_1);
        cuentas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goShow = new Intent(User.this, showCuenta.class);
                goShow.putExtra("Nombre", username);
                startActivity(goShow);
            }
        });
        cuentas = findViewById(R.id.cuenta_2);
        cuentas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goShow = new Intent(User.this, showCuenta.class);
                goShow.putExtra("Nombre", username);
                startActivity(goShow);
            }
        });
        //ACÁ LE COLOCO DEBAJO DE LA FOTO DE PERFIL EL ACCOUNTNAME DE LA CUENTA PRINCIPAL DEL USER
        // MI UNICA MANERA DE HACER LA COMPROBACION FUE CREAR UN USER NUEVO PORQUE LOS QUE HABIAN EN LA
        //FOTO QUE ME ENVIASTES CREO QUE NINGUNO TENIA EN CUENTA PRINCIPAL "TRUE" XD

        TextView AccounTextname = findViewById(R.id.AccountName);
        firestoreDB.collection("Cuentas")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                if(document.get("CuentaPrincipal").equals("true") && document.get("UserName").equals(username)){
                                    AccounTextname.setText(document.get("AccountName").toString());
                                }
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(User.this, "No se pudo conectar a la base de datos", Toast.LENGTH_SHORT).show();
                    }
                });
        //-----------------------------------------------------------------
    }
    public void updateUser(TextView name, TextView mail){
        Map<String, String> User = new HashMap<>();
        User.put("Username", name.getText().toString().trim());
        User.put("Email", mail.getText().toString().trim());
        firestoreDB.collection("Usuarios")
                .document(username)
                .set(User)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // on successful completion of this process
                        // we are displaying the toast message.

                    }
                }).addOnFailureListener(new OnFailureListener() {
            // inside on failure method we are
            // displaying a failure message.
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(User.this, "Fail to update the data..", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void deleteUser() {
        firestoreDB.collection("Usuarios").document(username)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(User.this, "Usuario eliminado", Toast.LENGTH_SHORT).show();
                        Intent toMain = new Intent(User.this, MainActivity.class);
                        startActivity(toMain);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(User.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}