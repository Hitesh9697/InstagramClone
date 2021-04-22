package com.example.instagramclone;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.instagramclone.databinding.FragmentSharePhotosBinding;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static com.parse.Parse.getApplicationContext;

public class SharePhotosFragment extends Fragment {

    private FragmentSharePhotosBinding binding;
    private Bitmap receivedImageBitMap;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SharePhotosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SharePhotosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SharePhotosFragment newInstance(String param1, String param2) {
        SharePhotosFragment fragment = new SharePhotosFragment();
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
        binding = FragmentSharePhotosBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            //doSomeOperations();
                            try {
                                Uri selectedImage = data.getData();
                                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                                Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String filePath =cursor.getString(columnIndex);
                                cursor.close();
                                receivedImageBitMap = BitmapFactory.decodeFile(filePath);
                                binding.imageViewPlaceHolder.setImageBitmap(receivedImageBitMap);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

        binding.imageViewPlaceHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(
                        getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_GRANTED) {
                    // You can use the API that requires the permission.
                    //performAction(...);

                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    someActivityResultLauncher.launch(intent);

                } else {
                    // You can directly ask for the permission.
                    // The registered ActivityResultCallback gets the result of this request.
                    String[] permission = new String[] {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    //mPermissionResult.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                    permissionResult.launch(permission);
                }
            }
        });

        binding.buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(receivedImageBitMap != null && !binding.editTextDescription.getText().toString().equals("")) {

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    receivedImageBitMap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] bytes = byteArrayOutputStream.toByteArray();
                    ParseFile parseFile = new ParseFile("image.png", bytes);
                    ParseObject parseObject = new ParseObject("Photo");
                    parseObject.put("picture", parseFile);
                    parseObject.put("description", binding.editTextDescription.getText().toString());
                    parseObject.put("username", ParseUser.getCurrentUser().getUsername());
                    parseObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Toast.makeText(getContext(), "Uploading successful!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                if(receivedImageBitMap == null) {
                    Toast.makeText(getContext(), "Please select image to share", Toast.LENGTH_SHORT).show();
                }
                if (binding.editTextDescription.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Please enter description", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return  view;
    }

    private ActivityResultLauncher<String> mPermissionResult = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    if(result) {
                        Log.e(TAG, "onActivityResult: PERMISSION GRANTED");
                    } else {
                        Log.e(TAG, "onActivityResult: PERMISSION DENIED");
                    }
                }
            });
    private ActivityResultLauncher<String[]> permissionResult = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
        @Override
        public void onActivityResult(Map<String, Boolean> result) {
            for (Map.Entry entry : result.entrySet()) {
                Log.e("error", "${it.key} = ${it.value}");
            }
        }
    });

   /* @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }*/
}