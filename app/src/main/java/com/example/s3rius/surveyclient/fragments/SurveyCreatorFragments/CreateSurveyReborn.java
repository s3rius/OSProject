package com.example.s3rius.surveyclient.fragments.SurveyCreatorFragments;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.s3rius.surveyclient.R;
import com.example.s3rius.surveyclient.fragments.TakeSurvey;
import com.example.s3rius.surveyclient.fragments.surveypac.Answer;
import com.example.s3rius.surveyclient.fragments.surveypac.Category;
import com.example.s3rius.surveyclient.fragments.surveypac.Question;
import com.example.s3rius.surveyclient.fragments.surveypac.Survey;
import com.example.s3rius.surveyclient.fragments.surveypac.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateSurveyReborn extends Fragment {
    ViewGroup container;
    Survey newSurvey;
    LayoutInflater inflater;

    public CreateSurveyReborn() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        newSurvey = new Survey();
        newSurvey.setQuestions(new ArrayList<Question>());
        SharedPreferences sPrefs = this.getActivity().getPreferences(Context.MODE_PRIVATE);
        try {
            newSurvey.setCreator(new ObjectMapper().readValue(sPrefs.getString("saved_user", null), User.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        this.container = container;
        this.getActivity().setTitle(R.string.new_survey);
        final View view = inflater.inflate(R.layout.fragment_create_survey_reborn, container, false);
//        final Button addAnswer = (Button)view.findViewById(R.id.addAnswer);
        Button addQuestion = (Button) view.findViewById(R.id.addQuestion);
        final Button creationComplete = (Button) view.findViewById(R.id.creationComplete);
        creationComplete.setVisibility(View.GONE);
        final boolean[] visible = {false};
//        addAnswer.setVisibility(View.GONE);
//        creationComplete.setVisibility(View.GONE);
        creationComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSurveyGood(newSurvey)) {
                    AlertDialog.Builder commentAlert = new AlertDialog.Builder(container.getContext());
                    commentAlert.setTitle(R.string.comment).
                            setMessage(R.string.commentSurvey).
                            setCancelable(true).
                            setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (newSurvey.getComment() != null) {
                                        newSurvey.setComment(null);
                                    }
                                    enterNameUploadSurvey(newSurvey);
                                }
                            }).
                            setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    AlertDialog.Builder commentSurvey = new AlertDialog.Builder(container.getContext());
                                    View commentView = inflater.inflate(R.layout.custom_alert_done_survey, null);

                                    TextView title = (TextView) commentView.findViewById(R.id.tv);
                                    title.setText(getString(R.string.commentThisSurvey));
                                    final EditText comment = (EditText) commentView.findViewById(R.id.surveyName);
                                    if (newSurvey.getComment() != null) {
                                        comment.setHint(newSurvey.getComment());
                                    }
                                    commentSurvey.setView(commentView);
                                    commentSurvey.setCancelable(false)
                                            .setPositiveButton("OK",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(final DialogInterface dialog, int id) {
                                                            if (!isOnlySpacesOrEmpty(comment.getText().toString())) {
                                                                newSurvey.setComment(comment.getText().toString());
                                                                enterNameUploadSurvey(newSurvey);
                                                            } else
                                                                Toast.makeText(container.getContext(), getString(R.string.field_is_empty), Toast.LENGTH_SHORT).show();
                                                        }
                                                    })
                                            .setNegativeButton(getString(R.string.cancel),
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {

                                                        }
                                                    });
                                    AlertDialog alertDialog = commentSurvey.create();
                                    alertDialog.show();
                                }
                            });
                    AlertDialog alertDialog1 = commentAlert.create();
                    alertDialog1.show();
                }
            }
        });
        final LinearLayout questionLayout = (LinearLayout) view.findViewById(R.id.newQuestionLinear);
        addQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!visible[0]) {
                    creationComplete.setVisibility(View.VISIBLE);
                }
                final View newQuestion = inflater.inflate(R.layout.new_question_reborn_rowlayout, null);
                Question question = new Question("", new ArrayList<Answer>());
                question.setId(newSurvey.getQuestions().size());
                newSurvey.getQuestions().add(question);
                Button addAnswer = (Button) newQuestion.findViewById(R.id.addAnswer);
                Button deleteQuestion = (Button) newQuestion.findViewById(R.id.deleteQuestion);
                final EditText questionText = (EditText) newQuestion.findViewById(R.id.newQuestionText);
                questionText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        View questionId = (RelativeLayout) questionText.getParent();
                        for (int i = 0, size = questionLayout.getChildCount(); i < size; i++) {
                            if (questionLayout.getChildAt(i).equals(questionId)) {
                                newSurvey.getQuestions().get(i).setName(s.toString());
                                break;
                            }
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                final LinearLayout newAnswers = (LinearLayout) newQuestion.findViewById(R.id.newAnswerLinear);
                deleteQuestion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean notLast = false;
                        for (int i = 0, size = questionLayout.getChildCount(); i < size; i++) {
                            if (questionLayout.getChildAt(i).equals(v.getParent())) {
                                if (!notLast) {
                                    newSurvey.getQuestions().remove(i);
                                    if (newSurvey.getQuestions().size() == 0) {
                                        creationComplete.setVisibility(View.GONE);
                                        visible[0] = false;
                                    }
                                    try {
                                        newSurvey.getQuestions().get(i);
                                        notLast = true;
                                        size--;
                                    } catch (IndexOutOfBoundsException e) {
                                        break;
                                    }
                                }
                            }
                            if (notLast) {
                                newSurvey.getQuestions().get(i).setId(i);
                            }
                        }
                        questionLayout.removeView((View) v.getParent());
                    }
                });
                addAnswer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        final View newAnswer = inflater.inflate(R.layout.new_answer_reborn_rowlayout, null);
                        final Button deleteAnswer = (Button) newAnswer.findViewById(R.id.deleteAnswer);
                        for (int i = 0, size = questionLayout.getChildCount(); i < size; i++) {
                            if (questionLayout.getChildAt(i).equals(v.getParent())) {
                                Answer answer = new Answer();
                                answer.setUsersAnswered(0);
                                answer.setId(newSurvey.getQuestions().get(i).getAnswers().size());
                                newSurvey.getQuestions().get(i).getAnswers().add(answer);
                                break;
                            }
                        }
                        final EditText answerText = (EditText) newAnswer.findViewById(R.id.newAnswerText);
                        answerText.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                //                                    EditText question = (EditText) ((RelativeLayout) answerText.getParent().getParent().getParent()).getChildAt(0);
