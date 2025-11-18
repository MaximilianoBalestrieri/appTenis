package com.tec.apptenis.ui.detallesclase;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.tec.apptenis.databinding.FragmentDetallesClaseBinding;
import com.tec.apptenis.model.Clase;
import com.tec.apptenis.model.ClaseAlumno;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.stream.Collectors;

public class DetallesClaseFragment extends Fragment {

    private FragmentDetallesClaseBinding binding;
    private DetallesClaseViewModel viewModel;
    private Clase claseSeleccionada;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentDetallesClaseBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(DetallesClaseViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Obtener la clase del Bundle
        if (getArguments() != null) {
            claseSeleccionada = (Clase) getArguments().getSerializable("clase_seleccionada");
        }

        if (claseSeleccionada != null) {
            mostrarDetallesClase(claseSeleccionada);
        }

        // 2. Observar mensajes del ViewModel
        viewModel.getMensajeExito().observe(getViewLifecycleOwner(), mensaje -> {
            Toast.makeText(getContext(), mensaje, Toast.LENGTH_LONG).show();
            // Opcional: Navegar de vuelta o limpiar campos
        });

        viewModel.getMensajeError().observe(getViewLifecycleOwner(), mensaje -> {
            Toast.makeText(getContext(), mensaje, Toast.LENGTH_LONG).show();
        });

        // 3. Configurar el botón de guardar
        binding.btnGuardarDevolucion.setOnClickListener(v -> guardarDevolucion());
    }

    private void mostrarDetallesClase(Clase clase) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("es", "ES"));
        String fechaStr = clase.getFecha() != null ? dateFormat.format(clase.getFecha()) : "N/A";
        String horaStr = clase.getHora() != null && clase.getHora().length() >= 5 ? clase.getHora().substring(0, 5) : "N/A";

        // Nombres de alumnos
        String nombresAlumnos = clase.getClaseAlumnos().stream()
                .filter(ca -> ca.getAlumno() != null)
                .map(ca -> ca.getAlumno().getNombre())
                .collect(Collectors.joining(", "));

        // Llenar detalles
        binding.tvDetalleAlumno.setText(nombresAlumnos);
        binding.tvDetalleFechaHora.setText(String.format("📅 Fecha: %s | ⌚ Hora: %s", fechaStr, horaStr));
        binding.tvDetalleEstado.setText("Estado: " + clase.getEstado());
    }

    private void guardarDevolucion() {
        if (claseSeleccionada == null || claseSeleccionada.getClaseAlumnos().isEmpty()) {
            Toast.makeText(getContext(), "Error: No se encontró la clase o el alumno asociado.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 1. Recopilar datos
        String comentario = binding.etComentarioDevolucion.getText().toString().trim();
        String ejemplo = binding.etEjemploDevolucion.getText().toString().trim();

        if (comentario.isEmpty() || ejemplo.isEmpty()) {
            Toast.makeText(getContext(), "Por favor, complete Comentario y Ejemplo.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Obtener IdClaseAlumno
        // ASUNCIÓN: Si hay múltiples alumnos, solo guardamos la devolución para el primero.
        // Si necesitas guardar una devolución por cada alumno, este Fragmento requerirá más complejidad (ej: un RecyclerView interno).
        int idClaseAlumno = claseSeleccionada.getClaseAlumnos().get(0).getIdClaseAlumno();

        // 3. Llamada al ViewModel
        viewModel.guardarDevolucion(comentario, ejemplo, idClaseAlumno);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}