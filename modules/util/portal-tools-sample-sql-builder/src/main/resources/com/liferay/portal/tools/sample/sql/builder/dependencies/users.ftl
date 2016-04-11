<#assign groupIds = dataFactory.getNewUserGroupIds(groupModel.groupId)>
<#assign roleIds = dataFactory.getUserRoleIds()>

<#assign userModels = dataFactory.newUserModels()>

<#list userModels as userModel>
	<#assign userGroupModel = dataFactory.newGroupModel(userModel)>

	<#assign layoutModel = dataFactory.newLayoutModel(userGroupModel.groupId, "home", "", "")>

	<@insertLayout
		_layoutModel = layoutModel
	/>

	<@insertGroup
		_groupModel = userGroupModel
		_publicPageCount = 1
	/>

	<@insertUser
		_groupIds = groupIds
		_roleIds = roleIds
		_userModel = userModel
	/>
</#list>