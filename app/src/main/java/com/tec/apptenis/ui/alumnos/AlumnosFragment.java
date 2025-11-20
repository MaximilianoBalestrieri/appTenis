package com.tec.apptenis.ui.alumnos;

import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tec.apptenis.R;
import com.tec.apptenis.databinding.FragmentAlumnosBinding;
import com.tec.apptenis.model.Alumno;

import java.util.ArrayList;
import java.util.List;

public class AlumnosFragment extends Fragment implements AlumnosAdapter.OnAlumnoClickListener {

    private AlumnosViewModel mViewModel;
    private FragmentAlumnosBinding binding;
    private AlumnosAdapter adapter;
    private static final String TAG = "AlumnosFragment";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentAlumnosBinding.inflate(inflater, container, false);
        adapter = new AlumnosAdapter(new ArrayList<>(), this);
        binding.rvAlumnos.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvAlumnos.setAdapter(adapter);

        binding.fabAgregarAlumno.setOnClickListener(v -> {
            // Navegación al alta de alumno
            Toast.makeText(getContext(), "Navegando a Alta de Alumno...", Toast.LENGTH_SHORT).show();
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(AlumnosViewModel.class);

        mViewModel.getListaAlumnos().observe(getViewLifecycleOwner(), alumnos -> {
            List<Alumno> lista = alumnos != null ? alumnos : new ArrayList<>();
            adapter.setAlumnos(lista);

            if (lista.isEmpty()) {
                binding.rvAlumnos.setVisibility(View.GONE);
                binding.tvEmptyList.setVisibility(View.VISIBLE);
            } else {
                binding.rvAlumnos.setVisibility(View.VISIBLE);
                binding.tvEmptyList.setVisibility(View.GONE);
            }
        });

        mViewModel.cargarAlumnos();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onAlumnoClick(Alumno alumno) {
        Log.d(TAG, "Alumno seleccionado: ID=" + alumno.getIdAlumno());

        Bundle bundle = new Bundle();
        bundle.putInt("alumno_id", alumno.getIdAlumno());

        try {
            Navigation.findNavController(requireView()).navigate(
                    R.id.action_alumnosFragment_to_detalleAlumnoFragment,
                    bundle
            );
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Error de navegación: " + e.getMessage());
            Toast.makeText(getContext(), "Error de navegación: verifica el nav_graph.", Toast.LENGTH_LONG).show();
        }
    }
}
