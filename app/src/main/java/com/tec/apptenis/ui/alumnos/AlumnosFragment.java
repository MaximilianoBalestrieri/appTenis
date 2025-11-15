package com.tec.apptenis.ui.alumnos;

import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
// 🔥 IMPORTACIÓN NECESARIA para Navigation Component
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tec.apptenis.R; // Necesario para IDs de acciones de navegación
import com.tec.apptenis.databinding.FragmentAlumnosBinding;
import com.tec.apptenis.model.Alumno;
// No necesitamos estas importaciones si usamos el NavController
// import com.tec.apptenis.ui.alumnos.DetallesAlumnoFragment;
// import com.tec.apptenis.MainActivity;


import java.util.ArrayList;
import java.util.List;

// Implementamos la interfaz del Adapter para escuchar clics
public class AlumnosFragment extends Fragment implements AlumnosAdapter.OnAlumnoClickListener {

    private AlumnosViewModel mViewModel;
    private FragmentAlumnosBinding binding;
    private AlumnosAdapter adapter;
    private static final String TAG = "AlumnosFragment";

    // **IMPORTANTE**: Define el ID de la ACCIÓN de navegación aquí
    // Debes reemplazar 'R.id.action_alumnosFragment_to_detalleAlumnoFragment'
    // con el ID de la acción que definiste en tu archivo nav_graph.xml
    private static final int ACTION_TO_DETALLE_ALUMNO = R.id.action_alumnosFragment_to_detalleAlumnoFragment;

    // Si no usas la acción, usa el ID del destino (Fragmento) de Detalle: R.id.nav_detalle_alumno

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentAlumnosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Configurar RecyclerView y Adapter
        // Pasamos 'this' (el Fragment) como el Listener al Adapter
        adapter = new AlumnosAdapter(new ArrayList<Alumno>(), this);
        binding.rvAlumnos.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvAlumnos.setAdapter(adapter);

        // Configurar el Floating Action Button (FAB)
        binding.fabAgregarAlumno.setOnClickListener(v -> {
            navigateToAltaAlumno();
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(AlumnosViewModel.class);

        // Observar LiveData y actualizar el Adapter
        mViewModel.getListaAlumnos().observe(getViewLifecycleOwner(), alumnos -> {
            List<Alumno> lista = alumnos != null ? alumnos : new ArrayList<>();
            adapter.setAlumnos(lista);

            // Lógica para mostrar/ocultar la vista vacía
            if (lista.isEmpty()) {
                binding.rvAlumnos.setVisibility(View.GONE);
                binding.tvEmptyList.setVisibility(View.VISIBLE);
            } else {
                binding.rvAlumnos.setVisibility(View.VISIBLE);
                binding.tvEmptyList.setVisibility(View.GONE);
            }
        });

        // Iniciar la carga de datos
        mViewModel.cargarAlumnos();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // --- IMPLEMENTACIÓN DEL CLICK LISTENER PARA EDICIÓN (CORREGIDO) ---
    @Override
    public void onAlumnoClick(Alumno alumno) {
        Log.d(TAG, "Alumno seleccionado para EDITAR: ID=" + alumno.getIdAlumno());

        // 1. Crear el Bundle y pasar el ID del alumno
        Bundle bundle = new Bundle();
        // Usamos la clave que el DetalleAlumnoFragment espera
        bundle.putInt("alumno_id", alumno.getIdAlumno());

        // 2. Navegar usando el NavController
        try {
            // El NavController gestiona la pila de retroceso automáticamente,
            // solucionando el problema del botón 'Atrás'.
           // Navigation.findNavController(requireView()).navigate(ACTION_TO_DETALLE_ALUMNO, bundle);
            Navigation.findNavController(requireView()).navigate(
                    R.id.action_alumnosFragment_to_detalleAlumnoFragment,
                    bundle
            );
        } catch (IllegalArgumentException e) {
            // Esto ocurre si el ACTION_TO_DETALLE_ALUMNO no está definido correctamente en el nav_graph.xml
            Log.e(TAG, "Error de navegación (ID de acción incorrecto): " + e.getMessage());
            Toast.makeText(getContext(), "Error de navegación: verifica el nav_graph.", Toast.LENGTH_LONG).show();
        }
    }


    // --- LÓGICA DE NAVEGACIÓN PARA ALTA (Ejemplo usando NavController) ---
    private void navigateToAltaAlumno() {
        Log.d(TAG, "FAB presionado. Iniciando alta de alumno...");
        Toast.makeText(getContext(), "Navegando a Alta de Alumno...", Toast.LENGTH_SHORT).show();

        // Ejemplo de cómo navegar a un destino 'nav_alta_alumno' usando NavController
        // Navigation.findNavController(requireView()).navigate(R.id.nav_alta_alumno);
    }
}