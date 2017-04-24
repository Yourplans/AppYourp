package com.apps.appyourp.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.apps.appyourp.R;
import com.apps.appyourp.objects.SitiosVO;
import com.bumptech.glide.Glide;


import java.util.ArrayList;

/**
 * Created by Andres Rangel on 23/12/2015.
 */
public class AdapterHome extends RecyclerView.Adapter<AdapterHome.AdapterViewHolder> {

    ArrayList<SitiosVO> items;
    Context context;


    public AdapterHome(ArrayList<SitiosVO> items, Context context) {
        this.items = items;
        this.context = context;
    }

    /**
    metodo que se implementa al extender del recycler view y es donde inflamos nuestro layout adapter con los datos de
    la clase que se esta instanciando y que posteriomente hemos creado y casteado los datos
     */
    @Override
    public AdapterHome.AdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate( R.layout.adapter_home,parent,false);
        AdapterViewHolder adapterViewHolder=new AdapterViewHolder(view);
        return adapterViewHolder;
    }

    /**
    metodo que se implementa al extender del recycler view y es donde asiganmos a nuestro arreglo los datos
    correspondientes de cada componente
     */

    @Override
    public void onBindViewHolder(final AdapterHome.AdapterViewHolder holder, int position) {

        holder.glide.with(context).load(items.get(position).getLink1()).into(holder.imagenSitio);
        holder.nombre.setText(items.get(position).getName());
        holder.direccion.setText(items.get(position).getAddress());
        holder.ratingBar.setRating( Float.parseFloat( items.get(position).getScore() ) );
    }


    /**
    metodo que obtiene y retorna el arreglo con su tamaño
     */
    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     *clase que se crea para hacer el casteo de los componentes que utilizaremos en el adaptador
     */
    public class AdapterViewHolder extends RecyclerView.ViewHolder {
        Glide glide;
        ImageView imagenSitio;
        TextView nombre,direccion;
        RatingBar ratingBar;
        public AdapterViewHolder(View view) {
            super(view);
            ratingBar= (RatingBar)view.findViewById(R.id.ratingBar);
            imagenSitio= (ImageView) view.findViewById(R.id.imagenSitio);
            nombre= (TextView) view.findViewById(R.id.txtNombre);
            direccion= (TextView) view.findViewById(R.id.txtDireccion);
            // asignamos la tipografia roboto de material design a nuestros componentes
            Typeface typeface= Typeface.createFromAsset(context.getAssets(),"font/Roboto-Regular.ttf");
            nombre.setTypeface(typeface);
            direccion.setTypeface(typeface);

        }


    }
}
