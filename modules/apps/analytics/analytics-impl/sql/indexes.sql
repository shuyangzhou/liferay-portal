create index IX_C02F5433 on Analytics_AnalyticsEvent (companyId, createDate);
create index IX_237E2264 on Analytics_AnalyticsEvent (createDate, className, classPK, referrerClassName, referrerClassPK, type_);
create index IX_C4F5EF74 on Analytics_AnalyticsEvent (createDate, className, classPK, type_);
create index IX_B8A22F4D on Analytics_AnalyticsEvent (createDate, elementKey, type_);
create index IX_8FFC623D on Analytics_AnalyticsEvent (createDate, referrerClassName, referrerClassPK, elementKey, type_);