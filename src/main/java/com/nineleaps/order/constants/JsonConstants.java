package com.nineleaps.order.constants;

public class JsonConstants {
	
	public static final String mockOrderJson =   "{   \"id\":\"6\",\r\n\t\t\t \"date\":\"2020-06-16\",\r\n\t\t\t \"customerName\":\"Padma\",\r\n\t\t\t \"customerEmail\":\"padmapriyapvld@gmail.com\",\r\n\t\t\t \"customerAddress\":\"Bangalore\",\r\n\t\t\t\"item\":[{\r\n\t            \"productId\" :\"P003\",\r\n\t\t        \"quantity\" : \"1\",\r\n\t\t        \"price\": \"100.0\"\r\n\t\t\t\t},{\r\n\t\t\t\t\"productId\" :\"P003\",\r\n\t\t        \"quantity\" : \"2\",\r\n\t\t        \"price\": \"200.0\"\r\n\t\t\t\t\r\n\t\t\t\t\r\n\t\t\t\t}],\r\n\t\t\t\"total\":\"300\"\r\n\t} ";    
	public static final String mockSupplierJson = "{\"email\": \"testpoc123@gmail.com\",\r\n  \"name\": \"Black Table\",\r\n  \"supplierId\": \"S003\"\r\n}";
	public static final String mockProductJson =" {\"description\":\"Black Table\",\r\n  \"name\":\"Table\",\r\n  \"price\":\"100.0\",\r\n  \"productId\":\"P003\",\r\n  \"supplierId\":\"S003\"\r\n}\r\n";
		

}
