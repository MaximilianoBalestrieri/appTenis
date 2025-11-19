package com.tec.apptenis.ui.detallesclase;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tec.apptenis.model.Clase;
import com.tec.apptenis.model.Devolucion;
import com.tec.apptenis.model.DevolucionRequest;
import com.tec.apptenis.request.TenisApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetallesClaseViewModel extends AndroidViewModel {

    private final MutableLiveData<String> mensajeError = new MutableLiveData<>();
    private final MutableLiveData<String> mensajeExito = new MutableLiveData<>();
    private final MutableLiveData<Clase> claseActualizada = new MutableLiveData<>();

    public DetallesClaseViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getMensajeError() { return mensajeError; }
    public LiveData<String> getMensajeExito() { return mensajeExito; }
    public LiveData<Clase> getClaseActualizada() { return claseActualizada; }

    /**
     * Guarda la devolución y refresca la clase automáticamente.
     * @param comentario Comentario de la devolución
     * @param ejemplo Ejemplo de la devolución
     * @param idClaseAlumno ID de la relación Clase-Alumno
     * @param idClase ID de la clase
     */
    public void guardarDevolucion(String comentario, String ejemplo, int idClaseAlumno, int idClase) {
        Context context = getApplication().getApplicationContext();
        String token = TenisApi.leerToken(context);

        DevolucionRequest request = new DevolucionRequest(comentario, ejemplo, idClaseAlumno);

        TenisApi.getTenisApiService().crearDevolucion("Bearer " + token, request)
                .enqueue(new Callback<Devolucion>() {
                    @Override
                    public void onResponse(@NonNull Call<Devolucion> call, @NonNull Response<Devolucion> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            mensajeExito.setValue("Devolución guardada con éxito!");

                            // 🔄 Refrescar la clase después de guardar
                            new android.os.Handler(android.os.Looper.getMainLooper())
                                    .postDelayed(() -> refrescarClase(idClase), 100);
                        } else {
                            mensajeError.setValue("Error al guardar: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Devolucion> call, @NonNull Throwable t) {
                        mensajeError.setValue("Error de red: " + t.getMessage());
                    }
                });
    }

    /**
     * Refresca la clase desde el servidor
     * @param idClase ID de la clase
     */
    public void refrescarClase(int idClase) {
        Context context = getApplication().getApplicationContext();
        String token = TenisApi.leerToken(context);

        TenisApi.getTenisApiService().obtenerClasePorId("Bearer " + token, idClase)
                .enqueue(new Callback<Clase>() {
                    @Override
                    public void onResponse(@NonNull Call<Clase> call, @NonNull Response<Clase> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            claseActualizada.setValue(response.body());
                        } else {
                            mensajeError.setValue("No se pudo refrescar la clase.");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Clase> call, @NonNull Throwable t) {
                        mensajeError.setValue("Error de red al refrescar: " + t.getMessage());
                    }
                });
    }
}
