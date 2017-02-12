package com.company.alex.cuentasmail;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private String[] componerListaCorreos(String listaxcomas) {

        String[] lista_correos = null;
        lista_correos = listaxcomas.split(",");

        return lista_correos;

    }

    private String[] getCuentasMail(AccountManager accountManager) {
        String[] cuentas_mail = null;
        //TODO recuperar cuentas de Email

        //AccountManager tiene un metodo que te devuelve un array de cuentas
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        Account[] lista_cuentas = accountManager.getAccounts();
        String str_aux = new String();

        for(Account cuenta: lista_cuentas){

            Log.d(getClass().getCanonicalName(), "Cuenta = " + cuenta.name);
            if(cuenta.type.equals("com.google")){//Si la cuenta es de gmail

                str_aux += cuenta.name + ",";

            }

        }

        cuentas_mail = componerListaCorreos(str_aux.substring(0,str_aux.length() - 1));

        return cuentas_mail;

    }

    private void obtenerCorreos() {

        AccountManager accountManager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
        String[] correos_usuarios = getCuentasMail(accountManager);

        for (String correo : correos_usuarios) {

            Log.d(getClass().getCanonicalName(), "CORREO = " + correo);

        }

    }
    private boolean tengoPermisos(){
        boolean permisos = false;

        permisos = ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED;
        return permisos;
    }

    private void pidoPermisos(){

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.GET_ACCOUNTS}, 100);

    }

    private boolean permiso_concedido(int[] array_res){
        return (array_res.length > 0 && array_res[0] == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 100){
            if(permiso_concedido(grantResults)){

                obtenerCorreos();

            } else {
                finish();
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(tengoPermisos()) {

            obtenerCorreos();

        }else{

            pidoPermisos();
            Log.d(getClass().getCanonicalName(), "SIN PERMISOS");
            finish();

        }

    }
}
