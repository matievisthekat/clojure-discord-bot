(ns clojure-discord-bot.commands
  (:require [discljord.messaging :as msg]))

(defn create-commands [conn app-id]
  (msg/create-global-application-command!
   conn
   app-id
   "help"
   "View the commands available for this bot"
   {:default-permission :send-messages}))