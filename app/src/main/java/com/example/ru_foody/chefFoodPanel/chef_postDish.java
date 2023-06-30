package com.example.ru_foody.chefFoodPanel;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.ru_foody.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public class chef_postDish extends AppCompatActivity {

    ImageButton imageButton;
    Button postDishButton;
    Spinner dishesSpinner;
    TextInputLayout descriptionInput, quantityInput, priceInput;
    String description, quantity, price, dish;
    Uri imageUri;
    Bitmap bitmap;

    Uri croppedImageUri;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, dataRef;
    FirebaseAuth firebaseAuth;
    StorageReference ref;
    String fName, emailid, mobileno, chefId, randomUid;

    private static final int PICK_IMAGE_REQUEST_CODE = 123;
    private static final int PERMISSION_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_post_dish);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        dishesSpinner = findViewById(R.id.dishes);
        descriptionInput = findViewById(R.id.description);
        quantityInput = findViewById(R.id.quantity);
        priceInput = findViewById(R.id.price);
        postDishButton = findViewById(R.id.post);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("FoodDetails");

        try {
            String userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
            dataRef = firebaseDatabase.getReference("Chef").child(userId);
            dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    Chef chef = snapshot.getValue(Chef.class);
                    assert chef != null;
                    fName = chef.getFName();
                    emailid = chef.getEmailId();
                    mobileno = chef.getMobileNo();

                    imageButton = findViewById(R.id.image_upload);

                    imageButton.setOnClickListener(v -> {
                        CheckStoragePermission();
                        onSelectImageClick();
                    });

                    postDishButton.setOnClickListener(v -> {
                        dish = dishesSpinner.getSelectedItem().toString().trim();
                        description = Objects.requireNonNull(descriptionInput.getEditText()).getText().toString().trim();
                        quantity = Objects.requireNonNull(quantityInput.getEditText()).getText().toString().trim();
                        price = Objects.requireNonNull(priceInput.getEditText()).getText().toString().trim();

                        if (isValid()) {
                            uploadImage();
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle onCancelled event
                }
            });
        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }
    }

    private void CheckStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(chef_postDish.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(chef_postDish.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else {
                onSelectImageClick();
            }
        } else{
            onSelectImageClick();
        }
    }

    private void uploadImage() {

        if(imageUri != null){
            final ProgressDialog progressDialog = new ProgressDialog(chef_postDish.this);
            progressDialog.setTitle("Uploading.....");
            progressDialog.show();
            randomUid = UUID.randomUUID().toString();
            ref = storageReference.child(randomUid);
            chefId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
            ref.putFile(imageUri).addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl().addOnSuccessListener(uri -> {
                FoodDetails info = new FoodDetails(dish,quantity,price,description,String.valueOf(uri),randomUid,chefId);
                FirebaseDatabase.getInstance().getReference("FoodDetails").child(fName).child(emailid).child(mobileno).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(randomUid)
                        .setValue(info).addOnCompleteListener(task -> {

                            progressDialog.dismiss();
                            Toast.makeText(chef_postDish.this,"Dish Posted Successfully!",Toast.LENGTH_SHORT).show();
                        });

            })).addOnFailureListener(e -> {
                progressDialog.dismiss();
                Toast.makeText(chef_postDish.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }).addOnProgressListener(taskSnapshot -> {
                double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                progressDialog.setMessage("Uploaded "+(int) progress+"%");
                progressDialog.setCanceledOnTouchOutside(false);
            });
        }

    }



    private boolean isValid() {
        descriptionInput.setErrorEnabled(false);
        descriptionInput.setError("");
        quantityInput.setErrorEnabled(false);
        quantityInput.setError("");
        priceInput.setErrorEnabled(false);
        priceInput.setError("");

        boolean isValidDescription = false, isValidPrice = false, isValidQuantity = false, isValid;
        if (TextUtils.isEmpty(description)) {
            descriptionInput.setErrorEnabled(true);
            descriptionInput.setError("Description is required");
        } else {
            descriptionInput.setError(null);
            isValidDescription = true;
        }
        if (TextUtils.isEmpty(quantity)) {
            quantityInput.setErrorEnabled(true);
            quantityInput.setError("Enter number of plates or items");
        } else {
            isValidQuantity = true;
        }
        if (TextUtils.isEmpty(price)) {
            priceInput.setErrorEnabled(true);
            priceInput.setError("Please mention price");
        } else {
            isValidPrice = true;
        }
        isValid = (isValidDescription && isValidQuantity && isValidPrice);
        return isValid;
    }

    private void onSelectImageClick() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        launcher.launch(intent);
    }

    ActivityResultLauncher<Intent> launcher
            = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null && data.getData() != null){

                        imageUri = data.getData();

                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(
                                    chef_postDish.this.getContentResolver(),
                                    imageUri
                            );
                        }catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
    );

    private void startCropImageActivity() {
        if (imageUri != null) {
            UCrop.Options options = new UCrop.Options();
            options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
            options.setCompressionQuality(80);
            options.withMaxResultSize(1024, 1024);

            // Create a file to store the cropped image
            File destinationFile = new File(getCacheDir(), "cropped_image.jpg");
            try {
                destinationFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            croppedImageUri = Uri.fromFile(destinationFile);

            UCrop.of(imageUri, croppedImageUri)
                    .withOptions(options)
                    .start(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onSelectImageClick();
            } else {
                Toast.makeText(this, "Permission not granted. Unable to pick image.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                imageUri = data.getData();
                if (imageUri != null) {
                    startCropImageActivity();
                } else {
                    Toast.makeText(this, "Failed to retrieve image URI.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Failed to retrieve image.", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            assert data != null;
            final Uri resultUri = UCrop.getOutput(data);
            if (resultUri != null) {
                // Handle the cropped image URI (resultUri)
                imageButton.setImageURI(resultUri);
            } else {
                Toast.makeText(this, "Failed to retrieve cropped image URI.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}