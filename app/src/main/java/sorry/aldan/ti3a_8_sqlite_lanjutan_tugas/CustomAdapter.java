package sorry.aldan.ti3a_8_sqlite_lanjutan_tugas;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomHolder> {

    ArrayList<UserModel> dataset;

    public CustomAdapter(ArrayList<UserModel> dataset) {
        this.dataset = dataset;
    }

    @NonNull
    @Override
    public CustomHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.lv_item,viewGroup,false);
        CustomHolder viewHolder = new CustomHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomHolder customHolder, int i) {
        UserModel um = dataset.get(i);
        customHolder.name.setText(um.getNama());
        customHolder.hobby.setText(um.getHobby());
        customHolder.city.setText(um.getCity());
        customHolder.department.setText(um.getDepartment());
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public class CustomHolder extends RecyclerView.ViewHolder{
        TextView name,hobby,city,department;
        public CustomHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            hobby = itemView.findViewById(R.id.hobby);
            city = itemView.findViewById(R.id.city);
            department = itemView.findViewById(R.id.department);
        }
    }
}
