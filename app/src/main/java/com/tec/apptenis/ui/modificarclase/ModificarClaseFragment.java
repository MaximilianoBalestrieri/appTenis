package com.tec.apptenis.ui.modificarclase;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.tec.apptenis.databinding.FragmentModificarClaseBinding;
import com.tec.apptenis.model.Clase;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ModificarClaseFragment extends Fragment {

    private FragmentModificarClaseBinding binding;
    private ModificarClaseViewModel viewModel;
    private Clase clase;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentModificarClaseBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(ModificarClaseViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            clase = (Clase) getArguments().getSerializable("clase_seleccionada");
        }

        if (clase != null) {
            binding.etHora.setText(clase.getHora());

            // Spinner de estados
            List<String> estados = Arrays.asList("Programada", "Cancelada");
            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                    android.R.layout.simple_spinner_item, estados);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.spinnerEstado.setAdapter(adapter);

            // Seleccionar el estado actual
            int index = estados.indexOf(clase.getEstado());
            binding.spinnerEstado.setSelection(index >= 0 ? index : 0);
        }

        // Observadores
        viewModel.getMensajeExito().observe(getViewLifecycleOwner(), msg -> {
            Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
            // Volver al detalle de la clase
            Navigation.findNavController(view).popBackStack();
        });

        viewModel.getMensajeError().observe(getViewLifecycleOwner(), msg ->
                Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show()
        );

        // Guardar cambios
        binding.btnGuardarCambios.setOnClickListener(v -> guardarCambios());
    }

    private void guardarCambios() {
        if (clase == null) return;

        String nuevaHora = binding.etHora.getText().toString().trim();
        String nuevoEstado = binding.spinnerEstado.getSelectedItem().toString();

        if (nuevaHora.isEmpty()) {
            Toast.makeText(getContext(), "Ingrese una hora válida.", Toast.LENGTH_SHORT).show();
            return;
        }

        viewModel.modificarClase(clase.getIdClase(), nuevaHora, nuevoEstado);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
