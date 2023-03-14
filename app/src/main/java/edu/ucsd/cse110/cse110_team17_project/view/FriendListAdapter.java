package edu.ucsd.cse110.cse110_team17_project.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import edu.ucsd.cse110.cse110_team17_project.R;
import edu.ucsd.cse110.cse110_team17_project.model.UserInfo;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.ViewHolder> {
    private List<UserInfo> userInfoList = Collections.emptyList();


    public void setUserInfoList(List<UserInfo> newUserInfoList) {
        this.userInfoList.clear();
        this.userInfoList = newUserInfoList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.friend_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText("New Friend");
    }

    @Override
    public int getItemCount() {
        return userInfoList.size();
    }

    @Override
    public long getItemId(int position) {
//        return userInfoList.get(position).id;
        // TODO: Implement
        return 1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private UserInfo userInfo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.friend_item_text);
        }

        public UserInfo getUserInfo() {
            return userInfo;
        }

        public void setUserInfo(UserInfo userInfo) {
            this.userInfo = userInfo;
            this.textView.setText(userInfo.label);
        }

    }
}


