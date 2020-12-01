package com.example.myapplication;

import android.R.layout;

import android.content.ClipData;
import android.content.ClipboardManager;

import android.os.Bundle;

import android.text.InputType;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.EditText;

import android.widget.Spinner;


import androidx.fragment.app.Fragment;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;



/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeightFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeightFragment extends Fragment {

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

        final EditText line_top = (EditText) view.findViewById(R.id.textView_top);
        line_top.setInputType(InputType.TYPE_NULL);
        SaveViewModel model_string = new ViewModelProvider(requireActivity()).get(SaveViewModel.class);
        model_string.getSelected().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String value) {
                line_top.setText(value);
            }
        });

        final EditText line_down = (EditText) view.findViewById(R.id.textView_down);
        line_down.setInputType(InputType.TYPE_NULL);

        SharedViewModel model = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        model.getSelected().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String value) {
                new_setConv(value);
            }
        });

        final Spinner spinner_top = (Spinner) view.findViewById(R.id.spinner_top);
        ConversionViewModel model_conv = new ViewModelProvider(requireActivity()).get(ConversionViewModel.class);
        model_conv.getSelected().observe(getViewLifecycleOwner(), new Observer<Conversion>() {
            @Override
            public void onChanged(Conversion value) {
                String[] values = value.getValues();
                ArrayAdapter<String> adapter_top = new ArrayAdapter<String>(requireActivity().getBaseContext(), layout.simple_spinner_item, values);// conv.getValues());
                adapter_top.setDropDownViewResource(layout.simple_spinner_dropdown_item);
                spinner_top.setAdapter(adapter_top);
                SaveTopViewModel model_order_top = new ViewModelProvider(requireActivity()).get(SaveTopViewModel.class);
                model_order_top.getSelected().observe(getViewLifecycleOwner(), new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer order) {
                        spinner_top.setSelection(order);
                    }
                });
            }
        });

        final Spinner spinner_down = (Spinner) view.findViewById(R.id.spinner_down);
        ConversionViewModel model_conv_down = new ViewModelProvider(requireActivity()).get(ConversionViewModel.class);
        model_conv_down.getSelected().observe(getViewLifecycleOwner(), new Observer<Conversion>() {
            @Override
            public void onChanged(Conversion value) {
                String[] values = value.getValues();
                ArrayAdapter<String> adapter_down = new ArrayAdapter<String>(requireActivity().getBaseContext(), layout.simple_spinner_item, values);
                adapter_down.setDropDownViewResource(layout.simple_spinner_dropdown_item);
                spinner_down.setAdapter(adapter_down);
                SaveDownViewModel model_order_down = new ViewModelProvider(requireActivity()).get(SaveDownViewModel.class);
                model_order_down.getSelected().observe(getViewLifecycleOwner(), new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer order) {
                        spinner_down.setSelection(order);
                    }
                });
            }
        });

        spinner_top.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                new_setConv(null);
                SaveTopViewModel model_order = new ViewModelProvider(requireActivity()).get(SaveTopViewModel.class);
                model_order.select(position);
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
                new_setConv(null);
                SaveDownViewModel model_order = new ViewModelProvider(requireActivity()).get(SaveDownViewModel.class);
                model_order.select(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });




        return view;
    }

    public void new_setConv(String item)
    {
        final EditText text_top = (EditText) requireView().findViewById(R.id.textView_top);
        final EditText text_down = (EditText) requireView().findViewById(R.id.textView_down);
        final Spinner spinner_top = (Spinner) requireView().findViewById(R.id.spinner_top);
        final Spinner spinner_down = (Spinner) requireView().findViewById(R.id.spinner_down);

        String line_value = text_top.getText().toString();
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

        final String new_line_value = line_value;
        ConversionViewModel model_conv_down = new ViewModelProvider(requireActivity()).get(ConversionViewModel.class);
        model_conv_down.getSelected().observe(getViewLifecycleOwner(), new Observer<Conversion>() {
            @Override
            public void onChanged(Conversion value) {
                double num;
                String ans_text = "";
                if (!new_line_value.equals("")) {
                    try {
                        num = Double.parseDouble(new_line_value);
                        double ans = value.Convert(spinner_top.getSelectedItem().toString(), spinner_down.getSelectedItem().toString(), num);
                        double ans_double = (double)Math.round(ans * 1000000);
                        ans_double /= 1000000;
                        ans_text = Double.toString(ans_double);

                    } catch (Exception e) {
                        ans_text = "";
                    }
                }
                text_down.setText(ans_text);
                SaveViewModel model_order = new ViewModelProvider(requireActivity()).get(SaveViewModel.class);
                model_order.select(new_line_value);
            }
        });
    }
}