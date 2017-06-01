package at.tnt.pingSite;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import at.tnt.utils.HttpUtils;

public class PingJob implements Job {
	
		private static boolean getStringFromInputStream(InputStream is) {

			BufferedReader br = null;
			StringBuilder sb = new StringBuilder();

			String line;
			try {

				br = new BufferedReader(new InputStreamReader(is));
				while ((line = br.readLine()) != null) {
					if(line.contains("Đã nhận được Thông báo Sơ đồ trang web")) {
						System.out.println(line);
						return true;
					};
					sb.append(line);
				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			return false;

		}

	public void execute(JobExecutionContext context)
			throws JobExecutionException {
				JobDataMap dataMap = context.getJobDetail().getJobDataMap();
				String sitemap = dataMap.getString("sitemap");
				StringBuffer url = new StringBuffer("http://google.com.vn/ping?sitemap=");
				url.append(sitemap);
				System.out.println("Bắt đầu bụp nó====>"+url);
				HashMap<String, String> headers = new HashMap<String, String>();
				HttpResponse response = HttpUtils.httpGet(url.toString(), headers);
				InputStream is;
				try {
					is = response.getEntity().getContent();
					boolean isOK = getStringFromInputStream(is); 
					if(isOK) {
						System.out.println("Thành công");
					} else {
						System.out.println("Lỗi cmnr");
					}
				} catch (UnsupportedOperationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
}
