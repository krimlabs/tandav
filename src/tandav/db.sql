-- :name get-all-trades :? :*
select * from trades

-- :name insert-trade :! :n
insert into trades (buy_cur, sell_cur, buy_units, sell_units, fee_cur, fee_units, tx_time)
values (:buy-cur, :sell-cur, :buy-units, :sell-units, :fee-cur, :fee-units,:tx-time)
