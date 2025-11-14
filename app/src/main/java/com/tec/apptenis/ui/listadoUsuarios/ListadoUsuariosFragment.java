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

import com.tec.apptenis.R;
import com.tec.apptenis.databinding.FragmentListadousuariosBinding;
import com.tec.apptenis.model.Usuario;
import com.tec.apptenis.ui.registroAlumno.RegistroAlumnoFragment;

import java.util.ArrayList;

// IMPLEMENTA LA INTERFAZ DE CLIC
public class ListadoUsuariosFragment extends Fragment implements ListadoUsuariosAdapter.OnUsuarioClickListener {

    private ListadoUsuariosViewModel mViewModel;
    private FragmentListadousuariosBinding binding;
    private ListadoUsuariosAdapter adapter;
    public static final String TAG_FRAGMENT = "ListadoUsuariosFragment";

    private static final int CONTAINER_ID = R.id.nav_host_fragment_content_main_activity_menu;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentListadousuariosBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(ListadoUsuariosViewModel.class);
        View root = binding.getRoot();

        // 1. Configurar RecyclerView y Adapter
        adapter = new ListadoUsuariosAdapter(getContext(), new ArrayList<Usuario>(), this);
        binding.recyclerViewUsuarios.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewUsuarios.setAdapter(adapter);

        // 2. Observar la lista de usuarios y mensajes
        mViewModel.getListaUsuarios().observe(getViewLifecycleOwner(), usuarios -> {
            adapter.setUsuarios(usuarios);
        });

        mViewModel.getMensajeResultado().observe(getViewLifecycleOwner(), mensaje -> {
            if (mensaje != null && !mensaje.isEmpty()) {
                Toast.makeText(getContext(), mensaje, Toast.LENGTH_LONG).show();
                mViewModel.clearMensajeResultado();
            }
        });

        // 3. OBSERVACIÓN DE NAVEGACIÓN
        mViewModel.getUsuarioSeleccionadoId().observe(getViewLifecycleOwner(), userId -> {
            if (userId != null && userId > 0) {
                navigateToRegistroAlumno(userId);
                mViewModel.seleccionarUsuarioParaRegistro(null);
            }
        });

        // 4. Iniciar la carga de datos
        mViewModel.cargarUsuarios();

        return root;
    }

    // 5. IMPLEMENTACIÓN DEL CLIC
    @Override
    public void onUsuarioClick(Usuario usuario) {
        mViewModel.seleccionarUsuarioParaRegistro(usuario);
    }

    /**
     * Maneja la transición al Fragmento de Registro de Alumno (con el ID).
     */
    private void navigateToRegistroAlumno(int userId) {
        Bundle bundle = new Bundle();
        bundle.putInt("USUARIO_ID", userId);

        RegistroAlumnoFragment registroFragment = new RegistroAlumnoFragment();
        registroFragment.setArguments(bundle);

        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    // Usamos el ID de contenedor que identificamos
                    .replace(CONTAINER_ID, registroFragment)
                    // 🔥 ¡CORRECCIÓN CLAVE! Añadimos el nuevo fragmento a la pila de atrás.
                    // Esto permite que popBackStack() elimine SOLO el RegistroFragment.
                    //.addToBackStack(null)
                    .commit();
        } else {
            Log.e(TAG_FRAGMENT, "Activity es null, no se puede navegar.");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}