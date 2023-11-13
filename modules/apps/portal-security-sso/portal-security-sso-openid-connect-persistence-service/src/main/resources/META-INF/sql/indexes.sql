create index IX_396C5BCB on OpenIdConnectSession (accessTokenExpirationDate);
create index IX_5D03D521 on OpenIdConnectSession (authServerWellKnownURI[$COLUMN_LENGTH:256$], clientId[$COLUMN_LENGTH:256$], companyId);
create unique index IX_60980B41 on OpenIdConnectSession (authServerWellKnownURI[$COLUMN_LENGTH:256$], clientId[$COLUMN_LENGTH:256$], userId);