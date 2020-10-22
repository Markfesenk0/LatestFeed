package com.example.latestfeed;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.latestfeed.Adapters.PackageDetailsAdapter;
import com.example.latestfeed.IsraelPost.Entities.MyPackage;
import com.example.latestfeed.IsraelPost.Entities.Parcel;

import java.util.ArrayList;

public class ViewPackageDialog extends AppCompatDialogFragment {

    private static ArrayList<ArrayList<String>> insertList;
    private static RecyclerView detailsRecycler;
    private static PackageDetailsAdapter packageDetailsAdapter;
    private static Parcel parcel;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.package_details_dialog, null);

        builder.setView(view)
                .setTitle("Package Details")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        if (getArguments() != null) {
            parcel = (Parcel) getArguments().getSerializable("package");
            assert parcel != null;
            insertList = parcel.getInfoLines();
            detailsRecycler = view.findViewById(R.id.details_recycler);
            detailsRecycler.setHasFixedSize(true);
            detailsRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            packageDetailsAdapter = new PackageDetailsAdapter(insertList);
            detailsRecycler.setAdapter(packageDetailsAdapter);
        }
        return builder.create();
    }

}