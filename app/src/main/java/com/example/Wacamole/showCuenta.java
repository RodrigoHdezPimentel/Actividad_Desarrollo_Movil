package com.example.Wacamole;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
        Intent usernamerecibido = getIntent();
        username = usernamerecibido.getStringExtra("Nombre");
        TextView AccounTextname = findViewById(R.id.Username_Text);

        ImageView delete =  findViewById(R.id.Delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {deleteAccount();
                actualizarCuentaPrincipal();
            }
        });
       // AccounTextname.setText(username);
        //HECHOOO

        //Para cuando le den click la flecha, te manda a la clase user.java
        ImageView ImgFlecha = findViewById(R.id.backMenu);
        ImgFlecha.setOnClickListener(new View.OnClickListener() {
        @Override
            public void onClick(View view) {
                Intent intentToCuenta = new Intent(showCuenta.this, User.class);
                intentToCuenta.putExtra("Nombre", username);
                startActivity(intentToCuenta);
            }
        });
        colorBorde();

        LinearLayout LLCuentas = findViewById(R.id.LLCuentas);
        LLCuentas.removeAllViews();
        firestoreDB.collection("Cuentas")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                TextView NombreCuenta = new TextView(showCuenta.this);
                                NombreCuenta.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                NombreCuenta.setTextSize(25);
                                NombreCuenta.setPadding(0, 10,0,10);

                                if(document.get("UserName").equals(username)){
                                    NombreCuenta.setText(document.get("AccountName").toString());
                                    NombreCuenta.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            changeAccount(NombreCuenta);

                                        }
                                    });
                                    LLCuentas.addView(NombreCuenta);
                                }
                                        //HE AGREGADO QUE SE COLOQUE EL ACCOUNTNAME DE LA CUENTA PRINCIPAL
                                if(document.get("CuentaPrincipal").equals("true") && document.get("UserName").equals(username)){
                                    AccounTextname.setText(document.get("AccountName").toString());
                                    accountName=document.get("AccountName").toString();
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
    }

//Solamente sirve para poner un puto borde al texview que dice "Cuentas" xD
public void colorBorde(){
        //el borde puede ser para el scroll o para el textView
    //ScrollView scrollView = findViewById(R.id.scrollView);
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

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(showCuenta.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    //actualizo CuentaPrincipal despues de borrar cuenta principal
    public void actualizarCuentaPrincipal(){
        firestoreDB.collection("Cuentas")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.get("CuentaPrincipal").equals("false") && document.get("UserName").equals(username)){
                                    accountName=document.get("AccountName").toString();
                                    Map<String, String> cuenta = new HashMap<>();
                                    cuenta.put("AccountName", accountName);
                                    cuenta.put("CuentaPrincipal", "true");//
                                    cuenta.put("FotoPerfil", "0");
                                    cuenta.put("Highest Score", "0");
                                    cuenta.put("UserName", username); //PARA INDICAR AL USUARIO
                                    firestoreDB.collection("Cuentas")
                                            .document(accountName)
                                            .set(cuenta)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                    // on successful completion of this process
                                                    // we are displaying the toast message.
                                                    Intent newIntent = new Intent(showCuenta.this, User.class);
                                                    newIntent.putExtra("Nombre", username);
                                                    startActivity(newIntent);

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                // inside on failure method we are
                                                // displaying a failure message.
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(showCuenta.this, "Fail to update the data..", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                    break;
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
    }
                    public void changeAccount(TextView newAccount){
                        Toast.makeText(this, newAccount.getText().toString(), Toast.LENGTH_SHORT).show();
                                Map<String, String> NewCuenta = new HashMap<>();
                                NewCuenta.put("AccountName", newAccount.getText().toString());
                                NewCuenta.put("CuentaPrincipal", "true");//
                                NewCuenta.put("FotoPerfil", "0");
                                NewCuenta.put("Highest Score", "0");
                                NewCuenta.put("UserName", username); //PARA INDICAR AL USUARIO
                                firestoreDB.collection("Cuentas")
                                        .document(newAccount.getText().toString())
                                        .set(NewCuenta)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
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
                                                       }).addOnFailureListener(new OnFailureListener() {
                                                           // inside on failure method we are
                                                           // displaying a failure message.
                                                           @Override
                                                           public void onFailure(@NonNull Exception e) {
                                                               Toast.makeText(showCuenta.this, "Fail to update the data..", Toast.LENGTH_SHORT).show();
                                                           }
                                                       });

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {

                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(showCuenta.this, "Fail to update the data..", Toast.LENGTH_SHORT).show();
                                            }
                                        });

    }
}

