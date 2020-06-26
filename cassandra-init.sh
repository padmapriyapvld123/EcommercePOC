
CQL="DROP keyspace cycling1;

CREATE KEYSPACE cycling1
WITH replication = {'class':'SimpleStrategy', 'replication_factor' : 3};

DELETE FROM system_schema.tables WHERE keyspace_name = 'cycling' AND table_name = 'order_items'; 
DELETE FROM system_schema.columns WHERE keyspace_name = 'cycling' AND table_name = 'order_items'; 



CREATE TABLE cycling1."product_supplier" (
	productid varchar,
	description varchar,
	name varchar,
	price double,
	supplierid varchar,
	PRIMARY KEY (productid)
);



CREATE TABLE cycling1."supplier" (
	supplierid varchar,
	email varchar,
	name varchar,
	PRIMARY KEY (supplierid)
);


CREATE TYPE cycling1."item_entity"
(product_id text,
price double,
quantity int);


CREATE TABLE cycling1."order_items"
(id text,
date text,
customer_name text,
customer_email text,
customer_address text,
total double,
item_entity list<frozen<item_entity>>,
PRIMARY KEY (id,customer_email));"

until echo $CQL | cqlsh; do
  echo "cqlsh: Cassandra is unavailable to initialize - will retry later"
  sleep 2
done &

exec /docker-entrypoint.sh "$@"
