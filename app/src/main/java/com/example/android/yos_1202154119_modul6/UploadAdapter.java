package com.example.android.yos_1202154119_modul6;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Yosafat Dhimas on 01/04/2018.
 */

public class UploadAdapter extends RecyclerView.Adapter<UploadAdapter.ImageViewHolder> {

    List<UploadModel> mUpload;
    Context mContext;
    UploadModel mCurrentUpload;

    UploadAdapter(Context context, List<UploadModel> upload){
        mUpload = upload;
        mContext = context;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_list_profile, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        mCurrentUpload = mUpload.get(position);

        holder.mTitle.setText(mCurrentUpload.getmTitle());
        holder.mCaption.setText(mCurrentUpload.getmCaption());
        holder.mEmail.setText(mCurrentUpload.getmEmail());

        Picasso.get()
                .load(mCurrentUpload.getmUrl())
                .fit()
                .centerCrop()
                .into(holder.mImage);
    }

    @Override
    public int getItemCount() {
        return mUpload.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{

        TextView mEmail;
        TextView mTitle;
        TextView mCaption;
        ImageView mImage;

        public ImageViewHolder(View itemView) {
            super(itemView);

            mEmail = itemView.findViewById(R.id.email);
            mTitle = itemView.findViewById(R.id.titlePost);
            mCaption = itemView.findViewById(R.id.captionPost);
            mImage = itemView.findViewById(R.id.imageViewUploaded);
        }
    }
}