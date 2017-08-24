package system.management.information.itms;

import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;

public class PostActivity extends AppCompatActivity {

    private ImageButton mSelectImage;
    private EditText mTxtHeader,mTxtDetail;
    private String date,currentDateTimeString;
    private Button btnSubmit;
    private Uri mImageUri = null;
    private  static final int GALLERY_REQUEST =1;
    StorageReference mStorage;
    ProgressDialog progressDialog;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        date = simpleDateFormat.format(new Date());

        currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());


        mSelectImage = (ImageButton) findViewById(R.id.imageSelect);
        mTxtHeader = (EditText) findViewById(R.id.txtHeader);
        mTxtDetail = (EditText) findViewById(R.id.txtDetail);
        btnSubmit = (Button) findViewById(R.id.submitBtn);

        progressDialog = new ProgressDialog(this);
        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPosting();
            }
        });
    }

    private void  startPosting(){

        progressDialog.setMessage("Posting to Blog...");
        progressDialog.show();
        final String header_val = mTxtHeader.getText().toString().trim();
        final String detail_val = mTxtDetail.getText().toString().trim();
        final String date_time_val = currentDateTimeString;

        if(!TextUtils.isEmpty(header_val) && !TextUtils.isEmpty(detail_val) && mImageUri!=null){
                StorageReference filepath = mStorage.child("Blog_Image").child(mImageUri.getLastPathSegment());

                filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUri = taskSnapshot.getDownloadUrl();

                        DatabaseReference newPost = mDatabase.push();
                        newPost.child("Header").setValue(header_val);
                        newPost.child("Detail").setValue(detail_val);
                        newPost.child("Photos").setValue(downloadUri.toString());
                        newPost.child("Date").setValue(date_time_val);
                        progressDialog.dismiss();
                        Toast.makeText(PostActivity.this,"Save data complete.",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(PostActivity.this,MainActivity.class));
                    }
                });
        }
    }


    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            mImageUri = data.getData();
            mSelectImage.setImageURI(mImageUri);
        }
    }
}
