package com.ivim.ivimadmin;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterListaInspectores    extends RecyclerView.Adapter<AdapterListaInspectores.ViewHolderRecycler>{
    private ArrayList<ListaInspectores> recyclerListaInspectores;
    AdapterListaInspectores.ViewHolderRecycler viewholderListaInspectores,anterior;
    private  RecyclerView recyclerView;
    private Context context;
    private String id,nombres,apellido_1,apellido_2,telefono,correo,fecha_ingreso,latitud,longitud,ultima_fecha_conexion;



    public AdapterListaInspectores(Inspectores activity, int item, ArrayList<ListaInspectores> recyclerListaInspectores, Resources resources)
    {
        this.recyclerListaInspectores =recyclerListaInspectores;
    }
    @Override
    public AdapterListaInspectores.ViewHolderRecycler onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_inspectores,parent,false);
        context=parent.getContext();

        return new AdapterListaInspectores.ViewHolderRecycler(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderRecycler holder, int position) {
        viewholderListaInspectores =holder;
        id=recyclerListaInspectores.get(position).getId();
        nombres=recyclerListaInspectores.get(position).getNombres();
        apellido_1=recyclerListaInspectores.get(position).getApellido_1();
        apellido_2=recyclerListaInspectores.get(position).getApellido_2();
        telefono=recyclerListaInspectores.get(position).getTelefono();
        correo=recyclerListaInspectores.get(position).getCorreo();
        fecha_ingreso=recyclerListaInspectores.get(position).getFecha_ingreso();
        latitud=recyclerListaInspectores.get(position).getLatitud();
        longitud=recyclerListaInspectores.get(position).getLongitud();
        ultima_fecha_conexion=recyclerListaInspectores.get(position).getLongitud();
        holder.fecha_ingreso_tv.setText(fecha_ingreso);
        holder.nombre_completo_inspector_tv.setText(nombres+" "+apellido_1+" "+apellido_2);
        holder.numero_telefono_inspector.setText(telefono);


        holder.div_inspector.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int posiTion2 = holder.getAdapterPosition();
                id= recyclerListaInspectores.get(posiTion2).getId();
                nombres= recyclerListaInspectores.get(posiTion2).getNombres();
                apellido_1= recyclerListaInspectores.get(posiTion2).getApellido_1();
                apellido_2= recyclerListaInspectores.get(posiTion2).getApellido_2();
                telefono= recyclerListaInspectores.get(posiTion2).getTelefono();
                correo= recyclerListaInspectores.get(posiTion2).getCorreo();
                fecha_ingreso= recyclerListaInspectores.get(posiTion2).getFecha_ingreso();
                latitud= recyclerListaInspectores.get(posiTion2).getLatitud();
                longitud= recyclerListaInspectores.get(posiTion2).getLongitud();
                ultima_fecha_conexion= recyclerListaInspectores.get(posiTion2).getUltima_fecha_conexion();


                id=recyclerListaInspectores.get(posiTion2).getId();
                ((Inspectores)context).verActividadInspector( id,nombres,apellido_1,apellido_2,telefono,correo,posiTion2,fecha_ingreso,latitud,longitud,ultima_fecha_conexion);
            }
        });


    }
    @Override
    public int getItemCount(){
        return recyclerListaInspectores.size();
    }
    public class ViewHolderRecycler extends RecyclerView.ViewHolder {
        TextView fecha_ingreso_tv,nombre_completo_inspector_tv,numero_telefono_inspector;
        LinearLayout div_inspector;
        public ViewHolderRecycler(View itemView) {
            super(itemView);
            fecha_ingreso_tv=(TextView)itemView.findViewById(R.id.fecha_ingreso_tv);
            nombre_completo_inspector_tv =(TextView)itemView.findViewById(R.id.nombre_completo_inspector_tv);
            numero_telefono_inspector =(TextView)itemView.findViewById(R.id.numero_telefono_inspector);
            div_inspector=itemView.findViewById(R.id.div_inspector);
        }

    }


}