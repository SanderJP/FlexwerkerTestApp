FlexwerkerTestApp
=============

LogEngine:
  LogEngine le = LogEngine.getInstance();
- iets loggen:
  le.log("Test1", "Dit is de test!");
- alle logs in een listview
  le.displayLog();
- delete het hele log, geen backup geen 'weet je het zeker', gewoon weg!
  le.deleteLog();