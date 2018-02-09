package tekhne.com.cinemax;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import tekhne.com.cinemax.db.Cine;

/**
 * Created by bzgroup on 1/23/18.
 */

public class CineAdaptador extends BaseAdapter{


    private List<Cine> cineList;
    private Context context;
    public CineAdaptador(Context context , List<Cine> listcines) {
        this.cineList = listcines;
        this.context = context;
    }



    @Override
    public int getCount() {
        return cineList.size();
    }

    @Override
    public Object getItem(int position) {
        return cineList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if(convertView == null){
           // row = View.inflate(context,R.layout.row_cine,parent);
            row = LayoutInflater.from(context).inflate(R.layout.row_cine,parent,false);
        }


        TextView txt_name = (TextView)row.findViewById(R.id.txt_name);
        TextView txt_about = (TextView)row.findViewById(R.id.txt_about);
        TextView txt_horario = (TextView)row.findViewById(R.id.txt_horario);
       ImageView image =  (ImageView)row.findViewById(R.id.img_cine);
        ImageView ubicacion =  (ImageView)row.findViewById(R.id.img_ubicacion);

        Cine cine = cineList.get(position);

        txt_name.setText(cine.getNombre());
        txt_about.setText(cine.getDireccion());
        txt_horario.setText("6:00-12:00 LU-DO");
        image.setImageDrawable(context.getResources().getDrawable(R.mipmap.cinecenter));
        ubicacion.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_place_red_400_24dp));

        ubicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent =  new Intent(context, MapsActivity.class);
                 intent.putExtra("latitud", "-17.7986884");
                 intent.putExtra("longitud", "-63.1789864");
                 context.startActivity(intent);
            }
        });
        return row;
    }
}
