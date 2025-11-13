package com.tec.apptenis.ui.listadoUsuarios;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tec.apptenis.model.IdUsuarioRequest;
import com.tec.apptenis.model.Usuario;
import com.tec.apptenis.model.Alumno; // Import necesario para la respuesta de creación
import com.tec.apptenis.request.TenisApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListadoUsuariosViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Usuario>> listaUsuarios = new MutableLiveData<>();
    private final MutableLiveData<String> mensajeResultado = new MutableLiveData<>(); // LiveData para mensajes de resultado

    public ListadoUsuariosViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Usuario>> getListaUsuarios() {
        return listaUsuarios;
    }

    public LiveData<String> getMensajeResultado() {
        return mensajeResultado;
    }

    // 🔥 NUEVO MÉTODO: Permite al Fragment limpiar el valor del LiveData
    public void clearMensajeResultado() {
        mensajeResultado.setValue(null);
    }

    // --- FUNCIÓN DE CARGA ---
    public void cargarUsuarios() {
        Log.d("ListadoUsuariosVM", "-> Iniciando función cargarUsuarios().");
        String token = TenisApi.leerToken(getApplication());

        // Manejo estricto del token
        if (token == null || token.isEmpty()) {
            Log.e("ListadoUsuariosVM", "Token es nulo o vacío.");
            Toast.makeText(getApplication(), "Error: Token de sesión no encontrado.", Toast.LENGTH_SHORT).show();
            listaUsuarios.postValue(new ArrayList<>()); // Vaciar lista para evitar pantalla en blanco
            return;
        }

        Log.d("ListadoUsuariosVM", "Token encontrado. Iniciando llamada a la API.");

        TenisApi.TenisApiService api = TenisApi.getTenisApiService();
        Call<List<Usuario>> call = api.obtenerUsuarios("Bearer " + token);

        call.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(@NonNull Call<List<Usuario>> call, @NonNull Response<List<Usuario>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaUsuarios.postValue(response.body());
                } else {
                    // Si hay un error de respuesta HTTP
                    Toast.makeText(getApplication(), "Error al cargar usuarios: " + response.code(), Toast.LENGTH_SHORT).show();
                    Log.e("ListadoUsuariosVM", "Error en respuesta: " + response.toString());
                    listaUsuarios.postValue(new ArrayList<>()); // Vaciar lista
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Usuario>> call, @NonNull Throwable t) {
                // Si hay un error de red/conexión
                Log.e("ListadoUsuariosVM", "Fallo de conexión: " + t.getMessage());
                Toast.makeText(getApplication(), "Error de red al cargar usuarios.", Toast.LENGTH_SHORT).show();
                listaUsuarios.postValue(new ArrayList<>()); // Vaciar lista
            }
        });
    }

    // --- FUNCIÓN DE CREACIÓN DE ALUMNO ---
    public void intentarCrearAlumno(Usuario usuario) {
        String token = TenisApi.leerToken(getApplication());

        if (token == null || usuario == null) {
            mensajeResultado.postValue("Error: Sesión expirada o usuario no válido.");
            return;
        }

        TenisApi.TenisApiService api = TenisApi.getTenisApiService();
        IdUsuarioRequest requestBody = new IdUsuarioRequest(usuario.getIdUsuario());

        Call<Alumno> call = api.crearAlumnoDesdeUsuario("Bearer " + token, requestBody);
        call.enqueue(new Callback<Alumno>() {
            @Override
            public void onResponse(@NonNull Call<Alumno> call, @NonNull Response<Alumno> response) {
                if (response.isSuccessful()) {
                    mensajeResultado.postValue("✅ Éxito: Alumno creado: " + usuario.getEmail());
                    // Después de crear un alumno, recargamos la lista si fuera necesario.
                    // cargarUsuarios();
                }
                // Código 409 Conflict: El recurso ya existe (el usuario ya es alumno)
                else if (response.code() == 409) {
                    mensajeResultado.postValue("❌ Error: El usuario " + usuario.getEmail() + " ya es un Alumno.");
                }
                else {
                    mensajeResultado.postValue("❌ Error al crear Alumno: " + response.code());
                    Log.e("ListadoUsuariosVM", "Fallo creación: " + response.toString());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Alumno> call, @NonNull Throwable t) {
                mensajeResultado.postValue("❌ Error de red/servidor al intentar crear Alumno.");
                Log.e("ListadoUsuariosVM", "Fallo de red: " + t.getMessage());
            }
        });
    }
}