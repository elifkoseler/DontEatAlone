package com.elf.dea;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyMeetingsAdapter extends RecyclerView.Adapter<MyMeetingsAdapter.ViewHolder> {

    private ArrayList<String> meetingNameList;
    private ArrayList<String> meetingRestaurantNameList;
    private ArrayList<String> meetingDateTimeList;
    private ArrayList<String> meetingDistrictList;
    private ArrayList<String> meetingImageList;

    private static MyMeetingsRecyclerViewListClickListener itemListener;


    public MyMeetingsAdapter(ArrayList<String> meetingNameList,
                                ArrayList<String> meetingRestaurantNameList, ArrayList<String> meetingDateTimeList,
                                ArrayList<String> meetingDistrictList, ArrayList<String> meetingImageList, MyMeetingsRecyclerViewListClickListener itemListener) {
        this.meetingNameList = meetingNameList;
        this.meetingRestaurantNameList = meetingRestaurantNameList;
        this.meetingDateTimeList = meetingDateTimeList;
        this.meetingDistrictList = meetingDistrictList;
        this.meetingImageList = meetingImageList;
        this.itemListener = itemListener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.activity_my_meetings_adapter,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.meetingNameText.setText(meetingNameList.get(position));
        holder.meetingRestaurantNameText.setText(meetingRestaurantNameList.get(position));
        holder.meetingDistrictText.setText(meetingDistrictList.get(position));
        holder.meetingDateTimeText.setText(meetingDateTimeList.get(position));
        Picasso.get().load(meetingImageList.get(position)).into(holder.imageView);

    }

    @Override
    public int getItemCount() { //recycleviewda ka?? tane row olacak

        return meetingNameList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imageView;
        TextView meetingNameText;
        TextView meetingRestaurantNameText;
        TextView meetingDateTimeText;
        TextView meetingDistrictText;
        Button button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            System.out.println("FeedRecycleAdapter > Post Holder!!");
            imageView = itemView.findViewById(R.id.editImageView);
            meetingNameText = itemView.findViewById(R.id.editmeetingNameText);
            meetingDateTimeText = itemView.findViewById(R.id.editdatetimeText);
            meetingDistrictText = itemView.findViewById(R.id.editDistrictText);
            meetingRestaurantNameText = itemView.findViewById(R.id.editRestaurantText);
            button = itemView.findViewById(R.id.editButton);
            button.setOnClickListener(this);
            itemView.setOnClickListener(this);

        }
        @Override
        public void onClick(View v) {
            itemListener.MyMeetingsRecyclerViewListClicked(v, this.getLayoutPosition());

        }
    }
    public interface MyMeetingsRecyclerViewListClickListener {
        void  MyMeetingsRecyclerViewListClicked(View v, int position);
    }

}