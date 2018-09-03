--SELECT DBO.fn_getTencentShipoutExcelMsg('7CE829P6HG')ALTER function [dbo].[fn_getTencentShipoutExcelMsg](	@SN	VARCHAR(100))returnsNVARCHAR(MAX)asBEGIN	declare @efoxdataStr	NVARCHAR(MAX);	DECLARE @assyData	TABLE(		SN		VARCHAR(100),		ctype	VARCHAR(100),		PN		VARCHAR(100),		csn		VARCHAR(100),		descriptions	NVARCHAR(300),		num		INT,		category	VARCHAR(100)	)	--抓取model0的数据	INSERT INTO @assyData	SELECT	a.sysserialno SN ,b.CONTROLVALUE ctype,A.partno PN,A.cserialno  csn,CONTROLDESC AS descriptions,			ROW_NUMBER() over(PARTITION BY b.CONTROLVALUE order by b.CONTROLDESC,CASE WHEN b.CONTROLDESC =N'硬盘' THEN A.cserialno  ELSE right(A.cserialno,8) END) as num,A.categoryname	FROM	mfsyscserial A,ECONTROLVALUE B	WHERE	categoryname IN ('MG06','MG28FC','MG28IA','MG28LED','MG28SW',			'MG29A','MG29B','MG59','MG67','MGL006','MG95','MG65','MG54','MG09','MG14','MG42','MG15','MG19') 			AND sysserialno =@SN 			AND B.CONTROLNAME LIKE 'tecent_mapping_category_to_name_%'			AND REPLACE(B.CONTROLNAME,'tecent_mapping_category_to_name_','') =A.categoryname			--and categoryname <>'MG06'		--抓取model1的数据	INSERT INTO @assyData		SELECT 	A.sysserialno SN ,D.CONTROLVALUE ctype,C.partno PN,C.cserialno  csn,CONTROLDESC AS descriptions,			ROW_NUMBER() over(PARTITION BY D.CONTROLVALUE order by d.CONTROLDESC,CASE WHEN D.CONTROLDESC =N'硬盘' THEN C.cserialno  ELSE right(C.cserialno,8) END) as num ,c.categoryname	FROM 	mfworkstatus A, mfsyscserial B,mfsyscserial C ,ECONTROLVALUE D	WHERE 	A.sysserialno =@SN				AND A.sysserialno = B.sysserialno 			AND B.categoryname='L010'				AND B.cserialno  = C.sysserialno			AND	C.categoryname IN ('MG06','MG28FC','MG28IA','MG28LED','MG28SW',				'MG29A','MG29B','MG59','MG67','MGL006','MG95','MG65','MG54','MG09','MG14','MG42','MG15','MG19') 			AND D.CONTROLNAME LIKE 'tecent_mapping_category_to_name_%'			AND REPLACE(D.CONTROLNAME,'tecent_mapping_category_to_name_','') =C.categoryname	--抓取cable 通过料号分组，根据用量比例生成。因为没有组装，所以只能根据物料信息生成	DECLARE @QTY INT	SELECT @QTY =COUNT(B.sysserialno) FROM mfworkstatus A,mfworkstatus B   	WHERE A.sysserialno =@SN AND A.workorderno =B.workorderno		INSERT INTO @assyData	SELECT T2.SN,T2.ctype,T2.PN,T2.csn,T2.descriptions,ROW_NUMBER() over(PARTITION BY T2.categoryname order by t2.ctype,right(CSN,8)) as num,T2.categoryname  FROM (	SELECT	A.sysserialno SN ,D.CONTROLVALUE ctype,C.partno PN,''  csn,CONTROLDESC AS descriptions,c.categoryname	FROM mfworkstatus A, mfsyscserial B,mfworkdetail C ,ECONTROLVALUE D,mfworkstatus e	WHERE	A.sysserialno =@SN			AND A.sysserialno = B.sysserialno 			AND B.categoryname='L010'				AND B.cserialno =E.sysserialno 			AND E.workorderno  = C.workorderno			AND	C.categoryname IN ('MG95') 			AND D.CONTROLNAME LIKE 'tecent_mapping_category_to_name_%'			AND REPLACE(D.CONTROLNAME,'tecent_mapping_category_to_name_','') =C.categoryname		union  all	SELECT	A.sysserialno SN ,D.CONTROLVALUE ctype,C.partno PN,''  csn,CONTROLDESC AS descriptions,c.categoryname	FROM	mfworkstatus A, mfsyscserial B,mfworkdetail C ,ECONTROLVALUE D,mfworkstatus e	WHERE	A.sysserialno =@SN			AND A.sysserialno = B.sysserialno 			AND B.categoryname='L010'				AND B.cserialno =E.sysserialno 			AND E.workorderno  = C.workorderno			AND	C.categoryname IN ('MG95') 			AND D.CONTROLNAME LIKE 'tecent_mapping_category_to_name_%'			AND REPLACE(D.CONTROLNAME,'tecent_mapping_category_to_name_','') =C.categoryname			AND CONVERT(INT,c.originalqty)>1				union  all		SELECT	A.sysserialno SN ,D.CONTROLVALUE ctype,C.partno PN,''  csn,CONTROLDESC AS descriptions,c.categoryname	FROM	mfworkstatus A, mfsyscserial B,mfworkdetail C ,ECONTROLVALUE D,mfworkstatus e	WHERE	A.sysserialno =@SN			AND A.sysserialno = B.sysserialno 			AND B.categoryname='L010'				AND B.cserialno =E.sysserialno 			AND E.workorderno  = C.workorderno			AND	C.categoryname IN ('MG95') 			AND D.CONTROLNAME LIKE 'tecent_mapping_category_to_name_%'			AND REPLACE(D.CONTROLNAME,'tecent_mapping_category_to_name_','') =C.categoryname			AND CONVERT(INT,c.originalqty)>2				union all	SELECT	A.sysserialno SN ,D.CONTROLVALUE ctype,C.partno PN,''  csn,CONTROLDESC AS descriptions,c.categoryname				FROM mfworkstatus A, mfsyscserial B,mfworkdetail C ,ECONTROLVALUE D,mfworkstatus e	WHERE	A.sysserialno =@SN			AND A.sysserialno = B.sysserialno 			AND B.categoryname='L010'				AND B.cserialno =E.sysserialno 			AND E.workorderno  = C.workorderno			AND	C.categoryname IN ('MG95') 			AND D.CONTROLNAME LIKE 'tecent_mapping_category_to_name_%'			AND REPLACE(D.CONTROLNAME,'tecent_mapping_category_to_name_','') =C.categoryname			AND CONVERT(INT,c.originalqty)>3	) T2									--生成json			SET @efoxdataStr=''	SELECT	@efoxdataStr=@efoxdataStr+(CASE WHEN num =1 THEN '],"'+ctype+'":[{' ELSE ',{' END )			+'"pn":"'+PN+'"'+','+'"sn":'+'"'+csn+'"'+','+'"type":'+'"'+descriptions+'"'+','+'"efoxpn":"'+PN+'"'+','+'"efoxsn":'+'"'+csn+'"'			+CASE WHEN num =1 THEN '}' ELSE '}' END  	FROM (		SELECT * FROM @assyData 	) T	--order by ctype,category,pn	IF LEN(@efoxdataStr)>2	BEGIN		SET @efoxdataStr = SUBSTRING(@efoxdataStr,3,LEN(@efoxdataStr)-2)+']'	END	RETURN @efoxdataStrEND