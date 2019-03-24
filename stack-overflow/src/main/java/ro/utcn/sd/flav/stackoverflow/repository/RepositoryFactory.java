package ro.utcn.sd.flav.stackoverflow.repository;


public interface RepositoryFactory {

    AccountRepository createAccountRepository();

    QuestionRepository createQuestionRepository();

    TagRepository createTagRepository();

    AnswerRepository createAnswerRepository();

    VoteQuestionRepository createVoteQuestionRepository();

    VoteAnswerRepository createVoteAnswerRepository();


    // Here add other repositories;
}
