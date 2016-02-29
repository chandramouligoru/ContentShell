package org.chromium.content_shell_apk;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("unused")
public class WorkspotService extends Service {

    private static final int MAX_THREADS = 8;

    private static final String SCID_NOT_APPLICABLE_FOR_THIS_REQUEST = "scid n/a";

    private final IBinder binder = new WorkspotServiceBinder();
    private final ExecutorService executorService;

    public WorkspotService() {
        super();
        this.executorService = Executors.newFixedThreadPool(MAX_THREADS);
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_NOT_STICKY;
    }
//
//    /*
//		 * Return the Future, in case somebody wants to monitor the progress
//		 */
//    public <RequestType extends O1WebAPIBase> Future<Object> invoke(WorkspotServiceRequest<RequestType> request)
//    {
//        WorkspotServiceCallable<RequestType> callable = new WorkspotServiceCallable<RequestType>(request);
//        return executorService.submit(callable);
//    }
//
//
//    /*
//     * Helper to submit a callable whcih return a value
//     * Since the service has a executor , it can be used to service
//     * all types of requests
//     */
//    public <ReturnType> Future<ReturnType> submit(Callable<ReturnType> callableRequest) {
//        // TODO Auto-generated method stub
//        return executorService.submit(callableRequest);
//    }
//
   public class WorkspotServiceBinder extends Binder {
        public WorkspotService getService() {
            return WorkspotService.this;
        }
    }
//
//
//    private static class WorkspotServiceCallable<RequestType extends O1WebAPIBase>
//            implements Callable<Object>, IMainHandlerCompletion {
//        private final WorkspotServiceRequest<RequestType> request;
//        private final AtomicBoolean requestIsComplete = new AtomicBoolean(false);
//
//        protected WorkspotServiceCallable(
//                final WorkspotServiceRequest<RequestType> request) {
//            this.request = request;
//        }
//
//        public Object call() {
//            try {
//                boolean executionResult = MainHandlerExecutor.getInstance()
//                        .invoke(request.getRequest(), this);
//
//                // If execution failed due to some exception, then send the
//                // failure to the operation
//                if (executionResult == false) {
//                    requestComplete(null);
//                }
//                // TODO: Is this loop required? Is it here to give time to
//                // requestComplete?
//                while (requestIsComplete.get() == false) {
//                    Thread.sleep(10);
//                }
//            } catch (Exception e) {
//                WSLog.d(TAG.WORKSPOTSERVICE,
//                        "Exception in Service executor %d", e.toString());
//            }
//
//            return request.getResponse();
//        }
//
//        public void requestComplete(O1WebAPIBaseResponse response) {
//            if (!Thread.currentThread().isInterrupted()) {
//
//                try {
//                    if (request.getResponseOperation() != null
//                            && response != null) {
//                        request.setResponse(response);
//                        request.getResponseOperation().processResponse(
//                                request.getRequest(), response,
//                                !response.anyErrors());
//                    } else {
//                        //If we receive no response, then we treat this as failure
//                        WSLog.e(TAG.WORKSPOTSERVICE, "Request Failed");
//                        request.getResponseOperation().requestFailed(
//                                request.getRequest());
//                    }
//                } catch (Exception e) {
//                    WSLog.e(TAG.WORKSPOTSERVICE,
//                            "Exception caught in requestComplete", e);
//                }
//            } else {
//                WSLog.d(TAG.WORKSPOTSERVICE,
//                        "Thread Interrupted, not calling processResponse");
//            }
//            requestIsComplete.set(true);
//
//        }
//    }

    @Override
    public void onDestroy() {
        //shutdown the executor service.
        if(executorService != null)
            executorService.shutdown();
    }

//    @Override
//    public void onTaskRemoved(Intent rootIntent) {
//        WSLog.d(TAG.WORKSPOTSERVICE,
//                "Task removed. lets clear the service.");
//        stopSelf();
//    }
}
