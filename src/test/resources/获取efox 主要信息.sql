
--SELECT DBO.fn_getTencentShipoutExcelMsg('7CE829P6HG')
ALTER function [dbo].[fn_getTencentShipoutExcelHeaderMsg]
(
	@SN	VARCHAR(100)
)
returns
VARCHAR(MAX)

as

BEGIN

	declare @masterStr	varchar(max);
	DECLARE @ASSETCODE VARCHAR(100)='';
	DECLARE @PRODUCTTYPE VARCHAR(500)='';
	DECLARE @machine VARCHAR(100)='';
	DECLARE @deviceType VARCHAR(100)='';
	DECLARE @ver VARCHAR(100)='';
	DECLARE @PO VARCHAR(100)='';
	declare @dataMaster TABLE (
		po VARCHAR(100),
		accecode	VARCHAR(100),
		SN			VARCHAR(100),
		factoryname	VARCHAR(100),
		machine		VARCHAR(100),
		deviceType	VARCHAR(100),
		ver			VARCHAR(100)
	)
	--INSERT INTO @dataMaster VALUES(
	--'POGO2018***','TYSV1804042E','','FOXCONN','RM760-FX','SC3-10G','6.0.0.11'
	--)
	
	SELECT	@PRODUCTTYPE=PRODUCTTYPE,@ASSETCODE=ASSETCODE
	FROM	TencentAsetcode WHERE SN=@SN
	
	SELECT @machine=VALUE FROM dbo.fn_Split(@PRODUCTTYPE,'_') WHERE SORTNO=1
	SELECT @ver=VALUE FROM dbo.fn_Split(@PRODUCTTYPE,'_') WHERE SORTNO=2
	SELECT @deviceType=Value FROM dbo.fn_Split(@PRODUCTTYPE,'_') WHERE SORTNO=3
	DECLARE @deviceindex  INT;
	SET @deviceindex = CHARINDEX('(',@deviceType)
	SET @deviceindex = CASE WHEN @deviceindex=0 THEN LEN(@deviceType) ELSE @deviceindex END
	SET @deviceType = SUBSTRING(@deviceType,0,@deviceindex)
	
	
	INSERT INTO @dataMaster
	VALUES( @PO,@ASSETCODE,@SN,'FOXCONN',@deviceType,@machine,@ver)

	SELECT @masterStr = '"po":"'+po+'","accecode":"'+accecode+'","factoryname":"'+factoryname+'","machine":"'+machine 
	+'","deviceType":"'+deviceType+'","ver":"'+ver+'","sn":"'+sn+'"'
	FROM @dataMaster
	
	RETURN @masterStr
END