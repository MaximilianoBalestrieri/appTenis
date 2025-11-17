package com.tec.apptenis.ui.clase;

import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.tec.apptenis.R;
import com.tec.apptenis.databinding.FragmentClaseBinding;
import com.tec.apptenis.model.Alumno;
// 🟢 IMPORTAR EL DTO DE PETICIÓN
import com.tec.apptenis.model.ClaseCreacionRequest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors; // Necesario para la conversión de IDs en Java 8+

public class ClaseFragment extends Fragment implements ClaseAdapter.OnSelectionChangeListener {

    private ClaseViewModel mViewModel;
    private FragmentClaseBinding binding;
    private ClaseAdapter adapter;
    private static final String TAG = "ClaseFragment";

    // Formateadores para java.time (útiles para mostrar/parsear)
    private final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    // 🟢 NUEVO: Formateador para la API (el backend C# espera la hora con segundos)
    private final DateTimeFormatter TIME_FORMATTER_API = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentClaseBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Inicialización y configuración de componentes de la UI
        setupSpinnerEstado();
        setupDateTimePickers();
        setupRecyclerView();

        binding.btnGuardarClase.setOnClickListener(v -> {
            guardarClase();
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(ClaseViewModel.class);

        // Observar LiveData de Alumnos Seleccionables
        mViewModel.alumnosDisponibles.observe(getViewLifecycleOwner(), alumnos -> {
            adapter.submitList(alumnos);

            // Lógica para lista vacía
            boolean isEmpty = alumnos == null || alumnos.isEmpty();
            binding.rvAlumnosSeleccion.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
            binding.tvEmptyListAlumnos.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        });

        // Observar y manejar posibles mensajes de éxito/error del ViewModel (Opcional, pero recomendado)
        // mViewModel.getResultadoGuardado().observe(getViewLifecycleOwner(), resultado -> {
        //     // Aquí puedes resetear el formulario o navegar si el guardado fue exitoso
        // });

        // Iniciar la carga de datos de los alumnos desde la API
        mViewModel.cargarAlumnosParaSeleccion();
    }

    // --- MÉTODOS DE CONFIGURACIÓN DE UI ---

    private void setupRecyclerView() {
        adapter = new ClaseAdapter(new ArrayList<>(), this);
        binding.rvAlumnosSeleccion.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvAlumnosSeleccion.setAdapter(adapter);
    }

    private void setupSpinnerEstado() {
        String[] estados = {"Programada", "Realizada", "Cancelada"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                estados
        );
        binding.spinnerEstado.setAdapter(adapter);
    }

    private void setupDateTimePickers() {
        // --- Selector de Fecha ---
        binding.tvFecha.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                    (view, y, m, d) -> {
                        LocalDate selectedDate = LocalDate.of(y, m + 1, d);
                        binding.tvFecha.setText(selectedDate.format(DATE_FORMATTER));
                    }, year, month, day);
            datePickerDialog.show();
        });

        // --- Selector de Hora ---
        binding.tvHora.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(),
                    (view, h, m) -> {
                        LocalTime selectedTime = LocalTime.of(h, m);
                        binding.tvHora.setText(selectedTime.format(TIME_FORMATTER));
                    }, hour, minute, true);
            timePickerDialog.show();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // --- IMPLEMENTACIÓN DEL LISTENER DEL ADAPTER ---

    @Override
    public void onAlumnoSelectionChanged(int position, boolean isChecked) {
        mViewModel.actualizarEstadoSeleccion(position, isChecked);
    }

    // ----------------------------------------------------------------------
    // 🟢 LÓGICA FINAL PARA GUARDAR LA CLASE
    // ----------------------------------------------------------------------
    private void guardarClase() {
        // 1. Obtener datos de la UI
        String fechaStr = binding.tvFecha.getText().toString();
        String horaStr = binding.tvHora.getText().toString();
        String comentario = binding.etComentario.getText().toString();
        String estado = binding.spinnerEstado.getSelectedItem().toString();

        // 2. Validación
        if (fechaStr.isEmpty() || horaStr.isEmpty()) {
            Toast.makeText(getContext(), "Debes seleccionar la fecha y hora.", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Alumno> alumnosSeleccionados = mViewModel.getAlumnosSeleccionados();
        if (alumnosSeleccionados.isEmpty()) {
            Toast.makeText(getContext(), "Debes seleccionar al menos un alumno.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 3. Preparación de datos para la API
        try {
            // 3.1 Obtener solo los IDs de los alumnos
            List<Integer> idsAlumnos = alumnosSeleccionados.stream()
                    .map(Alumno::getIdAlumno) // Asume que Alumno tiene getIdAlumno()
                    .collect(Collectors.toList());

            // 3.2 Formatear la hora correctamente (HH:mm:ss)
            LocalTime horaLocal = LocalTime.parse(horaStr, TIME_FORMATTER);
            String horaApiStr = horaLocal.format(TIME_FORMATTER_API);

            // 3.3 Obtener el ID del profesor
            // ⚠️ REEMPLAZA ESTE VALOR ⚠️
            // DEBES OBTENER ESTO DE TU SISTEMA DE AUTENTICACIÓN/SESIÓN.
            int idProfesorActual = 1;

            // 4. Crear el Objeto de Petición (DTO)
            ClaseCreacionRequest request = new ClaseCreacionRequest(
                    fechaStr,
                    horaApiStr,
                    idProfesorActual,
                    estado,
                    comentario,
                    idsAlumnos
            );

            // 5. Llamar al ViewModel para enviar a la API REST
            mViewModel.guardarNuevaClase(request);

        } catch (Exception e) {
            Log.e(TAG, "Error al procesar datos para la API: " + e.getMessage());
            Toast.makeText(getContext(), "Error en el formato de datos o en la lista de alumnos.", Toast.LENGTH_LONG).show();
        }
    }
}