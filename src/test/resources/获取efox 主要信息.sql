
--SELECT DBO.fn_getTencentShipoutExcelHeaderMsg('7CE829P6HG')
ALTER function fn_getTencentShipoutExcelHeaderMsg
(
	@SN	VARCHAR(100)
)
returns
VARCHAR(MAX)

as

BEGIN

	declare @masterStr	varchar(max);
	
	declare @dataMaster TABLE (
		po VARCHAR(100),
		accecode	VARCHAR(100),
		SN			VARCHAR(100),
		factoryname	VARCHAR(100),
		machine		VARCHAR(100),
		deviceType	VARCHAR(100),
		ver			VARCHAR(100)
	)
	INSERT INTO @dataMaster VALUES(
	'POGO2018***','TYSV1804042E','','FOXCONN','RM760-FX','SC3-10G','6.0.0.11'
	)
	
	SELECT @masterStr = '"po":"'+po+'","accecode":"'+accecode+'","factoryname":"'+factoryname+'","machine":"'+machine 
	+'","deviceType":"'+deviceType+'","ver":"'+ver+'"'
	FROM @dataMaster
	
	RETURN @masterStr
END