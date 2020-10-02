package main.java.db;


import main.java.db.mapper.QuestionMapper;
import main.java.domain.Question;

import java.util.ArrayList;
import java.util.List;


public class QuestionUOW implements IUnitOfWork{
    protected static ThreadLocal current = new ThreadLocal();
    protected List<Question> newObjects = new ArrayList<>();
    protected List<Question> dirtyObjects = new ArrayList<>();
    protected List<Question> deletedObjects = new ArrayList<>();

    public static void newCurrent() {
        setCurrent(new QuestionUOW());
    }

    public static void setCurrent(QuestionUOW uow) {
        current.set(uow);
    }

    public static QuestionUOW getCurrent() {
        return (QuestionUOW) current.get();
    }

    public void registerNew(Question question) {
        assert (question.getExamID()!=0 && question.getQuestionNumber()!=0) : "question does not exist";
        assert !dirtyObjects.contains(question) : "question is dirty";
        assert !deletedObjects.contains(question) : "question is deleted";
        assert !newObjects.contains(question) : "question is new";
        newObjects.add(question);
    }


    public void registerDirty(Question question) {
        assert (question.getExamID()!=0 && question.getQuestionNumber()!=0) : "question does not exist";
        assert !deletedObjects.contains(question) : "question is deleted";
        if (!dirtyObjects.contains(question) && !newObjects.contains(question)) {
            dirtyObjects.add(question);
        }
    }

    public void registerDeleted(Question question) {
        assert (question.getExamID()!=0 && question.getQuestionNumber()!=0) : "question does not exist";
        if (newObjects.remove(question)) return;
        dirtyObjects.remove(question);
        if (!deletedObjects.contains(question)) {
            deletedObjects.add(question);
        }
    }

    public void registerClean(Question question) {
        assert (question.getExamID()!=0 && question.getQuestionNumber()!=0) : "question does not exist";
    }

    @Override
    public void commit() {
        for (Object obj : newObjects) {
            assert obj instanceof Question;
            QuestionMapper.insert((Question) obj);
        }
        for (Object obj : dirtyObjects) {
            assert obj instanceof Question;
            QuestionMapper.update((Question) obj);
        }
        for (Object obj : deletedObjects) {
            assert obj instanceof Question;
            QuestionMapper.delete((Question) obj);
        }
    }

    @Override
    public void registerNew(Object obj) {
        assert obj instanceof Question;
        registerNew((Question) obj);
    }

    @Override
    public void registerDirty(Object obj) {
        assert obj instanceof Question;
        registerDirty((Question) obj);
    }

    @Override
    public void registerDeleted(Object obj) {
        assert obj instanceof Question;
        registerDeleted((Question) obj);
    }

    @Override
    public void registerClean(Object obj) {
        assert obj instanceof Question;
        registerClean((Question) obj);
    }

}
