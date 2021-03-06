--exec [efoxsfcgetTencentShipoutExcelMsg] '7CE829P6U7','2018-02-01','2018-08-01'

ALTER PROCEDURE [dbo].[efoxsfcgetTencentShipoutExcelMsg](
 @ssn VARCHAR(MAX),
 @startTime	DATE,
 @endTime	DATE
)
AS
--set @ssn = '7CE829P6DV,7CE830P1N1,7CE829P6UA,7CE830P1MZ'
	declare @tboxSN	TABLE(
		ID	INT IDENTITY(1,1),
		SKUNO	VARCHAR(100),
		SN	VARCHAR(100),
		NUM INT
	)

	--获取装箱出货的SN
	IF RTRIM(LTRIM(ISNULL(@ssn,'')))=''
	BEGIN
	
		INSERT INTO @tboxSN
		 SELECT  F1.skuno ,F1.SN,ROW_NUMBER() OVER (PARTITION BY skuno  ORDER BY  SCANTIME DESC) AS RN  
		 FROM(
				SELECT TOP 1000 A.skuno ,A.sysserialno  AS SN,MAX(C.scandatetime) AS SCANTIME
				FROM  mfsysproduct A, mfworkstatus B ,mfsysevent c
				WHERE a.field1<>''
					  AND A.sysserialno = B.sysserialno 
					  AND B.routeid LIKE 'LXTe%'
					  AND a.sysserialno =c.sysserialno
					  AND C.eventname='NEW T-BOX'
					  AND C.scandatetime>@startTime AND C.scandatetime<@endTime
					  GROUP BY  A.skuno ,A.sysserialno   
		) F1

	END
	ELSE
	BEGIN
		INSERT INTO @tboxSN
		SELECT  F1.skuno ,F1.SN,ROW_NUMBER() OVER (PARTITION BY skuno  ORDER BY  lasteditdt DESC) AS RN  
				FROM(
			SELECT  A.skuno ,A.sysserialno  AS SN,A.lasteditdt
			FROM  mfsysproduct A, mfworkstatus B
			WHERE a.field1<>''
				  AND A.sysserialno = B.sysserialno 
				  AND B.routeid LIKE 'LXTe%'
				  AND A.sysserialno IN (SELECT Value FROM dbo.fn_Split(@ssn,','))
			) F1
	
	END
	
		
	declare @snJsonData TABLE(
		sn		VARCHAR(100),
		json	NVARCHAR(MAX),
		SOURCE	varchar(100)
	)		
		
	
	--EFOX 抓取的信息
	declare @efoxData TABLE(
		sn		VARCHAR(100),
		ctype	VARCHAR(100),
		PN	varchar(100),
		csn		VARCHAR(100)
	)	
	

    --获取每一个SN对应的 属性信息
	INSERT INTO @snJsonData
	SELECT  SSN AS sn,REPLACE(Field9,'{"system":{','{"system":{'+dbo.fn_getTencentShipoutExcelHeaderMsg(SSN)+','+DBO.fn_getTencentShipoutExcelMsg(SSN)+',')as json,'os' AS 'source' 
	FROM (
		SELECT   ROW_NUMBER() OVER (PARTITION BY SSN  ORDER BY lasteditdt DESC) AS RN  ,*
		FROM (SELECT top 5000 * 
				FROM eFoxSFCUpdateSSNStatusBySSN_tencent_input 
				WHERE SSN IN (SELECT sn FROM @tboxSN)
					  AND Field9<>''
				ORDER BY lasteditdt DESC
			)F2
	) F1
	WHERE F1.RN=1
	
	
	--UPDATE @tboxSN SET NUM=1 ,SKUNO='1A21T9100-600-GX1' WHERE ID IN (3)
	--UPDATE @tboxSN SET NUM=2 ,SKUNO='1A21T9100-600-GX1' WHERE ID IN (4)
	--select *from @tboxSN
	--select sn,SUBSTRING(json,LEN('{"system":')+1,len(json)-LEN('{"system":')-1) from @snJsonData
	update @snJsonData set json = SUBSTRING(json,LEN('{"system":')+1,len(json)-LEN('{"system":')-1)
	
	declare @jsonDataStr NVARCHAR(MAX)
	SET @jsonDataStr =''
	
	
	--SELECT	(CASE WHEN NUM =1 THEN ']},{"'+SKUNO+'":['+'JSON' ELSE ','+'JSON' END) 
	--FROM	@tboxSN A,@snJsonData B
	--WHERE	A.SN=B.sn
	--ORDER BY ID
	
	--分料号进行分组，拼接成json数据
	SELECT	@jsonDataStr =@jsonDataStr+(CASE WHEN NUM =1 THEN ']},{"skuno":"'+SKUNO+'","system":['+json ELSE ','+json END) 
	FROM	@tboxSN A,@snJsonData B
	WHERE	A.SN=B.sn
	ORDER BY ID
	
	IF LEN(@jsonDataStr)>2
	BEGIN
		SET @jsonDataStr = '['+SUBSTRING(@jsonDataStr,4,LEN(@jsonDataStr)-3)+']}]'
	END
	
	SELECT @jsonDataStr as json
