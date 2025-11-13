package com.tec.apptenis.ui.alumnos;

import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tec.apptenis.databinding.FragmentAlumnosBinding;
import com.tec.apptenis.model.Alumno;
// Asegúrate de importar la Activity que maneja la navegación (Ej: MainActivity)
// import com.tec.apptenis.MainActivity;
// import com.tec.apptenis.ui.altaAlumno.AltaAlumnoFragment; // Fragmento de destino (futuro)


import java.util.ArrayList;
import java.util.List;

public class AlumnosFragment extends Fragment {

    private AlumnosViewModel mViewModel;
    private FragmentAlumnosBinding binding;
    private AlumnosAdapter adapter;
    private static final String TAG = "AlumnosFragment"; // Para Logcat

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentAlumnosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // 1. Configurar RecyclerView y Adapter
        adapter = new AlumnosAdapter(new ArrayList<Alumno>());
        binding.rvAlumnos.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvAlumnos.setAdapter(adapter);

        // 🔥 2. Configurar el Floating Action Button (FAB) 🔥
        binding.fabAgregarAlumno.setOnClickListener(v -> {
            navigateToAltaAlumno();
        });

        // NOTA: El ViewModel se inicializa en onViewCreated (buena práctica)

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(AlumnosViewModel.class);

        // 3. Observar LiveData y actualizar el Adapter
        mViewModel.getListaAlumnos().observe(getViewLifecycleOwner(), alumnos -> {
            List<Alumno> lista = alumnos != null ? alumnos : new ArrayList<>();
            adapter.setAlumnos(lista); // Siempre actualizamos el adapter, aunque sea con una lista vacía

            // 💡 Lógica para mostrar/ocultar la vista vacía (tvEmptyList)
            if (lista.isEmpty()) {
                binding.rvAlumnos.setVisibility(View.GONE);
                binding.tvEmptyList.setVisibility(View.VISIBLE);
            } else {
                binding.rvAlumnos.setVisibility(View.VISIBLE);
                binding.tvEmptyList.setVisibility(View.GONE);
            }
        });

        // 4. Iniciar la carga de datos
        mViewModel.cargarAlumnos();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // --- LÓGICA DE NAVEGACIÓN ---
    private void navigateToAltaAlumno() {
        Log.d(TAG, "FAB presionado. Iniciando alta de alumno...");
        Toast.makeText(getContext(), "Navegando a Alta de Alumno...", Toast.LENGTH_SHORT).show();

        // 💡 FUTURA IMPLEMENTACIÓN:
        // Cuando crees el Fragmento para el alta, lo harás aquí,
        // generalmente delegando la navegación a la Activity principal.

        /* Fragment altaFragment = new AltaAlumnoFragment(); // Ejemplo
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).navigateToFragment(
                    altaFragment,
                    "Alta de Alumno",
                    null
            );
        }
        */
    }
}