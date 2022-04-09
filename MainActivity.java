package com.example.rsinghflashcardapp;

import static android.view.View.VISIBLE;

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

import java.util.List;
public class MainActivity extends AppCompatActivity {

    TextView flashcardQuestion;
    TextView flashcardAnswer;
    FlashcardDatabase flashcardDatabase;
    List<Flashcard> allFlashcards;
    int cardIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flashcardQuestion = findViewById(R.id.flashcard_question);
        flashcardAnswer = findViewById(R.id.flashcard_answer);
        flashcardQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flashcardQuestion.setVisibility(View.INVISIBLE);
                flashcardAnswer.setVisibility(VISIBLE);

                View flashCardAnswer = findViewById(R.id.flashcard_answer);

                int cx = flashCardAnswer.getWidth() / 2;
                int cy = flashcardAnswer.getHeight() / 2;

                float finalRadius = (float) Math.hypot(cx, cy);

                Animator anim = ViewAnimationUtils.createCircularReveal(flashCardAnswer, cx, cy, 0f, finalRadius);

                flashcardQuestion.setVisibility(View.INVISIBLE);
                flashCardAnswer.setVisibility(VISIBLE);

                anim.setDuration(3000);
                anim.start();





            }
        });

        






        ImageView addQuestionImageView = findViewById(R.id.flashcard_add_button);
        addQuestionImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddCardActivity.class);

                startActivityForResult(intent, 100);

                Intent i = new Intent(MainActivity.this, AddCardActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });

        flashcardDatabase = new FlashcardDatabase(getApplicationContext());
        allFlashcards = flashcardDatabase.getAllCards();

        if(allFlashcards != null && allFlashcards.size()>0) {
            Flashcard firstCard = allFlashcards.get(0);
            flashcardQuestion.setText(firstCard.getQuestion());
            flashcardAnswer.setText(firstCard.getAnswer());


        }

        findViewById(R.id.flashcard_next_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(allFlashcards == null || allFlashcards.size() == 0){
                    return;

                }


                cardIndex += 1;

                if (cardIndex >= allFlashcards.size()) {
                    Snackbar.make(view, "You have reached the end of the cards",Snackbar.LENGTH_SHORT).show();

                    cardIndex = 0;


                }

//                Flashcard currentCard = allFlashcards.get(cardIndex);
//                flashcardQuestion.setText(currentCard.getQuestion());
//                flashcardAnswer.setText(currentCard.getAnswer());
//
//                flashcardQuestion.setVisibility(View.VISIBLE);
//                flashcardAnswer.setVisibility(View.INVISIBLE);


                final Animation leftOutAnim = AnimationUtils.loadAnimation(view.getContext(), R.anim.left_out);
                final Animation rightInAnim = AnimationUtils.loadAnimation(view.getContext(), R.anim.right_in);

                leftOutAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        // this method is called when the animation first starts



                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        // this method is called when the animation is finished playing
                        flashcardQuestion.startAnimation(rightInAnim);

                        Flashcard currentCard = allFlashcards.get(cardIndex);
                        flashcardQuestion.setText(currentCard.getQuestion());
                        flashcardAnswer.setText(currentCard.getAnswer());

                        flashcardQuestion.setVisibility(VISIBLE);
                        flashcardAnswer.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        // we don't need to worry about this method
                    }
                });

                flashcardQuestion.startAnimation(leftOutAnim);





            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            if(data!=null){
                String questionString= data.getExtras().getString("QUESTION_KEY");
                String answerString = data.getExtras().getString("ANSWER_KEY");
                flashcardQuestion.setText(questionString);
                flashcardAnswer.setText(answerString);

                Flashcard flashcard = new Flashcard(questionString, answerString);
                flashcardDatabase.insertCard(flashcard);

                allFlashcards = flashcardDatabase.getAllCards();

            }

        }



    }






    }
