package at.tnt.pingSite;

import java.util.HashMap;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import at.tnt.utils.Config;

/**
 * Hello world!
 *
 */
public class App {
	
	private static int intervalSource;
	
	
	//"0/5 * * * * ?"
	public static void initJob(String interval, String siteMapPath) {
		try {
			JobDetail job = JobBuilder.newJob(PingJob.class)
					.withIdentity("pingSiteMapJob", "first").build();
			
			JobDataMap jobDataMap = job.getJobDataMap();
			jobDataMap.put("sitemap", siteMapPath);
			

	    	Trigger trigger = TriggerBuilder
			.newTrigger()
			.withIdentity("pingSiteMapTrigger", "first")
			.withSchedule(
				CronScheduleBuilder.cronSchedule(interval))
			.build();

	    	Scheduler scheduler;
			scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
	    	scheduler.scheduleJob(job, trigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		HashMap<String, String> cfg = Config.loadConfig("src/main/resources/config.properties");
		String intervalString = cfg.get("interval");
		String url = cfg.get("sitemap");
		StringBuffer pattern = new StringBuffer("0/");
		pattern.append(intervalString);
		pattern.append(" * * * * ?");
		System.out.println("Bắt đầu chạy. Ping lại sau "+intervalString);
		initJob(pattern.toString(),url);
	}
}
