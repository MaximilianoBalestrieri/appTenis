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

    // --- NUEVAS PROPIEDADES AÑADIDAS ---
    // 1. LiveData que comunica al Fragmento el resultado de la verificación
    private final MutableLiveData<UsuarioExistencia> existenciaAlumno = new MutableLiveData<>();

    // --- PROPIEDADES EXISTENTES ---
    private final MutableLiveData<List<Usuario>> listaUsuarios = new MutableLiveData<>();
    private final MutableLiveData<String> mensajeResultado = new MutableLiveData<>();

    // Eliminamos 'usuarioSeleccionadoId' ya que será reemplazado por 'existenciaAlumno'
    // private final MutableLiveData<Integer> usuarioSeleccionadoId = new MutableLiveData<>();

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

    // 2. Getter para la verificación de existencia
    public LiveData<UsuarioExistencia> getExistenciaAlumno() {
        return existenciaAlumno;
    }

    // Método para limpiar el mensaje después de mostrarlo
    public void clearMensajeResultado() {
        mensajeResultado.setValue(null);
    }

    // Método para limpiar el LiveData de existencia después de la navegación/notificación
    public void clearExistenciaAlumno() {
        existenciaAlumno.setValue(null);
    }

    // --- FUNCIÓN DE VERIFICACIÓN (REEMPLAZA seleccionarUsuarioParaRegistro) ---
    /**
     * Llama a la API para verificar si el usuario ya es un alumno.
     * El resultado se publica en el LiveData 'existenciaAlumno'.
     */
    public void verificarYNavegar(Usuario usuario) {
        if (usuario == null) return;

        String token = TenisApi.leerToken(getApplication());
        TenisApiService api = TenisApi.getTenisApiService();

        // El endpoint es: GET api/Alumnos/existe/{usuarioId}
        Call<Boolean> call = api.verificarExistenciaAlumno("Bearer " + token, usuario.getIdUsuario());

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                if (response.isSuccessful() && response.body() != null) {
                    boolean existe = response.body();

                    // Publicamos el resultado en el LiveData
                    // Esto activa el observador en el ListadoUsuariosFragment
                    existenciaAlumno.setValue(
                            new UsuarioExistencia(usuario.getIdUsuario(), existe, usuario.getEmail())
                    );
                } else {
                    mensajeResultado.postValue("Error en la verificación de existencia: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                Log.e("ListadoUsuariosVM", "Error de red en verificación: " + t.getMessage());
                mensajeResultado.postValue("Error de red: No se pudo verificar la existencia del alumno.");
            }
        });
    }

    // --- FUNCIÓN DE CARGA ---
    public void cargarUsuarios() {
        // ... (Tu código de cargarUsuarios existente se mantiene igual)
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


    // --- CLASE AUXILIAR INTERNA (Necesaria para comunicar ID, Nombre y Estado) ---
    /**
     * Clase auxiliar que agrupa el ID, el nombre y el resultado de existencia
     * para enviarlo de forma segura al Fragmento.
     */
    public static class UsuarioExistencia {
        private final int userId;
        private final boolean existe;
        private final String nombreUsuario;

        public UsuarioExistencia(int userId, boolean existe, String nombreUsuario) {
            this.userId = userId;
            this.existe = existe;
            this.nombreUsuario = nombreUsuario;
        }

        public int getUserId() {
            return userId;
        }

        public boolean isExiste() {
            return existe;
        }

        public String getNombreUsuario() {
            return nombreUsuario;
        }
    }
}