SELECT e.EmpID, e.OrganizationCode, te.PositionID, e.FirstName, e.LastName, e.LastName, e.SaleManType, e.CreateDate, e.CreateBy, 
	e.LastUpdateDate, e.LastUpdateBy, e.SyncedDate, te.TeamCode, te.SubTeamCode, t.Name AS TeamName, p.PositionName, 
	st.SaleTypeName 
FROM Employee AS e INNER JOIN TeamEmployee AS te ON e.EmpID = te.EmpID 
	INNER JOIN Team AS t ON te.TeamCode = t.Code 
	INNER JOIN Position AS p ON te.PositionID = p.PositionID 
	INNER JOIN SaleType AS st ON st.SaleTypeCode = e.SaleManType 
WHERE e.EmpID = ?