-- CREATE UUID FUNCTIONS --

DELIMITER //

CREATE FUNCTION `BIN_TO_UUID`(b BINARY(16)) RETURNS char(36)
BEGIN
  DECLARE hexStr CHAR(32);
  SET hexStr = HEX(b);
  RETURN LOWER( CONCAT(
    SUBSTRING(hexStr, 9, 8), '-',
    SUBSTRING(hexStr, 5, 4), '-',
    SUBSTRING(hexStr, 13, 4), '-',
    SUBSTRING(hexStr, 17, 4), '-',
    SUBSTRING(hexStr, 21)
   ));
END//

CREATE FUNCTION `UUID_TO_BIN`(uuid CHAR(36)) RETURNS binary(16)
BEGIN
  RETURN UNHEX( CONCAT( 
    SUBSTRING(uuid, 15, 4),
    SUBSTRING(uuid, 10, 4),
    SUBSTRING(uuid,  1, 8),
    SUBSTRING(uuid, 20, 4),
    SUBSTRING(uuid, 25)
  ));
END//

DELIMITER ;