package main.java.db.UOW;


import main.java.db.mapper.ChoiceMapper;
import main.java.domain.Choice;

import java.util.ArrayList;
import java.util.List;

public class ChoiceUOW implements IUnitOfWork{
    protected static ThreadLocal current = new ThreadLocal();
    protected List<Choice> newObjects = new ArrayList<>();
    protected List<Choice> dirtyObjects = new ArrayList<>();
    protected List<Choice> deletedObjects = new ArrayList<>();

    public static void newCurrent() {
        setCurrent(new ChoiceUOW());
    }

    public static void setCurrent(ChoiceUOW uow) {
        current.set(uow);
    }

    public static ChoiceUOW getCurrent() {
        return (ChoiceUOW) current.get();
    }

    public void registerNew(Choice choice) {
        assert (choice.getExamID()!=0 && choice.getQuestionNumber()!=0) : "choice does not exist";
        assert !dirtyObjects.contains(choice) : "choice is dirty";
        assert !deletedObjects.contains(choice) : "choice is deleted";
        assert !newObjects.contains(choice) : "choice is new";
        newObjects.add(choice);
    }


    public void registerDirty(Choice choice) {
        assert (choice.getExamID()!=0 && choice.getQuestionNumber()!=0) : "choice does not exist";
        assert !deletedObjects.contains(choice) : "choice is deleted";
        if (!dirtyObjects.contains(choice) && !newObjects.contains(choice)) {
            dirtyObjects.add(choice);
        }
    }

    public void registerDeleted(Choice choice) {
        assert (choice.getExamID()!=0 && choice.getQuestionNumber()!=0) : "choice does not exist";
        if (newObjects.remove(choice)) return;
        dirtyObjects.remove(choice);
        if (!deletedObjects.contains(choice)) {
            deletedObjects.add(choice);
        }
    }

    public void registerClean(Choice choice) {
        assert (choice.getExamID()!=0 && choice.getQuestionNumber()!=0) : "choice does not exist";
    }

    @Override
    public void commit() {
        for (Object obj : newObjects) {
            assert obj instanceof Choice;
            ChoiceMapper.insert((Choice) obj);
        }
        for (Object obj : dirtyObjects) {
            assert obj instanceof Choice;
            ChoiceMapper.update((Choice) obj);
        }
        for (Object obj : deletedObjects) {
            assert obj instanceof Choice;
            ChoiceMapper.delete((Choice) obj);
        }
    }

    @Override
    public void registerNew(Object obj) {
        assert obj instanceof Choice;
        registerNew((Choice) obj);
    }

    @Override
    public void registerDirty(Object obj) {
        assert obj instanceof Choice;
        registerDirty((Choice) obj);
    }

    @Override
    public void registerDeleted(Object obj) {
        assert obj instanceof Choice;
        registerDeleted((Choice) obj);
    }

    @Override
    public void registerClean(Object obj) {
        assert obj instanceof Choice;
        registerClean((Choice) obj);
    }

}
