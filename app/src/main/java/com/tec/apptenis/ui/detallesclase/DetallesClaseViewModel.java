package com.tec.apptenis.ui.detallesclase;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tec.apptenis.model.DevolucionRequest;
import com.tec.apptenis.model.Devolucion; // Modelo de respuesta si tu API devuelve el objeto
import com.tec.apptenis.request.TenisApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetallesClaseViewModel extends AndroidViewModel {

    private final MutableLiveData<String> mensajeError = new MutableLiveData<>();
    private final MutableLiveData<String> mensajeExito = new MutableLiveData<>();

    public DetallesClaseViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getMensajeError() {
        return mensajeError;
    }

    public LiveData<String> getMensajeExito() {
        return mensajeExito;
    }

    /**
     * Guarda la nueva Devolución en la base de datos a través de la API.
     */
    public void guardarDevolucion(String comentario, String ejemplo, int idClaseAlumno) {
        Context context = getApplication().getApplicationContext();
        String token = TenisApi.leerToken(context);

        DevolucionRequest request = new DevolucionRequest(comentario, ejemplo, idClaseAlumno);

        TenisApi.getTenisApiService().crearDevolucion("Bearer " + token, request)
                .enqueue(new Callback<Devolucion>() { // Asumiendo que tu API devuelve un objeto Devolucion
                    @Override
                    public void onResponse(@NonNull Call<Devolucion> call, @NonNull Response<Devolucion> response) {
                        if (response.isSuccessful()) {
                            mensajeExito.setValue("Devolución guardada con éxito!");
                        } else {
                            // Intenta leer el mensaje de error del cuerpo de la respuesta
                            String errorBody = response.errorBody() != null ? response.errorBody().toString() : "Error desconocido.";
                            mensajeError.setValue("Fallo al guardar: " + response.code() + " - " + errorBody);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Devolucion> call, @NonNull Throwable t) {
                        mensajeError.setValue("Error de red: " + t.getMessage());
                    }
                });
    }
}