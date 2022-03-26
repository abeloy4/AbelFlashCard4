package com.example.abelflashcard3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class AddCardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        EditText addQuestion = findViewById(R.id.new_question);
        EditText addAnswer = findViewById(R.id.new_answer);

        ImageView cancel = findViewById(R.id.cancel_icon);
        ImageView save = findViewById(R.id.save_icon);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent();
                String first_question = addQuestion.getText().toString();
                String first_answer = addAnswer.getText().toString();
                data.putExtra("QUESTION_KEY", first_question);
                data.putExtra("ANSWER_KEY", first_answer);
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }
}