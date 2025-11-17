package com.tec.apptenis.ui.clase;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tec.apptenis.model.Alumno;
import com.tec.apptenis.model.AlumnoSeleccionable;
import com.tec.apptenis.model.Clase;
import com.tec.apptenis.request.TenisApi;
import com.tec.apptenis.model.ClaseCreacionRequest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClaseViewModel extends AndroidViewModel {

    // Lista observable que contendrá los alumnos y su estado de selección (true/false)
    private final MutableLiveData<List<AlumnoSeleccionable>> _alumnosDisponibles = new MutableLiveData<>();
    public LiveData<List<AlumnoSeleccionable>> alumnosDisponibles = _alumnosDisponibles;

    // LiveData para comunicar el evento de éxito y navegación/cierre
    private final MutableLiveData<Boolean> _claseGuardadaExitosa = new MutableLiveData<>();
    public LiveData<Boolean> getClaseGuardadaExitosa() {
        return _claseGuardadaExitosa;
    }

    public ClaseViewModel(@NonNull Application application) {
        super(application);
        // Inicializar a false para evitar que se dispare al inicio
        _claseGuardadaExitosa.setValue(false);
    }

    // 🟢 MÉTODO DE RESETEO: Público y a nivel de clase
    /**
     * Resetea el LiveData de éxito para evitar que el Fragment navegue al rotar
     * o al regresar si el LiveData ya estaba en true.
     */
    public void resetClaseGuardadaEstado() {
        _claseGuardadaExitosa.setValue(false);
    }

    /**
     * Carga todos los alumnos usando la API REST.
     */
    public void cargarAlumnosParaSeleccion() {
        String token = TenisApi.leerToken(getApplication());

        if (token == null || token.isEmpty()) {
            Toast.makeText(getApplication(), "Error: Token no encontrado.", Toast.LENGTH_LONG).show();
            _alumnosDisponibles.postValue(new ArrayList<>());
            return;
        }

        TenisApi.TenisApiService api = TenisApi.getTenisApiService();
        Call<List<Alumno>> call = api.obtenerAlumnos("Bearer " + token);

        call.enqueue(new Callback<List<Alumno>>() {
            @Override
            public void onResponse(@NonNull Call<List<Alumno>> call, @NonNull Response<List<Alumno>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // 1. Convertir la lista de Alumno a AlumnoSeleccionable
                    List<AlumnoSeleccionable> listaSeleccionable = new ArrayList<>();
                    for (Alumno alumno : response.body()) {
                        // Inicialmente, todos están deseleccionados (false)
                        listaSeleccionable.add(new AlumnoSeleccionable(alumno, false));
                    }
                    // 2. Publicar la lista para el Fragment/Adapter
                    _alumnosDisponibles.postValue(listaSeleccionable);
                } else {
                    Toast.makeText(getApplication(), "Error al cargar alumnos: " + response.code(), Toast.LENGTH_SHORT).show();
                    _alumnosDisponibles.postValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Alumno>> call, @NonNull Throwable t) {
                Log.e("ClaseViewModel", "Fallo de conexión: " + t.getMessage());
                Toast.makeText(getApplication(), "Error de red al cargar alumnos.", Toast.LENGTH_SHORT).show();
                _alumnosDisponibles.postValue(new ArrayList<>());
            }
        });
    }

    /**
     * Actualiza el estado de selección en el modelo de datos.
     */
    public void actualizarEstadoSeleccion(int position, boolean isChecked) {
        List<AlumnoSeleccionable> currentList = _alumnosDisponibles.getValue();
        if (currentList != null && position >= 0 && position < currentList.size()) {
            currentList.get(position).setSelected(isChecked);
            // Notificar al LiveData
            _alumnosDisponibles.setValue(currentList);
        }
    }

    /**
     * Obtiene solo los objetos Alumno que fueron seleccionados.
     * @return Una lista de objetos Alumno.
     */
    public List<Alumno> getAlumnosSeleccionados() {
        List<Alumno> seleccionados = new ArrayList<>();
        List<AlumnoSeleccionable> currentList = _alumnosDisponibles.getValue();

        if (currentList != null) {
            for (AlumnoSeleccionable item : currentList) {
                if (item.isSelected()) {
                    seleccionados.add(item.getAlumno());
                }
            }
        }
        return seleccionados;
    }


    // ----------------------------------------------------------------------
    // MÉTODO guardarNuevaClase
    // ----------------------------------------------------------------------
    /**
     * Envía la solicitud de creación de clase con alumnos inscritos a la API REST.
     * @param request El DTO con los datos de la Clase y los IDs de los Alumnos.
     */
    public void guardarNuevaClase(ClaseCreacionRequest request) {
        String token = TenisApi.leerToken(getApplication());
        if (token == null || token.isEmpty()) {
            Toast.makeText(getApplication(), "Token no encontrado. No se puede guardar.", Toast.LENGTH_LONG).show();
            return;
        }

        TenisApi.TenisApiService api = TenisApi.getTenisApiService();
        Call<Clase> call = api.crearClase("Bearer " + token, request);

        call.enqueue(new Callback<Clase>() {
            @Override
            public void onResponse(@NonNull Call<Clase> call, @NonNull Response<Clase> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplication(), "✅ Clase creada con éxito!", Toast.LENGTH_LONG).show();
                    // 🟢 ACCIÓN CLAVE: Activar la señal de éxito para que el Fragment la observe
                    _claseGuardadaExitosa.setValue(true);

                } else {
                    Log.e("ClaseViewModel", "Error al crear clase: " + response.code() + " - " + response.message());
                    Toast.makeText(getApplication(), "❌ Error al crear clase. Código: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Clase> call, @NonNull Throwable t) {
                Log.e("ClaseViewModel", "Fallo de red al crear clase: " + t.getMessage());
                Toast.makeText(getApplication(), "❌ Error de red al intentar guardar la clase.", Toast.LENGTH_LONG).show();
            }
        });
    }
}