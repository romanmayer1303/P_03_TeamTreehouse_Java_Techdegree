package com.teamtreehouse.techdegree.overboard.model;

import com.teamtreehouse.techdegree.overboard.exc.AnswerAcceptanceException;
import com.teamtreehouse.techdegree.overboard.exc.VotingException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserTest {

    private User questioner;
    private User answerer;
    private User randomUser;
    private Board board;
    private Question question;
    private Answer answer;

    @Before
    public void setUp() throws Exception {
        board = new Board("King of Queens");
        questioner = new User(board, "Doug Heffernan");
        question = questioner.askQuestion("Where is the cheese?");
        answerer = new User(board, "Carrie Heffernan");
        answer = answerer.answerQuestion(question, "I told you it's in the car!");
        randomUser = new User(board, "Deacon Palmer");
    }

    @Test
    public void questionersReputationGoesUpBy5PointsIfTheirQuestionIsUpvoted() {
        int reputationBefore = questioner.getReputation();

        question.addUpVoter(randomUser);
        int reputationAfter = questioner.getReputation();

        assertEquals(reputationBefore + 5, reputationAfter);
    }

    @Test
    public void answerersReputationGoesUpBy10PointsIfTheirAnswerIsUpvoted() {
        // arrange
        int reputationBefore = answerer.getReputation();

        // act
        question.addAnswer(answer);
        answer.addUpVoter(randomUser);

        // assert
        int reputationAfter = answerer.getReputation();
        assertEquals(reputationBefore + 10, reputationAfter);
    }

    @Test
    public void answerersReputationGoesUpBy15PointsIfTheirAnswerIsAccepted() {
        int reputationBefore = answerer.getReputation();

        question.addAnswer(answer);
        answer.setAccepted(true);
        int reputationAfter = answerer.getReputation();

        assertEquals(reputationBefore + 15, reputationAfter);
    }

    @Test
    public void answerersReputationGoesDownBy1PointIfTheirAnswerIsDownVoted() {
        int reputationBefore = answerer.getReputation();

        question.addAnswer(answer);
        answer.addDownVoter(randomUser);

        int reputationAfter = answerer.getReputation();
        assertEquals(reputationBefore - 1, reputationAfter);
    }

    @Test(expected = VotingException.class)
    public void authorIsNotAllowedToUpvoteHisOwnQuestion() throws Exception {
        questioner.upVote(question);
    }

    @Test(expected = VotingException.class)
    public void authorIsNotAllowedToDownvoteHisOwnQuestion() throws Exception {
        questioner.downVote(question);
    }

    @Test(expected = VotingException.class)
    public void authorIsNotAllowedToUpvoteHisOwnAnswer() throws Exception {
        answerer.upVote(answer);
    }

    @Test(expected = VotingException.class)
    public void authorIsNotAllowedToDownvoteHisOwnAnswer() throws Exception {
        answerer.downVote(answer);
    }

    @Test
    public void theOriginalQuestionerIsAllowedToAcceptAnAnswer() {
        questioner.acceptAnswer(answer);
    }

    @Test(expected = AnswerAcceptanceException.class)
    public void onlyTheOriginalQuestionerIsAllowedToAcceptAnAnswer() {
        randomUser.acceptAnswer(answer);
    }

}