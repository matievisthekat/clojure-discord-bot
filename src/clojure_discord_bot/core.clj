(ns clojure-discord-bot.core
  (:require [clojure.edn :as edn]
            [clojure.core.async :refer [chan close!]]
            [clojure-discord-bot.commands :as commands]
            [discljord.messaging :as rest]
            [discljord.connections :as ws]
            [discljord.formatting :as formatting]
            [discljord.events :as events]))

(def state (atom nil))
(def bot-id (atom nil))
(def config (edn/read-string (slurp "config.edn")))
(def intents #{:guild-messages})

(defmulti handle-event (fn [type _data] type))

(defmethod handle-event :message-create
  [_ {:keys [channel-id author mentions] :as _data}]
  (when (some #{@bot-id} (map :id mentions))
    #_{:clj-kondo/ignore [:unresolved-var]}
    (rest/create-message! (:rest @state) channel-id :content (str "Hello " (formatting/mention-user author) ", use `/help` to view my commands")))
  )

(defmethod handle-event :ready
  [_ _]
  (ws/status-update! (:ws @state) :activity (ws/create-activity :name (:playing config))))

(defmethod handle-event :default [_ _])

(defn start-bot! [token & intents]
  (let [event-channel (chan 100)
        ws-connection (ws/connect-bot! token event-channel :intents (set intents))
        rest-connection (rest/start-connection! token)]
    (reset! state {:events  event-channel
                   :ws      ws-connection
                   :rest    rest-connection})))

(defn stop-bot! [{:keys [rest ws events] :as _state}]
  (rest/stop-connection! rest)
  (ws/disconnect-bot! ws)
  (close! events))

(defn -main [& _]
  (start-bot! (get config :token) intents)
  (reset! bot-id (get (rest/get-current-user! (get @state :rest)) :id))
  ;; (commands/create-commands (get @state :rest) (get config :app-id))
  (try
    (events/message-pump! (:events @state) handle-event)
    (finally (stop-bot! @state))))

