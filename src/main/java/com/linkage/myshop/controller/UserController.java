package com.linkage.myshop.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.linkage.myshop.controller.exception.FileEmptyException;
import com.linkage.myshop.controller.exception.FileSizeOutOfLimitException;
import com.linkage.myshop.controller.exception.FileTypeNotSupportException;
import com.linkage.myshop.controller.exception.FileUploadException;
import com.linkage.myshop.entity.User;
import com.linkage.myshop.service.IUserService;
import com.linkage.myshop.service.util.ResponseResult;
/**
 * 控制层
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController{
	public static final String UPLOAD_PARENT_PATH = "uploadAvatar";
	public static final long File_MAX_Size = 5*1024*1024;
	
	public static final List<String> UPLOAD_CONTENT_TYPE = new ArrayList<String>();
	static{
		UPLOAD_CONTENT_TYPE.add("image/jpeg");
		UPLOAD_CONTENT_TYPE.add("image/png");
	}
	@Autowired
	private IUserService iUserService;

	//注册
	@PostMapping("/reg.do")
	public ResponseResult<Void> handleReg(User user){
		iUserService.reg(user);
		return new ResponseResult<Void>(SUCCESS);
	}

	//登录
	@PostMapping("/login.do")
	public ResponseResult<User> handleLogin(@RequestParam("username") String username,@RequestParam("password") String password , HttpSession session){
		System.out.println("login-Controller");
		User user = iUserService.login(username, password);
		//登录后把用户的id和用户名都存入session中
		session.setAttribute("uid", user.getId());
		session.setAttribute("username", user.getUsername());
		return new ResponseResult<User>(SUCCESS,user);
	}

	//修改密码
	@PostMapping("/change_password.do")
	public ResponseResult<Void> handleChangePassword(@RequestParam("old_password") String oldPassword,
			@RequestParam("new_password") String newPassword ,HttpSession session){
		Integer id = getSessionId(session);
		String modifiedUser = session.getAttribute("username").toString();
		System.err.println("id - modifiedUser:"+id+modifiedUser+"\t"+oldPassword+"\t newpassword:"+newPassword+iUserService);
		iUserService.changePassword(id, oldPassword, newPassword, modifiedUser);
		return new ResponseResult<Void>(SUCCESS);
	}

	//显示信息
	@RequestMapping("/show_user_data.do")
	public ResponseResult<User> showUserData(HttpSession session){
		Integer id = getSessionId(session);
		User data = iUserService.showUserData(id);
		return new ResponseResult<User>(SUCCESS,data);
	}

	//修改用户信息
	@PostMapping("/change_Info.do")
	public ResponseResult<Void> handleChangeInfo(User user,HttpSession session){
		System.err.println("change_Info_user:"+user.toString());
		Integer id = getSessionId(session);
		user.setId(id);
		iUserService.changeInfo(user);
		return new ResponseResult<Void>(SUCCESS);
	}
	
	//上传头像
	@PostMapping("/upload_avatar.do")
	public ResponseResult<String> handleUpdateAvatar(@RequestParam("file") MultipartFile uploadfile,
			HttpSession session){
		System.out.println("update_avatar：come in");
		if(uploadfile.isEmpty()){
			throw new FileEmptyException("上传的文件为空！");
		}
		if(uploadfile.getSize() > File_MAX_Size){
			throw new FileSizeOutOfLimitException("上传文件大小:" + uploadfile.getSize() +"byte，超出限制为" + File_MAX_Size + "byte");
		}
		System.err.println("上传contentType:"+uploadfile.getContentType());
		if(!UPLOAD_CONTENT_TYPE.contains(uploadfile.getContentType())){
			throw new FileTypeNotSupportException("上传的文件类型不支持！");
		}
		//获取保存文件的真实路径
		String dir = session.getServletContext().getRealPath(UPLOAD_PARENT_PATH);
		//生成文件对象 
		File dirFile = new File(dir);
		//判断文件路径是否存在
		if(!dirFile.exists()){
			//不存在该路径，创建该文件路径
			dirFile.mkdirs();
		}
		System.err.println("dir:"+dir);
		//上传文件的原文件名
		String originalFilename = uploadfile.getOriginalFilename();
		//加上时间 和 随机数(中间用引号隔开，不然两个数会相加)重新生成文件名
		String saveName = System.currentTimeMillis() + "" + (Math.ceil(Math.random()*1000000)+900000) + originalFilename;
		//最后保存头像的路径名
		String avatar = "../"+UPLOAD_PARENT_PATH +"/" + saveName;
		System.err.println("avatar:"+avatar);
		
		File dest = new File(dir,saveName);
		//保存头像
		try {
			uploadfile.transferTo(dest);
		} catch (IllegalStateException e) {
			throw new FileUploadException("文件上传失败！");
		} catch (IOException e) {
			throw new FileUploadException("文件上传失败！");
		}
		System.err.println("ready updateAvatar:保存头像完成！");
		//从session中获取用户id
		Integer id = getSessionId(session);
		//从session中获取用户名（也可以从业务实现类中查询id时获取用户名）
		String username = session.getAttribute("username").toString();
		//更新头像路径
		iUserService.updateAvatar(id,username,avatar);
		//把头像路径放入返回结果
		ResponseResult<String> rr = new ResponseResult<String>();
		rr.setState(SUCCESS);
		rr.setData(avatar);
		return rr;
	}
	
}


