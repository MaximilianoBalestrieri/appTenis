package com.tec.apptenis.ui.alumnos; // Ajusta el paquete

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tec.apptenis.model.Alumno;
import com.tec.apptenis.request.TenisApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlumnosViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Alumno>> listaAlumnos = new MutableLiveData<>();

    public AlumnosViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Alumno>> getListaAlumnos() {
        return listaAlumnos;
    }

    public void cargarAlumnos() {
        String token = TenisApi.leerToken(getApplication());

        // 💡 IMPORTANTE: Verifica también si el token está vacío.
        if (token == null || token.isEmpty()) {
            Toast.makeText(getApplication(), "Error: Token no encontrado o vacío.", Toast.LENGTH_LONG).show();
            // Si no hay token, publicamos una lista vacía para que el adapter lo sepa.
            listaAlumnos.postValue(new ArrayList<>());
            return;
        }

        TenisApi.TenisApiService api = TenisApi.getTenisApiService();
        // ⚠️ Debes agregar obtenerAlumnos() a tu interfaz TenisApiService
        Call<List<Alumno>> call = api.obtenerAlumnos("Bearer " + token);

        call.enqueue(new Callback<List<Alumno>>() {
            @Override
            public void onResponse(@NonNull Call<List<Alumno>> call, @NonNull Response<List<Alumno>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaAlumnos.postValue(response.body());
                } else {
                    Toast.makeText(getApplication(), "Error al cargar alumnos: " + response.code(), Toast.LENGTH_SHORT).show();
                    Log.e("AlumnosViewModel", "Error en respuesta: " + response.toString());
                    listaAlumnos.postValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Alumno>> call, @NonNull Throwable t) {
                Log.e("AlumnosViewModel", "Fallo de conexión: " + t.getMessage());
                Toast.makeText(getApplication(), "Error de red al cargar alumnos.", Toast.LENGTH_SHORT).show();
                listaAlumnos.postValue(null);
            }
        });
    }
}