package qna.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qna.NotFoundException;
import qna.domain.Question;
import qna.domain.QuestionRepository;
import qna.domain.User;

@Service
@Transactional(readOnly = true)
public class QnAService {
    private static final Logger log = LoggerFactory.getLogger(QnAService.class);
    private final QuestionRepository questionRepository;
    private final DeleteHistoryService deleteHistoryService;

    public QnAService(QuestionRepository questionRepository, DeleteHistoryService deleteHistoryService) {
        this.questionRepository = questionRepository;
        this.deleteHistoryService = deleteHistoryService;
    }

    @Transactional
    public void deleteQuestion(User loginUser, Long questionId) {
        Question question = findQuestionById(questionId);
        deleteHistoryService.saveAll((question.delete(loginUser)).getHistories());
    }

    private Question findQuestionById(Long id) {
        return questionRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(NotFoundException::new);
    }
}
