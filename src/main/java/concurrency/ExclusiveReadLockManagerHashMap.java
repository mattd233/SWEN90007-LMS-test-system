package main.java.concurrency;


import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ExclusiveReadLockManagerHashMap implements LockManager {
    public static ExclusiveReadLockManagerHashMap instance;

    // key: lockable, value: owner
    public ConcurrentMap<Integer, String> lockMap;

    public static ExclusiveReadLockManagerHashMap getInstance() {
        if (instance == null) {
            return new ExclusiveReadLockManagerHashMap();
        }
        return instance;
    }

    private ExclusiveReadLockManagerHashMap() {
        lockMap = new ConcurrentHashMap<>() {
        };
    }

    public boolean acquireLock(int lockable, String owner) {
        if (!lockMap.containsKey(lockable)) {
            lockMap.put(lockable, owner);
            System.out.println("Exam " + lockable + " is locked by " + lockMap.get(lockable));
            return true;
        }

        System.out.println("A lock already exists for the lockable.");
        return false;
    }

    public synchronized void releaseLock(int lockable, String owner) throws Exception {
        System.out.println(lockMap.size());
        String curOwner = lockMap.get(lockable);
        System.out.println("Trying to release lock with lockable " + lockable);
        System.out.println(curOwner);
        System.out.println(owner);
        if (!owner.equals(curOwner)) {
            throw new Exception("Trying to release lock of another owner.");
        } else {
            lockMap.remove(lockable);
            System.out.println("Lock on exam " + lockable + " is released by " + owner);
        }
    }

    // check if the lock is owned by the current owner (e.g. page is refreshed)
    public synchronized Boolean checkLock(int lockable, String curOwner) {
        if (lockMap.containsKey(lockable)) {
            String owner = lockMap.get(lockable);
            if (owner.equals(curOwner)) {
                return true;
            }
        }
        return false;
    }



}
