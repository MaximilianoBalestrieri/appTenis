package com.tec.apptenis.ui.login;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tec.apptenis.MainActivity;
import com.tec.apptenis.model.LoginResponse;
import com.tec.apptenis.model.UsuarioRequest;
import com.tec.apptenis.request.TenisApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivityViewModel extends AndroidViewModel {

    private final MutableLiveData<String> mMensaje = new MutableLiveData<>();

    public LiveData<String> getMMensaje() {
        return mMensaje;
    }

    public LoginActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public void logueo(String email, String contrasenia) {

        if (email.isEmpty() || contrasenia.isEmpty()) {
            mMensaje.setValue("Error, campos vacíos");
            return;
        }

        UsuarioRequest request = new UsuarioRequest(email, contrasenia);

        TenisApi.TenisApiService api = TenisApi.getTenisApiService();

        // AHORA SÍ: se llama a login y se espera un LoginResponse
        Call<LoginResponse> call = api.login(request);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response.isSuccessful() && response.body() != null) {

                    LoginResponse login = response.body();

                    // Guardamos token
                    TenisApi.guardarToken(getApplication(), login.getToken());

                    // Guardamos email, rol y nombre
                    getApplication()
                            .getSharedPreferences("MisPreferencias", Application.MODE_PRIVATE)
                            .edit()
                            .putString("email", email)
                            .putString("rol", login.getRol())
                            .putString("nombre", login.getNombre())
                            .apply();

                    // Navegación
                    Intent intent = new Intent(getApplication(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    getApplication().startActivity(intent);

                } else {
                    mMensaje.setValue("Credenciales incorrectas");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                mMensaje.setValue("Error de conexión: " + t.getMessage());
                Log.e("Login", "Error en login", t);
            }
        });
    }
}
