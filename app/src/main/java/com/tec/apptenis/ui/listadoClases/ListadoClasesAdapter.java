package com.tec.apptenis.ui.listadoClases; // Usamos minúsculas por convención

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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ListadoClasesAdapter extends ListAdapter<Clase, ListadoClasesAdapter.ClaseViewHolder> {


    public static interface OnClaseClickListener {
        void onClaseClick(Clase clase);
    }

    private final OnClaseClickListener listener;

    // 2. CONSTRUCTOR AJUSTADO PARA RECIBIR EL LISTENER
    public ListadoClasesAdapter(OnClaseClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener; // Guardamos la referencia al fragmento
    }

    // Dejamos un constructor sin listener por si es necesario, aunque el Fragment usará el otro
    public ListadoClasesAdapter() {
        this(null);
    }

    // 3. AJUSTAR onCreateViewHolder para pasar el listener al ViewHolder
    @NonNull
    @Override
    public ClaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_clase, parent, false);
        return new ClaseViewHolder(view, listener); // <-- Pasamos el listener
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

        // 4. CONSTRUCTOR DEL VIEWHOLDER AJUSTADO PARA EL CLIC
        public ClaseViewHolder(@NonNull View itemView, final OnClaseClickListener listener) {
            super(itemView);
            tvFecha = itemView.findViewById(R.id.tv_fecha);
            tvHora = itemView.findViewById(R.id.tv_hora);
            tvEstado = itemView.findViewById(R.id.tv_estado);
            tvAlumnos = itemView.findViewById(R.id.tv_alumnos);

            // 5. IMPLEMENTACIÓN DEL CLIC EN LA FILA
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION && getBindingAdapter() != null) {
                            // Obtener el objeto Clase de la posición
                            Clase clase = ((ListadoClasesAdapter) getBindingAdapter()).getItem(position);
                            listener.onClaseClick(clase); // <-- Llamar al método del Fragment
                        }
                    }
                }
            });
        }

        public void bind(Clase clase) {

            // 1. FORMATEAR LA FECHA
            Date fechaOriginal = clase.getFecha();
            if (fechaOriginal != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("es", "ES"));
                tvFecha.setText(dateFormat.format(fechaOriginal));
            } else {
                tvFecha.setText("N/A");
            }

            // 2. FORMATEAR LA HORA
            String horaOriginal = clase.getHora();
            if (horaOriginal != null && horaOriginal.length() >= 5) {
                String horaFormateada = horaOriginal.substring(0, 5);
                tvHora.setText(horaFormateada);
            } else {
                tvHora.setText("N/A");
            }

            // 3. LÓGICA DE ALUMNOS (Usando solo getNombre)
            List<ClaseAlumno> claseAlumnos = clase.getClaseAlumnos();

            if (claseAlumnos != null && !claseAlumnos.isEmpty()) {
                String nombresAlumnos = claseAlumnos.stream()
                        .filter(ca -> ca.getAlumno() != null)
                        .map(ca -> ca.getAlumno().getNombre())
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

    // --- DiffUtil Callback ---
    private static final DiffUtil.ItemCallback<Clase> DIFF_CALLBACK = new DiffUtil.ItemCallback<Clase>() {
        @Override
        public boolean areItemsTheSame(@NonNull Clase oldItem, @NonNull Clase newItem) {
            return oldItem.getIdClase() == newItem.getIdClase();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Clase oldItem, @NonNull Clase newItem) {
            return oldItem.getFecha().equals(newItem.getFecha()) &&
                    oldItem.getHora().equals(newItem.getHora()) &&
                    oldItem.getEstado().equals(newItem.getEstado()) &&
                    oldItem.getClaseAlumnos().equals(newItem.getClaseAlumnos());
        }
    };
}