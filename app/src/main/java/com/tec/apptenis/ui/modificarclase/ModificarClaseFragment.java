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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.tec.apptenis.databinding.FragmentModificarClaseBinding;
import com.tec.apptenis.model.Clase;

public class ModificarClaseFragment extends Fragment {

    private FragmentModificarClaseBinding binding;
    private ModificarClaseViewModel viewModel;
    private Clase claseSeleccionada;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentModificarClaseBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(ModificarClaseViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController navController = Navigation.findNavController(view);

        if (getArguments() != null) {
            claseSeleccionada = (Clase) getArguments().getSerializable("clase_seleccionada");
            if (claseSeleccionada != null) {
                cargarDatosClase();
            }
        }

        viewModel.getMensajeExito().observe(getViewLifecycleOwner(), mensaje -> {
            Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
            navController.popBackStack(); // Volvemos al DetallesClaseFragment
        });

        viewModel.getMensajeError().observe(getViewLifecycleOwner(), mensaje ->
                Toast.makeText(getContext(), mensaje, Toast.LENGTH_LONG).show()
        );

        binding.btnGuardarCambios.setOnClickListener(v -> guardarCambios());
    }

    private void cargarDatosClase() {
        // Spinner de estado
        String[] estados = {"Programada", "Cancelada", "Finalizada"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, estados);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerEstado.setAdapter(adapter);

        binding.spinnerEstado.setSelection(claseSeleccionada.getEstado().equals("Programada") ? 0 : 1);

        binding.etHora.setText(claseSeleccionada.getHora());
        binding.etComentario.setText(claseSeleccionada.getComentario());
    }

    private void guardarCambios() {
        if (claseSeleccionada == null) return;

        claseSeleccionada.setEstado(binding.spinnerEstado.getSelectedItem().toString());
        claseSeleccionada.setHora(binding.etHora.getText().toString().trim());
        claseSeleccionada.setComentario(binding.etComentario.getText().toString().trim());

        viewModel.actualizarClase(claseSeleccionada);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
