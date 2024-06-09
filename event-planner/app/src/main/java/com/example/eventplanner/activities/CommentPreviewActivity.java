package com.example.eventplanner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventplanner.R;
import com.example.eventplanner.databinding.ActivityCommentPreviewBinding;
import com.example.eventplanner.databinding.ActivityOwnerDashboardBinding;
import com.example.eventplanner.model.Comment;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class CommentPreviewActivity extends AppCompatActivity {

    ActivityCommentPreviewBinding binding;
    LinearLayout commentsContainer;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_comment_preview);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding = ActivityCommentPreviewBinding.inflate(getLayoutInflater());

        commentsContainer = binding.commentsContainer;

        binding.backBtn.setOnClickListener(v->{
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        });

        retrieveCommentsByCompanyId(mAuth.getCurrentUser().getUid());
    }

    private void retrieveCommentsByCompanyId(String companyId){
        db.collection("Comment")
                .whereEqualTo("companyId", companyId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots ->{
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        displayComment(document.toObject(Comment.class));
                    }
                });
    }

    private void displayComment(Comment comment){
        View commentView = LayoutInflater.from(this).inflate(R.layout.comment_card, commentsContainer, false);

        TextView commentGradeTextView = commentView.findViewById(R.id.comment_grade_value);
        commentGradeTextView.setText(comment.getGrade());

        TextView commentDescriptionTextView = commentView.findViewById(R.id.comment_description_value);
        commentDescriptionTextView.setText(comment.getDescription());

        MaterialButton showReportCommentButton = commentView.findViewById(R.id.show_report_comment_btn);
        LinearLayout commentContainer = commentView.findViewById(R.id.comment_container);

        EditText reportDescriptionEditText = commentView.findViewById(R.id.report_description_value);

        MaterialButton reportCommentButton = commentView.findViewById(R.id.report_comment_btn);

        showReportCommentButton.setOnClickListener(v -> {
            if (commentContainer.getVisibility() == View.GONE) {
                commentContainer.setVisibility(View.VISIBLE);
            } else {
                commentContainer.setVisibility(View.GONE);
            }
        });

        reportCommentButton.setOnClickListener(v -> {
            String explanation = reportDescriptionEditText.getText().toString();
            if (!explanation.isEmpty()) {

            } else {
            }
        });

        commentsContainer.addView(commentView);
    }
}