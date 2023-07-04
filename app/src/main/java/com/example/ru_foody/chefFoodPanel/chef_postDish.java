package com.example.ru_foody.chefFoodPanel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ru_foody.R;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.Objects;
import java.util.UUID;

public class chef_postDish extends AppCompatActivity {

    ImageButton imageButton;
    Button postDishButton;
    Spinner dishesSpinner;
    TextInputLayout descriptionInput, quantityInput, priceInput;
    String description, quantity, price, dish, RandomUID;
    Uri imageUri;

    Uri resultUri;
    ProgressBar progressBar;

    FirebaseStorage storage;
    StorageReference storageReference, imageRef;
    FirebaseDatabase firebaseDatabase;
    FirebaseFirestore firebaseFirestore;

    Task<Void> databaseReference;
    DatabaseReference dataRef;
    FirebaseAuth firebaseAuth;
    String fName, emailid, mobileno, chefId;

    ActivityResultLauncher<Intent> pickImageLauncher;
    ActivityResultLauncher<Intent> cropImageLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_post_dish);

        progressBar = findViewById(R.id.progressBar);
        dishesSpinner = findViewById(R.id.dishes);
        descriptionInput = findViewById(R.id.description);
        quantityInput = findViewById(R.id.quantity);
        priceInput = findViewById(R.id.price);
        postDishButton = findViewById(R.id.post);
        imageButton = findViewById(R.id.image_upload);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        try {
            String userid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
            dataRef= FirebaseDatabase.getInstance().getReference("Chef").child(userid);

            dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    Chef chef = snapshot.getValue(Chef.class);
                    assert chef != null;
                    fName = chef.getFName();
                    emailid = chef.getEmailId();
                    mobileno = chef.getMobileNo();

                    progressBar.setVisibility(View.INVISIBLE);

                    imageButton.setOnClickListener(v -> onSelectImageClick());

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
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getData() != null) {
                            imageUri = data.getData();
                            if (imageUri != null) {
                                startCropImageActivity();
                            } else {
                                Toast.makeText(chef_postDish.this, "Failed to retrieve image URI.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(chef_postDish.this, "Failed to retrieve image.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        cropImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        assert data != null;
                        resultUri = UCrop.getOutput(data);
                        if (resultUri != null) {
                            imageButton.setImageURI(resultUri);
                            Toast.makeText(chef_postDish.this, "Cropped Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(chef_postDish.this, "Failed to retrieve cropped image URI.", Toast.LENGTH_SHORT).show();
                        }
                    } else if (result.getResultCode() == UCrop.RESULT_ERROR) {
                        assert result.getData() != null;
                        final Throwable error = UCrop.getError(result.getData());
                        assert error != null;
                        Toast.makeText(chef_postDish.this, "Cropping failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

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

    private void uploadImage() {
        if (resultUri != null) {
            if (TextUtils.isEmpty(RandomUID)) {
                RandomUID = UUID.randomUUID().toString(); // Generate a random UUID if RandomUID is empty
            }
            uploadToFirebase(resultUri);
        } else {
            Toast.makeText(chef_postDish.this, "Please Select Image", Toast.LENGTH_SHORT).show();
        }

    }

    private void uploadToFirebase(Uri Uri1) {
        progressBar.setVisibility(View.VISIBLE);
        chefId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        imageRef = storageReference.child(RandomUID);
        UploadTask uploadTask = imageRef.putFile(Uri1);

        uploadTask.addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String imageUrl = Objects.requireNonNull(task.getResult()).toString();

                // Create FoodDetails object with the data
                FoodDetails foodDetails = new FoodDetails(dish, quantity, price, description, imageUrl, RandomUID, chefId);
                databaseReference = FirebaseDatabase.getInstance().getReference("FoodDetails ")
                        .child(chefId)
                        .setValue(foodDetails)
                        .addOnCompleteListener(task1 -> {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(chef_postDish.this, "Dish Posted Successfully!", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(chef_postDish.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            } else {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(chef_postDish.this, "Failed to retrieve image URL.", Toast.LENGTH_SHORT).show();
            }
        })).addOnProgressListener(taskSnapshot -> {
            progressBar.setVisibility(View.VISIBLE);
            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
            progressBar.setProgress((int) progress);

        }).addOnFailureListener(e -> {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(chef_postDish.this, e.getMessage(), Toast.LENGTH_SHORT).show();

        });
    }



    private void onSelectImageClick() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        pickImageLauncher.launch(intent);
    }

    private void startCropImageActivity() {
        if (imageUri != null) {
            Uri destinationUri = Uri.fromFile(new File(getCacheDir(), "cropped_image.jpg"));
            UCrop uCrop = UCrop.of(imageUri, destinationUri)
                    .withAspectRatio(1, 1)
                    .withMaxResultSize(500, 500);
            cropImageLauncher.launch(uCrop.getIntent(this));
        } else {
            Toast.makeText(this, "Failed to retrieve image URI.", Toast.LENGTH_SHORT).show();
        }
    }


}