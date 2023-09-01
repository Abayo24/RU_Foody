package com.example.ru_foody.chefFoodPanel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.ru_foody.ChefFoodPanel_BottomNavigation;
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
import com.google.firebase.storage.UploadTask;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.Objects;
import java.util.UUID;

public class UpdateDelete_Dish extends AppCompatActivity {

    TextInputLayout description, quantity, price;
    TextView dishName;
    ImageButton imageButton;
    Uri imageUri;
    private Uri resultUri;
    String dbUri;
    Button updateDish, deleteDish;
    String Description, Quantity, Price, Dishes, chefId;
    String RandomUID;
    ProgressBar progressBar;
    StorageReference ref;
    FirebaseStorage storage;
    StorageReference storageReference;

    DatabaseReference databaseReference;
    FirebaseAuth FAuth;
    String ID;
    DatabaseReference data;
    String fName, mobileno, emailid;

    ActivityResultLauncher<Intent> pickImageLauncher;
    ActivityResultLauncher<Intent> cropImageLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete_dish);

        description = findViewById(R.id.description);
        quantity = findViewById(R.id.quantity);
        price = findViewById(R.id.price);
        dishName = findViewById(R.id.dish_name);
        imageButton = findViewById(R.id.image_upload);
        updateDish = findViewById(R.id.updateBtn);
        deleteDish = findViewById(R.id.deleteBtn);
        ID = getIntent().getStringExtra("updatedeletedish");

        String userid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        data = FirebaseDatabase.getInstance().getReference("Chef").child(userid);
        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Chef chef = snapshot.getValue(Chef.class);
                assert chef != null;
                fName = chef.getFname();
                emailid = chef.getEmailId();
                mobileno = chef.getMobileNo();

                updateDish.setOnClickListener(v -> {
                    Description = Objects.requireNonNull(description.getEditText()).getText().toString().trim();
                    Quantity = Objects.requireNonNull(quantity.getEditText()).getText().toString().trim();
                    Price = Objects.requireNonNull(price.getEditText()).getText().toString().trim();

                    if (isValid()) {
                        if (imageUri != null) {
                            uploadImage();
                        } else {
                            updatedesc(dbUri);
                        }
                    }

                });

                deleteDish.setOnClickListener(v -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(UpdateDelete_Dish.this);
                    builder.setMessage("Are You Sure You Want to Delete Dish");
                    builder.setPositiveButton("Yes", (dialog, which) -> {
                        FirebaseDatabase.getInstance().getReference("FoodDetails")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child(ID)
                                .removeValue();
                        AlertDialog.Builder food = new AlertDialog.Builder(UpdateDelete_Dish.this);
                        food.setMessage("Your Dish Has Been Deleted");
                        food.setPositiveButton("OK", (dialog1, which1) -> startActivity(new Intent(UpdateDelete_Dish.this, ChefFoodPanel_BottomNavigation.class)));
                        AlertDialog alert = food.create();
                        alert.show();
                    });
                    builder.setNegativeButton("NO", (dialog, which) -> dialog.cancel());
                    AlertDialog alert = builder.create();
                    alert.show();
                });

                String useridd = FirebaseAuth.getInstance().getCurrentUser().getUid();
                databaseReference = FirebaseDatabase.getInstance().getReference("FoodDetails")
                        .child(useridd)
                        .child(ID);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UpdateDishModel updateDishModel = snapshot.getValue(UpdateDishModel.class);
                        assert updateDishModel != null;
                        Objects.requireNonNull(description.getEditText()).setText(updateDishModel.getDescription());
                        Objects.requireNonNull(quantity.getEditText()).setText(updateDishModel.getQuantity());
                        Objects.requireNonNull(price.getEditText()).setText(updateDishModel.getPrice());
                        Dishes = updateDishModel.getDish();
                        dishName.setText("Dish Name:"+ updateDishModel.getDish());
                        Glide.with(UpdateDelete_Dish.this).load(updateDishModel.getImageURL()).into(imageButton);
                        dbUri = updateDishModel.getImageURL();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                FAuth = FirebaseAuth.getInstance();
                databaseReference = FirebaseDatabase.getInstance().getReference("FoodDetails");
                storage = FirebaseStorage.getInstance();
                storageReference = storage.getReference();
                imageButton.setOnClickListener(v -> onSelectImageClick());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
                                Toast.makeText(UpdateDelete_Dish.this, "Failed to retrieve image URI.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(UpdateDelete_Dish.this, "Failed to retrieve image.", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(UpdateDelete_Dish.this, "Cropped Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(UpdateDelete_Dish.this, "Failed to retrieve cropped image URI.", Toast.LENGTH_SHORT).show();
                        }
                    } else if (result.getResultCode() == UCrop.RESULT_ERROR) {
                        assert result.getData() != null;
                        final Throwable error = UCrop.getError(result.getData());
                        assert error != null;
                        Toast.makeText(UpdateDelete_Dish.this, "Cropping failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updatedesc(String dbUri) {

        chefId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        FoodDetails foodDetails = new FoodDetails( Dishes, Quantity, Price, Description, dbUri, ID, chefId);
        FirebaseDatabase.getInstance().getReference("FoodDetails")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(ID)
                .setValue(foodDetails).addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(UpdateDelete_Dish.this, "Dish Posted Successfully!", Toast.LENGTH_SHORT).show();
                });
    }

    private void uploadImage() {

        if (imageUri != null){
            progressBar.setVisibility(View.VISIBLE);
            RandomUID = UUID.randomUUID().toString();
            ref = storageReference.child(RandomUID);
            UploadTask uploadTask = ref.putFile(imageUri);

            uploadTask.addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    String imageUri = Objects.requireNonNull(task.getResult()).toString();
                    updatedesc(imageUri);

                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(UpdateDelete_Dish.this, "Failed to retrieve image URL.", Toast.LENGTH_SHORT).show();
                }
            })).addOnProgressListener(taskSnapshot -> {
                progressBar.setVisibility(View.VISIBLE);
                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                progressBar.setProgress((int) progress);

            }).addOnFailureListener(e -> {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(UpdateDelete_Dish.this,"Failed : " + e.getMessage(), Toast.LENGTH_SHORT).show();

            });
        }
    }

    private boolean isValid() {
        description.setErrorEnabled(false);
        description.setError("");
        quantity.setErrorEnabled(false);
        quantity.setError("");
        price.setErrorEnabled(false);
        price.setError("");

        boolean isValidDescription = false, isValidPrice = false, isValidQuantity = false, isValid;
        if (TextUtils.isEmpty(Description)) {
            description.setErrorEnabled(true);
            description.setError("Description is required");
        } else {
            description.setError(null);
            isValidDescription = true;
        }
        if (TextUtils.isEmpty(Quantity)) {
            quantity.setErrorEnabled(true);
            quantity.setError("Enter number of plates or items");
        } else {
            isValidQuantity = true;
        }
        if (TextUtils.isEmpty(Price)) {
            price.setErrorEnabled(true);
            price.setError("Please mention price");
        } else {
            isValidPrice = true;
        }
        isValid = (isValidDescription && isValidQuantity && isValidPrice);
        return isValid;
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