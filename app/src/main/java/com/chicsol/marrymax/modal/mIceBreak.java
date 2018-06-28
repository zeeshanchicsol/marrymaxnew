package com.chicsol.marrymax.modal;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class mIceBreak {


    public List<List<mIceBreak>> getQuestionsChoiceList() {
        return QuestionsChoiceList;
    }

    public void setQuestionsChoiceList(List<List<mIceBreak>> questionsChoiceList) {
        QuestionsChoiceList = questionsChoiceList;
    }

    public List<List<mIceBreak>> getQuestionsList() {
        return QuestionsList;
    }

    public void setQuestionsList(List<List<mIceBreak>> questionsList) {
        QuestionsList = questionsList;
    }

    private List<List<mIceBreak>> QuestionsChoiceList;
    private List<List<mIceBreak>> QuestionsList;

    @SerializedName("question_id")
    public String question_id;

    @SerializedName("question")
    public String question;

    @SerializedName("answer_id")
    public String answer_id;

    @SerializedName("answer")
    public String answer;

    @SerializedName("questionids")
    public String questionids;

    @SerializedName("to_member_id")
    public String to_member_id;

    @SerializedName("to_member_id_dec")
    public String to_member_id_dec;


    @SerializedName("answered")
    public String answered;

    @SerializedName("answer_viewed")
    public String answer_viewed;

    @SerializedName("group_name")
    public String group_name;


    @SerializedName("session_id")
    public String session_id;

    @SerializedName("answerids")
    public String answerids;

    @SerializedName("num")
    public String num;

    @SerializedName("path")
    public String path;

    @SerializedName("userpath")
    public String userpath;

    @SerializedName("alias")
    public String alias;

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer_id() {
        return answer_id;
    }

    public void setAnswer_id(String answer_id) {
        this.answer_id = answer_id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestionids() {
        return questionids;
    }

    public void setQuestionids(String questionids) {
        this.questionids = questionids;
    }

    public String getTo_member_id() {
        return to_member_id;
    }

    public void setTo_member_id(String to_member_id) {
        this.to_member_id = to_member_id;
    }

    public String getTo_member_id_dec() {
        return to_member_id_dec;
    }

    public void setTo_member_id_dec(String to_member_id_dec) {
        this.to_member_id_dec = to_member_id_dec;
    }

    public String getAnswered() {
        return answered;
    }

    public void setAnswered(String answered) {
        this.answered = answered;
    }

    public String getAnswer_viewed() {
        return answer_viewed;
    }

    public void setAnswer_viewed(String answer_viewed) {
        this.answer_viewed = answer_viewed;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public String getAnswerids() {
        return answerids;
    }

    public void setAnswerids(String answerids) {
        this.answerids = answerids;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUserpath() {
        return userpath;
    }

    public void setUserpath(String userpath) {
        this.userpath = userpath;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }


}
