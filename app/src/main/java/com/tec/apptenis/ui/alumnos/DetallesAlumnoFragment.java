package com.tec.apptenis.ui.alumnos;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.tec.apptenis.databinding.FragmentDetallesAlumnoBinding;


import com.tec.apptenis.model.Alumno;

public class DetallesAlumnoFragment extends Fragment {

    private FragmentDetallesAlumnoBinding binding;
    private DetallesAlumnoViewModel viewModel;
    private boolean editando = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentDetallesAlumnoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        viewModel = new ViewModelProvider(this).get(DetallesAlumnoViewModel.class);

        // 1. Cargar el alumno (si el ID fue pasado)
        if (getArguments() != null) {
            viewModel.recuperarAlumno(getArguments());
        }

        // 2. Observar los datos del alumno y llenar la UI
        viewModel.getAlumno().observe(getViewLifecycleOwner(), alumno -> {
            if (alumno != null) {
                // Llenar campos
                binding.etNombre.setText(alumno.getNombre());
                binding.etTelefono.setText(alumno.getTelefono());
                binding.etDireccion.setText(alumno.getDireccion());
                binding.etLocalidad.setText(alumno.getLocalidad());
                binding.etNivel.setText(alumno.getNivel());
                binding.etManoHabil.setText(alumno.getManohabil());
                binding.etReves.setText(alumno.getReves());


                if (alumno.getIdAlumno() != 0) {
                    habilitarCampos(false);
                    binding.btnEditarActualizar.setText("Editar");
                    editando = false;
                } else {
                    // Si el ID es 0 (es creación), habilitamos para llenar
                    habilitarCampos(true);
                    binding.btnEditarActualizar.setText("Guardar Nuevo");
                    editando = true;
                }
            }
        });


        viewModel.getMensaje().observe(getViewLifecycleOwner(), msg -> {
            if (msg != null && !msg.isEmpty()) {
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });

        // botón Editar/Actualizar/Guardar
        binding.btnEditarActualizar.setOnClickListener(v -> {
            Alumno alumno = viewModel.getAlumno().getValue();
            if (alumno == null) return;

            if (!editando) {
                // Modo EDICIÓN -> Cambiar a modo ACTUALIZAR
                habilitarCampos(true);
                binding.btnEditarActualizar.setText("Actualizar");
                editando = true;
            } else {


                // 4.1. Recoger datos de los campos y actualizar el objeto
                alumno.setNombre(binding.etNombre.getText().toString());
                alumno.setTelefono(binding.etTelefono.getText().toString());
                alumno.setDireccion(binding.etDireccion.getText().toString());
                alumno.setLocalidad(binding.etLocalidad.getText().toString());
                alumno.setNivel(binding.etNivel.getText().toString());
                alumno.setManohabil(binding.etManoHabil.getText().toString());
                alumno.setReves(binding.etReves.getText().toString());

                //  Enviar a la API
                viewModel.actualizarAlumno(alumno);


                habilitarCampos(false);
                binding.btnEditarActualizar.setText("Editar");
                editando = false;
            }
        });

        return root;
    }


    private void habilitarCampos(boolean habilitar) {
        binding.etNombre.setEnabled(habilitar);
        binding.etTelefono.setEnabled(habilitar);
        binding.etDireccion.setEnabled(habilitar);
        binding.etLocalidad.setEnabled(habilitar);
        binding.etNivel.setEnabled(habilitar);
        binding.etManoHabil.setEnabled(habilitar);
        binding.etReves.setEnabled(habilitar);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}