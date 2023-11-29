package com.example.Wacamole;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class showCuenta extends AppCompatActivity {
    String username;
    String accountName;
    FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_cuenta);
        //Variables de la actitvity
        Intent usernamerecibido = getIntent();
        username = usernamerecibido.getStringExtra("Nombre");
        TextView AccounTextname = findViewById(R.id.Username_Text);
        ImageView delete = findViewById(R.id.Delete);
        ImageView ImgFlecha = findViewById(R.id.backMenu);
        LinearLayout LLCuentas = findViewById(R.id.LLCuentas);

        //Lleva al metodo de borrar la cuenta
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alerta = new AlertDialog.Builder(showCuenta.this);
                alerta.setMessage("¿Quieres eliminar la cuenta?")
                        .setCancelable(false)
                        .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                deleteAccount();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                dialogInterface.cancel();
                            }
                        });
                AlertDialog title = alerta.create();
                title.show();

            }
        });

        //Para cuando le den click la flecha, te manda a la clase user.java
        ImgFlecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToCuenta = new Intent(showCuenta.this, User.class);
                intentToCuenta.putExtra("Nombre", username);
                startActivity(intentToCuenta);
            }
        });
        //Colocamos un borde al scrollView
        colorBorde();
        //Reseteamos el LinearLayout
        LLCuentas.removeAllViews();
        //Accdemos a cuentas para buscar las que pertenezcan al usuario del log in
        firestoreDB.collection("Cuentas")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            //Recorremos las cuantas buscando coincidencias en el username y le damos atributos
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                TextView NombreCuenta = new TextView(showCuenta.this);
                                NombreCuenta.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                NombreCuenta.setTextSize(25);
                                NombreCuenta.setPadding(0, 25, 0, 25);
                                NombreCuenta.setTextColor(getResources().getColor(R.color.white)); // Cambia el color del texto a blanco

                                ShapeDrawable backgroundDrawable = new ShapeDrawable(new RectShape());
                                backgroundDrawable.getPaint().setColor(getColor(R.color.background_Superdark_green));

                                ShapeDrawable borderDrawable = new ShapeDrawable(new RectShape());
                                borderDrawable.getPaint().setStrokeWidth(12f);
                                borderDrawable.getPaint().setStyle(Paint.Style.STROKE);
                                borderDrawable.getPaint().setColor(getResources().getColor(R.color.background_dark_green));

                                Drawable[] layers = {backgroundDrawable, borderDrawable};
                                LayerDrawable layerDrawable = new LayerDrawable(layers);

                                NombreCuenta.setBackground
                                        (layerDrawable);
                                if (document.get("CuentaPrincipal").equals("true") && document.get("UserName").equals(username)) {
                                    AccounTextname.setText(document.get("AccountName").toString());
                                    accountName = document.get("AccountName").toString();
                                }
                                //Escribimos todas las cuentas del user menos en la que esta logeado
                                if (document.get("UserName").equals(username) && !document.get("AccountName").equals(accountName)) {
                                    NombreCuenta.setText(document.get("AccountName").toString());
                                    //Al clicar el nombre de una cuanta, nos cambiará de cuenta
                                    NombreCuenta.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            changeAccount(NombreCuenta);
                                        }
                                    });
                                    //Añade el textview con la cuenta al ScrollView
                                    LLCuentas.addView(NombreCuenta);
                                }
                            }
                        }
                    }
                });
    }

    //Solamente sirve para poner un puto borde al texview que dice "Cuentas" xD
    public void colorBorde() {
        //El borde puede ser para el scroll o para el textView
        TextView textView = findViewById(R.id.String_cuenta);

        ShapeDrawable backgroundDrawable = new ShapeDrawable(new RectShape());
        backgroundDrawable.getPaint().setColor(getColor(R.color.background_light_green));

        ShapeDrawable borderDrawable = new ShapeDrawable(new RectShape());
        borderDrawable.getPaint().setStrokeWidth(6f);
        borderDrawable.getPaint().setStyle(Paint.Style.STROKE);
        borderDrawable.getPaint().setColor(getResources().getColor(android.R.color.black));

        Drawable[] layers = {backgroundDrawable, borderDrawable};
        LayerDrawable layerDrawable = new LayerDrawable(layers);

        textView.setBackground
                (layerDrawable);

    }

    //Elimino la cuenta
    public void deleteAccount() {
        firestoreDB.collection("Cuentas").document(accountName)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(showCuenta.this, "Cuenta eliminada", Toast.LENGTH_SHORT).show();

                        firestoreDB.collection("Cuentas").get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            //Busca si el user tiene cuentas
                                            boolean noHayCuenta = false;

                                            for (QueryDocumentSnapshot documento : task.getResult()) {
                                                if (documento.get("UserName").equals(username)) {
                                                    noHayCuenta = true;
                                                    break;
                                                }
                                            }
                                            //Si no tiene cuantas, le obligamos a crear una
                                            if (noHayCuenta) {
                                                actualizarCuentaPrincipal();
                                                //Si tiene otras cuentas, le cambiamos a la siguiente que haya registrado
                                            } else {
                                                Intent goCuenta = new Intent(showCuenta.this, Cuenta.class);
                                                goCuenta.putExtra("userNew", true);
                                                goCuenta.putExtra("Nombre", username);
                                                startActivity(goCuenta);
                                            }
                                        }
                                    }
                                });
                    }
                });
    }

    //actualizo CuentaPrincipal despues de borrar cuenta principal
    public void actualizarCuentaPrincipal() {
        //Al cambiar de cuenta, la siguiente que encuentre con ese user va a cambiar a true
        firestoreDB.collection("Cuentas")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            //Busca otra cuenta del user y se le asigna como principal
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.get("CuentaPrincipal").equals("false") && document.get("UserName").equals(username)) {
                                    accountName = document.get("AccountName").toString();
                                    Map<String, String> cuenta = new HashMap<>();
                                    cuenta.put("AccountName", accountName);
                                    cuenta.put("CuentaPrincipal", "true");//
                                    cuenta.put("FotoPerfil", "0");
                                    cuenta.put("Highest Score", "0");
                                    cuenta.put("UserName", username);
                                    //Se hace update de la cuenta cambiada
                                    firestoreDB.collection("Cuentas")
                                            .document(accountName)
                                            .set(cuenta)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Intent newIntent = new Intent(showCuenta.this, User.class);
                                                    newIntent.putExtra("Nombre", username);
                                                    startActivity(newIntent);
                                                }
                                            });
                                    break;
                                }
                            }
                        }
                    }
                });
    }

    //Cambiar la cuenta en la que estes logeado
    public void changeAccount(TextView newAccount) {
        Map<String, String> NewCuenta = new HashMap<>();
        NewCuenta.put("AccountName", newAccount.getText().toString());
        NewCuenta.put("CuentaPrincipal", "true");//
        NewCuenta.put("FotoPerfil", "0");
        NewCuenta.put("Highest Score", "0");
        NewCuenta.put("UserName", username); //PARA INDICAR AL USUARIO
        firestoreDB.collection("Cuentas")
                .document(newAccount.getText().toString())
                .set(NewCuenta).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Map<String, String> oldCuenta = new HashMap<>();
                        oldCuenta.put("AccountName", accountName);
                        oldCuenta.put("CuentaPrincipal", "false");//
                        oldCuenta.put("FotoPerfil", "0");
                        oldCuenta.put("Highest Score", "0");
                        oldCuenta.put("UserName", username); //PARA INDICAR AL USUARIO
                        firestoreDB.collection("Cuentas")
                                .document(accountName)
                                .set(oldCuenta).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Intent newIntent = new Intent(showCuenta.this, User.class);
                                        newIntent.putExtra("Nombre", username);
                                        startActivity(newIntent);
                                    }
                                });
                    }
                });
    }
}


