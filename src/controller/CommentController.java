package controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;








import entity.Blog;
import entity.Comment;
import service.BlogService;
import service.CommentService;
import util.ResponseUtil;
import net.sf.json.JSONObject;

/**
 * ����Controller��
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/comment")
public class CommentController {
	
	@Resource
	private CommentService commentService;
	
	@Resource
	private BlogService blogService;
	
	/**
	 * ��ӻ����޸�����
	 * @param comment
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/save")
	public String save(Comment comment,@RequestParam("imageCode") String imageCode,HttpServletRequest request,HttpServletResponse response,HttpSession session)throws Exception{
		String sRand=(String) session.getAttribute("sRand"); // ��ȡϵͳ��ɵ���֤��
		JSONObject result=new JSONObject();
		int resultTotal=0; // �����ļ�¼����
		if(!imageCode.equals(sRand)){
			result.put("success", false);
			result.put("errorInfo", "��֤����д����");
		}else{
			String userIp=request.getRemoteAddr(); // ��ȡ�û�IP
			comment.setUserIp(userIp);
			if(comment.getId()==null){
				resultTotal=commentService.add(comment);
				// �ò��͵Ļظ������1
				Blog blog=blogService.findById(comment.getBlog().getId());
				blog.setReplyHit(blog.getReplyHit()+1);
				blogService.update(blog);
			}else{
				
			}
			if(resultTotal>0){
				result.put("success", true);
			}else{
				result.put("success", false);
			}
		}
		ResponseUtil.write(response, result);
		return null;
	}

}
