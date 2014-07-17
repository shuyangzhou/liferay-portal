create table Analytics_AnalyticsEvent (
	analyticsEventId LONG not null primary key,
	companyId LONG,
	userId LONG,
	createDate DATE null,
	anonymousUserId LONG,
	className VARCHAR(75) null,
	classPK LONG,
	referrerClassName VARCHAR(75) null,
	referrerClassPK LONG,
	elementKey VARCHAR(75) null,
	type_ VARCHAR(75) null,
	clientIP VARCHAR(75) null,
	userAgent VARCHAR(75) null,
	languageId VARCHAR(75) null,
	url VARCHAR(75) null,
	additionalInfo VARCHAR(75) null
);