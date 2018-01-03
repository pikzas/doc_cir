<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/include/back_authority.jsp"%>
<script type="text/javascript">
	//加载页面 获取树 如果为空 初始化一个树
	function initTree() {
		var treeData = ${treeData};
		return treeData;
	};


	$("tr[name='role']").bind("click",function () {
		var tid = $(this).attr("data-id");
		var treeObj = $.fn.zTree.getZTreeObj("ztree1");
		//每次点击前 将所有选项置空
		treeObj.checkAllNodes(false);
		//依据ID查询出所有的菜单
		$.ajax({
			url:'<c:url value="/back/role/getPowerByRole.do"/>',
			data:{roleID:tid},
			success:function(ret) {
				if(ret!=null&&ret!=undefined){
					var json = eval("(" + ret + ")");
					$.each(json,function (i,item){
						//var node = treeObj.getNodeByTId(item.id);
						var node = treeObj.getNodeByParam("id",item.id, null);
						treeObj.checkNode(node, true, false);
					});
				};
			}
		});
	});
	
	
	function changeStyle() {
		var treeObj = $.fn.zTree.getZTreeObj("ztree1");
		//获取选中节点
		var nodes = treeObj.getCheckedNodes(true);
		var menuids = [];
		for(var i = 0;i < nodes.length;i++){
			menuids.push(nodes[i].id);
		}
		var tid = $("tr[name='role'].selected").attr("data-id");
		$.ajax({
			url:"<c:url value='/back/role/updatePower.do'/>",
			type:"post",
			data:{roleID:tid,menuIDs:menuids.toString()},
			success:function (ret) {
				if(ret=="success"){
					//模拟一次点击事件
					$("tr[name='role'].selected").trigger("click");
					$.CurrentNavtab.navtab("refresh");
				}else{
					alert("更新失败");
				}
			}
		});


	}
	

</script>
<div class="bjui-pageContent">
	<div style="float:left; width:300px;">
		<ul id="ztree1" class="ztree" data-toggle="ztree" data-options="{nodes:'initTree',expandAll:true,onClick: 'ZtreeClick',checkEnable:'true'}"></ul>
	</div>
	<div style="margin-left:400px; height:80%; overflow:hidden;">
		<div style=" overflow:hidden;">
			<fieldset style="height:100%;">
				<legend>角色列表</legend>
				<div id="layout-01" style="height:94%; overflow:hidden;">
					<table class="table table-bordered table-hover table-striped table-top" >
						<thead>
						<tr>
							<th>角色名称</th>
							<th>角色描述</th>
							<th>排序</th>
						</tr>
						</thead>
						<tbody>
						<c:forEach items="${roleList}" var="info" varStatus="s">
							<tr data-id="${info.id}" name="role">
								<td>${info.name}</td>
								<td>${info.description}</td>
								<td>${info.sort}</td>
							</tr>
						</c:forEach>
						</tbody>
					</table>
				</div>
			</fieldset>
		</div>

	</div>

	<div style="padding: 20px;">

		<hr>
		<div class="btn-group" style="margin-top: 5px;">
			<button type="button"  class="btn-blue btn-nm" style="float: left;position:fixed;" onclick="changeStyle()">保存权限</button>
		</div>
	</div>
</div>