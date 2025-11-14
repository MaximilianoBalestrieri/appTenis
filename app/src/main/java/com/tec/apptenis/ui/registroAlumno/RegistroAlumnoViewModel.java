package com.tec.apptenis.ui.registroAlumno;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tec.apptenis.model.AlumnoRegistroDTO;
import com.tec.apptenis.request.TenisApi;
import com.tec.apptenis.request.TenisApi.TenisApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroAlumnoViewModel extends AndroidViewModel {

    private final TenisApiService apiService;
    private MutableLiveData<Integer> registroResult;

    // LiveData para notificar al Fragmento si el usuario ya es un alumno.
    private MutableLiveData<Boolean> alumnoYaExiste = new MutableLiveData<>();

    public RegistroAlumnoViewModel(@NonNull Application application) {
        super(application);
        apiService = TenisApi.getTenisApiService();
    }

    // --- Getters ---
    public LiveData<Integer> getRegistroResult() {
        if (registroResult == null) {
            registroResult = new MutableLiveData<>();
        }
        return registroResult;
    }

    public LiveData<Boolean> getAlumnoYaExiste() {
        return alumnoYaExiste;
    }

    // --- Lógica de Negocio ---

    /**
     * Llama al endpoint GET /existe para verificar si el usuario ya tiene perfil.
     * Usa el método TenisApiService.verificarExistenciaAlumno que devuelve Call<Boolean>.
     */
    public void verificarPerfilExistente(int usuarioId) {
        String token = TenisApi.leerToken(getApplication());

        if (token == null || usuarioId <= 0) {
            alumnoYaExiste.setValue(null); // Indica error o estado no verificable
            return;
        }

        // 🛑 Usamos tu método existente y correcto: verificarExistenciaAlumno
        apiService.verificarExistenciaAlumno("Bearer " + token, usuarioId).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // response.body() es true si existe, false si no
                    alumnoYaExiste.setValue(response.body());
                } else {
                    // Error HTTP, no podemos confirmar la existencia
                    alumnoYaExiste.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                // Error de red
                alumnoYaExiste.setValue(null);
            }
        });
    }

    /**
     * Realiza la llamada final para completar el registro (POST /completar).
     */
    public void registrarAlumno(AlumnoRegistroDTO dto) {

        if (dto.getUsuarioId() <= 0) {
            registroResult.setValue(0);
            return;
        }

        apiService.completarRegistroAlumno(dto).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // 201 Created (o 200) si fue exitoso. 409 Conflict si ya existía.
                registroResult.setValue(response.code());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                registroResult.setValue(-1);
            }
        });
    }
}