//                                    if (!isOnlySpacesOrEmpty(s)) {
//                                        creationComplete.setVisibility(View.VISIBLE);
//                                    } else {
//                                        LinearLayout layout = ((LinearLayout) (((RelativeLayout) answerText.getParent().getParent().getParent()).getParent()));
//                                      }
                                for (int i = 0, size = questionLayout.getChildCount(); i < size; i++) {
                                    if (questionLayout.getChildAt(i).equals(((RelativeLayout) v.getParent()))) {
                                        for (int j = 0, size1 = ((LinearLayout) ((RelativeLayout) questionLayout.getChildAt(i)).getChildAt(1)).getChildCount(); j < size1; j++) {
                                            if ((((RelativeLayout) ((LinearLayout) ((RelativeLayout) questionLayout.getChildAt(i)).getChildAt(1)).getChildAt(j))).equals(answerText.getParent())) {
                                                newSurvey.getQuestions().get(i).getAnswers().get(j).setName(s.toString());
                                                break;
                                            }
                                        }
                                        break;
                                    }
                                }
                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });

                        deleteAnswer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                for (int i = 0, size = questionLayout.getChildCount(); i < size; i++) {
                                    if (questionLayout.getChildAt(i).equals(((LinearLayout) ((RelativeLayout) v.getParent()).getParent()).getParent())) {
                                        boolean notLast = false;
                                        for (int j = 0, size1 = ((LinearLayout) ((RelativeLayout) questionLayout.getChildAt(i)).getChildAt(1)).getChildCount(); j < size1; j++) {
                                            if ((((RelativeLayout) ((LinearLayout) ((RelativeLayout) questionLayout.getChildAt(i)).getChildAt(1)).getChildAt(j))).equals(answerText.getParent())) {
                                                if (!notLast) {
                                                    newSurvey.getQuestions().get(i).getAnswers().remove(j);
                                                    try {
                                                        newSurvey.getQuestions().get(i).getAnswers().get(j);
                                                        notLast = true;
                                                        size1--;
                                                    } catch (IndexOutOfBoundsException e) {
                                                        break;
                                                    }
                                                }
                                            }
                                            if (notLast) {
                                                newSurvey.getQuestions().get(i).getAnswers().get(j).setId(j);
                                            }
                                        }
                                        break;
                                    }
                                }
                                newAnswers.removeView((View) v.getParent());
                            }
                        });
                        newAnswers.addView(newAnswer);
                    }
                });
                questionLayout.addView(newQuestion);
