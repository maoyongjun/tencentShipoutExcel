ALTER PROCEDURE [dbo].[efoxsfcgetTencentShipoutExcelMsg](
 @ssn VARCHAR(MAX),
 @startTime	DATE,
 @endTime	DATE
)
AS


	
			
		
	
	--EFOX §ì¨úªº«H®§
	declare @efoxData TABLE(
		sn		VARCHAR(100),
		ctype	VARCHAR(100),
		PN	varchar(100),
		csn		VARCHAR(100)
	)	
	


	IF RTRIM(LTRIM(ISNULL(@ssn,'')))=''
	BEGIN
		SELECT TOP 10 SSN AS sn,REPLACE(Field9,'{"system":{','{"system":{'+dbo.fn_getTencentShipoutExcelHeaderMsg(SSN)+','+DBO.fn_getTencentShipoutExcelMsg(SSN)+',')as json,'os' AS 'source'
		FROM eFoxSFCUpdateSSNStatusBySSN_tencent_input 
		WHERE Field9 <>'' 
		AND lasteditdt>@startTime AND lasteditdt<@endTime
		ORDER BY lasteditdt DESC
	END
	ELSE
	BEGIN
		SELECT TOP 10 SSN AS sn,REPLACE(Field9,'{"system":{','{"system":{'+dbo.fn_getTencentShipoutExcelHeaderMsg(SSN)+','+DBO.fn_getTencentShipoutExcelMsg(SSN)+',')as json,'os' AS 'source'
		FROM eFoxSFCUpdateSSNStatusBySSN_tencent_input 
		WHERE Field9 <>'' 
		AND lasteditdt>@startTime AND lasteditdt<@endTime
		AND SSN IN (SELECT Value FROM dbo.fn_Split(@ssn,','))
		ORDER BY lasteditdt DESC
	END