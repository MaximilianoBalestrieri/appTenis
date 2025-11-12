package com.tec.apptenis.ui.listadoUsuarios;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tec.apptenis.model.Usuario;
import com.tec.apptenis.request.TenisApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListadoUsuariosViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Usuario>> listaUsuarios = new MutableLiveData<>();

    public ListadoUsuariosViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Usuario>> getListaUsuarios() {
        return listaUsuarios;
    }

    public void cargarUsuarios() {
        String token = TenisApi.leerToken(getApplication());
        if (token == null) {
            Toast.makeText(getApplication(), "Error: Token no encontrado.", Toast.LENGTH_SHORT).show();
            return;
        }

        TenisApi.TenisApiService api = TenisApi.getTenisApiService();
        // Nota: Asumo que tienes un método en tu TenisApiService que acepta List<Usuario>
        Call<List<Usuario>> call = api.obtenerUsuarios("Bearer " + token);

        call.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(@NonNull Call<List<Usuario>> call, @NonNull Response<List<Usuario>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaUsuarios.postValue(response.body());
                } else {
                    Toast.makeText(getApplication(), "Error al cargar usuarios: " + response.code(), Toast.LENGTH_SHORT).show();
                    Log.e("ListadoUsuariosVM", "Error en respuesta: " + response.toString());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Usuario>> call, @NonNull Throwable t) {
                Log.e("ListadoUsuariosVM", "Fallo de conexión: " + t.getMessage());
                Toast.makeText(getApplication(), "Error de red al cargar usuarios.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}