create table AssetCategoryProperty (
	mvccVersion LONG default 0 not null,
	ctCollectionId LONG default 0 not null,
	categoryPropertyId LONG not null,
	companyId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	categoryId LONG,
	key_ VARCHAR(75) null,
	value VARCHAR(75) null,
	primary key (categoryPropertyId, ctCollectionId)
);