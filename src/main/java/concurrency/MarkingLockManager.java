package main.java.concurrency;

import main.java.db.mapper.SubmissionLockMapper;

public class MarkingLockManager implements LockManager {

    public static MarkingLockManager instance;

    public static MarkingLockManager getInstance() {
        if (instance == null) {
            return new MarkingLockManager();
        }
        return instance;
    }

    public boolean acquireSubmissionLock(int examID, int studentID, String owner) {
        if (!SubmissionLockMapper.hasKey(examID, studentID)) {
            SubmissionLockMapper.insert(examID, studentID, owner);
            System.out.println("Student " + studentID + " and exam " + examID + " is locked by " + owner);
            return true;
        }
        System.out.println("The submission is locked by someone else.");
        return false;
    }

    public synchronized void releaseSubmissionLock(int examID, int studentID, String owner) throws Exception {
        String curOwner = SubmissionLockMapper.getOwner(examID, studentID);
        System.out.println("Trying to release lock with student id " + studentID + " and exam id " + examID);
        System.out.println(curOwner);
        System.out.println(owner);
        if (!owner.equals(curOwner)) {
            throw new Exception("Trying to release lock of another owner.");
        } else {
            SubmissionLockMapper.delete(examID, studentID);
            System.out.println("Lock on student id " + studentID + " and exam id " + examID + " is released by " + owner);
        }
    }

    public synchronized void releaseAllLocksByOwner(String owner) throws Exception {
        System.out.println("Releasing all lock with owner " + owner);
        SubmissionLockMapper.releaseAllLocksByOwner(owner);
    }

    // check if the lock is owned by the current owner (e.g. page is refreshed)
    public synchronized Boolean checkSubmissionLock(int examID, int studentID, String curOwner) {
        if (SubmissionLockMapper.hasKey(examID, studentID)) {
            String owner = SubmissionLockMapper.getOwner(examID, studentID);
            if (owner.equals(curOwner)) {
                return true;
            }
        }
        return false;
    }

}
