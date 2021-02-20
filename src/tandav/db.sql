-- :name get-all-transactions :? :*
select * from transactions

-- :name insert-transaction :! :n
insert into transactions (buy_cur, sell_cur, buy_units, sell_units, fee_cur, fee_units, tx_time)
values (:buy-cur, :sell-cur, :buy-units, :sell-units, :fee-cur, :fee-units,:tx-time)
