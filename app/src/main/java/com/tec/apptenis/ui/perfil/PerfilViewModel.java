package com.tec.apptenis.ui.perfil;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tec.apptenis.model.Profesor;
import com.tec.apptenis.request.TenisApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilViewModel extends AndroidViewModel {

    private MutableLiveData<Boolean> mEstado = new MutableLiveData<>();
    private MutableLiveData<String> mNombreBoton = new MutableLiveData<>();
    private MutableLiveData<Profesor> mProfesor = new MutableLiveData<>();

    public PerfilViewModel(@NonNull Application application) {
        super(application);
        mEstado.setValue(false);
        mNombreBoton.setValue("EDITAR");
    }
//----------------------------------------------------------------------

    public LiveData<Boolean> getmEstado() {
        return mEstado;
    }

    public LiveData<String> getmNombreBoton() {
        return mNombreBoton;
    }

    public LiveData<Profesor> getProfesor() {
        return mProfesor;
    }

    public void obtenerPerfil() {
        String token = TenisApi.leerToken(getApplication());
        if (token == null) {
            Toast.makeText(getApplication(), "Error: Token no encontrado.", Toast.LENGTH_SHORT).show();
            return;
        }

        TenisApi.TenisApiService api = TenisApi.getTenisApiService();
        Call<List<Profesor>> call = api.obtenerPerfil("Bearer " + token);

        call.enqueue(new Callback<List<Profesor>>() {
            @Override
            public void onResponse(Call<List<Profesor>> call, Response<List<Profesor>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Profesor> profesores = response.body();
                    if (!profesores.isEmpty()) {
                        mProfesor.postValue(profesores.get(0));
                    } else {
                        Toast.makeText(getApplication(), "Perfil no encontrado en la lista.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplication(), "Error al obtener perfil: " + response.code(), Toast.LENGTH_SHORT).show();
                    Log.e("PerfilViewModel", "Error en respuesta de obtenerPerfil: " + response.toString());
                }
            }

            @Override
            public void onFailure(Call<List<Profesor>> call, Throwable throwable) {
                Log.e("PerfilViewModel", "Fallo de conexión obtenerPerfil: " + throwable.getMessage());
                Toast.makeText(getApplication(), "Error de red: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void cambioBoton(String nombreBoton, String apellido, String telefono, String email, String clave) {


        if (nombreBoton.equalsIgnoreCase("EDITAR")) {
            mEstado.setValue(true);
            mNombreBoton.setValue("GUARDAR");
        }

        else {
            mEstado.setValue(false);
            mNombreBoton.setValue("EDITAR");

            Profesor profesorActual = mProfesor.getValue();
            if (profesorActual == null || profesorActual.getIdProfesor() <= 0) {
                Toast.makeText(getApplication(), "Error: Perfil no cargado o ID no disponible.", Toast.LENGTH_SHORT).show();
                mEstado.setValue(true);
                mNombreBoton.setValue("GUARDAR");
                return;
            }

            int idProfesor = profesorActual.getIdProfesor();
            Profesor actualizado = profesorActual; //

            // ===== VALIDACIONES =====
            if (apellido.trim().isEmpty() || telefono.trim().isEmpty() || email.trim().isEmpty() || clave.trim().isEmpty()) {
                Toast.makeText(getApplication(), "Todos los campos son obligatorios.", Toast.LENGTH_SHORT).show();
                mEstado.setValue(true);
                mNombreBoton.setValue("GUARDAR");
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(getApplication(), "El correo electrónico no tiene un formato válido.", Toast.LENGTH_SHORT).show();
                mEstado.setValue(true);
                mNombreBoton.setValue("GUARDAR");
                return;
            }


            actualizado.setapellido(apellido);
            actualizado.setTelefono(telefono);


            if (actualizado.getUsuario() != null) {
                actualizado.getUsuario().setEmail(email);
                actualizado.getUsuario().setClave(clave);
            } else {
                Log.e("PerfilViewModel", "Error: Objeto Usuario es NULL al intentar guardar.");
                Toast.makeText(getApplication(), "Error: Datos de usuario base faltantes.", Toast.LENGTH_SHORT).show();
                mEstado.setValue(true);
                mNombreBoton.setValue("GUARDAR");
                return;
            }
            // -------------------------------------------------------------

            // LLAMADA A LA API PARA ACTUALIZAR
            String token = TenisApi.leerToken(getApplication());
            TenisApi.TenisApiService api = TenisApi.getTenisApiService();

            Call<Profesor> call = api.actualizarProfesor("Bearer " + token, idProfesor, actualizado);

            call.enqueue(new Callback<Profesor>() {
                @Override
                public void onResponse(Call<Profesor> call, Response<Profesor> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getApplication(), "ACTUALIZADO CON ÉXITO", Toast.LENGTH_SHORT).show();

                        if (response.code() == 204) {
                            mProfesor.postValue(actualizado);
                        } else if (response.body() != null) {
                            mProfesor.postValue(response.body());
                        }

                        mEstado.postValue(false);
                        mNombreBoton.postValue("EDITAR");

                    } else {
                        Toast.makeText(getApplication(), "ERROR AL ACTUALIZAR: " + response.code(), Toast.LENGTH_SHORT).show();
                        Log.e("PerfilViewModel", "Error al actualizar: " + response.toString());
                        mEstado.postValue(true);
                        mNombreBoton.postValue("GUARDAR");
                    }
                }

                @Override
                public void onFailure(Call<Profesor> call, Throwable t) {
                    Log.e("PerfilViewModel", "Fallo de conexión actualizarPropietario: " + t.getMessage());
                    Toast.makeText(getApplication(), "Error de red al actualizar", Toast.LENGTH_SHORT).show();
                    mEstado.postValue(true);
                    mNombreBoton.postValue("GUARDAR");
                }
            });
        }
    }
}