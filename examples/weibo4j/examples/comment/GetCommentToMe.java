package weibo4j.examples.comment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import weibo4j.Comments;
import weibo4j.Users;
import weibo4j.model.Comment;
import weibo4j.model.CommentWapper;
import weibo4j.model.Paging;
import weibo4j.model.WeiboException;

public class GetCommentToMe {

	public static void main(String[] args) {
		GetCommentToMe getCommentToMe = new GetCommentToMe();
		
		String access_token = args[0];
		
		//实例化两个用来获取数据的类
		Comments cm = new Comments();
		cm.client.setToken(access_token);
		Users um = new Users();
		um.client.setToken(access_token);
		try {
			Long loopTimes = getCommentToMe.getLoopTimes(cm);
			Long commentsCount = 0L;
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			for (int i = 1; i <= loopTimes; i++) {
				CommentWapper comment = cm.getCommentToMe(new Paging(i, 150),0, 0);
				for (Comment c : comment.getComments()) {
					String id = c.getUser().getScreenName();
					commentsCount++;
					if (map.containsKey(id)) {
						Integer count = map.get(id);
						count++;
						map.put(id, count);
					} else {
						map.put(id, 1);
					}

				}

			}
			
			//对map中数据按value进行降序排序
			ArrayList<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String,Integer>>(map.entrySet());
			Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
				 public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {    
		                return (o2.getValue() - o1.getValue());    
		           } 
			});
			
			System.out.println("first:" + list.get(0).getKey() + ":" + list.get(0).getValue());
			System.out.println("second:" + list.get(1).getKey() + ":" + list.get(1).getValue());
			System.out.println("third:" + list.get(2).getKey() + ":" + list.get(2).getValue());
			System.out.println("fourth:" + list.get(3).getKey() + ":" + list.get(3).getValue());
			System.out.println("comments:" + commentsCount);
			
		} catch (WeiboException e) {
			e.printStackTrace();
		}
	}
	
	private Long getLoopTimes(Comments cm) throws WeiboException {
		CommentWapper comment = cm.getCommentToMe();
		Long totalNumber = comment.getTotalNumber();
		return totalNumber/150 + 1;
	}

}
