(ns tasty.core
  (:use
    [tentacles.events :only [events]]
    [twitter.oauth]
    [twitter.callbacks]
    [twitter.callbacks.handlers]
    [twitter.api.restful])
  (:import (twitter.callbacks.protocols SyncSingleCallback))
  (:gen-class))

(def ^:dynamic *creds* (make-oauth-creds "your-app-consumer-key" "your-app-consumer-secret" "your-user-access-token" "your-user-access-token-secret"))

(defn push-event?
  "Repository push event?"
  [event]
  (= "PushEvent" (event :type)))

(defn cookbook-push-event?
  "Chef cookbook push event?"
  [event]
  (and (push-event? event) (re-find #"chef|cookbook" (:name (event :repo)))))

(defn event-description
  "String representation of the provided event"
  [event]
  (format "%s: %s %s"
    (:name (event :repo)) (:url (event :repo))
    (:message (first (:commits (event :payload))))))

(defn truncate
  "Truncate to a specific length"
  [s l]
  (cond (> (count s) l) (subs s 0 l)
    :else s))

(defn tweet
  "Tweet the provided message (may be truncated)"
  [message]
  (update-status :oauth-creds *creds* :params {:status (truncate message 140)}))

(defn tweet-cookbooks
  "Tweet new cookbooks"
  []
  (dorun
    (map tweet (map event-description (filter cookbook-push-event? (events))))))

(defn -main
  []
  (while true (do (tweet-cookbooks) (Thread/sleep 30000))))
