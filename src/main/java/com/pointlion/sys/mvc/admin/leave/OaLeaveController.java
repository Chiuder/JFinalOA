package com.pointlion.sys.mvc.admin.leave;

import java.util.HashMap;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.pointlion.sys.interceptor.MainPageTitleInterceptor;
import com.pointlion.sys.mvc.common.base.BaseController;
import com.pointlion.sys.mvc.common.model.OaLeave;
import com.pointlion.sys.mvc.common.utils.UuidUtil;

@Before(MainPageTitleInterceptor.class)
public class OaLeaveController extends BaseController {
	public static final OaLeaveService service = OaLeaveService.me;
	/***
	 * 列表页面
	 */
	public void getApplyListPage(){
		setBread("功能名称",this.getRequest().getServletPath(),"管理");
    	render("/WEB-INF/admin/leave/list.html");
    }
	/***
     * 获取分页数据
     **/
    public void listData(){
    	String curr = getPara("pageNumber");
    	String pageSize = getPara("pageSize");
    	Page<Record> page = service.getPage(Integer.valueOf(curr),Integer.valueOf(pageSize));
    	renderPage(page.getList(),"",page.getTotalRow());
    }
    /***
     * 保存
     */
    public void save(){
    	OaLeave o = getModel(OaLeave.class);
    	if(StrKit.notBlank(o.getId())){
    		o.update();
    	}else{
    		o.setId(UuidUtil.getUUID());
    		o.save();
    	}
    	renderSuccess();
    }
    /***
     * 获取编辑页面
     */
    public void getEditPage(){
    	setBread("请假销假",this.getRequest().getServletPath().substring(0,this.getRequest().getServletPath().lastIndexOf("/")+1)+"getApplyListPage","请假申请");
    	//添加和修改
    	String id = getPara("id");
    	if(StrKit.notBlank(id)){//修改
    		OaLeave o = service.getById(id);
    		setAttr("o", o);
    	}
    	render("/WEB-INF/admin/leave/edit.html");
    }
    /***
     * 删除
     * @throws Exception
     */
    public void delete() throws Exception{
		String ids = getPara("ids");
    	//执行删除
		service.deleteByIds(ids);
    	renderSuccess("删除成功!");
    }
    
    /**************************************************************************/
	public void setBread(String name,String url,String nowBread){
		Map<String,String> pageTitleBread = new HashMap<String,String>();
		pageTitleBread.put("pageTitle", name);
		pageTitleBread.put("url", url);
		pageTitleBread.put("nowBread", nowBread);
		this.setAttr("pageTitleBread", pageTitleBread);
	}
}