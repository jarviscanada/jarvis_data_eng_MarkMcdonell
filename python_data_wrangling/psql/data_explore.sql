

SELECT '1' as one;
select * from retail limit 100;

SELECT '2' as two;
--select count(invoice_no) from retail;

SELECT '3' as three;
--select count(distinct customer_id) from retail;

SELECT '4' as four;
--select min(invoice_date), max(invoice_date) from retail;

SELECT '5' as five;
--select count(distinct stock_code) from retail;


SELECT '6' as six;
--avg of invoice total
SELECT AVG(totals) as average_revenue FROM(
SELECT SUM(unit_price*quantity) as totals
FROM retail 
GROUP BY invoice_no
HAVING SUM(unit_price*quantity) > 0) AS derivedTable;

SELECT '7' as seven;
-- total revenue
select sum(unit_price*quantity) from retail;

SELECT '8' as eight;
-- change date to YYYYMM, get total revenue by month
SELECT to_char(invoice_date, 'YYYYMM') AS yyyymm, 
sum(unit_price*quantity) 
FROM retail
group by to_char(invoice_date, 'YYYYMM')
order by to_char(invoice_date, 'YYYYMM');

SELECT invoice_no, invoice_date FROM retail ;




