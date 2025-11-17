package com.tec.apptenis.ui.listadoClases; // Importante: Corregí el paquete a 'listadoclases' minúsculas

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.tec.apptenis.R;
import com.tec.apptenis.model.Clase;
import com.tec.apptenis.model.ClaseAlumno;

import java.text.SimpleDateFormat; // 🟢 Necesario para el formato de fecha
import java.util.Date; // 🟢 Necesario para el formato de fecha
import java.util.List;
import java.util.Locale; // 🟢 Necesario para el formato de fecha en español
import java.util.stream.Collectors;

public class ListadoClasesAdapter extends ListAdapter<Clase, ListadoClasesAdapter.ClaseViewHolder> {

    public ListadoClasesAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public ClaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_clase, parent, false);
        return new ClaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClaseViewHolder holder, int position) {
        Clase currentClase = getItem(position);
        holder.bind(currentClase);
    }

    // --- ViewHolder ---
    static class ClaseViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvFecha;
        private final TextView tvHora;
        private final TextView tvEstado;
        private final TextView tvAlumnos;

        public ClaseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFecha = itemView.findViewById(R.id.tv_fecha);
            tvHora = itemView.findViewById(R.id.tv_hora);
            tvEstado = itemView.findViewById(R.id.tv_estado);
            tvAlumnos = itemView.findViewById(R.id.tv_alumnos);
        }

        public void bind(Clase clase) {

            // 1. FORMATEAR LA FECHA (Date a String legible)
            Date fechaOriginal = clase.getFecha();
            if (fechaOriginal != null) {
                // Formato: dd/MM/yyyy, usando Locale español para consistencia
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("es", "ES"));
                tvFecha.setText(dateFormat.format(fechaOriginal));
            } else {
                tvFecha.setText("N/A");
            }

            // 2. FORMATEAR LA HORA (String de TimeSpan a hh:mm)
            String horaOriginal = clase.getHora(); // Viene como "HH:mm:ss"
            if (horaOriginal != null && horaOriginal.length() >= 5) {
                // Tomamos solo los primeros 5 caracteres (HH:mm)
                String horaFormateada = horaOriginal.substring(0, 5);
                tvHora.setText(horaFormateada);
            } else {
                tvHora.setText("N/A");
            }

            // 3. LÓGICA DE ALUMNOS (Usando solo getNombre)
            List<ClaseAlumno> claseAlumnos = clase.getClaseAlumnos();

            if (claseAlumnos != null && !claseAlumnos.isEmpty()) {
                // Mapear la lista de ClaseAlumno a una lista de nombres (solo getNombre)
                String nombresAlumnos = claseAlumnos.stream()
                        .filter(ca -> ca.getAlumno() != null)
                        .map(ca -> ca.getAlumno().getNombre()) // 🟢 Corregido: SOLO getNombre()
                        .collect(Collectors.joining(", "));

                if (!nombresAlumnos.isEmpty()) {
                    tvAlumnos.setText("Alumno(s): " + nombresAlumnos);
                } else {
                    tvAlumnos.setText("Alumno(s): No disponible");
                }
            } else {
                tvAlumnos.setText("Alumno(s): Ninguno asignado");
            }

            // 4. ESTADO
            tvEstado.setText(clase.getEstado());
        }
    }

    // --- DiffUtil Callback (Sin cambios) ---
    private static final DiffUtil.ItemCallback<Clase> DIFF_CALLBACK = new DiffUtil.ItemCallback<Clase>() {
        @Override
        public boolean areItemsTheSame(@NonNull Clase oldItem, @NonNull Clase newItem) {
            return oldItem.getIdClase() == newItem.getIdClase();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Clase oldItem, @NonNull Clase newItem) {
            // Aseguramos que la comparación de contenido sea segura y completa
            return oldItem.getFecha().equals(newItem.getFecha()) &&
                    oldItem.getHora().equals(newItem.getHora()) &&
                    oldItem.getEstado().equals(newItem.getEstado()) &&
                    // Nota: Asumimos que la comparación de la lista de alumnos es correcta en el modelo
                    oldItem.getClaseAlumnos().equals(newItem.getClaseAlumnos());
        }
    };
}