package alterationstudio.dompetku;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;


import alterationstudio.dompetku.Config.Config;
import alterationstudio.dompetku.Helpers.Helper;
import alterationstudio.dompetku.Response.Login;
import alterationstudio.dompetku.Service.LoginService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Callback<Login>{

    TextView regis;
    EditText passwordEt, emailEt;
    String passwordStr, emailStr;
    Button loginBtn;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Cek Login atau belum, kalau sudah login, langsung masuk ke Beranda Activity
         */

        if(this.isLogin()){
            Intent i = new Intent(this, BerandaActivity.class);
            startActivity(i);
            finish();
        }

        passwordEt = (EditText) findViewById(R.id.etPassword);
        emailEt = (EditText) findViewById(R.id.etEmail);
        loginBtn = (Button) findViewById(R.id.btLogin);
        regis = (TextView) findViewById(R.id.tvRegis);
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Login");
        progressDialog.setMessage("Loading... Please Wait");

        regis.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == regis){
            Intent i = new Intent(this, RegisActivity.class);
            startActivity(i);
            finish();
        } else {
            passwordStr = passwordEt.getText().toString();
            emailStr = emailEt.getText().toString();

            if(passwordStr.matches("") || emailStr.matches("")){
                Toast.makeText(this,"Field Tidak Boleh Kosong",Toast.LENGTH_LONG).show();
            } else {
                /**
                 * Cek Email Valid
                 */
                if(!Helper.isValidEmail(emailStr)){
                    Toast.makeText(this,"Email Tidak Valid",Toast.LENGTH_LONG).show();
                } else {
                    /**
                     * Cek Password Minimal 6 digit
                     */
                    if(passwordStr.length() < 6){
                        Toast.makeText(this,"Kata Sandi Minimal 6 digit",Toast.LENGTH_LONG).show();
                    } else {
                        progressDialog.show();
                        /**
                         * Cek from API
                         */


                        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(Config.getServerUrl())
                                .client(client)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        LoginService loginService = retrofit.create(LoginService.class);
                        Call<Login> registerCall = loginService.postLogin(emailStr,passwordStr, Config.getApiKey());
                        registerCall.enqueue(this);
                    }
                }

            }
        }
    }

    @Override
    public void onResponse(Call<Login> call, Response<Login> response) {
        progressDialog.dismiss();
        Login login = response.body();
        Toast.makeText(this,login.getMsg(),Toast.LENGTH_LONG).show();
        if(login.getStatus()){

            /**
             * Jika berhasil, simpan data dalam sharePreference
             */
            SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putBoolean("userLogin",true);
            editor.putString("userName",login.getUsername());
            editor.putString("userKey",login.getUserKey());
            editor.putString("userGambar",login.getGambar());
            editor.putInt("userSaldo",login.getSaldo());
            editor.putString("userEmail",emailEt.getText().toString());

            /**
             * Untuk object kategori
             */
            Gson gson = new Gson();
            String kategoriStr = gson.toJson(login.getKategori());
            editor.putString("kategoriTransaksi",kategoriStr);
            editor.commit();

            if(this.isLogin()){
                Intent i = new Intent(this, BerandaActivity.class);
                startActivity(i);
                finish();
            }
        }
    }

    @Override
    public void onFailure(Call<Login> call, Throwable t) {

    }

    public boolean isLogin(){
        SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        if(sharedPref.getBoolean("userLogin",false)){
            return true;
        } else {
            return false;
        }
    }


}
