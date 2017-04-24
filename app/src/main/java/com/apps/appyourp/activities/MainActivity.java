package com.apps.appyourp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.apps.appyourp.R;
import com.apps.appyourp.adapters.AdapterHome;
import com.apps.appyourp.login.Login;
import com.apps.appyourp.objects.FirebaseReference;
import com.apps.appyourp.objects.SitiosVO;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.ResultCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * Created by Rangel on 04/03/2017.
 * Main Activity
 */

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {


    ProgressDialog progressDialog;
    private GoogleApiClient googleApiClient;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    ImageButton option;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    ArrayList<SitiosVO> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setContentView( R.layout.activity_main );
        option= (ImageButton) findViewById( R.id.option );
        option.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup( view );
            }
        } );
        progressDialog=new ProgressDialog(MainActivity.this);
        progressDialog.setMessage( "Cerrando Session" );
        progressDialog.setProgressStyle( progressDialog.STYLE_SPINNER );
        /**
        Instancias para detectar la autenticacion con google y cerrar session
         */
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder( GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    logOut();
                }
            }
        };

        recyclerView= (RecyclerView) findViewById(R.id.recycler);
        arrayList=new ArrayList<>();
        layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        adapter=new AdapterHome(arrayList,getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        database.getReference("city/armenia/site/events/").addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList.removeAll(arrayList);
                for (DataSnapshot snapshot:
                     dataSnapshot.getChildren()) {

                    SitiosVO sitiosVO=snapshot.getValue(SitiosVO.class);
                    arrayList.add(sitiosVO);
                    recyclerView.setAdapter( adapter );
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        } );


    }

    /**
     * metodo para cerrar la session de firebase,google y facebook
     *
     */
    private void cerrarSession() {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback( new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()) {
                    logOut();
                } else {
                    Toast.makeText(getApplicationContext(),"Fallo al cerrar session", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * metodo que cree para finalizar todas las actividades cuando se presione el boton salir
     * ya que por algun motivo me estaba llamando dos veces al main activity
     * y me toco finalizar con el finishAffinity() que cierra cualquier actividad
     * y agregar una condicion ya que solo funciona en la api 16 en adelante
     */
    public void onBackPressed(){
     super.onBackPressed();
     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
             finishAffinity();

     }else {
         finish();
     }
 }

    /**
     * Con este metodo se llama de nuevo a la actividad login cuando se presione cerrar Session
     */

    public void logOut(){
        Intent intent=new Intent( getApplicationContext(), Login.class );
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity( intent );
        progressDialog.dismiss();
        finish();
    }

    /**
     * metodo obligatorio para poder identificar la autenticacion con google
     * @param connectionResult
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(getApplicationContext(), v);
        MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.main, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                progressDialog.show();
                cerrarSession();
                return true;
            }
        });
        popup.show();
    }
}