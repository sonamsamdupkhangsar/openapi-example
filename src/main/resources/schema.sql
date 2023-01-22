CREATE TABLE if not exists Invoice (id UUID PRIMARY KEY, purchase_order_id UUID, subtotal decimal,
tax decimal, grand_total decimal);
create table if not exists Invoice_Detail(id uuid primary key, invoice_id uuid, description varchar,
quantity int, unit_price decimal, amount decimal);