package com.tec.apptenis.ui.listadoUsuarios;

import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.navigation.Navigation; // 🔥 Importación clave para NavController

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tec.apptenis.R;
import com.tec.apptenis.databinding.FragmentListadousuariosBinding;
import com.tec.apptenis.model.Usuario;

import java.util.ArrayList;
import java.util.List;

// IMPLEMENTA LA INTERFAZ DE CLIC
public class ListadoUsuariosFragment extends Fragment implements ListadoUsuariosAdapter.OnUsuarioClickListener {

    private ListadoUsuariosViewModel mViewModel;
    private FragmentListadousuariosBinding binding;
    private ListadoUsuariosAdapter adapter;
    public static final String TAG_FRAGMENT = "ListadoUsuariosFragment";

    // ID de la ACCIÓN que definimos en mobile_navigation.xml
    private static final int ACTION_TO_REGISTRO = R.id.action_listadousuarios_to_registroalumno;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentListadousuariosBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(ListadoUsuariosViewModel.class);
        View root = binding.getRoot();

        // 1. Configurar RecyclerView y Adapter
        // El constructor del Adapter debe recibir el context (si lo usa), la lista y el listener
        adapter = new ListadoUsuariosAdapter(getContext(), new ArrayList<Usuario>(), this);
        binding.recyclerViewUsuarios.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewUsuarios.setAdapter(adapter);

        // 4. Iniciar la carga de datos
        mViewModel.cargarUsuarios();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 2. Observar la lista de usuarios y mensajes
        mViewModel.getListaUsuarios().observe(getViewLifecycleOwner(), usuarios -> {
            List<Usuario> lista = usuarios != null ? usuarios : new ArrayList<>();
            adapter.setUsuarios(lista);
            // Lógica para mostrar/ocultar la vista vacía (si existe)
            if (lista.isEmpty()) {
                binding.recyclerViewUsuarios.setVisibility(View.GONE);
                // Asumiendo que tienes un TextView con id tvEmptyList para mostrar lista vacía
                // binding.tvEmptyList.setVisibility(View.VISIBLE);
            } else {
                binding.recyclerViewUsuarios.setVisibility(View.VISIBLE);
                // binding.tvEmptyList.setVisibility(View.GONE);
            }
        });

        mViewModel.getMensajeResultado().observe(getViewLifecycleOwner(), mensaje -> {
            if (mensaje != null && !mensaje.isEmpty()) {
                Toast.makeText(getContext(), mensaje, Toast.LENGTH_LONG).show();
                mViewModel.clearMensajeResultado();
            }
        });

        // 3. OBSERVACIÓN DE EXISTENCIA Y NAVEGACIÓN (Lógica central)
        mViewModel.getExistenciaAlumno().observe(getViewLifecycleOwner(), usuarioConExistencia -> {
            if (usuarioConExistencia != null) {

                int userId = usuarioConExistencia.getUserId();
                boolean existe = usuarioConExistencia.isExiste();
                String nombreUsuario = usuarioConExistencia.getNombreUsuario(); // Asumiendo que el ViewModel lo guarda

                if (existe) {
                    // Si ya existe, NO NAVEGAMOS y solo notificamos
                    Toast.makeText(getContext(), nombreUsuario + " ya está registrado como alumno.", Toast.LENGTH_LONG).show();
                } else {
                    // Si NO existe, navegamos al registro
                    navigateToRegistroAlumno(userId);
                }
                // Limpiar el LiveData después de usarlo para evitar múltiples ejecuciones
                mViewModel.clearExistenciaAlumno();
            }
        });
    }


    // 5. IMPLEMENTACIÓN DEL CLIC
    @Override
    public void onUsuarioClick(Usuario usuario) {
        // En lugar de navegar directamente, le pedimos al ViewModel que verifique la existencia.
        // El resultado de la verificación activará el observador en onViewCreated.
        mViewModel.verificarYNavegar(usuario);
    }

    /**
     * Maneja la transición al Fragmento de Registro de Alumno usando NavController.
     */
    private void navigateToRegistroAlumno(int userId) {
        Bundle bundle = new Bundle();
        // La clave debe coincidir con el argumento en mobile_navigation.xml
        bundle.putInt("USUARIO_ID", userId);

        try {
            // 🔥 Usamos NavController para gestionar correctamente la pila
            Navigation.findNavController(requireView()).navigate(ACTION_TO_REGISTRO, bundle);
        } catch (Exception e) {
            Log.e(TAG_FRAGMENT, "Error de navegación usando NavController: " + e.getMessage());
            Toast.makeText(getContext(), "Error de navegación. ID de acción incorrecto.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}