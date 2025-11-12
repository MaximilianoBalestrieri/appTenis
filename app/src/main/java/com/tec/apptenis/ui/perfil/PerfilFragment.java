package com.tec.apptenis.ui.perfil;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tec.apptenis.R;
import com.tec.apptenis.databinding.FragmentPerfilBinding;

public class PerfilFragment extends Fragment {

    private PerfilViewModel mViewModel;
    private FragmentPerfilBinding binding;



    public static PerfilFragment newInstance() {
        return new PerfilFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding=FragmentPerfilBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(PerfilViewModel.class);
        View root=binding.getRoot();

        mViewModel.getProfesor().observe(getViewLifecycleOwner(), profesor ->  {

            if (profesor.getIdProfesor() != 0) {
                binding.etId.setText(String.valueOf(profesor.getIdProfesor()));
            }
            binding.etNombre.setText(profesor.getapellido());
            binding.etTelefono.setText(profesor.getTelefono());

            // 🔥 CORRECCIÓN CLAVE: Acceder a Email y Clave a través de getUsuario()
            if (profesor.getUsuario() != null) {

                binding.etEmail.setText(profesor.getUsuario().getEmail());
                binding.etContra.setText(profesor.getUsuario().getClave());
            }
            // Nota: Las llamadas profesor.getEmail() y profesor.getClave() han sido reemplazadas.
        });

        mViewModel.obtenerPerfil();

        mViewModel.getmEstado().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                binding.etNombre.setEnabled(aBoolean);
                binding.etTelefono.setEnabled(aBoolean);
                binding.etEmail.setEnabled(aBoolean);
                binding.etContra.setEnabled(aBoolean);
            }
        });

        mViewModel.getmNombreBoton().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.btnEditarGuardar.setText(s);
            }
        });

        binding.btnEditarGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.cambioBoton(
                        binding.btnEditarGuardar.getText().toString(),
                        binding.etNombre.getText().toString(),
                        binding.etTelefono.getText().toString(),
                        binding.etEmail.getText().toString(),
                        binding.etContra.getText().toString()
                );
            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO: Use the ViewModel
    }

}