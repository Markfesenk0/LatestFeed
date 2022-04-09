package com.example.latestfeed.IHerb;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.latestfeed.R;
import com.thebluealliance.spectrum.SpectrumPalette;

public class AddIHerbItemDialog extends AppCompatDialogFragment {

    private EditText urlAdrressText;
    private IHerbItemDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.iherb_item_add_dialog, null);

        builder.setView(view)
                .setTitle("Add Item")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String url = urlAdrressText.getText().toString();
                        if (!URLUtil.isValidUrl(url)) {
                            Toast.makeText(getContext(), "Try Again", Toast.LENGTH_SHORT).show();
                        } else {
                            listener.applyInputIHerbItem(url);
                            dialog.dismiss();
                        }
                    }
                });
        urlAdrressText = view.findViewById(R.id.edit_iherb_address);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (IHerbItemDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement IHerbItemDialogListener");
        }
    }


    public interface IHerbItemDialogListener {
        void applyInputIHerbItem(String url);
    }
}
