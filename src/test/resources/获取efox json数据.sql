
--SELECT DBO.fn_getTencentShipoutExcelMsg('7CE829P6HG')
ALTER function fn_getTencentShipoutExcelMsg
(
	@SN	VARCHAR(100)
)
returns
VARCHAR(MAX)

as

BEGIN

	declare @efoxdataStr	VARCHAR(MAX);
	DECLARE @assyData	TABLE(
		SN		VARCHAR(100),
		ctype	VARCHAR(100),
		PN		VARCHAR(100),
		csn		VARCHAR(100),
		descriptions	VARCHAR(300),
		num		INT,
		category	VARCHAR(100)
	)
	--抓取model1的数据
	INSERT INTO @assyData
	SELECT	a.sysserialno SN ,b.CONTROLVALUE ctype,A.partno PN,A.cserialno  csn,CONTROLDESC AS descriptions,
			ROW_NUMBER() over(PARTITION BY A.categoryname order by lasteditdt) as num,A.categoryname
	FROM	mfsyscserial A,ECONTROLVALUE B
	WHERE	categoryname IN ('MG06','MG08','MG28FC','MG28IA','MG28LED','MG28SI','MG28SW','MG29',
			'MG29A','MG29B','MG59','MG65','MG67','MGL006','MG95','MG54') 
			AND sysserialno =@SN 
			AND B.CONTROLNAME LIKE 'tecent_mapping_category_to_name_%'
			AND REPLACE(B.CONTROLNAME,'tecent_mapping_category_to_name_','') =A.categoryname
			--and categoryname <>'MG06'
	
	--抓取通用下		
	INSERT INTO @assyData	
	SELECT A.sysserialno SN ,D.CONTROLVALUE ctype,C.partno PN,C.cserialno  csn,CONTROLDESC AS descriptions,
			ROW_NUMBER() over(PARTITION BY C.categoryname order by C.lasteditdt) as num ,c.categoryname
			FROM mfworkstatus A, mfsyscserial B,mfsyscserial C ,ECONTROLVALUE D
	WHERE A.sysserialno =@SN	
	AND A.sysserialno = B.sysserialno 
	AND B.categoryname='L010'	
	AND B.cserialno  = C.sysserialno
	AND	C.categoryname IN ('MG06','MG08','MG28FC','MG28IA','MG28LED','MG28SI','MG28SW','MG29',
		'MG29A','MG29B','MG59','MG65','MG67','MGL006','MG95','MG54') 
		AND D.CONTROLNAME LIKE 'tecent_mapping_category_to_name_%'
		AND REPLACE(D.CONTROLNAME,'tecent_mapping_category_to_name_','') =C.categoryname

	--抓取cable
	DECLARE @QTY INT
	SELECT @QTY =COUNT(B.sysserialno) FROM mfworkstatus A,mfworkstatus B   
	WHERE A.sysserialno =@SN AND A.workorderno =B.workorderno
	
	INSERT INTO @assyData
	SELECT T2.SN,T2.ctype,T2.PN,T2.csn,T2.descriptions,ROW_NUMBER() over(PARTITION BY T2.categoryname order by t2.pn) as num,T2.categoryname  FROM (
	SELECT	A.sysserialno SN ,D.CONTROLVALUE ctype,C.partno PN,C.partno  csn,CONTROLDESC AS descriptions,c.categoryname
	FROM mfworkstatus A, mfsyscserial B,mfworkdetail C ,ECONTROLVALUE D,mfworkstatus e
	WHERE	A.sysserialno =@SN
			AND A.sysserialno = B.sysserialno 
			AND B.categoryname='L010'	
			AND B.cserialno =E.sysserialno 
			AND E.workorderno  = C.workorderno
			AND	C.categoryname IN ('MG95') 
			AND D.CONTROLNAME LIKE 'tecent_mapping_category_to_name_%'
			AND REPLACE(D.CONTROLNAME,'tecent_mapping_category_to_name_','') =C.categoryname
	
	union
	SELECT	A.sysserialno SN ,D.CONTROLVALUE ctype,C.partno PN,C.partno  csn,CONTROLDESC AS descriptions,c.categoryname
	FROM	mfworkstatus A, mfsyscserial B,mfworkdetail C ,ECONTROLVALUE D,mfworkstatus e
	WHERE	A.sysserialno =@SN
			AND A.sysserialno = B.sysserialno 
			AND B.categoryname='L010'	
			AND B.cserialno =E.sysserialno 
			AND E.workorderno  = C.workorderno
			AND	C.categoryname IN ('MG95') 
			AND D.CONTROLNAME LIKE 'tecent_mapping_category_to_name_%'
			AND REPLACE(D.CONTROLNAME,'tecent_mapping_category_to_name_','') =C.categoryname
			AND C.requestqty/@QTY>1
			
	union	
	SELECT	A.sysserialno SN ,D.CONTROLVALUE ctype,C.partno PN,C.partno  csn,CONTROLDESC AS descriptions,c.categoryname
	FROM	mfworkstatus A, mfsyscserial B,mfworkdetail C ,ECONTROLVALUE D,mfworkstatus e
	WHERE	A.sysserialno =@SN
			AND A.sysserialno = B.sysserialno 
			AND B.categoryname='L010'	
			AND B.cserialno =E.sysserialno 
			AND E.workorderno  = C.workorderno
			AND	C.categoryname IN ('MG95') 
			AND D.CONTROLNAME LIKE 'tecent_mapping_category_to_name_%'
			AND REPLACE(D.CONTROLNAME,'tecent_mapping_category_to_name_','') =C.categoryname
			AND C.requestqty/@QTY>2
			
	union
	SELECT	A.sysserialno SN ,D.CONTROLVALUE ctype,C.partno PN,C.partno  csn,CONTROLDESC AS descriptions,c.categoryname
				FROM mfworkstatus A, mfsyscserial B,mfworkdetail C ,ECONTROLVALUE D,mfworkstatus e
	WHERE	A.sysserialno =@SN
			AND A.sysserialno = B.sysserialno 
			AND B.categoryname='L010'	
			AND B.cserialno =E.sysserialno 
			AND E.workorderno  = C.workorderno
			AND	C.categoryname IN ('MG95') 
			AND D.CONTROLNAME LIKE 'tecent_mapping_category_to_name_%'
			AND REPLACE(D.CONTROLNAME,'tecent_mapping_category_to_name_','') =C.categoryname
			AND C.requestqty/@QTY>3
	) T2		
					
	
	--生成json		
	SET @efoxdataStr=''
	SELECT	@efoxdataStr=@efoxdataStr+(CASE WHEN num =1 THEN '],"'+ctype+'":[{' ELSE ',{' END )
			+'"pn":"'+PN+'"'+','+'"sn":'+'"'+csn+'"'+','+'"type":'+'"'+descriptions+'"'
			+CASE WHEN num =1 THEN '}' ELSE '},' END  
	FROM (
		SELECT * FROM @assyData 
	) T
	order by category,ctype,pn
	IF LEN(@efoxdataStr)>2
	BEGIN
		SET @efoxdataStr = SUBSTRING(@efoxdataStr,3,LEN(@efoxdataStr)-2)+']'
	END
	RETURN @efoxdataStr
END