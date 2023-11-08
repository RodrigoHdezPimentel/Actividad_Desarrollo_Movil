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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class User extends AppCompatActivity {
String username;
FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        //Variables de la activity
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
        //Volver a la activity anterior
        flecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(User.this, menu.class);
                newIntent.putExtra("Nombre",username);
                startActivity(newIntent);
            }
        });
        //ACÁ LO QUE HAGO ES PARA COLOCAR EL EMAIL EN EDIT TEXT
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

        //ACÁ LE COLOCO DEBAJO DE LA FOTO DE PERFIL EL ACCOUNTNAME DE LA CUENTA PRINCIPAL DEL USER
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
                });
    }
    //Actualizar usuario junto a sus cuantas
    public void updateUser(TextView name, TextView mail){
        //Creo un map para hacer el update del user
        Map<String, String> User = new HashMap<>();
        User.put("Username", name.getText().toString().trim());
        User.put("Email", mail.getText().toString().trim());

    //Si no hay cambios en el nombre no actualizamos las cuentas
        if(!username.equals(name.getText().toString())) {
            //hacemos el update del user
            firestoreDB.collection("Usuarios")
                .document(name.getText().toString().trim())
                .set(User)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Si va bien actualizamos los username de las cuentas
                        firestoreDB.collection("Cuentas")
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        //Recorremos las cuentas para encontrar las que pertenecen al antiguo Username y las actualizamos
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            if (document.get("UserName").equals(username)) {

                                                Map<String, String> NewAccount = new HashMap<>();
                                                NewAccount.put("AccountName", document.get("AccountName").toString());
                                                NewAccount.put("CuentaPrincipal", document.get("CuentaPrincipal").toString());
                                                NewAccount.put("FotoPerfil", document.get("FotoPerfil").toString());
                                                NewAccount.put("Highest Score", document.get("Highest Score").toString());
                                                NewAccount.put("UserName", name.getText().toString());

                                                //Actualizamos el username de las cuentas
                                                firestoreDB.collection("Cuentas")
                                                        .document(document.get("AccountName").toString())
                                                        .set(NewAccount);
                                            }
                                        }
                                        //Eliminamos el antiguo user
                                        firestoreDB.collection("Usuarios")
                                                .document(username).delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        username = name.getText().toString().trim();

                                                    }
                                                });
                                    }
                                }
                            });
                    }

            });
        //Si no se actualiza el Username, se hace el update del user por si ha cambiado el email
        }else{
            firestoreDB.collection("Usuarios")
                .document(name.getText().toString().trim()).set(User);
        }
    }
    //Elimina el usuario
    public void deleteUser() {
        firestoreDB.collection("Usuarios").document(username)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        firestoreDB.collection("Cuentas").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot list) {
                                //Recorremos en los documentos y buscamos en los UserName del usuario que se eliminó
                                for(QueryDocumentSnapshot document : list){

                                    if(document.get("UserName").toString().equals(username)){
                                        //Eliminamos las cuentas del usuario eliminado
                                        firestoreDB.collection("Cuentas")
                                                .document(document.get("AccountName").toString()).delete();
                                    }
                                }
                               Toast.makeText(User.this, "Usuario eliminado", Toast.LENGTH_SHORT).show();
                                Intent toMain = new Intent(User.this, MainActivity.class);
                                startActivity(toMain);
                            }
                        });
                        
                    }
                });
    }


}