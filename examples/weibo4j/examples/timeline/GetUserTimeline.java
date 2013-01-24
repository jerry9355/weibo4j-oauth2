package weibo4j.examples.timeline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import weibo4j.Timeline;
import weibo4j.examples.oauth2.Log;
import weibo4j.model.Paging;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;
import weibo4j.model.WeiboException;

public class GetUserTimeline {
	private final String REGEX = "@[\\u4e00-\\u9fa5\\w\\-]+";

	public static void main(String[] args) {
		GetUserTimeline getUserTimeline = new GetUserTimeline();
		
		String access_token = args[0];
		Timeline tm = new Timeline();
		tm.client.setToken(access_token);
		
		try {
			Long loopTimes = getUserTimeline.getLoopTimes(tm);
			Map<String, Integer> map = new HashMap<String, Integer>();
			Long count = 0L;
			for (int i = 1; i <= loopTimes; i++) {
				StatusWapper status = tm.getUserTimelineByUid("1827432437", new Paging(i,100), 0, 0);
				for(Status s : status.getStatuses()){
					count++;
					Log.logInfo(s.getText());
					String text = s.getText();
					Integer position = text.indexOf("//");
					if (position >= 0) {
						text = text.substring(0, position);
					}
					Pattern pattern = Pattern.compile(getUserTimeline.REGEX);
					Matcher matcher = pattern.matcher(text);
					while (matcher.find()) {
						String screenName = matcher.group();
						screenName = screenName.substring(1, screenName.length());
						if (map.containsKey(screenName)) {
							map.put(screenName, map.get(screenName) + 1);
						} else {
							map.put(screenName, 1);
						}
					} 
					
				}
			}
			
			ArrayList<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String,Integer>>(map.entrySet());
			Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
				 public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {    
		                return (o2.getValue() - o1.getValue());    
		           } 
			});
			
			for (int i = 0; i < 4; i++) {
				Map.Entry<String, Integer> entry = list.get(i);
				System.out.println(entry.getKey() + ":" + entry.getValue());
			}
			System.out.println(count);
			
		} catch (WeiboException e) {
			e.printStackTrace();
		}
		
	}
	
	private Long getLoopTimes(Timeline tl) throws WeiboException {
		StatusWapper status = tl.getUserTimeline();
		Long totalNumber = status.getTotalNumber();
		return totalNumber/100 + 1;
		
	}

}
