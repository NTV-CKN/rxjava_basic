package com.example.rxjava_java.ui.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rxjava_java.R;
import com.example.rxjava_java.data.source.local.model.User;
import com.example.rxjava_java.databinding.ItemUserBinding;
import com.example.rxjava_java.domain.IOnItemClick;
import com.example.rxjava_java.domain.IOnMenuMoreClick;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private final List<User> users = new ArrayList<>();
    private final IOnItemClick itemClick;
    private final IOnMenuMoreClick menuMoreClick;

    public UserAdapter(IOnItemClick itemClick, IOnMenuMoreClick menuMoreClick) {
        this.itemClick = itemClick;
        this.menuMoreClick = menuMoreClick;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateUsers(List<User> users) {
        this.users.clear();
        this.users.addAll(users);
        notifyDataSetChanged();
//        notifyItemChanged(0, this.users.size());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemUserBinding binding = ItemUserBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(users.get(position));
    }

    @Override
    public int getItemCount() {
        return this.users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemUserBinding binding;

        public ViewHolder(ItemUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void onBind(User user) {
            binding.tvEmailUser.setText(user.getEmail());
            binding.tvFullNameUser.setText(user.getFullName());

            Glide.with(binding.getRoot())
                    .load(user.getUrl())
                    .error(R.drawable.ic_not_sp_image)
                    .into(binding.imgAvatarUser);

            //set event click
            binding.getRoot().setOnClickListener(v -> itemClick.onClick(user.getId()));
            binding.imgMore.setOnClickListener(v-> {
                PopupMenu popupMenu = new PopupMenu(binding.getRoot().getContext(), binding.imgMore);
                popupMenu.inflate(R.menu.menu_more);
                popupMenu.setOnMenuItemClickListener(item -> menuMoreClick.menuClick(user, item));
                popupMenu.show();
            });

        }
    }
}
