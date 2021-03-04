-- Transactions
-- ------------

-- :name get-all-transactions :? :*
select * from transactions

-- :name insert-transaction :! :n
insert into transactions (buy_cur, sell_cur, buy_units, sell_units, fee_cur, fee_units, tx_time)
values (:buy-cur, :sell-cur, :buy-units, :sell-units, :fee-cur, :fee-units,:tx-time)


-- Accounts
-- --------

-- :name get-all-accounts :? :*
select * from accounts

-- :name insert-account :! :n
insert into accounts (name, address, api_key, secret_key) values (:name, :address, :api-key, :secret-key)

-- :name delete-account-by-id :! :n
delete from accounts where id = :id
