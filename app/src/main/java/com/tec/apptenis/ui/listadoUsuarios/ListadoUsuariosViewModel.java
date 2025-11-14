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
import com.tec.apptenis.request.TenisApi.TenisApiService; // Importar la interfaz anidada

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListadoUsuariosViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Usuario>> listaUsuarios = new MutableLiveData<>();

    // 1. LiveData para comunicar el ID seleccionado a la vista (Fragment)
    private final MutableLiveData<Integer> usuarioSeleccionadoId = new MutableLiveData<>();

    private final MutableLiveData<String> mensajeResultado = new MutableLiveData<>();

    public ListadoUsuariosViewModel(@NonNull Application application) {
        super(application);
    }

    // --- GETTERS ---
    public LiveData<List<Usuario>> getListaUsuarios() {
        return listaUsuarios;
    }

    public LiveData<String> getMensajeResultado() {
        return mensajeResultado;
    }

    // 2. Getter para la navegación
    public LiveData<Integer> getUsuarioSeleccionadoId() {
        return usuarioSeleccionadoId;
    }

    // Método para limpiar el mensaje después de mostrarlo
    public void clearMensajeResultado() {
        mensajeResultado.setValue(null);
    }

    // --- FUNCIÓN DE SELECCIÓN DE USUARIO (NAVEGACIÓN) ---
    /**
     * Publica el ID del usuario seleccionado para que el Fragmento pueda navegar.
     * Esta función reemplaza la llamada a la API de creación de alumno.
     */
    public void seleccionarUsuarioParaRegistro(Usuario usuario) {
        if (usuario != null) {
            // El valor de LiveData cambia, notificando al Fragment para que navegue.
            usuarioSeleccionadoId.setValue(usuario.getIdUsuario());
        }
    }

    // NOTA: Se ha eliminado la función intentarCrearAlumno(Usuario usuario)
    // para corregir el flujo de la aplicación.


    // --- FUNCIÓN DE CARGA ---
    public void cargarUsuarios() {
        String token = TenisApi.leerToken(getApplication());

        if (token == null || token.isEmpty()) {
            Toast.makeText(getApplication(), "Error: Token de sesión no encontrado.", Toast.LENGTH_SHORT).show();
            listaUsuarios.postValue(new ArrayList<>());
            return;
        }

        TenisApiService api = TenisApi.getTenisApiService();
        Call<List<Usuario>> call = api.obtenerUsuarios("Bearer " + token);

        call.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(@NonNull Call<List<Usuario>> call, @NonNull Response<List<Usuario>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaUsuarios.postValue(response.body());
                } else {
                    Toast.makeText(getApplication(), "Error al cargar usuarios: " + response.code(), Toast.LENGTH_SHORT).show();
                    listaUsuarios.postValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Usuario>> call, @NonNull Throwable t) {
                Toast.makeText(getApplication(), "Error de red al cargar usuarios.", Toast.LENGTH_SHORT).show();
                listaUsuarios.postValue(new ArrayList<>());
            }
        });
    }
}