package thomsonreuters.news.swift.metadata.pimu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//C:\oraclexe\app\oracle\product\10.2.0\server\BIN> - where I can find sqlldr
// md/taxman@10.242.136.242:1521/PRGNPRD.ime.reuters.com - connection string
// system@//localhost:1521/xe - connection string
// 

//sqlldr md/taxman@10.242.136.242:1521/PRGNPRD.ime.reuters.com control=D:\workspace\pimu\sqlloader.ctl log=D:\workspace\pimu\sqllog.log rows=20000 bindsize=1048576

public class Runner { 
	private static Logger logger = LoggerFactory.getLogger(Runner.class);

	public static void main(String[] args) throws Exception {
		logger.info("Application is started.------------");

		new Application().run();

		logger.info("Application finished its work.------------");
	}
}
