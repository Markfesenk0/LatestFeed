package com.example.latestfeed;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.thebluealliance.spectrum.SpectrumPalette;

public class AddPackageDialog extends AppCompatDialogFragment {

    private EditText nameEditText;
    private EditText numberEditText;
    private PackageDialogListener listener;
    private SpectrumPalette palette;
    private int selectedColor;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.package_add_dialog, null);

        builder.setView(view)
                .setTitle("Add Package")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = nameEditText.getText().toString();
                        String number = numberEditText.getText().toString();
                        if (name.equalsIgnoreCase("") || number.length() < 13) {
                            Toast.makeText(getContext(), "Try Again", Toast.LENGTH_SHORT).show();
                        } else {
                            listener.applyInput(name, number, selectedColor);
                            dialog.dismiss();
                        }
                    }
                });
        nameEditText = view.findViewById(R.id.edit_name);
        numberEditText = view.findViewById(R.id.edit_tracking_number);
        palette = view.findViewById(R.id.palette);
        palette.setOnColorSelectedListener(new SpectrumPalette.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                selectedColor = color;
            }
        });
 //       selectedColor = getResources().getColor(R.color.white);

        palette.setSelectedColor(getResources().getColor(R.color.white));
        selectedColor = getResources().getColor(R.color.white);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (PackageDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement PackageDialogListener");
        }
    }

    public void chooseColor(View view) {
//        chosenColor = (ImageView) view;
//        if (chosenColor != null) {
//            chosenColor.setVisibility(View.INVISIBLE);
//        }
//        chosenColor.setVisibility(View.VISIBLE);
    }

    public interface PackageDialogListener {
        void applyInput(String name, String number, int color);
    }
}
