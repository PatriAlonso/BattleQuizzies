package com.example.patrichuan.battlequizzies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterScreen extends ActionBarActivity {

    private Button Registerbtn;
    private LinearLayout MainLayout;
    private EditText userEdit, passEdit, repitPassEdit, emailEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        setContentView(R.layout.registerscreen_layout);

        FontsOverride.setDefaultFont(this, "DEFAULT", "fonts/HVD_Comic_Serif_Pro.otf");

        // Aplicamos a Password
        EditText EditPass = (EditText) findViewById(R.id.Pass1);
        EditText EditPass2 = (EditText) findViewById(R.id.Pass2);

        FontsOverride.setPasswordFont(this, EditPass);
        FontsOverride.setPasswordFont(this, EditPass2);

        Registerbtn = (Button) findViewById(R.id.Registerbtn);

        Registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (register()) {
                    Intent SiguienteActivity = new Intent(v.getContext(), LoginScreen.class);
                    SiguienteActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(SiguienteActivity);
                    overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                }
            }
        });

        //Hide status bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Hide the action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        MainLayout = (LinearLayout) findViewById(R.id.main_layout);
        MainLayout.setBackgroundResource(R.drawable.background);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean register() {
        String usernametxt;
        String passwordtxt;
        String repitPass;
        String email;

        userEdit = (EditText) findViewById(R.id.UserEt);
        passEdit = (EditText) findViewById(R.id.Pass1);
        repitPassEdit = (EditText) findViewById(R.id.Pass2);
        emailEdit = (EditText) findViewById(R.id.EmailEt);


// Retrieve the text entered from the EditText
        usernametxt = userEdit.getText().toString().toUpperCase();
        passwordtxt = passEdit.getText().toString().toUpperCase();
        repitPass = repitPassEdit.getText().toString().toUpperCase();
        email = emailEdit.getText().toString().toUpperCase();


        // Force user to fill up the form
        if (usernametxt.equals("") && passwordtxt.equals("") && repitPass.equals("") && email.equals("")) {
            //VENTANA EMERGENTE RELLENAR CAMPOS VACIOS
            Toast.makeText(getApplicationContext(),
                    "Por favor completa los campos vacios para el registro",
                    Toast.LENGTH_LONG).show();
            return false;

        } else {
            if (passwordtxt.equals(repitPass)) {
                if (isEmailValid(email)) {
                    // Save new user data into Parse.com Data Storage
                    ParseUser user = new ParseUser();
                    user.setUsername(usernametxt);
                    user.setPassword(passwordtxt);
                    user.setEmail(email);
                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(com.parse.ParseException e) {
                            if (e == null) {
                                // Show a simple Toast message upon successful registration
                                Toast.makeText(getApplicationContext(),
                                        "Successfully Signed up, please log in.",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "Error en el registro, el nombre de usuario o el email ya están registrados.", Toast.LENGTH_LONG)
                                        .show();
                            }
                        }
                    });
                    return true;
                } else {
                    //VENTANA EMERGENTE EMAIL INVALIDO
                    Toast.makeText(getApplicationContext(),
                            "Has introducido mal tu email.",
                            Toast.LENGTH_LONG).show();
                    return false;
                }
            } else {
                //VENTANA EMERGENTE CONTRASEÑA NO COINCIDE
                Toast.makeText(getApplicationContext(),
                        "La contraseña no coincide, debe ser la misma en los dos campos.",
                        Toast.LENGTH_LONG).show();
                return false;
            }
        }

    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }
}
