package com.example.instagramclone;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.instagramclone.databinding.FragmentProfileBinding;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import static android.content.Context.INPUT_METHOD_SERVICE;


public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_MOVE){
                    //do something
                    try {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                return true;
            }
        });



        final ParseUser parseUser = ParseUser.getCurrentUser();

        binding.editTextProfileName.setText(getProfileInfoFromServer(parseUser, "name"));
        binding.editTextProfileBio.setText(getProfileInfoFromServer(parseUser, "bio"));
        binding.editTextProfileProfession.setText(getProfileInfoFromServer(parseUser, "profession"));
        binding.editTextProfileHobbies.setText(getProfileInfoFromServer(parseUser, "hobbies"));
        binding.editTextProfileFavoriteSports.setText(getProfileInfoFromServer(parseUser, "favorite_text"));

        binding.buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                parseUser.put("name", binding.editTextProfileName.getText().toString());
                parseUser.put("bio", binding.editTextProfileBio.getText().toString());
                parseUser.put("profession", binding.editTextProfileProfession.getText().toString());
                parseUser.put("hobbies", binding.editTextProfileHobbies.getText().toString());
                parseUser.put("favorite_text", binding.editTextProfileFavoriteSports.getText().toString());
                parseUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null) {
                            Toast.makeText(getContext(), "Successfully updated!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Update failed! " + e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });

        return view;
    }

    private String getProfileInfoFromServer(ParseUser parseUser, String className ) {
        if (parseUser.get(className) == null){
            return "";
        } else {
            return parseUser.get(className).toString();
        }
    }

}