package com.tec.apptenis.ui.listadoClases;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tec.apptenis.model.Clase;
import com.tec.apptenis.request.TenisApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListadoClasesViewModel extends AndroidViewModel {

    // LiveData que contendrá la lista de Clases obtenida de la API
    private final MutableLiveData<List<Clase>> _clases = new MutableLiveData<>();
    public LiveData<List<Clase>> getClases() {
        return _clases;
    }

    public ListadoClasesViewModel(@NonNull Application application) {
        super(application);
        // Cargar las clases inmediatamente al inicializar el ViewModel
        cargarClases();
    }

    /**
     * Carga la lista de clases desde la API REST.
     */
    public void cargarClases() {
        String token = TenisApi.leerToken(getApplication());

        if (token == null || token.isEmpty()) {
            Toast.makeText(getApplication(), "Error: Token no encontrado.", Toast.LENGTH_LONG).show();
            _clases.postValue(new ArrayList<>());
            return;
        }

        TenisApi.TenisApiService api = TenisApi.getTenisApiService();
        // ⚠️ Asumo que tienes un método 'obtenerClases()' en tu TenisApiService
        Call<List<Clase>> call = api.obtenerClases("Bearer " + token);

        call.enqueue(new Callback<List<Clase>>() {
            @Override
            public void onResponse(@NonNull Call<List<Clase>> call, @NonNull Response<List<Clase>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    _clases.setValue(response.body());
                } else {
                    Log.e("ListadoClasesVM", "Error al cargar clases: " + response.code());
                    Toast.makeText(getApplication(), "Error al cargar clases. Código: " + response.code(), Toast.LENGTH_SHORT).show();
                    _clases.setValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Clase>> call, @NonNull Throwable t) {
                Log.e("ListadoClasesVM", "Fallo de conexión: " + t.getMessage());
                Toast.makeText(getApplication(), "Error de red al cargar clases.", Toast.LENGTH_SHORT).show();
                _clases.setValue(new ArrayList<>());
            }
        });
    }
}