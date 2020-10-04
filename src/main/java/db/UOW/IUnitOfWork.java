package main.java.db.UOW;

interface IUnitOfWork {

    static void newCurrent() {

    }

    static void setCurrent(IUnitOfWork uow) {

    }

    static IUnitOfWork getCurrent() {
        return null;
    }

    void registerNew(Object obj);
    void registerDirty(Object obj);
    void registerClean(Object obj);
    void registerDeleted(Object obj);
    void commit();
}
