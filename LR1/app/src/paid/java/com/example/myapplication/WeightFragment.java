package com.example.myapplication;

import android.R.layout;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import android.os.Bundle;

import android.text.InputType;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeightFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeightFragment extends Fragment {

    EditText line_top;
    EditText line_down;
    Spinner spinner_top;
    Spinner spinner_down;

    ConvViewModel convViewModel;
    SharedViewModel sharedViewModel;

    private ClipboardManager clipboardManager;
    private ClipData clipData;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WeightFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WeightFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WeightFragment newInstance(String param1, String param2) {
        WeightFragment fragment = new WeightFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weight, container, false);

        convViewModel = new ViewModelProvider(requireActivity()).get(ConvViewModel.class);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        sharedViewModel.select("");

        line_top = (EditText) view.findViewById(R.id.textView_top);
        line_top.setInputType(InputType.TYPE_NULL);
        line_down = (EditText) view.findViewById(R.id.textView_down);
        line_down.setInputType(InputType.TYPE_NULL);
        spinner_top = (Spinner) view.findViewById(R.id.spinner_top);
        spinner_down = (Spinner) view.findViewById(R.id.spinner_down);

        convViewModel.getSelectedValue().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String value) {
                line_top.setText(value);
            }
        });

        sharedViewModel.getSelected().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String value) {
                setNewVal(value);
            }
        });

        convViewModel.getSelectedSpinnerTop().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer value) {
                String[] values = convViewModel.conv.getValues();
                ArrayAdapter<String> adapter_top = new ArrayAdapter<String>(requireActivity().getBaseContext(), layout.simple_spinner_item, values);// conv.getValues());
                adapter_top.setDropDownViewResource(layout.simple_spinner_dropdown_item);
                spinner_top.setAdapter(adapter_top);
                spinner_top.setSelection(value);
            }
        });

        convViewModel.getSelectedSpinnerDown().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer value) {
                String[] values = convViewModel.conv.getValues();
                ArrayAdapter<String> adapter_down = new ArrayAdapter<String>(requireActivity().getBaseContext(), layout.simple_spinner_item, values);// conv.getValues());
                adapter_down.setDropDownViewResource(layout.simple_spinner_dropdown_item);
                spinner_down.setAdapter(adapter_down);
                spinner_down.setSelection(value);
            }
        });

        spinner_top.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                convViewModel.selectSpinnerTop(position);
                line_down.setText(convViewModel.makeConversion(line_top.getText().toString()));
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spinner_down.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                convViewModel.selectSpinnerDown(position);
                line_down.setText(convViewModel.makeConversion(line_top.getText().toString()));
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        Button button_copy_top = (Button) view.findViewById(R.id.button_copy_top);
        button_copy_top.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String text = "nothing";
                if (!line_top.getText().toString().equals("")) {
                    clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    text = line_top.getText().toString();
                    clipData = ClipData.newPlainText("text", text);
                    clipboardManager.setPrimaryClip(clipData);
                }
                text += " was copied";
                Toast toast = Toast.makeText(getActivity().getBaseContext(), text, Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        Button button_copy_down = (Button) view.findViewById(R.id.button_copy_down);
        button_copy_down.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String text = "nothing";
                if (!line_down.getText().toString().equals("")) {
                    clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    text = line_down.getText().toString();
                    clipData = ClipData.newPlainText("text", text);
                    clipboardManager.setPrimaryClip(clipData);
                }
                text += " was copied";
                Toast toast = Toast.makeText(getActivity().getBaseContext(), text, Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        ImageButton button_change = (ImageButton) view.findViewById(R.id.button_change);
        button_change.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String new_top_text = line_down.getText().toString();
                int id = (int) spinner_top.getSelectedItemId();
                spinner_top.setSelection((int) spinner_down.getSelectedItemId());
                spinner_down.setSelection(id);
                line_top.setText(new_top_text);
                convViewModel.selectValue(new_top_text);
            }
        });


        return view;
    }

    public void setNewVal(String item) {
        String line_value = line_top.getText().toString();
        if (item != null) {
            if (!item.equals("|"))
                line_value += item;
            else {
                String ans = "";
                if (line_value.length() != 0)
                    ans = line_value.substring(0, line_value.length() - 1);
                line_value = ans;
            }
        }
        line_top.setText(line_value);
        String text_down = convViewModel.selectValue(line_value);
        line_down.setText(text_down);
    }

}