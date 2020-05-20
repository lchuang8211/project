package com.example.appiii;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder>{

    View itemView;
    private static final  String TAG = "RecyclerViewAdapter";
    private ArrayList<String> myImageNames = new ArrayList<>();
    private ArrayList<String> myImage = new ArrayList<>();
    private Context mContext;


    public RecycleViewAdapter( Context context, ArrayList<String> myImageNames, ArrayList<String> myImage) {
        this.myImageNames = myImageNames;
        this.myImage = myImage;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }
    int getposition = 0;

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder: called");
        holder.setIsRecyclable(false);
        getposition=holder.getAdapterPosition();
        Log.i(TAG, "onBindViewHolder: holder.getAdapterPosition():"+ holder.getAdapterPosition());
        myImageNames.get(position);
        Glide.with(mContext).asBitmap().load( myImage.get(position) ).into(holder.getItem_image);
        holder.btn_getItem.setText(myImageNames.get(position));
//        holder.getItem_txt.setText(myImageNames.get(position));
        Log.i(TAG, "onBindViewHolder: myImageNames.get(position): " + myImageNames.get(position));
//        getposition = position;

//        holder.getParentLayout.setOnClickListener(new View.OnClickListener(){
////            @Override
////            public void onClick(View v) {
////                Log.i(TAG, "onClick: click on: "+ myImageNames.get(getposition));
////                Log.i(TAG, "onClick: click on getposition :"+ getposition);
////                Toast.makeText(mContext,  myImageNames.get(getposition), Toast.LENGTH_SHORT).show();
////            }
////        });
    }

    @Override
    public int getItemCount() {
        return myImageNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView getItem_image;
        TextView getItem_txt, txt_click;
        Button btn_getItem;
        RelativeLayout getParentLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btn_getItem = itemView.findViewById(R.id.btn_getItem);
            btn_getItem.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(),
                            "click " +getAdapterPosition(),Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "onClick: ViewHolder on getAdapterPosition() :"+ getAdapterPosition());
                }
            });
            getItem_image = itemView.findViewById(R.id.getCirlceImage);
//            getItem_txt = itemView.findViewById(R.id.getItem_txt);
            getParentLayout = itemView.findViewById(R.id.getParent_Layout);
//            InitialComponent();
        }
    }

//    private void InitialComponent() {
//
//    }

}
