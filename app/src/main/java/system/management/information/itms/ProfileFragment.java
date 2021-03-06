package system.management.information.itms;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.math.BigInteger;
import java.security.SecureRandom;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment {

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private int CAMERA_REQUEST_CODE = 0;
    private ProgressDialog progressDialog;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private ImageView imageProfile;
    private EditText  textName,textPhone;
    private TextView  textEmail,textViewName,textViewPhone,textViewEmail,textViewProfile;
    private Button btEditImage,btProfile,btEdit,btSave;
    Typeface Fonts;



    public ProfileFragment() {
        // Required empty public constructor
    }

    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        textName = (EditText) rootView.findViewById(R.id.txtNameShow);
        textPhone = (EditText) rootView.findViewById(R.id.txtPhoneShow);
        textEmail = (TextView) rootView.findViewById(R.id.txtEmailShow);

        btEditImage = (Button) rootView.findViewById(R.id.btEditImage);
        btProfile = (Button) rootView.findViewById(R.id.btProfile);
        btEdit = (Button) rootView.findViewById(R.id.btEdit);
        btSave = (Button) rootView.findViewById(R.id.btSave);



//        textViewName = (TextView) rootView.findViewById(R.id.textViewName);
//        textViewPhone = (TextView) rootView.findViewById(R.id.textViewPhone);
//        textViewEmail = (TextView) rootView.findViewById(R.id.textViewEmail);
//        textViewProfile = (TextView) rootView.findViewById(R.id.textViewProfile);

        imageProfile =(ImageView) rootView.findViewById(R.id.imageUser);

        Fonts = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Kanit-Light.ttf");

        textName.setTypeface(Fonts);
        textPhone.setTypeface(Fonts);
        textEmail.setTypeface(Fonts);
        btProfile.setTypeface(Fonts);
        btEditImage.setTypeface(Fonts);
        btEdit.setTypeface(Fonts);
        btSave.setTypeface(Fonts);

//        textViewName.setTypeface(Fonts);
//        textViewPhone.setTypeface(Fonts);
//        textViewEmail.setTypeface(Fonts);
//        textViewProfile.setTypeface(Fonts);

        btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(textName.isEnabled()== false && textPhone.isEnabled()== false && textEmail.isEnabled() == false) {
                    textName.setEnabled(true);
                    textPhone.setEnabled(true);
                }else{
                    textName.setEnabled(false);
                    textPhone.setEnabled(false);
                    textEmail.setEnabled(false);
                }
            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(textName.isEnabled()== false && textPhone.isEnabled()== false && textEmail.isEnabled() == false) {
                    Toast.makeText(getActivity(),"ไม่สามารถบันทึกได้",Toast.LENGTH_SHORT).show();
                }else{

                }
            }
        });


        btEditImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                if(intent.resolveActivity(getActivity().getPackageManager()) != null){
                    startActivityForResult(Intent.createChooser(intent,"กรุณาเลือกรูปประจำตัว"),CAMERA_REQUEST_CODE);
                }
            }
        });

        progressDialog = new ProgressDialog(getActivity());
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
               if(firebaseAuth.getCurrentUser()!= null){
                   mStorage = FirebaseStorage.getInstance().getReference();
                   mDatabase= FirebaseDatabase.getInstance().getReference().child("User");
                   final FirebaseUser user = firebaseAuth.getCurrentUser();
                   mDatabase.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener(){
                       @Override
                       public void onDataChange(DataSnapshot dataSnapshot) {
                            textName.setText(dataSnapshot.child("name").getValue().toString());
                            textPhone.setText(dataSnapshot.child("telephone").getValue().toString());
                            String imageUrl = dataSnapshot.child("image").getValue().toString();
                            textEmail.setText(user.getEmail());
                            if(!imageUrl.equals("ยังไม่มีรูป") || TextUtils.isEmpty(imageUrl)){
                                Picasso.with(getActivity()).load(Uri.parse(dataSnapshot.child("image").getValue().toString())).into(imageProfile);
                            }
                       }

                       @Override
                       public void onCancelled(DatabaseError databaseError) {

                       }

                   });


               }


                btSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String EditName = textName.getText().toString();
                        String EditPhone = textPhone.getText().toString();
                        mDatabase.child(firebaseAuth.getCurrentUser().getUid()).child("name").setValue(EditName);
                        mDatabase.child(firebaseAuth.getCurrentUser().getUid()).child("telephone").setValue(EditPhone);
                        Toast.makeText(getActivity(),"แก้ไขข้อมูลสำเร็จ",Toast.LENGTH_SHORT).show();

                        textName.setEnabled(false);
                        textPhone.setEnabled(false);
                    }
                });
            }
        };



        // Inflate the layout for this fragment
        return rootView;
    }

    public String getRandomString(){
        SecureRandom random = new SecureRandom();
        return new BigInteger(130,random).toString(32);
    }

    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK){

            if (mAuth.getCurrentUser() == null)
                return;
            progressDialog.setMessage("กำลังอัพโหลดรูปภาพ...");
            progressDialog.show();

            final Uri uri = data.getData();

            if (uri == null){
                progressDialog.dismiss();
                return;
            }

            if (mAuth.getCurrentUser() == null)
                return;

            if(mStorage == null)
                mStorage = FirebaseStorage.getInstance().getReference();
            if (mDatabase == null)
                mDatabase = FirebaseDatabase.getInstance().getReference().child("User");

            final  StorageReference filepath = mStorage.child("Photos").child(getRandomString());
            final DatabaseReference currentUser = mDatabase.child(mAuth.getCurrentUser().getUid());

            currentUser.child("image").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String image = dataSnapshot.getValue().toString();

                    if(!image.equals("ยังไม่มีรูป") && !image.isEmpty()){
                        Task<Void> task = FirebaseStorage.getInstance().getReferenceFromUrl(image).delete();
                        task.addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                    Toast.makeText(getActivity(),"ลบรูปภาพสำเร็จ",Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(getActivity(),"ไม่สามารถลบรูปภาพได้",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    currentUser.child("image").removeEventListener(this);

                    filepath.putFile(uri).addOnSuccessListener(getActivity(), new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Uri dowloadUri = taskSnapshot.getDownloadUrl();
                            Toast.makeText(getActivity(),"เปลี่ยนรูปภาพสำเร็จ",Toast.LENGTH_SHORT).show();
                            Picasso.with(getActivity()).load(uri).fit().centerCrop().into(imageProfile);
                            DatabaseReference currentUser = mDatabase.child(mAuth.getCurrentUser().getUid());
                            currentUser.child("image").setValue(dowloadUri.toString());
                        }
                    }).addOnFailureListener(getActivity(), new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }
    }


}
