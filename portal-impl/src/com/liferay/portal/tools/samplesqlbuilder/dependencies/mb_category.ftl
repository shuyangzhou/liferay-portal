<#setting number_format = "0">

insert into MBCategory values ('${portalUUIDUtil.generate()}', ${mbCategory.categoryId}, ${mbCategory.groupId}, ${mbCategory.companyId}, ${mbCategory.userId}, '', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, '${mbCategory.name}', '${mbCategory.description}', '${mbCategory.displayStyle}', ${mbCategory.threadCount}, ${mbCategory.messageCount}, CURRENT_TIMESTAMP, 0, ${mbCategory.userId}, '', CURRENT_TIMESTAMP);

<#if (mbCategory.categoryId != 0)>
	insert into MBMailingList values ('${portalUUIDUtil.generate()}', ${counter.get()}, ${mbCategory.groupId}, ${companyId}, ${mbCategory.userId}, '', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, ${mbCategory.categoryId}, '', 'pop3', '', 110, FALSE, '', '', 5, '', FALSE, '', 25, FALSE, '', '', FALSE, FALSE);
</#if>