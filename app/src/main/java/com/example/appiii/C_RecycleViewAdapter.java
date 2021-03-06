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

/**  RecycleView Adapter : 主要是將兩個不同介面的裝置，透過Adapter做連接或傳送 **/
public class C_RecycleViewAdapter extends RecyclerView.Adapter<C_RecycleViewAdapter.ViewHolder>{

    View itemView;
    private static final  String TAG = "RecyclerViewAdapter";
    private ArrayList<String> myImageNames = new ArrayList<>();
    private ArrayList<String> myImage = new ArrayList<>();
    private Context mContext;


    public C_RecycleViewAdapter(Context context, ArrayList<String> myImageNames, ArrayList<String> myImage) {
        this.myImageNames = myImageNames;
        this.myImage = myImage;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {  // part 1 : 建立 Holder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_list_item, parent, false);  //嵌入 RecycleView 的 list item XML
        ViewHolder holder = new ViewHolder(view);  // 讓 holder 去控制 RecycleView
        return holder;
    }
    int getposition = 0;

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {  // part 2 : 複製 RecyclerView 的 XML && 定義 XML 的設定
        Log.i(TAG, "onBindViewHolder: called");
        holder.setIsRecyclable(false);
        getposition=holder.getAdapterPosition();
        Log.i(TAG, "onBindViewHolder: holder.getAdapterPosition():"+ holder.getAdapterPosition());
        myImageNames.get(position);
        Glide.with(mContext).asBitmap().load( myImage.get(position) ).into(holder.getItem_image);  // Gilde : 圖片 library
        holder.btn_getItem.setText(myImageNames.get(position));
//        holder.getItem_txt.setText(myImageNames.get(position));
        Log.i(TAG, "onBindViewHolder: myImageNames.get(position): " + myImageNames.get(position));
    }

    @Override
    public int getItemCount() { // part 3 :
        return myImageNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{ // ViewHolder 類別 Class 變數要在內部定義 才能包在 ViewHolder 中使用
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
//            getParentLayout = itemView.findViewById(R.id.getParent_Layout);  //

        }
    }

}
