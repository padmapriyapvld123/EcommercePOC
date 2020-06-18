
CQL="DROP keyspace cycling;

CREATE KEYSPACE cycling
WITH replication = {'class':'SimpleStrategy', 'replication_factor' : 3};

CREATE TABLE cycling."product_supplier" (
	productid varchar,
	description varchar,
	name varchar,
	price double,
	supplierid varchar,
	PRIMARY KEY (productid)
);

CREATE TABLE cycling."supplier" (
	supplierid varchar,
	email varchar,
	name varchar,
	PRIMARY KEY (supplierid)
);


CREATE TYPE cycling."item_entity"
(product_id text,
price double,
quantity int);

CREATE TABLE cycling."order_items"
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
