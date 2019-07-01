package th.co.thiensurat.business.controller;

import android.app.Activity;
import android.content.Context;

import th.co.bighead.utilities.BHLoading;

public abstract class BackgroundProcess extends TSRController {
	private Context context;

	public BackgroundProcess(Context context) {
		this.context = context;
	}

	protected void before() {
	}

	protected abstract void calling();

	protected void after() {
	}

	public void start() {
        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BHLoading.show(context);
                try {

                    /*//Backgrouness TimerdProc
                    final Calendar startTime = Calendar.getInstance();*/
                    before();
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            try {
                                calling();

                                ((Activity)context).runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        // TODO Auto-generated method stub
                                        try {
                                            BHLoading.close();
                                            after();

                                            /*//Backgrouness TimerdProc
                                            Calendar endTime = Calendar.getInstance();
                                            long difference = endTime.getTimeInMillis() - startTime.getTimeInMillis();

                                            String hms = String.format("%02d:%02d:%02d.%03d", TimeUnit.MILLISECONDS.toHours(difference),
                                                    TimeUnit.MILLISECONDS.toMinutes(difference) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(difference)),
                                                    TimeUnit.MILLISECONDS.toSeconds(difference) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(difference)),
                                                    difference - TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(difference)));
                                            Log.d("BackgroundProcess", hms);*/
                                        } catch (Exception e) {
                                            // TODO: handle exception
                                            throw e;
                                        }
                                    }
                                });
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                BHLoading.close();
                                throw e;//e.printStackTrace();
                            }
                        }
                    });
                    thread.start();

                } catch (Exception e) {
                    // TODO: handle exception
                    BHLoading.close();
                    throw e;
                }
            }
        });
	}

    public void start(final boolean enableLoading) {
        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(enableLoading){BHLoading.show(context);}
                try {
                    before();
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            try {
                                calling();

                                ((Activity)context).runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        // TODO Auto-generated method stub
                                        try {
                                            if(enableLoading){BHLoading.close();}
                                            after();
                                        } catch (Exception e) {
                                            // TODO: handle exception
                                            throw e;
                                        }
                                    }
                                });
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                if(enableLoading){BHLoading.close();}
                                throw e;//e.printStackTrace();
                            }
                        }
                    });
                    thread.start();

                } catch (Exception e) {
                    // TODO: handle exception
                    if(enableLoading){BHLoading.close();}
                    throw e;
                }
            }
        });
    }

    public void start(final boolean enableLoading, final boolean afterLoading) {
        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(enableLoading){BHLoading.show(context);}
                try {
                    before();
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            try {
                                calling();

                                ((Activity)context).runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        // TODO Auto-generated method stub
                                        try {
                                            if(!afterLoading){BHLoading.close();}
                                            after();
                                            if(afterLoading){BHLoading.close();}
                                        } catch (Exception e) {
                                            // TODO: handle exception
                                            throw e;
                                        }
                                    }
                                });
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                if(enableLoading){BHLoading.close();}
                                throw e;//e.printStackTrace();
                            }
                        }
                    });
                    thread.start();

                } catch (Exception e) {
                    // TODO: handle exception
                    if(enableLoading){BHLoading.close();}
                    throw e;
                }
            }
        });
    }
}
