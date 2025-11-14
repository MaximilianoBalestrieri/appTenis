package com.tec.apptenis.ui.registroAlumno;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.textfield.TextInputEditText;
import com.tec.apptenis.MainActivity;
import com.tec.apptenis.R;
import com.tec.apptenis.model.AlumnoRegistroDTO;
import com.tec.apptenis.ui.listadoUsuarios.ListadoUsuariosFragment;

public class RegistroAlumnoFragment extends Fragment {

    private int usuarioId = -1;
    private RegistroAlumnoViewModel viewModel;

    // Vistas
    private TextInputEditText etNombre, etTelefono, etDireccion, etLocalidad, etNivel, etManoHabil, etReves;
    private Button btnCompletarRegistro;

    // Bandera para evitar que se intente registrar si ya existe
    private boolean perfilExistente = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            usuarioId = getArguments().getInt("USUARIO_ID", -1);
        }

        if (usuarioId == -1) {
            Toast.makeText(getContext(), "Error: ID de usuario no encontrado para el registro.", Toast.LENGTH_LONG).show();
        }

        viewModel = new ViewModelProvider(this).get(RegistroAlumnoViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registro_alumno, container, false);

        // 2. Inicializar Vistas
        etNombre = view.findViewById(R.id.et_nombre);
        etTelefono = view.findViewById(R.id.et_telefono);
        etDireccion = view.findViewById(R.id.et_direccion);
        etLocalidad = view.findViewById(R.id.et_localidad);
        etNivel = view.findViewById(R.id.et_nivel);
        etManoHabil = view.findViewById(R.id.et_mano_habil);
        etReves = view.findViewById(R.id.et_reves);
        btnCompletarRegistro = view.findViewById(R.id.btn_completar_registro);

        // 3. Configurar Observadores
        setupObservers();

        // 4. Configurar el Listener del Botón
        btnCompletarRegistro.setOnClickListener(v -> completarRegistro());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 5. INICIAR VERIFICACIÓN DE EXISTENCIA
        if (usuarioId > 0) {
            viewModel.verificarPerfilExistente(usuarioId);
        }
    }

    private void setupObservers() {
        viewModel.getRegistroResult().observe(getViewLifecycleOwner(), this::handleRegistroResult);

        // OBSERVADOR DE EXISTENCIA DEL ALUMNO
        viewModel.getAlumnoYaExiste().observe(getViewLifecycleOwner(), yaExiste -> {
            if (yaExiste != null) {
                perfilExistente = yaExiste;
                btnCompletarRegistro.setEnabled(!yaExiste);

                if (yaExiste) {
                    Toast.makeText(getContext(), "Advertencia: Este usuario ya tiene un perfil de alumno asociado. Volviendo a la lista.", Toast.LENGTH_LONG).show();

                    // 🔥 ¡CORRECCIÓN CLAVE! Cerramos el fragmento si ya existe.
                    // Esto remueve el RegistroFragment y revela el ListadoUsuariosFragment
                    if (getParentFragmentManager() != null) {
                        getParentFragmentManager().popBackStack();
                    }
                } else {
                    Toast.makeText(getContext(), "Usuario válido. Complete los datos para el registro.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Error al verificar la existencia del perfil. Intente de nuevo.", Toast.LENGTH_LONG).show();
                btnCompletarRegistro.setEnabled(false);
            }
        });
    }


    private void completarRegistro() {
        if (perfilExistente) {
            Toast.makeText(getContext(), "Imposible registrar. El perfil de alumno ya existe.", Toast.LENGTH_SHORT).show();
            return;
        }

        String nombre = etNombre.getText().toString().trim();
        String telefono = etTelefono.getText().toString().trim();

        if (nombre.isEmpty() || telefono.isEmpty() || usuarioId == -1) {
            Toast.makeText(getContext(), "Complete los campos y asegúrese de tener un ID de usuario.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear el DTO
        AlumnoRegistroDTO dto = new AlumnoRegistroDTO(
                usuarioId,
                nombre,
                telefono,
                etDireccion.getText().toString().trim(),
                etLocalidad.getText().toString().trim(),
                etNivel.getText().toString().trim(),
                etManoHabil.getText().toString().trim(),
                etReves.getText().toString().trim()
        );

        viewModel.registrarAlumno(dto);
    }

    private void handleRegistroResult(int responseCode) {
        switch (responseCode) {
            case 201: // Creado con éxito
                Toast.makeText(getContext(), "Registro de alumno completado con éxito.", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(requireActivity(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                requireActivity().finish();
                break;
            case 409: // Conflict (Usuario ya tiene perfil - Ocurre si falla la verificación inicial)
                Toast.makeText(getContext(), "Advertencia: El usuario ya tiene un perfil de alumno asociado (Error de servidor 409).", Toast.LENGTH_LONG).show();
                perfilExistente = true;
                btnCompletarRegistro.setEnabled(false);
                break;
            case -1: // Error de conexión o Retrofit
                Toast.makeText(getContext(), "Error de red. No se pudo conectar al servidor.", Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(getContext(), "Error desconocido o de validación (" + responseCode + ").", Toast.LENGTH_LONG).show();
                break;
        }
    }
}