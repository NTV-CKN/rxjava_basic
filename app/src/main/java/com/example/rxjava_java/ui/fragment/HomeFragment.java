package com.example.rxjava_java.ui.fragment;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuProvider;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.example.rxjava_java.MyApplication;
import com.example.rxjava_java.R;
import com.example.rxjava_java.databinding.FragmentHomeBinding;
import com.example.rxjava_java.domain.strategy_sort.SortByNameAscImpl;
import com.example.rxjava_java.domain.strategy_sort.SortByNameDescImpl;
import com.example.rxjava_java.ui.RegisterActivity;
import com.example.rxjava_java.ui.adapter.UserAdapter;
import com.example.rxjava_java.ui.viewmodel.NavigationViewModel;
import com.example.rxjava_java.ui.viewmodel.UserViewModel;
import com.example.rxjava_java.ui.viewmodel.ViewModelFactoryHelper;
import com.example.rxjava_java.utils.SnackBarHelper;
import com.example.rxjava_java.utils.TypeNavigation;

import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomeFragment extends Fragment implements MenuProvider {
    private FragmentHomeBinding binding;
    private UserViewModel userViewModel;
    private NavigationViewModel navigationViewModel;
    private CompositeDisposable disposable;
    private UserAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().addMenuProvider(
                this,
                getViewLifecycleOwner(),
                Lifecycle.State.RESUMED
        );
        setupFields();
        setupFloatingBtn();
        setEventUsers();
    }

    @Override
    public void onStart() {
        super.onStart();
        disposable.add(
                userViewModel.getAllUsers()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(
                                users -> userViewModel.postUsers(users),
                                throwable -> SnackBarHelper.getSnackbarNegative(requireContext(), binding.getRoot(), "Error").show()
                        )
        );
        Log.d("SVU", "Home-Start");
    }

    @Override
    public void onStop() {
        super.onStop();
        //must clear to avoid leak memory
        disposable.clear();
    }

    private void setEventUsers() {
        userViewModel.users.observe(getViewLifecycleOwner(), users -> adapter.updateUsers(users));
    }

    private void setupFloatingBtn() {
        binding.floatingAdd.setOnClickListener(v -> startActivity(new Intent(requireActivity(), RegisterActivity.class)));
    }

    private void setupFields() {
        //setup viewmodels
        MyApplication myApplication = (MyApplication) requireActivity().getApplication();
        userViewModel = new ViewModelProvider(requireActivity(), new ViewModelFactoryHelper(myApplication)).get(UserViewModel.class);
        navigationViewModel = new ViewModelProvider(requireActivity(), new ViewModelFactoryHelper(myApplication)).get(NavigationViewModel.class);


        //declare composite disposable
        disposable = new CompositeDisposable();

        //setup adapter for recyclerview
        adapter = new UserAdapter(
                id ->
                        disposable.add(
                                navigationViewModel.findUserById(id)
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribeOn(Schedulers.io())
                                        .subscribe(
                                                user -> {
                                                    navigationViewModel.setUser(user);
                                                    navigationViewModel.postNav(TypeNavigation.TO_DETAIL);
                                                },
                                                throwable -> SnackBarHelper.getSnackbarNegative(requireContext(), binding.getRoot(), "Error!").show()
                                        )
                        ),
                (user, menuItem) -> {
                    if (menuItem.getItemId() == R.id.menu_delete) {
                        navigationViewModel.setUser(user);
                        navigationViewModel.postNav(TypeNavigation.TO_DELETE);
                        return true;
                    }
                    if (menuItem.getItemId() == R.id.menu_update) {
                        navigationViewModel.setUser(user);
                        Log.d("SVU", user.getId() + "");
                        navigationViewModel.postNav(TypeNavigation.TO_UPDATE);
                        return true;
                    }
                    return false;
                }
        );

        binding.recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), DividerItemDecoration.HORIZONTAL);
        dividerItemDecoration.setDrawable(Objects.requireNonNull(AppCompatResources.getDrawable(requireContext(), R.drawable.divider)));
        binding.recyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_toolbar_main_activity, menu);
        try {
            View searchView = menu.findItem(R.id.menu_search_main_activity).getActionView();
            if (searchView instanceof SearchView) {
                ImageView imgClose = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
                ImageViewCompat.setImageTintList(imgClose, ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white)));

                ImageView imgSearch = searchView.findViewById(androidx.appcompat.R.id.search_button);
                ImageViewCompat.setImageTintList(imgSearch, ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white)));

                ((SearchView) searchView).setQueryHint("Find user by name");
                ((SearchView) searchView).setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        if((newText.isEmpty())) {
                            adapter.updateUsers(userViewModel.users.getValue());
                            return true;
                        }
                        disposable.add(
                                userViewModel.findUserByFullName(newText)
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribeOn(Schedulers.io())
                                        .subscribe(
                                                users -> adapter.updateUsers(users)
                                        )
                        );
                        return true;
                    }
                });
            }
        } catch (Exception ex) {
            Log.e("SVU", Objects.requireNonNull(ex.getMessage()));
        }
    }


    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.menu_sort_main_activity) {
            int[] selectedIndex = {-1};

            AlertDialog.Builder builder = new AlertDialog.Builder(
                    new ContextThemeWrapper(requireContext(), com.google.android.material.R.style.Theme_Material3_Light_Dialog_Alert)
            );

            String[] strs = {"Name A-Z", "Name Z-A"};

            builder.setTitle("Sort by")
                    .setSingleChoiceItems(strs, selectedIndex[0], (dialog, which) -> selectedIndex[0] = which)
                    .setPositiveButton("Ok", (dialog, which) -> {
                        if (selectedIndex[0] == 0) {
                            new SortByNameAscImpl().sortListUser(Objects.requireNonNull(userViewModel.users.getValue()));
                            adapter.updateUsers(userViewModel.users.getValue());
                        } else {
                            new SortByNameDescImpl().sortListUser(Objects.requireNonNull(userViewModel.users.getValue()));
                            adapter.updateUsers(userViewModel.users.getValue());
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
            return true;
        }
        if(menuItem.getItemId() == R.id.menu_more_main_activity) {
            SnackBarHelper.getSnackbarNeutral(binding.getRoot().getContext(), binding.getRoot(), "Updating!").show();
            return true;
        }
        return false;
    }
}
