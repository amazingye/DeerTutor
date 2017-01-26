package com.ye.deertutor.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ye.deertutor.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CourseFragment extends Fragment {


    public CourseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.course_fg_layout,container,false);
        return view;
    }

}
