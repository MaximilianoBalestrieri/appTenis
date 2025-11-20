package com.tec.apptenis.ui.modificarclase;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tec.apptenis.model.Alumno;
import com.tec.apptenis.model.Clase;
import com.tec.apptenis.model.ClaseAlumno;
import com.tec.apptenis.request.TenisApi;

import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModificarClaseViewModel extends AndroidViewModel {

    private MutableLiveData<Clase> claseLiveData = new MutableLiveData<>();
    private MutableLiveData<String> mensajeExito = new MutableLiveData<>();
    private MutableLiveData<String> mensajeError = new MutableLiveData<>();

    public ModificarClaseViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Clase> getClase() {
        return claseLiveData;
    }

    public LiveData<String> getMensajeExito() {
        return mensajeExito;
    }

    public LiveData<String> getMensajeError() {
        return mensajeError;
    }

    public void cargarClase(int idClase) {
        String token = TenisApi.leerToken(getApplication());

        TenisApi.getTenisApiService()
                .obtenerClasePorId("Bearer " + token, idClase)
                .enqueue(new Callback<Clase>() {
                    @Override
                    public void onResponse(Call<Clase> call, Response<Clase> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            claseLiveData.setValue(response.body());
                        } else {
                            mensajeError.setValue("No se pudo cargar la clase.");
                        }
                    }

                    @Override
                    public void onFailure(Call<Clase> call, Throwable t) {
                        mensajeError.setValue("Error de conexión al cargar clase.");
                        Log.e("API", t.getMessage());
                    }
                });
    }

    public void actualizarClase(Clase claseModificada) {

        String token = TenisApi.leerToken(getApplication());

        TenisApi.getTenisApiService()
                .modificarClase(
                        "Bearer " + token,
                        claseModificada.getIdClase(),
                        claseModificada
                )
                .enqueue(new Callback<Clase>() {
                    @Override
                    public void onResponse(Call<Clase> call, Response<Clase> response) {
                        if (response.isSuccessful() && response.body() != null) {

                            mensajeExito.setValue("Clase actualizada correctamente.");
                        } else {
                            mensajeError.setValue("No se pudo actualizar la clase.");
                        }
                    }

                    @Override
                    public void onFailure(Call<Clase> call, Throwable t) {
                        mensajeError.setValue("Error de conexión al actualizar clase.");
                        Log.e("API", t.getMessage());
                    }
                });
    }
    public Clase copiarClaseParaEditar(Clase claseOriginal) {
        Clase copia = new Clase();

        copia.setIdClase(claseOriginal.getIdClase());
        copia.setIdProfesor(claseOriginal.getIdProfesor());
        copia.setFecha(claseOriginal.getFecha());
        copia.setHora(claseOriginal.getHora());
        copia.setComentario(claseOriginal.getComentario());
        copia.setEstado(claseOriginal.getEstado());

        // Copiar alumnos
        copia.setClaseAlumnos(
                claseOriginal.getClaseAlumnos()
                        .stream()
                        .map(ca -> {
                            ClaseAlumno nuevo = new ClaseAlumno();
                            nuevo.setIdClaseAlumno(ca.getIdClaseAlumno());
                            nuevo.setIdAlumno(ca.getIdAlumno());
                            nuevo.setAlumno(ca.getAlumno());
                            return nuevo;
                        })
                        .collect(Collectors.toList())
        );

        return copia;
    }

}
