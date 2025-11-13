package com.tec.apptenis.ui.listadoUsuarios;

import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tec.apptenis.databinding.FragmentListadousuariosBinding;
import com.tec.apptenis.model.Usuario;

import java.util.ArrayList;

// IMPLEMENTA LA INTERFAZ DE CLIC
public class ListadoUsuariosFragment extends Fragment implements ListadoUsuariosAdapter.OnUsuarioClickListener {

    private ListadoUsuariosViewModel mViewModel;
    private FragmentListadousuariosBinding binding;
    private ListadoUsuariosAdapter adapter;
    private static final String TAG_FRAGMENT = "ListadoUsuariosFragment";
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(TAG_FRAGMENT, "onViewCreated ejecutado. Inicializando ViewModel y carga.");

        // 1. Inicializar Binding y ViewModel
        binding = FragmentListadousuariosBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(ListadoUsuariosViewModel.class);
        View root = binding.getRoot();

        // 2. Configurar RecyclerView y Adapter
        // MODIFICADO: Pasamos 'this' (el listener) al constructor
        adapter = new ListadoUsuariosAdapter(getContext(), new ArrayList<Usuario>(), this);
        binding.recyclerViewUsuarios.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewUsuarios.setAdapter(adapter);

        // 3. Observar los datos del ViewModel (Lista de Usuarios)
        mViewModel.getListaUsuarios().observe(getViewLifecycleOwner(), usuarios -> {
            adapter.setUsuarios(usuarios);
        });

        // 4. Observar el resultado de la operación (Mensajes)
        mViewModel.getMensajeResultado().observe(getViewLifecycleOwner(), mensaje -> {
            if (mensaje != null && !mensaje.isEmpty()) {
                Toast.makeText(getContext(), mensaje, Toast.LENGTH_LONG).show();
                // Opcional: limpiar el mensaje para evitar que se muestre en rotaciones
                mViewModel.clearMensajeResultado();
            }
        });

        // 5. Iniciar la carga de datos
        mViewModel.cargarUsuarios();

        return root;
    }

    // 🔥 IMPLEMENTACIÓN DEL MÉTODO DE CLIC 🔥
    @Override
    public void onUsuarioClick(Usuario usuario) {
        // Llamar al nuevo método del ViewModel para procesar la creación
        mViewModel.intentarCrearAlumno(usuario);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}