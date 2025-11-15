package com.tec.apptenis.ui.alumnos;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tec.apptenis.model.Alumno;
import com.tec.apptenis.request.TenisApi; // El ApiClient que nos mostraste
import com.tec.apptenis.request.TenisApi.TenisApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetallesAlumnoViewModel extends AndroidViewModel {

    private final MutableLiveData<Alumno> alumnoMutable;
    private final MutableLiveData<String> mensajeMutable;
    private final Context context;

    public DetallesAlumnoViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        alumnoMutable = new MutableLiveData<>();
        mensajeMutable = new MutableLiveData<>();
    }

    public LiveData<Alumno> getAlumno() {
        return alumnoMutable;
    }

    public LiveData<String> getMensaje() {
        return mensajeMutable;
    }

    /**
     * Carga un alumno existente por ID o prepara para crear uno nuevo (ID = 0).
     */
    public void recuperarAlumno(@NonNull Bundle bundle) {
        // Obtenemos el ID del alumno pasado desde el listado
        int alumnoId = bundle.getInt("alumno_id", 0);

        if (alumnoId != 0) {
            // Modo Edición: Llamar a la API para obtener el alumno
            String token = TenisApi.leerToken(context);
            if (token == null) {
                mensajeMutable.setValue("Error: Token de autorización no encontrado.");
                return;
            }

            TenisApiService api = TenisApi.getTenisApiService();
            // Usamos el endpoint hipotético para obtener un solo alumno por ID
            Call<Alumno> call = api.obtenerAlumnoPorId("Bearer " + token, alumnoId);

            call.enqueue(new Callback<Alumno>() {
                @Override
                public void onResponse(Call<Alumno> call, Response<Alumno> response) {
                    if (response.isSuccessful()) {
                        alumnoMutable.setValue(response.body());
                    } else {
                        mensajeMutable.setValue("Error al cargar alumno: " + response.code());
                        Log.e("API_ERROR", "Carga fallida: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<Alumno> call, Throwable t) {
                    mensajeMutable.setValue("Fallo de conexión al cargar alumno.");
                    Log.e("API_ERROR", "Fallo de conexión: " + t.getMessage());
                }
            });
        } else {
            // Modo Creación: Inicializar un alumno vacío
            alumnoMutable.setValue(new Alumno());
        }
    }

    /**
     * Envía el objeto Alumno actualizado a la API.
     * Utiliza el endpoint 'api/alumnos/actualizar' que usa PUT y requiere el objeto completo.
     */
    public void actualizarAlumno(Alumno alumno) {
        String token = TenisApi.leerToken(context);

        if (token == null) {
            mensajeMutable.setValue("Error: Token de autorización no encontrado.");
            return;
        }

        TenisApiService api = TenisApi.getTenisApiService();
        // Usamos el endpoint existente para la actualización
        Call<Alumno> call = api.actualizarAlumno("Bearer " + token, alumno);

        call.enqueue(new Callback<Alumno>() {
            @Override
            public void onResponse(Call<Alumno> call, Response<Alumno> response) {
                if (response.isSuccessful()) {
                    mensajeMutable.setValue("Alumno actualizado correctamente. ✅");
                    // Opcional: actualizar el LiveData con la respuesta del servidor
                    alumnoMutable.setValue(response.body());
                } else {
                    mensajeMutable.setValue("Error al actualizar: " + response.code());
                    Log.e("API_ERROR", "Actualización fallida: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Alumno> call, Throwable t) {
                mensajeMutable.setValue("Fallo de conexión al actualizar.");
                Log.e("API_ERROR", "Fallo de conexión: " + t.getMessage());
            }
        });
    }
}