package system.management.information.itms;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class IndexFragment extends Fragment {

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private EditText editTextTopic_1;
    private Button buttonSave,buttonedit;
    private TextView Topic;
    private ProgressDialog progressDialog;

    Typeface Fonts;

    public IndexFragment() {
        // Required empty public constructor
    }

    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_index, container, false);
        final Spinner spinner = (Spinner) rootView.findViewById(R.id.spinnerShowEdittext);

        editTextTopic_1 = (EditText) rootView.findViewById(R.id.Topic_1);
        buttonedit = (Button) rootView.findViewById(R.id.btEdit);
        buttonSave = (Button) rootView.findViewById(R.id.save);

        Topic = (TextView) rootView.findViewById(R.id.textView);

        Fonts = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Kanit-Light.ttf");

        Topic.setTypeface(Fonts);
        editTextTopic_1.setTypeface(Fonts);
        buttonSave.setTypeface(Fonts);


        buttonedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTextTopic_1.isEnabled()== false) {
                    editTextTopic_1.setEnabled(true);
                }else{
                    editTextTopic_1.setEnabled(false);
                }
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.planets_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);



        progressDialog = new ProgressDialog(getActivity());
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!= null){

                    mDatabase= FirebaseDatabase.getInstance().getReference().child("Website").child("Index");
                    final FirebaseUser user = firebaseAuth.getCurrentUser();
                    mDatabase.child("Header").addValueEventListener(new ValueEventListener(){
                        @Override
                        public void onDataChange(final DataSnapshot dataSnapshot) {
                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                                    String txt = spinner.getSelectedItem().toString();
                                    if(txt.equals("Header Slide Topic First")) {
                                        buttonedit.setEnabled(true);
                                        editTextTopic_1.setText(dataSnapshot.child("txtTopic_First").getValue().toString());
                                        ((ImageView) rootView.findViewById(R.id.imageSpin)).setImageResource(0);
                                        ((ImageView) rootView.findViewById(R.id.imageSpin)).setImageResource(R.drawable.header_first);
                                    }else if(txt.equals("Header Slide Details First")){
                                        buttonedit.setEnabled(true);
                                        editTextTopic_1.setText(dataSnapshot.child("txtDetails_First").getValue().toString());
                                        ((ImageView) rootView.findViewById(R.id.imageSpin)).setImageResource(0);
                                        ((ImageView) rootView.findViewById(R.id.imageSpin)).setImageResource(R.drawable.header_first);
                                    }else if(txt.equals("Select")){
                                        buttonedit.setEnabled(false);
                                        editTextTopic_1.setText("ข้อมูลจากเว็บไซต์");
                                        ((ImageView) rootView.findViewById(R.id.imageSpin)).setImageResource(0);
                                    }
                                }
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                final DatabaseReference mDatabaseEdit = FirebaseDatabase.getInstance().getReference();

                buttonSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String txt = spinner.getSelectedItem().toString();
                        if(txt.equals("Header Slide Topic First")) {
                            String EditHeader = editTextTopic_1.getText().toString();
                            mDatabaseEdit.child("Website").child("Index").child("Header").child("txtTopic_First").setValue(EditHeader);
                            editTextTopic_1.setEnabled(false);
                        }
                    }
                });
            }
        };


        return rootView;
    }

}
