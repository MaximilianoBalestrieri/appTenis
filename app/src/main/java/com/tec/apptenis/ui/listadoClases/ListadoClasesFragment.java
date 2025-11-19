package com.tec.apptenis.ui.listadoClases;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.tec.apptenis.R;
import com.tec.apptenis.databinding.FragmentListadoClasesBinding;
import com.tec.apptenis.model.Clase;

// 1. IMPLEMENTAR LA INTERFAZ DE CLIC
public class ListadoClasesFragment extends Fragment
        implements ListadoClasesAdapter.OnClaseClickListener {

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

        setupRecyclerView();

        mViewModel.getClases().observe(getViewLifecycleOwner(), clases -> {
            if (clases != null) {
                adapter.submitList(clases);

                // Mostrar u Ocultar mensaje de lista vacía
                boolean isEmpty = clases.isEmpty();
                binding.rvClases.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
                binding.tvEmptyList.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
            }
        });

    }

    private void setupRecyclerView() {

        adapter = new ListadoClasesAdapter(this);
        binding.rvClases.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvClases.setAdapter(adapter);
    }

        @Override
    public void onClaseClick(Clase clase) {
        // Creamos el Bundle y pasamos el objeto Clase
        Bundle bundle = new Bundle();
        bundle.putSerializable("clase_seleccionada", clase);

        // Navegar al DetallesClaseFragment usando el NavController y la acción definida
        NavController navController = Navigation.findNavController(requireView());

        // La navegación se dirige al destino de detalles y le pasa la data.
        navController.navigate(R.id.action_listadoClasesFragment_to_detallesClaseFragment, bundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Cada vez que volvemos al fragment, recargamos las clases para reflejar cambios
        mViewModel.cargarClases();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}