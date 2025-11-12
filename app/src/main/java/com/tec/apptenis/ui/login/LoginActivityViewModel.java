package com.tec.apptenis.ui.login;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tec.apptenis.MainActivity;
import com.tec.apptenis.request.TenisApi;
import com.tec.apptenis.model.UsuarioRequest; // <-- ¡Nuevo: Importar la clase UsuarioRequest!

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivityViewModel extends AndroidViewModel {

    private MutableLiveData<String> mMensaje;

    // Solo se conserva el LiveData para mensajes
    public LiveData<String> getMMensaje(){
        if (mMensaje == null) {
            mMensaje = new MutableLiveData<>();
        }
        return mMensaje;
    }

    public LoginActivityViewModel(@NonNull Application application) {
        super(application);
        mMensaje = new MutableLiveData<>();
    }

    /**
     * Procesa las credenciales, valida los campos e intenta el logueo
     * a través de la API, enviando el JSON requerido.
     */
    public void logueo(String email, String contrasenia){ // Renombré 'usuario' a 'email' para mayor claridad
        // 1. Validación de campos
        if (email.isEmpty() || contrasenia.isEmpty()){
            mMensaje.setValue("Error, campos vacíos");
            return;
        }

        // 2. Crear el objeto JSON que se enviará en el cuerpo (BODY)
        UsuarioRequest request = new UsuarioRequest(email, contrasenia); // <-- Usar el constructor

        // 3. Llamada a la API (Retrofit)
        TenisApi.TenisApiService tenisApiService = TenisApi.getTenisApiService();

        // 🔑 CORRECCIÓN: Usar el método 'login' y pasar el objeto 'request'
        Call<String> call = tenisApiService.login(request);

        call.enqueue(new Callback<String>() {
                         @Override
                         public void onResponse(Call<String> call, Response<String> response) {
                             if (response.isSuccessful()){

                                 // NOTA: Si la API devuelve un objeto {token, rol}, DEBES PARSEAR EL JSON
                                 // Pero, si el 'token' es lo único que necesitas, puedes intentar obtenerlo.
                                 String token = response.body();

                                 // Se actualiza la llamada a guardarToken
                                 TenisApi.guardarToken(getApplication(), token);

                                 // Guardar el email del usuario logueado
                                 getApplication()
                                         .getSharedPreferences("MisPreferencias", Application.MODE_PRIVATE)
                                         .edit()
                                         .putString("email", email)
                                         .apply();

                                 // Navegación exitosa
                                 Intent intent = new Intent(getApplication(), MainActivity.class);
                                 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                 getApplication().startActivity(intent);
                             } else {
                                 // 401 Unauthorized / Credenciales incorrectas (Si Postman funcionó, aquí debe estar el error)
                                 mMensaje.setValue("Credenciales incorrectas o error en el servidor.");
                             }
                         }

                         @Override
                         public void onFailure(Call<String> call, Throwable t) {
                             // Error de conexión (ya no debería ser el 415 o el CLEARTEXT)
                             mMensaje.setValue("Error de conexión: " + t.getMessage());
                             Log.e("Login", "Fallo de conexión", t);
                         }
                     }
        );
    }
}