SELECT s.*, ci.AccountCode1, c.ChannelName, e.FirstName, e.LastName, ci.ChannelItemName, c.ChannelCode,
    CASE
        WHEN s.PaymentType = 'Credit' THEN 'เซลล์สลิป'
        WHEN s.PaymentType = 'Cheque' THEN 'เช็ค'
        WHEN s.PaymentType = 'Cash' THEN 'เงินสด'
        ELSE ''
    END AS PaymentTypeName
FROM SendMoney s INNER JOIN ChannelItem ci ON s.ChannelItemID = ci.ChannelItemID
    INNER JOIN Channel c ON ci.ChannelID = c.ChannelID
    INNER JOIN Employee e ON s.CreateBy = e.EmpID
WHERE s.SendMoneyID = ?