package com.example.gaetalk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gaetalk.MemberInfo;
import com.example.gaetalk.R;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private ArrayList<MemberInfo> arrayList;
    private Context context;
    //어댑터에서 액티비티 액션을 가져올 때 context가 필요한데 어댑터에는 context가 없다.
    //선택한 액티비티에 대한 context를 가져올 때 필요하다.

    public CustomAdapter(ArrayList<MemberInfo> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    //실제 리스트뷰가 어댑터에 연결된 다음에 뷰 홀더를 최초로 만들어낸다.
    public CustomAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(context).inflate(R.layout.recy_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.CustomViewHolder holder, int position) {
        MemberInfo memberInfo = arrayList.get(position);
        holder.tvHumanName.setText(memberInfo.getHumanName());
        holder.tvName.setText(memberInfo.getName());
        holder.tvGender.setText(memberInfo.getGender());
        holder.tvBirth.setText(memberInfo.getBirth());
        holder.tvBreed.setText(memberInfo.getBreed());
    }

    @Override
    public int getItemCount() {
        // 삼항 연산자
        return arrayList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        //ImageView iv_profile;
        TextView tvHumanName;
        TextView tvName;
        TextView tvGender;
        TextView tvBirth;
        TextView tvBreed;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHumanName = itemView.findViewById(R.id.tvHumanName);
            tvName = itemView.findViewById(R.id.tvName);
            tvGender = itemView.findViewById(R.id.tvGender);
            tvBirth = itemView.findViewById(R.id.tvBirth);
            tvBreed = itemView.findViewById(R.id.tvBreed);
        }
    }
}