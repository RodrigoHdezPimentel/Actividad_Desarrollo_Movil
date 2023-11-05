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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class showCuenta extends AppCompatActivity {
    String username;
    FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_cuenta);
        Intent usernamerecibido = getIntent();
        username = usernamerecibido.getStringExtra("Nombre");
        TextView AccounTextname = findViewById(R.id.Username_Text);
       // AccounTextname.setText(username);

        //SE PODRIA REALIZAR DE DOS MANERAS:
        //1) COLOCAR EL LOGO DE DELETE AL LADO DEL NOMBRE DE CADA CUENTA.
        //2). QUE LA PERSONA PRIMERO LE DE CLICK A LA CUENTA Y DESPUES LE DE AL DELETE QUE ESTA ENCIMA DEL PERFIL
        //EN DONDE ABAJO ESTARÁ EL NOMBRE DEL ACCOUNT QUE ELIGIÓ, ESTO CONLLEVA A QUE CUANDO ELIJA LA CUENTA,
        //ESA PASA A SER LA PRINCIPAL, Y SI LE DA DELETE, LA PRINCIPAL LA OCUPARÁ LA SIGUIENTE CUENTA.
        //SOLO PODRÁ ELIMINAR CUANDO HAY MAS DE 1 CUENTA, PORQUE SI ELIMINA TODAS,
        // NOS TOCARA MANDARLO A CREARSE UN ACCOUNT NAME POR OBLIGACION XD

        //Para cuando le den click la flecha, te manda a la clase Cuenta.java
        ImageView ImgFlecha = findViewById(R.id.confirmar);
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
                                    LLCuentas.addView(NombreCuenta);
                                }
                                        //HE AGREGADO QUE SE COLOQUE EL ACCOUNTNAME DE LA CUENTA PRINCIPAL
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

}

