package com.tec.apptenis.ui.modificarclase;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tec.apptenis.model.ClaseCreacionRequest;
import com.tec.apptenis.model.Clase;
import com.tec.apptenis.request.TenisApi;

import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModificarClaseViewModel extends AndroidViewModel {

    private final MutableLiveData<String> mensajeExito = new MutableLiveData<>();
    private final MutableLiveData<String> mensajeError = new MutableLiveData<>();

    public ModificarClaseViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getMensajeExito() { return mensajeExito; }
    public LiveData<String> getMensajeError() { return mensajeError; }

    public void modificarClase(int idClase, String nuevaHora, String nuevoEstado) {
        Context context = getApplication().getApplicationContext();
        String token = TenisApi.leerToken(context);

        // Creamos un request usando los datos existentes de la clase
        // (solo modificamos hora y estado)
        ClaseCreacionRequest request = new ClaseCreacionRequest(
                null, // fecha no modificada
                nuevaHora,
                0, // idProfesor no modificamos aquí
                nuevoEstado,
                null, // comentario no modificamos
                Collections.emptyList() // alumnos no modificamos
        );

        TenisApi.getTenisApiService().crearClase("Bearer " + token, request)
                .enqueue(new Callback<Clase>() {
                    @Override
                    public void onResponse(@NonNull Call<Clase> call, @NonNull Response<Clase> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            mensajeExito.setValue("Clase modificada con éxito!");
                        } else {
                            mensajeError.setValue("Error al modificar: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Clase> call, @NonNull Throwable t) {
                        mensajeError.setValue("Error de red: " + t.getMessage());
                    }
                });
    }
}
