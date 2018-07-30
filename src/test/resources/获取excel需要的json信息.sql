ALTER PROCEDURE [dbo].[efoxsfcgetTencentShipoutExcelMsg](
 @ssn VARCHAR(MAX),
 @startTime	DATE,
 @endTime	DATE
)
AS


	
			
		
	
	--EFOX 抓取的信息
	declare @efoxData TABLE(
		sn		VARCHAR(100),
		ctype	VARCHAR(100),
		PN	varchar(100),
		csn		VARCHAR(100)
	)	
	


	IF RTRIM(LTRIM(ISNULL(@ssn,'')))=''
	BEGIN
		SELECT  SSN AS sn,REPLACE(Field9,'{"system":{','{"system":{'+dbo.fn_getTencentShipoutExcelHeaderMsg(SSN)+','+DBO.fn_getTencentShipoutExcelMsg(SSN)+',')as json,'os' AS 'source' 
		FROM (
			SELECT   ROW_NUMBER() OVER (PARTITION BY SSN  ORDER BY lasteditdt DESC) AS RN  ,*
			FROM (SELECT TOP 10 * 
					FROM eFoxSFCUpdateSSNStatusBySSN_tencent_input 
					WHERE Field9 <>'' 
					AND lasteditdt>@startTime AND lasteditdt<@endTime
					ORDER BY lasteditdt DESC
				)F2
		) F1
		WHERE F1.RN=1
	END
	ELSE
	BEGIN
		SELECT  SSN AS sn,REPLACE(Field9,'{"system":{','{"system":{'+dbo.fn_getTencentShipoutExcelHeaderMsg(SSN)+','+DBO.fn_getTencentShipoutExcelMsg(SSN)+',')as json,'os' AS 'source' 
		FROM (
			SELECT   ROW_NUMBER() OVER (PARTITION BY SSN  ORDER BY lasteditdt DESC) AS RN  ,*
			FROM (SELECT TOP 10 * 
					FROM eFoxSFCUpdateSSNStatusBySSN_tencent_input 
					WHERE Field9 <>'' 
					AND SSN IN (SELECT Value FROM dbo.fn_Split(@ssn,','))
					ORDER BY lasteditdt DESC
				)F2
		) F1
		WHERE F1.RN=1
		
	END