package com.tec.apptenis.ui.listadoClases;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;


import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tec.apptenis.databinding.FragmentListadoClasesBinding;
import com.tec.apptenis.model.Clase;

import java.util.ArrayList;
import java.util.List;

public class ListadoClasesFragment extends Fragment {

    private ListadoClasesViewModel mViewModel;
    private FragmentListadoClasesBinding binding;
    private ListadoClasesAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentListadoClasesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(ListadoClasesViewModel.class);

        // 1. Configurar el RecyclerView
        setupRecyclerView();

        // 2. Observar los datos
        mViewModel.getClases().observe(getViewLifecycleOwner(), clases -> {
            if (clases != null) {
                adapter.submitList(clases);

                // Mostrar/Ocultar mensaje de lista vacía
                boolean isEmpty = clases.isEmpty();
                binding.rvClases.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
                binding.tvEmptyList.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
            }
        });
    }

    private void setupRecyclerView() {
        adapter = new ListadoClasesAdapter();
        binding.rvClases.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvClases.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}