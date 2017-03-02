package com.teamtreehouse.techdegree.overboard.model;

import com.teamtreehouse.techdegree.overboard.exc.AnswerAcceptanceException;
import com.teamtreehouse.techdegree.overboard.exc.VotingException;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by romanmayer on 02/03/2017.
 */
public class QuestionTest {

    private User author;
    private Board board;
    private Question question;

    @Before
    public void setUp() throws Exception {
        board = new Board("King of Queens");
        author = new User(board, "Doug Heffernan");
        question = author.askQuestion("Where is the cheese?");
    }

    @Test
    public void questionersReputationGoesUpBy5PointsIfTheirQuestionIsUpvoted() {
        int reputationBefore = author.getReputation();
        User upvoter = new User(board, "Arthur Spooner");

        question.addUpVoter(upvoter);
        int reputationAfter = author.getReputation();

        assertEquals(reputationBefore + 5, reputationAfter);
    }

    @Test
    public void answerersReputationGoesUpBy10PointsIfTheirAnswerIsUpvoted() {
        User answerer = new User(board, "Carrie Heffernan");
        int reputationBefore = answerer.getReputation();

        Answer answer = answerer.answerQuestion(question, "I told you it's in the car!");
        question.addAnswer(answer);
        answer.addUpVoter(new User(board, "Arthur Spooner"));

        int reputationAfter = answerer.getReputation();
        assertEquals(reputationBefore + 10, reputationAfter);
    }

    @Test
    public void answerersReputationGoesUpBy10PointsIfTheirAnswerIsAccepted() {
        User answerer = new User(board, "Carrie Heffernan");
        int reputationBefore = answerer.getReputation();

        Answer answer = answerer.answerQuestion(question, "I told you it's in the car!");
        question.addAnswer(answer);
        answer.setAccepted(true);
        // addUpVoter(new User(board, "Arthur Spooner"));

        int reputationAfter = answerer.getReputation();
        assertEquals(reputationBefore + 15, reputationAfter);
    }

    @Test(expected = VotingException.class)
    public void authorIsNotAllowedToUpvoteHisOwnQuestion() throws Exception {
        author.upVote(question);
    }

    @Test(expected = VotingException.class)
    public void authorIsNotAllowedToDownvoteHisOwnQuestion() throws Exception {
        author.downVote(question);
    }

    @Test(expected = VotingException.class)
    public void authorIsNotAllowedToUpvoteHisOwnAnswer() throws Exception {
        User answerer = new User(board, "Carrie Heffernan");
        Answer answer = answerer.answerQuestion(question, "FRIDGE!!!");
        answerer.upVote(answer);
    }

    @Test(expected = VotingException.class)
    public void authorIsNotAllowedToDownvoteHisOwnAnswer() throws Exception {
        User answerer = new User(board, "Carrie Heffernan");
        Answer answer = answerer.answerQuestion(question, "FRIDGE!!!");
        answerer.downVote(answer);
    }

    @Test
    public void theOriginalQuestionerIsAllowedToAcceptAnAnswer() {
        User answerer = new User(board, "Carrie Heffernan");
        Answer answer = answerer.answerQuestion(question, "FRIDGE!!!");
        author.acceptAnswer(answer); // change author to "questioner"
    }

    @Test(expected = AnswerAcceptanceException.class)
    public void onlyTheOriginalQuestionerIsAllowedToAcceptAnAnswer() {
        User answerer = new User(board, "Carrie Heffernan");
        Answer answer = answerer.answerQuestion(question, "FRIDGE!!!");

        User randomUser = new User(board, "Deacon Palmer");
        randomUser.acceptAnswer(answer); // change author to "questioner"
    }

}
