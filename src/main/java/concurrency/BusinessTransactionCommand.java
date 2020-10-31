//package main.java.concurrency;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
//interface Command {
//
//}
//
//public class BusinessTransactionCommand implements Command{
//    public static final String APP_SESSION = "app_session";
//    public static final String LOCK_REMOVER = "lock_remover";
//
//    HttpServletRequest req;
//    HttpServletResponse rsp;
//
//    public BusinessTransactionCommand(HttpServletRequest req, HttpServletResponse rsp) {
//        this.req = req;
//        this.rsp = rsp;
//    }
//
//    protected void startNewBusinessTransaction() throws Exception {
//        HttpSession httpSession = getReq().getSession(true);
//        AppSession appSession = (AppSession) httpSession.getAttribute(APP_SESSION);
//        if (appSession  != null) {
//            ExclusiveReadLockManager.getInstance().releaseAllLocksByOwner(appSession.getId());
//        }
//        appSession = new AppSession(getReq().getRemoteUser(),
//                httpSession.getId());
//        AppSessionManager.setSession(appSession);
//        httpSession.setAttribute(APP_SESSION, appSession);
//        httpSession.setAttribute(LOCK_REMOVER,
//                new LockRemover(appSession.getId()));
//    }
//    protected void continueBusinessTransaction() {
//        HttpSession httpSession = getReq().getSession();
//        AppSession appSession = (AppSession) httpSession.getAttribute(APP_SESSION);
//        AppSessionManager.setSession(appSession);
//    }
//    protected HttpServletRequest getReq() {
//        return req;
//    }
//    protected HttpServletResponse getRsp() {
//        return rsp;
//    }
//
//
//
//
//}
