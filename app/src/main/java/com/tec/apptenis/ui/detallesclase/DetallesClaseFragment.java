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

        if (getArguments() != null) {
            claseSeleccionada = (Clase) getArguments().getSerializable("clase_seleccionada");
        }

        if (claseSeleccionada != null) {
            mostrarDetallesClase(claseSeleccionada);
        }

        // Observa éxito
        viewModel.getMensajeExito().observe(getViewLifecycleOwner(), mensaje -> {
            Toast.makeText(getContext(), mensaje, Toast.LENGTH_LONG).show();

            // Ahora refrescamos la clase desde el servidor
            if (claseSeleccionada != null) {
                viewModel.refrescarClase(claseSeleccionada.getIdClase());
            }
        });

        // Observa error
        viewModel.getMensajeError().observe(getViewLifecycleOwner(), mensaje ->
                Toast.makeText(getContext(), mensaje, Toast.LENGTH_LONG).show()
        );

        // CLASE ACTUALIZADA LUEGO DEL REFRESH
        viewModel.getClaseActualizada().observe(getViewLifecycleOwner(), clase -> {
            if (clase != null) {
                claseSeleccionada = clase;
                mostrarDetallesClase(claseSeleccionada);  // <—— refresca correctamente los EditText
            }
        });

        binding.btnGuardarDevolucion.setOnClickListener(v -> guardarDevolucion());
    }

    private void mostrarDetallesClase(Clase clase) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("es", "ES"));
        String fechaStr = clase.getFecha() != null ? dateFormat.format(clase.getFecha()) : "N/A";
        String horaStr = clase.getHora() != null && clase.getHora().length() >= 5
                ? clase.getHora().substring(0, 5)
                : "N/A";

        String nombresAlumnos = clase.getClaseAlumnos().stream()
                .filter(ca -> ca.getAlumno() != null)
                .map(ca -> ca.getAlumno().getNombre())
                .collect(Collectors.joining(", "));

        // Refrescar devoluciones correctamente
        if (!clase.getClaseAlumnos().isEmpty()) {
            ClaseAlumno ca = clase.getClaseAlumnos().get(0);

            if (ca.getDevoluciones() != null && !ca.getDevoluciones().isEmpty()) {
                binding.etComentarioDevolucion.setText(ca.getDevoluciones().get(0).getComentario());
                binding.etEjemploDevolucion.setText(ca.getDevoluciones().get(0).getEjemplo());
            } else {
                binding.etComentarioDevolucion.setText("");
                binding.etEjemploDevolucion.setText("");
            }
        }

        binding.tvDetalleAlumno.setText(nombresAlumnos);
        binding.tvDetalleFechaHora.setText("📅 Fecha: " + fechaStr + " | ⌚ Hora: " + horaStr);
        binding.tvDetalleEstado.setText("Estado: " + clase.getEstado());
    }

    private void guardarDevolucion() {
        if (claseSeleccionada == null || claseSeleccionada.getClaseAlumnos().isEmpty()) {
            Toast.makeText(getContext(), "Error: No se encontró la clase o el alumno asociado.", Toast.LENGTH_SHORT).show();
            return;
        }

        String comentario = binding.etComentarioDevolucion.getText().toString().trim();
        String ejemplo = binding.etEjemploDevolucion.getText().toString().trim();

        if (comentario.isEmpty() || ejemplo.isEmpty()) {
            Toast.makeText(getContext(), "Complete los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        int idClaseAlumno = claseSeleccionada.getClaseAlumnos().get(0).getIdClaseAlumno();

        viewModel.guardarDevolucion(comentario, ejemplo, idClaseAlumno);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
