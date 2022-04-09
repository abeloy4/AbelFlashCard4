package com.example.abelflashcard3;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.security.AccessController;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FlashcardDatabase flashcardDatabase;
    List<Flashcard> allFlashcards;

    int currentCardDisplayedIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView addIcon = findViewById(R.id.add_icon);
        TextView question = findViewById(R.id.flashcard_question);
        TextView answer = findViewById(R.id.flashcard_answer);

        flashcardDatabase = new FlashcardDatabase(getApplicationContext());
        allFlashcards = flashcardDatabase.getAllCards();

        if (allFlashcards != null && allFlashcards.size() > 0) {
            question.setText(allFlashcards.get(0).getQuestion());
            answer.setText(allFlashcards.get(0).getAnswer());
        }

        question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cx = answer.getWidth() / 2;
                int cy = answer.getHeight() / 2;
                float finalRadius = (float) Math.hypot(cx, cy);

                Animator animo = ViewAnimationUtils.createCircularReveal(answer, cx, cy , 2, finalRadius);

                question.setVisibility(View.INVISIBLE);
                answer.setVisibility(View.VISIBLE);

                animo.setDuration(3000);
                animo.start();

            }
        });
        addIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, AddCardActivity.class);
                MainActivity.this.startActivityForResult(intent, 100);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);

            }

        });
        findViewById(R.id.icon_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allFlashcards.size() == 0)
                    return;
                currentCardDisplayedIndex++;
                if (currentCardDisplayedIndex >= allFlashcards.size() - 1){
                    currentCardDisplayedIndex = 0;
                }
                final Animation leftOutAnim = AnimationUtils.loadAnimation(view.getContext(), R.anim.left_out);
                final Animation rightInAnim = AnimationUtils.loadAnimation(view.getContext(), R.anim.right_in);

                question.startAnimation(leftOutAnim);

                leftOutAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        question.startAnimation(leftOutAnim);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        question.startAnimation(rightInAnim);
                        question.setText(allFlashcards.get(currentCardDisplayedIndex).getQuestion());
                        question.setVisibility(View.VISIBLE);
                        answer.setText(allFlashcards.get(currentCardDisplayedIndex).getAnswer());
                        answer.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK){
            String word1 = data.getExtras().getString("QUESTION_KEY");
            String word2 = data.getExtras().getString("ANSWER_KEY");

            TextView addedQuestion = findViewById(R.id.flashcard_question);
            TextView addedAnswer = findViewById(R.id.flashcard_answer);

            addedQuestion.setText(word1);
            addedAnswer.setText(word2);

            flashcardDatabase.insertCard(new Flashcard(word1,word2));
            allFlashcards = flashcardDatabase.getAllCards();
        }


    }
}