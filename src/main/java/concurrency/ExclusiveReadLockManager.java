package main.java.concurrency;


import main.java.db.mapper.LockMapper;

import java.sql.SQLException;

public class ExclusiveReadLockManager implements LockManager {
    public static ExclusiveReadLockManager instance;

    public static ExclusiveReadLockManager getInstance() {
        if (instance == null) {
            return new ExclusiveReadLockManager();
        }
        return instance;
    }

    public boolean acquireLock(int lockable, String owner) {
        if (!LockMapper.hasKey(lockable)) {
            LockMapper.insert(lockable, owner);
            System.out.println("Exam " + lockable + " is locked by " + LockMapper.getOwner(lockable));
            return true;
        }

        System.out.println("The item is locked by someone else.");
        return false;
    }

    public synchronized void releaseLock(int lockable, String owner) throws Exception {
        String curOwner = LockMapper.getOwner(lockable);
        System.out.println("Trying to release lock with lockable " + lockable);
        System.out.println(curOwner);
        System.out.println(owner);
        if (!owner.equals(curOwner)) {
            throw new Exception("Trying to release lock of another owner.");
        } else {
            LockMapper.delete(lockable);
            System.out.println("Lock on exam " + lockable + " is released by " + owner);
        }
    }

    // check if the lock is owned by the current owner (e.g. page is refreshed)
    public synchronized Boolean checkLock(int lockable, String curOwner) {
        if (LockMapper.hasKey(lockable)) {
            String owner = LockMapper.getOwner(lockable);
            if (owner.equals(curOwner)) {
                return true;
            }
        }
        return false;
    }



}

