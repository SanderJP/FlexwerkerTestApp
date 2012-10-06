FlexwerkerTestApp
=============

LogEngine:
  LogEngine le = LogEngine.getInstance();
- iets loggen:
  le.log(String key, String value, String logFileName);
  VB: le.log("Test1", "Dit is de test!", "test1.txt");
- een logbestand in een listview
  le.displayLog(String logFileName);
- delete het log bestand, geen backup, geen 'weet je het zeker', gewoon weg!
  le.deleteLog(String logFileName);
  
Toast messages:
  ToastSingleton.makeToast(Context context, String msg);