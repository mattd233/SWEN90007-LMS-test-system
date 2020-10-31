package main.java.concurrency;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

public class LockRemover implements HttpSessionBindingListener {

    private String sessionId;
    public LockRemover(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public void valueBound(HttpSessionBindingEvent httpSessionBindingEvent) {

    }

    public void valueUnbound(HttpSessionBindingEvent event) {
        try {
//            beginSystemTransaction();
            ExclusiveReadLockManager.getInstance().releaseAllLocksByOwner(this.sessionId);
//            commitSystemTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
