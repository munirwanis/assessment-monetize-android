package com.wanis.assessmentmonetize;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class MainActivity extends AppCompatActivity {

    InterstitialAd mInterstitialAd;
    Button adButton, validationButton;
    EditText nameEditText, emailEditText, passwordEditText, passwordConfirmationEditText, cpfEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.config();
    }

    private void config() {
        adButton = findViewById(R.id.btnAd);
        validationButton = findViewById(R.id.btValidar);
        nameEditText = findViewById(R.id.etNome);
        emailEditText = findViewById(R.id.etEmail);
        passwordEditText = findViewById(R.id.etSenha);
        passwordConfirmationEditText = findViewById(R.id.etConfirmarSenha);
        cpfEditText = findViewById(R.id.etCpf);

        SimpleMaskFormatter smf = new SimpleMaskFormatter("NNN.NNN.NNN-NN");
        MaskTextWatcher mtw = new MaskTextWatcher(cpfEditText, smf);
        cpfEditText.addTextChangedListener(mtw);

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-2048265031968487/7982707743");

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                toast("Obrigado!");
                nameEditText.setText("");
                emailEditText.setText("");
                passwordEditText.setText("");
                passwordConfirmationEditText.setText("");
                cpfEditText.setText("");

                adButton.setEnabled(false);
            }
        });


        requestNewInterstitial();

        adButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveContacts();
                mInterstitialAd.show();

            }
        });
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mInterstitialAd.loadAd(adRequest);
    }

    public void validateForm(View view) {
        String nameString = this.nameEditText.getText().toString();
        String emailString = this.emailEditText.getText().toString();
        String passwordString = this.passwordEditText.getText().toString();
        String passwordConfirmationString = this.passwordConfirmationEditText.getText().toString();
        String cpfString = this.cpfEditText.getText().toString();

        boolean isFormValid = true;

        if (TextUtils.isEmpty(nameString)) {
            isFormValid = false;
            this.nameEditText.setError("O nome deve ser preenchido.");
        }

        if (TextUtils.isEmpty(emailString) || !isValidEmail(emailEditText.getText().toString())) {
            this.emailEditText.setError("O email deve ser preenchido e válido.");
        }

        if (TextUtils.isEmpty(passwordString)) {
            this.passwordEditText.setError("A senha deve ser preenchida.");
        }

        if (TextUtils.isEmpty(passwordConfirmationString) || !isPasswordValid(passwordEditText.getText().toString(), passwordConfirmationEditText.getText().toString())) {
            this.passwordConfirmationEditText.setError("A confirmação da senha deve ser preenchida e válida.");
        }

        if (TextUtils.isEmpty(cpfString) || cpfString.length() == 11) {
            this.cpfEditText.setError("O CPF deve ser preenchido e válido.");
        }

        if (isFormValid) {
            this.toast("Campos validados com sucesso!");
            adButton.setEnabled(true);
        }
    }

    public void saveContacts() {

        Contact contact = new Contact(nameEditText.getText().toString(), emailEditText.getText().toString(), passwordEditText.getText().toString(), passwordConfirmationEditText.getText().toString(), cpfEditText.getText().toString());

        try {
            FileOutputStream fos = openFileOutput("contacts.txt", Context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(contact);

            this.toast("Contato salvo!");
        } catch (Exception e) {
            this.toast("Erro: " + e.getMessage());
        }
    }

    private void toast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private boolean isValidEmail(String email) {
        return (Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    private boolean isPasswordValid(String firstPassword, String secondPassword) {
        return firstPassword.equals(secondPassword);
    }
}
