//package com.example.s3rius.surveyclient.fragments.SurveyCreatorFragments;
//
//
//import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import com.example.s3rius.surveyclient.R;
//import com.example.s3rius.surveyclient.fragments.surveypac.Answer;
//import com.example.s3rius.surveyclient.fragments.surveypac.Survey;
//
///**
// * A simple {@link Fragment} subclass.
// */
//public class CreateAnswers extends Fragment {
//
//    Survey survey;
//    int questInt;
//    int ansInt;
//    FloatingActionButton button;
//    FloatingActionButton addMore;
//    FloatingActionButton backButton;
//    EditText answer;
//
//    public CreateAnswers() {
//        // Required empty public constructor
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
//                             Bundle savedInstanceState) {
//        getActivity().setTitle(R.string.new_answer);
//        if (getArguments() != null) {
//            Bundle arguments = getArguments();
//            survey = (Survey) arguments.getSerializable("survey");
//            if (arguments.containsKey("questInt")) {
//                questInt = arguments.getInt("questInt");
//                int act = arguments.getInt("act");
//                View InputFragmentView = inflater.inflate(R.layout.fragment_create_answers, container, false);
//                button = (FloatingActionButton) InputFragmentView.findViewById(R.id.addAnsButtonDone);
//                addMore = (FloatingActionButton) InputFragmentView.findViewById(R.id.addOneAnsButtonDone);
//                backButton = (FloatingActionButton) InputFragmentView.findViewById(R.id.answerBackButton);
//                answer = (EditText) InputFragmentView.findViewById(R.id.new_Answer);
//                if(act == 0) {
//                    ansInt = arguments.getInt("ansInt");
//                    addMore.hide();
//                    button.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if (!answer.getText().toString().equals("")) {
//                                survey.getQuestions().get(questInt).getAnswers().get(ansInt).setName(answer.getText().toString());
//                                getFragmentManager().popBackStack();
//                            } else {
//                                Toast.makeText(container.getContext(), "Please enter the answer", Toast.LENGTH_LONG).show();
//                            }
//                        }
//                    });
//                    backButton.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            getFragmentManager().popBackStack();
//                        }
//                    });
//                }else {
//                    button.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if (!answer.getText().toString().equals("")) {
//                                survey.getQuestions().get(questInt).getAnswers().add(new Answer(answer.getText().toString(),0));
//                                getFragmentManager().popBackStack();
//                            } else {
//                                Toast.makeText(container.getContext(), "Please enter the answer", Toast.LENGTH_LONG).show();
//                            }
//                        }
//                    });
//
//                    backButton.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            getFragmentManager().popBackStack();
//                        }
//                    });
//
//                    addMore.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if (!answer.getText().toString().equals("")) {
//                                survey.getQuestions().get(questInt).getAnswers().add(new Answer(answer.getText().toString(),0));
//                                answer.setText("");
//                            } else {
//                                Toast.makeText(container.getContext(), "Please enter the answer", Toast.LENGTH_LONG).show();
//                            }
//                        }
//                    });
//                }
//                return InputFragmentView;
//            }
//        }
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_create_answers, container, false);
//    }
//
//    public Survey getSurvey() {
//        return survey;
//    }
//
//    public void setSurvey(Survey survey) {
//        this.survey = survey;
//    }
//}
