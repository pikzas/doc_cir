<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/include/back_authority.jsp"%>
<script type="text/javascript">
	//加载页面 获取树 如果为空 初始化一个树
	function initTree() {

		var treeData = ${treeData};

		return treeData;
	}

	//树节点的单击事件
    function ZtreeClick(event, treeId, treeNode) {
        event.preventDefault();
        var zTree = $.fn.zTree.getZTreeObj("ztree1");
        var upNode = zTree.getSelectedNodes()[0];
        if (!upNode) {
            alert('未选中任何菜单！');
            return;
        }
		if(upNode.id == undefined){
			upNode.id = getUUID();
		}

        zTree.updateNode(upNode);
        var $detail = $('#ztree-detail');
		if (treeNode.id) {
			$('#j_menu_id').val(treeNode.id);
		} else {
			$('#j_menu_id').val('');
		}
		if (treeNode.pId) {
			$('#j_menu_pId').val(treeNode.pId);
		} else {
			$('#j_menu_pId').val('');
		}
        if (treeNode.name) {
       		$('#j_menu_name').val(treeNode.name);
        }
        if (treeNode.url) {
            $('#j_menu_url').val(treeNode.url);
        } else {
            $('#j_menu_url').val('');
        }
        if (typeof treeNode.sort !='undefined' && treeNode.sort !=null) {
            $('#j_menu_sort').val(treeNode.sort);
        } else {
            $('#j_menu_sort').val('');
        }
        if (treeNode.description) {
        	$('#j_menu_description').val(treeNode.description);
        } else {
            $('#j_menu_description').val('');
        }

        if(treeNode.level == 0 || treeNode.level == 1){
        	$('#j_menu_url').parent().hide();
        }else{
        	$('#j_menu_url').parent().show();
        }

        $detail.attr('tid', treeNode.tId);
        $detail.show();
    }

  	//保存属性
    function M_Ts_Menu() {
        var zTree = $.fn.zTree.getZTreeObj("ztree1");
        var name = $('#j_menu_name').val();
        var url = $('#j_menu_url').val();
        var sort = $('#j_menu_sort').val();
        var description = $('#j_menu_description').val();
        var id = $('#j_menu_id').val();
        if ($.trim(name).length == 0) {
            alert('菜单名称输入有误！');
            return;
        }
        var upNode = zTree.getSelectedNodes()[0];
        if (!upNode) {
            alert('未选中任何菜单！');
            return
        }
		upNode.id=id;
		upNode.pId=upNode.getParentNode().id;
		upNode.name = name;
		upNode.url = url;
		upNode.sort = sort;
		upNode.description = description;
		$('#diyBtn_del_undefined').remove();
		$('#diyBtn_add_undefined').remove();
		zTree.updateNode(upNode);

    }

  	//删除前事件
    function M_BeforeRemove(treeId, treeNode) {
        if(typeof treeNode.id == 'undefined' || treeNode.id == null){
        	return true;
        }else{
        	var result;
        	$.ajax({
            	url:"<c:url value="/back/menu/removeMenu.do"/>?MenuId="+treeNode.id,
            	type:"get",
            	async:false,
            	success:function(ret){
            		result = ret;
            	}
            });
        	return result;
        }
    }
  	//删除结束事件
    function M_NodeRemove(event, treeId, treeNode) {
    	$('#ztree-detail').hide();
    }


	//提交到数据库
	function submitTree() {
		var tree = $.fn.zTree.getZTreeObj("ztree1");
		var menus = tree.getNodes();
		var treeArray = tree.transformToArray(menus);
		var postData = "[";
		for(var i=0;i<treeArray.length;i++){
			var jsonObject = new Object();
			jsonObject.id =treeArray[i].id;
			jsonObject.pId =treeArray[i].pId;
			jsonObject.name =treeArray[i].name;
			jsonObject.url =treeArray[i].url;
			jsonObject.sort =treeArray[i].sort;
			jsonObject.description =treeArray[i].description;
			jsonObject.level =treeArray[i].level;
			postData+=JSON.stringify(jsonObject)+",";
		}
		postData+="]";
		$.ajax({
			url:"<c:url value="/back/menu/saveMenu.do"/>",
			type:"post",
			dataType:"json",
			data:{"myMenus":postData},
			success:function(ret){
				if(ret.result == "y"){
					$(this).alertmsg('ok','菜单更新成功！');
				}else{
					$(this).alertmsg('error','菜单更新失败！');
				}
			}
		});
	};

	function getUUID() {
		function S4() {
			return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
		}
		return (S4()+S4()+"-"+S4()+"-"+S4()+"-"+S4()+"-"+S4()+S4()+S4());
	}
</script>
<div class="bjui-pageContent">
	<div style="padding: 20px;">
		<div class="clearfix">
			<div style="float: left; width: 300px; height: 300px; overflow: auto;">
				<ul id="ztree1" class="ztree" data-toggle="ztree" data-options="{nodes:'initTree',expandAll:false,
                        onClick: 'ZtreeClick',addHoverDom:'edit', removeHoverDom:'edit',maxAddLevel:4}"></ul>
			</div>
			<div id="ztree-detail" style="display: none; margin-left: 320px; width: 300px; height: 240px;">
				<div class="bs-example" data-content="详细信息">
					<input type="hidden"  name="name" id="j_menu_id" />
					<input type="hidden"  name="name" id="j_menu_pId" />
					<div class="form-group">
						<label for="j_menu_name" class="control-label x85">菜单名称：</label> <input type="text" class="form-control" name="name" id="j_menu_name" size="55" placeholder="请输入菜单名字" />
					</div>
					<div class="form-group">
						<label for="j_menu_url" class="control-label x85">URL：</label> <input type="text" class="form-control" name="url" id="j_menu_url" name="url" size="55" placeholder="请输入Url" />
					</div>
					<div class="form-group">
						<label for="j_menu_sort" class="control-label x85">菜单顺序：</label> <input type="text" class="form-control" name="sort" id="j_menu_sort" size="55" placeholder="请输入顺序" />
					</div>
					<div class="form-group">
						<label for="j_menu_description" class="control-label x85">菜单描述：</label>
						<input type="text" class="form-control" name="description" id="j_menu_description" size="55" placeholder="请输入描述" />
					</div>
					<div class="form-group" style="padding-top: 8px; border-top: 1px #DDD solid;">
						<button class="btn btn-green" onclick="M_Ts_Menu();">更新菜单</button>
					</div>
				</div>
			</div>

		</div>
		<hr>
		<div class="btn-group" style="margin-top: 5px;">
			<button type="button" class="btn-blue btn-nm" style="float: left;" onclick="submitTree();" id="submitTree">提交菜单</button>
		</div>
	</div>
</div>