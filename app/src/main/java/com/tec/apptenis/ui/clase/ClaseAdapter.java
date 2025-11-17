package com.tec.apptenis.ui.clase; // ¡Importante! Asegúrate que está en tu carpeta UI.clase

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tec.apptenis.R; // Asegúrate de tener el layout item_alumno_seleccionable.xml
import com.tec.apptenis.model.AlumnoSeleccionable;

import java.util.List;

public class ClaseAdapter extends RecyclerView.Adapter<ClaseAdapter.AlumnoSelectionViewHolder> {

    private List<AlumnoSeleccionable> listaSeleccionable;
    // Interfaz para comunicar los clics al Fragment/ViewModel
    private final OnSelectionChangeListener listener;

    public ClaseAdapter(List<AlumnoSeleccionable> lista, OnSelectionChangeListener listener) {
        this.listaSeleccionable = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AlumnoSelectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Necesitas un layout XML diferente para la selección múltiple
        View view = LayoutInflater.from(parent.getContext())
                // SUGERENCIA: Crea un layout 'item_alumno_seleccionable.xml' que contenga un TextView y un CheckBox
                .inflate(R.layout.item_alumno_seleccionable, parent, false);
        return new AlumnoSelectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlumnoSelectionViewHolder holder, int position) {
        AlumnoSeleccionable item = listaSeleccionable.get(position);

        // 1. Mostrar Nombre
        holder.tvNombreAlumno.setText(item.getNombreCompleto());

        // 2. Establecer estado inicial del CheckBox
        holder.checkBox.setChecked(item.isSelected());

        // 3. Manejar el click en el CheckBox
        // Usamos setOnClickListener en el CheckBox (o setOnCheckedChangeListener)
        // Usamos el listener de la interfaz para avisar al Fragment/ViewModel
        holder.checkBox.setOnClickListener(v -> {
            boolean isChecked = holder.checkBox.isChecked();
            if (listener != null) {
                // Notificar el cambio de estado (posición, nuevo estado)
                listener.onAlumnoSelectionChanged(position, isChecked);
            }
        });

        // 4. Opcional: Permitir click en el ítem completo para seleccionar/deseleccionar
        holder.itemView.setOnClickListener(v -> {
            // Invertir el estado actual y notificar
            boolean newState = !holder.checkBox.isChecked();
            holder.checkBox.setChecked(newState); // Actualiza visualmente
            if (listener != null) {
                listener.onAlumnoSelectionChanged(position, newState);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaSeleccionable != null ? listaSeleccionable.size() : 0;
    }

    // Método para actualizar la lista desde el LiveData del ViewModel
    public void submitList(List<AlumnoSeleccionable> nuevaLista) {
        this.listaSeleccionable = nuevaLista;
        notifyDataSetChanged();
    }

    // Interfaz de comunicación con el Fragment
    public interface OnSelectionChangeListener {
        void onAlumnoSelectionChanged(int position, boolean isChecked);
    }

    // ViewHolder para la selección
    public static class AlumnoSelectionViewHolder extends RecyclerView.ViewHolder {
        final TextView tvNombreAlumno;
        final CheckBox checkBox;

        public AlumnoSelectionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreAlumno = itemView.findViewById(R.id.text_alumno_nombre);
            checkBox = itemView.findViewById(R.id.checkbox_alumno);
        }
    }
}