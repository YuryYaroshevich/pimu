package thomsonreuters.news.swift.metadata.pimu;

import java.util.Date;

//C:\oraclexe\app\oracle\product\10.2.0\server\BIN> - where I can find sqlldr
// md/taxman@10.242.136.242:1521/PRGNPRD.ime.reuters.com - connection string
// system@//localhost:1521/xe - connection string
// 

//sqlldr md/taxman@10.242.136.242:1521/PRGNPRD.ime.reuters.com control=D:\workspace\pimu\sqlloader.ctl log=D:\workspace\pimu\sqllog.log rows=20000 bindsize=1048576

public class Runner {
	public static void main(String[] args) throws Exception {
		System.out.println(new Date() + " ---app start");
		new Application().run();
		System.out.println(new Date() + " ---app finish");
		//Runtime.getRuntime().exec("runSqlldr.bat");

	}
}