//                creationComplete.setVisibility(View.VISIBLE);

            }
        });
        return view;
    }

    public boolean isOnlySpacesOrEmpty(CharSequence sequence) {
        if (sequence != null) {
            if (!sequence.toString().trim().isEmpty()) {
                return false;
            } else
                return true;
        } else
            return true;
    }

    public boolean isSurveyGood(Survey survey) {
//        for (Question question : survey.getQuestions()) {
//            if (!isOnlySpacesOrEmpty(question.getName())) {
//                for (Answer answer : question.getAnswers()) {
//                    if (isOnlySpacesOrEmpty(answer.getName())) {
//                        return false;
//                    }
//                }
//            } else return false;
//        }
//        return true;
        for (int i = 0, questSize = survey.getQuestions().size(); i < questSize; i++) {
            if (!isOnlySpacesOrEmpty(survey.getQuestions().get(i).getName()) && survey.getQuestions().get(i).getAnswers().size() != 0) {
                for (int j = 0, ansSize = survey.getQuestions().get(i).getAnswers().size(); j < ansSize; j++) {
                    if (isOnlySpacesOrEmpty(survey.getQuestions().get(i).getAnswers().get(j).getName())) {
                        Toast.makeText(container.getContext(), getString(R.string.answer) + " " + (j + 1) + " " + getString(R.string.in) + " " + getString(R.string.question1) + " " + (i + 1) + " " + getString(R.string.is_empty), Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
            } else {
                Toast.makeText(container.getContext(), getString(R.string.question) + " " + (i + 1) + " " + getString(R.string.is_empty) + " " + getString(R.string.orMissingAnswers), Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    public void enterNameUploadSurvey(final Survey survey) {
        View surveyNameView = inflater.inflate(R.layout.custom_alert_done_survey, null);
        final AlertDialog.Builder surveyName = new AlertDialog.Builder(container.getContext());
        surveyName.setView(surveyNameView);
        final EditText surveyNameText = (EditText) surveyNameView.findViewById(R.id.surveyName);
        surveyName.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!isOnlySpacesOrEmpty(surveyNameText.getText().toString())) {
                    survey.setName(surveyNameText.getText().toString());
                    final AlertDialog.Builder builder = new AlertDialog.Builder(container.getContext());
                    final String[][] cats = new String[1][1];
                    AsyncHttpClient client1 = new AsyncHttpClient();
                    client1.setResponseTimeout(20000);
                    client1.get(getString(R.string.server) + "topics/", new AsyncHttpResponseHandler() {
                        ProgressDialog progressDialog = null;

                        @Override
                        public void onStart() {
                            super.onStart();
                            progressDialog = new ProgressDialog(container.getContext());
                            progressDialog.setMessage(getString(R.string.please_wait));
                            progressDialog.show();
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            if (progressDialog != null)
                                progressDialog.dismiss();
                            String responce = new String(responseBody);
                            JSONArray dataJsonObj = null;
                            try {
                                dataJsonObj = new JSONArray(responce);
                                cats[0] = new String[dataJsonObj.length()];
                                for (int i = 0; i < dataJsonObj.length(); i++) {
                                    JSONObject obj = null;
                                    obj = dataJsonObj.getJSONObject(i);
                                    cats[0][i] = (obj.get("name").toString());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            builder.setTitle(getString(R.string.chooseCat));
                            builder.setItems(cats[0], new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    newSurvey.setCategory(new Category().setName(cats[0][which]));
                                    String createdSurvey = "";
                                    try {
                                        createdSurvey = new ObjectMapper().writeValueAsString(newSurvey);
                                    } catch (JsonProcessingException e) {
                                        e.printStackTrace();
                                    }
                                    AsyncHttpClient client = new AsyncHttpClient();
                                    client.setResponseTimeout(20000);
                                    RequestParams params = new RequestParams();
                                    params.put("createdSurvey", createdSurvey);
                                    final ProgressDialog[] progressDialog = new ProgressDialog[1];
                                    client.post(getString(R.string.server) + "createdSurvey/", params, new AsyncHttpResponseHandler() {
                                        ProgressDialog progressDialog = null;

                                        @Override
                                        public void onStart() {
                                            super.onStart();
                                            progressDialog = new ProgressDialog(container.getContext());
                                            progressDialog.setMessage(getString(R.string.please_wait));
                                            progressDialog.show();
                                        }

                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                            if (progressDialog != null)
                                                progressDialog.dismiss();
                                            Toast.makeText(container.getContext(), getString(R.string.succsessfullySent), Toast.LENGTH_SHORT).show();
                                            TakeSurvey fragment = new TakeSurvey();
                                            android.support.v4.app.FragmentTransaction fragmentTransaction =
                                                    getFragmentManager().beginTransaction();
                                            fragmentTransaction.replace(R.id.fragment_container, fragment);
                                            fragmentTransaction.commit();
                                            fragmentTransaction.addToBackStack(null);
                                        }

                                        @Override
                                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                            if (progressDialog != null)
                                                progressDialog.dismiss();
                                            Toast.makeText(container.getContext(), getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                            AlertDialog dialog1 = builder.create();
                            dialog1.show();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            if (progressDialog != null)
                                progressDialog.dismiss();
                            Toast.makeText(container.getContext(), getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(container.getContext(), getString(R.string.field_is_empty), Toast.LENGTH_SHORT).show();
                }
            }
        }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = surveyName.create();
        dialog.show();
    }
}
