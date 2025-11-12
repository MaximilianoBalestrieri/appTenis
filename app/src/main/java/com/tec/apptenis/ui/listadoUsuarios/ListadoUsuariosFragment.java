package com.tec.apptenis.ui.listadoUsuarios;

import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tec.apptenis.databinding.FragmentListadousuariosBinding; // Asegúrate que este sea el nombre de tu archivo de binding
import com.tec.apptenis.model.Usuario;

import java.util.ArrayList;

public class ListadoUsuariosFragment extends Fragment {

    private ListadoUsuariosViewModel mViewModel;
    private FragmentListadousuariosBinding binding;
    private ListadoUsuariosAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // 1. Inicializar Binding y ViewModel
        binding = FragmentListadousuariosBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(ListadoUsuariosViewModel.class);
        View root = binding.getRoot();

        // 2. Configurar RecyclerView y Adapter
        adapter = new ListadoUsuariosAdapter(getContext(), new ArrayList<Usuario>());
        binding.recyclerViewUsuarios.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewUsuarios.setAdapter(adapter);

        // 3. Observar los datos del ViewModel
        mViewModel.getListaUsuarios().observe(getViewLifecycleOwner(), usuarios -> {
            // Cuando llegan los datos, se los pasamos al adapter
            adapter.setUsuarios(usuarios);
        });

        // 4. Iniciar la carga de datos
        mViewModel.cargarUsuarios();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}