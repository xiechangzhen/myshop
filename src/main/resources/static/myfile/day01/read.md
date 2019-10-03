mybatis.mapper-locations=classpath:mappers/*.xml

CREATE DATABASE myshopDB;

	CREATE TABLE t_user(
		id INT AUTO_INCREMENT COMMENT 'id',
		username VARCHAR(20) UNIQUE NOT NULL COMMENT '用户名',
		password CHAR(32) NOT NULL COMMENT '密码',
		salt CHAR(36) NOT NULL COMMENT '盐值（UUID）',
		gender INT COMMENT '性别',
		phone VARCHAR(20) COMMENT '手机号码',
		email VARCHAR(50) COMMENT '邮箱',
		avatar VARCHAR(100) COMMENT '头像',
		is_delete INT DEFAULT 0 COMMENT '是否删除，0-未删除，1-已删除',
		create_user VARCHAR(20) COMMENT '创建者',
		create_time DATETIME COMMENT '创建时间',
		modified_user VARCHAR(20) COMMENT '最后修改者',
		modified_time DATETIME COMMENT '最后修改时间',
		PRIMARY KEY(id)
	) DEFAULT CHARSET=UTF8;
	
留言板
	发布留言
	用户注册和登录
	删除留言
	修改留言
	用户头像
	修改资料
	回复
	删除回复
	权限管理
	
###加密
本次加密使用了随机的盐，可以使用UUID作为随机盐。
>UUID是指在一台机器上生成的数字，它保证对在同一时空中的所有机器都是唯一的，通常平台会提生成的API。按照开放软件基金会(OSF)制定的标准计算，
用到了以太网卡地址，纳秒时间、芯片ID码和许多可能的数字。
在java中，通过java.util.UUID类即可轻松的得到随机的UUID值：String uuid = UUID.randomUUID().toString();

登录

		<script type="text/javascript">
				//loginBtn
			$("#btn-login").click(function(){
				var url = "../user/login.do";
				var data = $("#form-login").serialize();
				console.log("登录数据："+data);
				$.ajax({
					"url":url,
					"data":data,
					"type":"POST",
					"dataType":"json",
					"success":function(json){
						if(json.state == 200){
							alert("登录成功");
							if($.cookie("avatar") != null){
								$.cookie("avatar",json.data.avatar,{expires:7});
							}
							console.log("登录成功，将头像路径存到Cookie：" + $.cookie("avatar"));
							location.href="index.html";
						} else if(json.state ==401 ){
						 	alert(json.message);
						} else if(json.state == 402){
							alert(json.message);
						} else{
							alert("登录异常");
						}
					}
				});
			});
		</script>

上传头型

	<script type="text/javascript">
	$(document).ready(function() {
		if ($.cookie("avatar") != null) {
			$("#img-avatar").attr("src", $.cookie("avatar"));
		}
	});
	
	$("#btn-upload").click(function() {
		var url = "../user/upload.do";
		var data = new FormData($("#form-upload")[0]);
		console.log("data=" + data);
		$.ajax({
			"url": url,
			"data": data,
			"type": "POST",
			"dataType": "json",
			"contentType": false,
			"processData": false,
			"success": function(json) {
				if (json.state == 200) {
					alert("修改头像成功！");
					$("#img-avatar").attr("src", json.data);
					  $.cookie("avatar", json.data, {
				            expires: 7
				        });

				} else {
					alert(json.message);
				}
			},
			"error": function() {
				alert("修改头像出错！");
			}
		});
	});
	</script>
	
###省市区联动:

	<script src="../bootstrap3/jquery-1.9.1.min.js"></script>
	<script type="text/javascript" src="../js/distpicker.data.js"></script>
	<script type="text/javascript" src="../js/distpicker.js"></script>

	<div data-toggle="distpicker">
		<select ></select> 
		<select ></select>
		<select></select>
	</div>	

用上导入2个和地区有关的js，data-toggle="distpicker"属性和3个select即可；
	
###使用Representational State Transfer （REST）
	@Autowired
	private IDistrictService districtService;
	//@RequestMapping("/list.do") 
	//如参数：parent则    1. {parent}  2.@PathVariable("parent")  3.js ：	var url = "../district/list/86";
	@RequestMapping("/list/{parent}")
	public ResponseResult<List<District>> handleShow(@PathVariable("parent") String parent){
		List<District> districts = districtService.getByParent(parent);
		return new ResponseResult<List<District>>(SUCCESS,districts);
	}
	